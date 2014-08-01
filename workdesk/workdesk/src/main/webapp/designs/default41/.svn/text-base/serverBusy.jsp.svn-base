<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	autoFlush="false" language="java"%>
	
<jsp:useBean id="theApp"
	scope="request" class="com.wewebu.ow.server.ui.OwWebApplication">
</jsp:useBean>
<jsp:useBean id="ow_Context" scope="session" type="com.wewebu.ow.server.ui.OwAppContext"></jsp:useBean>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<!-- if JavaScript is disabled in the browser use meta tag -->
<meta http-equiv="refresh" content="5" />
<link rel="stylesheet" href="<%=ow_Context.getDesignURL()%>/css/ow.css"
	type="text/css" />
</head>
<body class="wait_page">
    <div class="wait_dialog">
		<div class="wait_icon">
		</div>
		<div class="wait_msg">
			<%=ow_Context.localize("ui.OwWebApplication.concurrentrequest", "Server is still busy with current request, please wait ...")%>
		</div>
	</div>
</body>
</html>