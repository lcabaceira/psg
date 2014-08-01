<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Help for Search for eFiles
</span>

<p class="OwHelpText">
	   
    In "Search for eFiles" you see documents and data that have at least one property in common (e.g. customer number, document class, priority, date of modification, 
    ...). These properties are combined to search criteria and displayed as a folder in a tree structure. The only difference compared to a usual file structure: 
    The folders do not physically exist in the ECM system but virtually. 
    <br><br> 
</p>

<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        Virtual eFile and Folder View<br>   
    </span>
</a>

<p class="OwHelpText">
     
    In "Search for eFiles" you can see eFiles/folders just like in "eFile Management" or "Browse". But these folders do not physically exist in the ECM system,
    they are just of a combination of diverse search criteria. For example:
    The highest folder (root folder) shows all documents having a certain customer number. Its subfolder could show all documents having this certain customer number and 
    a specific document class. Another level below that, documents with the certain customer number and the specific document class are shown which have been checked in into 
    the ECM system in a certain year.  <br> 
    Just like in "eFile Management" or "Browse" you click the plus-minus symbol next to a eFile/folder to show or hide 
    its subfolders. In order to view a (sub-)folder's content click its name. The displayed folder is marked. 
    <br><br>
	The folder view shows the content of the selected folder. Per mouse-click, you can open documents in a viewer or if applicable in a program for 
	editing respectively. Next to that, like in "eFile Management" several document functions and list types are available. For a detailed description please
	have a look at help page <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","eFile Management","OwHelpLink")%>. 
	However, as a result of the virtual eFile structure there are some differences with the following functions:
	<br clear="all">
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Create new eFile/folder & <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Create new subfolder
</span>
<p class="OwHelpText">
    As no actual eFiles and folders exist you cannot create new eFiles or folders in "Search for eFiles". 
    In case you should need a specific combination of search criteria again and again and want to display them in a virutal eFile oder folder please contact your 
    administrator.  <br>
</p>


<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/paste.png" alt="" title=""/> 
	Paste Documents from Clipboard & <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/move.png" alt="" title=""/> 
	Move Documents from Clipboard
</span>
<p class="OwHelpText">
    Though you can copy documents to clipboard in "Search for eFiles" you cannot paste or move them to a virtual eFile or folder. Virtual eFiles/folders show documents having 
    a certain property. In order to move a document to another virtual eFile/folder you must change its properties (<%=((OwInfoView)m_View).getLink("owdocprops/default.jsp","Edit Properties","OwHelpLink")%>) 
    and make them fit to this virtual eFile/folder. Afterwards, the document will automatically be displayed in the matching virtual eFile/folder. 
    <br>
</p>


<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        <br><br>Fine-tuning the View<br>   
    </span>
</a>

<p class="OwHelpText">
    The wanted object does not appear among the results? Then fine-tune your view. Insert further search criteria above the eFile view and click "Search". <br>
    In order to accelerate searching, you can define the "Maximum number of results" in the search templates.  
    <br><br clear="all"><br><br>
</p>
