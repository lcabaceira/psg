<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true"
    import="com.wewebu.ow.server.ui.*,
            com.wewebu.ow.server.plug.owrecord.*" 
%><%
    // get a reference to the calling view
    OwRecordContentView m_View = (OwRecordContentView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%><%-- Open container for controls above result list --%>
	<div class="OwMainColumnHeader" id="OwMainColumnHeader_CONTENTVIEW">
<%-- Render the min max button --%>
<%       m_View.renderRegion(out,OwRecordContentView.MIN_MAX_CONTROL_VIEW); %>
<%-- Render the view control buttons (standard, filter, thumbnails, ajax...) --%>
<%       m_View.renderRegion(out,OwRecordContentView.OBJECT_LIST_CONTROL_REGION); %>
<%-- Close container for components above result list --%>
	</div>
    <div id="OwSublayout_ContentContainer" class="OwSublayout_ContentContainer">
<%         m_View.renderRegion(out,OwRecordContentView.OBJECT_LIST_REGION); %>
    </div>
<%   if ( ! m_View.getIsComplete() ) { %>
    <div class="OwWarningText" id="OwResultListWarningText">
		<%=m_View.getContext().localize1("owsearch.OwResultListView.notcomplete","The result list does not contain all results. Only the first %1 are displayed.",String.valueOf(m_View.getCount()))%>
    </div>
<%   } %>