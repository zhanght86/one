package com.sinosoft.one.monitor.shiro;


import com.sinosoft.one.monitor.account.model.Account;
import com.sinosoft.one.monitor.account.service.AccountManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    private AccountManager accountManager;

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public ShiroDbRealm() {
        super();
        super.setAuthenticationTokenClass(LoginToken.class);
    }

    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        LoginToken token = (LoginToken) authcToken;
        Account account = accountManager.findUserByLoginName(token.getLoginName());
        if (account != null) {
            if (account.getPassword() != null && !"".equals(account.getPassword().toString())) {
                return new SimpleAuthenticationInfo(account, account.getPassword(), getName());
            } else {
                return new SimpleAuthenticationInfo(account, DigestUtils.md5(token.getPassword()), getName());
            }
        } else {
            return null;
        }
    }

    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        return null;
    }


}
