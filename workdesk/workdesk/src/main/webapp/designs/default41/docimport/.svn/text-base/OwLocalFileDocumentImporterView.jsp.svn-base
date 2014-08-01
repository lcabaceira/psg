<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
    import    = "com.wewebu.ow.server.ui.*,com.wewebu.ow.server.docimport.OwLocalFileDocumentImporterView" 
    autoFlush = "true"
%><%
    // get a reference to the calling view
    OwLocalFileDocumentImporterView m_View = (OwLocalFileDocumentImporterView)request.getAttribute(OwView.CURRENT_MODULE_KEY);

%><div class="contentDocumentImporter">
<label for="fileCtrlId">
<%=m_View.getContext().localize("jsp.OwLocalFileDocumentImporterView.message","Add document to batch:")%>
</label>
<input id="fileCtrlId" class="OwPropertyControl owButtonFile" type="file" size="50" name="fileName" />&nbsp;
<%-- <output id="list"></output>--%>
<%
    OwNavigationView parent=(OwNavigationView)m_View.getParent();
    int index=parent.getNavigationIndex();
    String sUrl=m_View.getEventURL("Upload", "tabID="+Integer.toString(index));
    String formTargetID=m_View.getFormTarget().getFormName();
%>
<script type="text/javascript">
  function handleFileSelect(evt) {
	  var form = document.getElementById('<%=formTargetID%>');
	  form.action = '<%=sUrl%>';
	  form.submit();
  }
  document.getElementById('fileCtrlId').addEventListener('change', handleFileSelect, false);
</script>
</div>