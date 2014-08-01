var ua = navigator.userAgent.toLowerCase();
var isWin = (ua.indexOf("win") > -1);
var isMac = !isWin && (ua.indexOf("mac") > -1);

var isIE =  (isWin && (ua.indexOf('msie') > -1)) || ((document.all != undefined) && (document.getElementById  != undefined));
var isIE7 = isIE && (ua.indexOf('msie 7') > -1);
var isIE8 = isIE && (ua.indexOf('msie 8') > -1);
var isIE9 = isIE && (ua.indexOf('msie 9') > -1);

var isOpera = !isIE && (ua.indexOf('opera') > -1);
var m_timer;
var m_menuID;
var viewer;
var viewer_name_old;
var debug = false;
var viewerName;
var prevent_focus_regainment = false;
var selection_persistance_service_endpoint = new Array();
var owctxMenuIds=new Array();
var contextMenuShortcutEvents=new Array();
var contextMenuShortcutForms=new Array();
var activeListCheckBoxId;
var generalLoadingMask=null;
var OwdJs = {
	onChangeHandler:new Array(),
	addOnChangeListener:function(listener)
	{
		this.onChangeHandler.push(listener);
	},
	fireChangeEvent:function(classname,javaclassname,fieldprovidertype,fieldprovidername,fieldid,value)
	{
		if (this.onChangeHandler.length > 0)
		{
			for (var idx = 0; idx < this.onChangeHandler.length; idx++)
			{
				var handler = this.onChangeHandler[idx];
				if (handler.onChange)
				{
					try
					{
						handler.onChange(classname,javaclassname,fieldprovidertype,fieldprovidername,fieldid,value);
					}
					catch (e)
					{
						if (console)
						{
							console.log("Failed to process onChange method. error " + e);
						}
					}
				}
			}
		}
	},
	loadJsFile : function(config)
	{
		var el = document.createElement('script');
		if (config.async)
		{
			el.async = config.async;
		}
    	el.src = config.src;
    	if (config.type)
    		el.type = config.type;
    	else
    		el.type = "text/javascript";
    	if (config.onload)
    	{
    		el.onload = function()
    		{
    			config.onload();    
    		};
    	}
    	(document.getElementsByTagName('HEAD')[0]||document.body).appendChild(el);
	}
};

function addContextMenuShortcut(pluginId,listId,eventURL,formId)
{
	if (contextMenuShortcutEvents[pluginId]==null)
	{
		contextMenuShortcutEvents[pluginId]=new Array();
	}
	contextMenuShortcutEvents[pluginId][listId]=eventURL;
	
	contextMenuShortcutForms[listId]=formId;

}

function fireContextMenuShortcut(pluginId)
{
	if (contextMenuShortcutEvents[pluginId]!=null)
	{
		if (contextMenuShortcutEvents[pluginId][activeListCheckBoxId]!=null)
		{
			var formId=contextMenuShortcutForms[activeListCheckBoxId];
			document.forms[formId].action=contextMenuShortcutEvents[pluginId][activeListCheckBoxId];
			document.forms[formId].submit();
		}
	}
}

function addSelectionPersistanceEndpoint(listId,service)
{
	if (selection_persistance_service_endpoint[listId]==null)
	{
		selection_persistance_service_endpoint[listId]=new Array();
	}
	
	listEndpoints=selection_persistance_service_endpoint[listId];
	var i;
	for (i = 0; i < listEndpoints.length; i++)
	{
		if (service == listEndpoints[i])
		{
			return;
		}
	}
	listEndpoints[listEndpoints.length]=service;
}

function getEFormsURL(workdeskurl,queueName, wobNum, stepName, stepProcId, securityToken) 
{
	var url = workdeskurl + "/getProcessor?processorType=step&queueName=" + queueName + "&wobNum=" + wobNum + "&stepName=" + stepName + "&stepProcId=" + stepProcId + "&ut="+securityToken;
	return url;
}

function openEFormsWindow(eFormsURL) 
{
	var f = "resizable=yes, location=false, menubar=false, toolbar=false, status=false";
	var w = window.open(eFormsURL, "AWDeFormsWindow", f);
	w.resizeTo(screen.availWidth / 2, screen.availHeight);
	w.moveTo(0, 0);
	w.focus();
}

function openToolsWindow(toolsUrl,width,height) 
{
	var l = (screen.availWidth-width) / 2;
	var t = (screen.availHeight - height) / 2;
	var f = "resizable=true,location=false,menubar=false,toolbar=false,status=false,width="+width+",height="+height+",left="+l+",top="+t;
	var w = window.open(toolsUrl, "AWDToolsWindow", f);
	w.focus();
}

// since 3.1.0.0
function openToolsWindowExt(toolsUrl,width,height) 
{
	var l = (screen.availWidth-width) / 2;
	var t = (screen.availHeight - height) / 2;
	var f = "width="+width+",height="+height+",toolbar=yes,location=yes,directories=yes,status=yes,menubar=yes,scrollbars=yes,copyhistory=yes,resizable=yes,left="+l+",top="+t;
	var w = window.open(toolsUrl, "AWDToolsWindowExt", f);
	w.focus();
}

function onCustomValidation(classname,javaclassname,fieldprovidertype,fieldprovidername,fieldid,value)
{
	// insert your custom field validation code here
	//
	// Parameters:
	// classname : classname of this field
	// javaclassname : name of the java class, the value has been mapped to
	// (e.g. java.lang.string)
	// fieldprovidertype : type of the field provider. One of the following
	// TYPE_META_OBJECT = 0x0001 type of the fieldprovider is a object with
	// metadata
    // TYPE_SEARCH = 0x0002 type of the fieldprovider is search object /
	// template
    // TYPE_CREATE_OBJECT = 0x0010 type of the fieldprovider is a create object
	// dialog or view
    // TYPE_CHECKIN_OBJECT = 0x0020 type of the fieldprovider is a checkin
	// object dialog or view
    // TYPE_SMALL = 0x0040 type of the fieldprovider is small, fields must not
	// use a lot of space
    // TYPE_RESULT_LIST = 0x0080 type of the fieldprovider is a result list
    // TYPE_AJAX = 0x0100 type of the fieldprovider is a result list
	// fieldprovidername : name of the field provider.
	// fieldid : name of the HTML element representing this field
	// value : value of this field
	//
	// return
	// null : field content is valid
	// String : field content is NOT valid. Display returned String as error
	// message
	//
	//
	
	
	/*
	 * Example: if(classname=="ow_Filename"){
	 * 
	 * if(value.indexOf('!')==0){ return(" \"!\" is not allowed as first
	 * character! The name has to start with a letter or a number."); }
	 * 
	 * else{ return(null); } }
	 */
	
	/*
	 * Debug code: alert("onFieldManagerFieldExit\n"+
	 * "classname="+classname+"\n"+ "javaclassname="+javaclassname+"\n"+
	 * "fieldprovidertype="+fieldprividertype+"\n"+
	 * "fieldprovidername="+fieldprovidername+"\n"+ "fieldid="+fieldid+"\n"+
	 * "value="+value );
	 */
	return(null);
}

function onFieldManagerFieldExit(classname,javaclassname,fieldprividertype,fieldprovidername,fieldid,value)
{
    var hotinfo = document.getElementById("HotInfo_"+fieldid);
    if(hotinfo)
    {
	    var checkresult = onCustomValidation(classname,javaclassname,fieldprividertype,fieldprovidername,fieldid,value);
	    if(checkresult)
	    {
	    	hotinfo.innerHTML = checkresult;
	    }
	    else
	    {
	    	hotinfo.innerHTML = "";
	    }
    }
    OwdJs.fireChangeEvent(classname,javaclassname,fieldprividertype,fieldprovidername,fieldid,value);
}

function scrollElementToView(elementName, containerName)
{
    var elementObj = document.getElementById(elementName);
    var containerObj = document.getElementById(containerName);
    if( !(elementObj && containerObj) )
        return;
    if(containerObj.clientHeight != containerObj.scrollHeight)
    {
        if(elementObj.offsetParent == containerObj.offsetParent)
        {
            // container and element have the same offset parent the offsetTop
            // is calculated relative to
            var scroll = (elementObj.offsetTop - containerObj.offsetTop);
            containerObj.scrollTop = scroll - 4;
        }
        else if(elementObj.offsetParent == containerObj)
        {
            // the offsetTop of the element is relative to the container
            containerObj.scrollTop = elementObj.offsetTop - 4;
        }
        else
        {
            // the offsetTop of the element is not calculated relative to the
            // container and both have different offset parents.
            // we just can use the element top instead and hope this works out
            containerObj.scrollTop = elementObj.top - 4;
        }
    }
}

function navigateHREF(wnd,url)
{
// wnd.location.href = url.split("&amp;").join("&");
	wnd.location.href = url.replace(/&amp;/g,"&");
}

/**
 * This function is used for opening resources with mailto:.... or for
 * downloading files. The main idea is not to change the window.location.href
 * because this interferes with XHR requests.
 */
