<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*, com.wewebu.ow.server.util.*" 
    autoFlush   ="true"
    pageEncoding="utf-8"
    language="java"
    contentType="text/html; charset=utf-8"
%><%
    // get a reference to the calling view
    OwLayout m_View = (OwLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<br />
    <table border="0">
    <tr>
        <td class="OwStandardDialog_CLOSE_BTN"><% m_View.renderRegion(out,OwStandardDialog.CLOSE_BTN_REGION); %></td>

        <td>&nbsp;</td>
<%         if ( m_View.isRegion(OwStandardDialog.HELP_BTN_REGION) ) { %>

            <td class="OwStandardDialog_CLOSE_BTN"><% m_View.renderRegion(out,OwStandardDialog.HELP_BTN_REGION); %></td>
                
            <td>&nbsp;&nbsp;</td>
<%         } %>
        <td><% m_View.renderRegion(out,OwStandardDialog.MAIN_REGION); %></td>
    </tr>
    </table>
