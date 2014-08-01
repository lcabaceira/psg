<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Help for eFile Management
</span>

<p class=OwHelpIntro>
	<br>Manage your eFiles comfortably in "eFile Management". In order to open a eFile click on its title - either in 
	a result list of a search, in an attachment of a work item or directly in "eFile Management" from the list of last opened eFiles. The last four eFiles you 
	have opened are saved there and appear mostly above the eFile view. This list is also available after a re-login.  
	<br><br>
	<br clear="all">
</p>

<a name="#RecordTreeViewModule">
    <span class=OwHelpTopic2>
        eFile and Folder View  
    </span>
</a>

<p class="OwHelpText">
    When you have selected an eFile, you see the eFile information like customer number, customer name, latest changes etc. usually 
    above the eFile structure.  <br>
    <br><img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/filefolderview.png" alt="" title=""/>
    <br><br>The eFile structure with its folders and subfolders is displayed as tree. Click on the plus-minus symbol next to a folder to show or hide 
    its subfolders. In order to view a (sub-)folder's content click its name. The displayed folder is marked. 
    <br><br>
	The folder view shows the content of the selected folder. Per mouse-click, you can open documents in a viewer or if applicable in a program for 
	editing respectively. Next to that, several document functions are available (see below).
	You can choose between several types of document lists to view a folder's content:  
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewRow.png" alt="" title=""/> 
	Simple Document List
</span>
<p class="OwHelpText">
    This list type lists documents with name and other document properties. The last opened document is marked (e.g. bold). Documents you have already edited 
    appear different (e.g. in italics) than other documents. Thus you can easily find lately used documents.    
    <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewThumbnail.png" alt="" title=""/> 
	Thumbnail List
</span>
<p class="OwHelpText">
    This list type shows thumbnails of the documents. You can select whether the thumbnails should be displayed in small, medium or big size. 
    <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewCombined.png" alt="" title=""/> 
	Document List with Thumbnails
</span>
<p class="OwHelpText">
    This list type combines both previously described types. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/OwObjectListViewEXTJSGrid.png" alt="" title=""/> 
	AJAX List
</span>
<p class="OwHelpText">
    In AJAX lists, documents are listed like in simple document lists. However, AJAX lists offer much more flexibility: 
    For example a thumbnail of a document is displayed when the cursor is stopped for a short time over a list item. <br> 
    Next to that, you can modify the column order and width. The column order is changed per drag & drop when clicking the column header, 
    dragging it to the wanted position and dropping it there. The column width can be easily modified by just moving the separator 
    to the wanted position while holding the left mouse button pressed. Double clicking	the separator automatically adjusts the column to its optimum. <br>
    In an AJAX list, you can directly edit the displayed metadata of documents. Just double-click in the respective field and then 
    change, complete or overwrite the previous entry. If special formatting is needed (e.g. for customer numbers), correct values are shown in a drop 
    down selection box. 
    There you can quickly select the wanted property by inserting its initial letters. This function is available for all choice lists in Wordesk.
    <br><br>
</p>
	
<p class="OwHelpText">	
	All lists except for the thumbnail list can be sorted ascending and descending by clicking the respective column header. <br clear="all"><br><br>
</p>

<a name="#RecordTreeFunctionModule">
    <p class=OwHelpTopic2>
        Functions for eFiles and Documents 
    </p>
</a>

<p class="OwHelpText">
	<br>According to your user role and the eFile structure, some of the following functions for editing eFiles and documents 
	are available. Some can be run directly, some provide a dialog. All dialogs can be cancelled or 
	closed when clicking on the closing symbol (usually top right or top left).    
	<br>  
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/edit_properties.png" alt="" title=""/> 
	Edit Properties
</span>
<p class="OwHelpText">
    ... opens a dialog in which you can view and edit the properties of the current eFile or the selected documents. 
    Please find further information at the help page <%=((OwInfoView)m_View).getLink("owdocprops/default.jsp","Edit Properties","OwHelpLink")%>. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/history.png" alt="" title=""/> 
	View History
</span>
<p class="OwHelpText">
    ... opens the audit trail of the current eFile/folder or the selected documents respectively. 
    You can sort the items just like documents ascending and descending by clicking the respective column header. 
    <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/history_search.png" alt="" title=""/> 
	Search History
</span>
<p class="OwHelpText">
    With this function, you can selectively search for audit trail items. Just insert the needed 
    search criteria and click "Search". Afterwards, only those items complying with the criteria are displayed. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/bookmark.png" alt="" title=""/> 
	Add to favorites
</span>
<p class="OwHelpText">
    ... saves a link to the current eFile/folder or the selected documents to your favorites. 
    Thus, in "<%=((OwInfoView)m_View).getLink("owshortcut/default.jsp","Favorites","OwHelpLink")%>" you can fast access often needed
    eFiles/folders and documents without needing to search for them. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/copy.png" alt="" title=""/> 
	Copy
