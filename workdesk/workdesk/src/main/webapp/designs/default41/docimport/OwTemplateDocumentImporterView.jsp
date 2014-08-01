<%@ page 
    import="com.wewebu.ow.server.ui.*, 
    com.wewebu.ow.server.app.*, 
    com.wewebu.ow.server.util.*,
    com.wewebu.ow.server.dmsdialogs.*,com.alfresco.ow.server.docimport.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
%><%
    // get a reference to the calling view
    OwTemplateDocumentImporterView m_View = (OwTemplateDocumentImporterView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div class="OwTemplateDocumentImporterView">
    <div class="OwTemplateDocumentImporterView_INFO">
        <%=m_View.getContext().localize("AwdTemplateDocumentImporterView.jsp.TemplateSelect", "Please select template to add to upload stack:<br />")%>
    </div>
    <br>
    <div class="OwTemplateDocumentImporterView_LST">
<%      m_View.showTemplateFolder();
        m_View.renderRegion(out,OwStandardDialog.MAIN_REGION);
        m_View.renderRegion(out,OwTemplateDocumentImporterView.LISTVIEW_REGION);%>
    </div>

<%    m_View.renderRegion(out,OwTemplateDocumentImporterView.MENU_REGION);%>
</div>