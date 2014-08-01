<%@ page
	import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.plug.owdms.OwSaveDlgDocumentImportView"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>
<%
    // get a reference to the calling view
    OwSaveDlgDocumentImportView m_View = (OwSaveDlgDocumentImportView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div class="OwSaveDlgDocument">
	<%
	    if (m_View.isRegion(OwSaveDlgDocumentImportView.NAVIGATION_REGION))
	    {
	%>
	<div class="OwSaveDlgDocument_NAVIGATION">
			<%
			    m_View.renderRegion(out, OwSaveDlgDocumentImportView.NAVIGATION_REGION);
			%>
	</div>
	<%
	    }
	%>
	<div class="OwSaveDlgDocument_MAIN">
			<div class="block">
			<%
			    if (m_View.isRegion(OwSaveDlgDocumentImportView.MAIN_REGION))
			    {
			%>

			<%
			    m_View.renderRegion(out, OwSaveDlgDocumentImportView.MAIN_REGION);
			%>

			<%
			    }
			%>
	       </div>
	       <div class="block">
			<h3><%=m_View.getContext().localize("jsp.OwSaveDlgDocumentImportView.ImportedDocument", "Imported Document")%></h3>
			<%
			    if (m_View.getImportedDocument() != null)
			    {
			%><%=m_View.getImportedDocument().getDisplayName()%><br>
			<%
			    }
			    else
			    {
			%>
			<%=m_View.getContext().localize("jsp.OwSaveDlgDocumentImportView.None", "-None-")%><br>
			<%
			    }
			%>
			<%
			    if (m_View.isRegion(OwSaveDlgDocumentImportView.MENU_REGION))
			    {
			%>

			<%
			    m_View.renderRegion(out, OwSaveDlgDocumentImportView.MENU_REGION);
			%>

			<%
			    }
			%>
		</div>
	</div>
</div>