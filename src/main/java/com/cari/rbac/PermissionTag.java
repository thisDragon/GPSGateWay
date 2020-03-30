/**
 * 
 */
package com.cari.rbac;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.cari.rbac.RBACPermission;
import com.cari.sys.bean.SysUser;

/**
 * @author linliangyi@caripower
 * 2006.07.04
 */
public class PermissionTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5357399722954554280L;

	/**
	 * 系统模块ID 
	 */
	private String module_id;
	
	/**
	 * 系统模块ID 
	 */
	private String operator_id;
	
	public PermissionTag() {
		super();
	}

	public String getModule_id() {
		return module_id;
	}

	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}

	public String getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	/**
	 * 重载父类方法
	 * 进行鉴权和内容输出
	 */
	public final int doStartTag() throws JspException{
		HttpSession session = pageContext.getSession();
		//获取用户sessionBean
		SysUser user = (SysUser)session.getAttribute("LOGINUSER");
		String[] operator_ids = operator_id.split(",");
		for(int i = 0 ; i < operator_ids.length ; i++){
			if(RBACPermission.checkPermission(user.getRoles() , module_id , operator_ids[i])){
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
    public final int doEndTag() throws JspException {
    	 return EVAL_PAGE;
    }

}
