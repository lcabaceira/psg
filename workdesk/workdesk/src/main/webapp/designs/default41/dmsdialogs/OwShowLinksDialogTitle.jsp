<%@page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8"
	import="com.wewebu.ow.server.ecm.*,
	com.wewebu.ow.server.ui.*,
	com.wewebu.ow.server.plug.owlink.OwShowLinksDialog"
	autoFlush="true" 
%><%
    // get a reference to the calling view
    OwShowLinksDialog view = (OwShowLinksDialog) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<span class="OwShowLinksDialog_Header"><span class="PluginTitle"><%=view.getTitle()%></span>&nbsp;:&nbsp;
<span class="PluginObject">
<%
	    view.getMimeManager().insertIconLink(out, view.getCurrentItem());
	    view.getMimeManager().insertTextLink(out, view.getCurrentItem().getName(), view.getCurrentItem());
%>
</span>
</span>
