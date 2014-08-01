<%@page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8" autoFlush="true"
	import="com.wewebu.ow.server.ui.*,
	        com.wewebu.ow.server.plug.owrecord.*"
%><%-- ========================================================
    This JSP renders the search templates region.
    This was introduced just to be able to refresh this region through an Ajax Call.
    =========================================================
--%><%
    // get a reference to the calling view
    OwRecordSubLayout m_View = (OwRecordSubLayout) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    com.wewebu.ow.server.app.OwMainAppContext m_context = (com.wewebu.ow.server.app.OwMainAppContext) com.wewebu.ow.server.ui.OwWebApplication.getContext(session);

    if (m_View.isRegion(OwRecordSubLayout.SEARCH_TEMPLATE_REGION))
    {
%>
<div id="OwRecordSubLayout_SEARCHTEMPLATE">
	<h2 class="accessibility"><%=m_View.getContext().localize("jsp.OwRecordSubLayout.accessibility.SEARCH_TEMPLATE_REGION", "Filter for current selected content")%></h2>
<%
	    m_View.renderRegion(out, OwRecordSubLayout.SEARCH_TEMPLATE_REGION);
%>
</div>
<%
    }
%>