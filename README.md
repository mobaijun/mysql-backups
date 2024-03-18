## mysql-backups

使用 `SpringBoot2.x` 整合 `MySQL` 数据备份和简单邮件发送

## 快速开始

1. 创建 `backups` 数据库
2. 导入 `src/main/resources/mysql_backups.sql` 表
3. 修改 `application-dev.yml` 配置

~~~yml
# MySQL配置
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/backups?autoReconnect=true&useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Hongkong&allowPublicKeyRetrieval=true
    username: root
    password: root
    win-path: C:\\mysqlbackups\\
    linux-path: /mysql/backups/
  mail:
    username:
    password:
~~~

4. 启动项目

5. 打开接口测试地址：http://IP:PROD/swagger-ui/index.html

6. 校验接口
