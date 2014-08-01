<%@ page 
    import="java.util.Enumeration"
    autoFlush="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
    <head>
        <style type="text/css">
        html {
            font-family: Arial, Helvetica, sans-serif;
            font-size: 0.78em;
            line-height: 1.2em;
        }
        </style>    
    	<script type="text/javascript">
        	function toggleElementDisplay(id) {
            	var details = document.getElementById(id);
            	if ( details ) {
                	if (details.style.display == 'none') {
                    	details.style.display = '';
                    } else {
                        details.style.display = 'none';
                    }
            	}
        	}
       	</script>
       	<title>Alfresco Workdesk - Configdump</title>
    </head>
    
	<body>
		<div id="configdump">
			<jsp:include page="configdumpContent.jsp" flush="true"/>
		</div>
		
		<div id="debug.jsp">
			<a href="javascript:toggleElementDisplay('checkPackages');">
				<H3>Check all required Java Packages (jar files & libraries)</H3>
			</a>
			<div id="checkPackages" style="display: none;">
			    <a name="start"></a>
				<jsp:include page="checkPackages.jsp" flush="true"/>
			</div>		
		</div>
		<div id="HTTP communication">
		<table border="1">
		<H3>Request Parameter</H3>
<%  
          Enumeration enu = request.getHeaderNames();
          while (enu.hasMoreElements())
          {
            String name = String.valueOf(enu.nextElement());
            %><tr><td><%=name%></td><td>
            <%=request.getHeader(name)%>
            </td></tr><%
          }
%>        <tr><td>User Principal</td><td><%=request.getUserPrincipal()%></td></tr>
          <tr><td>Remote User</td><td><%=request.getRemoteUser()%></td></tr>
          <tr><td>Encoding</td><td><%=request.getCharacterEncoding()%></td></tr>
          <tr><td>Content length</td><td><%=request.getContentLength()%></td></tr>
          <tr><td>Ctx. Path</td><td><%=request.getContextPath()%></td></tr>
          <tr><td>Local Addr.</td><td><%=request.getLocalAddr()%></td></tr>
          <tr><td>Local Name</td><td><%=request.getLocalName()%></td></tr>
          <tr><td>Method</td><td><%=request.getMethod()%></td></tr>
          <tr><td>Path info</td><td><%=request.getPathInfo()%></td></tr>
          <tr><td>Path translated</td><td><%=request.getPathTranslated()%></td></tr>
          <tr><td>Protocol</td><td><%=request.getProtocol()%></td></tr>
        </table>
		</div>
	<br/><br/><br/>
	<p><font size="-2">
	Alfresco Workdesk<br/>
    Copyright (c) Alfresco Software, Inc. All Rights Reserved.<br/>
	</font></p>		
	</body>
</html>