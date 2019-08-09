package com.lv.demo.dao;

import com.lv.demo.entity.EsUser;
import com.lv.demo.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserEsDao extends ElasticsearchRepository<EsUser, Integer> {

}
