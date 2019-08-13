package com.lv.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestClient builder = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();

        return new RestHighLevelClient(builder);
    }
}
