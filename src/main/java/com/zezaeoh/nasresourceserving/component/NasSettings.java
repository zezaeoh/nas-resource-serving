package com.zezaeoh.nasresourceserving.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="nas")
@Getter
@Setter
public class NasSettings {
    private String basePath;
}