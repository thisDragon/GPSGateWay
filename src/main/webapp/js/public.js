/**
*	脚本公用规划
*	Creat Date:2006-07-18 LaoDing
*	company  Richmap @ CariPower 2006
*	本脚本为utf-8编码
*	目录
*	@1	后台数据源全局变量定义
*	@2	本地查询	localSearch()
*	@3	电话查询	phoneSearch()
*	@4	站点查询	siteSearch()
*	@5	线路查询	lineSearch()
*	@6	公交换乘	busSearch()
*	@7	自驾方案查询	driverSearch()
*	@8	公交换乘与自驾方案公用函数
*			openBusDiv() 信息中去这里和从这来的显示动作
*			closeBusDiv() 信息中去这里和从这来的隐藏动作
*			ShowBS() 信息中去这里和从这来内容显示
*			BusSelect() 公交换乘中间步骤选择onclick事件
*			SearchBusLine() 提交换乘中间步骤查询
*			DriverSelect() 自驾中间步骤选择onclick事件
*			SearchDriver() 提交换乘中间步骤查询
*	@9	查询某条信息的详情	infoSearch()
*	@10	提交地图显示
*			showLocation(dwKey) 显示地点
*			showBusLine(sID) 显示公交线路
*			showBusStop(sID) 显示公交站点
*			showBusSolution(sKey,sCode) 显示公交换乘方案
*			showDrivingSolution(sId,sOrderNo) 显示自驾方案
*			doAfterShowmap() 显示地图后需要做的动作
*	@11	字符串操作开始
*	@12	异步读取数据分页开始
*	@13	窗口拖动函数组开始
*	@14	切换城市与选择
*	@15	查询开始锁定与解除操作
*	@16	数据异步读取
*	@17	行变色公用函数
*	@18	错误反馈
*	@19	窗口类（有关任务栏）开始
*	@20	任务栏初始化开始
*	@21	快捷键动作捕获
*	@22	显示站点详情的popup窗口
*	@23	得到热点关键字
*	@24 
*	@25
*	@26
*	@27
*	@28
*	********************************************************
*	20071027 update by laoding
*	本次把SVG 的地图修订成raster的，并针对所有的数据源全部做了修订，所有的查询转search.js，请具体参看这个脚本，这个脚本只提供一些方法和基本函数
*	保留上面的目录以做凭证
*	********************************************************
*/
var sData_moreInfo="com/msgserv?act=GETMOREINFO&id=?id?&citycode=?citycode?";
var sData_CitySource="css/city.xml";	//定义城市信息

/**
*	phoneSearch() 电话查询
*	从外部读取电话号码关键字，检查关键字合法性并发送到内部读取数据，解析数据发送到外部
*	@param	sPhone 电话号码
*	@param	sXslPath 解析数据的样式xsl
*	@param	sCitycode 城市代码
*	@param	oFunc 解析数据后返回处理的函数
*	@param	oVai 解析数据后返回的数据赋值的变量名，使用前应先定义为全局变量
*	@param	sUrl 有分页后传递给函数的url
*	@param	nPageNo	有分页后的页码
*	@param	sFlag 本次查询的标志，用于分页页码的区分
*/
function phoneSearch(sPhone,sXslPath,sCitycode,oFunc,oVai,sUrl,nPageNo,sFlag){
	if(sCitycode==null||sCitycode=="")sCitycode=getcitycode();		//得到当前的城市代码
	if(sXslPath==null||sXslPath==""){sXslPath="css/Company.xsl"}
	//如果是分页查询
	if(sUrl!=null&&sUrl!=""){
		sUrl=sUrl.replace("SEARCH","TURNPAGE");		//内部需要把查询参数变换才能翻页
		getXmlData(sUrl+nPageNo,sXslPath,oFunc+"('"+sFlag+"','"+sUrl+"')","",oVai);		//读取数据
		return true;
	}
	//如果是第一次查询	
	if(sPhone.length<4){alert('您输入的电话号码不足4位数！请重新输入');return false;}
	var sTempUrl=sData_basePath+sData_phoneSearch.replace("?phone?",sPhone).replace("?citycode?",sCitycode);
	getXmlData(sTempUrl,sXslPath,oFunc+"('"+sFlag+"','"+sTempUrl+"')","",oVai);
}


/**
*	driverSearch() 自驾方案查询
*	从外部读取自驾关键字，检查关键字合法性并发送到内部读取数据，解析数据返回外部
*	@param	sBegin	起点名称，如果是从“去这里从这来”得来的数据，传确定点名称
*	@param	sEnd	终点名称
*	@param	sBeginl	起点经纬度，如果是从“去这里从这来”得来的数据，传确定点经纬度
*	@param	sEndl	终点经纬度
*	@param	sBeginf	起点标志，如果是从“去这里从这来”得来的数据，传确定点标志
*	@param	sEndf	终点标志
*	@param	nAct	动作：0做的是去这里的动作 1做的是从这来得动作 null或者空标志不是从“去这里从这来”得来的数据
*	@param	sFlag	去这里从这来的输入框标志
*	@param	sCitycode 城市代码
*	@param	sXslPath 解析数据的样式xsl
*	@param	oFunc	解析数据后返回处理的函数
*	@param	oVai	解析数据后返回的数据赋值的变量名，使用前应先定义为全局变量
*/
var sNowDSCity="";	//定义本次自驾方案的城市，由于内部数据源不能提供城市，只能在外部保留全局变量
function driverSearch(sBegin,sEnd,sBeginl,sEndl,sBeginf,sEndf,nAct,sFlag,sCitycode,sXslPath,oFunc,oVai){
	if(sXslPath==null||sXslPath=="")sXslPath="css/Driver.xsl";
	if(sBeginf==""||sBeginf==null)sBeginf="nothing";
	if(sEndf==""||sEndf==null)sEndf="nothing";
	if(sCitycode==null||sCitycode=="")sCitycode=getcitycode();
	if(nAct!=null&&nAct!=""){	//如果是从“去这里从这来”得来的数据
		var oTempObj=document.getElementById("Tit"+sFlag);	//得到当前起点或终点输入框的页面元素
		var sTempName=nAct==0?"起点":"终点";
		if(oTempObj.value==""){alert('请输入'+sTempName+'位置！');oTempObj.focus();return;}
		if(nAct==0){
			sEnd=sBegin;
			sBegin=oTempObj.value;
			sEndl=sBeginl;
			sBeginl="";
			sEndf=sBeginf;
			sBeginf="nothing";
		}else{
			sEnd=oTempObj.value;
			sEndl="";
			sEndf="nothing";
		}
	}
	var sTempUrl;
	sNowDSCity=sCitycode;	//保存自驾地城市
	sTempUrl=sData_driverSolution.replace("?begin?",sBegin).replace("?end?",sEnd).replace("?citycode?",sCitycode);
	sTempUrl=sTempUrl.replace("?beginl?",sBeginl).replace("?endl?",sEndl);
	sTempUrl=sData_basePath+sTempUrl.replace("?beginflag?",sBeginf).replace("?endflag?",sEndf);
	getXmlData(sTempUrl,sXslPath,oFunc+"('"+sCitycode+"','"+sBegin+"','"+sEnd+"')",'',oVai);
	return true;
}
/*-------------------------------公交换乘与自驾方案公用函数开始---------------------------------*/
/**	
*	openBusDiv() 信息中去这里和从这来的显示动作
*	@param flag	说明是要在哪里显示公交查询框
*/
var nOpenDivH=50;	//默认层弹出的高度为50px
function openBusDiv(sFlag){
	var oShowObj=document.getElementById("BSDiv"+sFlag);	//得到要在哪里显示“去这里，从这来”的查询框
	oShowObj.style.height=nOpenDivH;
	//当前停用层弹开的动作
	//if(parseInt(oShowObj.style.height)<nOpenDivH){oShowObj.style.height=parseInt(oShowObj.style.height)+4;setTimeout(function(){openBusDiv(sFlag)},20)}else{document.getElementById("Tit"+sFlag+"").focus();}
	document.getElementById("Tit"+sFlag).focus();
}

/**	
*	closeBusDiv() 信息中去这里和从这来的隐藏动作
*	@param flag	说明是要在哪里隐藏公交查询框
*/
function closeBusDiv(sFlag){
	var oShowObj=document.getElementById("BSDiv"+sFlag);
	oShowObj.style.display="none";
	//当前停用层隐藏的动作
	//if(parseInt(oShowObj.style.height)>=6){oShowObj.style.height=parseInt(oShowObj.style.height)-4;setTimeout(function(){closeBusDiv(sFlag)},20)}else{oShowObj.style.display="none";}
}

/**
*	ShowBS() 信息中去这里和从这来内容显示
*	点击名址查询的结果中的去这里和从这来后，显示查询框，注意，使用该函数必须存在公交查询函数busSearch_c()和自驾查询函数driverSearch_c();两个函数的例子在该函数后面有列出；
*	@param sLatLongs 该信息的经纬度
*	@param sTitle	该信息标题
*	@param nAct	标志：0 去这里 1 从这来
*	@param sCitycode	信息所在城市代码
*	@param sFlag	document索引，是哪条信息
*	@param bDisplay	显示的按钮和输入框是否在同一行,true是在同一行，false不在同一行，默认为false状态
*/
function ShowBS(sLatLongs,sTitle,nAct,sCitycode,sFlag,bDisplay){
	var hiddenObj=document.getElementById("ZBDiv");	//如果存在周边层（电信使用页面），应该隐藏周边层
	if(hiddenObj){hiddenObj.style.display='none';}
	var oShowObj=document.getElementById("BSDiv"+sFlag);//得到要在哪里显示
	var sOutstr=nAct==0?"起点：":"终点：";
	sOutstr+="<input type='text' id='Tit"+sFlag+"' size='";
	sOutstr+=bDisplay?"8":"12";
	sOutstr+="' onKeyDown=\"\">";
	if(bDisplay){nOpenDivH=22;}else{nOpenDivH=50;sOutstr+="<br>";}
	sOutstr+="<input type='button' id='btn_"+sFlag+"' value='公交' onclick=\"busSearch_C('"+sTitle+"','','"+sLatLongs+"','','','','"+nAct+"','"+sFlag+"','"+sCitycode+"')\">";
	sOutstr+="<input type='button' id='btn_"+sFlag+"' value='自驾' onclick=\"driverSearch_C('"+sTitle+"','','"+sLatLongs+"','','','','"+nAct+"','"+sFlag+"','"+sCitycode+"')\">";
	sOutstr+="<input type='button' value='取消' onclick=\"closeBusDiv('"+sFlag+"')\">";
	oShowObj.innerHTML=sOutstr;
	if(oShowObj.style.display=="none"){oShowObj.style.display="";openBusDiv(sFlag);}else{document.getElementById("Tit"+sFlag).focus();}	
}
/**函数busSearch_C的例子，重点在样式xsl路径、返回函数busSearch_end和返回数据变量名busSearchData
function busSearch_C(sBegin,sEnd,sBeginl,sEndl,sBeginf,sEndf,nAct,sFlag,sCitycode){
	busSearch(sBegin,sEnd,sBeginl,sEndl,sBeginf,sEndf,nAct,sFlag,sCitycode,"","busSearch_end","sBusData");
}
*/
/**函数driverSearch_C的例子，重点在样式xsl路径、返回函数driverSearch_end和返回数据变量名driverSearchData
function driverSearch_C(sBegin,sEnd,sBeginl,sEndl,sBeginf,sEndf,nAct,sFlag,sCitycode){
	driverSearch(sBegin,sEnd,sBeginl,sEndl,sBeginf,sEndf,nAct,sFlag,sCitycode,"","driverSearch_end","sDriverData");
}
*/

