package com.example.easy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@SpringBootApplication

@EnableJpaRepositories(basePackages = "com.example.easy")
@ComponentScan("com.example.easy")
@Configuration
@PropertySources({ @PropertySource("classpath:appConfig.properties"), @PropertySource("classpath:swagger.properties") })
public class EasyInventoryApplication {

	private final static Logger logger = LoggerFactory.getLogger(EasyInventoryApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Bms Rest Services... " + args);
		// System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(EasyInventoryApplication.class, args);

	}

	/*
	 * @Bean public FreeMarkerConfigurationFactoryBean
	 * getFreeMarkerBeanConfiguration() { FreeMarkerConfigurationFactoryBean bean =
	 * new FreeMarkerConfigurationFactoryBean();
	 * bean.setTemplateLoaderPath("/fmtemplates/"); return bean; }
	 */

	@Bean
	public Module datatypeHibernateModule() {
		return new Hibernate5Module();
	}
}
