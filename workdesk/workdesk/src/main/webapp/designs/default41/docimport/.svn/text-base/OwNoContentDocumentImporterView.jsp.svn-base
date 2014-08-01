<%@ page 
    import    = "com.wewebu.ow.server.ui.*,com.wewebu.ow.server.docimport.OwNoContentDocumentImporterView" 
    autoFlush = "true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%><%

    // get a reference to the calling view
    OwNoContentDocumentImporterView m_View = (OwNoContentDocumentImporterView)request.getAttribute(OwView.CURRENT_MODULE_KEY);

%>
<div class="contentDocumentImporter">
<%=m_View.getContext().localize("jsp.OwNoContentDocumentImporterView.message","Create or check in the document without content") %><br>
<% if ( m_View.isRegion(OwNoContentDocumentImporterView.MENU_REGION) ) { %>

<% m_View.renderRegion(out,OwNoContentDocumentImporterView.MENU_REGION); %>

<% } %>
</div>