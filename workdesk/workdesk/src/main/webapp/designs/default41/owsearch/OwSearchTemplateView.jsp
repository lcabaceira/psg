<%@ page
	import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.plug.owsearch.*"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"

%><%
    // get a reference to the calling view
    OwLayout m_View = (OwLayout) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    m_View.renderRegion(out, OwSearchTemplateView.ERRORS_REGION);
%><!-- Open container for controls above result list -->
<div class="OwMainColumnHeader">
			<!-- Render the min max button -->
<%          m_View.renderRegion(out, OwSearchTemplateView.MIN_MAX_CONTROL_VIEW);%>
		<!-- Close container for components above result list -->
</div>
<div id="OwMainContent" class="OwPropertyBlockLayout">
<%    m_View.renderRegion(out, OwSearchTemplateView.TEMPLATE_REGION);
      m_View.renderRegion(out, OwSearchTemplateView.MAX_SIZE_REGION);%>

    <div class="OwSearchTemplateView_MENU OwInlineMenu">
<%  if (m_View.isRegion(OwSearchTemplateView.SAVED_SEARCHES_SELECT_REGION))
    {
        m_View.renderRegion(out, OwSearchTemplateView.SAVED_SEARCHES_SELECT_REGION);
    }
    out.write("\n");
    m_View.renderRegion(out, OwSearchTemplateView.BUTTON_REGION);
    out.write("\n");
    if (m_View.isRegion(OwSearchTemplateView.SAVED_SEARCH_DELETE_BUTTON_REGION))
    {
        m_View.renderRegion(out, OwSearchTemplateView.SAVED_SEARCH_DELETE_BUTTON_REGION);
    }
%>	</div>

<%-- searchtree dump
<div style='background: #dddddd;border: solid 1pt black;font-family: Arial, Helvetica, sans-serif;font-size: 10 pt;'>
<b>SEARCHTREE-DUMP:</b>
<br>
<span style='font-size: 8pt'>
<% m_View.renderRegion(out,OwSearchTemplateView.DEBUG_SEARCH_DUMP_REGION); %>
</span>
</div>
--%>
</div>