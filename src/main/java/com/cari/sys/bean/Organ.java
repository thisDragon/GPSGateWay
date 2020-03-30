package com.cari.sys.bean;

public class Organ {
	
	public static final Organ ROOT_ORGAN = new Organ();
	static{
		ROOT_ORGAN.setOrganID("OrganEntity.ROOT");
		ROOT_ORGAN.setOrganCode("");
		ROOT_ORGAN.setOrganDesc("");
		ROOT_ORGAN.setOrganLevel(0);
		ROOT_ORGAN.setOrganName("组织机构配置");
		ROOT_ORGAN.setOrganSort(0);
	}

	//ORGAN_ID    VARCHAR2(100) Primary Key Not Null,,
	private String organID;
	//ORGAN_NAME  VARCHAR2(100) not null,
	private String organName;
	//ORGAN_CODE  VARCHAR2(20), --根机构编号(全宗号)
	private String organCode;
	//ORGAN_LEVEL INTEGER not null,
	private int organLevel;
	//ORGAN_SORT  INTEGER not null,
	private int organSort;
	//ORGAN_DESC  VARCHAR2(200)
	private String organDesc;
	
	public Organ() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根

	}

	/**
	 * @return 返回 organCode。
	 */
	public String getOrganCode() {
		return organCode;
	}

	/**
	 * @param organCode 要设置的 organCode。
	 */
	public void setOrganCode(String organCode) {
		this.organCode = organCode;
	}

	/**
	 * @return 返回 organDesc。
	 */
	public String getOrganDesc() {
		return organDesc;
	}

	/**
	 * @param organDesc 要设置的 organDesc。
	 */
	public void setOrganDesc(String organDesc) {
		this.organDesc = organDesc;
	}

	/**
	 * @return 返回 organID。
	 */
	public String getOrganID() {
		return organID;
	}

	/**
	 * @param organID 要设置的 organID。
	 */
	public void setOrganID(String organID) {
		this.organID = organID;
	}

	/**
	 * @return 返回 organLevel。
	 */
	public int getOrganLevel() {
		return organLevel;
	}

	/**
	 * @param organLevel 要设置的 organLevel。
	 */
	public void setOrganLevel(int organLevel) {
		this.organLevel = organLevel;
	}

	/**
	 * @return 返回 organName。
	 */
	public String getOrganName() {
		return organName;
	}

	/**
	 * @param organName 要设置的 organName。
	 */
	public void setOrganName(String organName) {
		this.organName = organName;
	}

	/**
	 * @return 返回 organSort。
	 */
	public int getOrganSort() {
		return organSort;
	}

	/**
	 * @param organSort 要设置的 organSort。
	 */
	public void setOrganSort(int organSort) {
		this.organSort = organSort;
	}
	
	/**
	 * 获取父节点ID编号
	 */
	public String getParentOrganID(){
		return Organ.getParentOrganID(this.organID);
	}
	
	/**
	 * 获取父机构ID编号
	 * @param organID 子机构ID
	 * @return
	 */
	public static String getParentOrganID(String organID){
		String parentOrganID = "";
		int lastSplitPos = organID.lastIndexOf('-');
		if(lastSplitPos >0){
			parentOrganID = organID.substring(0,lastSplitPos);
		}else{
			parentOrganID = ROOT_ORGAN.getOrganID();
		}
		return parentOrganID;
	}
	
}
