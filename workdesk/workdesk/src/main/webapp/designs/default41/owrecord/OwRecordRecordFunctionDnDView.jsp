<%@page contentType="text/html; charset=utf-8" language="java" pageEncoding="utf-8" autoFlush="true"
    import="com.wewebu.ow.server.plug.owrecord.*,
    com.wewebu.ow.server.app.OwMainAppContext,
    com.wewebu.ow.server.app.OwRecordFunction,
    com.wewebu.ow.server.ecm.OwObject,
    com.wewebu.ow.server.ui.OwView,
    com.wewebu.ow.server.util.OwHTMLHelper,
    com.wewebu.ow.server.app.OwDocumentFunction, 
    java.util.Map.Entry"

%><%
    // get a reference to the calling view
    OwRecordRecordFunctionDnDView m_View = (OwRecordRecordFunctionDnDView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    com.wewebu.ow.server.app.OwMainAppContext m_context = (com.wewebu.ow.server.app.OwMainAppContext) com.wewebu.ow.server.ui.OwWebApplication.getContext(session);
    boolean useExtJsForDragAndDrop = m_context.getConfiguration().isUseExtJsForDragAndDrop();
    boolean useHtml5DragAndDrop = m_context.isUseHtml5DragAndDrop();
    if(useHtml5DragAndDrop) {
        useExtJsForDragAndDrop = true;
    }

    OwObject folderObject = m_View.getCurrentSubFolderObject();
    OwObject rootObject = m_View.getCurrentRootFolder();

    OwRecordFunction recordFunctionPlugin = (OwRecordFunction) m_View.getDnD();
    if (null != recordFunctionPlugin)
    {
        String escapedToolTip = OwHTMLHelper.encodeToSecureHTML(recordFunctionPlugin.getTooltip());
        if (useExtJsForDragAndDrop)
        {
%>
<script src="<%=m_context.getBaseURL()%>/js/dndupload/upload.js" type="text/javascript"></script>
<%
        }

        if(useHtml5DragAndDrop) {
%>
            <script src="<%=m_context.getBaseURL()%>/js/dndupload/ProgressDialog_Simple.js" type="text/javascript"></script>
            <script src="<%=m_context.getBaseURL()%>/js/dndupload/upload_html5.js" type="text/javascript"></script>
<%                      
        } else {
%>
            <script src="<%=m_context.getBaseURL()%>/js/applet/embedDragDropApplet.js"  type="text/javascript"></script>
<%                              
        }

        int i = m_View.getDnDIndex();
        boolean fEndabled = m_View.getIsPluginEnabled(recordFunctionPlugin);
        String strEventURL = m_View.getRecordFunctionEventURL(i);
%>

<script type="text/javascript">
var upload_translated_messages = upload_translated_messages
    || {
        "upload.Messages.btnOk" : "<%=m_context.localize("upload.Messages.btnOk", "Ok")%>",
        "upload.Messages.btnCancel" : "<%=m_context.localize("upload.Messages.btnCancel", "Cancel")%>",
        "upload.Messages.btnClose" : "<%=m_context.localize("upload.Messages.btnClose", "Close")%>",
        "upload.Messages.btnYes" : "<%=m_context.localize("upload.Messages.btnYes", "Yes")%>",
        "upload.Messages.btnNo" : "<%=m_context.localize("upload.Messages.btnNo", "No")%>",
        "upload.Messages.lblUploading" : "<%=m_context.localize("upload.Messages.lblUploading", "Uploading")%>",
        "upload.Messages.lblOverall" : "<%=m_context.localize("upload.Messages.lblOverall", "Overall")%>",
        "upload.Messages.lblTimeEstimates" : "<%=m_context.localize("upload.Messages.lblTimeEstimates", "Elapsed time / Remaining time: {0} / {1} ({2} MB/s)")%>",
        "upload.Messages.msgCloseOnCompletion" : "<%=m_context.localize("upload.Messages.msgCloseOnCompletion", "Close this dialog when the upload completes.")%>",
        "upload.Messages.msgErrGeneral" : "<%=m_context.localize("upload.Messages.msgErrGeneral", "Error")%>",
        "upload.Messages.tltUpload" : "<%=m_context.localize("upload.Messages.tltUpload", "Upload progress")%>",
        "upload.Messages.errInvalidFilesExtension" : "<%=m_context.localize("upload.Messages.errInvalidFilesExtension", "File extension is not supported:")%>",
        "upload.Messages.errInvalidFiles" : "<%=m_context.localize("upload.Messages.errInvalidFiles", "There are some invalid files!")%>",
        "upload.Messages.errInvalidFilesSize" : "<%=m_context.localize("upload.Messages.errInvalidFilesSize", "File size is too big (configured maximum size of {0} MB):")%>",
        "upload.Messages.errInvalidBatchSize" : "<%=m_context.localize("upload.Messages.errInvalidBatchSize", "Batch size is too big (configured maximum size of {0} MB).")%>",
        "upload.Messages.questionSkipContinue" : "<%=m_context.localize("upload.Messages.questionSkipContinue", "Skip them and continue?")%>",
        "upload.Messages.errMultiFileNotSupported" : "<%=m_context.localize("upload.Messages.errMultiFileNotSupported", "Multifile upload not supported.")%>",
        "upload.Messages.errTooManyFiles" : "<%=m_context.localize("upload.Messages.errTooManyFiles", "You have tried to upload {0} files but just a maximum of {1} files are allowed.")%>",
        "upload.Messages.errUpload" : "<%=m_context.localize("upload.Messages.errUpload", "Upload error.")%>",
        "upload.Messages.errNoFiles" : "<%=m_context.localize("upload.Messages.errNoFiles", "No files to be uploaded.")%>",
        "upload.Messages.errFileNotFound" : "<%=m_context.localize("upload.Messages.errFileNotFound", "File not found {0}.")%>",
        "upload.Messages.pasteMenuItem" : "<%=m_context.localize("upload.Messages.pasteMenuItem", "Paste")%>"
    };
    
var dndUploadEventUrl = '<%=strEventURL%>';
/**
 * Function called after the upload process is finished.
 */
function uploadCompleted() {
    if (dndUploadEventUrl != null) {
        parent.location.href = dndUploadEventUrl;
    } else {
        alert('No Drag and Drop upload event URL configured!');
    }
}
<%
if (useExtJsForDragAndDrop)
    {%>
    // DNDUpload configurations and localizations.
    DND.onSuccess = uploadCompleted;
    DND.images = "<%=m_context.getBaseURL()%>/js/dndupload/imgs";
    Ext.apply(upload.Messages, upload_translated_messages);
<%}
  else
  {//No ExtJs%>
var upload = new Object();    
upload.Messages = upload_translated_messages;
<%}%>
</script>
<div style="width: 40px;">
    <ul class="OwRecordRecordFunctionView">
        <li>
<%
        // holds the whole button: icon-delimiter-link within an applet
        StringBuffer sButton = new StringBuffer();
        sButton.append("<html><head><link rel='stylesheet' href='");
        sButton.append(m_View.getContext().getDesignURL());
        sButton.append("/css/ow_applet.css' type='text/css'>");
        sButton.append("</head><body class='OwDragAndDropTarget'>");
        // OwDragAndDropTarget
        StringBuffer dropTarget = new StringBuffer();
        if(!useHtml5DragAndDrop) {
            dropTarget.append("<div class='OwDragDropAppletContent'");
        } else {
            dropTarget.append("<div class='OwDragDropHtml5Content'");
            dropTarget.append(" id='dropArea'");
        }
        dropTarget.append(">");

        if (!recordFunctionPlugin.getNoEvent())
        {
            dropTarget.append("<a title=\"");
            dropTarget.append(escapedToolTip);
            dropTarget.append("\" href=\"");
            dropTarget.append(strEventURL);
            dropTarget.append("\" target=\"_top\">");
        }

        // sButton.append(recordFunctionPlugin.getBigIconHTML(rootObject, folderObject));
        if(!useHtml5DragAndDrop) {
            dropTarget.append("<img class='OwFunctionBigIcon' src='" + recordFunctionPlugin.getBigIcon() + "' title='" + escapedToolTip + "' alt='' border='0' width='24' height='24'>");
        } else {
            dropTarget.append(recordFunctionPlugin.getBigIconHTML(rootObject, folderObject));
        }
        if (!recordFunctionPlugin.getNoEvent())
        {
            dropTarget.append("</a>");
        }

        dropTarget.append("</div>");
        sButton.append(dropTarget);
        sButton.append("</body></html>");

        String cookiesData = m_context.getSessionCookieData(request);

        String baseUrl = m_View.getContext().getBaseURL();
        boolean isMultifile = recordFunctionPlugin.isMultifileDragDropAllowed();

        if(!useHtml5DragAndDrop) {
%><%-- DND Applet setup --%> 
        <span class="OwFunctionBigIcon" id="<%=recordFunctionPlugin.getPluginID()%>"> 
                    <script type="text/javascript">
                              insertDndApplet("<%=recordFunctionPlugin.getPluginID()%>",
                                      "<%=cookiesData%>",
                                      "<%=baseUrl%>",
                                      "<%=sButton.toString()%>", 
                                      "cfg.properties",
                                       <%=Boolean.toString(isMultifile)%>,
                                       <%=Boolean.toString(useExtJsForDragAndDrop)%>,
                                       upload.Messages);
                              DnDAppletEnableDrop(<%=fEndabled%>);
                    </script>
        </span><%
        } else {
            
            java.util.Properties settings = (java.util.Properties) session.getAttribute("DNDApplet.properties");
            if (settings == null)
            {
                settings = new java.util.Properties();
                java.net.URL url = application.getResource("/applets/dndupload/cfg.properties");  
                settings.load(url.openStream());
                session.setAttribute("DNDApplet.properties", settings);
            }
%>
        <span class="OwFunctionBigIcon" id="<%=recordFunctionPlugin.getPluginID()%>"><%=dropTarget%>
            <script type="text/javascript">
            if (new XMLHttpRequest().upload) 
            {
                
               Ext.onReady(function() {
                   var uploadSettings = {
<%
                          for(Entry entry : settings.entrySet()) {
                              if("multifile".equalsIgnoreCase((String)entry.getKey())) {
                                  continue;
                              }
%>
                              <%=entry.getKey()%>:"<%=entry.getValue()%>",
<%
                          }
%>
                              multifile:"<%=isMultifile%>"
                    };

                   var dropArea = document.getElementById("dropArea");
                   setupHtml5Dnd(uploadSettings, <%=fEndabled%>);
                   addHtml5DropArea(dropArea, true);
               });
            }
            else
            {
                alert("<%=m_context.localize("jsp.owrecord.OwRecordRecordFunctionDndView.html5Upload.notSupported", "Your Browser does not support HTML5 upload, please change configuration.")%>");
            }
            </script>
        </span>
<%    
        }

         // link
         if (recordFunctionPlugin.getNoEvent())
         {
%> <span title="<%=escapedToolTip%>"><%=recordFunctionPlugin.getLabel(rootObject, folderObject)%></span>
<%
         }
         else
         {
%>
 <a title="<%=escapedToolTip%>" href="<%=strEventURL%>" target="_top"><span><%=recordFunctionPlugin.getLabel(rootObject, folderObject)%></span></a>
<%
         }
            // register with keyboard as well
            if (!recordFunctionPlugin.getNoEvent())
            {
                ((OwMainAppContext) m_View.getContext()).registerPluginKeyEvent(recordFunctionPlugin.getPluginID(), strEventURL, null, recordFunctionPlugin.getDefaultLabel());
            }
%>
        </li>
    </ul>
</div>
<%
    }
%>