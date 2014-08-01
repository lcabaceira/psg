<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Help for Favorites
</span>

<p class="OwHelpText">
	<br>Here you see links to eFiles/folders and documents you added to your favorites in <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","eFile Management","OwHelpLink")%>.<br><br>
	<img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/bookmark1.png" alt="" title=""/> <br>
	Click on a document to open it in the viewer. If you click an eFile, it opens in eFile Management.  
</p>

<p class="OwHelpText">
	By using the icon <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/bookmark_delete.png" alt="<%=m_View.getContext().localize("help.images.bookmark_delete.gif","Delete bookmark image")%>" title="<%=m_View.getContext().localize("help.images.bookmark_delete.gif","Delete bookmark image")%>"/> you can delete bookmarks to the documents and eFiles/folders respectively.  
	
</p>

<p class="OwHelpText">
    <br>Furthermore you can find stored searches here, which you added to your favorites in <%=((OwInfoView)m_View).getLink("owsearch/default.jsp","Search","OwHelpLink")%>.<br><br>
    <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/bookmark2.png" alt="" title=""/> <br>
    Clicking on the stored search will execute the save once more.  
</p>

<p class="OwHelpText">
    By using the icon <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/storedsearch_delete.png" alt="Delete stored search" title="Delete stored search"/> you can delete stored searches.  
    
</p>