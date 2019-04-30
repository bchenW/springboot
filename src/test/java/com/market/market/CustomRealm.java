package com.market.market;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.market.pojo.UserInfo;
import com.market.service.UserInfoService;

public class CustomRealm extends AuthorizingRealm{
	@Resource
    private UserInfoService userInfoService;
	
	Map<String,String> userMap = new HashMap<String,String>(16);
	{
		userMap.put("wy", "756cbc5bd8647393a967bd87410c780f");
		super.setName("customRealm");
	}
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*String userName =  (String) principals.getPrimaryPrincipal();
		//从数据库或者缓存中获取角色数据
		Set<String> roles = getRolesByUserName(userName);
		Set<String> permissions = getPermissionsByUserName(userName);
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setStringPermissions(permissions);
		simpleAuthorizationInfo.setRoles(roles);
		return simpleAuthorizationInfo;*/
		
		String userName= (String) principals.getPrimaryPrincipal();//UserInfo{id=1, UserInfoname='admin', password='3ef7164d1f6167cb9f2658c07d3c2f0a', enable=1}
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
	
	
	private Set<String> getPermissionsByUserName(String userName) {
		Set<String> sets = new HashSet<>();
		sets.add("user:delete");
		sets.add("user:add");
		return sets;
	}


	private Set<String> getRolesByUserName(String userName) {
		Set<String> sets = new HashSet<>();
		sets.add("admin");
		sets.add("user");
		return sets;
	}


	/**
	 * @param 认证
	 * */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		/*
		String userName = (String) token.getPrincipal();
		
		String password = getPasswordByUserName(userName);
		if(password == null) {
			return null;
		}
		SimpleAuthenticationInfo auth = new SimpleAuthenticationInfo("wy",password,"customRealm");
		auth.setCredentialsSalt(ByteSource.Util.bytes(userName));
		return auth;*/
		
		//获取用户的输入的账号
        String principal = (String) token.getPrincipal();
        System.out.println("principal:"+principal);
        UserInfo UserInfo = userInfoService.selectByUsername(principal);
        if(UserInfo==null) throw new UnknownAccountException();
       // UserInfo UserInfo = Optional.ofNullable(userInfoService.selectByUsername(principal)).orElseThrow(UnknownAccountException::new);
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
	private String getPasswordByUserName(String userName) {
		return userMap.get(userName);
	}
	
	public static void main(String[] args) {
		Md5Hash md5 =  new Md5Hash("1234","wy");
		System.out.println(md5.toString());
	}

}
