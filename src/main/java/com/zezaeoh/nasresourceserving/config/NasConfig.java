package com.zezaeoh.nasresourceserving.config;

import com.zezaeoh.nasresourceserving.component.NasSettings;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NasSettings.class)
public class NasConfig {
}
