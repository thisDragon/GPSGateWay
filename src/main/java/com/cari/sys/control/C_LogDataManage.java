package com.cari.sys.control;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.analog.data.util.DateUtils;
import com.cari.rbac.LogData;
import com.cari.rbac.LogDataManage;
import com.cari.web.comm.ListPage;
import com.cari.web.exception.SessionNotOpenException;
import com.cari.web.util.HttpParamCaster;
import com.cari.web.util.ServletUtil;

import jodd.util.StringUtil;

/**
* @ClassName: C_DataSourceForwardManage
* @Description: 转发订阅模块
* @author yangjianlong
* @date 2020年2月11日下午4:07:59
*
 */

public class C_LogDataManage extends HttpServlet {
	
	private static final Logger logger = LoggerFactory.getLogger(C_LogDataManage.class);
	private static final long serialVersionUID = 1L;
	
	private LogDataManage logDataManage;
	
	public C_LogDataManage() {
		super();
		logDataManage = logDataManage.getInstance();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("act");
		
		if ("toExport".equalsIgnoreCase(action)) {
			Integer logType = 0;
			String logTypeStr = HttpParamCaster.getUTF8Parameter(request, "logType", "");
			logType = StringUtil.isEmpty(logTypeStr)?null:Integer.parseInt(logTypeStr);
			String sourceType = HttpParamCaster.getUTF8Parameter(request, "sourceType", "");
			Integer state = 1;
			String stateStr = HttpParamCaster.getUTF8Parameter(request, "state", "");
			state= StringUtil.isEmpty(stateStr)?null:Integer.parseInt(stateStr);
			String startTime = HttpParamCaster.getUTF8Parameter(request, "startTime", "");
			String endTime = HttpParamCaster.getUTF8Parameter(request, "endTime", "");
			String str = "导出成功！";
			String fileName = DateUtils.date2Str(new Date(), DateUtils.NOT_FORMAT)+".xls";
        	boolean success = false;
        	
        	String[] headers = new String[]{"序号","数据类型","日志类型","日志状态","创建时间","内容"};  
	        try {
	        	String excelName = "数据日志";
	            OutputStream out = new FileOutputStream("D:\\img\\"+fileName);
	            
	            ListPage listPage = logDataManage.getList(1,10000,logType, sourceType, state, startTime, endTime);
	            List<LogData> logDataList = listPage.getDataList();
	            exportExcel(excelName,headers,logDataList , out);  
	            out.close();
	            success = true;
	        } catch (FileNotFoundException e) {  
	                e.printStackTrace();  
	        } catch (IOException e) {  
	                e.printStackTrace();  
	        }
        	
        	ServletUtil.outputXML(response, ServletUtil.createStandardResponse(success, str+";"+fileName));
		} else{
			return;
		}
	}
	
	protected void exportExcel(String title,String[] headers,List<LogData> logDataList,OutputStream out){  
        //声明一个工作簿  
        HSSFWorkbook workbook = new HSSFWorkbook();  
        //生成一个表格  
        HSSFSheet sheet = workbook.createSheet(title);  
        //设置表格默认列宽度为15个字符  
        sheet.setDefaultColumnWidth(20);  
        //生成一个样式，用来设置标题样式  
        HSSFCellStyle style = workbook.createCellStyle();  
        //设置这些样式  
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        //生成一个字体  
        HSSFFont font = workbook.createFont();  
        font.setColor(HSSFColor.VIOLET.index);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        //把字体应用到当前的样式  
        style.setFont(font);  
        // 生成并设置另一个样式,用于设置内容样式  
        HSSFCellStyle style2 = workbook.createCellStyle();  
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);  
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
        HSSFFont font2 = workbook.createFont();  
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font2);  
        //产生表格标题行  
        HSSFRow row = sheet.createRow(0);  
        for(int i = 0; i<headers.length;i++){  
            HSSFCell cell = row.createCell(i);  
            cell.setCellStyle(style);  
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
            cell.setCellValue(text);  
        }
        
        for (int i=0 ; i < logDataList.size() ; i++) {
        	LogData logData = logDataList.get(i);  
            row = sheet.createRow(i+1);  
            int j = 0;  
            row.createCell(j++).setCellValue(String.valueOf(i));
            if (logData.getSourceType() == null) {
            	row.createCell(j++).setCellValue("");
			}else{
				row.createCell(j++).setCellValue(logData.getSourceType());
			}
           
            row.createCell(j++).setCellValue(getLogTypeDesc(logData.getLogType()));
            row.createCell(j++).setCellValue(getState(logData.getState()));
            row.createCell(j++).setCellValue(String.valueOf(DateUtils.date2Str(new Date(logData.getCreateTime().getTime()), DateUtils.NORMAL_FORMAT)));
            row.createCell(j++).setCellValue(String.valueOf(logData.getContent()));
            
        }  
        try {  
            workbook.write(out);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
	
	private String getLogTypeDesc(Integer logType){
		String logTypeDesc = "";
		switch(logType){
			case 0:
				logTypeDesc = "上报日志";
			break;
			case 1:
				logTypeDesc = "订阅日志";
			break;
			case 2:
				logTypeDesc = "查询日志";
			break;
			default:
				logTypeDesc = "无效类型";
			break;
		}
		
		return logTypeDesc;
	}
	
	private String getState(Integer state){
		String stateDesc = "";
		switch(state){
			case 0:
				stateDesc = "转发失败";
			break;
			case 1:
				stateDesc = "转发成功";
			break;
			case -1:
				stateDesc = "未参与转发";
			break;
			default:
				stateDesc = "无效类型";
			break;
		}
		return stateDesc;
	}
	
	public void init() throws ServletException {
	}
}
