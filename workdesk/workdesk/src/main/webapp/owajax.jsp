<%@ page pageEncoding="UTF-8" autoFlush = "true"
	errorPage="owajaxerror.jsp" 
 contentType="text/html; charset=utf-8" language="java"%>


<jsp:useBean id="theApp"
    scope="request"
    class="com.wewebu.ow.server.ui.OwWebApplication" >
</jsp:useBean>


<%
	if(request.getSession()!=null && request.getSession().isNew()) {
	    response.setStatus(500);
	    response.getWriter().write("owsessiontimeout");
	    response.getWriter().close();
	} else {
		request.setCharacterEncoding("UTF-8");
		
	    theApp.setContextClass(com.wewebu.ow.server.app.OwMainAppContext.class);
	    theApp.setViewClass(com.wewebu.ow.server.app.OwMainLayout.class);
	    theApp.setLoginClass(com.wewebu.ow.server.app.OwLoginView.class);
		
	    theApp.handleAjaxRequest(application, request, response);
	    
	}
%>
