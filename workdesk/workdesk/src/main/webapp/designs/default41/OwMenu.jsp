<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
    import="com.wewebu.ow.server.ui.OwView,
        com.wewebu.ow.server.ui.OwMenu" 
%><%
    // get a reference to the calling view
    OwMenu m_View = (OwMenu)request.getAttribute(OwView.CURRENT_MODULE_KEY);

%><script type="text/javascript">
function navigateToMenu()
{
  var element = document.activeElement;
  var shouldExecute = element==null;
  if (!shouldExecute)
  {
   nodeName = element.nodeName.toLowerCase();
   if (nodeName=="input")
   {
     var type = element.type;
     if (type!=null) 
     {
       type = type.toLowerCase();
       shouldExecute = type=="text" || type=="password";
     }
     else
     {
       if (!(nodeName=='a'||nodeName=='button'))
       {
         shouldExecute = true;
       }
     }
   }
   if (shouldExecute)
   {
<%
      String menuURL = m_View.getMenuEventURL();
      if (menuURL.startsWith("javascript:"))
      {
          out.write(menuURL);
          out.write("\n");
      }
      else
      {
          out.write("navigateHREF(window,\"");
          out.write(menuURL);
          out.write( "\");");
      }
%>
    }
  }
  return !shouldExecute;
}
</script>