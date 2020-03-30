<%@ page contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,
	com.cari.sys.bean.*,
	com.cari.rbac.*,com.cari.web.util.DataFormater,
	com.cari.sys.biz.SystemConstant"%>
<%@ taglib uri="/WEB-INF/permission_tag.tld" prefix="wing" %>
	
<%
	List moduleList = (List)request.getAttribute("RIGHTMANAGE:MODULELIST");
	String roleId = request.getParameter("role_id");
	String sTitle = "权限配置";
	if (roleId != null){
		sTitle = RoleManage.getRoleNameById(roleId);
	}
	SysUser user = (SysUser)request.getSession().getAttribute("LOGINUSER");
%>
<html>
<head>
    <title>权限管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@include file="/HeadInclude.jsp"%>   
    <link rel="stylesheet" type="text/css" href="css/treestyle.css">
	<script src="js/treeLevel.js"></script>
	<script>
		var ajaxAction = "";
		var opStatus = false;
		//树节点电击回调函数
		function linkToRight(id , moduleNameStr){
			var type = document.getElementById(id).getAttribute("class");
			if(type == "node"){
				return false;
			}else{
				refreshRights();
				var ajaxForm = document.getElementById('rightForm');
				ajaxForm.module_id.value = id;
				ajaxForm.act.value = "GETMODULEOPS";
				ajaxAction = "GETMODULEOPS";
				AJForm.submitForm(ajaxForm);
				var moduleName = document.getElementById("moduleName");
				moduleName.innerHTML = moduleNameStr;
			}
		}
		//AJAX提交回调函数
		function getReturnData(data , statusCode , statusMessage){
		 	if( statusCode != AJForm.STATUS['SUCCESS'] ) {
		 	 alert( statusMessage );
			 return true;
		 	}
			//获取返回信息
			if("GETMODULEOPS" == ajaxAction){
				var divObj = document.getElementById("right");
				divObj.innerHTML = data;
				refreshOpCheck();
			}else if("SETMODULERIGHTS" == ajaxAction){
				var oDomDoc = Sarissa.getDomDocument();
				oDomDoc = (new DOMParser()).parseFromString(data, "text/xml");
				
				var status = oDomDoc.selectSingleNode("/root/Return").text;
				if (status == "1"){
					alert(oDomDoc.selectSingleNode("/root/Content").text);
				}else{
					alert(oDomDoc.selectSingleNode("/root/Error").text);
				}
			}
			ajaxAction = "";
		}

		//界面按钮响应操作
		function btAction(actionName){
			switch(actionName){
				case "SAVE":
					refreshRights();
					rightForm.act.value = "SETMODULERIGHTS";
					ajaxAction = "SETMODULERIGHTS";
					//alert(rightForm.rights.value);
					AJForm.submitForm(rightForm);
					break;
					
				case "RESET":
					rightForm.act.value = "SHOWDETAIL";	
					rightForm.submit();
					break;
						
				case "<%=SystemConstant.ALLRIGHTS%>":
					//打勾所有的操作
					rightForm.rights.value = "<%=SystemConstant.ALLRIGHTS%>#";
					refreshOpCheck();
					alert("已选择所有的操作权限。");
					break;
					
				case "<%=SystemConstant.NULLRIGHTS%>":
					//消去所有操作
					rightForm.rights.value = "<%=SystemConstant.NULLRIGHTS%>#";
					refreshOpCheck();
					alert("已取消所有的操作权限。");
					break;
					
			}
		}
		//设置模块操作选择框
		function refreshOpCheck(){
			var ops = document.getElementsByName("operation");
			var right_value = getNowModuleRight();
			//找到了当前模块的权限配置
			if(right_value > -1){
				for(var i = 0 ; i < ops.length ; i+=1){
					//alert(right_value + "**" + ops[i].value + "**" + (right_value&parseInt(ops[i].value)));
					if(right_value&parseInt(ops[i].value)){
						ops[i].checked = true;
					}
				}			
			}else if(rightForm.rights.value.indexOf("<%=SystemConstant.ALLRIGHTS%>#")>-1){
				for(var i = 0 ; i < ops.length ; i+=1){
					ops[i].checked = true;
				}			
			}else if(rightForm.rights.value.indexOf("<%=SystemConstant.NULLRIGHTS%>#")>-1){
				for(var i = 0 ; i < ops.length ; i+=1){
					ops[i].checked = false;
				}			
			}
		}
		
		//获取当前模块的新配置
		function getNowModuleRight(){
			//拆分权限字段,查找相应的模块权限值
			var rightsStr = rightForm.rights.value;
			var arRights = rightsStr.split("#");
			for(var i = 0 ; i < arRights.length ; i+=1){
				var tmp = arRights[i].split(".");
				if(tmp[0] == rightForm.module_id.value){
					return parseInt(tmp[1]);
				}
			}
			//没有当前模块的新配置
			return -1;
		}
		//更新rights域值
		function refreshRights(){
			if(opStatus){
				//拆分权限字段,查找相应的模块权限
				var rightsStr = rightForm.rights.value;
				var arRights = rightsStr.split("#");
				var found = false;
				for(var i = 0 ; i < arRights.length - 1; i+=1){
					var tmp = arRights[i].split(".");
					if(tmp[0] == rightForm.module_id.value){
						arRights[i] = tmp[0]+"."+calcOperatValue();
						found = true;
					}
				}
				
				if(found){
					var tmpRights = "";
					for(var i = 0 ; i < arRights.length-1; i+=1){
						tmpRights += arRights[i]+"#";
					}
					rightsStr = tmpRights;
					
				}else{//如果没有找到，要新增
					var op_value = calcOperatValue();
					var tmpRights = rightForm.module_id.value
							+ "."
							+ op_value + "#";
					if(rightsStr == "<%=SystemConstant.NULLRIGHTS%>#"){
						if(op_value > 0){
							rightsStr += tmpRights;
						}
					}else{
						rightsStr += tmpRights;
					}				
				}
				rightForm.rights.value = rightsStr;
				
				opStatus = false;
			}
						
		}
		//计算模块操作权值
		function calcOperatValue(){
			var operate_value = 0;
			var ops = document.getElementsByName("operation");
			for(var i = 0 ; i < ops.length ; i+=1){
				if(ops[i].checked){
					operate_value += parseInt(ops[i].value);
				}
			}	
			return operate_value;		
		}

	</script>
