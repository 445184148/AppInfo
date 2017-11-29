package com.win.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.win.dao.BackMapper;
import com.win.pojo.AppCategory;
import com.win.pojo.AppInfo;
import com.win.pojo.AppVersion;
import com.win.pojo.BackendUser;
import com.win.pojo.DataDictionary;

@Service
public class BackServiceImpl implements BackService {
	@Autowired
	private BackMapper bm;

	public BackMapper getBm() {
		return bm;
	}

	public void setBm(BackMapper bm) {
		this.bm = bm;
	}

	@Override
	public BackendUser loginBack(String userCode, String userPassword) {
		// TODO Auto-generated method stub
		return bm.loginBack(userCode, userPassword);
	}

	@Override
	public List<AppInfo> list(AppInfo ai, int begin, int end) {
		// TODO Auto-generated method stub
		return bm.list(ai, begin, end);
	}

	@Override
	public int selectAppCount(AppInfo ai) {
		// TODO Auto-generated method stub
		return bm.selectAppCount(ai);
	}

	@Override
	public List<DataDictionary> dataDictionary(String typeCode) {
		// TODO Auto-generated method stub
		return bm.dataDictionary(typeCode);
	}

	@Override
	public List<AppCategory> categoryLevelList(Integer id) {
		// TODO Auto-generated method stub
		return bm.categoryLevelList(id);
	}

	@Override
	public AppVersion selectVersion(Integer id) {
		// TODO Auto-generated method stub
		return bm.selectVersion(id);
	}

	@Override
	public int checksave(AppInfo ai) {
		// TODO Auto-generated method stub
		return bm.checksave(ai);
	}

}
