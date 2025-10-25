package com.sap.movies_service;

import com.sap.common_lib.security.web.SecurityConfig;
import com.sap.common_lib.security.web.WebClientConfig;
import com.sap.common_lib.security.web.filter.MicroServiceFilter;
import com.sap.common_lib.util.DateUtils;
import com.sap.common_lib.util.PublicEndpointUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ PublicEndpointUtil.class, SecurityConfig.class, MicroServiceFilter.class, DateUtils.class, WebClientConfig.class })
public class MoviesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesServiceApplication.class, args);
	}

}