function navigateServiceFrameTO(url)
{
	var serviceFrame = document.getElementById('serviceFrame');
	serviceFrame.src = url.replace(/&amp;/g,"&");
}

function SelectAllCombo(id,fSelect)
{
	var combo = document.getElementById(id);
	
	for (var i = 0;i < combo.options.length;i++)
	{
		combo.options[i].selected = fSelect;
	}
}

function setFocus(id_p) {
	if (document.getElementById) {
		var e = document.getElementById(id_p);
		visible = true;
		if (Ext) {
			
			// Try to see if we are dealing with an Ajax combobox
			var hiddenEl = Ext.get(id_p);
			if(hiddenEl) {
				var ajaxCmb = hiddenEl.next('.OwAjaxComponentCombo');
				if(null != ajaxCmb &&  ajaxCmb.isVisible(true)) {
					ajaxCmb.focus();
					return;
				}
			}

			el = Ext.get(id_p);
			if (el) {
				visible = el.isVisible(true);
			}
		}

		if (e != null && visible) {
			e.focus();
			return;
		} else if (document.getElementsByName) {
			e = document.getElementsByName(id_p)[0];
			if (e != null && visible) {
				e.focus();
				return;
			}
		}
	}
}

function toggleElementDisplay(id)
{
	var details = document.getElementById(id);
        if ( details )
        {
            if (details.style.display == 'none')
            {
                    details.style.display = '';
            }
            else
            {
                    details.style.display = 'none';
            }
        }
}

function enableScriptTable(id) 
{
	var elem;
	if (id)
	{
		elem = document.getElementById(id);
	} 
	if (elem != undefined){
		elem = document;
	}
    elem.oncontextmenu = showContextMenu;
    elem.onmousedown   = onDocumentMouseDown;
    elem.onselectstart = onDocumentSelectStart;
    // window.document.onclick = onMouseClick;
    // window.document.onmouseup = hideMenu;
}

function ignoreMouseDown(e)
{
	// we only observe left button events. cancel anything else
    // Concerning the mouse handling, please see
	// http://unixpapa.com/js/mouse.html section "Identifying Mouse Buttons".
    var leftMouseButtonId = isIE ? 1 : 0;
    var isNotLeftButton = (e.button != leftMouseButtonId) || (isMac && e.ctrlKey);
    var isNotSelectToggle= (isMac && !e.metaKey) || (!isMac && !e.ctrlKey); 
    
    return isNotLeftButton && !e.shiftKey && isNotSelectToggle;
}

function onMimeMouseDown(e){
	e = getEvent(e);

    if(ignoreMouseDown(e))
    {
        return true;
    }
    return onMimeDown(e,false);
}
function onMimeDown(e,breakEventChain)
{
    var firingObj = getFiringObject(e);
    clickRow = getSelectedRowFromObject(firingObj);
    result = executeSelection(e,clickRow,breakEventChain);
    return result;
}
function getKey(e){
    if (isIE)
    {
        return e.keyCode;
    }
    else
    {
        return e.which;
    }
}

function onMimeKeyDown(e){
	e = getEvent(e);
	var key=getKey(e);
	// check for enter - aka. onclick aka. onkeypress==enter
    if( key!=13 )
    {
        return true;
    }
    
    return onMimeDown(e,false);
}

function onKey(event,keyCode,breakEventChain,aFunction)
{
    var e = getEvent(event);
    var key=getKey(e);
    
    if( key!=keyCode )
    {
        return true;
    }
    else
    {
    
        var target = e.target ? e.target : e.srcElement;
        var fReturn=aFunction(event,target);
        if (breakEventChain)
        {
            // prevent default action in non-IE browsers
            if (e.preventDefault)
            {
                e.preventDefault();
            }
            // prevent default action in IE
            e.cancelBubble = true;
            
            return false;
        }
        else
        {
            return fReturn;
        }
    }
    
}
// execute MIME link
function executeMimeLink(theObject){
	if (theObject.target!=null&&theObject.target=="_new") {
		window.open(theObject.href);
	} else {
		window.location=theObject.href;
	}
}
function executeSelection(e,clickRow,breakEventChain) {
    if(clickRow == null)
    {
        // go on with the next event handler
        return true;
    }
    // get the table row that fired the event
    var listId=getListId(clickRow);
    // handle the click event depending on the keyboard modifier
    if(e.shiftKey && previousMouseDownListIndex[listId]!=null)
    {
        handleMouseDownShiftModifier(clickRow,e.ctrlKey);
    }
    else   
    {
    	var modifier=(isMac && e.metaKey) || (!isMac && e.ctrlKey);  
    	if(modifier)
	    {
	        handleMouseDownCtrlModifier(clickRow);
	    }
	    else if (!(isMac && e.ctrlKey))
	    {
	        handleMouseDownNoModifier(clickRow);
	    }
    }
    
    if (breakEventChain)
    {
        // prevent default action in non-IE browsers
        if (e.preventDefault)
        {
            e.preventDefault();
        }
        // prevent default action in IE
        e.cancelBubble = true;
    }
    
    return true;
}
function onDocumentMouseDown(e)
{
    // get the event object depending on the browser
    e = getEvent(e);
    
    if (ignoreMouseDown(e))
    {
        return true;
    }
    // get the object that send this event
    var firingObj = getFiringObject(e);
    // we can ignore every thing else than TR , TD and IMG
    if ( (firingObj.nodeName != "TD") && (firingObj.nodeName != "TR") 
    		&& ((firingObj.nodeName == "IMG" && firingObj.parentNode.nodeName == "A")
    		|| firingObj.nodeName == "A" || firingObj.nodeName == "INPUT") )
    {
        // go on with the next event handler
        return true;
    }
    // get the table row that fired the event
    var clickRow = getSelectedRowFromObject(firingObj);
    if(clickRow == null)
    {
        // go on with the next event handler
        return true;
    }
    var listId=getListId(clickRow);
    // handle the click event depending on the keyboard modifier
    if(e.shiftKey && previousMouseDownListIndex[listId]!=null)
    {
        handleMouseDownShiftModifier(clickRow,e.ctrlKey);
    }
    else if(e.ctrlKey)
    {
        handleMouseDownCtrlModifier(clickRow);
    }
    else {
    	var modifier=(isMac && e.metaKey) || (!isMac && e.ctrlKey);  
    	if(modifier)
	    {
	        handleMouseDownCtrlModifier(clickRow);
	    }
	    else if (!(isMac && e.ctrlKey))
	    {
	        handleMouseDownNoModifier(clickRow);
	    }
    }
    // prevent default action in non-IE browsers
    if (e.preventDefault)
    {
        e.preventDefault();
    }
    // prevent default action in IE
    e.cancelBubble = true;
    // stop the vent handling chain. we have handled this event
    return false;
}

function onDocumentSelectStart(e)
{
    // get the event object depending on the browser
    e = getEvent(e);
    // get the object that send this event
    var firingObj = getFiringObject(e);
    // we can ignore evering thing else than TR and TD objects
    if ( (firingObj.nodeName != "TD") &&  (firingObj.nodeName != "TR") )
    {
        // go on with the next event handler
        return true;
    }
    // get the table row that fired the event
    var clickRow = getSelectedRowFromObject(firingObj);
    if(clickRow == null)
    {
        // go on with the next event handler
        return true;
    }
    // disable text selection if someone clicks in the list box
    return false;
}

// previousMouseDownIndex
var previousMouseDownListIndex = new Array();


function createListName(prefix,listId)
{
	var idPrefix=(prefix!=null)?prefix:"";
	return  idPrefix+"_"+listId;
}

function createListElementId(prefix,index,listId)
{
	return  createListName(prefix,listId)+"_"+index;
}

function selectCheckBoxOnHandle(index,listId,select)
{
	var cbCheckboxId=createListElementId("owcbid",index,listId);
	var tcCheckboxId=createListElementId("owtcid",index,listId);
	if(document.getElementById(cbCheckboxId))
	{
	  document.getElementById(cbCheckboxId).checked = select;
	}
	if(document.getElementById(tcCheckboxId))
	{
	  document.getElementById(tcCheckboxId).checked = select;
	}
}

function handleMouseDownNoModifier(clickRow)
{
	var listId= getListId(clickRow);
    // deselect all
    selectAll(createListName("owmcb",listId),listId,false);
    selectAll(createListName("owtcb",listId),listId,false);
    // select only the current row
	var index =	getRowIndex(clickRow);

	previousMouseDownListIndex[listId] = index;
	selectCheckBoxOnHandle(index,listId,true);
	selectRow2(index,listId,true);
    onRowSelectHandlerInternal(index,clickRow,true);
    persistSelection(listId,index,true);
}

function handleMouseDownCtrlModifier(clickRow)
{
    // get the current state
	var index =	getRowIndex(clickRow);
	var listId= getListId(clickRow);
	previousMouseDownListIndex[listId] = index;
	var cbId=createListElementId("owcbid",index,listId);
	var tcId=createListElementId("owtcid",index,listId);
    var checkboxObject = document.getElementById(cbId) ?
                         document.getElementById(cbId) :
                         document.getElementById(tcId);
    // determine the new state (toggle)
    var newState = checkboxObject ? (checkboxObject.checked ? false : true) : true;
    // select only the current row
    selectCheckBoxOnHandle(index,listId,newState);
    selectRow2(index,listId,newState);
    onRowSelectHandlerInternal(index,clickRow,newState);
    persistSelection(listId,index,newState);
}

