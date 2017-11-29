package com.win.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.win.pojo.AppCategory;
import com.win.pojo.AppInfo;
import com.win.pojo.AppVersion;
import com.win.pojo.BackendUser;
import com.win.pojo.DataDictionary;

/**
 * 
 * @author 华大大 后台接口 2017-11-16
 */
public interface BackService {
	/**
	 * 
	 * @return BackendUser 后台登陆
	 */
	public BackendUser loginBack(@Param("userCode") String userCode, @Param("userPassword") String userPassword);

	// 软件集合
	public List<AppInfo> list(@Param("ai") AppInfo ai, @Param("begin") int begin, @Param("end") int end);

	// 查询软件总数
	public int selectAppCount(AppInfo ai);

	// 查询dataDictionary
	public List<DataDictionary> dataDictionary(@Param("typeCode") String typeCode);

	// 查询分级分类
	public List<AppCategory> categoryLevelList(@Param("id") Integer id);

	// 查询版本
	public AppVersion selectVersion(Integer id);
	//审核
	public int checksave(AppInfo ai);
}
