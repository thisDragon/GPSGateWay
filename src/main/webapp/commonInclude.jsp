<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script language="javascript">
	var M_BASEPATH = "<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"%>";
	//扩展String类方法treplaceAll()，替换所有的字符串
	//参数：原字符串，目标字符串
	String.prototype.replaceAll = function(sOldStr,sNewStr){
		var toReplace = new RegExp(sOldStr,"g");
		return this.replace(toReplace, sNewStr);
	}
	//扩展String类方法toChinese()，替换英文符号为中文符号
	//替换目标：< > ' "
	String.prototype.toChinese = function(){
		var nPos = -1;
		var bOdd = true;
		var sResult = "";
	
		if (this == "") return "";
		//1.替换小于号
		sResult = this.replace(/</g,"＜");
		//2.替换大于号
		sResult = sResult.replace(/>/g,"＞");
		//3.替换双引号
		nPos = sResult.indexOf("\"");
		while (nPos != -1){
			if (bOdd){
				sResult = sResult.replace("\"" , "“");
				bOdd = !bOdd;
			}else{
				sResult = sResult.replace("\"","”");
				bOdd = bOdd;
			}
			nPos = sResult.indexOf("\"");
		}
	
		//4.替换单引号
		bOdd = true;
		nPos = sResult.indexOf("\'");
		while (nPos != -1){
			if (bOdd){
				sResult = sResult.replace("\'","‘");
				bOdd = !bOdd;
			}else{
				sResult = sResult.replace("\'","’");
				bOdd = bOdd;
			}
			nPos = sResult.indexOf("\'");
		}
		
		return sResult;
	}
	//扩展String类方法isDate()，判断字符串是否标准日期格式（YYYY-MM-DD）
	//返回值：true/false
	String.prototype.isDate = function(){
		if (this=="") return true;
	
		var asDate = new Array(); 
		asDate = this.split("-");
	
		//如果分割成三段，则表示在格式上符合日期格式
		if (asDate.length!=3) return false; 
	
		//通过了以上的判断，说明在格式上符合日期格式
		var sYear = asDate[0];
		var sMonth = asDate[1];
		var sDay = asDate[2];
	
		//年月日只要有一个非数字，就不符合日期格式
		if (isNaN(sYear) || isNaN(sMonth) || isNaN(sDay)) return false;
		//年月日只要有一个空字符串，就不符合日期格式
		if (sYear=="" || sMonth=="" || sDay=="") return false;
		//年份只能是四位数，月份不能大于两位数，天也不能大于两位数
		if (sYear.length!=4 || sMonth.length>2 || sDay.length>2) return false;
	
		//以上都符合时，说明在格式上已经合格，接下来再判断日期是否合法
		sYear = sYear.substring(0,1)=="0"?sYear.substring(1,sYear.length):sYear;
		sMonth = sMonth.substring(0,1)=="0"?sMonth.substring(1,sMonth.length):sMonth;
		sDay = sDay.substring(0,1)=="0"?sDay.substring(1,sDay.length):sDay;
		var nYear = parseInt(sYear);
		var nMonth = parseInt(sMonth);
		var nDay = parseInt(sDay);
		if (nDay>0 && nDay<32 && nMonth>0 && nMonth<13 && nYear>999){
			//要求：年份必须是四位数的数字；月份必须是1到12的自然数；天数必须是1到31的自然数；
			if (nMonth==2){
				//如果是2月份，需要判断闰年的情况
				//闰年是29天，普通的是28天；
				if (nYear%4==0 && (nYear%100!=0 || nYear%400==0)){
					return (nDay<=29)?true:false;
				}else{
					return (nDay<=28)?true:false;
				}
			}else{
				//除了2月份外，其它月份需要判断大月和小月的天数
				//1、3、5、7、8、10、12月份为31天
				//4、6、9、11月份为30天
				if ((nMonth<8 && nMonth%2!=0)||(nMonth>7 && nMonth%2==0)){
					return (nDay<=31)?true:false;
				}else{
					return (nDay<=30)?true:false;
				}
			}
		}else{
			return false;
		}
	}
	String.prototype.toDate = function(){
		var vDate = this.split('-');
  		var newDate = new Date(vDate[0],vDate[1],vDate[2]); 
  		return newDate;
	}
	String.prototype.hasHTMLCode = function(){
		if (this.indexOf("&")>-1 || this.indexOf("'")>-1 || this.indexOf("<")>-1 || this.indexOf(">")>-1 || this.indexOf("\"")>-1){
			return true;
		}else{
			return false;
		}
	}
</script>