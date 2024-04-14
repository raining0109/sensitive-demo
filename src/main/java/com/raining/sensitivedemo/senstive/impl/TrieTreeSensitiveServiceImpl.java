package com.raining.sensitivedemo.senstive.impl;

import com.raining.sensitivedemo.senstive.SensitiveProperty;
import com.raining.sensitivedemo.senstive.SensitiveService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 前缀树实现 敏感词过滤
 */
@Component
@Slf4j
public class TrieTreeSensitiveServiceImpl implements SensitiveService {

    @Autowired
    private SensitiveProperty sensitiveProperty;

    //替换符
    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String keywords;
            while ((keywords = reader.readLine()) != null) {
                //如果在白名单中，则忽略
                if (sensitiveProperty.getAllow().contains(keywords)) {
                    continue;
                }
                //添加到前缀树
                this.addKeyword(keywords);
            }
            //将配置文件中的敏感词也加入到前缀树中
            sensitiveProperty.getDeny().forEach(this::addKeyword);
        } catch (IOException e) {
            log.error("加载敏感词文件失败：", e.getMessage());
        }
    }

    /**
     * 将一个敏感词添加到前缀树中去
     *
     * @param keyword 需要添加的敏感词
     */
    private void addKeyword(String keyword) {
        TrieNode tmpNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            //先查一下是否有这个子节点，有的话就不需要创建了
            TrieNode subNode = tmpNode.getSubNode(c);
            if (subNode == null) {
                //初始化子节点
                subNode = new TrieNode();
                tmpNode.addSubNode(c, subNode);
            }
            //指向子节点，进入下一轮循环
            tmpNode = subNode;

            //设置结束的标记
            if (i == keyword.length() - 1) {
                tmpNode.setKeywordEnd(true);
            }
        }
    }

    //公用方法
    public String replace(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        //指针1
        TrieNode tmpNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            //跳过符号
            if (isSymbol(c)) {
                //若指针1处于根节点
                if (tmpNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                //无论符号在开头还是中间，指针3都要向下走一步
                position++;
                continue;
            }

            // 检查下级节点
            tmpNode = tmpNode.getSubNode(c);
            if (tmpNode == null) {
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                begin++;
                position = begin;
                //重新指向根节点
                tmpNode = rootNode;
            } else if (tmpNode.isKeywordEnd()) {
                //发现了敏感词，将begin开头，position结尾的字符串替换掉
                sb.append(REPLACEMENT);
                position++;
                begin = position;
                tmpNode = rootNode;
            } else {
                //检查下一个字符
                position++;
            }
        }
        //3提前到终点了，2还没有到终点，将最后一批字符串存入结果
        sb.append(text.substring(begin));

        return sb.toString();
    }

    //判断是否为特殊符号
    private boolean isSymbol(Character c) {
        return CharUtils.isAsciiNumeric(c) && (c < 0x2e80 || c > 0x9fff);
    }


    //前缀树
    @Data
    private class TrieNode {
        //关键词结束的标识
        private boolean isKeywordEnd = false;

        //子节点(key是下级字符，value是下级节点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        //添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }

}
