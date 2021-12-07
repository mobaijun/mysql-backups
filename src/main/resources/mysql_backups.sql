-- ----------------------------------------
-- ----MYSQL数据备份表
-- ----------------------------------------
-- DROP TABLE IF EXISTS `mysql_backups`;
CREATE TABLE IF NOT EXISTS `mysql_backups` (
 `id` INT ( 11 ) NOT NULL AUTO_INCREMENT COMMENT '主键id',
 `mysql_ip` VARCHAR ( 15 ) DEFAULT NULL COMMENT '数据库IP',
 `mysql_port` VARCHAR ( 5 ) DEFAULT NULL COMMENT '数据库端口',
 `mysql_cmd` VARCHAR ( 230 ) DEFAULT NULL COMMENT '备份命令',
 `mysql_back_cmd` VARCHAR ( 230 ) DEFAULT NULL COMMENT '恢复命令',
 `database_name` VARCHAR ( 20 ) DEFAULT NULL COMMENT '数据库名称',
 `backups_path` VARCHAR ( 180 ) DEFAULT NULL COMMENT '备份数据地址',
 `backups_name` VARCHAR ( 50 ) DEFAULT NULL COMMENT '备份文件名称',
 `operation` INT ( 11 ) DEFAULT NULL COMMENT '操作次数',
 `status` INT ( 1 ) DEFAULT NULL COMMENT '数据状态（0正常，1删除）',
 `recovery_time` DATETIME DEFAULT NULL COMMENT '恢复时间',
 `create_time` DATETIME DEFAULT NULL COMMENT '备份时间',
 PRIMARY KEY ( `id` ),
 INDEX baskups_index ( mysql_ip, mysql_port, backups_path, database_name,backups_name) USING BTREE COMMENT '索引'
) ENGINE = INNODB AUTO_INCREMENT = 1 CHARSET = UTF8 ROW_FORMAT = COMPACT COMMENT = 'MySQL数据备份表';