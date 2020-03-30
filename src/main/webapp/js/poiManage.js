/*
*	本脚本只被使用于后台的标点管理页面\地图名片管理页
*	creatDate:20061011 laoding
*	update:20070211	laoding 把之前的SVG地图换为raster地图
*	pageCode:utf-8
*/
/**
*	数据表行的点击动作
*	@param obj 行的tr元素
*	@param coli 该行的标记,可用于该行checkbox的定位及编号
*/

function colClick(obj,coli){
	var src = event.srcElement;
	var srcObj=event.srcElement.className;
	//if(srcObj=="row3"||srcObj=="row12"||srcObj=="row4"||srcObj=="row0"||srcObj=="row11"||srcObj=="row5")return false;
	if (src.ignored){
		return false;
	}
	var checkObj=document.getElementById("poi_"+coli);
	if(event.srcElement!=checkObj){
		checkObj.checked=checkObj.checked?false:true;
		obj.className=checkObj.checked?"colCon_click":"colCon_out";
	}else{
		obj.className=checkObj.checked?"colCon_click":"colCon_out";
	}
}
/**
*	数据表行的鼠标移出动作
*	@param obj 行的tr元素
*/
function colOut(obj){
	if(obj.className!="colCon_click")obj.className='colCon_out';	
}
/**
*	数据表行的鼠标漂浮动作
*	@param obj 行的tr元素
*/
function colOver(obj){
	if(obj.className!="colCon_click")obj.className='colCon_over';	
}
//以下定义模拟按纽的鼠标移入和移出动作
function btnOver(obj){
	obj.style.color="blue";	
}
function btnOut(obj){
	obj.style.color="#234783";	
}
/**
*	checkbox的全选及取消全选动作
*	@param isCheck 是选中(true)还是取消选中(false)
*	@param form checkbox所属的form
*	@param checkboxName 所有checkbox的统一名称
*/
function selectAllpoi(isCheck,form,checkboxName){	
	if(!form)return false;
	if(!form(checkboxName))return false;
	if(form(checkboxName).length){
		for(i=0;i<form(checkboxName).length;i++){
			if(form(checkboxName)[i].checked!=isCheck){
				form(checkboxName)[i].parentElement.parentElement.fireEvent("onclick");
			};
		}
	}else{
		if(form(checkboxName).checked!=isCheck){
			form(checkboxName).parentElement.parentElement.fireEvent("onclick");
		};
	}
	return true;
}
//隐藏页面上的所有className为isAbleToHidden的select框 act:hidden 隐藏 act:visible 显示
function showHideAllSelect(act){
	with (document.all.tags("SELECT")){
 		for (i=0; i<length; i++){
			if(item(i).className=="isAbleToHidden"){
 				item(i).style.visibility = act;
			}
		}
	}
}
/**
*	PageState 页面状态栏类
*/
function PageStateObj(x,y){
	this.bStateMessageLock=false;
	this.sMessage="";	//当前显示的字符串
	this.oShowObj=document.getElementById("pageStateMessage");	
	document.getElementById("pageStateMessage").style.left=x;
	document.getElementById("pageStateMessage").style.top=y-22;
	//设置状态栏位置
	this.setPosition=function(x,y){
		document.getElementById("pageStateMessage").style.left=x;
		document.getElementById("pageStateMessage").style.top=y-22;
	};
	/**
	*	显示状态栏
	*	@param message 显示的字符串 如果为空,显示上个状态
	*	@param action 显示状态 default(缺省):只负责显示 lock:锁定显示 quickshow:2秒短暂强行提示但不改变lock状态 unlock:解除锁定显示
	*/
	this.show=function(message,action){
		if(action==null)action="default";
		if(this.bStateMessageLock&&action!="quickshow"&&action!="unlock"){
			this.show("上一步操作尚未完成！请稍候…","quickshow");return false;
		}
		if(message!=null&&message!=""&&action!="quickshow")this.sMessage=message;
		switch(action){
			case "default":			
				this.oShowObj.innerHTML=this.sMessage;
			break;
			case "lock":
				this.oShowObj.innerHTML=this.sMessage;
				this.bStateMessageLock=true;
			break;
			case "quickshow":
				this.oShowObj.innerHTML=message;
				setTimeout(function(){PageState.show();},2000);
			break;
			case "unlock":
				this.oShowObj.innerHTML=this.sMessage;
				this.bStateMessageLock=false;
			break;
			default:
				return false;
			break;
		}
		return true;
	};
	//解除显示锁定
	this.unlock=function(){
		this.bStateMessageLock=false;
	};
}
/**
*	contextMenu	右键菜单类
*/
function contextMenu(){
	this.m_sId=null;	//菜单的唯一标志
	this.m_nX=0;	//菜单出现的位置x
	this.m_nY=0;	//菜单出现的位置y
	this.m_bIsInitialized=false;	//是否初始化标志
	this.m_oDiv=null;	//该右键菜单层元素
	/**
	*	初始化菜单
	*	@param id 菜单的唯一id
	*	@param menuX 菜单出现的位置x
	*	@param menuY 菜单出现的位置y
	*/
	this.init=function(menuId,menuX,menuY){
		this.m_sId=menuId;
		this.m_nX=menuX;
		this.m_nY=menuY;
		if(document.getElementById('contextMenu_'+this.m_sId)){//如果之前这个层就已经存在，清空之前的菜单项
			this.m_bIsInitialized=true;
			var childNode=document.getElementById("menuItem_"+this.m_sId).children(0);
			document.getElementById("menuItem_"+this.m_sId).removeChild(childNode);
			this.m_oDiv=document.getElementById("contextMenu_"+this.m_sId);
			this.setPosition(menuX,menuY);
		}else{
			var faceStr='<div style="position:absolute;z-Index:610;width:150px;left:'+this.m_nX+';top:'+this.m_nY+';display:none" id="contextMenu_'+this.m_sId+'">'
						+'<table cellpadding="0" cellspacing="0" class="ta01"><tr><td>'
						+'	<table cellpadding="0" cellspacing="1" class="ta02" id="menuItem_'+this.m_sId+'" onblur="poiArray[\'poiObj_'+this.m_sId+'\'].menuObj.showHide(\'hidden\');">'
						+'	</table>'
						+'</td></tr></table></div>';
			document.body.insertAdjacentHTML("beforeEnd",faceStr);
			this.m_oDiv=document.getElementById("contextMenu_"+this.m_sId);
			this.m_bIsInitialized=true;
		}		
		this.showHide('show');		
		return true;
	};
	//增加一条分割线
	this.addLine=function(){
		if(!this.m_bIsInitialized){alert("菜单并未被初始化");return false;}
		var menuItemContainer=document.getElementById("menuItem_"+this.m_sId);
		var newrow=menuItemContainer.insertRow(menuItemContainer.rows.length);
		var newcell=newrow.insertCell();
		newcell.setAttribute("height","2");
		newcell.setAttribute("background","images/manage/line.gif");		
	};	
	/*	添加一个菜单项
	*	@param menuName	菜单名称
	*	@param clickFunc 菜单点击所调用的函数	
	*/
	this.addItem=function(menuName,clickFunc){
		if(!this.m_bIsInitialized){alert("菜单并未被初始化");return false;}
		var menuItemContainer=document.getElementById("menuItem_"+this.m_sId);
		var newrow=menuItemContainer.insertRow(menuItemContainer.rows.length);
		var newcell=newrow.insertCell();
		var tempNo=this.m_sId;
		newcell.setAttribute("height","22");
		newcell.attachEvent("onmouseover",function(){newcell.className='ta02_bg'});
		newcell.attachEvent("onmouseout",function(){newcell.className='';isMenuClick=false;});
		newcell.attachEvent("onmousedown",function(){isMenuClick=true;return false;});
		newcell.attachEvent("onmouseup",function(){isMenuClick=false;eval(clickFunc);return false;});
		//newcell.innerHTML="<NOBR>"+menuName+"</NOBR>";
		newcell.innerHTML=menuName;
		return true;
	};
	//添加一个不可点击框（标题）
	this.addTitle=function(textValue){
		if(!this.m_bIsInitialized){alert("菜单并未被初始化");return false;}
		var menuItemContainer=document.getElementById("menuItem_"+this.m_sId);
		var newrow=menuItemContainer.insertRow(menuItemContainer.rows.length);
		var newcell=newrow.insertCell();
		newcell.setAttribute("height","22");
		newcell.attachEvent("onmouseout",function(){isMenuClick=false;});
		newcell.attachEvent("onmousedown",function(){isMenuClick=true;return false;});	//设定鼠标按下的时候是isMenuClick，这个时候不应该隐藏菜单
		newcell.attachEvent("onmouseup",function(){isMenuClick=false;return false;});	//设定鼠标按下的时候已完成isMenuClick，这个时候才隐藏菜单
		//newcell.innerHTML="<NOBR><font style='color:#555555'>"+textValue+"</font></NOBR>";
		newcell.innerHTML="<font style='color:#555555'>"+textValue+"</font>";
		return true;
	}
	//显示隐藏该菜单 接收"hidden"和"show"
	this.showHide=function(action){
		action=action=="hidden"?"none":"";
		if(action==""&&richmap.getState==4){//如果这个时候地图的标点按纽是按下状态
			richmap.setState(0);	//设置为移动状态，这样避免鼠标点菜单的时候又进行标点工作
		}
		if(action=="none"){
			if(event){//alert(event.srcElement.id);				
				if(event.srcElement.id=="menuItem_"+this.m_sId){
					var tempId=this.m_sId;
					setTimeout(function(){hiddenMenu(document.getElementById("contextMenu_"+tempId));},50);
					return false;
				}
			}
		}
		document.getElementById("contextMenu_"+this.m_sId).style.display=action;
		//this.m_oDiv.style.display=action;
	};
	//设置菜单位置
	this.setPosition=function(menuX,menuY){
		if(!this.m_bIsInitialized){alert("菜单并未被初始化");return false;}
		this.m_nX=menuX;	//菜单出现的位置x
		this.m_nY=menuY;	//菜单出现的位置y
		this.m_oDiv.style.left=menuX+"px";
		this.m_oDiv.style.top=menuY+"px";
	};
	//聚焦菜单
	this.focus=function(){
		if(!this.m_bIsInitialized){alert("菜单并未被初始化");return false;}
		this.showHide("show");
		document.getElementById("menuItem_"+this.m_sId).focus();
		//this.m_oDiv.focus();
	};	
}
//在menu失去焦点的时候隐藏菜单
var isMenuClick=false;	//失去焦点的这个动作是否是点击菜单
function hiddenMenu(obj){
	if(isMenuClick){
		setTimeout(function(){hiddenMenu(obj);},50);
	}else{
		obj.style.display='none';	//如果不是点击菜单才去隐藏菜单
	}
}
//显示和隐藏高级检索
var sSearchState="search";//定义当前的搜索类别是普通搜索
function showHideAdSearch(){
	document.getElementById("SearchCon").style.display=sSearchState=="search"?"none":"";
	document.getElementById("advanceSearchCon").style.display=sSearchState=="search"?"":"none";	
	sSearchState=sSearchState=="search"?"adSearch":"search";
	PageState.setPosition(10,sSearchState=="search"?81:149);
}
//显示&隐藏层
function showHideDiv(divId,action){
	action=action=="hidden"?"none":"";
	document.getElementById(divId).style.display=action;
}
//开始多点标注
var markRunState=false;//记录进入多点标注后,是否标点作过操作,后面的附加经纬度,清空经纬度等操作,要让这个标志置到true状态
function markStart(){
	if(pilotingRun)changePilotingRun();//默认不显示参考地标
	var poiForm=document.PoiList;
	if(!poiForm)return false;	//如果列表还未产生
	if(!poiForm.dwkey)return false;//如果搜索没有结果
	var sCitycode=getpoicitycode();		//得到当前页的城市代码
	if(sCitycode=="")return false;//如果还没城市值，说明还没查询
	clearMap();//先清空之前在地图上显示的点
	var isCheck=false;	//判定是否有一个或多个记录被选中
	if(poiForm.dwkey.length){
		for(var i=0;i<poiForm.dwkey.length;i++){
			if(poiForm.dwkey[i].checked){
				isCheck=true;
				creatPoiFromDom(parseInt(poiForm.dwkey[i].id.replace("poi_","")));//从顶部的xml中提取数据来生成该记录的poi对象
			}
		}
		if(!isCheck){alert("您没有选中任何记录！");return false;}	
	}else{
		if(poiForm.dwkey.checked){
			creatPoiFromDom(parseInt(poiForm.dwkey.id.replace("poi_","")));//从顶部的xml中提取数据来生成该记录的poi对象
		}else{
			alert("您没有选中任何记录！");return false;
		}
	}
	if(!PageState.show("初始化多点标注.."))return false;
	markRunState=false;		//设置未被操作过	
	//显示遮盖层
	document.getElementById("coverDiv").style.width=document.body.clientWidth;
	document.getElementById("coverDiv").style.height=document.body.clientHeight;
	showHideDiv("coverDiv","show");
	//显示地图
	mapWindow.show();
	richmap.setCity(sCitycode);
	//重新刷新地图窗口
	refreshMap("Y");
	//隐藏所有select元素
	showHideAllSelect("hidden");
	PageState.show("开始多点标注");
	return true;
}
//判断列表是否被选中
function checkListSelect(){
	var checkCount=0;	//多少个被选中
	var poiForm=document.PoiList;
	if(!poiForm)return 0;
	if(!poiForm.dwkey)return 0;
	if(poiForm.dwkey.length){
		for(var i=0;i<poiForm.dwkey.length;i++){
			if(poiForm.dwkey[i].checked){
				checkCount++;
			}
		}
	}else{
		if(poiForm.dwkey.checked)checkCount=1;
	}
	if(checkCount==0){alert("您没有选中任何记录！");return false;}
	return checkCount;
}
//删除poi点
function deletePoi(){
	if(checkListSelect()==0)return false;
	if(!confirm("您确定删除选定记录?"))return false;
	if(!PageState.show("删除poi...","lock"))return false;	//锁定查询
	var poiForm=document.PoiList;
	
	//检查批量粘帖的POI中是否有公交车站
	var index;
	var typeCode;
	//是否地名注记
	if(poiForm.dwkey.length){
		for(var i=0;i<poiForm.dwkey.length;i++){
			if(poiForm.dwkey[i].checked){
				index = i + 1;
			}
		}
	}else{
		index = 1;
	}
	poiForm.action="manage/poi?act=delete";
	AJForm.submitForm(poiForm);
	return false;
}
//add by xio 2007.07.03,合并那个审核、更新队列到审核
//审核:审核并更新队列
function checkAndQueue(){
	if(checkListSelect()==0)return false;
	if(!confirm("您确定审核选定记录?"))return false;

	if(!PageState.show("审核poi...","lock")) return false;	//锁定查询
	var poiForm=document.PoiList;
	var index;
	if(poiForm.dwkey.length){
		for(var i=0;i<poiForm.dwkey.length;i++){
			if(poiForm.dwkey[i].checked){
				index = i + 1;
			}
		}
	}else{
		index = 1;
	}

	/*目前批量处理的情况下，还是同意这样做吧
	if (nameLabel) {
		alert("您操作的POI中包含地名注记，请进入POI编辑页面进行该操作，操作返回，审核并更新队列未提交！");
		PageState.show("审核并更新队列操作取消，您操作的POI中包含地名注记POI！", "unlock")
		return false;
	}
	*/
	poiForm.action="manage/poi?act=CHECKLIST";
	AJForm.submitForm(poiForm);
	return true;
}
//审核poi点
function confirmPoi(){
	if(checkListSelect()==0)return false;
	if(!confirm("您确定审核选定记录?"))return false;
	if(!PageState.show("审核poi...","lock")) return false;	//锁定查询
	var poiForm=document.PoiList;
	
	//是否地名注记
	var index;
	if(poiForm.dwkey.length){
		for(var i=0;i<poiForm.dwkey.length;i++){
			if(poiForm.dwkey[i].checked){
				index = i + 1;
			}
		}
	}else{
		index = 1;
	}
	
	poiForm.action = basePath + "manage/poi?act=CHECKLIST";
	AJForm.submitForm(poiForm);
	return true;
}
//新增poi点
function addPoi(){
	self.open(basePath + "manage/poi?act=TOADD");
	return true;
}
//修改poi
function modifyPoi(){
	var asCheck=checkListSelect();
	if(asCheck==0)return false;
	if(asCheck!=1){alert("您只能选中一个进行修改");return false;}
	var modifyDwkey="";
	var modifyIndex=0;
	var poiForm=document.PoiList;
	if(poiForm.dwkey.length){
		for(var i=0;i<poiForm.dwkey.length;i++){
			if(poiForm.dwkey[i].checked){				
				modifyDwkey=poiForm.dwkey[i].value;
				modifyIndex = i + 1;
			}
		}
	}else{
		modifyDwkey=poiForm.dwkey.value;
		modifyIndex = 1;
	}
	return modifyPoiByDwKey(modifyDwkey, modifyIndex);
}
//修改poi
function modifyPoiByDwKey(dwKey, index){
	//如果没有修改权限就没有该功能
	if (!hasModifyRights) {
		return false;
	}
	self.open(basePath + "manage/poi?act=TOUPDATE&dwkey="+dwKey);
	return true;
}
//刷新列表
function refreshSearch(){
	var ajfrm=document.ajForm;
	if(ajfrm.cityName.value=="")return false;
	if(!PageState.show("刷新查询列表...","lock")) return false;	//锁定查询
	AJForm.submitForm(ajfrm);
}

