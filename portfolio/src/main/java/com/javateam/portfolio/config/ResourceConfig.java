package com.javateam.portfolio.config;

import javax.annotation.Resources;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		
		registry.addResourceHandler("/jquery/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/jquery/");
		
		registry.addResourceHandler("/bootstrap/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap/");
	
		// axios
		registry.addResourceHandler("/axios/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/axios/");
				
		// swagger
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		
		// bootstrap icons
		registry.addResourceHandler("/bootstrap-icons/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap-icons/");
				
		// summernote
		registry.addResourceHandler("/summernote/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/summernote/");
		
		// 상품 이미지 자원 경로
		registry.addResourceHandler("/thumbnails/**")
				.addResourceLocations("file:///D:/Ssong/downloads/product_images/thumbnails/");
		
	}
}