</head>

<body>
<form name="rightForm" method="post" action="manage/right" class="formMargin1" onsubmit="ajform:getReturnData">
	<input type=hidden name="act"> 
	<input type=hidden name="role_id" value="<%=DataFormater.noNullValue(request.getParameter("role_id"))%>">
	<input type=hidden name="module_id">
	<input type=hidden name="rights">
	
	<br>
  		<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabM">
		<tr>
			<td style="background-image:url(images/manage/conKa02.gif);" class="conKa02">
				<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="4" height="23" style="background-image:url(images/manage/conTitle01.jpg);" class="title01"></td>
					<td width="150" style="background-image:url(images/manage/conTitle02.jpg);" class="title02"><%=sTitle %></td>
					<td width="12" style="background-image:url(images/manage/conTitle03.jpg);"  class="title03"></td>
				</tr>
				</table>
			</td>
		</tr>
		</table>
		
		<TABLE width="90%" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#a3d7ef" style="BORDER-COLLAPSE:collapse;border-top-width:0px">
		<tr>
			<td class="toolH" colspan="9" style="border-top-width:0px">
				<table bordr=0 width="100%">
				<tr>
					<td width="40%">
						<wing:permission module_id="SYS-RIGHT" operator_id="modify">
						<input type="button" class="contentBtn" style="background-image:url(images/manage/contentBtn.gif);" onClick="btAction('SAVE')" value="保 存">
						<input type="button" class="contentBtn4" style="background-image:url(images/manage/contentBtn4.gif);" onClick="btAction('<%=SystemConstant.ALLRIGHTS%>')" value="全选权限">
						<input type="button" class="contentBtn4" style="background-image:url(images/manage/contentBtn4.gif);" onClick="btAction('<%=SystemConstant.NULLRIGHTS%>')" value="清空权限">
						</wing:permission>
					    <wing:permission module_id="SYS-RIGHT" operator_id="view">
						<input type="button" class="contentBtn" style="background-image:url(images/manage/contentBtn.gif);" onClick="btAction('RESET')" value="重 置">
						</wing:permission>
	    			</td>
				</tr>
				</table>
			</td>
		</tr>
    	<TR height="24" align="center">
    	<td><br>
    		<table height="100%"  border="0" align="center" cellpadding="0" cellspacing="0" >
        	<tr>
				<td width="350" align="center" valign="top">
                    <table width="90%" align="center" height="21"  border="0" cellpadding="0" cellspacing="0" >
                    <tr>
                        <td height="23"  class="conKa03" style="background-image:url(images/manage/conKa03.gif);" >功能模块列表</td>
                     </tr>
                  	</table>
                    <table width="90%" align="center" border="0" cellpadding="0" cellspacing="0" class="tableBk" >
                    <tr>
                    	<td width="20">&nbsp;</td>
                        <td width="95%" height="350"  valign="top" class="qxInput" id="qxListTd1">
							 <div id="mainDiv" style="overflow:auto;height:100%; width:100%;" >
								<p id="<%=Module.ROOT.getModuleID()%>"></p>
							    <script>
					<%
					    for(Iterator it = moduleList.iterator(); it.hasNext(); ){
					        Module module = (Module)it.next();
					        String userId = user.getUserId();
					        String moduleId = module.getModuleID();
					        //System.err.println(module.getModuleName()+"-----"+module.getModuleID());
					        //System.err.println(userId+"-----"+userId.equals("admin"));
					        if(!userId.equals("admin") && (moduleId.equals("SYS-CFG") || moduleId.equals("SYS-DICT")) || moduleId.equals("SYS-AREA")){
					        	continue;
					        }
						    int index= module.getModuleID().lastIndexOf('-');
					        String parent_id = "";
					        if (index == -1){ 
					           parent_id = Module.ROOT.getModuleID();
					        }else{
					           parent_id = module.getModuleID().substring(0,index);
					        }
					%>
						        appendNode('<%=module.getModuleID()%>',
						                   '<%=parent_id%>',
						                   '<%=module.getModuleName()%>',
						                   'javascript:linkToRight("<%=module.getModuleID()%>" , "<%=module.getModuleName()%>")');
					<%
						}//   end for
					%>
	 							switchSubNodes("<%=Module.ROOT.getModuleID()%>","none");
	      						</script>
							</div>
						</td>
					</tr>
                  	</table>
                </td>
                <td width="50">&nbsp;</td>
                <td width="350" align="left" valign="top">
                    <table width="90%" align="center" height="21"  border="0" cellpadding="0" cellspacing="0" >
                    <tr>
                        <td height="23" align="left" class="conKa03" style="background-image:url(images/manage/conKa03.gif);" >权限列表：<span id="moduleName"></span></td>
                    </tr>
                  	</table>
                    <table width="90%" align="center" border="0" cellpadding="0" cellspacing="0" class="tableBk" >
                    <tr>
                        <td height="350" valign="top"  class="qxInput" id="qxListTd1">
	                    <table width="209" border="0" cellpadding="0" cellspacing="0" >
	                      <tr>
	                      	<td width="20">&nbsp;</td>
	                      </tr>	
	                      <tr>
	                      	<td width="20">&nbsp;</td>
	                      	<td id="right"  style="line-height:2pt;"></td>
					  	  </tr>
					  	</table>  	
					  	</td>
					</tr>
					</table>  
                </td>
            </tr>
            </table>
            <br><br>
       </td>
	</tr>
	</table>
</form>
</body>
</html>