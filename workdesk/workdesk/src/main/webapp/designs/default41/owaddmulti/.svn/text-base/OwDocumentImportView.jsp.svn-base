<%@ page 
    import    = "com.wewebu.ow.server.ui.*,
                 com.wewebu.ow.server.app.OwDocumentImportItem,
                 com.wewebu.ow.server.plug.owaddmultidocuments.OwDocumentImportView" 
    autoFlush = "true"
    pageEncoding="utf-8"
    contentType="text/html; charset=utf-8"
    language="java"
%><%

    // get a reference to the calling view
    OwDocumentImportView m_View = (OwDocumentImportView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
    // get the document input stack size
    int documentInputStackSize = m_View.getImportedDocumentsCount();
%>
<div class="OwDocumentImportView">
  
    <% if ( m_View.isRegion(OwDocumentImportView.NAVIGATION_REGION) ) { %>
        <div class="OwDocumentImportView_NAVIGATION">
            <% m_View.renderRegion(out,OwDocumentImportView.NAVIGATION_REGION); %>
        </div>
    <% } %>
    <div class="OwDocumentImportView_MAIN">
    
    <% if ( m_View.isRegion(OwDocumentImportView.MAIN_REGION) ) { %>
        <div class="block">
        <% m_View.renderRegion(out,OwDocumentImportView.MAIN_REGION); %>
        </div>
    <% } %>
	    <div class="block">
		    <div class="contentDocumentStack">
		    <h3><%= m_View.getContext().localize("jsp.OwDocumentImportView.InputStack","Document batch:") %></h3>
		    <% if( documentInputStackSize > 0 ) {  
		        for(int i = 0; i <  documentInputStackSize; i++)
		        {
		            OwDocumentImportItem importedDocument = m_View.getImportedDocument(i); %>
		        <%=(i+1)%>.&nbsp;<%= importedDocument.getDisplayName() %><br>
		    <%  }
		       } else { %>
		        <%= m_View.getContext().localize("jsp.OwDocumentImportView.Empty","- empty -") %><br>
		    <% } %>
            <br/>
		    <% if ( m_View.isRegion(OwDocumentImportView.MENU_REGION) ) { %>
		
		        <% m_View.renderRegion(out,OwDocumentImportView.MENU_REGION); %>
		
		    <% } %>
		    </div>
	    </div>
    </div>
</div>