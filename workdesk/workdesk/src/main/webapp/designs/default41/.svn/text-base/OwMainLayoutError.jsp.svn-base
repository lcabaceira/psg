<%@ page
	import="com.wewebu.ow.server.util.OwString,com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwLayout m_View = (OwLayout) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    OwAppContext context = m_View.getContext();
    OwMainLayout mainLayout = null;
    if (m_View instanceof OwMainLayout)
    {
        mainLayout = (OwMainLayout) m_View;
        if (mainLayout != null && mainLayout.hasError())
        {
%>
<div id="OwMainLayout_ERROR" class="OwInfoBar">
	<h1 class="accessibility"><%=context.localize("jsp.OwMainLayout.accessibility.OwMainLayout.ERROR_REGION", "Region with information about errors.")%></h1>
	<%
	    m_View.renderRegion(out, OwMainLayout.ERROR_REGION);
	%>
</div>
<%
    }
    }
    else
    {
%>
<div id="OwMainLayout_ERROR" class="OwInfoBar">
	<h1 class="accessibility"><%=context.localize("jsp.OwMainLayout.accessibility.OwMainLayout.ERROR_REGION", "Region with information about errors.")%></h1>
	<%
	    m_View.renderRegion(out, OwMainLayout.ERROR_REGION);
	%>
</div>
<%
    }
%>