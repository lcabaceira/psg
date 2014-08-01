<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
    String header = (String)session.getAttribute(com.wewebu.ow.server.ui.OwWebApplication.SESSION_KEY_LOGGEDOUT_HEADER);
    String message = (String)session.getAttribute(com.wewebu.ow.server.ui.OwWebApplication.SESSION_KEY_LOGGEDOUT_MESSAGE);
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
    <link rel='stylesheet' href='designs/default41/css/ow.css' type='text/css'>
    <title>Alfresco Workdesk</title>
</head>

<body id='owbody' class='owBody default'>

<div id='OwMainLayout'>

        <div id='OwMainLayout_HEADER'>
            
            <div id='OwHeaderView_Logo'>
            <img src='designs/default41/images/OwGuiLayout/OW_Logo1.png' id='OwHeaderView_Logo1' alt='Workdesk Logo' title='Workdesk Logo' >
            <!-- 
                <img src='designs/default41/images/OwGuiLayout/OW_Logo2.gif' id='OwHeaderView_Logo2' alt='Alfresco Software, Inc.' title='Alfresco Software, Inc.'>
                <img src='designs/default41/images/OwGuiLayout/brand_logo.png' id='OwHeaderView_Logo3' alt='Alfresco Software, Inc. Logo' title='Alfresco Software, Inc. Logo' >
            -->
            </div>
            
        </div>

        <div id='OwMainLayout_NAVIGATION'>
                <div class='OwMainLayout_edge OwMainLayout_topLeft'><!-- --></div>
                <div class='OwMainLayout_edge OwMainLayout_topRight'><!-- --></div>
                &nbsp;

        </div>

<div id='OwLoginView'>

    <h1 id='OwLoginView_title'>
    <%=header%>
    </h1>


    <div id='OwLoginView_loginpart'>
    <%=message%>
    </div>


    

</div>

        <div id='OwMainLayout_MAIN_endstrip'>
            <span class='OwMainLayout_edge OwMainLayout_bottomLeft'><!-- --></span>
            <span class='OwMainLayout_edge OwMainLayout_bottomRight'><!-- --></span>
        </div>

<script type='text/javascript'>

                function OwOnDocumentLoad()
                {
                    window.setTimeout(function(){document.body.focus();},500);
                        
                }
</script>
        
</body>
</html>