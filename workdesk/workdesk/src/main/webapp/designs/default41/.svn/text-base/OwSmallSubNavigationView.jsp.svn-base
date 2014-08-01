<%@ page import="com.wewebu.ow.server.ui.OwView,
            com.wewebu.ow.server.ui.OwNavigationView,
            com.wewebu.ow.server.app.OwSubNavigationView"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%><%
    // get a reference to the calling view
    OwSubNavigationView m_View = (OwSubNavigationView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div class="OwSmallSubNavigation">
	<div id="tabspace">
		<div id="fill"></div>
<%
		    for (int i = 0; i < m_View.getTabList().size(); i++)
		    {
		        OwNavigationView.OwTabInfo tab = (OwNavigationView.OwTabInfo) m_View.getTabList().get(i);

		        if (! tab.getDelimiter())
		        {
		            if (tab.getDisabled() || (!m_View.isPreviousPanelValid(i)))
		            {
%>
		                <div class="OwSmallSubNavigation_disabled">
<% 
		            }
		            else
		            {
%>
      <div class="<%=  i == m_View.getNavigationIndex() ? "OwSmallSubNavigation_selection" :   "OwSmallSubNavigation_tab" %>" onclick="window.location.href='<%=m_View.getNavigateEventURL(i)%>'"  title="<%=((OwSubNavigationView.OwSubTabInfo) tab).getName()%>" >
<%
                    }
%>
			<div id="space_left"></div>
			<div id="text">
<%
            String toolTip = ((OwSubNavigationView.OwSubTabInfo) tab).getName();
            // optional Image
					if (((OwSubNavigationView.OwSubTabInfo) tab).getImage() != null)
					{

%>
					<img src="<%=((OwSubNavigationView.OwSubTabInfo) tab).getImage()%>" alt="<%=toolTip%>" title="<%=toolTip%>" style="vertical-align:top;">
<%
                    }
%>
         <a href='<%=m_View.getNavigateEventURL(i)%>'>
<% 
                    out.write(toolTip);
%>
          </a>
         </div><!-- end of #text -->

		</div><!--  end of .OwSmallSubNavigation_disabled or .OwSmallSubNavigation_selection .OwSmallSubNavigation_tab -->
		<div id="fill"></div>
<%
			     } // end of if no delimiter
			 } // end of for 
%>
   </div> <!--  end of #tabspace -->
</div> <!--  end of .OwSmallSubNavigation -->