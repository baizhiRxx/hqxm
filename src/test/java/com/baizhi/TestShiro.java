package com.baizhi;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestShiro {
    /*
        IniSecurityManagerFactory 方法已经过期
        原因 : shiro官方更希望使用者以环境配置的形式使用shiro而非单纯使用配置文件的形式使用shiro
     */
    @Test
    public void shiro_core(){
        // 创建安全管理工厂
        IniSecurityManagerFactory iniSecurityManagerFactory = new IniSecurityManagerFactory("classpath:shiro.ini");
        // 获取安全管理器
        SecurityManager securityManager = iniSecurityManagerFactory.getInstance();
        // 配置安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        // 获取主体对象
        Subject subject = SecurityUtils.getSubject();
        // 获取令牌
        AuthenticationToken authenticationToken = new UsernamePasswordToken("13643889030","962482");
        // 登陆
        subject.login(authenticationToken);
        // 查看登陆状态
        boolean authenticated = subject.isAuthenticated();
        // 根据角色
        if (subject.hasRole("admin")) {
            System.out.println("你好 管理员");
        }
    }
    @Test
    public void testMD5(){
        Md5Hash md5Hash = new Md5Hash("962482","ABCD",1024);
        System.out.println(md5Hash);
    }
}
