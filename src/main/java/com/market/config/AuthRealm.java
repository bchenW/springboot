package com.market.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.Resources;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Configuration;

import com.market.pojo.UserInfo;
import com.market.service.UserInfoService;

@Configuration
public class AuthRealm extends AuthorizingRealm {

	@Resource
    private UserInfoService userInfoService;
	
    /**
     * 认证回调函数,登录时调用
     * 首先根据传入的用户名获取UserInfo信息；然后如果UserInfo为空，那么抛出没找到帐号异常UnknownAccountException；
     * 如果UserInfo找到但锁定了抛出锁定异常LockedAccountException；最后生成AuthenticationInfo信息，
     * 交给间接父类AuthenticatingRealm使用CredentialsMatcher进行判断密码是否匹配，
     * 如果不匹配将抛出密码错误异常IncorrectCredentialsException；
     * 另外如果密码重试此处太多将抛出超出重试次数异常ExcessiveAttemptsException；
     * 在组装SimpleAuthenticationInfo信息时， 需要传入：身份信息（用户名）、凭据（密文密码）、盐（UserInfoname+salt），
     * CredentialsMatcher使用盐加密传入的明文密码和此处的密文密码进行匹配。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
    	//获取用户的输入的账号
       /* String principal = (String) token.getPrincipal();
        UserInfo UserInfo = Optional.ofNullable(userInfoService.selectByUsername(principal)).orElseThrow(UnknownAccountException::new);
        */String principal = (String) token.getPrincipal();
        System.out.println("principal:"+principal);
        UserInfo UserInfo = userInfoService.selectByUsername(principal);
        if(UserInfo==null) throw new UnknownAccountException();
        
        if (!UserInfo.getState().equals("0")) {	//不等于0就标识该用户被禁用
            throw new LockedAccountException();
        }
        // 从数据库查询出来的账号名和密码,与用户输入的账号和密码对比
        // 当用户执行登录时,在方法处理上要实现 UserInfo.login(token)
        // 然后会自动进入这个类进行认证
        // 交给 AuthenticatingRealm 使用 CredentialsMatcher 进行密码匹配，如果觉得人家的不好可以自定义实现
        // TODO 如果使用 HashedCredentialsMatcher 这里认证方式就要改一下 SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, "密码", ByteSource.Util.bytes("密码盐"), getName());
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, UserInfo.getPassword(), getName());
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("UserInfo_SESSION", UserInfo);
        return authenticationInfo;
    }

    /*授权*/
    /**
     * 只有需要验证权限时才会调用, 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.在配有缓存的情况下，只加载一次.
     * 如果需要动态权限,但是又不想每次去数据库校验,可以存在ehcache中.自行完善
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    	String userName= (String) SecurityUtils.getSubject().getPrincipal();//UserInfo{id=1, UserInfoname='admin', password='3ef7164d1f6167cb9f2658c07d3c2f0a', enable=1}
        System.out.println("userName:"+userName);
    	Map<String,Object> map = new HashMap<String,Object>();
        map.put("userName",userName);
        Set<String> roleList = userInfoService.getRolesByUserName(map);	//获取到role
        Set<String> permissionList = userInfoService.getPermissionsByUserName(map);	//获取到role
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        
        simpleAuthorizationInfo.setStringPermissions(permissionList);
  		simpleAuthorizationInfo.setRoles(roleList);
        /*for(Resources resources: resourcesList){
            info.addStringPermission(resources.getResurl());
        }*/
        return simpleAuthorizationInfo;
    }
    /**
     * 只有需要验证权限时才会调用, 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.在配有缓存的情况下，只加载一次.
     * 如果需要动态权限,但是又不想每次去数据库校验,可以存在ehcache中.自行完善
     */
   /* @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        Session session = SecurityUtils.getSubject().getSession();
        UserInfo UserInfo = (UserInfo) session.getAttribute("UserInfo_SESSION");
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 用户的角色集合
        Set<String> roles = new HashSet<>();
        roles.add(UserInfo.getRoleName());
        info.setRoles(roles);
        // 用户的角色对应的所有权限，如果只使用角色定义访问权限，下面可以不要
        // 只有角色并没有颗粒度到每一个按钮 或 是操作选项  PERMISSIONS 是可选项
        final Map<String, Collection<String>> permissionsCache = DBCache.PERMISSIONS_CACHE;
        final Collection<String> permissions = permissionsCache.get(UserInfo.getRoleName());
        info.addStringPermissions(permissions);
        return info;
    }*/
    
    //授权
  	/*@Override
  	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
  		//String userName =  (String) principals.getPrimaryPrincipal();
		Session session = SecurityUtils.getSubject().getSession();
		UserInfo UserInfo = (UserInfo) session.getAttribute("UserInfo_SESSION");
         
  		//从数据库或者缓存中获取角色数据
  		Set<String> roles = getRolesByUserName(UserInfo.getName());
  		Set<String> permissions = getPermissionsByUserName(UserInfo.getName());
  		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
  		simpleAuthorizationInfo.setStringPermissions(permissions);
  		simpleAuthorizationInfo.setRoles(roles);
  		//final Map<String, Collection<String>> permissionsCache = DBCache.PERMISSIONS_CACHE;
  		return simpleAuthorizationInfo;
  	}*/
  	
  

}
