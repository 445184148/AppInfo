package com.win.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.win.dao.DevMapper;
import com.win.pojo.AppCategory;
import com.win.pojo.AppInfo;
import com.win.pojo.AppVersion;
import com.win.pojo.DataDictionary;
import com.win.pojo.DevUser;

@Service
public class DevServiceImpl implements DevService {
	@Autowired
	private DevMapper dm;

	public DevMapper getDm() {
		return dm;
	}

	public void setDm(DevMapper dm) {
		this.dm = dm;
	}

	@Override
	public DevUser loginDev(String devCode, String devPassword) {
		// TODO Auto-generated method stub
		return dm.loginDev(devCode, devPassword);
	}

	@Override
	public List<AppInfo> selectAppInfoList(AppInfo ai, int begin, int end) {
		// TODO Auto-generated method stub
		return dm.selectAppInfoList(ai, begin, end);
	}

	@Override
	public int selectAppCount(AppInfo ai) {
		// TODO Auto-generated method stub
		return dm.selectAppCount(ai);
	}

	@Override
	public List<AppCategory> categoryLevelList(@Param("id") Integer id) {
		return dm.categoryLevelList(id);
	}

	@Override
	public List<DataDictionary> dataDictionary(String typeCode) {
		// TODO Auto-generated method stub
		return dm.dataDictionary(typeCode);
	}

	@Override
	public int existAppName(String APKName) {
		// TODO Auto-generated method stub
		return dm.existAppName(APKName);
	}

	@Override
	public List<AppVersion> appVersionList(int appId) {
		// TODO Auto-generated method stub
		return dm.appVersionList(appId);
	}

	@Override
	public int appinfoaddsave(AppInfo ai) {
		// TODO Auto-generated method stub
		return dm.appinfoaddsave(ai);
	}

	@Override
	public int addversionsave(AppVersion av) {
		// TODO Auto-generated method stub
		return dm.addversionsave(av);
	}

	@Override
	public int delfile(Integer id, String flag) {
		// TODO Auto-generated method stub
		return dm.delfile(id, flag);
	}

	@Override
	public int appversionmodifysave(AppVersion av) {
		// TODO Auto-generated method stub
		return dm.appversionmodifysave(av);
	}

	@Override
	public int appinfomodifysave(AppInfo ai) {
		// TODO Auto-generated method stub
		return dm.appinfomodifysave(ai);
	}

	@Override
	public int delappInfo(Integer id) {
		// TODO Auto-generated method stub
		return dm.delappInfo(id);
	}

	@Override
	public int delVersion(Integer appId) {
		// TODO Auto-generated method stub
		return dm.delVersion(appId);
	}

	@Override
	public int updAppInfoVersion(Integer id, Integer versionId) {
		// TODO Auto-generated method stub
		return dm.updAppInfoVersion(id, versionId);
	}

	@Override
	public int sale(Integer id, String type) {
		// TODO Auto-generated method stub
		return dm.sale(id, type);
	}

}
