package com.example.ProjectTest.util;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


@ConfigurationProperties(prefix = "test.name")
@Component
@Data
public class ConfigurationPropertiesFile {
	
	
	private String text;


	
	
	
}