function handleMouseDownShiftModifier(clickRow,ctrlModifier)
{
	var listId= getListId(clickRow);
	
    // deselect all if ctrl key is not pressed
    if(!ctrlModifier)
    {
    	selectAll(createListName("owmcb",listId),listId,false);
    	selectAll(createListName("owtcb",listId),listId,false);
    }
    // get the current row
	var index =	getRowIndex(clickRow);
	
	// get the first and the last affected row
    var firstRow = -1;
    var lastRow = -1;
    if(index < previousMouseDownListIndex[listId])
    {
        firstRow = index;
        lastRow = previousMouseDownListIndex[listId];
    }
    else
    {
        firstRow = previousMouseDownListIndex[listId];
        lastRow = index;
    }
    // mark anything in between
    persistlist = "";
    for(var i = firstRow; i <= lastRow; i++)
    {
    	selectCheckBoxOnHandle(i,listId,true);
        selectRow2(i,listId,true);
        // add to persist list
        if(persistlist != "")
            persistlist = persistlist + ",";
        persistlist = persistlist + i;
    }
    persistSelection(listId,persistlist,true);
    onRowSelectHandlerInternal(index,clickRow,true);
}

function getEvent(e)
{
    if(isIE)
    {
            return window.event;
    } 
    else 
    {
            return e;
    }
}

function getFiringObject(e)
{
    if(isIE)
    {
            return window.event.srcElement;
    } 
    else 
    {
            return e.target;
    }
}

function getSelectedRowFromEvent(e)
{
    // find firing object
    var firingObj = getFiringObject(e);

	return	getSelectedRowFromObject(firingObj);
}

function getSelectedRowFromObject(node)
{
    // find selected row
	while ( node && (node.nodeName != "DIV")) 
	{
		if ( node.parentNode && (node.parentNode.nodeName == "TBODY"))
			break;
			node = node.parentNode;
    }
    
    if ( node )
    {
	    if ( -1 != getRowIndex(node) )
			return node;
	}

    return null;
}

function showContextMenu(e)
{
    var row = getSelectedRowFromEvent(e);

    if ( row )
    {
        showMenuInternal(e,row);
        return false;
    }

    return true;
}

function placeObject(obj, left, top)
{
	mainDivPanel = document.getElementById('OwSubLayout_Div_Panel');
	if (mainDivPanel) {
		theParent = obj.parentNode || obj.parent;
		if (theParent) {
			theParent.removeChild(obj);
			menuAlreadyAdded = false;
			menuPosition = -1;
			nodes = mainDivPanel.childNodes;
			for (var i = 0; i< nodes.length;i++) {
				if (nodes[i].id == obj.id) {
					menuAlreadyAdded = true;
					menuPosition = i;
					break;
				}
			}
			if (menuAlreadyAdded) {
				mainDivPanel.replaceChild(obj,nodes[menuPosition]);
			} else {
				mainDivPanel.appendChild(obj);
			}
		}
	}
    obj.style.left = left + 'px';
    obj.style.top  = top + 'px';
}

function getWindowHeight()
{
    var mode = document.compatMode;
    return ( (mode || isIE) && !isOpera ) ? ( (mode == 'CSS1Compat') ? document.documentElement.clientHeight : document.body.clientHeight ) : self.innerHeight;
    // return isIE ? document.body.clientHeight : window.innerHeight;
}

function getWindowWidth()
{
    return isIE ? document.body.clientWidth  : window.innerWidth;
}

function persistSelection(listId,objid,status)
{
	if (objid==null || objid.length==0) {
		return;
	}
	
var persistBlock=function(){
	var listEndpoints=selection_persistance_service_endpoint[listId];
    // do nothing no endpoints are configured
    if(listEndpoints==null || listEndpoints.length==0)
    {
        return;
    }
    // get the XMLHttpRequest object
    var xmlHttp = false;
    if (typeof XMLHttpRequest != 'undefined')
    {
        // Mozilla, Opera, Safari and Internet Explorer 7
        xmlHttp = new XMLHttpRequest();
    }
    if (!xmlHttp)
    {
        // Internet Explorer 6 and older
        try
        {
            xmlHttp  = new ActiveXObject("Msxml2.XMLHTTP.6.0");
        }
        catch(e)
        {
            try
            {
                xmlHttp  = new ActiveXObject("Msxml2.XMLHTTP.3.0");
            }
            catch(e)
            {
                try
                {
                    xmlHttp  = new ActiveXObject("Microsoft.XMLHTTP");
                }
                catch(e)
                {
                    xmlHttp  = false;
                }
            }
        }
    }
    
    // start async request
    if (xmlHttp)
    {
    
    	for (var i = 0; i < listEndpoints.length; i++)
   		{
   	
            var newDate = new Date;
    		var url = listEndpoints[i] + '&objid=' + objid + '&status=' + status+'&time='+newDate.getTime();
    		try
    		{	
    			xmlHttp.open('GET',url,false);
    			xmlHttp.setRequestHeader("Cache-Control", "no-cache");
    			xmlHttp.send(null);
    		}
    		catch(e)
    		{
    		}
   		}
    }
};
 var joined=false;
 window.setTimeout(function(){
	 try{	 
		 persistBlock();
		}catch(e){}
	 joined=true;
 	},1);
 
var sleeping = true;
var now = new Date();
var alarm;
var naptime=20;
var startingMSeconds = now.getTime();
	while(sleeping && !joined){
		alarm = new Date();
		alarmMSeconds = alarm.getTime();
		if(alarmMSeconds - startingMSeconds > naptime){ 
			sleeping = false;			
		}
	}
	if(debug) {
		now = new Date();
		console.log("Sleeped for " + (now.getTime() - startingMSeconds) + "ms");
	}
}

function appendMenuId(menuId){
	alreadyAdded=false;
	for (var i = 0; i < owctxMenuIds.length; i++){
		if (owctxMenuIds[i]==menuId){
			alreadyAdded=true;
			break;
		}
	}
	if(!alreadyAdded){
		owctxMenuIds[owctxMenuIds.length]=menuId;
	}
}
function hideMenu()
{
	if (owctxMenuIds) {
		for (var i = 0; i < owctxMenuIds.length; i++)
		{
			var menuId=createListName('owctx',owctxMenuIds[i]);
			var menuObject = document.getElementById(menuId);
			if (menuObject) {
				menuObject.style.visibility = "hidden";
			}
			showSelectElements();
		}
	}
	
    // unmark row
    // selectAll("owmcb",false);
    // selectAll("owtcb",false);
}
function hideErrorMessage(){
	errorDiv = document.getElementById("OwMainLayout_ERROR");
	if (errorDiv) {
		errorDiv.style.visibility = "hidden";
	}
}
function showMenuInternal(e,row)
{
    e = getEvent(e);
    var listId= getListId(row);
    
    // Find out how close the mouse is to the corner of the window
    var menuObject = document.getElementById(createListName("owctx",listId));
    if(!menuObject) {
    	return;
    }
    var menuObjectWidth = menuObject.offsetWidth;
    var menuObjectHeight = menuObject.offsetHeight;
    
    hideSelectElements();
    
    var coords = calculateContextMenuPosition(e.clientX, e.clientY, menuObjectWidth, menuObjectHeight);
    
    placeObject(menuObject, coords[0], coords[1]);
    menuObject.style.visibility = "visible";

    // get the current state
	var index =	getRowIndex(row);
	
	previousMouseDownListIndex[listId] = index;
	var cbId=createListElementId("owcbid",index,listId);
    var checkboxObject = document.getElementById(cbId) ?
                         document.getElementById(cbId) :
                         document.getElementById(createListElementId("owtcid",index,listId));
    // determine the current state
    var currentState = checkboxObject ? checkboxObject.checked : false;
    // if this row is not selected, unselect all and select only this one
    if(!currentState)
    {
    	selectAll(createListName("owmcb",listId),listId,false);
    	selectAll(createListName("owtcb",listId),listId,false);
        
    	selectCheckBoxOnHandle(index,listId,true);
        selectRow2(index,listId,true);
        // calling this handler here will break the context menu handling
        // onRowSelectHandlerInternal(index,clickRow,true);
        persistSelection(listId,index,true);
    }

}

function calculateContextMenuPosition(eventClientX, eventClientY, menuObjectWidth, menuObjectHeight)
{
    // Find out how close the mouse is to the corner of the window
    var rightedge = getWindowWidth() - eventClientX;
    var bottomedge = getWindowHeight() - eventClientY;
    var menuLeft, menuTop;

    // if the horizontal distance isn't enough to accomodate the width of the
	// context menu
    if (rightedge < menuObjectWidth)
        // move the horizontal position of the menu to the left by it's width
        menuLeft = eventClientX - menuObjectWidth - 5;
    else
        // position the horizontal position of the menu where the mouse was
		// clicked
        menuLeft = eventClientX - 5;

    // same concept with the vertical position
    if (bottomedge < menuObjectHeight)
    {
    	if ( eventClientY < menuObjectHeight)
    	    menuTop = 0;
    	else
            menuTop = eventClientY - menuObjectHeight - 5;
    }
    else
        menuTop = eventClientY - 5 ;
	
	return new Array(menuLeft, menuTop);
}

