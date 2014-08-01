<%@page  pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.plug.owsearch.*"
%><%
    // get a reference to the calling view
    OwResultListView m_View = (OwResultListView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
	<div class="OwMainColumnHeader">
<%       if ( m_View.getCanGoBackToSearchTemplate() ) { 
		String back = m_View.getContext().localize("owsearch.OwResultListView.gobacktooltip","Back to search template");

%><%-- Render the back button --%>
		<div class="OwMainColumnHeader_back">
			<a title="<%=back%>" href="<%=m_View.getBackToSearchTemplateURL()%>"><img alt="<%=back%>" title="<%=back%>" src="<%=m_View.getContext().getDesignURL()%>/images/plug/owsearch/back.png"></a>
		</div>
<%        } 
	// Open container for controls above result list
	   if (m_View.isEnabledStoredSearch())  { 

%>         <div class="OwMainColumnHeader_search">
<%           if ( m_View.isRegion(OwResultListView.NEW_SAVED_SEARCH_REGION)) { 
			    m_View.renderRegion(out,OwResultListView.NEW_SAVED_SEARCH_REGION); 
			 } 
			 if ( m_View.isRegion(OwResultListView.NEW_SEARCH_BUTTON_REGION)) { 
			     m_View.renderRegion(out,OwResultListView.NEW_SEARCH_BUTTON_REGION); 
			 } 
%>		    </div>
<%     }%>
   	<div class="OwMainColumnHeader_control">
<%-- Render the view control buttons (standard, filter, thumbnails, ajax...) --%>
<%      m_View.renderRegion(out, OwResultListView.OBJECT_LIST_CONTROL_REGION); %>
<%-- Render the min max button 
<%      m_View.renderRegion(out, OwResultListView.MIN_MAX_CONTROL_VIEW); %>--%>
		</div>
<%-- Close container for components above result list --%>
    </div>
<%-- Container for result list title --%>
    <span class="OwResultListViewTitle" id="OwResultListViewTitle">
		<%=m_View.getContext().localize1("owsearch.OwResultListView.title","Results for Search: %1",m_View.getTemplateDisplayName())%>
	</span>

<%-- DUMP Query Statement for debugging
<% m_View.renderNamedRegion(out,com.wewebu.ow.server.ecm.OwObjectCollection.ATTRIBUTE_SQL); %>

--%><%--

<% if ( ! m_View.isShowMaximized() ) { %>

    <div id="ResultsHeadScrollPanel" class="OwAutoList">

<%}%>
--%>
<%   m_View.renderRegion(out,OwResultListView.OBJECT_LIST_REGION); 

     if ( ! m_View.getIsComplete() ) { %>
		<span class="OwWarningText" id="OwSearchNotCompleteWarningText">
		<%=m_View.getContext().localize1("owsearch.OwResultListView.notcomplete","The result list does not contain all results. Only the first %1 are displayed.",String.valueOf(m_View.getCount()))%>
		</span>
		
<%   } %>
<%--
<% if ( ! m_View.isShowMaximized() ) { %>

    </div>

<%}%>
--%>