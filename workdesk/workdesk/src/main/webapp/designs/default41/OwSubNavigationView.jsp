<%@ page import="com.wewebu.ow.server.ui.*,
            com.wewebu.ow.server.app.*" 
        autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%><%

    // get a reference to the calling view
    OwSubNavigationView m_View = (OwSubNavigationView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
//<!-- <div class="OwMainColumnHeader">&nbsp;</div> -->
%>
   <ul id="OwSubNavigationView">
<%
    for (int i=0;i < m_View.getTabList().size();i++)
    {
        OwNavigationView.OwTabInfo tab = (OwNavigationView.OwTabInfo)m_View.getTabList().get(i);
        if(!tab.isVisible()) {
            continue;
        }

        if ( tab.getDelimiter() )
        {
        // === draw delimiter
%>
         <li class="OwSubNavigationView_delimiter"><span class="accessibility"><%=m_View.getContext().localize("jsp.OwSubNavigationView.list_delimiter", "New list section")%></span></li>
<%
        }
        else
        {
        // === draw button
        /* % >
            <tr>
        < %*/
            if ( tab.getDisabled() || ( ! m_View.isPreviousPanelValid(i)) )
            {
%>
         <li class="OwSubNavigationView_not_selected">
            <span class="OwSubNavigationView_inactive">
<%
                    // optional Image
                    if ( ((OwSubNavigationView.OwSubTabInfo)tab).getImage() != null )
                    {
%>
                        <img alt="" src="<%=((OwSubNavigationView.OwSubTabInfo)tab).getImage()%>">
<%
                    }
                    out.write(((OwSubNavigationView.OwSubTabInfo)tab).getName());

%>            </span>
         </li>
<%
            }
            else
            {
%>
         <li class="<%=i == m_View.getNavigationIndex()? "OwSubNavigationView_selected" : "OwSubNavigationView_not_selected" %>">
            <a title="<%=((OwSubNavigationView.OwSubTabInfo)tab).getToolTip()%>" onkeydown="onKey(event,13,true,function(event,target){document.location=target.href;})" href="<%=m_View.getNavigateEventURL(i)%>"><%

                    // optional Image
                    if ( ((OwSubNavigationView.OwSubTabInfo)tab).getImage() != null )
                    {
%>
               <span class="OwSubNavigationView_icon" style="background-image: url(<%=((OwSubNavigationView.OwSubTabInfo)tab).getImage()%>)">
<%
                    }

                    out.write(((OwSubNavigationView.OwSubTabInfo)tab).getName());
                    
                    if ( ((OwSubNavigationView.OwSubTabInfo)tab).getImage() != null )
                    {
%>
               </span>
<%
                    }
                    
         %></a></li><%
            }

            /* % >
                </tr>
            < % */
        }
    }
%>
</ul>