function onClickItemCheckBox(id,listId)
{
    // mark row as well
    var checkBoxObject = document.getElementById(createListElementId("owcbid",id,listId));

    var rowObject = getSelectedRowFromObject(checkBoxObject);

	var status = checkBoxObject.checked;
	
    if ( (null != rowObject) && (!isRowMultiselect(rowObject)) )
    {
        selectAll(createListName("owmcb",listId),listId,false);
        selectAll(createListName("owtcb",listId),listId,false);
    }
    
    checkBoxObject.checked	= status;
    var tcCheckBoxId=createListElementId("owtcid",id,listId);
    if(document.getElementById(tcCheckBoxId))
        document.getElementById(tcCheckBoxId).checked = status;

    selectRow(rowObject,status);
    onRowSelectHandlerInternal(id,rowObject,status);
    
    persistSelection(listId,checkBoxObject.value,checkBoxObject.checked);
}

function onClickThumbnailCheckBox(id,listId)
{
	// mark row as well
	var checkBoxObject = document.getElementById(createListElementId("owtcid",id,listId));

	var rowObject = document.getElementById(createListElementId("mwrowid",id,listId));
	rowObject = (rowObject == null) ? document.getElementById(createListElementId("swrowid",id,listId)) : rowObject;

	var status = checkBoxObject.checked;

    if ( (null != rowObject) && (!isRowMultiselect(rowObject)) )
	{
		selectAll(createListName("owmcb",listId),listId,false);
		selectAll(createListName("owtcb",listId),listId,false);
	}


	checkBoxObject.checked = status;
	var cbId=createListElementId("owcbid",id,listId);
	if(document.getElementById(cbId))
		document.getElementById(cbId).checked = status;

	selectRow2(id,listId,status);
	onRowSelectHandlerInternal(id,rowObject,status);
    
    persistSelection(listId,checkBoxObject.value,checkBoxObject.checked);
}

function ContextFunction(url,formname)
{
    if ( formname == "" )
    {
    	navigateHREF(window,url);
    }
    else
    {
        var form = document.getElementsByName(formname)[0];
        
        form.action = url;
        form.submit();
    }
}

function selectAll(checkBoxName,listId,select)
{
	var elements = document.getElementsByName(checkBoxName);
	var i;
	var persistlist = "";
	for (  i=0;i < elements.length;i++)
	{
		activeListCheckBoxId=listId;
	    if(elements[i].checked != select)
	    {
            elements[i].checked = select;

            // also select the row
            var rownode = elements[i];
            while ( rownode ) 
            {
                if ( rownode.parentNode && (rownode.parentNode.nodeName == "TBODY") && (rownode.parentNode.className != "OwObjectListViewThumbnails_HeadTableBody") )
                    break;
                rownode = rownode.parentNode;
            }
            selectRow(rownode,select);
		
            // add to persist list
            if(persistlist != "")
                persistlist = persistlist + ",";
            persistlist = persistlist + elements[i].value;
	    }
	}	
	if (persistlist.indexOf('1')>-1 && select==false)
    {
    	select=false;
    }
    // persist at server. we need to deffer this call since we would break the
    // event chain of the Ctrl-A key in Firefox otherwise. Don't ask why...
	// window.setTimeout(function(){persistSelection(listId,persistlist,select);},1);
    
    persistSelection(listId,persistlist,select);
}


function select(row,toggle)
{
	var index =	getRowIndex(row);
	var listId=getListId(row);
	
    // unselect all only if less than 2 are selected
    var elements = document.getElementsByName(createListName("owmcb",listId));
    if(elements.length == 0)
    {
        elements = document.getElementsByName(createListName("owtcb",listId));
    }
    if ( (elements.length < 2) || ( ! isRowMultiselect(row) ) )
    {
        selectAll("owmcb",listId,false);
        selectAll("owtcb",listId,false);
    }

    var status = true;

	
	var cbCheckboxId=createListElementId("owcbid",index,listId);
	var tcCheckboxId=createListElementId("owtcid",index,listId);

    // select checkbox
    var checkboxObject = document.getElementById(cbCheckboxId) ?
                         document.getElementById(cbCheckboxId) :
                         document.getElementById(tcCheckboxId);

    if(checkboxObject)
    {
	    if ( toggle && checkboxObject.checked )
    	    status = false;
    	    
    	if(document.getElementById(cbCheckboxId))
        	document.getElementById(cbCheckboxId).checked = status;

    	if(document.getElementById(tcCheckboxId))
        	document.getElementById(tcCheckboxId).checked = status;
    }

    // selectRow(row,status);
    selectRow2(index,listId,status);
    onRowSelectHandlerInternal(index,row,status);
    
    persistSelection(checkboxObject.value,checkboxObject.checked);
}

// called when user selectes a single row to perform custom actions
function onRowSelectHandlerInternal(fIndex_p,rownode_p,fSelected_p)
{
	try
	{
	   if (onRowSelectHandler)
	   {
		  onRowSelectHandler(fIndex_p,rownode_p,fSelected_p);
	   }
	}
	catch (e)
	{
		// ignore, no handler defined
	}
}

function selectRow(rownode,select)
{
/*
 * if ( select ) { rownode.className = "OwGeneralList_RowSelected"; } else {
 * rownode.className = ((getRowIndex(rownode) % 2) != 0) ?
 * "OwGeneralList_RowEven" : "OwGeneralList_RowOdd"; }
 */
	var rowid = getRowIndex(rownode);
	var listId= getListId(rownode);
	var listrow = getListRow(rowid,listId);
	if(listrow)
	{
		if ( select )
		{
			// store old className
			if(listrow.className != "OwGeneralList_RowSelected")
			{
				listrow.backupClassName = listrow.className;
			}
			// set new selected classname
			listrow.className   = "OwGeneralList_RowSelected";
		}
		else
		{
			// check if we have a backup className set
			if(listrow.backupClassName)
			{
				// use this backup classname set during selection
				listrow.className = listrow.backupClassName;
			}
			else
			{
				// no backup classname.
				listrow.className = ((rowid % 2) != 0) ? "OwGeneralList_RowEven" : "OwGeneralList_RowOdd";
			}
		}
	}
	var tndiv = document.getElementById(createListElementId("owtndiv",rowid,listId));
	if(tndiv)
	{
		tndiv.className = select ? "OwObjectListViewThumbnails_divselected" : "OwObjectListViewThumbnails_div";
    }
}

function getListRow(rowid,listId)
{
	var listrow = document.getElementById("mwrowid_"+listId+"_"+rowid) ? document.getElementById("mwrowid_"+listId+"_"+rowid) : document.getElementById("swrowid_"+listId+"_"+rowid);	
	return listrow;
}

function selectRow2(rowid,listId,select)
{
	var listrow = getListRow(rowid,listId);
	if(listrow)
	{
		if ( select )
		{
			// store old className
			if(listrow.className != "OwGeneralList_RowSelected")
			{
				listrow.backupClassName = listrow.className;
			}
			// set new selected classname
			listrow.className   = "OwGeneralList_RowSelected";
		}
		else
		{
			// check if we have a backup className set
			if(listrow.backupClassName)
			{
				// use this backup classname set during selection
				listrow.className = listrow.backupClassName;
			}
			else
			{
				// no backup classname.
				listrow.className = ((rowid % 2) != 0) ? "OwGeneralList_RowEven" : "OwGeneralList_RowOdd";
			}
		}
	}
	var tndiv = document.getElementById(createListElementId("owtndiv",rowid,listId));
	if(tndiv)
	{
		tndiv.className = select ? "OwObjectListViewThumbnails_divselected" : "OwObjectListViewThumbnails_div";
    }
}

function getRowIndex(rownode)
{
    // row ID has the following syntax <type>_<listId>_<index>
    // the type is 7 characters long
	var typeLessId=rownode.id.slice(8);
	var separatorIndex=typeLessId.indexOf("_");
	if (separatorIndex<0 || separatorIndex>=typeLessId.length-1)
	{
		return -1;
	}
	
    var index = parseInt(typeLessId.substring(separatorIndex+1));

    if ( ! isNaN(index) )
    {
        return index;
    }
    else
    {
        return -1;
    }
}


function getListId(rownode)
{
	// row ID has the following syntax <type>_<listId>_<index>
    // the row type is 7 characters long
	return parseListId(rownode.id,8);
}

function parseListId(idString,typeLength)
{
	// list ID strings have the following syntax <type>_<listId>[_<index>]
    var typeLessId=idString.slice(typeLength);
	var separatorIndex=typeLessId.indexOf("_");
	if (separatorIndex<=0 || separatorIndex>=typeLessId.length)
	{
		separatorIndex=typeLessId.length;
	}
	
    var index = parseInt(typeLessId.substring(0,separatorIndex));

    if ( ! isNaN(index) )
    {
        return index;
    }
    else
    {
        return -1;
    }
}

