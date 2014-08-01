<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8" language="java" autoFlush="true"
	import="com.wewebu.ow.server.ui.*,
	       com.wewebu.ow.server.plug.owrecord.*"
%><%-- ========================================================
    This JSP renders the HotKey region.
    This was introduced just to be able to refresh this region through an Ajax Call.
    =========================================================
--%><%
    // get a reference to the calling view
    OwRecordSubLayout m_View = (OwRecordSubLayout) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    com.wewebu.ow.server.app.OwMainAppContext m_context = (com.wewebu.ow.server.app.OwMainAppContext) com.wewebu.ow.server.ui.OwWebApplication.getContext(session);

    if (m_View.isRegion(OwRecordSubLayout.HOT_KEY_REGION))
    {
%>
<div class="OwSubLayout_hotkey" style="clear:left">
	<h2 class="accessibility"><%=m_View.getContext().localize("jsp.OwRecordSubLayout.accessibility.HOT_KEY_REGION", "Menu for hot key navigation, for recent list of plugin")%></h2>
<%
	    m_View.renderRegion(out, OwRecordSubLayout.HOT_KEY_REGION);
%>
</div>
<%
    }
%>