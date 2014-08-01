<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%><%
    // get a reference to the calling view
    OwSubLayout m_View = (OwSubLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
    <div class="accessibility">
       <a href="#OwSubLayout_menu"><%=m_View.getContext().localize("jsp.OwSubLayout.accessibility.MENU_REGION.skip", "Skip to view menu region.")%></a>
       <br/>
       <a href="#OwSubLayout_NAVIGATION"><%=m_View.getContext().localize("jsp.OwSubLayout.accessibility.NAVIGATION.skip", "Skip to navigation.")%></a>
       <br/>
       <a href="#OwSubLayout_MAIN"><%=m_View.getContext().localize("jsp.OwSubLayout.accessibility.MAIN.skip", "Skip to current selection content region.")%></a>
       <br/>
    </div>
    <div id="OwSubLayout_menu">
    <h2 class="accessibility"><%=m_View.getContext().localize("jsp.OwSubLayout.accessibility.MENU_REGION", "Menu region")%></h2>
<%  if ( m_View.isRegion(OwSubLayout.MENU_REGION) ) 
    {  
        m_View.renderRegion(out,OwSubLayout.MENU_REGION);

    }
%>
    </div>
<%  if ( m_View.isRegion(OwSubLayout.HOT_KEY_REGION) ) 
    {  
%>
    <div class="OwSubLayout_hotkey">
      <h3 class="accessibility"><%=m_View.getContext().localize("jsp.OwSubLayout.accessibility.HOT_KEY_REGION", "Region for Hot key navigation")%></h3>
      <% m_View.renderRegion(out,OwSubLayout.HOT_KEY_REGION); %>
    </div>
<%  } 

%>
     <div id="OwSubLayout_Div_Panel">
<%       if ( m_View.isRegion(OwSubLayout.NAVIGATION_REGION) ) 
         { 
%>
             <div class="OwSubLayoutDynamic_NAVIGATION" id="OwSubLayout_NAVIGATION" style="visibility:hidden;" <%=m_View.createRegionCustomAttributes(OwSubLayout.NAVIGATION_REGION)%>>
               <h3 class="accessibility"><%=m_View.getContext().localize("jsp.OwSubLayout.accessibility.NAVIGATION_REGION", "sub navigation")%></h3>
<%-- Dirty Table Hack for flexible Column Width in IE; adjust Version Number of Conditional Comment if future Versions still show this Bug! 
					<!--[if lte IE 7]><table id="OwSubLayout_NAVIGATION_IEColumnFix"><tr><td><![endif]-->
                    	<div class="OwMainColumnHeader">&nbsp;</div> --%>
<%                     m_View.renderRegion(out, OwSubLayout.NAVIGATION_REGION); %>

<%--					<!--[if lte IE 7]></td></tr></table><![endif]--> --%>
              </div>
<%       }
%>
                <div id="OwSubLayout_MAIN" style="visibility:hidden;">
<%--                    	<!-- <div class="OwMainColumnHeader">&nbsp;</div> --> --%>
<%                     m_View.renderRegion(out, OwSubLayout.MAIN_REGION); %>
                </div>
<%		if ( m_View.isRegion(OwSubLayout.SECONDARY_REGION) ) 
  		{ 
%>
				<div id="OwSubLayout_SEC" style="visibility:hidden;" <%=m_View.createRegionCustomAttributes(OwSubLayout.SECONDARY_REGION)%>>
				<%m_View.renderRegion(out, OwSubLayout.SECONDARY_REGION);%>
                </div>
<%		}%>
         </div>
<script type="text/javascript">
function configLayout() {
	OwSubLayoutEXTJS.config={
		m_navigationPanelWidth:<%=m_View.getNavigationPanelWidth()%>,
		m_resizeURL:'<%=m_View.getAjaxEventURL("NavigationPanelResize",null)%>',
		m_PanelCollapsedStateChangedURL:'<%=m_View.getAjaxEventURL("PanelCollapsedStateChanged",null)%>'
	};
}
</script>