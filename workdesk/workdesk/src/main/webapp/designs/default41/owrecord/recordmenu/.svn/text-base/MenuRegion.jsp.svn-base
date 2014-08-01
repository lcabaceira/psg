<%@page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8" autoFlush="true"
	import="com.wewebu.ow.server.ui.*,
	        com.wewebu.ow.server.plug.owrecord.*"
%><%-- ========================================================
    This JSP renders the menu region, excluding the DnD applet.
    This was introduced just to be able to refresh this region through an Ajax Call.
    =========================================================
--%><%
    // get a reference to the calling view
    OwRecordSubLayout m_View = (OwRecordSubLayout) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    com.wewebu.ow.server.app.OwMainAppContext m_context = (com.wewebu.ow.server.app.OwMainAppContext) com.wewebu.ow.server.ui.OwWebApplication.getContext(session);

    if (m_View.isRegion(OwRecordSubLayout.MENU_REGION))
    {
%>
<h2 class="accessibility"><%=m_View.getContext().localize("jsp.OwRecordSubLayout.accessibility.MENU_REGION", "Menu of function for the current selected folder")%></h2>
<%
       m_View.renderRegion(out, OwRecordSubLayout.MENU_REGION);
    }
%>