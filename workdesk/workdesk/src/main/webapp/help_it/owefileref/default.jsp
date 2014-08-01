<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
	Help for "Open associated eFile" Document Function
</span>

<p class="OwHelpIntro">
	<br/>Open associated eFile
	<br/>Opens an eFile, which is directly associated with the initial source object, such as a task object.   
</p>
<br><br>
