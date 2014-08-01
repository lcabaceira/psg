<%@ page
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.plug.owrecord.*"
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
%><%
    // get a reference to the calling view
    OwLayout m_View = (OwLayout)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
        <div class="OwSearchCriteriaView_Container">
<%       m_View.renderRegion(out,OwRecordSearchTemplateView.SEARCH_CRITERIA_REGION);%>
        </div>

<%--         used to simulate a submit button in order to handle return key events --%>
        <input style="position:absolute;visibility:hidden;" type="image" alt="Enter">

        <div id="OwRecordSearchTemplateView_Control">
 <%
        boolean enableMaxResultsLimit = ((OwRecordSearchTemplateView) m_View).isMaxResultEnabled();
        if (enableMaxResultsLimit)
        {
 %>
         <p class="OwMaxResultListSize">
            <label for="<%=OwRecordSearchTemplateView.MAX_RESULT_LIST_KEY%>" class="OwPropertyName"><%= m_View.getContext().localize("owrecord.OwRecordSearchTemplateView.maxresultlistsize","Maximal Number of Results")%>:</label>
<%          m_View.renderRegion(out,OwRecordSearchTemplateView.MAX_SIZE_REGION); %>
         </p>
    <%
        }
        else
        {
    %>
    <label class="OwPropertyName"></label>
    <%
        }
         m_View.renderRegion(out,OwRecordSearchTemplateView.MENU_REGION);
     %>
        </div>

        <div class="break"><!-- --></div>
<%-- search tree dump
<div style='background: #000000;border: solid 1pt black;font-family: Arial, Helvetica, sans-serif;font-size: 10 pt;'>
<b>SEARCHTREE-DUMP:</b>
<br>
<span style='font-size: 8pt'>
<% m_View.renderRegion(out,OwRecordSearchTemplateView.DEBUG_SEARCH_DUMP_REGION); %>
</span>
</div>
--%>