package com.market.market;

import org.apache.catalina.realm.JDBCRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;

public class shiroTest {

	SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm(); //sim不支持添加权限
	
	DruidDataSource dataSource = new DruidDataSource();
	{
		dataSource.setUrl("jdbc:mysql://localhost:3306/supermarket");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
	}
	
	/*@Before
	public void addUser() {
		simpleAccountRealm.addAccount("wy", "123456","admin");	//预存一个认证的用户 第三个参数是给定的角色，可添加多个
	}*/
	
	//认证
	@Test
	public void test() {
		//构建SecurityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(simpleAccountRealm);
		
		//主题提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("wy","123456");
		subject.login(token);	//登录
		
		subject.checkRole("admin");
		System.out.println(subject.isAuthenticated());	//是否认证的结果 true：已经认证 false：没有认证
		subject.logout(); 	//登出
		System.out.println(subject.isAuthenticated());	//是否认证的结果 true：已经认证 false：没有认证
	}
	//自定义Realm
	@Test
	public void setByMySelf() {
		CustomRealm customRealm = new CustomRealm();
		//构建SecurityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(customRealm);
		
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		matcher.setHashIterations(1);
		matcher.setHashAlgorithmName("md5");
		customRealm.setCredentialsMatcher(matcher);
		
		//主题提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("wy","1234");
		subject.login(token);	//登录
		
		subject.checkRole("admin");
		subject.checkPermissions("user:delete","user:add");
		System.out.println(subject.isAuthenticated());	//是否认证的结果 true：已经认证 false：没有认证
	}
	
	@Test
	public void jdbdRealm() {
		JdbcRealm jdbcrealm = new JdbcRealm();
		jdbcrealm.setDataSource(dataSource);
		jdbcrealm.setPermissionsLookupEnabled(true);	//查询权限数据
		//userName, name, password, salt, state  user_info
		String realmSql = "select password from user_info where userName = ?";	//自定义认证sql
		jdbcrealm.setAuthenticationQuery(realmSql);
		String roleSql = "SELECT sr.role FROM sys_user_role sur " + 
				"LEFT JOIN sys_role sr ON sr.id = sur.role_id " + 
				"LEFT JOIN user_info ui ON ui.uid= sur.uid " + 
				"WHERE ui.userName = ?";
		jdbcrealm.setUserRolesQuery(roleSql);
		
		String permissionSql ="SELECT permission FROM sys_role_permission srp " + 
				"LEFT JOIN sys_role sr ON sr.`id` = srp.role_id " + 
				"LEFT JOIN sys_user_role sur ON sur.role_id=id " + 
				"LEFT JOIN user_info ui ON ui.`uid`= sur.`uid` " + 
				"LEFT JOIN sys_permission sp ON sp.`id` = srp.permission_id " + 
				"WHERE ui.`userName` =  ?";
		jdbcrealm.setPermissionsQuery(permissionSql);
		
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(jdbcrealm);
		//主题提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("王碧晨","1234");
		subject.login(token);	//登录
		subject.checkRole("vip");
		//subject.checkPermission("view"); 
		System.out.println(subject.isAuthenticated());	//是否认证的结果 true：已经认证 false：没有认证
		
	}
	
	@Test
	public void md5() {
		Md5Hash md5 =  new Md5Hash("1234","wy");
		System.out.println(md5.toString());
	}
	
}
