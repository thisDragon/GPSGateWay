/**
 * 
 */
package com.cari.web.util;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author fuqm
 *
 */
public class MsgXMLBuilder {
	public static final String ENCODING = "utf-8";		
	public static Document buildModuleOperation(List list){
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(ENCODING);
		Element eleRoot = doc.addElement("root");
		Element eleReturn = eleRoot.addElement("Return");		
		if (list != null && list.size() > 0) {
			eleReturn.addText("1");
		} else {
			eleReturn.addText("0");
//			System.out.println(doc.asXML());
			return doc;
		}
		Element eleContent = eleRoot.addElement("Content");				
		Element eleModuleOperation = null;	
		for (Iterator it = list.iterator();it.hasNext();){
			Object[] obj = (Object[]) it.next();
			eleModuleOperation = eleContent.addElement("Operation");
			eleModuleOperation.addElement("ModuleId").addText(DataFormater.noNullValue(obj[0]));
			eleModuleOperation.addElement("OperationId").addText(DataFormater.noNullValue(obj[1]));
		}
		list.clear();
		list = null;
//		System.out.println(doc.asXML());
		return doc;
	}
}
