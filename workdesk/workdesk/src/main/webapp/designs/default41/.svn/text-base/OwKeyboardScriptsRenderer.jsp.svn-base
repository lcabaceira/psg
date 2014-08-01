<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
  import="com.wewebu.ow.server.ui.OwAppContext,
  com.wewebu.ow.server.ui.OwKeyEvent,
  com.wewebu.ow.server.ui.OwWebApplication,
  java.util.Collection,
  java.util.Iterator"
%><% 
// get a reference to the context
	OwAppContext ctx = OwWebApplication.getContext(session);
	//ctx.renderKeyBoardScript(out);
	Collection keyEvents = ctx.getKeyEvents();

if (keyEvents != null)
{
%><script type="text/javascript">
window.document.onkeydown = onOwKeyEvent;
function onOwKeyEvent(e)
{<%-- // begin function --%>
 if ( ! e ) e = event;
 var iMaskedCode =  e.keyCode + (e.altKey ? <%=Integer.toString(OwAppContext.KEYBOARD_CTRLKEY_ALT)%> : 0) <% 
%>  + (e.ctrlKey ? <%=Integer.toString(OwAppContext.KEYBOARD_CTRLKEY_CTRL)%> : 0) <% 
%>  + (e.metaKey ? <%=Integer.toString(OwAppContext.KEYBOARD_CTRLKEY_META)%> : 0) <% 
%> + (e.shiftKey ? <%=Integer.toString(OwAppContext.KEYBOARD_CTRLKEY_SHIFT)%> : 0);
 var retVal = null;
 switch ( iMaskedCode )
 {
<%
  Iterator it = keyEvents.iterator();
  while (it.hasNext())
  {
      OwKeyEvent keyevent = (OwKeyEvent) it.next();

      out.write("case ");
      out.write(Integer.toString(keyevent.getMaskedCode()));
      out.write(":\n{\n"); // begin case

      // === trigger to registered URL 
      if (keyevent.getEventURL() != null && keyevent.getEventURL().startsWith(OwAppContext.FULLJS_MARKER))
      {
          String js = keyevent.getEventURL().substring(OwAppContext.FULLJS_MARKER.length());
          out.write(createJavaScript(js));
      }
      else
      {
          if (keyevent.getFormName() != null)
          {
              out.write("document.");
              out.write(keyevent.getFormName());
              out.write(".action=\"");
              out.write(keyevent.getEventURL());
              out.write("\";\ndocument.");
              out.write(keyevent.getFormName());
              out.write(".submit();\n");
              // event was handled, so return false
//              out.write("retVal = false;\n");
          }
          else
          {
              if (keyevent.getEventURL().startsWith(JAVA_SCRIPT))
              {
                  out.write(createJavaScript(keyevent.getEventURL()));
              }
              else
              {
                  out.write("navigateHREF(window,\"");
                  out.write(keyevent.getEventURL());
                  out.write("\");\n");
		          // event was handled, so return false
              }
          }
          out.write("if (retVal == null)");
          out.write("retVal = false;");
      }
      // end case
      out.write("\n}break;\n");
  }
%>
 }<%-- // end switch
 // event was not handled, so return true --%>
 if (retVal != null && retVal === false)
 {
	return false;
 }
 else
 {
    return true;
 }
}<%-- // end function --%>
</script>
<%}%><%!
public static String JAVA_SCRIPT = "javascript:";
public String createJavaScript(String eventUrl)
{
    StringBuilder script = new StringBuilder("retVal = ");
    if (eventUrl.startsWith(JAVA_SCRIPT))
    {
        script.append(eventUrl.substring(JAVA_SCRIPT.length()));
    }
    else
    {
        script.append(eventUrl);
    }
    if (!eventUrl.endsWith(";"))
    {
        script.append(";");
    }
    return script.toString();
}
%>