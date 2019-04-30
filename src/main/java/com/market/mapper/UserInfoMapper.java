package com.market.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;

import com.market.pojo.UserInfo;
@Mapper
public interface UserInfoMapper {
	
	int deleteByPrimaryKey(String uid);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
    
    List<String> getRolesByUserName(Map<String, Object> map);

    List<String> getPermissionsByUserName(Map<String, Object> map);

	UserInfo selectByUsername(String userName);

}
