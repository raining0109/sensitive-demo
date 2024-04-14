package com.raining.sensitivedemo.senstive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 敏感词服务类
 */
@Service
public interface SensitiveService {

    /**
     * 敏感词替换
     *
     * @param txt 待替换的文本
     * @return 返回替换后无敏感词的文本
     */
    public String replace(String txt);
}
