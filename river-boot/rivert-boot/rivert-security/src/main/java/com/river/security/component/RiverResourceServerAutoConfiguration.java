
package com.river.security.component;

import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @author river
 * @date 2020-06-23
 */
@ConfigurationPropertiesScan
@ComponentScan("com.river.security")
public class RiverResourceServerAutoConfiguration {

	@Bean
	@Primary
	public RestTemplate lbRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		// 传递ACCEPT JSON
		restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
			request.getHeaders().set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			return execution.execute(request, body);
		}));

		// 处理400 异常
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			@SneakyThrows
			public void handleError(ClientHttpResponse response) {
				if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
					super.handleError(response);
				}
			}
		});
		return restTemplate;
	}

}
