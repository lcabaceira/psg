<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Help for Settings
</span>

<p class=OwHelpIntro>
    <br>Each user can customize Alfresco Workdesk according to his personal needs.
    <br> You can modify different settings for each available plug-in (e.g. eFile Management, Search, ...) and for Alfresco Workdesk in general. 
</p>
<br>

<span class=OwHelpTopic2>
	General Settings
</span>
<br>
<br>

<p class=OwHelpText>   
    The general setting you define here apply for the whole Alfresco Workdesk. You can e.g. configure in which plugin (Search, eFile Management, P8 BPM, ...) 
    Wordesk should start after login. <br> <br>
    By default, the clipboard's content is displayed in the upper part of Wordesk. You can activate or deactivate this displaying here.<br><br>
    With the page size you set the number of entries in a document, work item or result list. If there are more entries than defined here,
    Wordesk provides several pages and with the possibility to navigate between them (e.g. 1-20, 21-40, ...). These settings apply for all list types. <br><br>
    Possibly, other setting options are available here. Please contact your administrator if you have further questions. <br><br>

<span class=OwHelpTopic3>
    <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/settings.png" alt="" title=""/>  
</span>
</p>
<br>
<br>
    
<span class=OwHelpTopic2>
	Settings for Different Plugins<br>
</span>
<br>

<p class=OwHelpText>
    From a list of available attributes, you can choose which attributes should appear as column titles for a certain plugin. With the arrows to the 
    left and to the right you add and delete marked attributes. With the arrows up and down you can change the order of 
    appearance. The highest attribute at the list of selected attributes will be the first column from the left.
	<br clear="all">
	<img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/columns.png" alt="" title=""/>
</p>
<br>

<p class=OwHelpText>
    For case management, you can define colleague as your proxy. Choose your proxy and a period of substitution. 
    When you activate it, all work items from you personal inbox can be viewed and processed by your proxy, too.
    <br clear="all"><br>
    <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/proxy.png" alt="" title=""/>
</p>
<br>

<span class=OwHelpTopic2>
	Changing personal settings
</span>
<p class=OwHelpText>
    <br>When you have changed all desired settings, please click "Save". Your modification will be set, changes will be available
    immediately.
</p>
<br>


<span class=OwHelpTopic2>
	Reset to default settings
</span>
<p class=OwHelpText>
    <br>You can reset all settings to default by clicking "Default Values". Then, all fields automatically are reset to default.
</p>