/**	
*	BusSelect() 公交换乘中间步骤选择onclick事件
*	处理起点终点的赋值和行的变色
*	@param	oThisObj	当前点击的元素
*	@param	sValue		所传递给后台查询的值
*	@param	sAction		点击的是起点还是终点 1 起点 2 终点
*	@param	sName		如果点击的时候需要在输入框显示所点击起终点的名称，这里传名称
*/
var oBeginObj=null,oEndObj=null;	//保存当前已选中的行元素
function BusSelect(oThisObj,sValue,sAction,sName){
	switch(sAction){
		case 1:
			if(oThisObj){
				if(oBeginObj!=null&&oBeginObj!=oThisObj){oBeginObj.style.backgroundColor="";}
				oThisObj.style.backgroundColor="#5c8ed3";
				oBeginObj=oThisObj;
			}
			document.getElementById("BName").value=sValue;
			if(sName!=null&&sName!="")document.getElementById("BName_tit").value=sName;
			//下面显示点击的点
			richmap.clearElement("busBegin");
			sValue = sValue.split("#");
			sValue = sValue[1].split("*");
			//显示用户起点
			var beginObj = new wingBasePoint("busBegin",parseInt(sValue[0]),parseInt(sValue[1]));
			beginObj.nShadow = 2;
			var tempObj = new wingPicObj();
			tempObj.sImageSrc = "/BestTone/images/icon/user_start.png";		//图片地址
			tempObj.nImageWidth = 25;	//图片长
			tempObj.nImageHeight = 27;	//图片高
			tempObj.sAlt =sName;
			tempObj.nScreenX = 0;			//图片热点相对坐标x
			tempObj.nScreenY = -27;			//图片热点相对坐标y
			tempObj.onmouseover = function(){
				try{wEMouseOver("busBegin");}catch(e){}
			};
			beginObj.addObj(tempObj);
			var textObj = new wingTextObj();
			textObj.sText = sName;
			textObj.nScreenX = 26;
			textObj.nScreenY = -27;
			textObj.sSize = "12px";
			textObj.sColor = "#FF7139";
			textObj.sBorder = "solid 1px #FF7139";
			textObj.sPadding = "2px";
			textObj.sBgColor = "#ffffff";
			textObj.onmouseover = function(){
				try{wEMouseOver("busBegin");}catch(e){}
			};
			beginObj.addObj(textObj);
			richmap.showElement(beginObj);
			var endValue = document.getElementById("EName").value;
			if(endValue != ""){
				//换算终点的位置做zoom动作
				endValue = endValue.split("#");
				endValue = endValue[1].split("*");
				richmap.zoomToMBR(parseInt(sValue[0]>endValue[0]?endValue[0]:sValue[0]),parseInt(sValue[1]<endValue[1]?endValue[1]:sValue[1]),parseInt(sValue[0]<endValue[0]?endValue[0]:sValue[0]),parseInt(sValue[1]>endValue[1]?endValue[1]:sValue[1]));
			}else{
				richmap.zoomTo(richmap.getMaxLayer()-1,sValue[0],sValue[1]);
			}
		break;
		case 2:
			if(oThisObj){
				if(oEndObj!=null&&oEndObj!=oThisObj){oEndObj.style.backgroundColor="";}
				oThisObj.style.backgroundColor="#5c8ed3";
				oEndObj=oThisObj;
			}
			document.getElementById("EName").value=sValue;
			if(sName!=null&&sName!="")document.getElementById("EName_tit").value=sName;
			//下面显示点击的点
			richmap.clearElement("busEnd");
			sValue = sValue.split("#");
			sValue = sValue[1].split("*");
			//显示用户起点
			var endObj = new wingBasePoint("busEnd",parseInt(sValue[0]),parseInt(sValue[1]));
			endObj.nShadow = 2;
			var tempObj = new wingPicObj();
			tempObj.sImageSrc = "/BestTone/images/icon/user_end.png";		//图片地址
			tempObj.nImageWidth = 25;	//图片长
			tempObj.nImageHeight = 27;	//图片高
			tempObj.sAlt =sName;
			tempObj.nScreenX = 0;			//图片热点相对坐标x
			tempObj.nScreenY = -27;			//图片热点相对坐标y
			tempObj.onmouseover = function(){
				try{wEMouseOver("busEnd");}catch(e){}
			};
			endObj.addObj(tempObj);
			var textObj = new wingTextObj();
			textObj.sText = sName;
			textObj.nScreenX = 26;
			textObj.nScreenY = -27;
			textObj.sSize = "12px";
			textObj.sColor = "#FF7139";
			textObj.sBorder = "solid 1px #FF7139";
			textObj.sPadding = "2px";
			textObj.sBgColor = "#ffffff";
			textObj.onmouseover = function(){
				try{wEMouseOver("busEnd");}catch(e){}
			};
			endObj.addObj(textObj);
			richmap.showElement(endObj);
			var endValue = document.getElementById("BName").value;
			if(endValue != ""){
				//换算终点的位置做zoom动作
				endValue = endValue.split("#");
				endValue = endValue[1].split("*");
				sValue[0] = parseInt(sValue[0]);
				sValue[1] = parseInt(sValue[1]);
				endValue[0] = parseInt(endValue[0]);
				endValue[1] = parseInt(endValue[1]);
				//var left = ;
				richmap.zoomToMBR(parseInt(sValue[0]>endValue[0]?endValue[0]:sValue[0]),parseInt(sValue[1]<endValue[1]?endValue[1]:sValue[1]),parseInt(sValue[0]<endValue[0]?endValue[0]:sValue[0]),parseInt(sValue[1]>endValue[1]?endValue[1]:sValue[1]));
			}else{
				richmap.zoomTo(richmap.getMaxLayer()-1,sValue[0],sValue[1]);
			}
		break;
	}
}

/**
*	SearchBusLine() 提交换乘中间步骤查询
*	解析数据并发配到公交换乘查询
*/
function SearchBusLine(){	
	var BObj=document.getElementById("BName").value;
	var EObj=document.getElementById("EName").value;
	if(BObj==""||BObj==null){alert("请在起点列表中选择您的公交起点!");return;}
	if(EObj==""||EObj==null){alert("请在终点列表中选择您的公交终点!");return;}
	BArr=BObj.split("#");
	EArr=EObj.split("#");
	try{busSelectWindow.min();}catch(e){}	//如果存在中间步骤地窗口，则隐藏
	busSearch_C(BArr[0],EArr[0],BArr[1],EArr[1],BArr[2],EArr[2],'','',sNowBSCity);
}

/**	
*	DriverSelect() 自驾中间步骤选择onclick事件
*	处理起点终点的赋值和行的变色
*	@param	oThisObj	当前点击的元素
*	@param	sValue		所传递给后台查询的值
*	@param	sAction		点击的是起点还是终点 1 起点 2 终点
*	@param	sName		如果点击的时候需要在输入框显示所点击起终点的名称，这里传名称
*/
var oDBeginObj=null,oDEndObj=null;	//保存当前已选中的行元素
function DriverSelect(oThisObj,sValue,sAction,sName){
	switch(sAction){
		case 1:
			if(oThisObj){
				if(oDBeginObj!=null&&oDBeginObj!=oThisObj){oDBeginObj.style.backgroundColor="";}
				oThisObj.style.backgroundColor="#5c8ed3";
				oDBeginObj=oThisObj;
			}
			document.getElementById("DBName").value=sValue;
			if(sName!=null&&sName!="")document.getElementById("DBName_tit").value=sName;
			//下面显示点击的点
			richmap.clearElement("driverBegin");
			sValue = sValue.split("#");
			sValue = sValue[1].split("*");
			//显示用户起点
			var beginObj = new wingBasePoint("driverBegin",parseInt(sValue[0]),parseInt(sValue[1]));
			beginObj.nShadow = 2;
			var tempObj = new wingPicObj();
			tempObj.sImageSrc = "/BestTone/images/icon/user_start.png";		//图片地址
			tempObj.nImageWidth = 25;	//图片长
			tempObj.nImageHeight = 27;	//图片高
			tempObj.sAlt =sName;
			tempObj.nScreenX = 0;			//图片热点相对坐标x
			tempObj.nScreenY = -27;			//图片热点相对坐标y
			tempObj.onmouseover = function(){
				try{wEMouseOver("driverBegin");}catch(e){}
			};
			beginObj.addObj(tempObj);
			var textObj = new wingTextObj();
			textObj.sText = sName;
			textObj.nScreenX = 26;
			textObj.nScreenY = -27;
			textObj.sSize = "12px";
			textObj.sColor = "#FF7139";
			textObj.sBorder = "solid 1px #FF7139";
			textObj.sPadding = "2px";
			textObj.sBgColor = "#ffffff";
			textObj.onmouseover = function(){
				try{wEMouseOver("driverBegin");}catch(e){}
			};
			beginObj.addObj(textObj);
			richmap.showElement(beginObj);
			var endValue = document.getElementById("DEName").value;
			if(endValue != ""){
				//换算终点的位置做zoom动作
				endValue = endValue.split("#");
				endValue = endValue[1].split("*");
				richmap.zoomToMBR(parseInt(sValue[0]>endValue[0]?endValue[0]:sValue[0]),parseInt(sValue[1]<endValue[1]?endValue[1]:sValue[1]),parseInt(sValue[0]<endValue[0]?endValue[0]:sValue[0]),parseInt(sValue[1]>endValue[1]?endValue[1]:sValue[1]));
			}else{
				richmap.zoomTo(richmap.getMaxLayer()-1,sValue[0],sValue[1]);
			}
		break;
		case 2:
			if(oThisObj){
				if(oDEndObj!=null&&oDEndObj!=oThisObj){oDEndObj.style.backgroundColor="";}
				oThisObj.style.backgroundColor="#5c8ed3";
				oDEndObj=oThisObj;
			}
			document.getElementById("DEName").value=sValue;
			if(sName!=null&&sName!="")document.getElementById("DEName_tit").value=sName;
			//下面显示点击的点
			richmap.clearElement("driverEnd");
			sValue = sValue.split("#");
			sValue = sValue[1].split("*");
			//显示用户起点
			var endObj = new wingBasePoint("driverEnd",parseInt(sValue[0]),parseInt(sValue[1]));
			endObj.nShadow = 2;
			var tempObj = new wingPicObj();
			tempObj.sImageSrc = "/BestTone/images/icon/user_end.png";		//图片地址
			tempObj.nImageWidth = 25;	//图片长
			tempObj.nImageHeight = 27;	//图片高
			tempObj.sAlt =sName;
			tempObj.nScreenX = 0;			//图片热点相对坐标x
			tempObj.nScreenY = -27;			//图片热点相对坐标y
			tempObj.onmouseover = function(){
				try{wEMouseOver("driverEnd");}catch(e){}
			};
			endObj.addObj(tempObj);
			var textObj = new wingTextObj();
			textObj.sText = sName;
			textObj.nScreenX = 26;
			textObj.nScreenY = -27;
			textObj.sSize = "12px";
			textObj.sColor = "#FF7139";
			textObj.sBorder = "solid 1px #FF7139";
			textObj.sPadding = "2px";
			textObj.sBgColor = "#ffffff";
			textObj.onmouseover = function(){
				try{wEMouseOver("driverEnd");}catch(e){}
			};
			endObj.addObj(textObj);
			richmap.showElement(endObj);
			var endValue = document.getElementById("DBName").value;
			if(endValue != ""){
				//换算终点的位置做zoom动作
				endValue = endValue.split("#");
				endValue = endValue[1].split("*");
				sValue[0] = parseInt(sValue[0]);
				sValue[1] = parseInt(sValue[1]);
				endValue[0] = parseInt(endValue[0]);
				endValue[1] = parseInt(endValue[1]);
				//var left = ;
				richmap.zoomToMBR(parseInt(sValue[0]>endValue[0]?endValue[0]:sValue[0]),parseInt(sValue[1]<endValue[1]?endValue[1]:sValue[1]),parseInt(sValue[0]<endValue[0]?endValue[0]:sValue[0]),parseInt(sValue[1]>endValue[1]?endValue[1]:sValue[1]));
			}else{
				richmap.zoomTo(richmap.getMaxLayer()-1,sValue[0],sValue[1]);
			}
		break;
	}
}

