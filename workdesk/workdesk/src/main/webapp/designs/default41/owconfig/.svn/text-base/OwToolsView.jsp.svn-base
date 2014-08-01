<%@ page
    import="java.util.*, com.wewebu.ow.server.ui.*, com.wewebu.ow.server.plug.owconfig.*, com.wewebu.ow.server.plug.owconfig.OwToolsView.OwAdminToolDescription, com.wewebu.ow.server.util.OwHTMLHelper"
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwToolsView m_View = (OwToolsView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
    // get collection of admin tools
    Collection m_AdminToolDescriptions = m_View.getAdminToolDescriptions();
%>
<div class="OwToolsView">
<%
    Iterator adminToolsIt = m_AdminToolDescriptions.iterator();
    while(adminToolsIt.hasNext())
    {
        OwAdminToolDescription atd = (OwAdminToolDescription)adminToolsIt.next();
        %>
    <div class="block">
        <div class="OwToolsView_title">
	       <a href="<%OwHTMLHelper.writeSecureHTML(out,atd.getUrl());%>" title="<%=atd.getDisplayName()%>"><h2><img src="<%=atd.getIcon()%>" alt=""> <%=atd.getDisplayName()%></h2></a>
        </div>
        <div class="OwToolsView_description"><%=atd.getDescription()%></div>
    </div>
        <%
    }
%>
</div>

