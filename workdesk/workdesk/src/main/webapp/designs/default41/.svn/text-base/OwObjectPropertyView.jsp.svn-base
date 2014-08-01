<%@ page 
    import="com.wewebu.ow.server.util.OwString,
    		com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyView,
            com.wewebu.ow.server.ui.*,
            com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
%><%
    // get a reference to the calling view
    OwLayout m_view = (OwLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);
    m_view.renderRegion(out,OwObjectPropertyView.ERRORS_REGION); %>
<div class="OwObjectPropertyView">
<%   m_view.renderRegion(out,OwObjectPropertyView.MAIN_REGION); %>
    <div class="OwObjectPropertyView_MENU OwInlineMenu">
<%   if ( m_view.isRegion(OwObjectPropertyView.MODES_REGION) ) { 
	   m_view.renderRegion(out,OwObjectPropertyView.MODES_REGION);
	 } 
     m_view.renderRegion(out,OwObjectPropertyView.MENU_REGION); %>
    </div>
</div>