function isRowMultiselect(rownode)
{
	return rownode.id.charAt(0) == 'm';
}

function toggleCheckBoxes(checkBoxName)
{
	var elements = document.getElementsByName(checkBoxName);
	var i;
    var checked = true;
    var listId=parseListId(checkBoxName,6);
	for (  i=0;i < elements.length;i++)
	{
		if ( elements[i].checked )
                {
                    checked = false;
                    break;
                }
	}	
        
    selectAll(checkBoxName,listId,checked);
}

function keyboardSelectAll()
{
	toggleCheckBoxes(createListName("owmcb",activeListCheckBoxId));
	toggleCheckBoxes(createListName("owtcb",activeListCheckBoxId));
}

function toggleAccessRightsRadio(formname,prefix,value)
{
  if(document.forms[formname])
  {
    
    for(var i = 0; i < document.forms[formname].elements.length; i++)
    {
      var elem = document.forms[formname].elements[i];
      if( (elem.name.substr(0,prefix.length) == prefix) && (elem.value==value) )
      {
        elem.checked = true;
      }
    }
  }
}

function toggleAccessRightsLines(groupid)
{
  var groupcaptionline = document.getElementById("control_"+groupid);
  var groupimgplus = document.getElementById("imgplus_"+groupid);
  var groupimgminus = document.getElementById("imgminus_"+groupid);
  var toggle = groupcaptionline.toggle;
  var tablebody = document.getElementById("OwRoleConfigurationAccessRightsListView_body");
  if(toggle)
  {
      groupcaptionline.toggle = 0;
      groupimgplus.style.display = "";
      groupimgminus.style.display = "none";
  }
  else
  {
      groupcaptionline.toggle = 1;
      groupimgplus.style.display = "none";
      groupimgminus.style.display = "";
  }
  for(var i = 0; i < tablebody.rows.length; i++)
  {
    if(tablebody.rows[i].id.substr(0,groupid.length+1) == groupid+"_")
    {
      if(toggle)
          tablebody.rows[i].style.display = "none";
      else
          tablebody.rows[i].style.display = "";
    }
  }
}

function starTimeOut(e)
{  
	m_timer = window.setTimeout(hideMenu, 600);
}

function clearTimeOut(e)
{
    window.clearTimeout(m_timer);
}

// functions to change backgroundColor in table row
m_oldRow = null;

function overctx(tr) 
{
	if (tr != m_oldRow) 
	{
            var label = document.getElementById(tr.id + "_label");
            label.className = "OwContextMenu_Label_sel";

            tr.className = "OwContextMenu_Item_sel";
	}
}

function outctx(tr) 
{
	if (tr != m_oldRow) 
	{
            var label = document.getElementById(tr.id + "_label");
            label.className = "OwContextMenu_Label";

            tr.className = "OwContextMenu_Item";
	} 
        else 
        {
            var label = document.getElementById(tr.id + "_label");
            label.className = "OwContextMenu_Label_sel";

            tr.className = "OwContextMenu_Item_sel";
	}
}

function clickctx(tr,url,formname) 
{
    tr.className = "OwContextMenu_Item";
    if (m_oldRow) 
    {
        m_oldRow.className = "OwContextMenu_Item";
    }
    
    m_oldRow = tr; 
    ContextFunction(url,formname);
}

// Hides all <select> elements in IE. This is so they don't show above the
// context menu
function hideSelectElements()
{
    if (isIE)
    {
        var selectElements = document.getElementsByTagName("select");
        for (var i = 0; i < selectElements.length; i++)
            selectElements[i].style.visibility = "hidden";
    }
}

// Show all <select> elements (hidden by hideSelectElements() function)
function showSelectElements()
{
    if (isIE)
    {
        var selectElements = document.getElementsByTagName("select");
        for (var i = 0; i < selectElements.length; i++)
            selectElements[i].style.visibility = "visible";
    }
}

function openOfficeDocument(deploymentRoot,dmsid,documentTitle,readOnly,requiredFileExt)
{
	if (window.ZIDI == undefined)
	{
		OwdJs.loadJsFile({
			src :"./js/OfficeLauncher.js",
			onload : function()
			{
				OwdJs.loadJsFile({
					src :"./js/officeIntegration.js",
					onload : function ()
					{
						ZIDI.openOfficeDocument(deploymentRoot, dmsid, documentTitle, readOnly, requiredFileExt);
					}
				});
			}
		});
	}
	else
    {
        ZIDI.openOfficeDocument(deploymentRoot, dmsid, documentTitle, readOnly, requiredFileExt);
    }
}

//@deprecated since 4.1.1.0
function openOfficeDocumentEx(deploymentRoot,dmsid,documentTitle,readOnly,requiredFileExt,owdPath)
{
    openOfficeDocument(deploymentRoot,dmsid,documentTitle,readOnly,requiredFileExt);
}

function openViewerWindow(url,name,dmsid,page)
{
	viewer = window.open(url,name,'scrollbars=1,resizable=1,location=0,menubar=0,toolbar=0,status=0');
	viewer.focus();
}

function openViewerWindowArranged(url,name,dmsid,page,ViewerWidth,ViewerHeight,ViewerTopX,ViewerTopY,WindowWidth,WindowHeight,WindowTopX,WindowTopY,fWithMainWnd_p,units)
{
	// ,location=0,menubar=0,toolbar=0,status=0
    var openParams = "scrollbars=1,resizable=1,location=0,menubar=0,toolbar=0,status=0";
    viewerName = name;
    var height;
    if(units == 0)
    {// pixel
        if ( (ViewerWidth == 0) || (ViewerHeight == 0) )
        {
            // viewer.resizeTo(screen.availWidth / 2, screen.availHeight);
            openParams = openParams + ",width=" + (screen.availWidth/2 - 4);
            if (isIE)
            {
                // openParams = openParams + ",width=" + screen.availWidth/2;
                height = screen.availHeight - (screen.height - screen.availHeight);
                openParams = openParams + ",height=" + height;
            } else {
                
                openParams = openParams + ",height=" + screen.availHeight;
            }
            // viewer.moveTo(screen.availWidth / 2, 0);
            openParams = openParams + ",left=" + screen.availWidth/2;
            openParams = openParams + ",top=0";
        } else {
            // viewer.resizeTo(ViewerWidth, ViewerHeight);
            openParams = openParams + ",width=" + ViewerWidth;
            openParams = openParams + ",height="+ ViewerHeight;
            // viewer.moveTo(ViewerTopX, ViewerTopY);
            openParams = openParams + ",top=" + ViewerTopY;
            openParams = openParams + ",left=" + ViewerTopX;
        }
    }
    else
    {// percent
        if ( (ViewerWidth == 0) || (ViewerHeight == 0) )
        {   // viewer.resizeTo(screen.availWidth / 2, screen.availHeight);
            openParams = openParams + ",width=" + (screen.availWidth/2 - 4);
            if (isIE)
            {   // openParams = openParams + ",width=" + screen.availWidth/2;
                height = screen.availHeight - (screen.height - screen.availHeight);
                openParams = openParams + ",height=" + height;
            } else {
                
                openParams = openParams + ",height=" + screen.availHeight;
            }
        } else {
        // viewer.resizeTo(screen.availWidth / (100/ViewerWidth),
		// screen.availHeight/(100/ViewerHeight));
            if (isIE)
            {
            	openParams = openParams + ",width=" + (screen.availWidth / (100/ViewerWidth) - 10);
                height = screen.availHeight - (screen.height - screen.availHeight);
                height = height/(100/ViewerHeight);
                openParams = openParams + ",height=" + height;
            } else {
            	openParams = openParams + ",width=" + (screen.availWidth / (100/ViewerWidth) - 8);
                height = screen.availHeight;
                height = height/(100/ViewerHeight);
                openParams = openParams + ",height=" + height;
            }
        }
        // viewer.moveTo(screen.availWidth / 2, 0);
        // viewer.moveTo(screen.availWidth / (100/ViewerTopX),
		// screen.availHeight / (100/ViewerTopY));
        if ((ViewerTopX == 0))
        { 
            openParams = openParams + ",left=0";
        } else { 
            openParams = openParams + ",left=" + screen.availWidth / (100/ViewerTopX);
        }
        if((ViewerTopY == 0))
        {
            openParams = openParams + ",top=0";
        } else {
            openParams = openParams + ",top=" + screen.availHeight / (100/ViewerTopY);
        }
    }

    if ( fWithMainWnd_p )
    {
        var winWidth, winHeight, winX, winY;
        var focus = false;
        if ( (WindowWidth == 0) || (WindowHeight == 0) )
        {
            winWidth = screen.availWidth / 2;
            winHeight = screen.availHeight;
            winX = 0; winY = 0;
        }
        else
        {
            if(units == 0)
            {
                winWidth = WindowWidth;
                winHeight = WindowHeight;
                winX = WindowTopX, winY = WindowTopY; 
            }
            else
            {
                winWidth = screen.availWidth / (100/WindowWidth);
                winHeight = screen.availHeight/(100/WindowHeight);
                winX = screen.availWidth / (100/WindowTopX);
                winY = screen.availHeight/(100/WindowTopY);
            }
            focus = true;
        }

        window.resizeTo(winWidth, winHeight);
        window.moveTo(winX, winY);
        if (focus)
        {
            window.focus();
        }
    }
    if(debug)confirm("openParams:\n"+openParams);// for debug only
    try {
    	viewer = window.open('', name, openParams);
        if('about:blank' == viewer.location)
        {
           viewer.location = url;
        }
        else
        {
            if (viewer.ow_view_reloadDocument)//check if window open ADV component
            {
                var arrN = url.split("&");
                var docTitle = "";
                for (var i = 0; i < arrN.length; i++)
                {
                    if (arrN[i].indexOf("docTitle=") >= 0)
                    {
                        docTitle = arrN[i].slice(arrN[i].indexOf("=") + 1, arrN[i].length);
                        break;
                    }
                }
                viewer.ow_view_reloadDocument(dmsid, page, docTitle, null);
            }
            else
            {
                viewer.location = url;
            }
        }
    } catch (ex) {
    	if (debug)
    		alert("Error on opening viewer" + ex.description + "\n" + ex.message + "\nurl="+url+"\nname="+name+"\nopenParams="+openParams);
    }
    try 
    {
        if (viewer != undefined) viewer.focus();
    } catch (err) {
        if (debug) alert("Error during viewer focusing:\n"+err.description+"\n"+err.message);
        try {
            viewer = window.open(url, name, openParams);// try again the previous window was not closed correctly
        } catch (er) {
            alert("error in error.handling :\n" + er.description+"\n"+er.message);
        }
    }
    try
    {
      // resize main window
       if(viewer.addEventListener) {
           viewer.addEventListener('beforeunload', doOnBeforeUnload, false);
       } else if (window.attachEvent) {
           viewer.attachEvent('onbeforeunload', doOnBeforeUnload);
       }
    } catch (err) {
       if (debug)
       {
           alert("Problem with listener registration for viewer window");
       }
       if (console)
       {
        console.log("Problem with listener registration for viewer window\n" + err);
       }
    }
}
    
