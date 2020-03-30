
var preSwapID = "aNode_";
var preNodeID = "imgNode_";
var preBoxID = "box_";
var preBoxFileID = "fileBox_";
var preLinkID = "aTree_";
var preTreeID = "imgTree_";
var preChildID = "divChild_";
var preDiv = "div_";
var preDivBox = "divBox_";

var imgDir = "images/manage/";
var imgNodeLine   = imgDir + "NodeLine.gif";
var imgNodeClosed = imgDir + "NodeClosed.gif";
var imgNodeLeaf   = imgDir + "NodeLeaf.gif";
var imgNodeOpen   = imgDir + "NodeOpen.gif";
var imgTreeClosed = imgDir + "TreeClosed.gif";
var imgTreeLeaf   = imgDir + "TreeLeaf.gif";
var imgTreeOpen   = imgDir + "TreeOpen.gif";

var colorHref = null; //????????


//??????????????????????
function switchNode(nodeID, display)
{
    var nodeStyle = document.getElementById(preChildID + nodeID).style;
    var nodeImage = document.getElementById(preNodeID + nodeID);
    var treeImage = document.getElementById(preTreeID + nodeID);

    if (display == "none" || (display == null && nodeStyle.display == "block"))
    {
        nodeStyle.display = "none";
        nodeImage.src = imgNodeClosed;
        treeImage.src = imgTreeClosed;
		treeImage.height="16";
        return;
    }

    if (display == "block" || (display == null && nodeStyle.display == "none"))
    {
        nodeStyle.display = "block";
        nodeImage.src = imgNodeOpen;
        treeImage.src = imgTreeOpen;
		treeImage.height="16";
        return;
    }
}

function appendChildNode(nodeID,display){
   
    var nodeStyle = document.getElementById(preChildID + nodeID).style;
    var nodeImage = document.getElementById(preNodeID + nodeID);
    var treeImage = document.getElementById(preTreeID + nodeID);
    var waitInfo = document.getElementById(nodeID+"-wt");
    if (waitInfo != null){
         
     if (display == "none" || (display == null && nodeStyle.display == "block"))
     { 
        var handleSubNode=document.getElementById("handleSubNode"); 
        handleSubNode.src=handleURL + nodeID;
        return;
     }
   }
}


//????????????????????????
function switchSubNodes(nodeID, display)
{
    var subElements = document.getElementById(nodeID).all.tags("div");
    if ("none" == display){
      for (var i = 0; i < subElements.length; i ++)
      {
        if (subElements[i].getAttribute("class") == "node")
        {
           
            switchNode(subElements[i].getAttribute("id"), display);
            
        }
      }    
    }else{
     for (var i = 0; i < subElements.length; i ++)
     {
        if (subElements[i].getAttribute("class") == "node" && 
            document.getElementById(subElements[i].getAttribute("id")+"-wt") == null)
        {
        
            switchNode(subElements[i].getAttribute("id"), display);
        }
     }
    }
}
//?????????nodeID????????id
function switchMultiNodes(nodeID, display)
{
    var node = nodeID.split(",");
    for(i=0 ; i< node.length ; i++)
    {
        switchSubNodes(node[i], display);
    }
}


