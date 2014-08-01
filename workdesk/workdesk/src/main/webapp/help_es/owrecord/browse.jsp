<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Help for Browse
</span>

<p class="OwHelpText">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/browse.png" alt="" title=""/><br><br>    
    In "Browse" you can manually navigate through the filing as familiar from Windows Explorer for example.
    The file structure with its folders and subfolders is displayed as tree. Click on the plus-minus symbol next to a folder to show or hide 
    its subfolders. In order to view a (sub-)folder's content click its name. The displayed folder is marked. 
    <br><br>
	The folder view shows the content of the selected folder. Per mouse-click, you can open documents in a viewer or if applicable in a program for 
	editing respectively. Like in eFile Management, several functions for editing eFiles/folders and documents as well as different list types are available.
	Please have a look at help page <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","eFile Management","OwHelpLink")%> to get a detailed description. 
	<br clear="all">
<br><br> 
</p>
