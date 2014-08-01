<%@page	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%><%
// set header every time before sending the body
   response.setHeader("Expires", "-1");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../../headinc.jsp"%><%
    // create the cookies data
    String cookiesData = m_context.getSessionCookieData(request);

    StringBuffer m_sServerURL = new StringBuffer();
    if (request.isSecure())
    {
        m_sServerURL.append("https://");
    }
    else
    {
        m_sServerURL.append("http://");
    }
    m_sServerURL.append(request.getServerName());
    m_sServerURL.append(":").append(String.valueOf(request.getServerPort()));
    m_sServerURL.append(request.getContextPath());
%>
		<title><%=m_context.localize("server.plug.owdocsend.window.title", "Send documents as e-mail attachment")%></title>
		<script language="JavaScript">
		function openEmailClient(param) {
			if (param == null) {
				alert('<%=m_context.localize("server.plug.owdocsend.noAttachment", "No attachment found")%>');
				return false;
			}
			var mailURL='mailto:?subject=<%=m_context.localize("server.plug.owdocsend.subject", "Email with attachment from Alfresco Workdesk")%>&body=<%=m_context.localize("server.plug.owdocsend.body", "See attached file...")%>&attach=';
			mailURL += param;
			self.location.href=mailURL;
			closeSelf();
		}
		function showErrorAndClose(errorMessage) {
			alert(errorMessage);
			closeSelf();
		}
		function closeSelf() {
			if (self && self.open &&!self.closed)
			{
				self.close();
			}
		}

 		function isAttachmentDwnEnabled()
 		{
			return window.opener.canAttach;
		}

 		function getEmailBody()
		{
			return window.opener.mailBody;
		}

		function getEmailSubject()
		{
			return window.opener.mailSubject;
		}


  	</script>
	</head>
	<body class="owBody default">

        <div style="width: 100%; height: 30%; padding-top: 40px;">
            <p align="center">
                <applet
                    id="multifileattachment" name="Multifile Attachment"
                    width="120" height="20" alt="Multifile Attachment"
                    archive="ow_clientservices.jar,httpclient-4.1.1.jar,httpcore-4.1.jar,commons-logging-1.1.1.jar,commons-codec-1.4.jar"
                    code="com.wewebu.ow.clientservices.applets.OwFileDownloadApplet"
                    codebase="<%=m_sServerURL.toString()%>/applets/clientservices" class="OwFileDownloadApplet" MAYSCRIPT="true">
                    <param name="cache_archive_ex" value="ow_clientservices.jar;preload;4.2+"/>
                    <param name="jSession" value="<%=cookiesData%>" />
                    <param name="downloadURL"   value="<%=m_sServerURL.toString()%>/multifileDownload/?z=<%=Long.toHexString(new java.util.Random().nextLong())%>" />
                    <param name="msg_no_attachmentDownloaded" value="<%=m_context.localize("server.plug.owdocsend.msg_no_attachmentDownloaded","No attachment was downloaded!")%>"/>
                    <param name="msg_exception" value="<%=m_context.localize("server.plug.owdocsend.msg_exception","Exception: {0} \nPlease see the Java console.")%>"/>
                    <param name="accept_insecure_certificates" value="false"/>';
                </applet>
            </p>
            <p align="center" style="font-weight: bold; color: #000000;">
                <%=m_context.localize("server.plug.owdocsend.msg_status","Creating email...")%>
            </p>
         </div>
         <div style="width: 100%; height: 30%;"></div>
   </body>
</html>