//??????????????
function appendNode(id, parent, text, link, checked)
{

   
    
    var parentElement = document.getElementById(id);
    if (parentElement != null)
    {
        alert("????????????????" + id);
        return;
    }

    parentElement = document.getElementById(parent);
    if (parentElement == null)
    {
        alert("????????????????" + parent);
        return;
    }

    var parentTier = parentElement.getAttribute("tier");
    var childElement = document.getElementById(preChildID + parent);
    
    if (parentTier == null)
    {
        parentTier = -1;
        childElement = parentElement;
    }
    else
    {
      
        if (childElement == null)
        {
           
            parentElement.setAttribute("class", "node");
            var swapElement = createA(preSwapID + parent, "javascript:switchNode('" + parent + "');appendChildNode('" + parent + "')");       
            swapElement.appendChild(createImg(preNodeID + parent, imgNodeClosed));
            parentElement.replaceChild(swapElement, document.getElementById(preNodeID + parent));
            childElement = createDiv(preChildID + parent, "child", null);
            parentElement.appendChild(childElement);
            
        }
        switchNode(parent, "block");
    }

    var leafElement = createDiv(id, "leaf", parentTier + 1);

    for (var i = 0; i < parentTier + 1; i ++)
    {
        leafElement.appendChild(createImg(null, imgNodeLine));
    }
    leafElement.appendChild(createImg(preNodeID + id, imgNodeLeaf));

    if (checked != null)
    {
        if(checked=="file")
        {
                leafElement.appendChild(createInputFile(preBoxFileID + id, preBoxFileID + id, "checkbox", checked));
        }
        else
        leafElement.appendChild(createInput(preBoxID + id, preBoxID + id, "checkbox", checked));
    }

    var linkElement = createA(preLinkID + id, link);
    linkElement.appendChild(createImg(preTreeID + id, imgTreeLeaf));
    linkElement.appendChild(document.createTextNode(text));

    leafElement.appendChild(linkElement);
    childElement.appendChild(leafElement);
    return leafElement;
}

function appendNodeWithParent(id , parentElement , text, link, checked){
    
	var parent = parentElement.getAttribute("id");
    var parentTier = parentElement.getAttribute("tier");
    var childElement = document.getElementById(preChildID + parent);
    
    if (parentTier == null)
    {
        parentTier = -1;
        childElement = parentElement;
    }
    else
    {
      
        if (childElement == null)
        {
           
            parentElement.setAttribute("class", "node");
            var swapElement = createA(preSwapID + parent, "javascript:switchNode('" + parent + "');appendChildNode('" + parent + "')");       
            swapElement.appendChild(createImg(preNodeID + parent, imgNodeClosed));
            parentElement.replaceChild(swapElement, document.getElementById(preNodeID + parent));
            childElement = createDiv(preChildID + parent, "child", null);
            parentElement.appendChild(childElement);
            
        }
        switchNode(parent, "block");
    }

    var leafElement = createDiv(id, "leaf", parentTier + 1);

    for (var i = 0; i < parentTier + 1; i ++)
    {
        leafElement.appendChild(createImg(null, imgNodeLine));
    }
    leafElement.appendChild(createImg(preNodeID + id, imgNodeLeaf));

    if (checked != null)
    {
        if(checked=="file")
        {
                leafElement.appendChild(createInputFile(preBoxFileID + id, preBoxFileID + id, "checkbox", checked));
        }
        else
        leafElement.appendChild(createInput(preBoxID + id, preBoxID + id, "checkbox", checked));
    }

    var linkElement = createA(preLinkID + id, link);
    linkElement.appendChild(createImg(preTreeID + id, imgTreeLeaf));
    linkElement.appendChild(document.createTextNode(text));

    leafElement.appendChild(linkElement);
    childElement.appendChild(leafElement);
    return leafElement;
}
function createA(id, href)
{
    var aElement = document.createElement("span");
    aElement.setAttribute("id", id);
	aElement.className="divBgNone";
	aElement.style.cursor = "pointer";
	//aElement.setAttribute("href","#");
	//alert(href);
	
	href = href.replace("javascript:","");
    aElement["onclick"] = function(){
    	eval(href+";changeColor('"+id+"')");
    }
    return aElement;
}

function createDiv(id, className, tier)
{
    var divElement = document.createElement("div");
    divElement.setAttribute("id", id);
    divElement.setAttribute("class", className);
    if (tier != null) divElement.setAttribute("tier", tier);
    return divElement;
}

