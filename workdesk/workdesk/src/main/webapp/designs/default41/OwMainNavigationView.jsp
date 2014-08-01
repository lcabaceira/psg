<%@ page language="java"  pageEncoding="UTF-8" 
import="java.util.*,com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*" 
 contentType="text/html; charset=utf-8"%><%
    // get a reference to the calling view
    OwMainNavigationView m_View = (OwMainNavigationView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<% 	
	int navSize=m_View.getTabList().size();
	if (navSize<1) {
%>
		<div class="OwMainNavigationView_notavailable"><img src="<%=m_View.getContext().getDesignURL()%>/images/error.png" alt=""><a href="#"><%=m_View.getContext().localize("app.OwConfiguration.rolemanager.noPluginAssigned", "No Master Plugin is assigned to your account or your user role. Please contact your administrator.")%></a><br clear="all" /></div>
<%	}
	else
	{
%>
<ul>
<%	
	    for (int i=0;i < navSize;i++)
	    {
	        OwMainNavigationView.OwSubTabInfo tab = (OwMainNavigationView.OwSubTabInfo)m_View.getTabList().get(i);
	       
	        %><li class="<%=i == m_View.getNavigationIndex() ? "OwMainNavigationView_selected" : "OwMainNavigationView_not_selected"%>" style="background-image: url(<%=tab.getIcon()%>)"><%
	        %><a href="<%=m_View.getNavigateEventURL(i)%>" style=""><%=((OwMainNavigationView.OwSubTabInfo)tab).getName()%></a></li>
<%
	    }
%>
</ul>
<%	    
	}
%>