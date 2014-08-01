<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" autoFlush = "false"  language="java"

%><jsp:useBean id="theApp"
    scope="request"
    class="com.wewebu.ow.server.ui.OwWebApplication" >
</jsp:useBean><%

	request.setCharacterEncoding("UTF-8");

    theApp.setContextClass(com.wewebu.ow.server.app.OwMainAppContext.class);
    theApp.setViewClass(com.wewebu.ow.server.app.OwMainLayout.class);
    theApp.setLoginClass(com.wewebu.ow.server.app.OwLoginView.class);
    theApp.setJspPath("main.jsp");

    theApp.handleRequest(application, request, response);

    //optional log session info
    //theApp.logSessionInfo(request,false);   
%>