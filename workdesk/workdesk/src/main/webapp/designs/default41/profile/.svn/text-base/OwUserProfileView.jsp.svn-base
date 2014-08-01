<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java" autoFlush="true"  
    import="com.wewebu.ow.server.ui.OwAppContext,
            com.wewebu.ow.server.ui.OwView,
            org.alfresco.wd.ui.profile.OwUserProfileView" 
%><%
   OwUserProfileView view = (OwUserProfileView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<div class="OwPane profile">
    <%String[][] data = view.getData();
      for (int i = 0; i < data.length; i++)
      {
          String label = data[i][0];
          String value = data[i][1] == null ? "" : data[i][1];
%>     <div class="row <%= i%2 == 0 ? "even" : "odd"%>"><span class="label"><%=label%></span><span class="value"><%=value%></span></div>
<%    }%>
</div>