function doOnBeforeUnload() 
{
    // var pntWindow =parent;
	// pntWindow.moveTo(0, 0);
	// pntWindow.resizeTo(screen.width, screen.height);
}

var IEMainWndXOffset	= 4;
var IEMainWndYOffset	= 132;

var IEViewerXOffset	= 4;
var IEViewerYOffset	= 23;


function SaveWindowPositions(mainWnd,viewerWnd,url)
{
	var ViewerWidth		= isIE ? viewerWnd.document.body.clientWidth + 2 * IEViewerXOffset : viewerWnd.outerWidth;
	var ViewerHeight	= isIE ? viewerWnd.document.body.clientHeight + IEViewerYOffset + IEViewerXOffset : viewerWnd.outerHeight;
	var ViewerTopX		= isIE ? viewerWnd.screenLeft - IEViewerXOffset  : viewerWnd.screenX;
	var ViewerTopY		= isIE ? viewerWnd.screenTop - IEViewerYOffset : viewerWnd.screenY;
	
	var WindowWidth		= isIE ? mainWnd.document.body.clientWidth + 2 * IEMainWndXOffset  : mainWnd.outerWidth;
	var WindowHeight	= isIE ? mainWnd.document.body.clientHeight + IEMainWndYOffset + IEMainWndXOffset : mainWnd.outerHeight;
	var WindowTopX		= isIE ? mainWnd.screenLeft - IEMainWndXOffset : mainWnd.screenX;
	var WindowTopY		= isIE ? mainWnd.screenTop - IEMainWndYOffset : mainWnd.screenY;
	
	// alert("ViewerWidth: " + ViewerWidth + " ViewerHeight: " + ViewerHeight +
	// " ViewerTopX: " + ViewerTopX + " ViewerTopY: " + ViewerTopY + "
	// WindowWidth: " + WindowWidth + " WindowHeight: " + WindowHeight + "
	// WindowTopX: " + WindowTopX + " WindowTopY: " + WindowTopY);

	var linkTo		= url + "&OwViewerWidth=" + ViewerWidth + "&OwViewerHeight=" + ViewerHeight + "&OwViewerTopX=" + ViewerTopX + "&OwViewerTopY=" + ViewerTopY + "&OwWindowWidth=" + WindowWidth + "&OwWindowHeight=" + WindowHeight + "&OwWindowTopX=" + WindowTopX + "&OwWindowTopY=" + WindowTopY;

	navigateHREF(mainWnd,linkTo);
}

/*
 * This function will resize the thumbnails in the thumbnail list views. The
 * thumbnail list views have a navigation to set the dimension of the thumbnails
 * to "small", "medium" and "big".
 */
var currentThumnailTypeConfig = null;
function Thumbnails_setType(type, width, height, scale) {
	currentThumnailTypeConfig = {
		type : type,
		width : width,
		height : height,
		scale : scale
	};

	var thumbnails = document
			.getElementsByName('OwObjectListViewThumbnails_image');
	if (thumbnails) {
		for ( var i = 0; i < thumbnails.length; i++) {
			var thumbnail = thumbnails[i];
			var src = thumbnail.getAttribute('type' + type + 'src');
			//force image reload if URLs are the same
			thumbnail.src = src + '&timestamp=' + new Date();
		}
	}
}

function tbn_updateBoxSizes() {
	if (null == currentThumnailTypeConfig) {
		return;
	}

	var width = currentThumnailTypeConfig.width; 
	var height = currentThumnailTypeConfig.height; 
	var scale = currentThumnailTypeConfig.scale;
	
	var tntables = document
			.getElementsByName('OwObjectListViewThumbnails_table');
	if (tntables) {
		for ( var i = 0; i < tntables.length; i++) {
			var tntable = tntables[i];
			var tntablew = width + 20;
			if (width > 0) {
				tntable.style.width = tntablew + 'px';
			} else {
				tntable.style.width = '100%';
			}
			if (height > 0) {
				tntable.style.height = height;
			} else {
				tntable.style.height = '100%';
			}
		}
	}

	var thumbnails = document
			.getElementsByName('OwObjectListViewThumbnails_image');
	if (thumbnails) {
		for ( var i = 0; i < thumbnails.length; i++) {
			var thumbnail = thumbnails[i];
			thumbnail.removeAttribute('style');
			var old_width = thumbnail.offsetWidth;
			var old_height = thumbnail.offsetHeight;
			var old_src = thumbnail.src;
			if ((old_width != width) || (old_height != height)
					|| (old_src != src)) {
				if (scale) {
					if (width > 0) {
						thumbnail.style.width = width + "px";
						thumbnail.offsetWidth = width;
						// thumbnail.style.height="100%";
					}

					if (height > 0) {
						thumbnail.style.height = height + "px";
						thumbnail.offsetHeight = height;
						thumbnail.style.width = "100%-20px";
					}
				} else {
					thumbnail.style.width = null;
					thumbnail.style.height = null;
					thumbnail.style.minHeight = null;
					thumbnail.offsetWidth = 0;
					thumbnail.offsetHeight = 0;
				}
			}
		}
	}
}

	
/*
 * Function is called when the dialog close button is clicked, so the viewer
 * window will be closed when closing the dialog
 */
function closeViewerWindow()
{
    try {
        if (viewer != undefined)
            viewer.close();
    } catch(err) {
        if (debug)alert("Error during closing viewer window:\n"+err.description+"\n"+err.message);

        try {
             viewer = window.open("", viewerName);
             viewer.close();
        } catch(err2) {
            if (debug)alert("Error window.close() not working:\n"+err.description+"\n"+err2.description);
        }
    }
    return true;
}

/*
 * This function will be called to resize the height of the sublayout window. It
 * will resize the height of the window depending of the browser height. All
 * elements of the sublayout navigation and the sublayout content must be
 * resized as well.
 */
