# —种基于 MyBatis 拦截器和 Trie 树的敏感词过滤算法

## 项目效果

以查询数据库中评论表为例：

数据库内容：
![comment_db.png](docs%2Fsrc%2Fcomment_db.png)

过滤前：
```bash
$ curl http://localhost:8080/alpha/comment/10001

你怕不是个脏话吧
```

在数据库DO对象上相关字段添加注解：

```java
/**
 * 评论内容
 */
@SensitiveField(bind = "content")
private String content;
```

再次测试，结果如下：

```bash
$ curl http://localhost:8080/alpha/comment/10001

你怕不是个***吧
```

敏感词存放位置：
1. resources/sensitive-words.txt
2. resources/application.yml（自定义白名单黑名单）

## 项目原理

基于MyBatis拦截器`handleResultSets`方法的执行，重写`ResultSets`。

```java
//package com.raining.sensitivedemo.senstive.ibatis;

@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {java.sql.Statement.class})
})
@Component
@Slf4j
public class SensitiveInterceptor implements Interceptor {
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //重写该方法
    }
}
```

敏感词过滤算法采用前缀树Trie，优化了朴素算法。

```java
//package com.raining.sensitivedemo.senstive.impl;

public class TrieTreeSensitiveServiceImpl implements SensitiveService {
    //具体逻辑
}
```

## 附录

示例数据库建库脚本：

```sql
-- comment definition
CREATE TABLE `comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '文章ID',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `content` varchar(300) NOT NULL DEFAULT '' COMMENT '评论内容',
  `top_comment_id` int(11) NOT NULL DEFAULT '0' COMMENT '顶级评论ID',
  `parent_comment_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '父评论ID',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COMMENT='评论表';
```