function createImg(id, src)
{
    var imgElement = document.createElement("img");
    if (id != null) imgElement.setAttribute("id", id);
    imgElement.setAttribute("align", "absmiddle");
    imgElement.setAttribute("border", "0");
    imgElement.setAttribute("src", src);
    return imgElement;
}

function createInput(id, name, type, checked)
{
    var inputElement = document.createElement("input");
    inputElement.setAttribute("id", id);
    inputElement.setAttribute("name", name);
    inputElement.setAttribute("type", type);
    //inputElement.setAttribute("checked", checked);
    inputElement.onclick=change;
    return inputElement;
}
function createInputFile(id, name, type, checked)
{
    var inputElement = document.createElement("input");
    inputElement.setAttribute("id", id);
    inputElement.setAttribute("name", name);
    inputElement.setAttribute("type", type);
    inputElement.onclick=changeFile;
    return inputElement;
}
//??????????????
function removeNode(id)
{
    var leafElement = document.getElementById(id);
    if (leafElement == null)
    {
        alert("????????????????" + id);
        return;
    }

    var childElement = leafElement.parentNode;
    childElement.removeChild(leafElement);

    if (childElement.getAttribute("class") == "child"
        && childElement.children.length == 0)
    {
        var parentElement = childElement.parentNode;
        var parent = parentElement.getAttribute("id");

        parentElement.setAttribute("class", "leaf");
        parentElement.replaceChild(
            createImg(preNodeID + parent, imgNodeLeaf),
            document.getElementById(preSwapID + parent)
        );
        document.getElementById(preTreeID + parent).src = imgTreeLeaf;
		document.getElementById(preTreeID + parent).height="16";

        parentElement.removeChild(childElement);
    }
}


//??????????????????
function updateNode(id, text)
{
    var linkElement = document.getElementById(preLinkID + id);
    if (linkElement == null)
    {
        alert("????????????????" + id);
        return;
    }

    linkElement.replaceAdjacentText("beforeEnd", text);
}

//------------------------------------------------------------------------------
//????CHECKBOX????????????????????????????
function change()
{
    var e=window.event.srcElement;
    var id=e.id;
    if(e.checked)
    {
        //选中该结点对应层中所有操作

		var parent = e.parentNode;
		for(i=parent.all.length-1;i>=0;i--){
			var Element = parent.all[i];
			if(Element.type=="checkbox" && Element.id !=id){
				Element.checked = true;
				try{
					//回调页面函数
					selectID(Element.id.substring(Element.id.indexOf("_") + 1) , Element.checked);
				}catch(e){

				} 
			}
		}
        //选中其所有上级结点

        while(id.lastIndexOf("-")!=(-1))
        {
            id=id.substring(0,id.lastIndexOf("-"));
            document.getElementById(id).checked=true;
        }
        

    }else
    {
        var temp = document.getElementById("mainDiv");
        //将所有下级结点设为空选

        if(temp !=null )
        {
	        for(i=temp.all.length-1;i>=0;i--)
	        {
	            var Element = temp.all[i];
	            //确定是否下级
	            if(Element.id.substring(0,id.length)==id)
	                {
	                    if(Element.checked)
	                        Element.checked=false;
	                    //空选该结点对应层中所有操作

	                    var div=document.getElementById(preDiv+Element.id.substring(4,Element.id.length));
	                    if(div!=null)
	                    {
		                    for(k=div.all.length-1;k>=0;k--)
		                    {
		                        if(div.all[k].type=="checkbox")
		                            div.all[k].checked=false;
		                    }
		                }
	                }
	        }
	    }
    }
}
//????file????CHECKBOX????????????????????????????
function changeFile()
{
    var e=window.event.srcElement;
    var id=e.id;
    if(e.checked)
    {
        document.getElementById(id).checked=true;
        //??????????????????????????????????????????
        var position=id.lastIndexOf("jmhk-");
        if(position!=-1)
        {
            var par = e.parentNode.parentNode;
            for(var j=0;j<par.all.length;j++)
            {
                if(par.all[j].type=="checkbox")
                    par.all[j].checked=true;
                if(par.all[j].id==id)
                    break;
            }
        }
    while(id.lastIndexOf("-")!=(-1))
    {
            //????????
            id=id.substring(0,id.lastIndexOf("-"));
            var tmp=document.getElementById(id);
            if(tmp!=null)
                if(!tmp.checked)
                    tmp.checked=true;

        }
    }
    else
    {
        //??????????????????????????????????????????
        var position=id.lastIndexOf("jmhk-");
        if(position!=-1)
        {
            var par = e.parentNode.parentNode;
            for(var k=par.all.length-1;k>=0;k--)
            {
                if(par.all[k].type=="checkbox")
                    par.all[k].checked=false;
                if(par.all[k].id==id)
                    break;
            }
        }
        var temp = e.parentNode;
        //??????????????????????
        for(i=temp.all.length-1;i>=0;i--)
        {
            var Element = temp.all[i];
            //????????????
            if(Element.id.substring(0,id.length)==id)
                {
                    if(Element.checked)
                        Element.checked=false;
                }
        }
    }
}

