<%@ page 
    import="com.wewebu.ow.server.util.OwString,
            com.wewebu.ow.server.ui.*,com.wewebu.ow.server.plug.owbpm.*" 
    autoFlush   ="true"
    pageEncoding="utf-8"
    language="java"
    contentType="text/html; charset=utf-8"
%><%
    // get a reference to the calling view
    OwLayout m_View = (OwLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
	<!-- Open container for controls above result list -->
	<div class="OwMainColumnHeader">
		<!-- Render the min max button -->
<%       m_View.renderRegion(out, OwBPMWorkItemListView.MIN_MAX_CONTROL_VIEW);
%>		<!-- Render the view control buttons (standard, filter, thumbnails, ajax...) -->
<%       m_View.renderRegion(out, OwBPMWorkItemListView.OBJECT_LIST_CONTROL_REGION);   %>
	<!-- Close container for components above result list -->
	</div>

	<!-- Open container for controls above result list -->
	<div id="OwBPMWorkItemListView_SEARCH_CRITERIA_BOX">
	
	    <%
	    if ( m_View.isRegion(OwBPMWorkItemListView.SEARCH_CRITERIA_REGION) )
	    {
	    %>
	    <h2 class="accessibility"><%=m_View.getContext().localize("jsp.OwBPMWorkItemListView.accessibility.SEARCH_CRITERIA_REGION", "Region to search for work items")%></h2>
			<!-- Render the filter input field -->
	        <div class="OwBPMWorkItemListView_SEARCH_CRITERIA">

<%             m_View.renderRegion(out, OwBPMWorkItemListView.SEARCH_CRITERIA_REGION);   %>

	        </div>

			<!-- Render the search & reset buttons -->
	        <div class="OwBPMWorkItemListView_SEARCH_CRITERIA_BUTTON">

<%             m_View.renderRegion(out, OwBPMWorkItemListView.SEARCH_CRITERIA_BUTTON_REGION);   %>

	        </div>
<%
	    }
%>		<!-- Render the resubmission control -->
	    <div class="OwBPMWorkItemListView_OPTION_MENU">
	    <h3 class="accessibility"><%=m_View.getContext().localize("jsp.OwBPMWorkItemListView.accessibility.View_OPTION_MENU", "Options for result list")%></h3>
<%          m_View.renderRegion(out, OwBPMWorkItemListView.OPTION_MENU_REGION);   %>
	    </div>

	<!-- Close container for components above result list -->
	</div>
	<div id="OwBPMWorkItemListView">
	  <h3 class="accessibility"><%=m_View.getContext().localize("jsp.OwBPMWorkItemListView.accessibility.View_WORKITEM_LIST", "List of work items")%></h3>
      <div class="OwBPMWorkItemListView_WORKITEM_LIST">
<%       m_View.renderRegion(out, OwBPMWorkItemListView.WORKITEM_LIST_REGION);   %>
		</div>
	</div>