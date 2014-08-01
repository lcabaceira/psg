<%@page import="com.wewebu.ow.server.app.OwAboutMessageHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true"  
    import="com.wewebu.ow.server.util.OwString,
            com.wewebu.ow.server.ecm.OwNetworkContext,
            com.wewebu.ow.server.app.OwMainLayout,
            com.wewebu.ow.server.ui.OwAppContext,
            com.wewebu.ow.server.app.OwMainAppContext" 
 %><%
    // get a reference to the calling view
    OwLayout m_View = (OwLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);
    OwAppContext context = m_View.getContext();
%>
   <div id="OwMainLayout">

   <div class="accessibility">
<%   if ( m_View.isRegion(OwMainLayout.MESSAGE_REGION) ){%>
        <a href="#OwMainLayout_MESSAGES"><%=context.localize("jsp.OwMainLayout.accessibility.MESSAGE_REGION.skip", "Skip to the message block for information.")%></a>
        <br/>
<%   }
     if (!m_View.isRegion(OwMainLayout.MAXIMIZED_REGION) ){%>
        <a href="#OwMainLayout_HEADER"><%=context.localize("jsp.OwMainLayout.accessibility.OwMainLayout_HEADER.skip", "Skip to user information and logout button.")%></a>
        <br/>
<%   }
     if ( m_View.isRegion(OwMainLayout.ERROR_REGION) ){%>
        <a href="#OwMainLayout_ERROR"><%=context.localize("jsp.OwMainLayout.accessibility.OwMainLayout.ERROR_REGION.skip", "Skip to the information about errors.")%></a>
        <br/>
<%   }
     if ( m_View.isRegion(OwMainLayout.CLIPBOARD_REGION) && (! m_View.isRegion(OwMainLayout.MAXIMIZED_REGION)) )
     {
%>        <a href="#OwMainLayout_clipboard"><%=context.localize("jsp.OwMainLayout.accessibility.OwMainLayout.CLIPBOARD_REGION.skip", "Skip to the clipboard.")%></a>
        <br/>
<%   }
     if ( m_View.isRegion(OwMainLayout.DIALOG_REGION) )
     {
        if (! m_View.isRegion(OwMainLayout.MAXIMIZED_REGION) )
        {
%>        <a href="#OwMainLayout_NAVIGATION"><%=context.localize("jsp.OwMainLayout.accessibility.NAVIGATION.skip", "Skip to the main navigation bar.")%></a>
        <br/>
<%
        }
%>      <a href="#OwMainLayout_DIALOG"><%=context.localize("jsp.OwMainLayout.accessibility.DIALOG_REGION.skip", "Skip to the currently opened dialog.")%></a>
        <br/>
<%
     }
     else
     {
        if ( m_View.isRegion(OwMainLayout.MAXIMIZED_REGION) )
        {
%>           <a href="#OwMaximizedLayout_MAIN"><%=context.localize1("jsp.OwMainLayout.accessibility.MAXIMIZED_REGION.skip", "Skip to the maximized view of %1.", m_View.getTitle())%></a>
           <br/>
<%      }
        else
        {
%>           <a href="#OwMainLayout_NAVIGATION"><%=context.localize("jsp.OwMainLayout.accessibility.NAVIGATION.skip", "Skip to the main navigation bar.")%></a>
           <br/>

           <a href="#OwMainLayout_MAIN"><%=context.localize1("jsp.OwMainLayout.accessibility.MAIN_REGION.skip", "Skip to the content of the current selected Masterplugin %1.", m_View.getTitle())%></a>
           <br/>
<%
        }
     } 
%>        <a href="#OwMainFooter"><%=context.localize("jsp.OwMainLayout.accessibility.FOOTER_REGION.skip", "Skip to footer.")%></a>
        <br/>
        <a href="#owkeyinfo"><%=context.localize("jsp.OwMainLayout.accessibility.owkeyinfo.skip", "Skip to accessibility key information.")%></a>
   </div><%--

    //--------------------------------------------------------------------------
    // BEGINN OF MESSAGE-BLOCK
    //--------------------------------------------------------------------------
