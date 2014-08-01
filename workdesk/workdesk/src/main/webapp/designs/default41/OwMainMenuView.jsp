<%@ page 
    import="com.wewebu.ow.server.ui.*,
            com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
%><%
    // get a reference to the calling view
    OwMenuView m_View = (OwMenuView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<ul>
<%
    for (int i=0;i < m_View.getTabList().size();i++)
    {
        OwNavigationView.OwTabInfo tab = (OwNavigationView.OwTabInfo)m_View.getTabList().get(i);
        %><li><%
        // === write image
            if ( ((OwMenuView.OwMenuTabInfo)tab).getImage() != null )
            {
                if ( ((OwMenuView.OwMenuTabInfo)tab).getDisabled() )
                {
                %><img src="<%=((OwMenuView.OwMenuTabInfo)tab).getImage().toString()%>" alt=""><%
                }
                else
                {
                // === menu item is enabled
                    String toolTip = ((OwMenuView.OwMenuTabInfo)tab).getToolTip();
                    %><a <%
                    if ( ((OwMenuView.OwMenuTabInfo)tab).getToolTip() != null )
                    {
                    %> title="<%=toolTip%>" <%
                    }
                    %> href="<%=m_View.getNavigateEventURL(i)%>"><img src="<%=((OwMenuView.OwMenuTabInfo)tab).getImage().toString()%>"  alt="<%=toolTip%>" title="<%=toolTip%>"></a><%
                }
                /*%>&nbsp;<%*/
            }
        // === write label
            if ( ((OwMenuView.OwMenuTabInfo)tab).getDisabled() )
            {
            %><span><%=((OwMenuView.OwMenuTabInfo)tab).getName()%></span><%
            }
            else
            {
            // === menu item is enabled
                %><a <%

                if ( ((OwMenuView.OwMenuTabInfo)tab).getToolTip() != null )
                {
                %> title="<%=((OwMenuView.OwMenuTabInfo)tab).getToolTip()%>" <%
                }

                %> href="<%=m_View.getNavigateEventURL(i)%>"><%=((OwMenuView.OwMenuTabInfo)tab).getName()%></a><%
            }
            %></li><%
    }
%>
</ul>