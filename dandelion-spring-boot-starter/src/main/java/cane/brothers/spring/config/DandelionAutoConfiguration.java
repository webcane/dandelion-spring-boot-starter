package cane.brothers.spring.config;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.github.dandelion.core.web.DandelionFilter;
import com.github.dandelion.core.web.DandelionServlet;
import com.github.dandelion.thymeleaf.dialect.DandelionDialect;

/**
 * Dandelion spring boot auto configuration class.
 * 
 * @author Mikhail Niedre
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(SpringTemplateEngine.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@AutoConfigureBefore(ThymeleafAutoConfiguration.class)
public class DandelionAutoConfiguration {

	/**
	 * default dandelion servlet configuration
	 */
	@Configuration
	@ConditionalOnMissingBean(name = "dandelionFilterRegistration")
	protected static class DandeliobServletDefaultConfiguration {

		@Bean
		//@ConditionalOnMissingBean
		public Filter dandelionFilter() {
			return new DandelionFilter();
		}

		@Bean
		public FilterRegistrationBean dandelionFilterRegistration() {
			FilterRegistrationBean registrationBean = new FilterRegistrationBean(dandelionFilter(),
					dandelionServletRegistration());
			registrationBean.addUrlPatterns("/*");
			return registrationBean;
		}

		@Bean
		@ConditionalOnMissingBean
		public DandelionServlet dandelionServlet() {
			return new DandelionServlet();
		}

		@Bean
		public ServletRegistrationBean dandelionServletRegistration() {
			ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dandelionServlet(),
					"/dandelion-assets/*");
			servletRegistrationBean.setName("dandelionServlet");
			return servletRegistrationBean;
		}

	}
	
	
	/**
	 * Provide dandelion dialect for thymeleaf view resolver.
	 */
	@Configuration
	@ConditionalOnClass(DandelionDialect.class)
	protected static class DandelionWebLayoutConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public DandelionDialect dandelionDialect() {
			return new DandelionDialect();
		}
	}
}
