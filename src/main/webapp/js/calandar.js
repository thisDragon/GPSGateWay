var myC_x,myC_y;
var myC_timeset=null,myC_timeset1=null;
var divObj=null;
var inputName;
//构建对象
function myCalendar(){
	var myDate = new Date();		
	this.year = myDate.getFullYear();
	this.month = myDate.getMonth()+1;
	this.date = myDate.getDate();
	
	this.format="yyyy-mm-dd";	 
	//this.style = myStyle(1);　
	this.show = function(){
		var week = new Array('日','一','二','三','四','五','六');
		var outStr="<div class='cs' onselectstart='return false' oncontextmenu='return false' onmousedown='if(event.button==2)this.style.display=\"none\"' id='myC_div'><div class='shadow'></div><div style='position:absolute;left:0px;top:0px;z-index:1'>";
		//创建头部
		outStr+="<table class='tit' id='myC_Top' onmousedown='myC_x=event.x-parentNode.parentNode.style.pixelLeft;myC_y=event.y-parentNode.parentNode.style.pixelTop;setCapture()' onmouseup='releaseCapture();' onmousemove='myCMove(this.parentElement.parentElement);'>";
		outStr+="<tr><td width=10 onmouseover='this.style.color=\"black\"' onmouseout='this.style.color=\"\"' onclick='cutYear()' style='font-family: Webdings;cursor:hand;' title='减少年份'>7</td>";
		outStr+="<td title='减少月份' onmouseover='this.style.color=\"black\"' onclick='cutMonth()' onmouseout='this.style.color=\"\"' width=10 style='font-family: Webdings;cursor:hand;'>3</td>";
		outStr+="<td align=center onmouseover=this.className='move1'; onmouseout=this.className='';divHidden(myC.parentElement.nextSibling); onclick='createyear("+this.year+",this);divShow(myC.parentElement.nextSibling);'></td>";
		outStr+="<td align=center onclick='createmonth("+this.month+",this);divShow(myC.parentElement.nextSibling)' onmouseover=this.className='move1'; onmouseout=this.className='';divHidden(myC.parentElement.nextSibling);></td>";
		outStr+="<td width=10 onmouseover='this.style.color=\"black\"' onmouseout='this.style.color=\"\"' onclick='addMonth()' style='font-family: Webdings;cursor:hand;' title='增加月份'>4</td>";
		outStr+="<td width=10 style='font-family: Webdings;cursor:hand;' onmouseover='this.style.color=\"black\"' onmouseout='this.style.color=\"\"' onclick='addYear()' title='增加年份'>8</td></tr></table>";
		//创建星期条目
		outStr+="<table class='week'><tr>";
		for(i=0;i<7;i++){
			outStr+="<td align=center>"+week[i]+"</td>";
		}
		outStr+="</tr></table>";
		//创建日期条目
		outStr+="<table class='ds' id='myC' cellspacing=2 cellpadding=0>";
		for(i=0;i<6;i++){
			outStr+="<tr>";
			for(j=0;j<7;j++){
				outStr+="<td width=10% height=16 align=center onmouseover='mOver(this)' onmouseout='mOut(this)' onclick='if(this.innerText!=\"\")getValue(inputName,this.innerText);myC_div.style.display=\"none\"'></td>";
			}
			outStr+="</tr>";
		}
		outStr+="</table>";
		//建建水印
		outStr+="</div>";		
		//创建选择图层
		outStr+="<div style='position:absolute;left:0px;top:0px;z-index:3' onmouseover=divShow(this) onmouseout=divHidden(this)></div>";
		outStr+="</div>";
		document.body.insertAdjacentHTML("beforeEnd",outStr);
		//显示日期
		showDate(this.year,this.month);	
		document.getElementById("myC_div").style.display='none';
	};
}
 //设置样式
function myStyle(num){
	if(!num||isNaN(num)){alert('参数不对,采用默认样式！');num=1;}
	var style = new Array();	  
	style[1]=".week{background-color:#DfDfff;font-size:12px;width:140px;}"
		   +".ds{width:140px;font-size:12px;cursor:hand}"
		   +".mover{border:1px solid black;background-color:#f4f4f4;}"
		   +".move1{border:1px solid #5d5d5d;background-color:#f4f4f4;color:#909eff;font-size:12px}"
												 +".tit{background-color:#909EFF;width:140px;font-size:12px;color:white;cursor:default}"
		   +".cs{position:absolute;border:1px solid #909eff;width:142px;left:0px;top:0px;z-index:9999;}"
		   +".shadow{position:absolute;left:0px;top:0px;font-family: Arial Black;font-size:50px;color:#d4d4d4;z-index:1;text-align:center;}";
	outStr="<style type='text/css'>";
	outStr+=style[num];
	outStr+="</style>";
	document.body.insertAdjacentHTML("beforeEnd",outStr);
}

function getValue(obj,value){
	document.getElementById(obj).value=parseInt(myC_Top.cells[2].innerText)+"-"+parseInt(myC_Top.cells[3].innerText)+"-"+value;
}	
function showDate(year,month){ 
	var myDate = new Date(year,month-1,1);
	var today = new Date();
	var day = myDate.getDay();
	var length = new Array(31,30,31,30,31,30,31,31,30,31,30,31);
	length[1] = ((year%4==0)&&(year%100!=0)||(year%400==0))?29:28;
	
	for(i=0;i<myC.cells.length;i++){myC.cells[i].innerHTML = "";}	
	for(i=0;i<length[month-1];i++){
		myC.cells[i+day].innerHTML = (i+1); 
		if(new Date(year,month-1,i+1).getDay()==6||new Date(year,month-1,i+1).getDay()==0){myC.cells[i+day].style.color='red';}
	}	
	myC_Top.cells[2].innerText=year+"年";
	myC_Top.cells[3].innerText=month+"月";
	with(myC.parentNode.previousSibling.style){
		pixelLeft=myC.offsetLeft;
		pixelTop=myC.offsetTop;
		height = myC.clientHeight;
		width = myC.clientWidth;
	}
	myC.parentElement.parentElement.style.height=myC.parentElement.offsetHeight;
	myC.parentElement.previousSibling.innerHTML=year;
}
	
