package com.market.market;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class CustomRealm extends AuthorizingRealm{
	
	Map<String,String> userMap = new HashMap<String,String>(16);
	{
		userMap.put("wy", "756cbc5bd8647393a967bd87410c780f");
		super.setName("customRealm");
	}
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName =  (String) principals.getPrimaryPrincipal();
		//从数据库或者缓存中获取角色数据
		Set<String> roles = getRolesByUserName(userName);
		Set<String> permissions = getPermissionsByUserName(userName);
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setStringPermissions(permissions);
		simpleAuthorizationInfo.setRoles(roles);
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
	 * @param 主题传过来的认证信息
	 * */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		String userName = (String) token.getPrincipal();
		
		String password = getPasswordByUserName(userName);
		if(password == null) {
			return null;
		}
		SimpleAuthenticationInfo auth = new SimpleAuthenticationInfo("wy",password,"customRealm");
		auth.setCredentialsSalt(ByteSource.Util.bytes(userName));
		return auth;
	}
	private String getPasswordByUserName(String userName) {
		return userMap.get(userName);
	}
	
	public static void main(String[] args) {
		Md5Hash md5 =  new Md5Hash("1234","wy");
		System.out.println(md5.toString());
	}

}
