<%@page  pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true"
    import="com.wewebu.ow.server.ui.*,
            com.wewebu.ow.server.app.*" 

%><%
    // get a reference to the calling view
    OwMenuView m_View = (OwMenuView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div class="owButtons">
<%
    for (int i=0;i < m_View.getTabList().size();i++)
    {
        OwNavigationView.OwTabInfo tab = (OwNavigationView.OwTabInfo)m_View.getTabList().get(i);

        // === write image
            if ( ((OwMenuView.OwMenuTabInfo)tab).getImage() != null )
            {
                if ( ((OwMenuView.OwMenuTabInfo)tab).getDisabled() )
                {
                %><img alt="" src="<%=((OwMenuView.OwMenuTabInfo)tab).getImage().toString()%>"><%
                }
                else
                {
                // === menu item is enabled
                    %><a <%
                    String tooltip = ((OwMenuView.OwMenuTabInfo)tab).getToolTip(); 
                    if ( tooltip  != null )
                    {
                    %>title="<%=((OwMenuView.OwMenuTabInfo)tab).getToolTip()%>" <%
                    }
                    %> href="<%=m_View.getNavigateEventURL(i)%>"><img <%
                    if (tooltip != null)
                    {
                        %>alt="<%=tooltip%>" title="<%=tooltip%>"<%
                    }
                    %> src="<%=((OwMenuView.OwMenuTabInfo)tab).getImage().toString()%>"></a><%
                }
                %>&nbsp;<%
            }
            // === write label
            if ( ((OwMenuView.OwMenuTabInfo)tab).getDisabled() )
            {
            	// don't render button
            	continue;
            }
            else
            {
            // === menu item is enabled
                %><input type="button" <%

                if ( ((OwMenuView.OwMenuTabInfo)tab).getToolTip() != null )
                {
                %> title="<%=((OwMenuView.OwMenuTabInfo)tab).getToolTip()%>" <%
                }

                String sUrl = m_View.getNavigateEventURL(i);
                String name = ((OwMenuView.OwMenuTabInfo)tab).getName();
                if((sUrl.length() >= 11) && (sUrl.substring(0,11).equalsIgnoreCase("javascript:")))
                {
                // is javascript. strip "javascript:" off the front
                    sUrl = sUrl.substring(11);
                // button with JS code in the onclick 
                    %> onclick="<%=sUrl%>" onkeydown="onKey(event,13,true,function(event){<%=sUrl%>})" name="<%=name%>" value="<%=name%>"><%
                }
                else
                {
                    // is a real url. button with onclick="window.location.href=''"
                    %> 
                    onclick="window.location.href='<%=sUrl%>'" onkeydown="onKey(event,13,true,function(event){window.location.href='<%=sUrl%>'})" name="<%=name%>" value="<%=name%>"><%
                }
            }
    }
%></div>