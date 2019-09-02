package org.naruto.framework;

import org.naruto.framework.core.repository.CustomRepositoryFactoryBean;
import org.naruto.framework.sync.Mysql2ESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
@ComponentScan(excludeFilters={
        @ComponentScan.Filter(type= FilterType.ANNOTATION,classes={SpringBootApplication.class, Controller.class}),
})
public class IncrementSyncApplication implements CommandLineRunner {

    @Autowired
    private Mysql2ESService mysql2ESService;

    public static void main(String[] args) throws Exception{
        new SpringApplicationBuilder(IncrementSyncApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        mysql2ESService.sync();
    }
}