--%><div id="OwMainLayout_MESSAGE_container"><jsp:include page="OwMainLayoutMessage.jsp" flush="true"/></div><%
    //--------------------------------------------------------------------------
    // END OF MESSAGE-BLOCK
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // BEGINN OF HEAD
    //--------------------------------------------------------------------------
    if ( ! m_View.isRegion(OwMainLayout.MAXIMIZED_REGION) )
    {
%>
      <div id="OwMainLayout_HEADER">
         <h1 class="accessibility"><%=context.localize("jsp.OwMainLayout.accessibility.OwMainLayout_HEADER", "User information and logout button")%></h1>
         <div id="OwHeaderView_Logo">
         <a href="#" onclick="showAbout(<%=OwAboutMessageHelper.getAboutInfoAsJs(context)%>,'<%=OwAboutMessageHelper.getCopyright(context)%>'); return false;">
             <img src="<%=context.getDesignURL()%>/images/OwGuiLayout/<%=context.localize("image.workdesklogo","OW_Logo1.png")%>" id="OwHeaderView_Logo1" alt="<%=context.localize("image.ow_logo1.gif","Workdesk Logo")%>" title="<%=context.localize("image.ow_logo1.gif","Workdesk Logo")%>">
           </a>
            <%--
                <img src="<%=m_View.getContext().getDesignURL()%>/images/OwGuiLayout/OW_Logo2.gif" id="OwHeaderView_Logo2" alt="<%=m_View.getContext().localize("image.ow_logo2.gif","Alfresco Software, Inc.")%>" title="<%=m_View.getContext().localize("image.ow_logo2.gif","Alfresco Software, Inc.")%>">
                <img src="<%=m_View.getContext().getDesignURL()%>/images/OwGuiLayout/brand_logo.png" id="OwHeaderView_Logo3" alt="<%=m_View.getContext().localize("image.brand_logo.gif","Alfresco Software, Inc. Logo")%>" title="<%=m_View.getContext().localize("image.brand_logo.gif","Alfresco Software, Inc. Logo")%>">
            --%>
          <div id="OwHeaderView_Login">
           <form name="<%=m_View.getFormName()%>" method="post" action="">
               <div id="ROLE_SELECT_REGION" style="float:left">
<%                  m_View.renderRegion(out,OwMainLayout.ROLE_SELECT_REGION); %>&nbsp;
               </div>
               <div id="MANDATOR_REGION" style="float:left">
<%              if ( m_View.isRegion(OwMainLayout.MANDATOR_REGION) )  
                { 
                    m_View.renderRegion(out,OwMainLayout.MANDATOR_REGION); 
                }
                %>&nbsp;</div>
               <div style="float:left"><%--m_View.renderRegion(out,OwMainLayout.LOGININFO_REGION);//LOGININFO_REGION will be deprecated--%>                  
                <span class="OwHeaderView_Info">           
<% 
               Object userDisplayName = (((OwNetworkContext)context).resolveLiteralPlaceholder(null, OwMainAppContext.LITERAL_PLACEHOLDER_USER_DISPLAY_NAME));
               String renderedName;
               if ( userDisplayName == null || userDisplayName.equals(""))
               {
                   renderedName = (((OwNetworkContext)context).resolveLiteralPlaceholder(null, OwMainAppContext.LITERAL_PLACEHOLDER_USER_NAME)).toString();;
               }
               else
               {
                   renderedName = userDisplayName.toString();
               }              
               %><a href="<%m_View.renderRegion(out, OwMainLayout.PROFILE_ACTION_REGION);%>" title="My Profile"><%=renderedName%></a>
               &nbsp;|&nbsp;
<%java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL, context.getLocale());
%><%=df.format(new java.util.Date())%>&nbsp;|&nbsp;<%m_View.renderRegion(out, OwMainLayout.LOGOUT_BUTTON_REGION);%>
                </span>
               </div>
           </form>
          </div>
         </div>
      </div>
<%
    }
    //--------------------------------------------------------------------------
    // END OF HEAD
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // BEGINN OF ERROR-BLOCK
    //--------------------------------------------------------------------------
%>
	<div id="OwMainLayout_ERROR_container">
<% 
    if ( m_View.isRegion(OwMainLayout.ERROR_REGION) )
    {
%>
<jsp:include page="OwMainLayoutError.jsp" flush="true"/>
<%
    }
%>
    </div>
