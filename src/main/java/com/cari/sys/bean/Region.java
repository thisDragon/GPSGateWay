package com.cari.sys.bean;

public class Region implements Comparable{

	public static final Region REGION_ROOT = new Region();
	static{
		REGION_ROOT.setDwKey("REGION_ROOT");
		REGION_ROOT.setFullName("行政区域配置");
		REGION_ROOT.setOrderNo("");
	}
	//DWKEY           VARCHAR2(12) not null,
	private String dwKey;
	//CITYCODE        VARCHAR2(20),
	private String cityCode;
	//PROVINCE        VARCHAR2(20),
	private String province = "";
	//CITY            VARCHAR2(20),
	private String city = "";
	//COUNTY          VARCHAR2(20),
	private String county = "";
	//ACTIVE          CHAR(1) default 'N',
	private String active = "N";
	//ORDERNO         VARCHAR2(10),
	private String orderNo;
	//CENTERX         NUMBER(20,8),
	private double centerX;
	//CENTERY         NUMBER(20,8),
	private double centerY;
	//LEFT			  NUMBER(20,8)
	private double left;
	//RIGHT			  NUMBER(20,8)
	private double right;
	//TOP			  NUMBER(20,8)
	private double top;
	//BOTTOM		  NUMBER(20,8)
	private double bottom;
	//DEFAULTLAYER    NUMBER(2)
	private int defaultLayer;
	//区域全称
	private StringBuffer fullName;

	private double centerDistance = 99999.0;
	
    /**
     * 取得最小级别的行政地区名称
     * @return
     */
    public String getName() {
    	String name = "";
        if (is_County()) {
        	name = this.county;
        } else if (is_City()) {
        	name = this.city;
        } else if (is_Province()) {
        	name = this.province;
        }
        
        return name;
    }
    /**
     * 取得行政地区全名
     * @return
     */
    public String getFullName() {
    	if(fullName == null){
    		fullName = new StringBuffer();
            if (province != null && !province.equals("")) {
                fullName.append(province);
            }
            if (city != null && !city.equals("")) {
                fullName.append("-");
                fullName.append(city);
            }
            if (county != null && !county.equals("")) {
                fullName.append("-");
                fullName.append(county);
            }
    	}
        return fullName.toString();
    }
    
    
    /**
     * 根据fullName初始化Bean
     * @param fullName
     */
    public void setFullName(String fullName) {
    	if (fullName == null) {
    		return;
    	}
    	if ("REGION_ROOT".equals(this.dwKey)){
    		this.fullName = new StringBuffer();
    		this.fullName.append(fullName);
    		return;
    	}
    	String[] segs = fullName.split("-");
    	for(int i = 0 ; i < segs.length; i++) {
            if (i == 0 ) {
            	setProvince(segs[0]);
            } else if (i == 1) {
            	setCity(segs[1]);
            } else if (i == 2) {
                setCounty(segs[2]);
            }
        }
    }
    
    /**
     * 取得上级行政区划名称
     * @return
     */
    public String getParentFullName() {
    	String parentFullName = "";
        if (is_County() || is_City()) {
        	parentFullName = fullName.substring(0 , fullName.lastIndexOf("-"));
        } else if (is_Province()) {
        	parentFullName = REGION_ROOT.getFullName();
        }
        return parentFullName;
    }
    
	/**
	 * @return 返回 active。

	 */
	public String getActive() {
		return active;
	}
	/**
	 * @param active 要设置的 active。

	 */
	public void setActive(String active) {
		this.active = active;
	}
	
	/**
	 * @return 返回 centerX。

	 */
	public double getCenterX() {
		return centerX;
	}
	/**
	 * @param centerX 要设置的 centerX。

	 */
	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}
	/**
	 * @return 返回 centerY。

	 */
	public double getCenterY() {
		return centerY;
	}
	/**
	 * @param centerY 要设置的 centerY。

	 */
	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}
	/**
	 * @return 返回 city。

	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city 要设置的 city。

	 */
	public void setCity(String city) {
		if(city == null){
			this.city = "";
		}else{
			this.city = city;
		}
	}
	/**
	 * @return 返回 county。

	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county 要设置的 county。

	 */
	public void setCounty(String county) {
		if(county == null){
			this.county = "";
		}else{
			this.county = county;
		}
	}
	
	/**
	 * @return 返回 dwKey。

	 */
	public String getDwKey() {
		return dwKey;
	}
	/**
	 * @param dwKey 要设置的 dwKey。

	 */
	public void setDwKey(String dwKey) {
		this.dwKey = dwKey;
	}
	
	/**
	 * @return 返回 orderNo。

	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo 要设置的 orderNo。

	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return 返回 province。

	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province 要设置的 province。

	 */
	public void setProvince(String province) {
		if(province == null){
			this.province = "";
		}else{
			this.province = province;
		}
	}
	/**
	 * @return 返回 默认层级。

	 */
	public int getDefaultLayer() {
		return defaultLayer;
	}
	/**
	 * @param scale 要设置的 默认层级。

	 */
	public void setDefaultLayer(int defaultLayer) {
		this.defaultLayer = defaultLayer;
	}
	/**
	 * @return 返回 城市代码。

	 */
	public String getCityCode() {
		return cityCode;
	}
	/**
	 * @param smMap 要设置的 城市代码。

	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
    /**
     * 判断该Bean是否为省份


     * @return
     */
    public boolean is_Province() {
        return (!province.equals("") && city.equals("") && county.equals(""));
    }
    /**
     * 判断该Bean是否为城市


     * @return
     */
    public boolean is_City() {
        return (!city.equals("") && !is_County());
    }
    /**
     * 判断该Bean是否为县城


     * @return
     */
    public boolean is_County() {
        return !county.equals("");
    }
    
	public double getBottom() {
		return bottom;
	}
	public void setBottom(double bottom) {
		this.bottom = bottom;
	}
	public double getLeft() {
		return left;
	}
	public void setLeft(double left) {
		this.left = left;
	}
	public double getRight() {
		return right;
	}
	public void setRight(double right) {
		this.right = right;
	}
	public double getTop() {
		return top;
	}
	public void setTop(double top) {
		this.top = top;
	}
	public double getCenterDistance() {
		return centerDistance;
	}
	public void setCenterDistance(double centerDistance) {
		this.centerDistance = centerDistance;
	}
	
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		if (!(arg0 instanceof Region)){
			throw new ClassCastException("It is not Region Class.");
		}
		if (this.cityCode.equals(((Region)arg0).cityCode)){
			return 0;
		}else{
			if (this.centerDistance < ((Region)arg0).centerDistance){
				return -1;
			}else{
				return 1;
			}
		}
	}
	
	public boolean equals(Object arg0){
		if (this == arg0){
			return true;
		}else{
			if (arg0 instanceof Region){
				if (this.cityCode.equals(((Region)arg0).cityCode)){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
	}
	
	public int hashCode(){
		return this.cityCode.hashCode();
	}
}
