<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
    Help for "Edit Properties"
</span>

<p class="OwHelpIntro">
    <br><br>At "Edit Properties" you can view and change documents' properties. Next to that you can view the access rights and the documents' history, and find
    out which versions exist in which files. <br>
    In parallel, the documents are displayed in a viewer next to "Edit Properties". When you edit the properties of several documents at once you can 
    switch between them using the arrow symbols. The viewer display switches, too. <br><br>
</p>


<span class="OwHelpTopic2">
    Functions
</span>

<br>
<br>
<p class="OwHelpText">
	 <!-- img src='<%=m_View.getContext().getBaseURL()%>/help_en/images/documentFunctions.png'><br><br -->
	At "Edit properties", a complex version management for documents is available:
	<br>
     <ul type="square" class="OwHelpText">
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/checkout.png" alt="" title=""/> 
        Checkout: Locks the documents for editing. Thus, only you can access the document and e.g.
        download and edit it.
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/cancelcheckout.png" alt="" title=""/> 
        Cancel Checkout: Unlocks an checked out document without saving changes.  
   	 	</li>
     	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/demoteversion.png" alt="" title=""/> 
        Demote Version: Demotes the current major version to the last possible minor version, e.g. 
        from 2.0 to 1.1. 
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/promoteversion.png" alt="" title=""/> 
        Promote Version: Promotes the latest checked in minor version to the next possible major version, e.g.
        from 3.1 to 4.0 (only possible, if it was demoted first).
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/save.png" alt="" title=""/> 
        Save: Save a document as a new minor version to the system. Anyway, the 
        document will be locked until you check it in.
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/checkin.png" alt="" title=""/> 
        CheckIn: Here you can save a document as a new major version to the system (<%=((OwInfoView)m_View).getLink("owadddocument/default.jsp","how to do ...","OwHelpLink")%>).
    	</li>
	</ul>	
</p>
<br>

<span class="OwHelpTopic2">
    Views  
</span>
<br>
<br>

<span class="OwHelpTopic3">
	Properties
</span>

<p class="OwHelpText">	
    Here you can modify the properties of a document. Just fill in the new values in the according fields.
    Then click "Save" to keep the changes. <br>
    Note: Fields marked with an asterisk are mandatory fields and have to be filled in.
    Automatically set properties like Date Created or Customer Number can not be modified, so those fields are inactive. <br>
    In long choice lists (drop down selection boxes), you can quickly select the wanted property by inserting its 
    initial letters. The cursor skips to the next fitting entry and the choice list shortens. 
    This function is available for all choice lists in Wordesk.   
    <br><br>
</p>


<span class="OwHelpTopic3">
	System Properties
</span>

<p class="OwHelpText">
    System properties are information about documents and files which are created automatically by the system.
    You can only view but not modify them.   <br><br>
</p>

<span class="OwHelpTopic3">
	Access Rights (optional)
</span>

<p class="OwHelpText">
     ... lists which rights of access different users have on the document. 
     Only the administrator can give or explicitly deny rights of access.
     If you do not possess the rights you need, please contact your administrator.<br><br>
</p>

<span class="OwHelpTopic3">
	Files 
</span>

<p class="OwHelpText">
    The view "Files" shows a list of all eFiles (and folders) which contain the current document.
    Clicking on a link opens the selected eFile/folder and the subfolder in which the document is contained.<br><br>
</p>

<span class="OwHelpTopic3">
	Versions
</span>

<p class="OwHelpText">
     Here you can find all available versions of the document and manage them.
     If you click on a certain version only its properties will be displayed.<br><br>
</p>



<span class="OwHelpTopic3">
	Audit Trail 
</span>

<p class="OwHelpText">
     This view shows the audit trail of the document. You can sort the entries by clicking on the respective column header like in all
     lists in Wordesk. The administrator defines which process steps are saved at the audit trail.
</p>

<br>

