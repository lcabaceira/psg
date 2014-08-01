<%@ page isErrorPage="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>
<%@page import="java.io.PrintWriter"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>

		<link rel='stylesheet' href='designs/default41/css/ow.css'
			type='text/css'>

	</head>
	<body>
		<%
		    //Throwable e = m_context.getError();
		    if (exception != null)
		    {
		        // print error which occurred upon recent request
		        //com.wewebu.ow.server.util.OwExceptionManager.PrintCatchedException(m_context.getLocale(), exception, new java.io.PrintWriter(out), "OwErrorStack");
		        String localizedMessage = exception.getLocalizedMessage();
		        PrintWriter pw = new PrintWriter(out);
		%>
		<p class="OwInfoBar">
			The Alfresco Workdesk application failed to execute your request
			<br />
			Reason:
			<%=localizedMessage%>
		</p>
		<br />
		<%
		    if (exception instanceof javax.servlet.ServletException)
		        {
		            javax.servlet.ServletException servletException = (javax.servlet.ServletException) exception;
		            if (servletException.getRootCause() != null)
		            {
		%>
		<p class="OwInfoBar">
			Root cause:
			<br />
			<pre class='OwErrorStack'>
		<%
servletException.getRootCause().printStackTrace(pw);
		%>
		</pre>
		</p>
		<%
		    }
		        }
		        else
		        {
		%>
		<p class="OwInfoBar">
			Stacktrace:
			<br />
			<pre class='OwErrorStack, OwInfoBar'><%exception.printStackTrace(pw);%></pre>
		</p>
		<%
		    }
		    }
		%>

	</body>
</html>
