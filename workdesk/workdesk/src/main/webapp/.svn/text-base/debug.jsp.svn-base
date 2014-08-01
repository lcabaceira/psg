<%@ page autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML>
	<HEAD>
		<TITLE>Alfresco Workdesk - Debug Page</TITLE>
		<META http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<STYLE type="text/css">
        html {
            font-family: Arial, Helvetica, sans-serif;
            font-size: 0.78em;
            line-height: 1.2em;
        }
        </STYLE>
	</HEAD>
	<BODY>
		<DIV id="head">
			<DIV id="headbox">
				<P id="logo">
					<A href="http://www.alfresco.com/"><br>
					</A>
				</P>
			</DIV>
		</DIV>

		<DIV id="content">
			<%
			    if (request.getParameter("checkpackages") != null)
			    {
			%>
				<H3>
					Alfresco Workdesk - Check all required Java Packages (jar files & libraries)
				</H3>
				<jsp:include page="checkPackages.jsp" flush="true" />
				<BR><BR>
			<%
			    }
			    else
			    {
			%>
				<H3>
					Alfresco Workdesk Debug Page
				</H3>
				<H4>
					Available debug options:
				</H4>
				<UL>
				<LI>
				<a href="debug.jsp?checkpackages=1">Check all required Java Packages (jar files & libraries)</a>
				</LI>
				<BR/>
                <LI>
				<a href="configdump.jsp">Alfresco Workdesk - Configuration Dump (configdump.jsp)</a>
				</LI>
				</UL>
			<%
			    }
			%>
		</DIV>
<br/><br/><br/>
<p><font size="-2">
Alfresco Workdesk
Copyright (c) Alfresco Software, Inc. All Rights Reserved.
</font></p>
	</BODY>
</HTML>