package com.mobai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobai.pojo.SystemMysqlBackups;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: https://www.mobaijun.com
 * Date: 2021/12/7 14:27
 * InterfaceName:SystemMysqlBackupsService
 * 类描述：MySQL备份接口
 */
public interface SystemMysqlBackupsService extends IService<SystemMysqlBackups> {

    /**
     * mysql备份接口
     */
    Object mysqlBackups(String filePath, String url, String userName, String password);

    /**
     * 恢复数据库
     *
     * @param smb      恢复对象
     * @param userName 数据库用户名
     * @param password 数据库密码
     * @return
     */
    Object rollback(SystemMysqlBackups smb, String userName, String password);
}
