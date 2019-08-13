package com.lv.demo.controller;

import com.lv.demo.config.RestClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.MetaDataIndexTemplateService;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/rest")
public class EsRestController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 去往首页
     *
     */
    @RequestMapping("/index")
    public ModelAndView goToIndex() {
        return new ModelAndView("list");
    }

    /**
     * 查询数据
     *
     */
    @GetMapping("/getdata/{id}")
    public Map getData(@PathVariable String id) throws IOException {
        GetRequest getRequest = new GetRequest("mysql01", "jdbc", id);
        GetResponse response = restHighLevelClient.get(getRequest);
        log.info("response: {}", response.toString());
        Map<String, Object> map = response.getSource();
        log.info("获取到的数据为: {}", map);
        return map;
    }

    /**
     * 更新数据
     */
    @PutMapping("/update/{id}/{name}")
    public void updateName(@PathVariable String id, @PathVariable  String name) throws IOException {
        UpdateRequest request = new UpdateRequest("mysql01", "jdbc", id).doc("name", name);
        UpdateResponse update = restHighLevelClient.update(request);
    }

    /**
     *  查询所有的数据
     */
    @GetMapping("/list")
    public List getAll() throws IOException {
        List list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termsQuery("_type", "jdbc"));
        builder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));
        builder.from(0);
        builder.size(100);
        searchRequest.source(builder);
        SearchResponse response = restHighLevelClient.search(searchRequest);
        Iterator iterator =  response.getHits().iterator();
        while (iterator.hasNext()) {
  //          log.info("数据为： {}", iterator.next().toString());
            list.add(iterator.next());
        }
        log.info("获取到的数据为: {}", list.size());
        return list;
    }

    /**
     * 查询需要的数据
     */
    @GetMapping("/find/{key}")
    public List getDataByKey(@PathVariable String key) throws IOException {
        List list = new ArrayList();
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        log.info("key: {}", key);
        //AND, OR 连接查询
//        builder.query(QueryBuilders.boolQuery()
//                .should(QueryBuilders.termsQuery("age", key))
//                    .should(QueryBuilders.termsQuery("name", key))
//                        .should(QueryBuilders.termsQuery("id", key)));

        //模糊查询
        builder.query(QueryBuilders.wildcardQuery("name", "*" + key + "*"));

        HighlightBuilder hBuilder = new HighlightBuilder();
        hBuilder.preTags("<h2>");
        hBuilder.postTags("</h2>");
        hBuilder.field("name");

        request.source(builder);
        SearchResponse response = restHighLevelClient.search(request);
        Iterator iterator = response.getHits().iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        log.info("获取到的数据为：{}", list);
        return list;
    }

    /**
     * 查询需要的数据并高亮显示
     */
    @GetMapping("/finds/{key}")
    public List getDatasByKey(@PathVariable String key) throws IOException {
        List list = new ArrayList();
        List value = new ArrayList();
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        log.info("key: {}", key);

        HighlightBuilder hBuilder = new HighlightBuilder();
        hBuilder.preTags("<span style=\"color:red;font-weight:bold;\">");
        hBuilder.postTags("</span>");
        hBuilder.field("name");

        builder.query(QueryBuilders.matchQuery("name", key))
                .highlighter(hBuilder);
        request.source(builder);
        SearchResponse response = restHighLevelClient.search(request);
        SearchHits searchHits = response.getHits();

        System.out.println("共搜到：" + searchHits.getTotalHits() + "条结果");

        //遍历结果
        for (SearchHit hit : searchHits) {
            log.info("String方式打印文档搜索内容:{}", hit.getSourceAsString());

            log.info("Map方式打印高亮内容: {}" + hit.getSource());

            System.out.println("遍历集合，打印高亮片段：");

            String high = "";
            Text[] text = hit.getHighlightFields().get("name").getFragments();

            for (Text str : text) {
                high += str;
                log.info(str.string());
            }
            Map map = hit.getSourceAsMap();
            map.put("name", high);
            list.add(map);
        }

        log.info("获取到的数据为：{}", list);
        return list;
    }
}
