<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true"
	import="com.wewebu.ow.server.ui.*,
	        com.wewebu.ow.server.plug.owrecord.*"
%><%-- ========================================================
    This JSP renders the menu bar that includes:
        - record functions region.
        - hotkey region
        - search template region
    =========================================================
--%><div id="<%=OwRecordSubLayout.OW_SUB_LAYOUT_MENU_DIV_ID%>">
<%
	    // get a reference to the calling view
	    OwRecordSubLayout m_View = (OwRecordSubLayout) request.getAttribute(OwView.CURRENT_MODULE_KEY);
	    com.wewebu.ow.server.app.OwMainAppContext m_context = (com.wewebu.ow.server.app.OwMainAppContext) com.wewebu.ow.server.ui.OwWebApplication.getContext(session);

%><%-- DND_APPLET_REGION --%>
	<div id="<%=OwRecordSubLayout.OW_REGION_DND_APPLET_DIV_ID%>" style="float: left;">
<%
		    if (m_View.isRegion(OwRecordSubLayout.DND_REGION))
		    {
%>
		<h2 class="accessibility"><%=m_View.getContext().localize("jsp.OwRecordSubLayout.accessibility.MENU_REGION", "Menu of function for the current selected folder")%></h2>
<%
		    m_View.renderRegion(out, OwRecordSubLayout.DND_REGION);
		    }
%>
	</div>
<%-- MENU_REGION --%>
	<div id="<%=OwRecordSubLayout.OW_REGION_MENU_DIV_ID%>">
		<jsp:include page="recordmenu/MenuRegion.jsp" />
	</div>
<%-- HOT_KEY_REGION --%>
	<div id="<%=OwRecordSubLayout.OW_REGION_HOTKEY_DIV_ID%>">
		<jsp:include page="recordmenu/HotKeyRegion.jsp" />
	</div>
<%-- SEARCH_TEMPLATE_REGION --%>
	<div id="<%=OwRecordSubLayout.OW_REGION_SEARCHTEMPLATE_DIV_ID%>">
		<jsp:include page="recordmenu/SearchTemplatesRegion.jsp" />
	</div>
</div>