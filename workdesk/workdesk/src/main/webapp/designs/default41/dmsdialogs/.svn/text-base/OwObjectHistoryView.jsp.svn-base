<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
    import="com.wewebu.ow.server.ui.*, 
    com.wewebu.ow.server.app.*, 
    com.wewebu.ow.server.util.*,
    com.wewebu.ow.server.dmsdialogs.views.OwObjectHistoryView" 
    autoFlush="true"
%><%
    // get a reference to the calling view
    OwLayout m_View = (OwLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);

    if ( m_View.isRegion(OwObjectHistoryView.SEARCH_CRITERIA_REGION) ) { 
%>
	<div id="OwMainContent">
<%      m_View.renderRegion(out,OwObjectHistoryView.SEARCH_CRITERIA_REGION); 
        m_View.renderRegion(out,OwObjectHistoryView.MENU_REGION); 
%>	</div>
<%  } 

    m_View.renderRegion(out,OwObjectHistoryView.OBJECT_LIST_REGION);%>