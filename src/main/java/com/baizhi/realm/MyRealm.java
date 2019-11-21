package com.baizhi.realm;

import com.baizhi.dao.UserDao;
import com.baizhi.entity.Permission;
import com.baizhi.entity.Role;
import com.baizhi.entity.User;
import com.baizhi.util.SpringContextUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MyRealm extends AuthorizingRealm {
    /*
    认证方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        User u = new User();
        u.setPhoneNumber(username);
        UserDao userDao = (UserDao) SpringContextUtil.getBean(UserDao.class);
        User user = userDao.selectOne(u);
        AuthenticationInfo authenticationInfo = new SimpleAccount(user.getPhoneNumber(),user.getPassword(),ByteSource.Util.bytes(user.getSalt()),this.getName());
        return authenticationInfo;
    }
    /*
    授权方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserDao userDao = (UserDao) SpringContextUtil.getBean(UserDao.class);
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        User user = userDao.queryUserRolePermission(primaryPrincipal);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        ArrayList role = new ArrayList();
        ArrayList primission = new ArrayList();
        List<Role> roles = user.getRoles();
        for (Role role1 : roles) {
            role.add(role1.getRole());
            for (Permission permission : role1.getPermissions()) {
                primission.add(permission.getPermission());
            }
        }
        authorizationInfo.addRoles(role);
        authorizationInfo.addStringPermissions(primission);
        return authorizationInfo;
    }
}
