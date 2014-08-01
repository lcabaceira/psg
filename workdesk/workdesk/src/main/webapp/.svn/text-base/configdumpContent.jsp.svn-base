<%@ page autoFlush="true" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8" language="java"
	import="java.util.*"%>

<jsp:useBean id="theConfigBean" scope="request"
	class="com.wewebu.ow.server.app.OwConfigDump"></jsp:useBean>


<%
    Map<String, String> hiddenTags = new HashMap<String, String>();
    hiddenTags.put("java.naming.security.credentials", "*******");

    theConfigBean.setHiddenTags(hiddenTags);
    theConfigBean.handleRequest(application, request, response);
%>