</span>
<p class="OwHelpText">
    ... copies the current eFile/folder or the selected documents to the clipboard. From there, you can easily add them as 
    attachment to work items for example. When pasting a document from clipboard, the original document stays in the original folder. When 
    moving it, the document is moved from the original into the new folder.<br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/deletebtn.png" alt="" title=""/> 
	Delete
</span>
<p class="OwHelpText">
    If your user role allows it, you can irretrievably delete documents, folders and eFiles with this function. <br>
</p>



<br><br>

<a name="#RecordTreeFunctionModule">
    <p class=OwHelpTopic2>
        eFile Functions 
    </p>
</a>

<p class="OwHelpText">
	<br>Next to that, some of the following functions for editing eFiles/folders may be available:
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Create new eFile/folder
</span>
<p class="OwHelpText">
    ... opens a dialog for creating new eFiles/folders. Insert the name of the new eFile/folder and define its properties if needed. Then click "Save" and the new 
    eFile/folder is created. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Create new subfolder
</span>
<p class="OwHelpText">
    ... opens a dialog for creating a new subfolder in the current eFile or folder.
    Choose the eFile/folder class first and click "next". Then define the properties of the subfolder an click "save".  
    The new subfolder is created. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_document.png" alt="" title=""/> 
	Add document
</span>
<p class="OwHelpText">
    With this function you can add documents to the current folder. There are different possibilities to do so (drag & drop, copy & paste, upload). 
    Please find a detailed description <%=((OwInfoView)m_View).getLink("owadddocument/default.jsp","here","OwHelpLink")%>. 
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/paste.png" alt="" title=""/> 
	Paste Documents from Clipboard
</span>
<p class="OwHelpText">
    When you have copied documents from an eFile/folder, a result list or a work item to clipboard, you can paste them (as a copy) to the current 
    folder using this function. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/move.png" alt="" title=""/> 
	Move Document from Clipboard
</span>
<p class="OwHelpText">
    When you copied documents from an eFile/folder, a result list or a work item to clipboard, you can move them to the current folder 
    using this function. Afterwards they are not longer displayed in the original place - just like using cut and paste. <br>
</p>



<br><br>
<a name="#RecordTreeFunctionModule">
    <p class=OwHelpTopic2>
        Document Functions 
    </p>
</a>

<p class="OwHelpText">
	<br>Some of the following functions for editing documents may be available:
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/cut.png" alt="" title=""/> 
	Cut
</span>
<p class="OwHelpText">
    ... cuts the selected documents from their current folder and adds them to clipboard. 
    From there, you can easily move them to other folders or eFiles. If you do not paste them anywhere
    they are kept in the original folder. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/checkout.png" alt="" title=""/> 
	Check Out
</span>
<p class="OwHelpText">
     ... locks documents for editing. Thus, only you can access these documents and e.g. download and edit them. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/cancelcheckout.png" alt="" title=""/> 
	Cancel Check Out
</span>
<p class="OwHelpText">
    ... unlocks checked out documents without saving changes. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/download.png" alt="" title=""/> 
	Download
</span>
<p class="OwHelpText">
    ... saves documents for editing as local copy. To prevent that other users edit the same documents 
    at the same time, you should first check them out.   
    <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/save.png" alt="" title=""/> 
	Save
</span>
<p class="OwHelpText">
    ... saves a document as new minor version in the system. A dialog helps you to select and upload the desired 
    document. However, it stays checked out until you check it in again.  
    <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/checkin.png" alt="" title=""/> 
	Check In
</span>
<p class="OwHelpText">
    ... saves a document as new major or minor version in the system and unlocks it. A dialog helps you to 
    edit the document's properties, to select a document from your file system and upload it. The procedure is 
    similar to <%=((OwInfoView)m_View).getLink("owadddocument/default.jsp","adding a document","OwHelpLink")%>.  <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/demoteversion.png" alt="" title=""/> 
	Demote Version
</span>
<p class="OwHelpText">
    ... demotes the latest major version to the last possible minor version, e.g. from 2.0 to 1.1. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/promoteversion.png" alt="" title=""/> 
	Promote Version
</span>
<p class="OwHelpText">
    ... promotes the latest minor version to the next possible major version, e.g. from 3.1 to 4.0.  <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/remove.png" alt="" title=""/> 
	Remove
</span>
<p class="OwHelpText">
    ... removes the document from an eFile/folder without deleting it. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/docview.png" alt="" title=""/> 
	Compare Documents
</span>
<p class="OwHelpText">
    ... opens the selected documents in the viewer in tile windows. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_note.png" alt="" title=""/> 
	Insert Note
</span>
<p class="OwHelpText">
     ... opens a dialog where you can insert notes - e.g. advices for editing to your colleagues. Existing notes 
     are not overwritten but persist in the audit trail. Clicking "Save" the note is saved. <br>
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/sendlink.png" alt="" title=""/> 
	Email-Link
</span>
<p class="OwHelpText">
    ... opens a new e-mail containing links to the selected documents in you default e-mail program. <br>
</p>




<br>