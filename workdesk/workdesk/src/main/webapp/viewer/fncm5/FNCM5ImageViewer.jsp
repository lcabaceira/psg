<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page
	import="java.util.Iterator, java.util.Collection, java.util.LinkedList,
	com.wewebu.ow.server.ecm.OwObject,
	com.wewebu.ow.server.app.OwMimeManager,
    com.wewebu.ow.server.ecm.OwStatusContextDefinitions,
    com.wewebu.ow.server.ecm.OwNetwork,
    com.wewebu.ow.server.util.OwExceptionManager,
    com.wewebu.ow.server.plug.owremote.OwRemoteLinkBuilder,
    com.wewebu.ow.server.plug.owremote.OwRemoteLinkConfiguration,
    com.wewebu.ow.server.ui.OwAppContext"
%>
<html>
<head>
<%@ include file="../../headinc.jsp" %>
<%
try
{
// === create base URL
    String userId 			= m_context.getNetwork().getCredentials().getUserInfo().getUserName();   
	String strBaseURL 		= m_context.getBaseURL();
	String strServerURL		= m_context.getServerURL();
	
// === get query parameters
    //get DMSID from query
    String strDMSID     	= request.getParameter("dmsid");
	String strDMSIDEnc		= OwAppContext.encodeURL(strDMSID);
	
    //get requested page number from query
    String strPage      	= request.getParameter("page");

	
    if ( strPage == null )
        strPage = "1";

    String fileParameter= null;
    //object store name, need for AnnotSecurity for P8
	String strOSName = null;

// === get capabilities for the viewer
    // get the requested object
    OwObject obj = null;
    
    if( strDMSID.equals(OwMimeManager.FILE_PREVIEW_DMSID) )
    {
        String fullpath = (String)session.getAttribute(OwMimeManager.FILE_PREVIEW_ATTRIBUTE_NAME);
        String fileName = (String) request.getSession().getAttribute(OwMimeManager.FILE_PREVIEW_ATTRIBUTE_FILENAME);
        String mimeType = (String) request.getSession().getAttribute(OwMimeManager.FILE_PREVIEW_ATTRIBUTE_MIMETYPE);
        java.io.File localFile = new java.io.File(fullpath);
        if (mimeType != null)
        {
            obj = new com.wewebu.ow.server.ecm.OwFileObject(m_context.getNetwork(), localFile, fileName, mimeType);
        }
        else
        {
            obj = new com.wewebu.ow.server.ecm.OwFileObject(m_context.getNetwork(), localFile);
        }
        strOSName = "LocalFile";
    }
    else
    {
        obj = m_context.getNetwork().getObjectFromDMSID(strDMSID,false);
        strOSName = obj.getResource().getDisplayName(m_context.getLocale());
    }

    //get number of pages
    String strPageCount     = String.valueOf(obj.getPageCount());
//    if ( strPageCount.equals("1") )
        fileParameter   = "filename";
  //  else
  //      fileParameter   = "basefilename";   
    

    // get print capability
    boolean canPrint = m_context.getNetwork().canDo(obj,OwNetwork.CAN_DO_FUNCTIONCODE_PRINT,OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
    // get delete capability
    boolean canDelete = m_context.getNetwork().canDo(obj,OwNetwork.CAN_DO_FUNCTIONCODE_DELETE_ANNOTATION,OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
    // get edit capability
    boolean canEdit = m_context.getNetwork().canDo(obj,OwNetwork.CAN_DO_FUNCTIONCODE_EDIT_ANNOTATION,OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
    // get edit capability
    boolean canCreate = m_context.getNetwork().canDo(obj,OwNetwork.CAN_DO_FUNCTIONCODE_CREATE_ANNOTATION,OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
    // get save capability
    boolean canSave = m_context.getNetwork().canDo(obj,OwNetwork.CAN_DO_FUNCTIONCODE_SAVE_CONTENT_TO_DISK,OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);

	String annotationHideContextButtons = null;
	
	if ( canEdit )
	{
		annotationHideContextButtons = "text,hyperlink,behind";
		
		if ( ! canDelete )	
			annotationHideContextButtons += ",delete";
	}
	else
	{
		annotationHideContextButtons = "save,security,text,increaseline,decreaseline,increasefont,decreasefont,increasearrowhead,decreasearrowhead,linecolor,fillcolor,strikethrough,transparent,hyperlink,rotater,rotatel,angleflip,behind,delete";
	}
	
// === check if pure P8 or IS Proxy document
	boolean fISProxy = false;
  
    // URL to the content of the object
    String strDocContentURL         = strBaseURL + "/getContent?dmsid=" + strDMSIDEnc;

    // URL to load the annotation content
    String strLoadAnnotationURL    	= strBaseURL + "/getContent?dmsid=" + strDMSIDEnc + "&cttyp=2&xsltrans={basedir}/viewer/fncm5/Annot.xsl&xslencode=ISO-8859-1";
    
	// URL to retrieve group user information for IS
	String strISUserGroupListURL    = strBaseURL + "/getContent" + "?dmsid=" + strDMSIDEnc + "&cttyp=3";
	
	// URL to retrieve group user information for P8
	String strP8UserGroupListURL 	= strBaseURL + "/getFNCMDaejaAnnotationSecurity?objectStoreName=" + strOSName + "&dmsid=" + strDMSIDEnc;


    // URL to the annotation content, automatically convert the annotation content with the Annot.xsl sheet.
	String strSaveAnnotationURL		= null;
	
	String strfilenetSystem			= null;
	
	// different annotation formats for IS proxy or pure P8 document
    if ( fISProxy )
    {
    // === IS Proxy
    	// the FileNet system
    	strfilenetSystem			= "0";
    	
	    // URL to save the annotation content to IS
    	strSaveAnnotationURL     	= strBaseURL + "/setFNIMDaejaViewerAnnotation?dmsid=" + strDMSID;
	}
	else
	{
	// === P8 
    	// the FileNet system
    	strfilenetSystem			= "3";
    	
	    // URL to save the annotation content to P8
	   	strSaveAnnotationURL     	= strBaseURL + "/setFNCMDaejaViewerAnnotation?objectStoreName=" + strOSName + "&dmsid=" + strDMSID;
	}

%>

	<title><%=obj.getName()%></title>
	</head>
	<body id="<%=com.wewebu.ow.server.ui.OwWebApplication.BODY_ID%>" style="margin:0px;">

		<script type="text/javascript">

			function ow_view_one_savepositions()
			{
				SaveWindowPositions(window.opener,self,'<%=m_context.getWindowPositionsSaveURL()%>');
			}

			function ow_view_reloadDocument(dmsid,page,name)
			{	
				//liest den Dateinamen aus dem dmsid-String
				
				document.title = name;	//Titel
				viewONE.setAnnotationFile('<%=strBaseURL%>' + '/getContent?dmsid=' + dmsid + '&cttyp=2&xsltrans={basedir}/viewer/fncm/Annot.xsl&xslencode=ISO-8859-1');
				viewONE.reloadAnnotations();
				viewONE.openFile('<%=strBaseURL%>' + '/getContent?dmsid=' + dmsid,page);		
			}

			function ow_send_email_link()
			{
<%
    if( !strDMSID.equals(OwMimeManager.FILE_PREVIEW_DMSID) )
    {
        OwRemoteLinkBuilder builder = new OwRemoteLinkBuilder();
        LinkedList listObj = new LinkedList();
        listObj.add(obj);
        out.println(builder.createJScript(listObj, m_context, OwRemoteLinkConfiguration.NULL_COFIGURATION, "window.opener.location.href", false));
    }
%>
			}
		</script>
        <div style="height: 100%;">
<%
	// check if mdi collection was submitted in session
	String sMdiCollectionAttributeName    	= OwMimeManager.MDI_OBJECTS_COLLECTION_ATTRIBUTE_NAME;
	
	Collection objects = null;

	if (request.getParameter("compare") != null &&  sMdiCollectionAttributeName != null )
		objects = (Collection)session.getAttribute(sMdiCollectionAttributeName);

	if ( null != objects )
	{
%>
		<div style="height: 8%; background: #cccccc">
		<script type="text/javascript">

			var	maxdocuments_g = <%=objects.size()%>;
			var currentdocument_g = 0;

			var docids = new Array(
<%
				String dmsId;
				Iterator it = objects.iterator();
				while ( it.hasNext() )
				{
					OwObject objref = (OwObject)it.next();
					dmsId = objref.getDMSID();
				    out.write("'");
				    out.write(OwAppContext.encodeURL(dmsId));
				    out.write("'");
					if (it.hasNext())
					{
					    out.write(",");
					} 
				}
            %>);

			function openNextDocument()
			{
				currentdocument_g++;
				if ( currentdocument_g >= maxdocuments_g )
					currentdocument_g = 0;

				var doclink = document.getElementById("document_" + currentdocument_g);
				setActive(doclink);
				doclink.click();
			}

			function openPrevDocument()
			{
				currentdocument_g--;
			
				if ( currentdocument_g < 0 )
					currentdocument_g = maxdocuments_g-1;

				var doclink = document.getElementById("document_" + currentdocument_g);
				setActive(doclink);
				doclink.click();
			}

			function setActive(object)
			{
				if ( object.setActive )
					object.setActive();
			}

			function compareDocument()
			{
				var compareindex = currentdocument_g;

				compareindex++;

				if ( compareindex >= maxdocuments_g )
					compareindex = 0;

				var sURL = "<%=strBaseURL%>/viewer/fncm/FNCMImageViewer.jsp?dmsid=" + docids[compareindex] + "&page=1";
				openViewerWindowArranged(sURL,'owcompare',docids[compareindex],1,50,100,0,0,0,0,0,0,false,1);
			}
		</script>

		<input title="<%=m_context.localize("fncm.viewer.FNCMImageViewer.openPrevDocumentTooltip", "Vorheriges Dokument") %>" type="button" onclick="openPrevDocument();" value="&nbsp;&nbsp;&nbsp;&#60;&nbsp;&nbsp;&nbsp;">
		<input title="<%=m_context.localize("fncm.viewer.FNCMImageViewer.openNextDocumentTooltip", "Nächstes Dokument") %>" type="button" onclick="openNextDocument();" value="&nbsp;&nbsp;&nbsp;&#62;&nbsp;&nbsp;&nbsp;">
		<input title="<%=m_context.localize("fncm.viewer.FNCMImageViewer.compareDocumentTooltip", "Dokument vergleichen") %>" type="button" onclick="compareDocument();" value="<%=m_context.localize("fncm.viewer.FNCMImageViewer.compareDocument", "Vergleichen") %>">
		<br />
<%
            it = objects.iterator();
			int iIndex = 0;
			while ( it.hasNext() )
			{
				OwObject objref = (OwObject)it.next();
%>
			<a id="document_<%=iIndex%>" class="OwMimeItem" href="javascript:ow_view_reloadDocument('<%=objref.getDMSID() %>',1,'<%=objref.getName() %>');"><%=objref.getName() %></a>
<%
				if ( it.hasNext() )
				{
%>
			<span class="OwMimeItem">&nbsp;|&nbsp;</span>
<%
				}
				iIndex++;
			}
%>
		</div><div style="height: 92%;">
<%
	}
	else
	{
%>
		<div style="height: 100%;">
<%
	}

%><%-- 

	YOU CAN USE OBJECT OR EMBED TAG AS WELL:
	
	NOTE: Object tag can keep the params whereas the embed tag needs the params as attributes.
	

	Exact JRE match: 		<OBJECT classid="clsid:CAFEEFAC-0014-0002-0000-ABCDEFFEDCBA"
	Use Version or above: 	<OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"   
	    width="100%" height="100%" align="middle"
	    codebase="http://java.sun.com/products/plugin/1.4/jinstall-14-win32.cab#Version=1,4,0,mn">
	    <PARAM name="code" value="ji.applet.jiApplet.class">
	    <PARAM name="codebase" value="<%=strBaseURL%>/FnJavaV1Files">
	    <PARAM NAME="type" VALUE="application/x-java-applet;jpi-version=1.4">
	    <PARAM NAME="scriptable" VALUE="true">
	    <COMMENT>
	        <EMBED type="application/x-java-applet" 
	        	width="100%" height="90%" align="middle"
	    		code="ji.applet.jiApplet.class" 
	    		archive="ji.jar,p8securityDlg.jar,p8securityDlgResources.jar"
	    		codebase="<%=strBaseURL%>/FnJavaV1Files"
	            pluginspage="http://java.sun.com/j2se/1.4/download.html">
	            <NOEMBED>
	                No Java 2 SDK, Standard Edition v 1.4 support for APPLET!!
	            </NOEMBED>
	        </EMBED>
	    </COMMENT>
	</OBJECT>
   writeParam('annotate', 'true');      // Required to enable annotations
    writeParam('annotateEdit', 'true');

--%>
    <applet archive="viewer-security-dialog.jar,viewer-security-dialog-i18n.jar,ji.jar,daeja1.jar"
          	codebase="/workdesk_deja/FnJavaV1Files"
            code="ji.applet.jiApplet.class" name="viewONE" id="viewONE" 
            width="100%" height="100%" hspace="0" vspace="0"
            align="middle" 
            mayscript="true" >
			<param name="bar1button1" value="ow_view_one_savepositions(),<%=m_context.localize("jsp.viewer.fncm.FNCMImageViewer.savepositionsbtn","Größe / Position speichern")%>,<%=strBaseURL%>/viewer/fncm/saveposenabled.gif,<%=strBaseURL%>/viewer/fncm/saveposdisabled.gif,false">
<%
    if( !strDMSID.equals(OwMimeManager.FILE_PREVIEW_DMSID) )
    {
%>
            <param name="bar1button2" value="ow_send_email_link(),<%=m_context.localize("jsp.viewer.fncm.FNCMImageViewer.email_link","e-Mail Link")%>,<%=strBaseURL%>/viewer/fncm/sendlinkenable.gif,<%=strBaseURL%>/viewer/fncm/sendlinkdisable.gif,false">
<%
    }
%>          <%-- <param name="java_arguments" value="-Xmx512m"> --%>
            <param name="JavaScriptExtensions" value="true">
            <param name="annotationJavascriptExtensions" value="true">

            <param name="cabbase" value="ji.cab">
            <param name="cache_archive" value="viewer-security-dialog.jar,viewer-security-dialog-i18n.jar,ji.jar">

            <param name="backcolor" value="192,192,192">
            <param name="mayscript" value="true">
            <param name="<%=fileParameter%>" value="<%=strDocContentURL%>">

            <param name="viewmode" value="fullpage">
            <param name="annotationSuppressEmptyMessages" value="true">
            <param name="annotationColorMask" value="0bgr">
            <param name="annotationFile" value="<%=strLoadAnnotationURL%>">
            <param name="annotationEncoding" value="UTF-8">
            <param name="annotationTrace" value="false">
<%--		Page identifier --%>
            <param name="page" value="<%=strPage%>">
            <param name="pageNumber" value="<%=strPage%>">
            <param name="pagecount" value="<%=strPageCount%>">

            <param name="allKeys" value="false">
            <param name="fileButtons" value="<%=canSave%>">
            <param name="fileMenus" value="<%=canSave%>">
            <param name="fileKeys" value="<%=canSave%>">

<%--		Print capability --%>
            <param name="printButtons" value="<%=String.valueOf(canPrint)%>">
            <param name="printMenus" value="<%=String.valueOf(canPrint)%>">
            <param name="printKeys" value="<%=String.valueOf(canPrint)%>">

<%--		Edit Annotation capability --%>
            <param name="annotationHideContextButtons"  value="<%=annotationHideContextButtons%>">

<%--		Create Annotation capability --%>
<% 		if (canCreate) { 
%>          <param name="annotationhidebuttons" value="line, highlightpoly, rectangle, redact, redactpoly, poly, openpoly, oval, hyperlink">
<% 		} else { 
%>          <param name="annotationhidebuttons" value="select,line,arrow,text,solidText,note,highlight,hyperlink,highlightPoly,rectangle,Square,redact,redactPoly,poly,openPoly,oval,circle,freehand,stamp,ruler,angle">
<% 		} 
%>          <param name="language" value="<%=m_context.getLocale().toString()%>">
            <param name="annotate" value="true">
            <param name="annotateEdit" value="true">
            <param name="flipOptions" value="true">
            <param name="scale" value="ftow">
            <param name="resProduct" value="FileNET Java Viewer">
            <param name="resWebSite" value="www.filenet.com">
            <param name="resEmail" value="css-filenet.com">
            <param name="annotationSavePost" value="<%=strSaveAnnotationURL%>">
            <param name="invertButtons" value="true">
            <param name="routeDocs" value="true">
            <param name="annotationAutoSaveJ2Shutdown" value="false"> 
            <param name="trace" value="false">
            <param name="traceNet" value="false">
            <param name="buttonResource1" value="fnbt1.v1">
            <param name="userId" value="<%=userId%>">
            <param name="xScroll" value="0">
            <param name="yScroll" value="100">

<%-- filenetExtendedAnnotations
              
					When set, the viewer is put into extended annotation mode. This enables the nonstandard
					annotations support within the viewer.
					When enabled, non-standard annotations are added to the XML returned by the viewer
					during an annotation save. These non-standard annotations are save with a class name
					parameter (F_CLASSNAME) of "Proprietary",a class ID property.(CLASS_ID) of
					{A91E5DF2-6B7B-11D1-B6D7-00609705F027} and a subclass name property
					(F_SUBCLASS) that describes the annotation.
					
					Below, is a list of the extended annotation
					types and their subclass names:
					
						Image Stamp v1-Image Stamp
						Open Polygon v1-Open Polygon
						Highlight Polygon v1-Highlight Polygon
						Polygon v1-Polygon
						Redaction Polygon v1-Redaction Polygon
						Rectangle v1-Rectangle
						Oval v1-Oval
						Redaction v1-Redaction
						Line v1-Line
					
					When this parameter is set, the signature selector functionality is enabled allowing the use
					of signatureSelector, signatureSelectorURL and signatureSelectorWindowName
					parameters for configuring the signature selector. These parameters wil have no effect if
					filenetExtendedAnnotations is not set to true.
					Lastly, with this parameter set to true, the stickynote view functionality is enabled and the
					fnStickyNoteView parameter can be used to control the viewing and printing of sticky note
					annotations.
					The default value of this parameter is "false"..

				<param NAME="filenetExtendedAnnotations" value="true">
--%><%-- 
fnStickyNoteView
					Specifies the default view value for new sticky note annotations.
					This parameter is only used when the filenetSystem parameter is set to 2 (for CS
					extended annotation mode) or 3 (for P8).
					The available values for this parameter are as follows:
					
						1 - Allows viewing but not printing of new sticky note annotations.
						2 - Allows printing but not viewing of new sticky note annotations.
						3 - Allows both printing and viewing of new stick note annotations.
					
					NOTE: This parameter has no effect if the filenetExtendedAnnotations parameter is not
					set to true.        

--%><%--param NAME="fnStickyNoteView" value="2"--%><%--
 
signatureSelector
					If set to "true", this parameter enables the signature selector menu item in the annotation
					stamps menu.
					This feature is only available when the filenetSystem parameter is set to 2 (for CS
					extended annotation mode).
					The default value for this parameter is "false".
					NOTE: This parameter has no effect if the filenetExtendedAnnotations parameter is not
					set to true.              

--%><%--param NAME="signatureSelector" value="true"--%><%--
 
signatureSelectorURL
					Defines the signature selector URL to be used for the signature selector.
					This parameter need only be provided if the signature selector is to be invoked from inside
					viewONE. If a call out to javaScript is required, the URL is not needed.
					When the signature selector menu item is clicked on from the stamp annotations menu,
					the specified URL is opened in a new window. The name of the window is set to the value
					supplied by the signatureSelectorWindowName parameter (see below).
					NOTE: This parameter has no effect if the filenetExtendedAnnotations parameter is not
					set to true.

--%><%--param NAME="signatureSelectorURL" value="http://myserver/mysigselector.htm" --%><%--
 
signatureSelectorWindowName
					Specifies the name to be given to the signature selector window.
					This parameter is only valid when used in conjunction with the signatureSelectorURL
					parameter (see above)
					If the value specified is the name of an existing window, viewONE will open the signature
					selector URL in this existing window rather than creating a new window.
					
					There are a number of predefined values that can be used with this parameter:
					
						"_self" Show in window and frame of applet.
						"_parent" Show in applet's parent frame.
						"_top" Show in the top-level frame of the applet's window.
						"_blank" Show in a new, unnamed top-level window.
					
					The default value for this parameter is "_blank".
					NOTE: This parameter has no effect if the filenetExtendedAnnotations parameter is not
					set to true.
				
				<param NAME="signatureSelectorWindowName" value="new_window">
--%><%-- 
annotationUnits
					This parameter changes the way in which viewONE expects annotation dimensions to be
					specified in the annotations definition file.
					Where "inches" is specified the viewer expects annotation's X, Y, WIDTH and HEIGHT
					properties to be given in inches. Where "pixels" is specified the viewer expects those
					properties to be given in pixels. This parameter also affects the annotationNoteSize
					parameter.
					The default value is "pixels".
					Note that if "inches" is specified, DPI information must exist in the all the header of the
					images that make up the document.
--%>              	<param name="annotationUnits" value="inches">

<%--docIDMarker
					This parameter can be used to configure the viewer for different doc ID identification
					strings. This string is searched for in the page document URL and the characters following
					it and up to the next "?" character are taken as the document ID value.
					The default value for this parameter is "id=".
					NOTE: When requesting a COLD background image, docIDMarker is used to replace the
					document identfication string in the URL specified by fileNetCOLDTemplateResource.              
--%>				<param name="docIDMarker" value="id=">

<%-- pageIDMarker
				
					This parameter can be used to configure the viewer for different page ID identification
					strings. This string is searched for in the page document URL and the characters following
					it and up to the next "?" character are taken as the page ID value.

				<param NAME="pageIDMarker" value="myPageId="> 
--%><%-- 

addEscapeCharactersToXML
				
					If set to true, illegal characters contained in fields returned in the annotation XML are
				   	replaced with the corresponding escape character, e.g.:
				   
				   	The character '&' is replaced with "&amp;"
--%>				<param name="addEscapeCharactersToXML" value="true">       
<%-- 

filenet 			
			When set to "true" this parameter enabled FileNet specific features in viewONE.
--%>				<param name="filenet" value ="true">
<%-- 
FNSecURL
					Specifies the URL to be used to access the security dialog for the P8 system. When a
					user clicks on the padlock icon of the annotation toolbar this URL is used to open the
					extended security dialog for P8.              
--%>				<param name="FNSecURL" value="<%=strP8UserGroupListURL%>">

<%-- filenetUG
				
					Specifies which the URL used to retrieve User Group information for IS and CS systems.
					This URL is used during display of annotation security dialogs for IS and CS.
					viewONE appends "&User=true&Group=true" to the URL.
--%>				<param name="filenetUG" value="<%=strISUserGroupListURL%>">

<%-- filenetSystem
				
					Specifies which FileNet system viewONE is being integrated with. 
					Accepted values are:
					
						0 FileNet Image Services
						1 FileNet Content Services
						3 FileNet P8 Content Engine
					
					The default value is "0".              
--%>				<param NAME="filenetSystem" value="4">

<%-- fileNetCOLDTemplateResource
				
					This parameter is used to define the URL used to retrieve the template for COLD
					documents retrieved from a P8 system. The URL must have a document identification
					string, e.g. "id=0", embedded somewhere within it's definition for it to work properly.
					ViewONE will search for the document id, defined by docIDMarker, and replace the "0"
					value with either the GUID (if one is present) or the DocID from the COLD document
					header.
					There is no default value for this parameter and if it is not defined, P8 COLD documents
					are retrieved using the document loading URL instead.              
				<param name="fileNetCOLDTemplateResource" value="<%=strDocContentURL%>"> --%>              
        </applet></div>
<%
}
catch (Exception e)
{
	OwExceptionManager.PrintCatchedException(m_context.getLocale(),e,response.getWriter(),"OwOwWebApplication_EXCEPTION");
}
%>
</div>
</body>
</html>