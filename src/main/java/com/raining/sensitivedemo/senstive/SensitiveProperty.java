package com.raining.sensitivedemo.senstive;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 从配置文件中加载配置
 */
@Data
@Component
@ConfigurationProperties(prefix = SensitiveProperty.SENSITIVE_KEY_PREFIX)
public class SensitiveProperty {

    //在配置文件中的前缀
    public static final String SENSITIVE_KEY_PREFIX = "sensitive-demo.sensitive";

    //true表示开启敏感词检验
    private Boolean enable;

    //自定义的敏感词
    private List<String> deny;

    //自定义的非敏感词
    private List<String> allow;

}
