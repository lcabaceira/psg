<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" 
    import="com.wewebu.ow.server.ui.button.OwImageButtonView,
            com.wewebu.ow.server.ui.button.OwImageButton,
			com.wewebu.ow.server.ui.OwView"
    
    autoFlush="true"
%><% // get a reference to the calling view
     OwImageButtonView view = (OwImageButtonView) request.getAttribute(OwView.CURRENT_MODULE_KEY);

%><p<%
  if (view.getHtmlId() != null)
  {
      %> id="<%=view.getHtmlId()%>"<%
  }
%> class="OwButtonControl<%
  for (String clazz: view.getDesignClasses())
  {
      out.write(" ");
      out.write(clazz);
  }
%>"><%

     for (OwImageButton button : view.getButtonList())
     {
%>
         <a<%
         if (button.getDesignClass() != null)
         {
             %> class="<%=button.getDesignClass()%>"<%
         }

         if (button.getTooltip() != null)
         {
            %> title="<%=button.getTooltip()%>"<% 
         }

         if (button.getEventString() != null)
         {
             %> href="<%= button.getEventString()%>" <%
         }
         %>>
             <span class="OwImageButtonView_icon" style="background-image: url(<%= button.getImageLink() == null ? "" : button.getImageLink()%>)"<%
              if (button.getTooltip() != null)
              {
                  %> title="<%=button.getTooltip()%>"<%
              }
            %>>
            </span>
            <span class="OwImageButtonView_text">
              <%if (button.getTooltip() != null)
              {
              %>  
                  <%=button.getTooltip()%><br/>
              <%
              }
              %>
            </span>
         </a>
<%
     }
%>
</p>