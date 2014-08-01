<%@ page 
    import="com.wewebu.ow.server.servlets.OwInitialisingContextListener,
            com.wewebu.ow.server.util.OwString,
            com.wewebu.ow.server.ui.*,
            com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%><%
    // get a reference to the calling view
    OwLayout m_View = (OwLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);
        
    Throwable startupErrors = (Throwable)((OwMainAppContext)m_View.getContext()).getApplicationAttribute(OwInitialisingContextListener.STARTUP_ERROR_KEY);
    if (startupErrors != null)
    {
    	((OwMainAppContext)m_View.getContext()).setError(startupErrors);
    }
%>
<div id="OwMainLayout">
		<div id="OwMainLayout_HEADER">

			<div id="OwHeaderView_Logo">
			<a href="#" onclick="showAbout(<%=OwAboutMessageHelper.getAboutInfoAsJs(m_View.getContext())%>,'<%=OwAboutMessageHelper.getCopyright(m_View.getContext())%>'); return false;">
			<img src="<%=m_View.getContext().getDesignURL()%>/images/OwGuiLayout/<%=m_View.getContext().localize("image.workdesklogo","OW_Logo1.png")%>" id="OwHeaderView_Logo1" alt="<%=m_View.getContext().localize("image.ow_logo1.gif","Workdesk Logo")%>" title="<%=m_View.getContext().localize("image.ow_logo1.gif","Workdesk Logo")%>">
            </a>
            <%--
            <img src="<%=m_View.getContext().getDesignURL()%>/images/OwGuiLayout/OW_Logo2.gif" id="OwHeaderView_Logo2" alt="<%=m_View.getContext().localize("image.ow_logo2.gif","Alfresco Software, Inc.")%>" title="<%=m_View.getContext().localize("image.ow_logo2.gif","Alfresco Software, Inc.")%>">
			<img src="<%=m_View.getContext().getDesignURL()%>/images/OwGuiLayout/brand_logo.png" id="OwHeaderView_Logo3" alt="<%=m_View.getContext().localize("image.brand_logo.gif","Alfresco Software, Inc. Logo")%>" title="<%=m_View.getContext().localize("image.brand_logo.gif","Alfresco Software, Inc. Logo")%>">
			--%>
			</div>

			<div id="OwLoginView_LOCALS_REGION"><% m_View.renderRegion(out,OwLoginView.LOCALS_REGION);   %></div>
		</div>

		<div id="OwMainLayout_NAVIGATION"><%-- 
				<div class="OwMainLayout_edge OwMainLayout_topLeft"></div>
				<div class="OwMainLayout_edge OwMainLayout_topRight"></div>
				--%>&nbsp;</div>

<div id="OwLoginView">
    <h1 id="OwLoginView_title"><%=m_View.getContext().localize("default.OwLoginView.jsp.welcome","Welcome to Alfresco Workdesk") %></h1>

    <div id="OwLoginView_loginpart">
<% m_View.renderRegion(out,OwLoginView.LOGINPART_REGION);   %>
    </div>    
</div>

<div id="OwMainLayout_MAIN_endstrip"><%--
	<span class="OwMainLayout_edge OwMainLayout_bottomLeft"></span>
	<span class="OwMainLayout_edge OwMainLayout_bottomRight"></span>--%>
</div>
</div>
<div id="OwMainFooter">
    <p class="OwCopyRightInfo">
    <%@ include file="../OwInfoLine.jsp" %>
    </p>
</div>