function arrangeRecordHeight(navigationElementId, isAjaxResponse) 
{
    var useDynamicSplit = false;
	var collapsibileWidgetHeight = 0;
	var subLayoutTable = document.getElementById('OwSubLayout_Div');
	if (!subLayoutTable) {
		subLayoutTable = document.getElementById('OwSubLayout_Div_Panel');
		useDynamicSplit = true;
		collapsibileWidgetHeight = 26; // collapsibleWidgetHeight
		// panelBorders = 10;
	}
	var subLayoutMain = document.getElementById('OwSubLayout_MAIN');
	if(!subLayoutTable || !subLayoutMain)
	{
		return;
	}

	var subLayoutNavigation = document.getElementById('OwSubLayout_NAVIGATION');
	var mainLayoutElement = document.getElementById('OwMainLayout');
	var mainLayoutTop = calculateOffsetTop(mainLayoutElement);
	var mainLayoutMainElement = document.getElementById('OwMainLayout_MAIN');
	var mainLayoutMainTop = mainLayoutMainElement.offsetTop;
	var mainEndstrip = document.getElementById('OwMainLayout_MAIN_endstrip').offsetHeight;

	var subLayoutTableTop =  calculateOffsetTop(subLayoutTable);
	if (isAjaxResponse && isIE7) {
		subLayoutTableTop += mainLayoutMainTop;
	}

	var max_MainLayout_Height = (getWindowHeight() - 50);
	if(max_MainLayout_Height < 350)
	{
		max_MainLayout_Height = 350;
	}

	var max_MainLayoutMAIN_Height = max_MainLayout_Height - mainLayoutTop - mainLayoutMainTop - mainEndstrip;
	mainLayoutElement.style.height = max_MainLayout_Height + "px";

	mainLayoutMainElement.style.height = max_MainLayoutMAIN_Height + "px";
	if (isIE7 && isAjaxResponse) {
		if (document.getElementById("OwRecordSubLayout_SEARCHTEMPLATE")) {
			mainLayoutMainElement.style.height = max_MainLayoutMAIN_Height - 5 + "px";
		}
	}

	subLayoutHeight = max_MainLayoutMAIN_Height - subLayoutTableTop + mainLayoutMainTop;

	subLayoutTable.style.height = subLayoutHeight + "px";
	//subLayoutMain.style.height = subLayoutHeight + "px";//height handling now based on CSS

	if(subLayoutNavigation)
	{
	  subLayoutNavigation.style.height = (subLayoutHeight) + "px";
	}

	var contentContainerHeight = subLayoutHeight;
	var registerNavigation = document.getElementById("OwRecordSubLayoutRegister_NAVIGATION");
	if(registerNavigation) {
		contentContainerHeight -= registerNavigation.offsetHeight;
	}
	var warningText = document.getElementById("OwResultListWarningText");
	if(warningText)
	{
		contentContainerHeight = contentContainerHeight - warningText.offsetHeight;
	}

	arrangeListViewHeight(contentContainerHeight, navigationElementId, isAjaxResponse);
	arrangeTreeViewHeight(contentContainerHeight, navigationElementId, isAjaxResponse, useDynamicSplit, collapsibileWidgetHeight, subLayoutHeight);

	subLayoutMain.style.overflow = "visible";
	subLayoutMain.style.minHeight = "100px";
	if (useDynamicSplit) 
	{
		if(subLayoutNavigation)
		{
			subLayoutNavigation.style.height = (subLayoutHeight) + "px";
			subLayoutNavigation.style.width = "100%";
			mainColumnHeader = document.getElementById("OwMainColumnHeader_PreviewPropertyView");
			if (mainColumnHeader) {
				mainColumnHeader.style.width = "100%";
	  		}
		}
	}
}

function arrangeListViewHeight(contentHeight, navigationElementId, isAjaxResponse)
{
	var contentContainer = document.getElementById("OwSublayout_ContentContainer");
	if (!contentContainer)
	{
		return;
	}
	var contentContainerHeight = contentHeight;
	var contentViewSelection = document.getElementById("OwMainColumnHeader_CONTENTVIEW");
	if (contentViewSelection)
	{
		contentContainerHeight = contentContainerHeight - getFullHeightInfo(contentViewSelection);
	}
	contentContainer.style.height = contentContainerHeight + "px";
	contentContainer.style.overflow = "hidden";

	var wrap = document.getElementById("wrap");
	var filter = document.getElementById("OwObjectListViewFilterRow_Filter");
	if (filter)
	{
		contentContainerHeight = contentContainerHeight - filter.offsetHeight;
	}
	// check if we have grid
	var gridPanel = document.getElementById("grid-panel");
	if(gridPanel)
	{
		var gridPanelHeight = contentContainerHeight;
		gridPanel.style.height = gridPanelHeight + "px";
	}

	if(wrap)
	{
	  if (document.getElementById("emptyList")) {
		  wrap.style.height = 30 + "px";
	  } else {
		  var wrapHeight = contentContainerHeight;
		  var footer = document.getElementById("footer");
		  if(footer)
		  {
			  wrapHeight = wrapHeight - footer.offsetHeight;
		  }
		  wrap.style.height = wrapHeight + "px";
	  }
	} else {
		var stickyFooter = document.getElementById("sticky_footer_no_use"); 
		if(stickyFooter) {
			var stickyFooterNoUseHeight = contentContainerHeight;
			stickyFooter.style.height = stickyFooterNoUseHeight + "px";
		}
	}
	var listViewTable = document.getElementById("OwObjectListView_table");
	if(listViewTable)
	{
		listViewTable.style.height = contentContainerHeight + "px";
	}
}

function getFullHeightInfo(elem)
{
	var marginTop = 0, marginBottom = 0;
	var getNumVal =new RegExp("\\d+", "");
	if (elem.currentStyle)
	{
		var styleContentView = elem.currentStyle;
		if (isIE8)
		{
			marginTop = getNumVal.exec(styleContentView["marginTop"])[0];
			marginBottom = getNumVal.exec(styleContentView["marginBottom"])[0];
		}
		else
		{
		    marginTop = getNumVal.exec(styleContentView["margin-top"])[0];
		    marginBottom = getNumVal.exec(styleContentView["margin-bottom"])[0];
		}
	}
	else if (window.getComputedStyle)
	{
		var styleContentView = document.defaultView.getComputedStyle(elem,null);
		if (isIE8)
		{
			marginTop = getNumVal.exec(styleContentView.getPropertyValue("marginTop"))[0];
			marginBottom = getNumVal.exec(styleContentView.getPropertyValue("marginBottom"))[0];		}
		else
		{
			marginTop = getNumVal.exec(styleContentView.getPropertyValue("margin-top"))[0];
			marginBottom = getNumVal.exec(styleContentView.getPropertyValue("margin-bottom"))[0];
		}
		
	}

	return parseInt(marginTop) + parseInt(marginBottom) + elem.offsetHeight;
}

function arrangeTreeViewHeight(contentHeight, navigationElementId, isAjaxResponse, useDynamicSplit, collapsibileWidgetHeight, availableHeight)
{
	var navigationElement = document.getElementById(navigationElementId);
	if (navigationElement==null && !useDynamicSplit) {
		// maybe we use the EXTJS tree as navigation element.
		navigationElement = document.getElementById('extjs-tree-panel');
	}
	if(navigationElement)
	{
		navigationElementTop = calculateOffsetTop(navigationElement);
		var navigationElementHeight = (availableHeight - navigationElementTop - 12);
        navigationElementHeight += navigationElementTop - 26 - collapsibileWidgetHeight;
		// if there is element #OwRecordPreviewPropertyView, devide the
		// height for this two elements
		var owRecordPreviewPropertyViewElement = document.getElementById("OwRecordPreviewPropertyView");
		if(owRecordPreviewPropertyViewElement && owRecordPreviewPropertyViewElement.offsetHeight > 0){
            if(owRecordPreviewPropertyViewElement.offsetHeight + navigationElement.offsetHeight > navigationElementHeight && owRecordPreviewPropertyViewElement.offsetHeight > navigationElementHeight/2){
                navigationElement.style.height = navigationElementHeight/2 + "px";
                owRecordPreviewPropertyViewElement.style.height = navigationElementHeight/2 + "px";
            }else{
                // check if there is a horizontal scroll bar
                if(owRecordPreviewPropertyViewElement.offsetWidth > navigationElement.offsetWidth){
                    // add 17 px because if ie7 scroll bar bug
                    owRecordPreviewPropertyViewElement.style.width = (owRecordPreviewPropertyViewElement.offsetWidth + 17) + "px"; 
                }
                navigationElement.style.height = (navigationElementHeight - owRecordPreviewPropertyViewElement.offsetHeight) + "px";
            }
		}else {
            navigationElement.style.height = navigationElementHeight + "px";
        }
	}	

	if(document.getElementById('extjs-tree-panel'))
	{
		var width = 0;
		var recordPropView = document.getElementById('OwRecordPreviewPropertyView');
		if (recordPropView) {
			width = recordPropView.offsetWidth;
		}
		if (width!=0) {
			document.getElementById('extjs-tree-panel').style.width = width+"px";
		}
	}
}
/**
 * Function that calculate the correct offset top for an element, used for IE7
 * offset top problem.
 * 
 * @param {Object} element - the DOM element
 * @return the offset top
 */
