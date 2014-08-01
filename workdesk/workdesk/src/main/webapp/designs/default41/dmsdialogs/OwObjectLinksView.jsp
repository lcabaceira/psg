<%@page import="com.wewebu.ow.server.dmsdialogs.views.OwTypedLinksView"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"	language="java" autoFlush="true"
	import="com.wewebu.ow.server.ui.*,
	com.wewebu.ow.server.app.*,
	com.wewebu.ow.server.util.*,
	com.wewebu.ow.server.dmsdialogs.views.OwObjectLinksView"

%><div class="OwObjectLinksView">
<%
			OwObjectLinksView m_View = (OwObjectLinksView) request.getAttribute(OwView.CURRENT_MODULE_KEY);

		    if (m_View.isRegion(OwObjectLinksView.LINKS_FILTER_REGION))
		    {
%>
		<div class="OwObjectLinksViewFilter">
			<div class="FilterName">
				<%OwInsertLabelHelper.insertLabel(out, m_View.getFilterDisplayName(), OwTypedLinksView.LINK_CLASS_ID, null);%>
			</div>
			<div class="FilterRegion">
<%
				    m_View.renderRegion(out, OwObjectLinksView.LINKS_FILTER_REGION);
%>
			</div>
		</div>
<%
		    }

		    if (m_View.isRegion(OwObjectLinksView.LINKS_REGION))
		    {
%>
		<div class="OwObjectLinksViewLinks">
<%
				    m_View.renderRegion(out, OwObjectLinksView.LINKS_REGION);
%>
		</div>
<%
		    }
%>
</div>