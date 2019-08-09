package com.lv.demo.controller;

import com.lv.demo.dao.UserEsDao;
import com.lv.demo.entity.EsUser;
import com.lv.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserEsController {

    @Autowired
    private UserEsDao userEsDao;

    /**
     * 跳转到搜索首页
     *
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView goToIndex() {
        return new ModelAndView("list");
    }


    /**
     * 根据Id查询ES数据
     * @return
     */
    @GetMapping("/get/{id}")
    public Optional<EsUser> getUserByName(@PathVariable String id) {
        return userEsDao.findById(Integer.parseInt(id));
    }

    /**
     * 查询出ES中的所有数据
     */
    @GetMapping("/getAllUser")
    public Iterable<EsUser> getAll() {
        return userEsDao.findAll();
    }

    /**
     * 根据实体类属性在ES查询数据
     */
    @GetMapping("/find/{key}")
    public List<EsUser> userSearch(@PathVariable String key) throws Exception {
        if (StringUtils.isBlank(key)) {
            throw new Exception("输入的数据不能为空");
        }
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(key);
        Iterable<EsUser> result = userEsDao.search(builder);
        Iterator<EsUser> iterator = result.iterator();
        List<EsUser> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * 向Es中增加数据， 不同步到mysql
     */
    @PostMapping("/esInsert")
    public EsUser insertEsUser(EsUser esUser) {
        return userEsDao.save(esUser);
    }

    /**
     * 根据ID删除数据
     */
    @DeleteMapping("/esDelete/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userEsDao.deleteById(id);
    }

    /**
     * 分页查询数据
     */
    @GetMapping("/{page}/{size}/{key}")
    public List<EsUser> searchByPage(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String key) throws Exception {
        //构建一个list,返回数据
        List<EsUser> list = new ArrayList<>();
        //构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.termQuery("name", key));
        //分页
        queryBuilder.withPageable(PageRequest.of(page, size));

        //搜索， 获取结果
        Page<EsUser> users = userEsDao.search(queryBuilder.build());
        //总条数
        log.info("数据的总条数：{}" , users.getTotalElements());
        //总页数
        log.info("总页数：{}" , users.getTotalPages());
        //当前页
        log.info("当前页：{}" , users.getNumber());
        //每页大小
        log.info("每页大小：{}" , users.getSize());

        if (users.equals("") || users == null) {
            throw new Exception("分页查询： 数据为空");
        }
        for (EsUser esUser : users) {
            list.add(esUser);
        }
        log.info("list数据详情：{}" , list);
        return list;
    }

    @GetMapping("/bucket")
    public void bucketQuery() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //不查询任何结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        //添加一个新的聚合，聚合类型为terms,聚合名称为ages，聚合字段为age
        queryBuilder.addAggregation(AggregationBuilders.terms("names").field("name.keyword"));
        //查询，需要把结果强转为AggregatePage类型
        AggregatedPage<EsUser> page = (AggregatedPage<EsUser>) userEsDao.search(queryBuilder.build());
        //解析、
        StringTerms agg = (StringTerms) page.getAggregation("names");
        //获取桶
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        //遍历
        for (StringTerms.Bucket bucket : buckets) {
            //获取桶中的key， 即名称
            log.info("名称: {}", bucket.getKeyAsString());
            //获取桶中的文档数量
            log.info("数量: {}", bucket.getDocCount());
        }
    }
}
