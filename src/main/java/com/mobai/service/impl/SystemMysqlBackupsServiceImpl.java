package com.mobai.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobai.dao.SystemMysqlBackupsMapper;
import com.mobai.pojo.SystemMysqlBackups;
import com.mobai.service.MailService;
import com.mobai.service.SystemMysqlBackupsService;
import com.mobai.utils.CountsUtils;
import com.mobai.utils.ErrorTip;
import com.mobai.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: <a href="https://www.mobaijun.com">...</a>
 * Date: 2021/12/7 14:27
 * ClassName:SystemMysqlBackupsServiceImpl
 * 类描述： 备份实现类
 */
@Slf4j
@Service
@AllArgsConstructor
public class SystemMysqlBackupsServiceImpl extends ServiceImpl<SystemMysqlBackupsMapper, SystemMysqlBackups> implements SystemMysqlBackupsService {
    private final SystemMysqlBackupsMapper systemMysqlBackupsMapper;
    private final MailService mailService;

    @Override
    public Object mysqlBackups(String filePath, String url, String userName, String password) {
        // 获取ip
        final String ip = url.substring(13, 22);
        // 获取端口号
        final String port = url.substring(23, 27);
        // 获取数据库名称
        final String databaseName = StringUtils.subStringDatabase(url);
        // 数据库文件名称
        StringBuilder mysqlFileName = new StringBuilder()
                .append(databaseName)
                .append("_")
                .append(DateUtil.format(new Date(), "yyyy-MM-dd-HH-mm-ss"))
                .append(CountsUtils.FILE_SUFFIX);
        // 备份命令
        StringBuilder cmd = new StringBuilder()
                .append("mysqldump ")
                .append("--no-tablespaces ")
                .append("-h")
                .append(ip)
                .append(" -u")
                .append(userName)
                .append(" -p")
                .append(password)
                // 如果需要排除某张备份表，把这个空格注释掉
                .append(" ")
                // 排除MySQL备份表
                // .append(" --ignore-table ")
                // .append(database_name)
                // .append(".mysql_backups")
                .append(databaseName)
                .append(" > ")
                .append(filePath)
                .append(mysqlFileName);
        // 判断文件是否保存成功
        if (!FileUtil.exist(filePath)) {
            FileUtil.mkdir(filePath);
            return new ErrorTip(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE.value(), "备份失败，文件保存异常，请查看文件内容后重新尝试！");
        }
        // 获取操作系统名称
        String osName = System.getProperty("os.name").toLowerCase();
        String[] command;
        if (CountsUtils.isSystem(osName)) {
            // Windows
            command = new String[]{"cmd", "/c", String.valueOf(cmd)};
        } else {
            // Linux
            command = new String[]{"/bin/sh", "-c", String.valueOf(cmd)};
        }
        SystemMysqlBackups smb = new SystemMysqlBackups();
        // 备份信息存放到数据库
        smb.setMysqlIp(ip);
        smb.setMysqlPort(port);
        smb.setBackupsName(String.valueOf(mysqlFileName));
        smb.setDatabaseName(databaseName);
        smb.setMysqlCmd(String.valueOf(cmd));
        smb.setBackupsPath(filePath);
        smb.setCreateTime(DateTime.now());
        smb.setStatus(1);
        smb.setOperation(0);
        systemMysqlBackupsMapper.insert(smb);
        log.error("数据库备份命令为：{}", cmd);
        // 获取Runtime实例
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process.waitFor() == 0) {
                log.info("Mysql 数据库备份成功,备份文件名：{}", mysqlFileName);
                // 发送邮件
                mailService.sendSimpleMail();
            } else {
                return new ErrorTip(HttpStatus.INTERNAL_SERVER_ERROR.value(), "网络异常，数据库备份失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorTip(HttpStatus.INTERNAL_SERVER_ERROR.value(), "网络异常，数据库备份失败");
        }
        return smb;
    }

    @Override
    public Object rollback(SystemMysqlBackups smb, String userName, String password) {
        // 备份路径和文件名
        StringBuilder realFilePath = new StringBuilder().append(smb.getBackupsPath()).append(smb.getBackupsName());
        if (!FileUtil.exist(String.valueOf(realFilePath))) {
            return new ErrorTip(HttpStatus.NOT_FOUND.value(), "文件不存在，恢复失败，请查看目录内文件是否存在后重新尝试！");
        }
        StringBuilder cmd = new StringBuilder()
                .append("mysql -h")
                .append(smb.getMysqlIp())
                .append(" -u")
                .append(userName)
                .append(" -p")
                .append(password)
                .append(" ")
                .append(smb.getDatabaseName())
                .append(" < ")
                .append(realFilePath);
        String[] command;
        log.error("数据库恢复命令为：{}", cmd);
        // 获取操作系统名称
        String osName = System.getProperty("os.name").toLowerCase();
        if (CountsUtils.isSystem(osName)) {
            // Windows
            command = new String[]{"cmd", "/c", String.valueOf(cmd)};
        } else {
            // Linux
            command = new String[]{"/bin/sh", "-c", String.valueOf(cmd)};
        }
        // 恢复指令写入到数据库
        smb.setMysqlBackCmd(String.valueOf(cmd));
        // 更新操作次数
        smb.setRecoveryTime(DateTime.now());
        smb.setOperation(smb.getOperation() + 1);
        // 获取Runtime实例
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process.waitFor() == 0) {
                log.error("Mysql 数据库恢复成功,恢复文件名：{}", realFilePath);
            } else {
                return new ErrorTip(HttpStatus.GATEWAY_TIMEOUT.value(), "网络异常，恢复失败，请稍后重新尝试！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorTip(HttpStatus.GATEWAY_TIMEOUT.value(), "网络异常，恢复失败，请稍后重新尝试！");
        }
        return smb;
    }
}
