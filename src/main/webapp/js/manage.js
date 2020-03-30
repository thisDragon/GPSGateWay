//同步调用器
function SyncCaller(sUrl) {
	var xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
	var oResult = new Object();
	xmlDoc.async = false;
	
	this.isError = false;
	this.error = "";
	this.content = "";
	this.call = function(sUrl){
		if (!xmlDoc.load(sUrl)){
			//调用过程出错
			oResult.isError = true;
			oResult.error = xmlDoc.parseError.reason;
			oResult.content = "";
		}else{
			var xmlNode = xmlDoc.documentElement;
			//XML格式错误
			if (xmlNode == null){
				oResult.isError = true;
				oResult.error = "XML结果无效！\n" + xmlDoc.xml;
				oResult.content = "";
			}else{
				//返回的是错误信息
				if (xmlNode.selectSingleNode("Return").text == "0"){
					oResult.isError = true;
					oResult.error = xmlNode.selectSingleNode("Error").text;
					oResult.content = "";
				}else{
					//返回的是正确信息
					oResult.isError = false;
					oResult.error = "";
					oResult.content = xmlNode.selectSingleNode("Content");
				}
			}
		}
		this.isError = oResult.isError;
		this.error = oResult.error;
		this.content = oResult.content;
	}
}

//AJAX异步调用器
function AjaxCaller(sMethod) {
	var httpRequest = null;
	try{
		httpRequest = new ActiveXObject("Msxml2.XMLHTTP");
	}catch(e){
		httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	var sCallbackFunction = "";
	var vCallbackFunctionArgument = new Array();
			
	this.setURL = function(sURL) {
		if (sMethod == null) sMethod = "POST";
		httpRequest.open(sMethod, sURL, true);
	}
	
	this.setHeader = function(sHeaderName, sHeaderValue) {
		httpRequest.setRequestHeader(sHeaderName, sHeaderValue);
	}
	
	this.setCallback = function(sFunctionName) {
		sCallbackFunction = sFunctionName;
	}
	
	this.setCallbackArgs = function(sFunctionArgument) {
		vCallbackFunctionArgument[vCallbackFunctionArgument.length] = sFunctionArgument;
	}

	this.send = function(data) {
		httpRequest.onreadystatechange = this.response;
		httpRequest.send(data);
	}
	
	this.getResponseXML = function() {
		return httpRequest.responseXML;
	}

	this.getResponseHTML = function() {
		return httpRequest.responseHTML;
	}

	this.response = function() {
		if (httpRequest) {
			if (httpRequest.readystate == 4) {
				if (httpRequest.status == 200) {
					if (sCallbackFunction != "") {
						eval(sCallbackFunction + "(httpRequest.responseXML,vCallbackFunctionArgument)");
					}
				}
			}
		}
	}
}

//XML解析器
function XMLParser(xml) {
	try{
		var root = xml.selectSingleNode("root");
		var status = root.selectSingleNode("Return").text;
		if (status == "1"){
			this.isError = false;
			this.error = "";
			this.content = root.selectSingleNode("Content");
		}else{
			this.isError = true;
			this.error = root.selectSingleNode("Error").text;
			this.content = "";
		}
	}catch (e){
		try {
			var xmldom = new ActiveXObject("Microsoft.XMLDOM");
			xmldom.loadXML(xml);
			var root = xmldom.selectSingleNode("root");
			var status = root.selectSingleNode("Return").text;
			if (status == "1"){
				this.isError = false;
				this.error = "";
				this.content = root.selectSingleNode("Content");
			}else{
				this.isError = true;
				this.error = root.selectSingleNode("Error").text;
				this.content = "";
			}
		}catch(e2){
			this.isError = true;
			this.error = "读取XML信息出错，错误信息：" + e2.toString();
			this.content = "";
		}
	}
}