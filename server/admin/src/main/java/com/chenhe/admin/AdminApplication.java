package com.chenhe.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableAdminServer
@EnableEurekaClient
@SpringBootApplication
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	/*@EnableWebSecurity
	static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		private final String adminContextPath;

		public WebSecurityConfig(AdminServerProperties adminServerProperties) {
			this.adminContextPath = adminServerProperties.getContextPath();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
			http.csrf().ignoringAntMatchers(
					adminContextPath + "/instances",
					adminContextPath + "/actuator/**"
			);
			super.configure(http);
		}
	}*/
}
