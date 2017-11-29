package com.win.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.win.pojo.AppCategory;
import com.win.pojo.AppInfo;
import com.win.pojo.AppVersion;
import com.win.pojo.DataDictionary;
import com.win.pojo.DevUser;

/**
 * 
 * @author 华大大 2017-11-16 开发者接口
 */
public interface DevService {
	// 登陆
	public DevUser loginDev(@Param("devCode") String devCode, @Param("devPassword") String devPassword);

	// 软件集合
	public List<AppInfo> selectAppInfoList(AppInfo ai, @Param("begin") int begin, @Param("end") int end);

	// 查询软件总数
	public int selectAppCount(AppInfo ai);

	// 查询dataDictionary
	public List<DataDictionary> dataDictionary(@Param("typeCode") String typeCode);

	// 查询分级分类
	public List<AppCategory> categoryLevelList(@Param("id") Integer id);

	// 查询软件名是否存在
	public int existAppName(@Param("APKName") String APKName);

	// 查询软件历史版本
	public List<AppVersion> appVersionList(@Param("appId") int appId);

	// 保存appinfoaddsave
	public int appinfoaddsave(AppInfo ai);

	// 保存新增版本信息
	public int addversionsave(AppVersion av);

	// 删除软件名
	public int delfile(@Param("id") Integer id, @Param("flag") String flag);

	// 修改版本信息
	public int appversionmodifysave(AppVersion av);

	// 修改软件信息
	public int appinfomodifysave(AppInfo ai);

	// 删除软件
	public int delappInfo(Integer id);

	// 删除版本
	public int delVersion(Integer appId);

	// 設置修改软件信息最新版本
	public int updAppInfoVersion(@Param("id") Integer id, @Param("versionId") Integer versionId);

	// 上下架
	public int sale(@Param("id") Integer id, @Param("type") String type);
}
