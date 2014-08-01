<%@ page
	import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*,com.wewebu.ow.server.util.*"
	autoFlush="true"
    pageEncoding="utf-8"
    language="java"
    contentType="text/html; charset=utf-8"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<div id="OwDemoSearchForm">
	<div id="OwDemoSearchForm_Header">
		<h1><%=m_View.getContext().localize("default41.demo.JspFormularTest.jsp.title", "JSP Page: Form test")%>
			(.../demo/JspFormularTest.jsp)
		</h1>
		<img
			alt="<%=m_View.getContext().localize("image.alfresco.png", "Alfresco Logo")%>"
			title="<%=m_View.getContext().localize("image.alfresco.png", "Alfresco Logo")%>"
			src="<%=m_View.getContext().getDesignURL()%>/images/plug/owdemo/alfresco.png" />
	</div>

	<!-- Main area -->
	<div id="OwDemoSearchForm_MAIN">
		<table class="OwFormTable OwNowrapLabel">
			<tr>
				<td class="OwPropertyName">
					Name:
				</td>
				<td class="DefaultInput">
					<%
					    m_View.renderNamedRegion(out, "ow_Filename");
					%>
				</td>
			</tr>
			<tr>
				<td class="OwPropertyName"><%=m_View.getContext().localize("default41.demo.JspFormularTest.jsp.lastChange", "Last modification")%>:
				</td>
				<td class="DefaultInput OwPropertyControl">
				    <div class="owInlineControls">
					<%
					    m_View.renderNamedRegion(out, "ow_Last-Modified");
					%>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>