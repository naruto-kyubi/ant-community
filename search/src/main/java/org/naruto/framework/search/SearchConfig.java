package org.naruto.framework.search;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.naruto.framework")
public class SearchConfig {

    @Value("${elasticsearch.host}")
    private String host;// master node

    @Value("${elasticsearch.port}")
    private int port;// master node

    @Value("${elasticsearch.cluster.name}")
    private String esClusterName;

    @Bean
    public TransportClient client() throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name", esClusterName)
                .build();


        TransportClient client  = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));

//        TransportClient transportClient = TransportClient.builder()
//                .settings(settings)
//                .build()
//                .addTransportAddressddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
//        if (ElasticSearchUtils.isNotEmpty(esHost2)) {
//            transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost2), esPort));
//        }
//        if (ElasticSearchUtils.isNotEmpty(esHost3)) {
//            transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost3), esPort));
//        }
        return client;
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }

}
