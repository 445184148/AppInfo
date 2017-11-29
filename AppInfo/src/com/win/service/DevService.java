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
 * @author ����� 2017-11-16 �����߽ӿ�
 */
public interface DevService {
	// ��½
	public DevUser loginDev(@Param("devCode") String devCode, @Param("devPassword") String devPassword);

	// �������
	public List<AppInfo> selectAppInfoList(AppInfo ai, @Param("begin") int begin, @Param("end") int end);

	// ��ѯ�������
	public int selectAppCount(AppInfo ai);

	// ��ѯdataDictionary
	public List<DataDictionary> dataDictionary(@Param("typeCode") String typeCode);

	// ��ѯ�ּ�����
	public List<AppCategory> categoryLevelList(@Param("id") Integer id);

	// ��ѯ������Ƿ����
	public int existAppName(@Param("APKName") String APKName);

	// ��ѯ�����ʷ�汾
	public List<AppVersion> appVersionList(@Param("appId") int appId);

	// ����appinfoaddsave
	public int appinfoaddsave(AppInfo ai);

	// ���������汾��Ϣ
	public int addversionsave(AppVersion av);

	// ɾ�������
	public int delfile(@Param("id") Integer id, @Param("flag") String flag);

	// �޸İ汾��Ϣ
	public int appversionmodifysave(AppVersion av);

	// �޸������Ϣ
	public int appinfomodifysave(AppInfo ai);

	// ɾ�����
	public int delappInfo(Integer id);

	// ɾ���汾
	public int delVersion(Integer appId);

	// �O���޸������Ϣ���°汾
	public int updAppInfoVersion(@Param("id") Integer id, @Param("versionId") Integer versionId);

	// ���¼�
	public int sale(@Param("id") Integer id, @Param("type") String type);
}
