//************************************************
//检查是否为空 
function validateRequire(obj)
 {
  var str = obj.value;
  var reg = /.+/;
  var flag = reg.test(str);
  return flag; 
 }
//************************************************
//检查身份证号码
function validateIdCard(obj)
 {
  var str = obj.value;
  if(str.replace(/ /g,"").length > 0){
  	var reg = /^([0-9]{15}|[0-9]{18})$/;
  	var flag = reg.test(str);
  	return flag;
  }else{
  	return true;
  }
 }
//************************************************
//检查电话号码

function validatePhone(obj)
 {
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var reg = /^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}$/;
  var flag = reg.test(str);
  return flag; 
 }
//************************************************
//检查手机号码

function validateMobile(obj)
 {
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var reg = /^((\(\d{3}\))|(\d{3}\-))?13\d{9}$/;
  var flag = reg.test(str);
  return flag; 
 }
//************************************************
//检查EMAIL
function validateEmail(obj)
 {
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
  var flag = reg.test(str);
  return flag;
 }
//************************************************
//检查URL
function validateUrl(obj)
 {
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var reg = /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
  var flag = reg.test(str);
  return flag;
 }
//***************************************
//检查中文

function validateChinese(obj)
 {
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var reg=/^[\u4e00-\u9fa5](\s*[\u4e00-\u9fa5])*$/;
  var flag = reg.test(str);
  return flag;
 }
//***************************************
//检查特殊字符

function validateUnSafe(obj)
 {
  var reg= /[(\/)(\\)(')(")(<)(>)]/g;
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var flag= reg.test(str);
  flag = !flag;
  return flag;
 }
//***************************************
//检查数字

function validateNumber(obj)
 {
  var reg= /^\d+$/;
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var flag= reg.test(str);
  return flag;
 }
//***************************************
//检查整数

function validateInteger(obj)
 {
  var reg= /^[-\+]?\d+$/;
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var flag= reg.test(str);
  return flag;
 }
//***************************************
//检查实数

function validateDouble(obj)
 {
  var reg= /^[-\+]?\d+(\.\d+)?$/;
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var flag= reg.test(str);
  return flag;
 }
//***************************************
//检查货币格式

function validateCurrency(obj)
 {
  var reg= /^\d+(\.\d+)?$/;
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var flag= reg.test(str);
  return flag;
 }
//***************************************
//检查邮政编码

function validateZip(obj)
 {
  var reg= /^[1-9]\d{5}$/;
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var flag= reg.test(str);
  return flag;
 }
//***************************************
//检查QQ号码
function validateQQ(obj)
 {
  var reg= /^[1-9]\d{4,8}$/;
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var flag= reg.test(str);
  return flag;
 }
//***************************************
//检查英文

function validateEnglish(obj)
 {
  var reg= /^[A-Za-z]+$/;
  var str = obj.value;
  if(str.replace(/ /g,"").length==0){
  	return true;
  }
  var flag= reg.test(str);
  return flag;
 }
//***************************************
//检查英文,数字，下划线或减号

function validateENL(obj)
 {
  var reg= /^[A-Za-z0-9_.\-]*$/;
  var str = obj.value;
  if(str.replace(/^[A-Za-z0-9_.\-]*$/g,"").length == 0){
  	return true;
  }
  
  return false;
 }
//***************************************
//验证主函数

function validateForm()
 {
 	try{
 	var msg="";
  for(i=0;i<Validate.length;i+=3){
  	var obj=document.getElementById(Validate[i]);
  	switch(Validate[i+1]){
  		case "Require":
  			if(!validateRequire(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			
  			break;
  		case "Chinese":
  			if(!validateChinese(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "English":
  			if(!validateEnglish(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "Number":
  			if(!validateNumber(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "Integer":
  			if(!validateInteger(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "Double":
  			if(!validateDouble(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "Email":
  			if(!validateEmail(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "Url":
  			if(!validateUrl(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "Phone":
  			if(!validatePhone(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "Mobile":
  			if(!validateMobile(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "Currency":
  			if(!validateCurrency(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "Zip":
  			if(!validateZip(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "IdCard":
  			if(!validateIdCard(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "QQ":
  			if(!validateQQ(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
  		case "UnSafe":
  			if(!validateUnSafe(obj)){
  				msg+=Validate[i+2]+"\n";
  			}
  			break;
        default:
            var obj2=document.getElementById(Validate[i+1]);
            if(obj.value!=obj2.value){
                msg+=Validate[i+2]+"\n";
            }
            break;
  	}
  }
}catch(err){
	alert(err.description);
	}
  if(msg==""){
  	return true;
  }
  else{
  	alert(msg);
  	return false;
  }
 }

/**
 * 检查长度，该长度为Byte长度
 * obj 检查的对象，可以为具有.value属性对象，或者String对象
 * length 长度
 */
function validateLength(obj, limit){
	if ((!obj) && (obj !== "")){
		return false;
	}

	var str;
	if (typeof obj.value == 'string'){
		str = obj.value;
	}else{
		str = obj;
	}
	if (! typeof str == 'string'){
		return false;
	}
	if (str === ""){
		return true;
	}

	var length = 0;
	var tempCode;
	for (var i = 0;i < str.length; i++){
		tempCode = str.charCodeAt(i);

		if ((!tempCode) && (tempCode !== 0)){
			continue;
		}

		if (tempCode < 256){
			length += 1;
		} else{
			length += 2;
		}
	}

	return (length <= limit);
}