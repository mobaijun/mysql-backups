package com.mobai.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Date: 2021/9/16 14:47
 * ClassName:SystemMysqlBackups
 * 类描述： MySQL备份实体
 */
@Data
@TableName("mysql_backups")
public class SystemMysqlBackups {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * MySQL服务器IP地址
     */
    @TableField("mysql_ip")
    private String mysqlIp;

    /**
     * MySQL服务器端口号
     */
    @TableField("mysql_port")
    private String mysqlPort;

    /**
     * MySQL服务器端口号
     */
    @TableField("database_name")
    private String databaseName;

    /**
     * MySQL备份指令
     */
    @TableField("mysql_cmd")
    private String mysqlCmd;

    /**
     * MySQL恢复指令
     */
    @TableField("mysql_back_cmd")
    private String mysqlBackCmd;

    /**
     * MySQL备份存储地址
     */
    @TableField("backups_path")
    private String backupsPath;

    /**
     * MySQL备份文件名称
     */
    @TableField("backups_name")
    private String backupsName;

    /**
     * 操作次数
     */
    @TableField("operation")
    private Integer operation;

    /**
     * 数据状态
     */
    @TableLogic
    @TableField("status")
    private Integer status;

    /**
     * 恢复时间
     */
    @TableField("recovery_time")
    private Date recoveryTime;

    /**
     * 备份时间
     */
    @TableField("create_time")
    private Date createTime;
}