//一些附加函数--------------------
//---------Begin-------------------
function mOver(obj){obj.className = 'mover';}
function mOut(obj){if(obj.className=='mover')obj.className = '';}	  
function addYear(){var year = parseInt(myC_Top.cells[2].innerText);var month = parseInt(myC_Top.cells[3].innerText); year++;showDate(year,month);}
function addMonth(){var year = parseInt(myC_Top.cells[2].innerText);var month = parseInt(myC_Top.cells[3].innerText);month++;if(month>12){month=1;year++;}showDate(year,month);}
function cutYear(){var year = parseInt(myC_Top.cells[2].innerText);var month = parseInt(myC_Top.cells[3].innerText);year--;showDate(year,month);}
function cutMonth(){var year = parseInt(myC_Top.cells[2].innerText);var month = parseInt(myC_Top.cells[3].innerText);month--;if(month<1){month=12;year--;}showDate(year,month);}
function divS(obj){
	if(obj!=divObj){
	  obj.style.backgroundColor="#909eff";
	   obj.style.color='black';
	}
	if(divObj!=null){ 
		divObj.style.backgroundColor='';
		divObj.style.color='';
	}	
	divObj = obj;		
}
	
function divShow(obj){
	if (myC_timeset!=null) clearTimeout(myC_timeset);
	obj.style.display='block';
}
function divHidden(obj){myC_timeset=window.setTimeout(function(){obj.style.display='none'},500);}
//创建年份选择
function createyear(year,obj){
	var ystr;
	var oDiv;
	ystr="<table class='move1' cellspacing=0 cellpadding=2 width="+obj.offsetWidth+">";
	ystr+="<tr><td style='cursor:hand' onclick='createyear("+(year-10)+",myC_Top.cells[2])' align=center>上翻</td></tr>";
	for(i=year-5;i<year+5;i++){
		if(year==i){
			ystr+="<tr style='background-color:#909eff'><td style='color:black;height:16px;cursor:hand' align=center onclick='myC_Top.cells[2].innerText=this.innerText;showDate("+i+",parseInt(myC_Top.cells[3].innerText));myC.parentElement.nextSibling.innerHTML=\"\"'>"+i+"年</td></tr>";
		}else{
			ystr+="<tr><td align=center style='cursor:hand'  onmouseover=divS(this) onclick='myC_Top.cells[2].innerText=this.innerText;showDate("+i+",parseInt(myC_Top.cells[3].innerText));myC.parentElement.nextSibling.innerHTML=\"\"'>"+i+"年</td></tr>";
			ystr+="<tr><td style='cursor:hand' onclick='createyear("+(year+10)+",myC_Top.cells[2])' align=center>下翻</td></tr>";
			ystr+="</table>";
		}
	}
	oDiv = myC.parentElement.nextSibling;
	oDiv.innerHTML='';
	oDiv.innerHTML = ystr;
	
	showDiv(oDiv,obj.offsetTop+obj.offsetHeight,obj.offsetLeft);
}
//创建月份选择
function createmonth(month,obj){
	var mstr;
	var oDiv;
	mstr="<table class='move1' cellspacing=0 cellpadding=2 width="+obj.offsetWidth+">";
	for(i=1;i<13;i++){
		if (month==i){
			mstr+="<tr style='background-color:#909eff'><td style='color:black;height:16px;cursor:hand' align=center onclick='myC_Top.cells[3].innerText=this.innerText;showDate(parseInt(myC_Top.cells[2].innerText),"+i+");myC.parentElement.nextSibling.innerHTML=\"\"'>"+i+"月</td></tr>";
		}else{
			mstr+="<tr><td align=center style='cursor:hand' onmouseover='divS(this)' onclick='myC_Top.cells[3].innerText=this.innerText;showDate(parseInt(myC_Top.cells[2].innerText),"+i+");myC.parentElement.nextSibling.innerHTML=\"\"'>"+i+"月</td></tr>";
		}
	}
	mstr+="</table>";
	oDiv = myC.parentElement.nextSibling;
	oDiv.innerHTML='';
	oDiv.innerHTML = mstr;
	showDiv(oDiv,obj.offsetTop+obj.offsetHeight,obj.offsetLeft);  
}

function showDiv(obj,top,left){
	obj.style.pixelTop=top;
	obj.style.pixelLeft=left;
}
function myCMove(obj){
	if(event.button==1){
		var X = obj.clientLeft;
		var Y = obj.clientTop;
		obj.style.pixelLeft= X+(event.x-myC_x);
		obj.style.pixelTop= Y+(event.y-myC_y);
		window.status=myC_y;
	}
}
function showDiv2(obj){ 
	inputName=obj.id;
	var e=obj;
	var ot = obj.offsetTop;
	var ol=obj.offsetLeft;
	while(obj=obj.parentElement){ot+=obj.offsetTop;ol+=obj.offsetLeft;}
	myC_div.style.pixelTop=ot+e.offsetHeight;
	myC_div.style.pixelLeft=ol;
	myC_div.style.display="block";
}	