//????DIV????CHECKBOX????????????????????????????
function changeDiv()
{
    var e = window.event.srcElement;
    var id=e.parentNode.id;
    if(e.checked)
    {
        document.getElementById(preBoxID+id.substring(4,id.length)).checked=true;
        while(id.lastIndexOf("-")!=(-1))
        {
            id=id.substring(0,id.lastIndexOf("-"));
            var tmp = document.getElementById(preBoxID+id.substring(4,id.length));
            if(!tmp.checked)
                tmp.checked=true;
        }
    }
}

//????????DIV
function appendDiv(id , moduleName , value , nameCN , parentId )
{
    var parentElement = document.getElementById(parentId);
    var divElement = document.createElement("div");
    divElement.setAttribute("id" , preDiv + id);
    divElement.style.display = "none";
    divElement.style.position = "absolute";
    divElement.style.width = "80px";
    divElement.style.height = "115px";
    //divElement.style.z-index="3";
    if(screen.width==1024)
    {
        divElement.style.left = "250px";
         divElement.style.top = "-223px";
    }
    else
    {
        divElement.style.left = "250px";
        divElement.style.top = "-139px";
    }
    //????????????
    var font = document.createElement("font");
    font.color="#FFFFFF";
    var headElement = document.createTextNode(moduleName);
    font.appendChild(headElement);

    divElement.appendChild(font);
    divElement.appendChild(document.createElement("br"));
    divElement.appendChild(document.createElement("br"));
    //divElement.style.z-index = "";
    var Id = value.split(",");
    var Name = nameCN.split(",");

    for(i=1;i<Id.length;i++)
    {
        var tmp = createCheckBox(preDivBox + Id[i]);
        divElement.appendChild(tmp);
        divElement.appendChild( document.createTextNode( Name[i] ) );
        var brElem = document.createElement("br");
        divElement.appendChild(brElem);
    }
    parentElement.appendChild(divElement);
}

//????????CHECKBOX
function createCheckBox(id)
{
    var checkBoxElement = document.createElement("input");
    checkBoxElement.setAttribute("id", id);
    checkBoxElement.setAttribute("type", "checkbox");
    checkBoxElement.onclick = changeDiv;
    return checkBoxElement;
}
//????tree??div??????
 function setDivHeight(){
   var movID = document.getElementById("mov1");
   movID.style.height=document.body.clientHeight-32;
 }

function changeColor(hrefId){ 
	if (colorHref != null)
	{
        colorHref.className = "divBgNone";
	}
	if (hrefId.indexOf(preSwapID) == -1)
	{
		var hrefElement = document.getElementById(hrefId);
	    hrefElement.className="divBg";
		colorHref = hrefElement;
	}
}