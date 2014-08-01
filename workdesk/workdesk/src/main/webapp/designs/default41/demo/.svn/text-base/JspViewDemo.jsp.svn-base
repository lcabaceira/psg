<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*, com.wewebu.ow.server.util.*" 
    autoFlush   ="true"
    pageEncoding="utf-8"
    language="java"
    contentType="text/html; charset=utf-8"
%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div id="OwMainContent">
<p><%=m_View.getContext().localize1("jsp.demo.JspViewDemo.usage","Use the plugin: %1 to display an arbitrary JSP-Page.","<b>OwINTOPJspMasterDocument</b>") %></p>
<p><%=m_View.getContext().localize1("jsp.demo.JspViewDemo.pageref","See %1.","<b>demo/JspViewDemo.jsp</b>") %></p>
<fieldset><legend>Test Clipboard Update-Properties</legend>
 <ul class="list">
	<li><a title="opensearch: eFileSearch with one search property" href=".?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=search&plugid=com.wewebu.ow.Search&stname=eFileSearch&maxsize=10&prop_ow_Filename=*">
	  Open Search "eFileSearch" (Only Search/Request Properties)
	 </a></li>
	<li><a title="opnesearch: JspFormsTest with one search and three update properties" href=".?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=search&plugid=com.wewebu.ow.Search&maxsize=10&stname=JspFormsTest&uprop_ow_Filename=hello&uprop_ID=110&uprop_Address=Suche3&uprop_ow_Filename=Toll">
	  Open Search "JspFormsTest" (Search and Update Properties)
	 </a></li>
	<li><a title="openrecord: dummy/akte4 with three update properties (Date)" href=".?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=openrecord&dmsid=owdm,owfi,/dummy/dummyarchiv/akte4/&uprop_ID=110&uprop_Address=Nice%20House&uprop_ow_Last-Modified=2008-08-08T08:08:00%2B01:00">
	  Open update "dummy/akte4", with Date (08. August 2008 08:08:00 GMT +1)
	 </a></li>
	<li><a title="openrecord: dummy/akte1/Vertrag_3 with three update properties (Date)" href=".?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=openrecord&dmsid=owdm,owfi,/dummy/dummyarchiv/akte1/Vertrag_3&uprop_ID=500&uprop_Address=Nice%20House&uprop_Status=true">
	  Open update "dummy/akte1/Vertrag_3", with Boolean (Status=true)
	 </a></li>
 </ul>
 <hr />
 <ul class="list">
 	<li><a title="error-openrecord: dummy/akte4 with three update properties" href=".?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=openrecord&dmsid=owdm,owfi,/dummy/dummyarchiv/akte4/&uprop_ID=1,2&uprop_Address=MyAddress&uprop_ow_Filename=Toll">
 	  Open update(ParseError ID = 1,2 only Integer allowed)
 	 </a></li>
 	<li><a title="error-opensearch: JspFormsTest only with update properties" href=".?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=search&plugid=com.wewebu.ow.Search&maxsize=10&stname=JspFormsTest&uprop_ID=OwFieldName&uprop_Address=Suche2&uprop_ow_Filename=Toll">
 	  Open Search (Only update Properties, ParseError ID = OwFieldName, ID should be an Integer)
 	 </a></li>
 	<li><a title="error-opensearch: eFileSearch with one search and update property" href=".?owappeid=com.wewebu.ow.RemoteControl.Doc&ctrlev=search&plugid=com.wewebu.ow.Search&stname=eFileSearch&maxsize=10&uprop_ow_Last-Modified=2008-08-08">
 	  Open Search (Only Date Properties, ParseError Date "2008-08-08" missing Time Definitions)
 	 </a></li>
 </ul>
</fieldset>

</div>