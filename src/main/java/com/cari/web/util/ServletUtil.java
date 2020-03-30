package com.cari.web.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @author richfans
 * Servlet请求的公共处理方法
 */
public class ServletUtil {
	public static final String ENCODING = "UTF-8";		//字符集名称
	public static final int DEFAULT_PAGE_COUNT = 20;	//数据分页的默认每页条目数
	public static final int SUCCESS_CODE = 1;			//成功标志
    
    public static Document createStandardResponse(boolean isSuccess, String infoText){
    	return createStandardDoc(isSuccess ? 1 : 0, infoText);
    }
    
    public static Document createStandardResponse(int code, String infoText){
    	return createStandardDoc(code, infoText);
    }

    private static Document createStandardDoc(int code, String infoText){
    	Document doc = DocumentHelper.createDocument();
    	Element root = doc.addElement("root");
    	root.addElement("Return").setText(Integer.toString(code));
    	if (SUCCESS_CODE == code){
    		root.addElement("Content").addCDATA(infoText);
    	}else{
    		root.addElement("Error").addCDATA(infoText);
    	}
    	return doc;
    	
    }
    
	/**
	 * 输出XML信息
	 * @param response
	 * @param xmlStr
	 * @throws IOException
	 */
	public static void outputXML(HttpServletResponse response, String xmlStr)
			throws IOException {
		if (xmlStr == null) {
			return;
		}
		response.setContentType("text/xml;charset=" + ENCODING);
		PrintWriter out = response.getWriter();
		out.write("<?xml version='1.0' encoding='" + ENCODING + "'?>");
		out.println(xmlStr);
		out.close();
	}
	
	/**
	 * 输出HTML文本
	 * @param response
	 * @param sContent
	 */
	public static void outputHTML(HttpServletResponse response, String sContent)
			throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(sContent);
		out.flush();
		out.close();
	}
	
	/**
	 * 输出XML信息
	 * @param response
	 * @param xmlDoc
	 * @throws IOException
	 */
    public static void outputXML(HttpServletResponse response, Document xmlDoc)
    		throws IOException {
    	outputXML(response, xmlDoc, ENCODING);
    }
    
    /**
	 * 输出XML信息
	 * @param response
	 * @param xmlDoc
	 * @throws IOException
	 */
    public static void outputXML(HttpServletResponse response, Document xmlDoc, String encoding)
    		throws IOException {
    	if (xmlDoc == null) {
    		return;
    	}
    	response.setContentType("text/xml;charset=" + encoding);
    	ServletOutputStream out = response.getOutputStream();
    	writeXMlResponse(out, xmlDoc, encoding);
    }
    
    /**
     * 输出XML文档
     * @param out 输出流
     * @param xmlDoc XML文档
     * @param encoding 输出编码格式
     * @throws IOException IO异常
     */
    public static void writeXMlResponse(OutputStream out, Document xmlDoc, 
    			String encoding) throws IOException {
        
        OutputFormat formatter = new OutputFormat();
        formatter.setEncoding(encoding);
        
        XMLWriter xmlWriter = null;
        try {
            xmlWriter = new XMLWriter(out , formatter);
            xmlWriter.write(xmlDoc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
