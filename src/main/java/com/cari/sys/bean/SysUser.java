package com.cari.sys.bean;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class SysUser {
	private String userId;//用户ID
	private String userName;//用户名称
	private String password;//用户密码
	private String code;//工号
	private String dept;//所属部门
	private String duty;//职务
	private String cartType;//证件类型
	private String cartCode;//证件号
	private Timestamp regDate;//注册时间
	private String nation;//国籍
	private String sex;//性别
	private String tel;//电话
	private String mobile;//手机
	private String email;//E-mail
	private String address;//地址
	private String postCode;//邮编
	private String remark;//备注
	
	private Set roles = new HashSet();

	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address The address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return Returns the cartCode.
	 */
	public String getCartCode() {
		return cartCode;
	}
	/**
	 * @param cartCode The cartCode to set.
	 */
	public void setCartCode(String cartCode) {
		this.cartCode = cartCode;
	}
	/**
	 * @return Returns the cartType.
	 */
	public String getCartType() {
		return cartType;
	}
	/**
	 * @param cartType The cartType to set.
	 */
	public void setCartType(String cartType) {
		this.cartType = cartType;
	}
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return Returns the dept.
	 */
	public String getDept() {
		return dept;
	}
	/**
	 * @param dept The dept to set.
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}
	/**
	 * @return Returns the duty.
	 */
	public String getDuty() {
		return duty;
	}
	/**
	 * @param duty The duty to set.
	 */
	public void setDuty(String duty) {
		this.duty = duty;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the export.
	 */
	/**
	 * @return Returns the mobile.
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile The mobile to set.
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return Returns the nation.
	 */
	public String getNation() {
		return nation;
	}
	/**
	 * @param nation The nation to set.
	 */
	public void setNation(String nation) {
		this.nation = nation;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the postCode.
	 */
	public String getPostCode() {
		return postCode;
	}
	/**
	 * @param postCode The postCode to set.
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	/**
	 * @return Returns the regDate.
	 */
	public Timestamp getRegDate() {
		return regDate;
	}
	/**
	 * @param regDate The regDate to set.
	 */
	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}
	/**
	 * @return Returns the remark.
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark The remark to set.
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return Returns the sex.
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex The sex to set.
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return Returns the tel.
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel The tel to set.
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Set getRoles() {
		return roles;
	}
	public void setRoles(Set roles) {
		this.roles = roles;
	}

}
