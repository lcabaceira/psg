<%@ page isErrorPage="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>
		<%
		    // get a reference to the context
		    com.wewebu.ow.server.app.OwMainAppContext m_context = (com.wewebu.ow.server.app.OwMainAppContext) com.wewebu.ow.server.ui.OwWebApplication.getContext(session);
		%>

<%-- 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
		<%
		    // get a reference to the context
		    com.wewebu.ow.server.app.OwMainAppContext m_context = (com.wewebu.ow.server.app.OwMainAppContext) com.wewebu.ow.server.ui.OwWebApplication.getContext(session);
		%>
	
		<link rel='stylesheet' href='<%=m_context.getDesignURL()%>/css/ow.css'
			type='text/css'>
		<link rel='stylesheet'
			href='<%=m_context.getBaseURL()%>/js/extjs/resources/css/ext-all.css'
			type='text/css'>

	</head>
	<body>
--%>
		<%
		 //Throwable e = m_context.getError();
	        if (exception != null)
	        {
	            // print error which occurred upon recent request
	            //com.wewebu.ow.server.util.OwExceptionManager.PrintCatchedException(m_context.getLocale(), exception, new java.io.PrintWriter(out), "OwErrorStack");
				String localizedMessage = com.wewebu.ow.server.util.OwExceptionManager.getLocalizedMessage( exception, m_context.getLocale());
				String stackTrace = com.wewebu.ow.server.util.OwExceptionManager.getRelevantStackTrace( exception);
	        	out.write("['"+localizedMessage+"','"+stackTrace+"']");
		%>
		
		<%} %>	
	<%-- 	The Alfresco Workdesk application failed to execute your request
		<br />
		Reason:
		<br />
		<%
		    java.io.PrintWriter pw = new java.io.PrintWriter(out);
		%>
		<%
		    if (exception instanceof javax.servlet.ServletException)
		    {
		        javax.servlet.ServletException servletException = (javax.servlet.ServletException) exception;
		        if (servletException.getRootCause() != null)
		        {
		%>
		Root cause:
		<br />
		<pre class='OwErrorStack'>
		<%
		servletException.getRootCause().printStackTrace(pw);
		        }
		    }
		    else
		    {
		%>
		Stacktrace:<br/>
		<pre class='OwErrorStack'>
		<%
		    exception.printStackTrace(pw);
		    }
		%>
		</pre>

	</body>
</html>
--%>