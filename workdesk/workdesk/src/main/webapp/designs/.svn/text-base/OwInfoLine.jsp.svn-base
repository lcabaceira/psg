<%@page	import="com.wewebu.ow.server.ui.OwLayout,com.wewebu.ow.server.ui.OwView,
                com.wewebu.ow.server.ui.OwAppContext,
                com.wewebu.ow.server.app.OwConfiguration,
                com.wewebu.ow.server.util.OwHTMLHelper" autoFlush="true"
%><%
    // get a reference to the calling view
    OwLayout m_ViewInfo = (OwLayout) request.getAttribute(OwView.CURRENT_MODULE_KEY);

%><a href="version.html" class="version">&nbsp;</a><%
	 OwAppContext ctx = m_ViewInfo.getContext(); 
     out.write(ctx.localize(OwConfiguration.getEditionString(), "Development Version - Only for development and testing. Not for production environments"));
	 out.write("<br />");
	 out.write(ctx.localize("default.OwMainLayout.jsp.version", "Version"));
	 out.write(": ");
	 out.write(ctx.localize(OwConfiguration.getVersionString(), OwConfiguration.getVersionString()));
	 out.write(" - ");
	 out.write(OwConfiguration.getBuildNumber());
	 out.write(", ");
	 out.write(ctx.localize("default.OwMainLayout.jsp.locale", "Language"));
	 out.write(": ");
	 out.write(OwHTMLHelper.encodeToSecureHTML(ctx.getLocale().toString()));
	 out.write(", ");
	 out.write(ctx.localize("default.OwMainLayout.jsp.copyright", "Copyright &copy; Alfresco Software, Inc."));
%>