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
 * @author ����� ��̨�ӿ� 2017-11-16
 */
public interface BackService {
	/**
	 * 
	 * @return BackendUser ��̨��½
	 */
	public BackendUser loginBack(@Param("userCode") String userCode, @Param("userPassword") String userPassword);

	// �������
	public List<AppInfo> list(@Param("ai") AppInfo ai, @Param("begin") int begin, @Param("end") int end);

	// ��ѯ�������
	public int selectAppCount(AppInfo ai);

	// ��ѯdataDictionary
	public List<DataDictionary> dataDictionary(@Param("typeCode") String typeCode);

	// ��ѯ�ּ�����
	public List<AppCategory> categoryLevelList(@Param("id") Integer id);

	// ��ѯ�汾
	public AppVersion selectVersion(Integer id);
	//���
	public int checksave(AppInfo ai);
}
