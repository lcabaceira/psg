<%@page  pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true" 
    import="com.wewebu.ow.server.ui.*,
           com.wewebu.ow.server.plug.owrecord.*" 
%><%
    // get a reference to the calling view
    OwRecordSubLayout m_View = (OwRecordSubLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);
	com.wewebu.ow.server.app.OwMainAppContext m_context = (com.wewebu.ow.server.app.OwMainAppContext)com.wewebu.ow.server.ui.OwWebApplication.getContext(session);

%> <div class="accessibility">
<%       if ( m_View.isRegion(OwRecordSubLayout.MENU_REGION) ){%>
            <a href="#OwSubLayout_menu"><%=m_View.getContext().localize("jsp.OwRecordSubLayout.accessibility.MENU_REGION.skip", "Skip to record view menu region.")%></a>
            <br/>
<%       }
         if ( m_View.isRegion(OwRecordSubLayout.SEARCH_TEMPLATE_REGION) ){%>
            <a href="#OwRecordSubLayout_SEARCHTEMPLATE"><%=m_View.getContext().localize("jsp.OwRecordSubLayout.accessibility.SEARCH_TEMPLATE_REGION.skip", "Skip to search template region.")%></a>
            <br/>
<%       }%>
       <a href="#OwSubLayout_NAVIGATION"><%=m_View.getContext().localize("jsp.OwRecordSubLayout.accessibility.NAVIGATION.skip", "Skip to navigation.")%></a>
       <br/>
       <a href="#OwSubLayout_MAIN"><%=m_View.getContext().localize("jsp.OwRecordSubLayout.accessibility.MAIN.skip", "Skip to current selection content region.")%></a>
       <br/>
   </div>
   <jsp:include page="OwRecordMenu.jsp" flush="true"/>

   <div id="OwSubLayout_Div_Panel">
       <div class="OwRecordSubLayout_NAVIGATION" id="OwSubLayout_NAVIGATION" style="visibility:hidden;">
<%
                  if ( m_View.isRegion(OwRecordSubLayout.PREVIEW_PROPERTY_REGION) ) 
                  {
                      m_View.renderRegion(out,OwRecordSubLayout.PREVIEW_PROPERTY_REGION);
                  } 
					// we render the scrolling div inside the OwTreeView <div id="OwTreeView" class="OwAutoList"> --% >
					// < % - - render navigation as tree view --% >
%>                <h3 class="accessibility"><%=m_View.getContext().localize("jsp.OwRecordSubLayout.accessibility.NAVIGATION_REGION", "Navigation region to browse through the structure")%></h3>   
<%					m_View.renderRegion(out,OwRecordSubLayout.NAVIGATION_REGION); 
					// < % - - render navigation as register view --% >
					// < % - - m_View.renderRegion(out,OwRecordSubLayout.NAVIGATION_REGION_REGISTER_MODE); --% >
					// < % - - </div> --% >

%>     </div>
       <div id="OwSubLayout_MAIN" style="visibility:hidden;">
           <h3 class="accessibility"><%=m_View.getContext().localize("jsp.OwRecordSubLayout.accessibility.MAIN_REGION", "Content region of current selected container object")%></h3>
<%                    m_View.renderRegion(out,OwRecordSubLayout.MAIN_REGION); %>
       </div>
   </div>
        
<script type="text/javascript">
function configLayout() {
	OwObjectLayoutEXTJS.config={
		m_clientSideId: '<%=m_View.getTreeClientSideId()%>',
		m_navigationPanelInitialWidth : <%=m_View.getNavigationPanelInitialWidth()%>,
		m_previewPropPanelInitialHeight : <%=m_View.getPreviewPropPanelInitialHeight()%>,
		m_navigationPanelResizeURL: '<%=m_View.getAjaxEventURL("NavigationPanelResize",null)%>',
		m_PanelCollapsedStateChangedURL:'<%=m_View.getAjaxEventURL("PanelCollapsedStateChanged",null)%>',
		m_propPreviewPanelCollapsed: <%=m_View.isPropPreviewPanelCollapsed()%>,
		m_navPanelCollapsed: <%=m_View.isNavPanelCollapsed()%>
	};
}
</script>