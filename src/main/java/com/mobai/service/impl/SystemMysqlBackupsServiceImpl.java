package com.mobai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobai.dao.SystemMysqlBackupsMapper;
import com.mobai.pojo.SystemMysqlBackups;
import com.mobai.service.MailService;
import com.mobai.service.SystemMysqlBackupsService;
import com.mobai.utils.CountsUtils;
import com.mobai.utils.StringUtils;
import com.mobaijun.common.util.date.DatePattern;
import com.mobaijun.common.util.date.LocalDateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: <a href="https://www.mobaijun.com">...</a>
 * Date: 2021/12/7 14:27
 * ClassName:SystemMysqlBackupsServiceImpl
 * 类描述： 备份实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemMysqlBackupsServiceImpl extends ServiceImpl<SystemMysqlBackupsMapper, SystemMysqlBackups> implements SystemMysqlBackupsService {

    private final SystemMysqlBackupsMapper systemMysqlBackupsMapper;

    private final MailService mailService;

    @Override
    public SystemMysqlBackups mysqlBackups(String filePath, String url, String userName, String password) {
        try {
            // 获取ip
            final String ip = url.substring(13, 22);
            // 获取端口号
            final String port = url.substring(23, 27);
            // 获取数据库名称
            final String databaseName = StringUtils.subStringDatabase(url);
            // 数据库文件名称
            StringBuilder mysqlFileName = new StringBuilder().append(databaseName).append("_").append(LocalDateUtil.toFormat(LocalDateTime.now(), DatePattern.YYYYMMDDHHMMSS)).append(CountsUtils.FILE_SUFFIX);
            // 备份命令
            StringBuilder cmd = new StringBuilder().append("mysqldump ").append("--no-tablespaces ").append("-h").append(ip).append(" -u").append(userName).append(" -p").append(password)
                    // 如果需要排除某张备份表，把这个空格注释掉
                    .append(" ")
                    // 排除MySQL备份表
                    //  .append(" --ignore-table ")
                    //  .append(databaseName)
                    //  .append(".mysql_backups")
                    .append(databaseName).append(" > ").append(filePath).append(mysqlFileName);
            // 判断文件是否保存成功
            Path file = Paths.get(filePath);
            if (!Files.exists(file)) {
                try {
                    Files.createDirectories(file.getParent());
                } catch (IOException e) {
                    log.error("文件创建失败，请检查目录是否存在");
                }
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
            smb.setCreateTime(LocalDateTime.now());
            log.error("数据库备份命令为：{}", cmd);
            // 获取Runtime实例
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            if (process.waitFor() == 0) {
                log.info("Mysql 数据库备份成功,备份文件名：{}", mysqlFileName);
                // 发送邮件
                mailService.sendSimpleMail();
            } else {
                throw new RuntimeException("网络异常，数据库备份失败");
            }
            systemMysqlBackupsMapper.insert(smb);
            return smb;
        } catch (Exception e) {
            log.error("备份失败: {}", e.getMessage());
            throw new RuntimeException("备份失败: " + e.getMessage());
        }
    }

    @Override
    public SystemMysqlBackups rollback(SystemMysqlBackups smb, String userName, String password) {
        try {
            // 备份路径和文件名
            StringBuilder realFilePath = new StringBuilder().append(smb.getBackupsPath()).append(smb.getBackupsName());
            if (!Files.exists(new File(String.valueOf(realFilePath)).toPath())) {
                throw new RuntimeException("文件不存在，恢复失败，请查看目录内文件是否存在后重新尝试！");
            }
            StringBuilder cmd = new StringBuilder().append("mysql -h").append(smb.getMysqlIp()).append(" -u").append(userName).append(" -p").append(password).append(" ").append(smb.getDatabaseName()).append(" < ").append(realFilePath);
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
            smb.setRecoveryTime(LocalDateTime.now());
            smb.setOperation(smb.getOperation() + 1);
            // 获取Runtime实例
            Process process;
            ProcessBuilder builder = new ProcessBuilder(command);
            process = builder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("Mysql 数据库恢复成功, 恢复文件名：{}", realFilePath);
                return smb;
            } else {
                throw new RuntimeException("数据库恢复失败，请稍后重新尝试！");
            }
        } catch (IOException | InterruptedException e) {
            log.error("数据库恢复时出现异常: {}", e.getMessage());
            throw new RuntimeException("数据库恢复失败，请稍后重新尝试！");
        }
    }
}