<%

    //--------------------------------------------------------------------------
    // END OF ERROR-BLOCK
    //--------------------------------------------------------------------------

    //----------------------------------------------------------------------
    // BEGINN OF CLIPBOARD
    //----------------------------------------------------------------------
    if ( m_View.isRegion(OwMainLayout.CLIPBOARD_REGION) && (! m_View.isRegion(OwMainLayout.MAXIMIZED_REGION)) )
    {
%>
        <div id="OwMainLayout_clipboard">
        <h1 class="accessibility"><%=context.localize("jsp.OwMainLayout.accessibility.OwMainLayout.CLIPBOARD_REGION", "Region which shows the object which are currently in the clipboard.")%></h1>
             <% m_View.renderRegion(out,OwMainLayout.CLIPBOARD_REGION); %>
        </div>
<%
    }
    //----------------------------------------------------------------------
    // END OF CLIPBOARD
    //----------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // BEGINN OF MAIN BLOCK
    //--------------------------------------------------------------------------

    if ( m_View.isRegion(OwMainLayout.DIALOG_REGION) )
    {
        //----------------------------------------------------------------------
        // BEGINN OF DIALOG
        //----------------------------------------------------------------------
        if (! m_View.isRegion(OwMainLayout.MAXIMIZED_REGION) )
        {
            //------------------------------------------------------------------
            // BEGINN OF NON-MAXIMIZED
            //------------------------------------------------------------------
%>
			<div id="OwMainLayout_NAVIGATION">
			    <h1 class="accessibility"><%=context.localize("jsp.OwMainLayout.accessibility.NAVIGATION", "Main navigation bar")%></h1>
<%-- 				<span class="OwMainLayout_edge OwMainLayout_topLeft"><!-- --></span>
				<span class="OwMainLayout_edge OwMainLayout_topRight"><!-- --></span>
                --%><% m_View.renderRegion(out,OwMainLayout.NAVIGATION_REGION); %>
            </div>
<%
            //------------------------------------------------------------------
            // END OF NON-MAXIMIZED
            //------------------------------------------------------------------
        }
%>
        <div id="OwMainLayout_DIALOG">
        <h1 class="accessibility"><%=context.localize("jsp.OwMainLayout.accessibility.DIALOG_REGION", "Current opend dialog")%></h1>
            <% m_View.renderRegion(out,OwMainLayout.DIALOG_REGION); %>
        </div>
<%
        //----------------------------------------------------------------------
        // END OF DIALOG
        //----------------------------------------------------------------------
    }
    else
    {
        //----------------------------------------------------------------------
        // BEGINN OF NON-DIALOG
        //----------------------------------------------------------------------

        // is a view requesting to be displayed maximized
        if ( m_View.isRegion(OwMainLayout.MAXIMIZED_REGION) )
        {
            //------------------------------------------------------------------
            // BEGINN OF MAXIMIZED
            //------------------------------------------------------------------
%>
            <div id="OwMaximizedLayout_MAIN">
            <h1 class="accessibility"><%=context.localize1("jsp.OwMainLayout.accessibility.MAXIMIZED_REGION", "Maximized view of %1", m_View.getTitle())%></h1>
<%              m_View.renderRegion(out, OwMainLayout.MAXIMIZED_REGION); %>
            </div>
<%
            //------------------------------------------------------------------
            // END OF MAXIMIZED
            //------------------------------------------------------------------
        }
        else
        {
            //------------------------------------------------------------------
            // BEGINN OF NON-MAXIMIZED
            //------------------------------------------------------------------
%>			<div id="OwMainLayout_NAVIGATION">
			    <h1 class="accessibility"><%=context.localize("jsp.OwMainLayout.accessibility.NAVIGATION", "Main navigation bar")%></h1>
<%-- 				<span class="OwMainLayout_edge OwMainLayout_topLeft"><!-- --></span>
				<span class="OwMainLayout_edge OwMainLayout_topRight"><!-- --></span>
                --%><% m_View.renderRegion(out, OwMainLayout.NAVIGATION_REGION); %>
            </div>
         <div id="OwMainLayout_MAIN">
         <h1 class="accessibility"><%=context.localize1("jsp.OwMainLayout.accessibility.MAIN_REGION", "Content of the current selected Masterplugin %1.", m_View.getTitle())%></h1>
            <% m_View.renderRegion(out, OwMainLayout.MAIN_REGION); %>
         </div>
 		<div id="OwMainLayout_MAIN_endstrip">
<%-- <span class="OwMainLayout_edge OwMainLayout_bottomLeft"><!-- --></span>
				<span class="OwMainLayout_edge OwMainLayout_bottomRight"><!-- --></span>
			--%></div><%
            //------------------------------------------------------------------
            // END OF NON-MAXIMIZED
            //------------------------------------------------------------------
        }
        //----------------------------------------------------------------------
        // END OF NON-DIALOG
        //----------------------------------------------------------------------
    }
%>
   </div>
   <div id="OwMainFooter">
      <!-- <p class="OwMainFooter_Deco"><!- - You can place a footer image here - -></p> -->
<% 
if (m_View.isRegion(OwMainLayout.KEYBOARD_REGION) && request.getHeader("user-agent") != null) { 

    context.registerKeyEvent(OwAppContext.KEYBOARD_KEY_F2,0,"javascript:toggleShortcutWindow();",context.localize("jsp.OwInfoLine.keyinfotoggle","Keyboard info"));
    String keyboard = context.localize("OwMainLayout.mouseover.keyboard","List of keyboard shortcuts");
%>
     <p class="OwMainFooter_Keyinfo">
        <a title="<%=keyboard%>" href="#"><img id="OwKeyInfoButton" alt="<%=keyboard%>" title="<%=keyboard%>" src="<%=context.getDesignURL()%>/images/OwMainLayout/keyboard.gif"></a>
     </p>
<% } %>
      <p class="OwCopyRightInfo"><%@ include file="../OwInfoLine.jsp" %></p>
</div>
<div id="<%=OwMainLayout.KEY_INFO_CONTAINER_ID%>" class="hidden">
    <jsp:include page="OwMainLayoutKeyboard.jsp" flush="true"/>
</div>