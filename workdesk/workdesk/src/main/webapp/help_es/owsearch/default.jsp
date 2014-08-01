<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<span class=OwHelpTopic1>
	Help for Search
</span>

<p class="OwHelpText">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/search.png"  style="vertical-align: middle;" alt="" title=""/><br><br>
	In "Search" you can search for documents and eFiles/folders. 
    Several search templates can be made available:
    eFile/folder search, document search and possibly several user specific search templates.<br>     
	<br>
    You can select the desired search template from the navigation bar (mostly left). 
    Then the corresponding search template providing entry fields for your search criteria opens on the right. 
  <br><br><br>
</p>

<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        Search Criteria<br>   
    </span>
</a>

<p class="OwHelpText">
    Depending on the search template, you can search for different criteria (e.g. name, customer number, date last modified, etc.).      
	It is also possible to use wildcards to replace one or several characters. 
	Your administrator will tell you which symbols (e.g.: *, ?, %, ...) you can use as wildcards. 
	<br><br>
    Enter your search criteria into the search template. If you want to delet entered search criteria click "Reset".  
	<br><br>
    When you click "Search" the result list with documents or eFiles/folders matching your 
    criteria will appear. If you did not enter any search criteria, all existing 
    eFiles/folders and documents are listed.
    <br clear="all"><br><br>
</p>

<a name="#DocListViewModule">
    <span class=OwHelpTopic2>
        Result List
    </span>
</a>

<p class="OwHelpText">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/resultlist.png"  style="vertical-align: middle;" alt="" title=""/><br><br>

    The result list shows all eFiles/folders and documents which match your search criteria. 
    You can sort the result list ascending or descending by clicking the column header 
    (e.g. name, customer number, etc.). <br><br>
    
    If you click on a document title in the result list it is opened in the viewer. 
    Similar as in "eFile Management" you can also edit documents in the result list. 
    It depends on your user-role which functions are already available here. To learn 
    more about single functions, please have a look at the help for 
    
    <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","eFile Management","OwHelpLink")%>.
    
    <br><br>
    If you click on an eFile in the result list it is opened in "eFile Management". 
    Returning to "Search" you will find the latest result list again. Clicking the previously used 
    search template, your entries are shown as initial values.<br><br>    
</p>

<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        <br>Tips for Searching<br>   
    </span>
</a>

<p class="OwHelpText">
    To accelerate searching, you can define the "Maximum number of results" in the search templates. 
    If the objects you searched for do not appear in the result list, you can fine-tune your search. 
    <br><br> 
    Additionally, you have the possibility to save often needed searches. To do so, you enter a name
    for the new search above the result list and click "Save search". You can select this search from a 
    check box in the search template afterwards. <br clear="all"><br><br>
</p>
