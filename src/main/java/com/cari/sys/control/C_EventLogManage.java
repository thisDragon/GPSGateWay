/*
 * 创建日期 2006-1-12
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.cari.sys.control;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cari.sql.hibernate.QueryCondition;
import com.cari.sys.bean.SysEventLog;
import com.cari.sys.bean.SysUser;
import com.cari.sys.biz.EventLogManage;
import com.cari.sys.biz.EventLogQueryCondition;
import com.cari.web.comm.ListPage;
import com.cari.web.util.HttpParamCaster;
import com.cari.web.util.ServletUtil;

/**
 * @author fuquanming
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class C_EventLogManage extends HttpServlet {

	private static final long serialVersionUID = 7099377371180413261L;

    public C_EventLogManage() {
        super();
    }

    public void destroy(){
        super.destroy(); 
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String requestAction = request.getParameter("act");
        SysUser user = (SysUser)request.getSession().getAttribute("LOGINUSER");
        EventLogManage em = new EventLogManage();
        
        if("ENTERMODULE".equals(requestAction)){
            request.getRequestDispatcher("/manage/sys/syslog.jsp").forward(request,response);
            return;
        }else if("TOQUERY".equalsIgnoreCase(requestAction)) {
            request.getSession().removeAttribute("LOGQUERYCONDITION_SESSION");
            request.getRequestDispatcher("/manage/sys/logquery.jsp").forward(request,response);
            
        } else if("QUERY".equalsIgnoreCase(requestAction)) {
        	int pageNo = HttpParamCaster.getIntParameter(request , "pageno", 1);
            String isNewQuery = HttpParamCaster.getParameter(request, "isNewQuery");
            if(isNewQuery != null) {
                request.getSession().removeAttribute("LOGQUERYCONDITION_SESSION");
            }
            QueryCondition qc = (QueryCondition)request.getSession().getAttribute("LOGQUERYCONDITION_SESSION");
            if(qc == null) {
	            qc = new EventLogQueryCondition();
	            Map parameter = request.getParameterMap();
	            qc.setParameters(parameter);
	            request.getSession().setAttribute("LOGQUERYCONDITION_SESSION" , qc);
            }
            ListPage usersPage = em.queryPage(pageNo , qc);
            
            EventLogManage.saveLog(user.getUserId(), user.getUserName(),"浏览信息", "系统日志", 
					request.getRemoteAddr(),"","成功");
            request.setAttribute("LOGMANAGE:LOGLIST" , usersPage);
            request.getRequestDispatcher("/manage/sys/sysloglist.jsp").forward(request,response);
        } else if ("VIEW".equalsIgnoreCase(requestAction)) {
            String	logId = HttpParamCaster.getParameter(request, "id");
            SysEventLog log = em.getEventLogByID(logId);
            request.getSession().setAttribute("LOGMANAGE:LOGMSG" , log);
            request.setAttribute("LOGMANAGE:LOGMSG" , log);
            request.getRequestDispatcher("/manage/sys/logform.jsp").forward(request , response);
            
       } else if ("DELLOG".equalsIgnoreCase(requestAction)) {
            String[] ids=request.getParameterValues("id");
            response.setContentType("text/html;charset=UTF-8");            
            try {
                em.deleteEventLogs(ids);
                ServletUtil.outputXML(response, ServletUtil.createStandardResponse(true , "操作成功"));
            } catch(Exception e) {
                e.printStackTrace();
        	    ServletUtil.outputXML(response, ServletUtil.createStandardResponse(false , "操作失败"));
        	}        	
       }
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occure
     */
    public void init() throws ServletException {
        // Put your code here
    }

}
