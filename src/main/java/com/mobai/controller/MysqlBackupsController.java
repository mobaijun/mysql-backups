package com.mobai.controller;

import com.mobai.annotation.ApiJsonObject;
import com.mobai.annotation.ApiJsonProperty;
import com.mobai.pojo.SystemMysqlBackups;
import com.mobai.service.SystemMysqlBackupsService;
import com.mobai.utils.CountsUtils;
import com.mobai.utils.ErrorTip;
import com.mobai.utils.SuccessTip;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: <a href="https://www.mobaijun.com">...</a>
 * Date: 2021/9/7 22:25
 * ClassName:MysqlBackupsController
 * 类描述： 备份控制器
 */

@RestController
@Api(value = "MySQL数据备份")
@RequestMapping(value = "/system")
public class MysqlBackupsController {

    /**
     * 数据库用户名
     */
    @Value("${spring.datasource.username}")
    private String userName;

    /**
     * 数据库密码
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 数据库url
     */
    @Value("${spring.datasource.url}")
    private String url;

    /**
     * Windows数据库地址
     */
    @Value("${spring.datasource.win-path}")
    private String windowsPath;

    /**
     * Linux数据地址
     */
    @Value("${spring.datasource.linux-path}")
    private String linuxPath;


    @Resource
    private SystemMysqlBackupsService systemMysqlBackupsService;

    @ApiOperation(value = "获取所有备份数据列表")
    @GetMapping("/backupsList")
    public Object backupsList() {
        List<SystemMysqlBackups> systemMysqlBackups = systemMysqlBackupsService.list();
        return new SuccessTip(systemMysqlBackups);
    }

    @ApiOperation(value = "MySQL备份")
    @PostMapping("/mysqlBackups")
    public Object mysqlBackups() {
        String path = null;
        // 获取操作系统名称
        String osName = System.getProperty("os.name").toLowerCase();
        if (CountsUtils.isSystem(osName)) {
            // Windows
            path = this.windowsPath;
        } else {
            // Linux
            path = this.linuxPath;
        }
        // 数据库用户名
        String userName = this.userName;
        // 数据库密码
        String password = this.password;
        // 数据库地址
        String url = this.url;
        // 调用备份
        Object systemMysqlBackups = systemMysqlBackupsService.mysqlBackups(path, url, userName, password);
        return new SuccessTip(systemMysqlBackups);
    }


    @ApiOperation(value = "恢复数据库")
    @PutMapping("/rollback")
    public Object rollback(@ApiJsonObject(name = "恢复数据库", value = {
            @ApiJsonProperty(name = "id", example = "1", value = "数据id", dataType = "long", required = true)})
                           @ApiParam(value = "恢复数据库") @RequestBody Map<String, Object> map) {
        Long id = Long.valueOf(map.get("id").toString());
        // 数据库用户名
        String userName = this.userName;
        // 数据库密码
        String password = this.password;
        // 根据id查询查询已有的信息
        SystemMysqlBackups smb = systemMysqlBackupsService.getBaseMapper().selectById(id);
        // 恢复数据库
        Object rollback = systemMysqlBackupsService.rollback(smb, userName, password);
        // 更新操作次数
        systemMysqlBackupsService.updateById(smb);
        return new SuccessTip(rollback);
    }
}