/**
*	SearchDriver() 提交换乘中间步骤查询
*	解析数据并发配到公交换乘查询
*/
function SearchDriver(){	
	var BObj=document.getElementById("DBName").value;
	var EObj=document.getElementById("DEName").value;
	if(BObj==""||BObj==null){alert("请在起点列表中选择您的自驾起点!");return;}
	if(EObj==""||EObj==null){alert("请在终点列表中选择您的自驾终点!");return;}
	BArr=BObj.split("#");
	EArr=EObj.split("#");
	try{driverSelectWindow.min();}catch(e){}	//如果存在中间步骤地窗口，则隐藏
	driverSearch_C(BArr[0],EArr[0],BArr[1],EArr[1],BArr[2],EArr[2],'','',sNowDSCity);
}
/*-------------------------------公交换乘公用函数结束---------------------------------*/

/* -------------------------------字符串操作开始----------------------------------- */
//	过滤在字符串首部的空格，包括全角的空格
function rtrim(sStr){
	return sStr.replace(/\s+$/,"").replace(/\s+$/,"");
}
//	过滤在字符串尾部的空格，包括全角的空格
function ltrim(sStr){
	return sStr.replace(/^\s+/,"").replace(/^\s+/,"");
}
//	过滤字符串首尾的空格
function trim(sStr){
	sStr=ltrim(sStr);
	sStr=rtrim(sStr);
	return sStr;
}
//把过滤空格写成String的私有函数
String.prototype.ltrim = function(){
	return this.replace(/^\s+/g,"");
};
String.prototype.rtrim = function(){
	return this.replace(/\s+$/g,"");
};
String.prototype.trim = function(){
	return this.replace(/^\s+|\s+$/g,"");
};
// 过滤常见的转义和后台关键字符
function clearKey(sStr){
	var sReplaceStr="";
	sStr=sStr.replace(/[\+\&\|\!\^\"\~\*\?\:\\\'\%\$\<\>\#]/g,"");
	return sStr;
}
//删除数组中的某个值
Array.prototype.remove=function(count){
	if(this.splice){return this.splice(count,1);}	//如果是ie5.5及以上，支持splice函数
	var tempArr=this.slice(count+1,this.length);
	for(var i=this.length-count;i>0;i--){
		this.pop();
	}
	for(var j=0;j<tempArr.length;j++){
		this.push(tempArr[j]);
	}
	return;
};
/**
*	TransText() 格式化查询字符串，把字符串中的关键字和分割符转化成半角
*	@param tempstr	需要转化的字符串
*	@param splitWord 分割符
*/
function TransText(tempstr,splitWord){
	if(tempstr.length==0){return "";}
	var Word1=splitWord.charCodeAt(0);
	var Word2=Word1+65248;
	var Words1=String.fromCharCode(Word1);
	var Words2=String.fromCharCode(Word2);
	if(tempstr.indexOf(Words1)==-1&&tempstr.indexOf(Words2)==-1)return tempstr;
	var isContent=false;
	var ss=new Array();
	for(i=0;i<tempstr.length;i++){
		var s1=tempstr.charCodeAt(i);
		var s2=s1;
		if(!isContent){
			if(s1>10000&&s1!=12288)s1=s1-65248;
			if(s1==12288)s1=32;
			
		}else{
			if(s1==12288)s1=32;
		}
		if(s2==Word1||s2==Word2){isContent=true;}
		ss[i]=String.fromCharCode(s1);
	}
	return ss.join("").toLowerCase();
}
/**
*	getRandFlag	得到一个唯一的标志
*/
var nRandCount=0;	//
function getRandFlag(){
	nRandCount++;
	var returnstr=nRandCount.toString(2);
	returnstr="l"+returnstr.replace(/0/g,"l");
	return "sRandFlag_"+returnstr;            
};
/**
*	getUrlParam 从某url中得到参数
*	@param sUrl 指定url,如果为空，则从当前页的url读取
*	@param sParam 指定要读取得参数
*/
function getUrlParam(sUrl,sParam){
	if(sUrl==""||sUrl==null)sUrl = window.location.href;
	var fi_index,fi_index1;
	fi_index = sUrl.indexOf("?");
	if (fi_index > -1){
		sUrl = "&" + sUrl.substring(fi_index+1);
		fi_index = sUrl.indexOf("&" + sParam + "=");
		if (fi_index > -1){
			fi_index1 = sUrl.indexOf("&",fi_index + 2);
			if (fi_index1 ==-1) fi_index1=9999;
			return unescape(sUrl.substring(fi_index + sParam.length + 2,fi_index1));
		}else{
			return "";
		}
	}else{
		return "";
	}
}
/* -------------------------------字符串操作结束----------------------------------- */

//=== 异步读取数据分页开始 ===
var CP_TitArr	=new Array();		//保存标志
var CP_CountArr	=new Array();		//保存总记录数
var CP_NPageArr	=new Array();		//保存当前页
var CP_UrlArr	=new Array();		//保存连接记录
var CP_FuncArr	=new Array();		//保存动作函数
var CP_FlagArr	=new Array();		//保存名称
var PageCounts=0;					//记录多个地方分页时的总计数
document.createStyleSheet('css/page.css');	//导入分页样式表
/**
*	分页 CPage v3.3 author:laoding creat:2005-12-22	LastUpdate:2006-06-28
*	@update 2006-6-28 新增分页样式 v3.2
*	@update 2006-8-4 修订分页输入框对页码的判断及点击分页的动作函数 v3.3 
*	显示异步读取的分页信息
*	@param PageShow	显示容器，同时和PageTitle一起组成本次分页的标志。如果不指定显示位置的话，分页信息将显示在该id标志的页面元素
*	@param howpages	一页显示多少条
*	@param Page_url	下一页的链接
*	@param PageFlag	本次分页的标志
*	@param TotalC	总计录数
*	@param func		指定的动作函数，即把Page_url和pageno传递给该函数
*	@param PageShowT	指定显示位置，这样，同一个分页可以显示在多个页面元素中
*	@param display	指定显示样式
					1、基本样式，形如：首页 上一页 共2538/16页 下一页 末页 到 页GO
					2、列表样式，形如：页码 << < 3 4 5 6 7 8 9 10 11 12 13 > >> 
*	@return null
*/ 
function creatPage(PageShow,howpages,Page_url,PageFlag,TotalC,func,PageShowT,display){
	var NCou=-1;	//判断是哪个地方的分页
	for(i=0;i<=PageCounts;i++){if(CP_TitArr[i]==PageFlag){NCou=i;}}
	//如果是第一次
	if(NCou==-1){
		//之前可能已经存在其他形式的分页
		if(CP_TitArr[PageCounts]!=null){PageCounts++;NCou=PageCounts;}else{NCou=PageCounts;}
		//初始化
		CP_TitArr[NCou]=PageFlag;	//记录分页标志
		CP_NPageArr[NCou]=1;		//记录本次分页的当前页
		CP_UrlArr[NCou]=Page_url;	//记录本次分页的url
		CP_FuncArr[NCou]=func;		//记录本次分页返回运行的函数
		CP_FlagArr[NCou]=PageShow;	//记录本次分页内容的显示位置
		if(TotalC==null||TotalC==""){
			//如果没有指定总记录数，那说明总记录数在生成的页面中
			CP_CountArr[NCou]=document.getElementById(PageShow+"Tb").count;
		}else{
			CP_CountArr[NCou]=TotalC;
		}
	}
	//计算总页数
	var totalpage=CP_CountArr[NCou]%howpages==0?parseInt(CP_CountArr[NCou]/howpages):(parseInt(CP_CountArr[NCou]/howpages)+1);
	if(totalpage==0){
		document.getElementById(PageShow).innerHTML="<font color=#eeeeee>无分页信息</font>";
	}else{
		if(display==null){display=1;}	//默认为分页样式1
		var pageStr="";	//定义向外输出的分页信息
		switch(display){
			case 1:	//分页样式1 形如：首页 上一页 共2538/16页 下一页 末页 到 页GO
				var firstpage=CP_NPageArr[NCou]==1?"<span class='page1_unlink'>首页</span>":"<span class='page1_link' onclick=\"turnPage("+ NCou +","+ totalpage +",1)\" id='"+ CP_FlagArr[NCou] +"_Home'>首页</span>";
				var downpage=CP_NPageArr[NCou]==totalpage?"<span class='page1_unlink'>下一页</span>":"<span onclick=\"turnPage("+ NCou +","+ totalpage +","+ (CP_NPageArr[NCou]*1+1) +")\" class='page1_link' id='"+CP_FlagArr[NCou]+"_Down'>下一页</span>";
				var uppage=CP_NPageArr[NCou]==1?"<span class='page1_unlink'>上一页</span>":"<span onclick=\"turnPage("+NCou+","+ totalpage +","+ (CP_NPageArr[NCou]*1-1) +")\" class='page1_link' id='"+CP_FlagArr[NCou]+"_Up'>上一页</span>";
				var endpage=CP_NPageArr[NCou]==totalpage?"<span class='page1_unlink'>末页</span>":"<span onclick=\"turnPage("+ NCou +","+ totalpage +","+ totalpage +")\" class='page1_link' id='"+CP_FlagArr[NCou]+"_End'>末页</span>";
				pageStr=firstpage+" "+uppage+" 共"+totalpage+"/"+CP_NPageArr[NCou]+"页 "+downpage+" "+endpage+" 到<input type='text' id='"+ CP_FlagArr[NCou] +"PN' class='page1_input' size='2' maxlength='4' onKeyDown=\"checkEnter('"+CP_FlagArr[NCou]+"GO')\">页<span id='"+ CP_FlagArr[NCou] +"GO' class='page1_btn' onclick=\"turnPage("+ NCou +","+ totalpage +")\">GO</span>";
			break;
			case 2://分页样式2 形如：页码 << < 3 4 5 6 7 8 9 10 11 12 13 > >> 
				var firstpage=CP_NPageArr[NCou]==1?"":"<span class='page2_btn' onclick=\"turnPage("+NCou+","+totalpage+",1)\" id='"+CP_FlagArr[NCou]+"_Home' title='到首页'><img src='namecard/images/home.jpg' border='0'></span>&nbsp;";
				var uppage=CP_NPageArr[NCou]==1?"":"<span class='page2_btn' onclick=\"turnPage("+NCou+","+totalpage+","+(CP_NPageArr[NCou]*1-1)+")\" id='"+CP_FlagArr[NCou]+"_Up' title='上一页'><img src='namecard/images/back.jpg' border='0'></span>&nbsp;";
				var downpage=CP_NPageArr[NCou]==totalpage?"":"<span class='page2_btn' onclick=\"turnPage("+NCou+","+totalpage+","+(CP_NPageArr[NCou]*1+1)+")\" id='"+CP_FlagArr[NCou]+"_Down' title='下一页'><img src='namecard/images/next.jpg' border='0'></span>&nbsp;";
				var endpage=CP_NPageArr[NCou]==totalpage?"":"<span class='page2_btn' onclick=\"turnPage("+NCou+","+totalpage+","+totalpage+")\" id='"+CP_FlagArr[NCou]+"_End' title='到最后一页'><img src='namecard/images/end.jpg' border='0'></span>";
				var pagelist="";	//显示页码
				var nStartPage=1;	//从第几页开始显示
				var nPageNoCount=10;	//在页面上最多显示几个页码
				var nStartPage=CP_NPageArr[NCou]==1?1:(CP_NPageArr[NCou]-1);	//如果当前页不是第一页，默认从当前页的上一页开始显示
				if((nStartPage+nPageNoCount)>totalpage&&totalpage>=nPageNoCount){nStartPage=totalpage-nPageNoCount+1}	//如果当前页后面的页码总和小于nPageNoCount页，从倒数第nPageNoCount条开始显示
				if(totalpage<nPageNoCount){nStartPage=1;}		//如果页码总和小于nPageNoCount页，从第一页开始显示
				for(var j=nStartPage;(j<=totalpage)&&(j<(nStartPage+nPageNoCount));j++){
					if(j==CP_NPageArr[NCou]){
						pagelist+="<span class='page2_focus'>"+j+"</span> ";
					}else{
						pagelist+="<span class='page2_blur' onclick=\"turnPage("+NCou+","+totalpage+","+j+")\" onMouseOver=\"this.className='page2_focus';\" onMouseOut=\"this.className='page2_blur';\">"+j+"</span> ";
					}
				}
				pageStr="<span class='page2_text'>页码</span> "+firstpage+uppage+pagelist+downpage+endpage;
			break;
		}
		if(PageShowT!=null&&PageShowT!=""){PageShow=PageShowT}
		document.getElementById(PageShow).innerHTML=pageStr;
	}
}
/**
*	turnPage() 判定输入的页码是否正确并运行分页
*	@param nNcou	//当前分页在总分页数组中的下标
*	@param nTotalPage	//当前分页的总页码
*	@param nPageNo	//当前分页的页码,如果不指定，说明当前的页码从输入框中来
*/
function turnPage(nNcou,nTotalPage,nPageNo){
	var oTempObj=document.getElementById(CP_FlagArr[nNcou]+"PN");	//得到页码输入框
	if((nPageNo==""||nPageNo==null)&&oTempObj){nPageNo=oTempObj.value;}	//得到输入框内值
	var sWrongStr="";	//定义错误
	if(nPageNo==""){
		sWrongStr="请输入页码！";
	}else if(nPageNo.toString().match(/^(?:[1-9]\d*|0)$/) == null){
		sWrongStr="请在页码中输入数字！";
	}else if(nPageNo<=0||nPageNo>nTotalPage){
		sWrongStr="您输入的页码超出范围！";
	}
	if(sWrongStr!=""){
		alert(sWrongStr);
		oTempObj.focus();
		oTempObj.select();
		return false;
	}else{		
		CP_NPageArr[nNcou]=nPageNo;	//保存当前分页的当前页码		
		eval(CP_FuncArr[nNcou]+"'"+CP_UrlArr[nNcou]+"','"+nPageNo+"','"+CP_TitArr[nNcou]+"');");
	}
}
/**
*	把某输入框的回车键绑定到指定元素的onclick事件
*	@param obj 指定元素的id
*/
function checkEnter(obj){
	if(event.keyCode==13){
		document.getElementById(obj).focus();
		document.getElementById(obj).fireEvent("onclick");return false;
	}	
}
//-------------------------------异步读取数据分页结束-------------------------------

//------------------------------- 窗口拖动函数组开始 -------------------------------
var nEvent_x=0,nEvent_y=0;	//初始化鼠标x,y
var nDrag_x=0,nDrag_y=0;	//初始化拖动窗口x,y
var nInfo_x=0,nInfo_y=0;	//初始化地图左边窗口x,y
var nInfoBtn_x=0,nInfoBtn_y=0; //初始化地图左边窗口按钮x,y
var bMoveable=false;		//当前是否有窗口处于被拖动状态

/**
*	DragDiv v1.0 窗口拖动公用函数	Author:laoding @ Richmap	CreatDate: 2006-04-14
*	开始拖动
*	@param classObj	窗口类，该类必须符合以下条件
			必须有成员.m_oDragObj,该成员指定了窗口拖动的起始鼠标触发事件的元素
			必须有成员函数getPositionX(x)来返回窗口的坐标x
			必须有成员函数getPositionY(y)来返回窗口的坐标y
			必须有成员函数setPosition(x,y)来设置窗口的坐标x,y
*/
function startDrag(classObj){
	if(event.button==1)	{
		classObj.m_oDragObj.setCapture();
		nEvent_x = event.clientX;
		nEvent_y = event.clientY;
		nDrag_x = classObj.getPositionX();
		nDrag_y = classObj.getPositionY();
		bMoveable = true;
	}
};
//拖动
function draging(classObj){
	if(bMoveable){
		classObj.setPosition(nDrag_x + event.clientX - nEvent_x,nDrag_y + event.clientY - nEvent_y);
	}
};
//结束拖动
function stopDrag(classObj){
	if(bMoveable){
		classObj.m_oDragObj.releaseCapture();
		bMoveable = false;
	}
	//当窗口被拖动到顶部以上
	if(classObj.getPositionY()<0){classObj.setPosition(classObj.getPositionX(),0);}
	//当窗口被拖动到底部以下
	var nBodyTop=document.body.scrollTop;
	var nBodyHeight=document.body.offsetHeight;
	if(classObj.getPositionY() > nBodyTop + nBodyHeight-50){
		classObj.setPosition(classObj.getPositionX(),nBodyTop + nBodyHeight-50);
	}
};
//------------------------------- 窗口拖动函数组结束 -------------------------------

/**
*	切换城市与选择
*	@param action 如果action=true应该使页面元素citycode的值设置到当前指定值
*/
function selectCity(citycode,action,cityname){
	//如果存在popup窗口,隐藏他
	//try{showHidePopup("hidden");}catch(e){}
	if(citycode==null){citycode=getcitycode();}	
	var selectObj=document.getElementById("cityCode");
	var endcityObj = document.getElementById("endCityCode");
	if(action){//如果需要设置当前指定值
		if(selectObj.length){	//如果值在select框里头
			for(i=0;i<selectObj.length;i++){
				if(selectObj[i].value.indexOf(citycode)>=0){
					selectObj[i].selected=true;
					endcityObj[i].selected=true;
					break;
				}
			}
		}else{//如果值在文本框里头
			selectObj.value=citycode;
			var tempObj=document.getElementById("cityName");
			if(tempObj){tempObj.value=cityname};
		}
	}
	//即时显示地图
	try{
		richmap.showCity(citycode);
	}catch(e){}
	loadPHSLocation(citycode);
}
//这个给地图触发事件使用
function selectCity2(citycode,action,cityname){
	//如果存在popup窗口,隐藏他
	//try{showHidePopup("hidden");}catch(e){}
	if(citycode==null){citycode=getcitycode();}	
	var selectObj=document.getElementById("cityCode");
	var endcityObj = document.getElementById("endCityCode");
	if(action){//如果需要设置当前指定值
		if(selectObj.length){	//如果值在select框里头
			for(i=0;i<selectObj.length;i++){
				if(selectObj[i].value.indexOf(citycode)>=0){
					selectObj[i].selected=true;
					endcityObj[i].selected=true;
					break;
				}
			}
		}else{//如果值在文本框里头
			selectObj.value=citycode;
			var tempObj=document.getElementById("cityName");
			if(tempObj){tempObj.value=cityname};
		}
	}
	loadPHSLocation(citycode);
}
//切换小灵通定位（有些城市没开通)
function loadPHSLocation(citycode){
	if (citycode=="FJFZ" || citycode=="FJXM"){
		hasPLS = true;
		document.getElementById("tdPHSLocation").innerHTML = "电话号码定位";
	}else{
		hasPLS = false;
		document.getElementById("tdPHSLocation").innerHTML = "电话号码定位（未开通）";
	}
	changeBar(0);
}
/**
*	载入城市
*	@param xmlurl 指定城市数据源
*	@param BusState 是否过滤不支持公交查询的城市
*	@param initCity 默认显示哪个城市，中文名称
*	@param display 载入到页面的样式
*	@param initCityCode	默认显示哪个城市，代码名称，如果设置了该属性，则initCity无效
*/
var cityStr="FJFZ,福州市/"
			+"FJXM,厦门市/"
			+"FJQZ,泉州市/"
			+"FJZZ,漳州市/"
			+"FJLY,龙岩市/"
			+"FJND,宁德市/"
			+"FJNP,南平市/"
			+"FJSM,三明市/"
			+"FJPT,莆田市";
var citySourceData=cityStr.split("/");
for(i=0;i<citySourceData.length;i++){citySourceData[i]=citySourceData[i].split(",");}
function loadCity(xmlurl,BusState,initCity,display,initCityCode){		
	display=(display==null||display=="")?0:display;
	switch(display){
		case 0:	//把城市生成到select框内
			var citySelection = document.getElementById("cityCode");
			var endcitySelection = document.getElementById("endCityCode");
			var selectsI=0;	//定义是否默认选中某个城市
			for(var i = 0 ; i < citySourceData.length ; i++){				
				var oOption = new Option(citySourceData[i][1],citySourceData[i][0]);
				var oOption2 = new Option(citySourceData[i][1],citySourceData[i][0]);
				citySelection.options.add(oOption);
				endcitySelection.options.add(oOption2);
				if(citySourceData[i][0]==initCityCode){
					selectsI=i;
				}else if(citySourceData[i][1]==initCity){
					selectsI=i;
				}				
			}
			citySelection.options[selectsI].selected=true;
			endcitySelection.options[selectsI].selected=true;
		break;
		case 1://把城市生成为tab模式,当前R4外部界面使用
			var outStr='<table border="0" cellpadding="0" cellspacing="0"><tr>';
			for(var i = 0 ; i < citySourceData.length ; i++){
				outStr+='<td height="18" class="CityMenuBlur" onClick="ChangeCity(this,\''+citySourceData[i][0]+'\',\''+citySourceData[i][1]+'\')">'+citySourceData[i][1]+'</td>';
			}
			outStr+='</tr></table>';
			var outObj=document.getElementById("showCitySelect");
			try{
				outObj.innerHTML=outStr;
				outObj.firstChild.firstChild.firstChild.firstChild.fireEvent("onclick");
			}catch(e){}
		break;
	}
}
/**
*	把城市信息转载到select对象
*	@param tagObj 目标对象
*	@param initCityCode 初始化时默认被选中的城市的代码
*	@param initCityName 初始化时默认被选中的城市的名称(优先级小于initCityCode)
*/
function loadCityToObj(tagObj,initCityCode,initCityName){
	tagObj=document.getElementById(tagObj);
	var tagObjType=tagObj.tagName;
	if(initCityCode==""&&initCityName!=null)initCityCode=getCityCodeByName(initCityName);
	if(tagObjType=="SELECT"){
		var selectsI=0;	//定义是否默认选中某个城市
		for(var i = 0 ; i < citySourceData.length ; i++){				
			var oOption = new Option(citySourceData[i][1],citySourceData[i][0]);
			tagObj.options.add(oOption);
			if(citySourceData[i][0]==initCityCode)selectsI=i;
		}
		return true;
	}else{
		return false;
	}
}
/**
*	返回当前页的城市代码
*	@param BusState 是否警告当前城市不支持公交查询的城市
*/
function getcitycode(busState){
	var  selectObj=document.getElementById("cityCode");
	if(!selectObj)return null;
	if(selectObj.length){
		var city=selectObj.value;
		if(city==""){return false;}
		if(busState&&city.indexOf("$N")>=0){alert("很抱歉，当前的城市‘"+selectObj[selectObj.selectedIndex].text.replace(/\s/g,"")+"’还不支持公交查询！我们将在近期提供该服务！");return false;}
		var cityArr=city.split("$");
		city=cityArr[0];
		return city;
	}else{return selectObj.value;}
}
/**
*	getCityNameByCode() 由城市代码返回城市名称
*	@param sCityCode 城市代码
*/
function getCityNameByCode(sCityCode){
	var sReturnName="";
	for(var i = 0 ; i < citySourceData.length ; i++){				
		if(citySourceData[i][0]==sCityCode){sReturnName=citySourceData[i][1];}
	}
	return sReturnName;
}
/**
*	getCityCodeByName() 由城市名称返回城市代码
*	@param sCityName 城市名称
*/
function getCityCodeByName(sCityName){
	var sReturnCode="";
	for(var i = 0 ; i < citySourceData.length ; i++){						
		if(citySourceData[i][1]==sCityName){sReturnCode=citySourceData[i][0];}
	}
	return sReturnCode;
}
/**
*	返回当前页的城市代码

*	@param busState 是否警告当前城市不支持公交查询的城市
*/
function getcityname(busState){
	var selectObj=document.getElementById("cityCode");
	if(!selectObj)return null;
	if(selectObj.length){
		var city=selectObj.value;
		if(city==""){return false;}
		if(busState&&city.indexOf("$N")>=0){alert("很抱歉，当前的城市‘"+selectObj[selectObj.selectedIndex].text.replace(/\s/g,"")+"’还不支持公交查询！我们将在近期提供该服务！");return false;}
		return selectObj[selectObj.selectedIndex].text;
	}else{return document.getElementById("cityName").value;}
}
//------------------------------- 切换城市与选择结束 -------------------------------


//------------------------------- 数据异步读取 -------------------------------
var returnData,returnDataAsDomObj;	//定义本次返回数据及返回对象
var hashTable_Run=new Array();	//保存每次查询的标志，是查询异常结束还是正常结束
var nRunTimeOut=20000;	//设置查询超时时间为20秒
var bGetDataRun = false;	//记录当前页面上是否存在未结束的查询
var oInterval_checkRun=null;	//初始化判断是否超时的循环对象
var nGetDataStartTime;	//查询开始时间
var xmlWrong=true;				//最后一次查询是否得到数据查询错误信息
/**
*	异步数据读取,可以独立读取、取消读取及超时停止返回
*	v2.0 最后更新2006.8.20 laoding
*	@param xmlPath	数据源xml地址
*	@param xslPath	对该xml进行解析的xsl地址
*	@param func		函数，数据解析完毕后转向执行的函数，如果有指定Sid，该值无效
*	@param Sid		指定数据显示的位置
*	@param reValue	变量名称，解析完的数据赋值到该变量，如果有指定Sid，该值无效
*	@param format	是否格式化出来的数据，比如把数据中的'&gt;'替换成'>',false 不替换，true替换，默认为false状态
*	@param windowObj 将此DOM挂到window上的对象
*	@param	bLockSearch 是否锁定页面上的查询 true 是（缺省) false 否
*/
var processorArr = new Array();
var xslDocArr = new Array();
var xmlDocArr = new Array();
var xmlStringArr = new Array();
function getXmlData(xmlPath,xslPath,func,Sid,reValue,format,windowObj,bLockSearch){
	if(bLockSearch==null)bLockSearch=true;
	var sFlag=getRandFlag();			//本次数据读取的唯一标志
	hashTable_Run[sFlag]=true;		//将本次查询的标志存到公用哈希表，true 表明查询没被异常终止	
	if(bLockSearch){if(!getDataRun(sFlag)) return false;}	//如果上一次查询未结束不允许下次查询,否则锁定查询
	processorArr[sFlag] = new XSLTProcessor();	
	xslDocArr[sFlag] = Sarissa.getDomDocument();
	xslDocArr[sFlag].async = true;
	xslDocArr[sFlag].onreadystatechange = function (){		
		if(xslDocArr[sFlag].readyState == 4){
			//如果出现提前结束查询，异步返回时不对返回数据操作。
			if(hashTable_Run[sFlag]==false){
				return false;
			}
			if(xslDocArr[sFlag].parseError!=0){
				//alert("查找不到您要的内容\r样式载入错误");
				alert("xsl载入错误！错误描述:\r\n"+Sarissa.getParseErrorText(xslDocArr[sFlag]));
				if(bLockSearch)getDataCancel(sFlag);	//完成本次查询,解除查询锁定
				return false;
			}
			processorArr[sFlag].importStylesheet(xslDocArr[sFlag]);
			processorArr[sFlag].setParameter(null, "title", "test");
			xmlDocArr[sFlag] = Sarissa.getDomDocument();
			xmlDocArr[sFlag].async = true;
			xmlDocArr[sFlag].onreadystatechange = function(){
				if(xmlDocArr[sFlag].readyState == 4){
					if(hashTable_Run[sFlag]==false){//如果出现提前结束查询，异步返回时不对返回数据操作。
						return false;
					}
					if(xmlDocArr[sFlag].parseError!=0){
						//alert("查找不到您要的内容\r载入错误！");
						alert("xml载入错误！错误描述:\r\n"+Sarissa.getParseErrorText(xmlDocArr[sFlag]));
						if(bLockSearch)getDataCancel(sFlag);	//完成本次查询,解除查询锁定
						return false;
					}
					
					//测试是否出来的xml是出错的信息
					xmlWrong=true;
					try{
						var testNode=xmlDocArr[sFlag].selectNodes("root/Return")[0].text;
						if(testNode=="0"){
							//如果出错，返回出错信息
							//该xml应该符合最新规范的xml,即出来的出错xml应该是：
							//<root><Return>0</Return><Error>这里是出错信息</Error></root>
							xmlWrong=false;
							returnData=xmlDocArr[sFlag].selectNodes("root/Error")[0].text;
						}
					}catch(e){}
					
					//过滤在cdata中的特殊字符"<>",以保证解析数据不会出错
					xmlStringArr[sFlag]=Sarissa.serialize(xmlDocArr[sFlag]);
					//alert(xmlStringArr[sFlag]);
					xmlStringArr[sFlag]=xmlStringArr[sFlag].replace(/\!\[CDATA\[[^\]\]]*\]\]/g,function($1){return Sarissa.escape($1)});					
					xmlStringArr[sFlag]=xmlStringArr[sFlag].replace(/\&\#([1-9]\d*|0)\;/g,"");
					xmlDocArr[sFlag] = (new DOMParser()).parseFromString(xmlStringArr[sFlag], "text/xml");
					
					//如果数据解析不是错误信息，则应把数据按样式表解析出来
					if(xmlWrong){
						var newDocument = processorArr[sFlag].transformToDocument(xmlDocArr[sFlag]);
						if(newDocument.parseError!=0){
							var ErrorStr=Sarissa.getParseErrorText(newDocument);
							if(ErrorStr!=Sarissa.PARSED_OK){
								//alert("查找不到您要的内容\r解析错误！");
								alert("数据解析错误！错误描述:\r\n"+ErrorStr);
								if(bLockSearch)getDataCancel(sFlag);	//完成本次查询,解除查询锁定
								return false;
							}
						}
						returnData=Sarissa.serialize(newDocument);
					}
					
					//是否把数据挂到window上
					if(windowObj!=null&&windowObj!=""){
						returnDataAsDomObj=xmlDocArr[sFlag];
						eval("window."+windowObj+"=returnDataAsDomObj;");
					}
					
					//格式化数据并返回数据或引发返回函数
					returnData=Sarissa.unescape(returnData);
					returnData=Sarissa.unescape(returnData);
					//alert(returnData);
					if(returnData==""){
						alert("查询无结果");
						if(bLockSearch)getDataCancel(sFlag);	//完成本次查询,解除查询锁定
						return false;
					}
					if(bLockSearch)getDataCancel(sFlag);	//完成本次查询,解除查询锁定
					if(Sid!=null&&Sid!=""){
						document.getElementById(Sid).innerHTML=returnData;
					}else{
						eval(reValue+"=returnData;");eval(func);
					}					
				}
				//delete strTest;
				//CollectGarbage();
				return true;
			};
			xmlDocArr[sFlag].load(xmlPath);
			return true;
		}
	};
	xslDocArr[sFlag].load(xslPath);
	return true;
}
/**
*	数据异步读取开始及锁定
*	@param sFlag 本次查询的标志
*/
function getDataRun(sFlag){
	//如果存在地图上的popup,隐藏他，当前只有中兴页面使用
	try{showHidePopup('hidden');}catch(e){}
	if(bGetDataRun){alert("上一查询尚未完成,请稍候 ...");return false;}
	//生成查询提示条
	if(document.getElementById("getData_Div") == null){
		var getData_InfoDiv = document.createElement("DIV");
		getData_InfoDiv.style.zIndex = "999";
		getData_InfoDiv.style.position = "absolute";
		getData_InfoDiv.style.width="200px";
		getData_InfoDiv.style.top =document.body.scrollTop+200;
		getData_InfoDiv.style.left = 400;
		getData_InfoDiv.id="getData_Div";
		getData_InfoDiv.innerHTML="<table width='100%' align=center bgcolor='red' style='color:white'><tr><td>信息查询中 请稍候...</td><td align='right'><span style='color:white;cursor:pointer' onclick=\"getDataCancel('"+sFlag+"')\" title='取消本次查询'>×</span></td></tr></table>";
		document.body.insertAdjacentElement("afterBegin",getData_InfoDiv);
	}
	var oTempObj=document.getElementById("getData_Div");
	oTempObj.style.top=document.body.scrollTop+200;
	oTempObj.innerHTML="<table width='100%' align=center bgcolor='red' style='color:white'><tr><td>信息查询中 请稍候...</td><td align='right'><span style='color:white;cursor:pointer' onclick=\"getDataCancel('"+sFlag+"')\" title='取消本次查询'>×</span></td></tr></table>";
	document.getElementById("getData_Div").style.visibility = "visible";
	bGetDataRun=true;
	nGetDataStartTime=new Date();	
	oInterval_checkRun=setInterval(function(){checkTimeOut(sFlag)},1000);
	return true;
}
/**
*	数据异步读取操作判断是否超时
*	@param sFlag 本次查询的标志
*/
function checkTimeOut(sFlag){
	if(bGetDataRun==false){
		clearInterval(oInterval_checkRun);
		//oInterval_checkRun=null;		
		//getDataCancel(sFlag);
	}else if(new Date().getTime()-nGetDataStartTime.getTime()>nRunTimeOut){		
		clearInterval(oInterval_checkRun);
		oInterval_checkRun=null;		
		getDataCancel(sFlag);
		alert('查询超时或查询无结果!');
	}
}
/**
*	数据异步读取操作解除
*	@param sFlag 本次查询的标志
*/
function getDataCancel(sFlag){
	hashTable_Run[sFlag]=false;	//设置本次查询终止
	bGetDataRun=false;
	document.getElementById("getData_Div").style.visibility = "hidden";	
	return true;
}
//------------------------------- 数据异步读取结束 -------------------------------

//------------------------------- 行变色公用函数 -------------------------------
function overC(obj){
	obj.style.backgroundColor=obj.style.backgroundColor=="#5c8ed3"?"#5c8ed3":"#E7EEFE";
}
function outC(obj){
	obj.style.backgroundColor=obj.style.backgroundColor=="#5c8ed3"?"#5c8ed3":"";
}
var oldBgColor="";
function overBg(obj,color){
	oldBgColor=color;
	obj.style.backgroundColor=color;
}
function outBg(obj){
	obj.style.backgroundColor=oldBgColor;
}
//------------------------------- 行变色公用函数结束 -------------------------------
function showWrongReport(wrongtitle,cityname,wrongmodule,wrongkey,wrongcontent,classname){
	if(classname==null||classname=="")classname="bottom";
	if(wrongtitle==null||wrongtitle=="")wrongtitle="错误反馈";
	if(wrongcontent==null)wrongcontent="";
	var wrongStr="<a href=\"javascript:void(0)\" class='"+classname+"' onclick=\"wrongReport('"+cityname+"','"+wrongmodule+"','"+wrongkey+"','"+wrongcontent+"');\">" + wrongtitle + "</a>";
	return wrongStr;
}
/**
*	错误反馈
*	param cityname 错误所属城市，可以是citycode,也可以是cityname
*	param wrongmodule 错误模块，传以下中文名
	w001	本地查询
	w002	公交换乘
	w003	自驾方案
	w004	线路查询
	w005	站点查询
	w006	电话查询
	w007	周边查询
	w008	详情查询
	w009	整体评价
	w010	线路详情
	w011	选择自驾起始点
	w012	选择换乘起始点
*	param wrongkey 查询关键字
*	param wrongcontent 错误描述
*/
function wrongReport(cityname,wrongmodule,wrongkey,wrongcontent){
	if(cityname.indexOf("FJ")>=0){cityname=getCityNameByCode(cityname);}
	if(cityname=="")cityname="无";
	advise(cityname,wrongmodule,wrongkey,wrongcontent);	
}

//-------------------------------独立窗口类开始-------------------------------
var nIndex=800;					//初始化窗口层级
var sNormalColor="#aaaaaa";		//窗口不在焦点时的颜色
var sFocusColor="#5C8ED3";		//窗口在焦点时的颜色7FACEA
var sFontColor="#000000";		//窗口标题颜色
var sFocusPic="bt01.gif";		//窗口在焦点时的图片
var sNormalPic="bt02.gif";		//窗口不在焦点时的图片
var oFocusWindow=new Array();	//在显示状态的窗口id容器
var sWingMapName="MapsWindow";	//定义地图窗口的实例名称
var flyWindowObj=new Array();	//记录和实例后的FlyWindowy
/**
*	FWindow v1.0 独立窗口类
*	Author:laoding @ Richmap	CreatDate: 2006-09-03
*	注意：实例化时，实例不应该在其他函数体内定义,应该是全局变量;
*	@初始化函数.init()参数说明：
*	@param sId		窗口唯一性id，一定要和实例同名
*	@param nWidth	指定窗口宽
*	@param nHeight	指定窗口高
*	@param nLeft	窗口位置左(x)
*	@param nTop		窗口位置顶(y)
*	@param sTit		窗口标题
*	@param sMsg		窗口内容
*	@param sIcon	指定窗口小图标
*	@param bScr		内容是否出现滚动条　true:是（缺省）false：否
*	@param bClose	是否可被关闭　true:可以（缺省）false：不可以
*	@param bMin		是否可被最小化　true:可以（缺省）false：不可以
*	@param bDrag	是否可被拖动　true:可以（缺省）false：不可以
*/
function FlyWindow(){
	this.m_sId;	
	this.m_sTitle="";		//窗口标题
	this.m_sMessage="";		//窗口内容
	this.m_sIcon="list.jpg";		//小图标
	this.m_nW=300;	//窗口宽
	this.m_nH=300;	//窗口高
	this.m_nX=0;	//窗口x
	this.m_nY=0;	//窗口y	

	this.m_nIndex=nIndex=nIndex+2;	//定义窗口层级
	this.m_bIsInitialized=false;	//窗口是否被初始化
	this.m_oFlyDiv=null;			//定义窗口层元素
	this.m_oDragObj=null;			//定义窗口被拖动的起始鼠标点元素
	this.m_oTitle=null;			//定义窗口标题元素
	this.m_oContent=null;			//定义窗口内容元素
	
	//返回窗口宽
	this.getWidth=function(){if(!this.isInit)return;return this.m_oFlyDiv.offsetWidth;};
	//返回窗口高
	this.getHeight=function(){if(!this.isInit)return;return this.m_oFlyDiv.offsetHeight;};
	//返回窗口显示状态
	this.getDisplay=function(){if(!this.isInit)return;return this.m_oFlyDiv.style.display;};
	//返回窗口的焦点状态
	this.getFocus=function(){if(this.m_sId==nNowFocusId)return true;else return false;};
	//返回窗口的显示状态
	this.getShow=function(){if(!this.isInit)return;if(this.getDisplay()=="none")return false;else return true;};
	//返回窗口图标文件名
	this.getIconPic=function(){return this.m_sIcon;};
	//返回窗口标题
	this.getTitle=function(){return this.m_sTitle;};
	//返回窗口的位置数组
	this.getPosition=function(){
		if(!this.isInit)return;
		var tempArr=new Array();
		tempArr[0]=parseInt(this.m_oFlyDiv.style.left);
		tempArr[1]=parseInt(this.m_oFlyDiv.style.top);
		return tempArr;
	};
	//返回窗口位置x
	this.getPositionX=function(){if(!this.isInit)return;return parseInt(this.m_oFlyDiv.style.left);	};
	//返回窗口位置y
	this.getPositionY=function(){if(!this.isInit)return;return parseInt(this.m_oFlyDiv.style.top);};
	//返回是否初始化完成
	this.isInit=function(){
		if(!this.m_bIsInitialized){alert("窗口未被初始化");return false;}
		return true;
	};

	//设置窗口标题内容
	this.setTitle=function(sTit){
		if(!this.isInit)return;
		this.m_sTitle=sTit;
		var ObjLength=this.m_oTitle.offsetWidth;	//限定的标题长度
		var TextLength=0;	//实际的标题长度
		var returnstr="";	//切割后的标题
		for(i=0;i<sTit.length;i++){	//如果标题长度大于窗口长，应截断（这段其实可以用css设置，哎，懒)
			if(sTit.charCodeAt(i)>1000){TextLength+=12;}else{TextLength+=6;}
			if(TextLength>=ObjLength){returnstr=sTit.substring(0,i-1	)+"...";i=sTit.length+1;}
		}
		if(returnstr=="")returnstr=sTit;
		this.m_oTitle.innerHTML=returnstr;
		this.m_oTitle.title=sTit;
	};	
	//设置小图标
	this.setIconPic=function(pic){
		this.m_sIcon=pic;
		var obj=document.getElementById("Icon_"+this.m_sId);
		obj.src="images/WingBar/"+this.m_sIcon;
		this.callApi("setIconPic");
	};
	//设置窗口内容
	this.setContent=function(msg){
		this.m_sMessage=msg;
		this.m_oContent.innerHTML=msg;
	};
	//设置窗口位置
	this.setPosition=function(nX,nY){		
		this.m_oFlyDiv.style.left=nX;
		this.m_oFlyDiv.style.top=nY;
	};
	//设置窗口宽
	this.setWidth=function(nW){
		return;	//发现设置总不顺畅，下次再说
	};
	//设置窗口高
	this.setHeight=function(nH){
		return;	//发现设置总不顺畅，下次再说
	};
	//初始化窗口	
	this.init=function(sId,nWidth,nHeight,nLeft,nTop,sTit,sMsg,sIcon,bScr,bClose,bMin,bDrag){
		this.m_sId=sId;
		if(flyWindowObj[this.m_sId]!=null){
			flyWindowObj[this.m_sId].focus(2);
			flyWindowObj[this.m_sId].setTitle(sTit);		//窗口标题
			flyWindowObj[this.m_sId].setContent(sMsg);		//窗口内容		
			return flyWindowObj[this.m_sId];
		}	//如果实例之前已经存在，返回该实例
		flyWindowObj[this.m_sId]=this;		//记录本次实例，在后面的页面实例中起作用
		//第一次初始化传入的窗口参数
		this.m_sTitle=sTit;		//窗口标题
		this.m_sMessage=sMsg;	//窗口内容
		this.m_sIcon=sIcon;		//小图标
		this.m_nW=nWidth;	//窗口宽
		this.m_nH=nHeight;	//窗口高
		this.m_nX=nLeft;	//窗口x
		this.m_nY=nTop;		//窗口y	
		this.m_bDrag=bDrag==null?true:bDrag;		//窗口是否有滚动条
		this.m_bClose=bClose==null?true:bClose;	//窗口是否被关闭
		this.m_bHasMin=bMin==null?true:bMin;		//是否可以最小化
		this.m_bHasScroll=bScr==null?true:bScr;	//内容是否出现滚动条
		this.m_sScroll=this.m_bHasScroll?"overflow:scroll;":"";
		this.m_sScrollClass=this.m_bHasScroll?"class='blueScroll';":""; 	//滚动条样式,如果不允许滚动，则样式为空，注意如果flywindow上内容里头有滚动条，则拖动的时候会引起ie crush

		var dragstr=this.m_bDrag?"onmousedown='startDrag("+this.m_sId+")' onmouseup='stopDrag("+this.m_sId+")' onmousemove='draging("+this.m_sId+")' ":" ";
		var closestr=this.m_bClose?"<td width='16'><img id='btn_close_"+this.m_sId+"' src='images/close01.gif' width='15' height='17' onclick='"+this.m_sId+".close();' title='关闭窗口'></td>":"";
		var minstr=this.m_bHasMin?"<td width='16'><img id='btn_min_"+this.m_sId+"' src='images/min01.gif' width='15' height='17' onclick='"+this.m_sId+".min();' title='最小化窗口( ESC )'></td>":"";
		var tempLength=0;
		tempLength=this.m_bClose?(tempLength+16):tempLength;
		tempLength=this.m_bHasMin?(tempLength+16):tempLength;
		var tempBackGroundImg=this.m_sId=="MapsWindow"?" background-image:url(images/MapBG.jpg) ":"";	//如果是地图窗口，使用地图背景图
		var divstr = ""
			+ "<div id=xMsg" + this.m_sId + " "
			+ "style='display:;z-index:" + this.m_nIndex + ";width:" + this.m_nW + ";height:" + this.m_nH + ";left:" + this.m_nX + ";"
			+ "top:" + this.m_nY + ";background-color:" + sFocusColor + ";color:" + sFontColor + ";font-size:8pt;font-family:Tahoma;position:absolute;cursor:default;"
			+ "border:1px solid " + sFocusColor + ";' "
			+ "onmousedown='"+this.m_sId+".focus(true);'"
			+ ">"
				+"<table width='" + (this.m_nW-1*2) + "' border='0' cellspacing='0' cellpadding='0' height='20' background='images/"+sFocusPic+"' "+dragstr+" id='DragObj_"+this.m_sId+"'>"
				+"<tr><td width='18' style='padding-left:3px;'><img src='images/WingBar/" + this.m_sIcon + "' id='Icon_"+this.m_sId+"' width='18px'></td>"
				+"<td width='" + (this.m_nW-tempLength-21-3-2) + "' style='padding-left:3px;color:#ffffff;font-size:9pt;font-weight:bold' id='DivT_"+this.m_sId+"'></td>"+ minstr + closestr
				+"<td width='2'></td></tr></table>"
				+ "<div "+this.m_sScrollClass+" style='width:" + (this.m_nW-1*2) + ";height:" + (this.m_nH-20-2) + ";background-color:white;line-height:14px;word-break:break-all;padding:3px;" + tempBackGroundImg + this.m_sScroll + "' id='DivCon_"+this.m_sId+"' "+tempBackGroundImg+">"
					+ this.m_sMessage
				+ "</div>"
			+ "</div>";
		document.body.insertAdjacentHTML("beforeEnd",divstr);
		
		this.m_oFlyDiv=document.getElementById("xMsg"+this.m_sId);
		this.m_oDragObj=document.getElementById("DragObj_"+this.m_sId);
		this.m_oTitle=document.getElementById("DivT_"+this.m_sId);
		this.m_oContent=document.getElementById("DivCon_"+this.m_sId);	
		this.m_bIsInitialized=true;
		this.setTitle(this.m_sTitle);
		this.callApi("init");	//调用接口，如果有被定义的话
		this.focus(3);	
	};
	//@param focusTo 隐藏后是否把焦点移动到下个窗口 true 是(缺省) false 否
	this.min=function(focusTo){
		if(focusTo==null)focusTo=true;
		if(!this.m_bHasMin&&!this.m_bClose){return;}
		//隐藏窗口
		this.m_oFlyDiv.style.display="none";
		if(focusTo){
			//查看是否在焦点列表中,如果有，清除之前的纪录
			for(var i=0;i<oFocusWindow.length;i++){
				if(oFocusWindow[i]==this.m_sId){oFocusWindow.remove(i);}
			}
			//判断是否存在最近的其他显示窗口，把焦点移到该窗口
			if(oFocusWindow.length>0){flyWindowObj[oFocusWindow[oFocusWindow.length-1]].focus(4);}	//把焦点移动到最靠近自己的窗口
		}
		this.callApi("min");	//调用接口，如果有被定义的话
	};
	//关闭窗口
	this.close=function(){
		//先隐藏，让焦点移动到其他窗口
		this.min();
		//清除窗口元素
		this.m_oFlyDiv.parentNode.removeChild(this.m_oFlyDiv);	
		//清除唯一性id，让下次生成成为可能
		this.callApi("close");	//调用接口，如果有被定义的话
		flyWindowObj[this.m_sId]=null;
		eval(this.m_sId+".m_bIsInitialized=false;");
	};
	//使窗口处于焦点状态,如果action为空，则当窗口已处于焦点状态时最小化窗口
	this.focus=function(action){
		if(event){//如果点击的是关闭按钮或最小化按钮，那么要取消他的focus动作，否则，和title的onclick的focus重复
			if(event.srcElement&&action!=4){
				if(event.srcElement.id.indexOf("btn_")>=0){return;}
			}
		}
		this.m_oFlyDiv.style.display="";
		//当窗口不处于焦点状态
		if(oFocusWindow[oFocusWindow.length-1]!=this.m_sId||action==4){
			//查看是否在焦点列表中,如果有，清除之前的纪录
			for(var i=0;i<oFocusWindow.length;i++){
				if(oFocusWindow[i]==this.m_sId){oFocusWindow.remove(i);}
			}
			//使窗口处于焦点状态
			this.m_nIndex=nIndex=nIndex+2;	
			this.m_oFlyDiv.style.zIndex=this.m_nIndex;
			this.m_oFlyDiv.style.backgroundColor=sFocusColor;
			this.m_oFlyDiv.style.borderColor=sFocusColor;
			var tempObj;
			this.m_oDragObj.style.backgroundImage="url(images/"+sFocusPic+")";
			tempObj=document.getElementById("btn_close_"+this.m_sId);
			if(tempObj){tempObj.src="images/close01.gif";}
			tempObj=document.getElementById("btn_min_"+this.m_sId);
			if(tempObj){tempObj.src="images/min01.gif";}
			//让之前处于焦点状态的窗口向下移动，并切换颜色			
			var oldWindowId=oFocusWindow[oFocusWindow.length-1];
			if(oldWindowId!=null){
				var oldClassObj=flyWindowObj[oldWindowId];	//得到之前处于焦点状态的窗口类实例
				oldClassObj.m_oFlyDiv.style.backgroundColor=sNormalColor;
				oldClassObj.m_oFlyDiv.style.borderColor=sNormalColor;
				oldClassObj.m_oFlyDiv.style.zIndex=this.m_nIndex-1;
				oldClassObj.m_oDragObj.style.backgroundImage="url(images/"+sNormalPic+")";
				tempObj=document.getElementById("btn_close_"+oldWindowId);
				if(tempObj){tempObj.src="images/close02.gif";}
				tempObj=document.getElementById("btn_min_"+oldWindowId);
				if(tempObj){tempObj.src="images/min02.gif";}
				oldClassObj.callApi("min");	
			}			
			//最后保存下当前焦点窗口id
			oFocusWindow.push(this.m_sId);
			if(action==2||action==3){this.setPosition(this.getPositionX(),this.m_nY+document.body.scrollTop);}
			this.callApi("focus");	//调用接口，如果有被定义的话
		}else{	//当前已经在焦点状态，按照条件看是否要改变窗口的位置
			switch(action){
				case 1:
					this.min();
				break;
				case 2:
					this.setPosition(this.getPositionX(),this.m_nY+document.body.scrollTop);
				break;
				case 3:
					if(this.getPositionY()!=this.m_nY+document.body.scrollTop){
						this.setPosition(this.getPositionX(),this.m_nY+document.body.scrollTop);
					}else{
						this.min();
					}
				break;
				default:
				break;
			}
		}
	};
	this.callApi=function(action){
		return true;
		//当前可以把flywindow的动作释放在这里，提供外部继承该类的时候重定义
		//action 动作：focus,min,close,init,setIconPic
	};
}
//-------------------------------窗口类结束-------------------------------

//-------------------------------带任务栏的窗口类-------------------------
/**
*	继承FlyWindow,增加对窗口在任务栏上的表现
*	creatdate:2006-9-4	laoding
*	sTitle 任务栏上的标题
*	必须有初始化后的任务栏容器提供支持，该容器必须符合下面的规定
*	名称：taskBar
*	私有成员:display	表示窗口在任务栏上纵向排列（值为"")还是横向平铺（值为"inline"）
*	私有成员:isInitialized	表示是否初始化成功 true 是 false 否
*	私有函数:addObj(msg)	增加任务
*	私有函数:addShortIco(msg)	增加短标题
*/
FlyWindowBar.prototype=new FlyWindow();
function FlyWindowBar(sTitle){	
	this.m_sIco_tit=sTitle==null?"":sTitle;	//定义任务栏文字
	this.m_oBarsContainer=null;	//定义bar的单个容器
	this.m_sBarBlurStr="";			//定义bar在blur状态的内容
	this.m_sBarFocusStr="";			//定义bar在focus状态的内容	
	//重定义接口函数，增加对任务栏的操作
	this.callApi=function(action){
		switch(action){
			case "init":	//初始化任务栏
				//必须定义taskBar实例，不然不能用				
				if(taskBar){
					if(!taskBar.isInitialized){alert("任务栏未被初始化");return false;}
				}else{
					alert("未为任务栏窗口定义任务栏容器！");return false;
				}
				this.m_sDisplay=taskBar.display;	//从任务栏类得到任务栏标题显示样式 "" 或者 "inline"
				var sOutStr='';				
				if(taskBar){
					if(taskBar.isInitialized){taskBar.addObj(sOutStr);}else{alert("任务栏未被初始化");return false;}
				}else{
					alert("未为任务栏窗口定义任务栏容器！");return false;
				}
				if(this.m_sIco_tit==null||this.m_sIco_tit==""){	//如果没有标题，只显示图标
					taskBar.addShortIco('<img src="images/WingBar/'+this.m_sIcon+'" title="'+this.m_sTitle+'" id="Bars_'+this.m_sId+'" onclick="'+this.m_sId+'.focus(3);">&nbsp;&nbsp;');
				}else{
					taskBar.addObj('<div style="display:' + this.m_sDisplay + ';cursor:default;" title="'+this.m_sTitle+'" id="FWindowBars_'+this.m_sId+'" onclick="'+this.m_sId+'.focus(3);"></div>');
					var closeStr;	//定义关闭窗口的按钮内容
					closeStr=this.m_bClose?"<td width=12 background='images/WingBar/tu_02.jpg' style='padding-left:3px;padding-right:3px'><img src='images/WingBar/close.gif' alt='关闭窗口' style='cursor:pointer;' width='12' height='12' onclick='"+this.m_sId+".close();'></td>":"";
					this.m_sBarBlurStr='<table border="0" cellspacing="0" cellpadding="0" style="display:inline">'
										+'  <tr><td width=3></td><td height="22"><img src="images/WingBar/tu_01.jpg"></td>'
										+'	<td align="center" background="images/WingBar/tu_02.jpg" style="padding-left:3px;padding-right:3px"><img src="images/WingBar/'+this.m_sIcon+'" id="BarIcon_'+this.m_sId+'"></td>'
										+'	<td valign="middle" background="images/WingBar/tu_02.jpg" style="padding-left:2px;padding-right:2px;font-size:9pt" id="IcoTit_'+this.m_sId+'">'+this.m_sIco_tit+'</td>'+closeStr
									  +'<td><img src="images/WingBar/tu_03.jpg"></td></tr></table>';
					closeStr=this.m_bClose?"<td width=12 background='images/WingBar/ao_02.jpg' style='padding-left:3px;padding-right:3px'><img src='images/WingBar/close.gif' alt='关闭窗口' style='cursor:pointer;' width='12' height='12' onclick='"+this.m_sId+".close();'></td>":"";
					this.m_sBarFocusStr='<table border="0" cellspacing="0" cellpadding="0" style="display:inline">'
										+'  <tr><td width=3></td><td><img src="images/WingBar/ao_01.jpg"></td>'
										+'	<td align="center" background="images/WingBar/ao_02.jpg" style="padding-left:3px;padding-right:3px"><img src="images/WingBar/'+this.m_sIcon+'" id="BarIcon_'+this.m_sId+'"></td>'
										+'	<td background="images/WingBar/ao_02.jpg" style="padding-left:2px;padding-right:2px;font-size:9pt" id="IcoTit_'+this.m_sId+'">'+this.m_sIco_tit+'</td>'+closeStr
									  +'<td><img src="images/WingBar/ao_03.jpg"></td></tr></table>';
				}
			break;
			case "min":		//最小化任务栏
				if(this.m_sIco_tit==null||this.m_sIco_tit=="")return;
				document.getElementById("FWindowBars_"+this.m_sId).innerHTML=this.m_sBarBlurStr;
				this.setIconTitle(this.m_sIco_tit);
			break;
			case "close":	//关闭任务栏
				if(this.m_sIco_tit==null||this.m_sIco_tit=="")return;
				var removeObj=document.getElementById("FWindowBars_"+this.m_sId);
				removeObj.parentNode.removeChild(removeObj);
			break;
			case "focus":	//使任务栏上的按钮处于焦点状态
				if(this.m_sIco_tit==null||this.m_sIco_tit=="")return;
				document.getElementById("FWindowBars_"+this.m_sId).innerHTML=this.m_sBarFocusStr;
				this.setIconTitle(this.m_sIco_tit);
			break;
			case "setIconPic":	//设置任务栏图标
				document.getElementById("BarIcon_"+this.m_sId).src="images/WingBar/"+this.m_sIcon;
			break;
			default:
			break;
		}
		return true;
	};
	//设置任务栏标题
	this.setIconTitle=function(tit){
		if(!this.isInit)return;
		if(this.m_sIco_tit==""||this.m_sIco_tit==null){return;}
		this.m_sIco_tit=tit;
		document.getElementById("IcoTit_"+this.m_sId).innerHTML=tit;
	};
}
//-------------------------------带任务栏的窗口类结束-------------------------

//------------------------------- richmap4\fztel\qztel 任务栏初始化开始 -------------------------------
//该类定义了任务栏窗口的容器
var nBarHeight=26;
var nBarTop;
var nBarLeft=0;//初始化任务栏位置
function FlyTaskbar(){
	nBarTop=document.body.clientHeight-nBarHeight;
	this.display="inline";	//定义任务栏显示标题是横向平铺
	this.m_nTop=document.body.scrollTop+document.body.clientHeight-nBarHeight;
	this.m_nLeft=document.body.scrollLeft+nBarLeft;	
	this.addObj=function(msg){	//增加一个任务
		this.m_oBarContainer.innerHTML+=msg;
	};
	this.addShortIco=function(msg){	//增加一个短任务，只显示图片
		document.getElementById("Bar_Short").innerHTML+=msg;
	};
	this.isInitialized=false;
	this.init=function(){
		var outstr='<div id="Wing_Bar" style="position:absolute;top:'+this.m_nTop+';left:'+this.m_nLeft+'px;z-index:999;"><table width="100%" border="0" cellspacing="0" cellpadding="0">'
					+'  <tr><td width="84"><img src="images/WingBar/wing.jpg" width="84" height="26"></td>'
					+'	<td width="12" background="images/WingBar/wing_03.jpg"><img src="images/WingBar/wing_04.jpg" width="3" height="26"></td>'
					+'	<td width="90" valign="bottom" background="images/WingBar/wing_03.jpg" id="Bar_Short"><img src="images/WingBar/icon_search.gif" width="16" height="16" onclick="Key_Ctrl_Delete();" title="返回顶部搜索框并关闭所有窗口 (Ctrl+Delete)">&nbsp;&nbsp;</td>'
					+'	<td background="images/WingBar/z_02.jpg" valign="top" id="Bar_Con"><div style="display:inline;"><img src="images/WingBar/z_01.jpg" width="15" height="26"></div></td>'
					+'	<td width="11"><img src="images/WingBar/z_03.jpg"></td>'
					+'	<td width="35"><img src="images/WingBar/wing_02.jpg"></td>'
					+'</tr></table></div>';
		document.body.insertAdjacentHTML("beforeEnd",outstr);
		this.m_oBarContainer=document.getElementById("Bar_Con");	//定义taskBar容器
		setInterval("barPlay()",10);
		this.isInitialized=true;
	};
}
//使任务栏保持位置
function barPlay(){
	var nGoL=12;
	var obj=document.getElementById("Wing_Bar");
	var nBodyTop=document.body.scrollTop;
	var nObjTop=parseInt(obj.style.top);
	nBarTop=document.body.clientHeight-nBarHeight;
	obj.style.top=nBodyTop+nBarTop;
	/*	屏蔽任务栏浮动效果
	if(nBodyTop+nBarTop-nObjTop>5){
		obj.style.top=nObjTop+nGoL;
	}else{
		obj.style.top=nBodyTop+nBarTop;
	}*/
};
//------------------------------- 任务栏初始化结束 -------------------------------

//------------------------------- 快捷键动作捕获 -------------------------------
document.onkeydown=function(){
	try{
		if(event.ctrlKey){
			switch(event.keyCode){
				case 45:	//捕获Ctrl+Insert
					Key_Ctrl_Insert();
				break;
				case 46:	//捕获Ctrl+Delete
					Key_Ctrl_Delete();
				break;
				case 35:	//捕获Ctrl+End
					Key_Ctrl_End();
					return false;
				break;
				case 36:	//捕获Ctrl+Home
					Key_Ctrl_Home();
					return false;
				break;
				case 33:	//捕获Ctrl+PageUp
					Key_Ctrl_PageUp();
					return false;
				break;
				case 34:	//捕获Ctrl+PageDown
					Key_Ctrl_PageDown();
					return false;
				break;
				case 107:	//捕获Ctrl+ +
					Key_Ctrl_Plus();
				break;
				case 109:	//捕获Ctrl+ -
					Key_Ctrl_Sub();
				break;
				default:break;
			}
		}
		if(event.altKey){
			switch(event.keyCode){
				//捕获Alt+0~9键（小键盘）
				case 96:case 97:case 98:case 99:case 99:case 100:case 101:case 102:	case 103:case 104:case 105:
					Key_Alt_09(event.keyCode-96+1);
					return false;
				break;
				//捕获Alt+0~9键
				case 48:case 49:case 50:case 51:case 52:case 53:case 54:case 55:case 56:case 57:
					Key_Alt_09(event.keyCode-48+1);
					return false;
				break;
			}			
		}
		switch(event.keyCode){
			case 27:	//捕获ESC键
				Key_Esc();
			break;		
		}
	}catch(e){}
};

//部分公用快捷操作函数

//到末页
function Key_Ctrl_End(){
	var tempObj=document.getElementById("ShowPg_End");
	if(tempObj){tempObj.fireEvent("onclick");return false;}
};
//到首页
function Key_Ctrl_Home(){
	var tempObj=document.getElementById("ShowPg_Home");
	if(tempObj){tempObj.fireEvent("onclick");return false;}
};
//下一页
function Key_Ctrl_PageDown(){
	var tempObj=document.getElementById("ShowPg_Down");
	if(tempObj){tempObj.fireEvent("onclick");return false;}
};
//上一页
function Key_Ctrl_PageUp(){
	var tempObj=document.getElementById("ShowPg_Up");
	if(tempObj){tempObj.fireEvent("onclick");return false;}
};
//关闭当前在顶部的窗口
function Key_Esc(){
	if(errors){//如果存在错误窗口，先隐藏错误窗口
		if(errors.m_oErrObj.style.display==""){
			errors.close();
		}else{
			if(oFocusWindow.length>0)flyWindowObj[oFocusWindow[oFocusWindow.length-1]].min();
		}
	}else{
		if(oFocusWindow.length>0)flyWindowObj[oFocusWindow[oFocusWindow.length-1]].min();
	}
};
//隐藏所有的flywindow，包含其继承的对象
function hiddenAllWindow(){
	var j=oFocusWindow.length;
	for(var i=0;i<j;i++){
		flyWindowObj[oFocusWindow[oFocusWindow.length-1]].min(false);//隐藏窗口，但不做焦点转移
		oFocusWindow.pop();
	}
}
//显示某条信息的详情
function Key_Alt_09(id){
	var tempObj=document.getElementById("Info_"+id);
	if(tempObj){
		tempObj.focus();
		tempObj.fireEvent("onclick");
	}
};
//全屏窗口
function Key_Ctrl_Plus(){//return;
	try{
		window.open(window.location.href,"fzTelecom","fullscreen=1,menubar=0,toolbar=0,directories=0,location=0,status=0,scrollbars=0,resizable=0");
		window.opener=null;
		window.close();
	}catch(e){
		alert("窗口被拦截,请关闭浏览器的窗口拦截功能！");
	}	
}
//取消全屏窗口
function Key_Ctrl_Sub(){//return;
	try{
		window.open(window.location.href,"unfzTelecom","fullscreen=0,menubar=1,toolbar=1,directories=1,location=1,status=1,scrollbars=1,resizable=1");
		window.opener=null;
		window.close();
	}catch(e){
		alert("窗口被拦截,请关闭浏览器的窗口拦截功能！");
	}
}
//捕捉F1键
window.onhelp=function(){
	if(HelpWindow!=null){HelpWindow.focus(3);}return false;
};
//------------------------------- 快捷键动作捕获结束 -------------------------------
/**
*	得到某元素在页面上的位置
*	返回位置数组
*/
function getoffset(obj){
	var t=obj.offsetTop; 
	var l=obj.offsetLeft;
	while(obj=obj.offsetParent){
		t+=obj.offsetTop;
		l+=obj.offsetLeft;
	}
	var rec = new Array(1);
	rec[0]  = t;
	rec[1] = l;
	return rec;
}