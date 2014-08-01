function DependentComboBox(formName, url, comboboxId, arrayDependentIds)
{
	this.formName = formName;
	this.url = url;
	this.comboboxId = comboboxId;
	this.arrDependentIds = arrayDependentIds;
	this.onSelect = function ()
	{
		var path = getUrlPath(url);
		var urlParamSplit = path.split("?");
		path = urlParamSplit[0];
		var params = "";
		if (urlParamSplit.length > 1)
		{
			params = urlParamSplit[1];
		}

		var arrFormParams = getFormValues(formName);
		if (arrFormParams.length > 0)
		{
			params = params + "&" + arrFormParams.join("&");
		}

		var xmlHttp = new XMLHttpRequest();
		xmlHttp.open("POST", path, true);
		xmlHttp.elemId=comboboxId;
		xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xmlHttp.onreadystatechange = callAjaxHandler;
		xmlHttp.send(params);
	};

	this.onChange = function(classname,javaclassname,fieldprovidertype,fieldprovidername,fieldid,value)
	{
		if (this.arrDependentIds.indexOf(classname) >= 0)
		{
			this.onSelect();
		}
	};

}

function callAjaxHandler()
{
	if (this.readyState==4 && this.status==200)
	{
		var resultArray = eval("(" + this.responseText + ")");
		if (Ext &&  Ext.getCmp(this.elemId))
		{
			var combo = Ext.getCmp(this.elemId);
			handleExtJs(combo, resultArray);
		}
		else
		{
			var combo = document.getElementById(this.elemId);
			handleHtml(combo, resultArray);
		}
	}
}

function handleExtJs(combo, resultArray)
{
	var selected = combo.value;
	resultArray.fields=['value', 'label'];
	resultArray.storeId=combo.store.storeId;
	combo.clearValue();
	combo.bindStore(new Ext.data.ArrayStore(resultArray));
	combo.value = selected;
}

function handleHtml(optionParent, result)
{
	var selected = optionParent.value;
	var data = result.data;
	var strInnerHtml = "";
	for ( var i=0; i < data.length; i++ )
	{
		strInnerHtml += "<option value=\"";
		strInnerHtml += data[i][0];
		strInnerHtml += "\"";
		if (data[i][0] == selected)
		{
			strInnerHtml += " selected";
		}
		strInnerHtml +=">";
		strInnerHtml +=data[i][1];
		strInnerHtml +="</option>\n";
	}
	optionParent.innerHtml = strInnerHtml;
}

function getUrlPath(url)
{
	var urlPath = url.substr(url.indexOf("://") + 3);
	urlPath = urlPath.substr(urlPath.indexOf("/"));
	return urlPath;
}

function getFormValues(formName)
{
	var formElements = new Array();
	var form = document.forms[formName];
	for ( var i = 0; i < form.elements.length; i++ )
	{
	   var e = form.elements[i];
	   if (e.type != "button")
	   {
		   if (e.name != undefined && e.name.length > 0)
		   {
			   formElements.push(encodeURIComponent(e.name) + "=" + encodeURIComponent(e.value));
		   }
	   }
	}
	return formElements;
}