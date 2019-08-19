package org.naruto.framework;

import org.modelmapper.ModelMapper;
import org.naruto.framework.core.repository.CustomRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
@ComponentScan(excludeFilters={@ComponentScan.Filter(type= FilterType.ANNOTATION,classes={SpringBootApplication.class})})
public class FrameworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrameworkApplication.class, args);
	}

	@Value("${uploadfile.tmp.location}")
	private String uploadfilelocation;

	//设置限制文件大小。
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//  单个数据大小
		factory.setMaxFileSize("10240KB"); // KB,MB
		/// 总上传数据大小
		factory.setMaxRequestSize("102400KB");

		//文件上传临时目录
		factory.setLocation(uploadfilelocation);
		return factory.createMultipartConfig();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
