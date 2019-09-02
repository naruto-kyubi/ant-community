package org.naruto.framework;

import lombok.extern.slf4j.Slf4j;
import org.naruto.framework.core.repository.CustomRepositoryFactoryBean;
import org.naruto.framework.sync.full.FullSyncService;
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
@Slf4j
public class FullSyncApplication implements CommandLineRunner {


    @Autowired
    private FullSyncService fullSyncService;

    public static void main(String[] args) throws Exception{
        new SpringApplicationBuilder(FullSyncApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("begin sync article data....");
        fullSyncService.exportArticle2ES();
        log.info("sync article data ok.");

        log.info("begin sync user data....");
        fullSyncService.exportUser2ES();
        log.info("sync user data ok.");

        System.exit(0);
    }
}