//对于poilist的form,post返回后处理
var returnDom=null;
function poiListReturn(data,statusCode,statusMessage){
	if( statusCode != AJForm.STATUS['SUCCESS']){
		PageState.show(statusMessage,"unlock");	//ajform异常信息
		return false;
	}
	returnDom=(new DOMParser()).parseFromString(data,"text/xml");
	var testNode=returnDom.selectNodes("root/Return")[0].text;
	if(testNode=="0"){
		PageState.show(returnDom.selectNodes("root/Error")[0].text,"unlock");	//显示错误信息
		window.alert(returnDom.selectNodes("root/Error")[0].text);
		return false;
	}else{
		PageState.show(returnDom.selectNodes("root/Content")[0].text,"unlock");		//显示成功信息
		window.alert(returnDom.selectNodes("root/Content")[0].text);
		if(document.PoiList.act.value=="MODMORETYPECODE"){
			hideCover();
			document.getElementById("category").value="";
			document.getElementById("modType").style.display="none";
		}
	}
	//重新刷新列表
	var ajfrm=document.ajForm;
	if(!PageState.show("刷新列表...","lock")) return false;	//锁定查询
	AJForm.submitForm(ajfrm);
}
/**
*	搜索结束后处理返回的数据
*/
PoiXmlDoc=null;			//保存列表Dom树
consultDoc=null;		//保存参考地标结果的Dom树
assistantDoc=null;		//保存辅助地标结果在Dom树
var PoiXslDoc=null;
var PoiProcessor=null;
var Poi_PageNo=0;	//列表的当前页码
var Poi_TotalPage=0;	//列表的总页数
var Poi_TotalCount=0;	//列表的总记录数
var Poi_PageCount=0;
function searchBack(data,statusCode,statusMessage){
	if( statusCode != AJForm.STATUS['SUCCESS']){
		PageState.show(statusMessage,"unlock");//解除查询锁定
		return false;
	}	
	if(data==""||data==null){PageState.show("查询数据出现错误，请和管理员联系!","unlock");return false;}	
	//判断会话超时
	if(data.indexOf("会话已经超时")>0){
		PageState.show("查询失败,会话已经超时,请重新登录!","unlock");
		return false;
	}
	//把数据生成为dom对象并挂接到windows
	PoiXmlDoc = (new DOMParser()).parseFromString(data,"text/xml");
	var testNode=PoiXmlDoc.selectNodes("root/Return")[0].text;
	if(testNode=="0"){
		PageState.show(PoiXmlDoc.selectNodes("root/Error")[0].text,"unlock");return false;	//如果查询出现错误
	}
	Poi_PageNo=PoiXmlDoc.selectNodes("root/Content/PageNo")[0].text;
	Poi_TotalPage=PoiXmlDoc.selectNodes("root/Content/PageCount")[0].text;
	Poi_TotalCount=PoiXmlDoc.selectNodes("root/Content/TotalSize")[0].text;
	Poi_PageCount=PoiXmlDoc.selectNodes("root/Content/PageSize")[0].text;
	PoiProcessor = new XSLTProcessor();	
	PoiXslDoc = Sarissa.getDomDocument();
	PoiXslDoc.async = true;
	PoiXslDoc.onreadystatechange = function(){
		if(PoiXslDoc.readyState == 4){
			if(PoiXslDoc.parseError!=0){
				PageState.show("xsl载入错误！错误描述:\r\n"+Sarissa.getParseErrorText(PoiXslDoc),"unlock");
				return false;
			}
			PoiProcessor.importStylesheet(PoiXslDoc);
			var newDocument = PoiProcessor.transformToDocument(PoiXmlDoc);//解析数据
			if(newDocument.parseError!=0){
				var ErrorStr=Sarissa.getParseErrorText(newDocument);
				if(ErrorStr!=Sarissa.PARSED_OK){
					PageState.show("数据解析错误！错误描述:\r\n"+ErrorStr,"unlock");
					return false;
				}
			}
			var returnData=Sarissa.serialize(newDocument);
			returnData=returnData.replace(/#k0kk/g,"<").replace(/#k1kk/g,">");//把内容里头的checkbox解析出来
			document.getElementById("PoiListContent").innerHTML=returnData;
			if(Poi_TotalPage>0){
				showPageConsole(Poi_PageNo,Poi_TotalPage,Poi_TotalCount,Poi_PageCount,"pageConsole","poiTurnPage");
			}else{
				document.getElementById("PageConsole").innerHTML=""; //清空分页信息
			}
			document.PoiList.selectAll.checked=false;	//之前如果有选定这个全选按纽，应该清除
			PageState.show("信息读取完毕","unlock");//解除查询锁定
		}
	};
	PoiXslDoc.load("css/poiList.xsl");//载入数据样式
	return true;
}
function poiTurnPage(pageno){
	var ajfrm=document.ajForm;
	ajfrm.pageNo.value=pageno;	
	if(!PageState.show("读取分页信息...","lock")) return false;	//锁定查询
	AJForm.submitForm(ajfrm);
}
/**
*	显示分页信息
*	@param PageNo 当前第几页
*	@param TotalPage 总共多少页
*	@param TotalCount 总记录数
*	@param PageCount 每页显示多少条
*	@param showObj 显示容器
*	@param turnPageFunc 点击翻页使用的函数
*/
function showPageConsole(PageNo,TotalPage,TotalCount,PageCount,showObj,turnPageFunc){
	var firstpage = PageNo==1?"<img src='images/manage/home_1.gif'>":"<img src='images/manage/home.gif' style='cursor:pointer' onclick='" + turnPageFunc + "(1)' alt='返回首页'>";
	var uppage = PageNo==1?"<img src='images/manage/up_1.gif'>":"<img src='images/manage/up.gif' style='cursor:pointer' onclick='" + turnPageFunc + "("+(PageNo-1).toString() + ")' alt='到上一页'>";
	var downpage = PageNo==TotalPage?"<img src='images/manage/next_1.gif'>":"<img src='images/manage/next.gif' style='cursor:pointer' onclick='" + turnPageFunc + "(" + (PageNo*1+1).toString() + ")' alt='到下一页'>";
	var endpage = PageNo==TotalPage?"<img src='images/manage/end_1.gif'>":"<img src='images/manage/end.gif' style='cursor:pointer' onclick='" + turnPageFunc+"(" + TotalPage + ")' alt='到末页'>";
	var pageStr = "<table border='0' cellpadding='2' cellspacing='0'><tr><td>总<font color=red>" + TotalCount + "</font>条</td><td>当前第<font color=red>"+ PageNo + "</font>页/共<font color=red>" + TotalPage + "</font>页</td><td>"+firstpage+"</td><td>"+uppage+"</td><td>" + downpage + "</td><td>" + endpage + "</td><td> 到<input type='text' id='PageText_" + showObj + "' style='border:solid 1px #eeeeee;' size='1' maxlength='4' onKeyDown=\"checkEnter('PageBtn_" + showObj + "')\">页</td><td><img id='PageBtn_" + showObj + "' src='images/manage/pagego.gif' style='cursor:pointer;' onclick=\"PageGo(" + TotalPage + ",'" + turnPageFunc + "','" + showObj + "');\"></td></tr></table>";
	document.getElementById(showObj).innerHTML=pageStr;
}
function PageGo(nTotalPage,turnPageFunc,showObj){
	var nPageNo=document.getElementById("PageText_"+showObj);
	var sWrongStr="";
	if(nPageNo.value==""){
		sWrongStr="请输入页码！";
	}else if(nPageNo.value.toString().match(/^(?:[1-9]\d*|0)$/) == null){
		sWrongStr="请在页码中输入数字！";
	}else if(nPageNo.value<=0||nPageNo.value>nTotalPage){
		sWrongStr="您输入的页码超出范围！";
	}
	if(sWrongStr!=""){alert(sWrongStr);nPageNo.focus();return false;}
	eval(turnPageFunc + "(" + nPageNo.value + ")");
}
/**
*	普通窗口的移动类
*	@param moveDivId	被移动的层元素id
*	@param dragObjId	开始拖动的目标
*/
function moveObj(moveDivId,dragObjId){
	this.m_oObj=document.getElementById(moveDivId);
	this.m_oDragObj=document.getElementById(dragObjId);
	this.close=function(){
		this.m_oObj.style.display='none';
		if(this.bindingObj.length>0){	//如果有捆绑层,需要将捆绑层一起隐藏
			for(var i=0;i<this.bindingObj.length;i++){
				this.bindingObj[i][0].style.display='none';
			}
		}
	};
	this.getPositionX=function(){
		return parseInt(this.m_oObj.style.left);
	};
	this.getPositionY=function(){
		return parseInt(this.m_oObj.style.top);
	};
	this.setPosition=function(x,y){
		this.m_oObj.style.left=x+"px";
		this.m_oObj.style.top=y+"px";
		if(this.bindingObj.length>0){//如果存在捆绑层,则需要捆绑层一起移动
			for(var i=0;i<this.bindingObj.length;i++){
				this.bindingObj[i][0].style.left=this.getPositionX()-this.bindingObj[i][1];
				this.bindingObj[i][0].style.top=this.getPositionY()-this.bindingObj[i][2];
			}
		}
	};
	this.show=function(){
		this.m_oObj.style.display="";
		if(this.bindingObj.length>0){	//如果有捆绑层,需要将捆绑层一起显示
			for(var i=0;i<this.bindingObj.length;i++){
				this.bindingObj[i][0].style.display="";
			}
		}
	};
	this.focus=function(){		
		this.m_oObj.focus();
	};
	this.bindingObj=new Array();	//该窗口移动是否有捆绑其他层一起移动
	//增加一个捆绑层 ,传入的是该层的id
	this.addBindingObj=function(objDivId){
		var objDiv=document.getElementById(objDivId);
		var tempArr=new Array();
		tempArr.push(objDiv);
		tempArr.push(this.getPositionX()-parseInt(objDiv.style.left));
		tempArr.push(this.getPositionY()-parseInt(objDiv.style.top));
		this.bindingObj.push(tempArr);
	};
	//显示/隐藏
	this.showHide=function(){
		if(this.m_oObj.style.display==""){
			this.close();
		}else{
			this.show();
		}
	}
}
//普通搜索准备
function checkSearch(){
	var frm=document.frmSearch;
	var ajfrm=document.ajForm;
	ajfrm.reset();	//先清空之前的值
	ajfrm.pageNo.value	= 1;
	ajfrm.address.value	= frm.address.value;
	ajfrm.cityName.value= frm.cityName.value;
	ajfrm.fullName.value	= frm.fullName.value;
	ajfrm.modifier.value	= frm.modifier.value;
	ajfrm.orderBy.value		= document.frmAdSearch.orderBy.value;
	ajfrm.order.value		= document.frmAdSearch.order.value;
	if(!PageState.show("开始普通查询...","lock")) return false;	//锁定查询
	AJForm.submitForm(ajfrm);
	return false;
}
//高级搜索准备
function checkAdSearch(){
	var frm=document.frmAdSearch;
	var ajfrm=document.ajForm;
	/*
	if(!checkDate(frm.modifyDateBegin.value)){frm.modifyDateBegin.focus();return false;}
	if(!checkDate(frm.modifyDateEnd.value)){frm.modifyDateEnd.focus();return false;}
	if(!checkDate(frm.createDateBegin.value)){frm.createDateBegin.focus();return false;}
	if(!checkDate(frm.createDateEnd.value)){frm.createDateEnd.focus();return false;}
	*/
	ajfrm.pageNo.value		= 1;
	ajfrm.cityName.value	= frm.cityName.value;
	ajfrm.category.value	= frm.categoryId.value;
	ajfrm.fullName.value	= frm.fullName.value;
	ajfrm.address.value		= frm.address.value;
	ajfrm.modifier.value	= frm.modifier.value;
	ajfrm.modifyDateBegin.value	= frm.modifyDateBegin.value;	
	ajfrm.modifyDateEnd.value	= frm.modifyDateEnd.value;
	ajfrm.creator.value		= frm.creator.value;
	ajfrm.createDateBegin.value	= frm.createDateBegin.value;	
	ajfrm.createDateEnd.value	= frm.createDateEnd.value;
	ajfrm.hasLatlong.value	= frm.hasLatlong.value;
	ajfrm.confirmedFlag.value	= frm.confirmedFlag.value;
	ajfrm.orderBy.value		= frm.orderBy.value;
	ajfrm.order.value		= frm.order.value;
	ajfrm.mapName.value = frm.mapName.value;
	ajfrm.layer.value = frm.layerKey.value;
	
	if(!PageState.show("开始高级查询...","lock")) return false;	//锁定查询
	AJForm.submitForm(ajfrm);
	return false;
}
/**
 * 判断是否为整数，包含正负
 * @param str 需要判断的字符串
 */
function checkInt(str){
	if (!str){
		return true;
	}

	var intPar = /^(-|\+)?\d+$/;
	return intPar.test(str);
}
/**
*	动态更新普通查询的域和高级查询对应域的值同步
*	@param form1 同步值所在form的名称
*	@param form2 同步被更新域所在form的名称
*	@param areaName 更新域名称
*/
function synChangeArea(form1,form2,areaName){
	document.forms[form2](areaName).value=document.forms[form1](areaName).value;
}

//得到当前页面的城市代码,该函数使用到的citySourceData在public.js中
function getpoicitycode(){	
	var citynames=document.getElementById("mapCityName").value;		//得到的值类似：福建省-福州市-闽清县
	if(citynames=="")return "";
	citynames=citynames.slice(citynames.indexOf("-")+1,citynames.indexOf("市")+1);
	for(var i = 0 ; i < citySourceData.length ; i++){						
		if(citySourceData[i][1]==citynames){
			return citySourceData[i][0];
		}				
	}
	return "";
}

/**
*	在地图上显示一个定制的泡泡
*	@param dwkey	该popup的唯一标志
*	@param screenx	该点的屏幕坐标x
*	@param screeny	该点的屏幕坐标y
*	@param text	显示点的名称,如果不显示,值保留为""或null
*	@param number popup编号
*	@param display	显示样式	1:红色正常点popup,一般为原有的showlocation显示的点(缺省) 
								2:红色泡泡,一般为有经纬度的待标注点
								3:绿色旗帜,一般为城市地标
								4:蓝色旗帜,一般为辅助定位poi点
								5:红色图钉,一般为显示剪贴板经纬度
*/
function showWinPopup(dwkey,screenx,screeny,text,number,display){
	if(display==""||display==null)display=1;
	if(number==""||number==null)number=1;
	var imageUrl,imageWidth,imageHeight,imageHotX,imageHotY,foreColor;
	switch(display){
		case 1:
			imageUrl="rpop" + number + ".png";
			imageWidth=23;
			imageHeight=23;
			imageHotX=3;
			imageHotY=22;
			foreColor="red";
		break;
		case 2:
			imageUrl="rpop" + number + ".png";
			imageWidth=23;
			imageHeight=23;
			imageHotX=3;
			imageHotY=22;
			foreColor="red";
		break;
		case 3:
			imageUrl="gflag.png";
			imageWidth=12;
			imageHeight=18;
			imageHotX=0;
			imageHotY=18;
			foreColor="green";
		break;
		case 4:
			imageUrl="bflag.png";
			imageWidth=12;
			imageHeight=18;
			imageHotX=0;
			imageHotY=18;
			foreColor="blue";
		break;
		case 5:
			imageUrl="mark.png";
			imageWidth=26;
			imageHeight=29;
			imageHotX=0;
			imageHotY=29;
			foreColor="red";
		break;
		default:
		return false;
	}
	var locationObj = new wingBasePoint(dwkey,screenx,screeny);	//生成基点
	var locationPic = new wingPicObj();	//添加图片
	//locationPic.sImageSrc = "/Wing/images/api/" + imageUrl;
	//TODO delete
	locationPic.sImageSrc = basePath + "images/api/" + imageUrl;
	locationPic.nImageWidth = imageWidth;
	locationPic.nImageHeight = imageHeight;
	locationPic.nScreenX = -imageHotX;
	locationPic.nScreenY = -imageHotY;
	locationObj.addObj(locationPic);
	var locationText = new wingTextObj();	//添加文本
	locationText.sText = text;
	locationText.nScreenX = imageWidth - imageHotX + 1;
	locationText.nScreenY = -imageHotY;
	locationText.sSize = "12px";
	locationText.sColor = foreColor;
	locationText.sBorder = "solid 1px " + foreColor;
	locationText.sPadding = "2px";
	locationText.sBgColor = "#ffffff";
	locationObj.addObj(locationText);
	richmap.addWingElement(locationObj);	
	return true;
}
//页面载入动作
var mapWindow=null;	//地图窗口
var PageState=null;	//页面状态栏实例
var richmap = null;	//定义RWing对象
function pageInit(){
	//初始化状态栏并定位
	PageState=new PageStateObj(10,81);
	PageState.show("页面载入完成");
}

//更新查询结果表中城市的显示
function resetCityNameViewer(dwKey, province, city, county){
	var cityNameViewer = document.getElementById("cityNameViewer" + dwKey);
	cityNameViewer.innerText = buildCityName(province, city, county);
}

//生成完整的城市名（省-市-县区）
function buildFullCityName(province, city, county){
	var fullCityName = "";
	if (province) {
		fullCityName += province;

		if (city){
			fullCityName += "-" + city;

			if (county){
				fullCityName += "-" + county;
			}
		}
	}
	return fullCityName;
}

//生成显示城市名(显示城市，或者区县，如果区县存在则显示城市)
function buildCityName(province, city, county){
	if (county){
		return county;
	}

	if (city){
		return city;
	}

	return "";
}

//分离完整的城市名成省、市、县区
function splitCity(cityName){
	if (!cityName){
		return ["", "", ""];
	}
	
	var cityNames = cityName.split("-");
	if (cityNames.length == 1){
		return [cityNames[0], "", ""];
	}
	if (cityNames.length == 2){
		return [cityNames[0], cityNames[1], ""];
	}
	if (cityNames.length == 3){
		return [cityNames[0], cityNames[1], cityNames[2]];
	}
	return ["", "", ""];
}


//获得查询结果Dom中，指定索引位置节点、指定元素的值
function getDomNodeValue(index, nodeName){
	var nodeList = PoiXmlDoc.selectNodes("root/Content/POIList")[0].childNodes;
	return nodeList[index*1-1].selectNodes(nodeName)[0].text;
}

function toModType(){
	var frm = document.forms["PoiList"];
	if(checkListSelect()==0)return false;
	showCover();
	document.getElementById("modType").style.display="";
}

function modClose(){
	hideCover();
	document.getElementById("modType").style.display="none";
}

function showCover(){
	var showObj = document.getElementById("coverDiv1");
	//隐藏遮盖层
	showObj.style.width=document.body.clientWidth;
	showObj.style.height=document.body.clientHeight;
	showObj.style.display = "";
}
function hideCover(){
	var showObj = document.getElementById("coverDiv1");
	showObj.style.display = "none";
}

function selectMapLayer(type){
	var arg = new Object();
	var wnd = window.showModalDialog(basePath + "manage/data/selectlayer.jsp",arg,"center:yes;help:no;dialogWidth:600px;dialogHeight:380px");
	if (wnd == "1"){
		if (type == 0){
			var frm = document.frmAdSearch;
			frm.layerKey.value = arg.layerKey;
		}else{
			var frm = document.poiForm;
			frm.layerKey.value = arg.layerKey;
			frm.minScale.value = arg.minScale;
			frm.maxScale.value = arg.maxScale;
		}
	}
}

function selectCategory(type){
	var arg = new Object();
	var wnd = window.showModalDialog(basePath + "manage/data/selectcategory.jsp",arg,"center:yes;help:no;dialogWidth:300px;dialogHeight:380px");
	if (wnd == "1"){
		if (type == 0){
			var frm = document.frmAdSearch;
			frm.categoryId.value = arg.categoryId;
			frm.categoryName.value = arg.categoryName;
		}else{
			var frm = document.poiForm;
			frm.categoryId.value = arg.categoryId;
			frm.categoryName.value = arg.categoryName;
			frm.legend.value = arg.legend;
			frm.fontColor.value = arg.fontColor;
			frm.fontFamily.value = arg.fontFamily;
			frm.fontSize.value = arg.fontSize;
			frm.shadow.value = arg.shadow;
			frm.borderColor.value = arg.borderColor;
		}
	}
}