function calculateOffsetTop(element) {
	result = 0;
	if (element) {
		result = element.offsetTop;
		var isIE7 = isIE && !isIE8;
		if (isIE7) {
			if (element.parentNode) {
				parentElement = element.parentNode;
				if (parentElement.id.indexOf('ext-')!=-1) {
					result +=parentElement.offsetTop;
				}
			}
		} 
	}
	return result;
}
function openCenteredDialogWindow(url, title, width, height) {
    var left = parseInt((screen.availWidth/2) - (width/2));
    var top = parseInt((screen.availHeight/2) - (height/2));
    var windowFeatures = 'width=' + width + ',height=' + height + ', toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=no, scrollbars=no,left=' + left + ',top=' + top + 'screenX=' + left + ',screenY=' + top;
    var dialog = window.open(url, title, windowFeatures);
    dialog.focus();
    return dialog;
}
// -----<trim string>--------------------------------------------------------
function trim(stringToTrim) {
	if (stringToTrim!=null) {
		return stringToTrim.replace(/^\s+|\s+$/g,"");
	} else {
		return null;
	}
}
function jumpTo(locationURL,currentPage,maxPage) {
    var page = document.getElementById("navigationPage").value;
    if (page != currentPage) {
        if (page>0 && page<=maxPage || maxPage == -1) {
    	    document.location=locationURL+(page-1);
    	} else {
    	    document.getElementById("navigationPage").value=currentPage;
    	}  
    }
}
function processkey(event,theForm,locationURL,currentPage,maxPage){
     if(event==null) event=window.event;
     if (event && event.keyCode == 13) {
       jumpTo(locationURL,currentPage,maxPage);
       event.cancelBubble=true;
       return false;
     }
     return true;
}
function fixMessageHeight(isVisible) {
	if (Ext.isIE7) {
		msgContainer = Ext.get("OwMainLayout_MESSAGE_container");
		if (msgContainer) {
			if (isVisible) {
				msgContainer.addClass("OwMessageContainerVisible");
				msgContainer.removeClass("OwMessageContainerInvisible");
			}else {
				msgContainer.removeClass("OwMessageContainerVisible");
				msgContainer.addClass("OwMessageContainerInvisible");
			}
		}
	}
}
function appendToUrl(url,append)
{
  url = url.replace(/&amp;/g,"&");
  var hashPos = url.indexOf('#');
  if(hashPos != -1)
  {
    url = url.substring(0,hashPos);
  }
  return(url+append);
}

function usesEXTJSGrid(initialized)
{
	if (initialized)
	{
		return (typeof(window['OwObjectListViewEXTJSGrid']) != 'undefined' && window['OwObjectListViewEXTJSGrid'].config.initialized);
	}
	else
	{
		return (typeof(window['OwObjectListViewEXTJSGrid']) != 'undefined');
	}
}


/**
 * Selects all elements in grid, if grid exists
 */
function ExtjsGrid_SelectAll()
{
	if (usesEXTJSGrid()) {
    	OwObjectListViewEXTJSGrid.selectAll();
    }
}
/**
 * Go to next page in grid, if grid and next page exists
 */
function ExtjsGrid_nextPage()
{
	if (usesEXTJSGrid()) {
    	OwObjectListViewEXTJSGrid.nextPage();
    }
}
/**
 * Go to previous page in grid, if grid and previous page exists
 */
function ExtjsGrid_previousPage()
{
	if (usesEXTJSGrid()) {
    	OwObjectListViewEXTJSGrid.previousPage();
    }
}
/**
 * Go to first page in grid, if grid exists
 */
function ExtjsGrid_firstPage(key,event)
{
	if (usesEXTJSGrid()) {
    	OwObjectListViewEXTJSGrid.firstPage(key,event);
    }
}
/**
 * Go to last page in grid, if grid exists
 */
function ExtjsGrid_lastPage(key,event)
{
	if (usesEXTJSGrid()) {
    	OwObjectListViewEXTJSGrid.lastPage(key,event);
    }
}
/**
 * Mark first row in grid, if grid exists
 */
function ExtjsGrid_firstRow()
{
	if (usesEXTJSGrid()) {
    	OwObjectListViewEXTJSGrid.firstRow();
    }
}
/**
 * Mark last row in grid, if grid exists
 */
function ExtjsGrid_lastRow()
{
	if (usesEXTJSGrid()) {
    	OwObjectListViewEXTJSGrid.lastRow();
    }
}
/**
 * Execute the plugin event, if grid exists
 * 
 * @param pluginid
 */
function ExtjsGrid_PluginEvent(pluginid)
{
	if (usesEXTJSGrid()) {
    	OwObjectListViewEXTJSGrid.pluginEvent(pluginid);
	}
}


/**
 * Select language 
 * 
 */
function selectLanguage()
{
	var elemId = 'language_select';
	var lang = null;
	if (Ext &&  Ext.getCmp(elemId))
	{
		var combo = Ext.getCmp(elemId);
		lang = combo.getValue();
	}
	else
	{
		lang = document.getElementById(elemId).value;
	}
	
    window.location.href="?owloc="+lang;
}

/**
 * 
 * Thumbnails layout
 * 
 */
var loadedThumbCounter=0;

function mergeNodes(a, b) {
    return [].slice.call(a).concat([].slice.call(b));
}

function getThumbnailDivs()
{
	var searchedClasses =["OwObjectListViewThumbnails_div","OwObjectListViewThumbnails_divselected"];
	if (document.getElementsByClassName) {
	    return mergeNodes(document.getElementsByClassName(searchedClasses[0]),document.getElementsByClassName(searchedClasses[1]));
	} else {
	  var i = 0;
	  var divs = document.getElementsByTagName("div");
	  var thumbnailDivs=[];
	  while (element = divs[i++]) {
	    if (searchedClasses.indexOf(element.className) >= 0 ) {
	    	thumbnailDivs.push(element);
	    }
	  }
	  return thumbnailDivs;
	}
}

function findVariableRows(item)
{
	var pluginRowClass="OwObjectListViewThumbnails_plugin_row";
	var imageRowClass="OwObjectListViewThumbnails_image_row";
	var nameRowClass="OwObjectListViewThumbnails_name_row";
	var rows=item.getElementsByTagName("tr");
	var pluginRow=null;
	var imageRow=null;
	var nameRow=null;
	for(var j = 0; j < rows.length; j++){
	    if(rows[j].className == pluginRowClass){
			pluginRow=rows[j];
	    } else if(rows[j].className == imageRowClass){
	    	imageRow=rows[j];	
	    } else if(rows[j].className == nameRowClass){
	       	nameRow=rows[j];
	    }
	    if (pluginRow!=null && imageRow!=null && nameRow!=null)
	    {
	    	break;	
	    }
	}
	
	return [pluginRow,imageRow,nameRow];
}

function onThumbReady(expectedCount)
{
	loadedThumbCounter++;
	if (expectedCount<=loadedThumbCounter)
	{
		loadedThumbCounter=0;
		tbn_updateBoxSizes();
		
		var items=getThumbnailDivs();
		var minHeight=0;
		var maxVariableHeight=0;
		var maxVariableWidth=-1;
		
		for (var i = 0; i < items.length; i++){
			items[i].removeAttribute("style");
			
			var rows=findVariableRows(items[i]);
			if (rows[0]!=null && rows[1]!=null)
			{
				rows[1].removeAttribute("style");
				var height=rows[0].offsetHeight+rows[1].offsetHeight;
				if (height>maxVariableHeight)
				{
					maxVariableHeight=height;
				}
				
				var widthReference=rows[1].getElementsByTagName("img")[0];
				if(widthReference!=null)
				{
					var width=widthReference.offsetWidth+10;
					if (width>maxVariableWidth)
					{
						maxVariableWidth=width;
					}
				}
			}
			if (items[i].offsetHeight>minHeight)
			{
				minHeight=items[i].offsetHeight;
			}
		}

		for (var i = 0; i < items.length; i++){
			//items[i].style.minHeight=minHeight+"px";
			//if (isIE)
			{
				//items[i].style.height=minHeight+"px";
			}
			
			var rows=findVariableRows(items[i]);
			if (rows[0]!=null && rows[1]!=null)
			{
				var height=rows[0].offsetHeight+rows[1].offsetHeight;
				var diff=maxVariableHeight-height;
				// See bug #OWD-5270
				// If we do not explicitly set the height of the row, some browser will not properly compute the size of the enclosing container. 
				if (diff>=0)
				{
					rows[1].style.height=""+(rows[1].offsetHeight+diff)+"px";
					//console.log('i:' + i + ' -> ' + (rows[0].offsetHeight + rows[1].offsetHeight + rows[2].offsetHeight));
				}
			}
			
			var width=-1;
			if (rows[1]!=null)
			{
				var imageElement=rows[1].getElementsByTagName("img")[0];
				if (imageElement!=null)
				{
					width=imageElement.offsetWidth;
				}
			}
			
			if (rows[0]!=null)
			{
				var pluginsTD=rows[0].getElementsByTagName("td")[0];
				pluginsTD.style.width=""+maxVariableWidth+"px";
			}
			
			if (width>0 && rows[2]!=null)
			{
				//TODO : set uniform width ? 
				//items[i].style.width=""+(maxVariableWidth+10)+"px";
			
				var nameParagarph=rows[2].getElementsByTagName("p")[0];
				if (nameParagarph!=null)
				{
					//nameParagarph.style.width=width+"px";
					
					//TODO : set uniform width ? 
					nameParagarph.style.width=maxVariableWidth+"px";
				}
			}
			// by explicitly settin the height of the div, we make sure we do not get into SubPixel problems
			items[i].style.height = "" + (items[i].offsetHeight) + "px";
		}
	}
}