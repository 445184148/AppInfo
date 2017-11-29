package com.win.pojo;

import java.util.Date;

public class BackendUser {
	private Integer id;// ����id
	private String userCode;// �û����루��¼�˺ţ�
	private String userName;// �û�����
	private String userPassword;// �û�����
	private Integer userType;// �û���ɫ����id
	private Integer createdBy;// ������
	private Date creationDate;// ����ʱ��
	private Integer modifyBy;// ������
	private Date modifyDate;// ����ʱ��
	private String userTypeName;// �û���ɫ��������

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(Integer modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public String toString() {
		return "BackendUser [id=" + id + ", userCode=" + userCode + ", userName=" + userName + ", userPassword="
				+ userPassword + ", userType=" + userType + ", createdBy=" + createdBy + ", creationDate="
				+ creationDate + ", modifyBy=" + modifyBy + ", modifyDate=" + modifyDate + ", userTypeName="
				+ userTypeName + "]";
	}

}