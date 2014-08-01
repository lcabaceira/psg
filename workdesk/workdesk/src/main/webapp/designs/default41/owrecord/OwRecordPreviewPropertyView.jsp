<%@ page
	import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.ecm.*,com.wewebu.ow.server.plug.owrecord.*"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>
<%
    // get a reference to the calling view
    OwRecordPreviewPropertyView m_View = (OwRecordPreviewPropertyView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<%-- Open container for controls above result list --%>
<div class="OwMainColumnHeader"	id="OwMainColumnHeader_PreviewPropertyView">
<%//Render the min max button
	    m_View.renderRegion(out, OwRecordPreviewPropertyView.MIN_MAX_REGION);
%>
</div>
<%
    if ((m_View.getPropertyNames() != null) && (!m_View.isShowMinimized()))
    {
        if (m_View.getPropertyNames().isEmpty() /*&& m_View.isDynamicSplitUsed()*/)
        {
            //don't display anything
        }
        else
        {
%>
<h3 class="accessibility"><%=m_View.getContext().localize("jsp.OwRecordPreviewPropertyView.accessibility.PREVIEW_PROPERTY_REGION", "Region for preview properties")%></h3>
<div id="<%=OwRecordPreviewPropertyView.PREVIEW_PROPERTIES_DIV_ID%>">
	<jsp:include page="OwRecordPreviewPropertiesRenderer.jsp" flush="true" />
</div>
<%
        }
    }
%>