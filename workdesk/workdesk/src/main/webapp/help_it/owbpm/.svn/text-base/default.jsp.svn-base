<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
	Help for BPM
</span>

<p class="OwHelpIntro">
	<br>With "P8 BPM" (for Case Management with IBM FileNet P8 BPM) respectively "Alfresco BPM" (for Case Manaegment with Alfresco Workflow) you can comfortably process work items respectively tasks. According to your role you
	can access different inboxes (only available for FileNet P8 BPM) and views as well as functions for work item/task processing.  
</p>
<br><br>


<span class="OwHelpTopic2">
    Inboxes and Views
</span>

<p class="OwHelpText">	
	<br>In the navigation bar (often at the left side) you see those inboxes and views
	you can select by mouse click. In most cases, a personal inbox, several group inboxes
	and overviews are available. <br><br>
</p>


<p class="OwHelpText">
	<ul>
		<li><b>Inbox</b><br>
		Your personal inbox provides all work items/tasks that you have to process personally. 
		<br><img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/bpm_navigation.png"  alt="" title=""/>
		</li><br>
		<li><b>Group Inboxes [Only available in IBM  FileNet P8 BPM]</b><br>
		According to your role, you can access certain group inboxes. There, you find 
		those work items that can be processed by you and several other persons. </li><br>
		<li><b>Overview [Only available in IBM FileNet P8 BPM]</b><br>
		Overviews show you a section through different inboxes - for example all work 
		items with top priority	or just those work items concerning a certain clientele. </li><br>
	</ul>
</p>

<p class="OwHelpText">	
	<br>Optionally, the following views are displayed:<br><br>
</p>
		
<p class="OwHelpText">
	<ul>		
		<li><b>Proxy [Only available in IBM FileNet P8 BPM]</b><br>
		In this view you find all work items from the personal inboxes of your 
		colleagues who set you as a proxy. </li><br>
		<li><b>Tracker [Only available in IBM FileNet P8 BPM]</b><br>
		If the administrator appointed you as tracker, you find all work items here that 
		you should track and analyze. </li><br>
	</ul>
</p>

<p class="OwHelpText">	
	<br>In all inboxes and views, you can find the resubmission view and filter functions 
	above the list of work items respectively tasks.<br><br>
</p>
		
<p class="OwHelpText">
	<ul>			
		
		<li><b>Resubmission</b><br>
		Work items/tasks that have to be processed later on can be resubmitted. Then, they are not
		longer displayed in the respective inbox/view until the resubmission date. To view and edit resubmitted 
		work items/tasks select "resubmission view". To get back to the current inbox just undo this selection. </li><br>
	    <li><b>Set Filters</b><br>
	    You can limit the views additionally, when you choose own filter criteria.  Click on 
	    the funnel symbol next to the respective column header and define the desired filter 
	    criteria. When you mark the check box the view is filtered. When you undo this marking
	    the view appears unfilterd again.</li>
	</ul>
</p>

<p class="OwHelpText">
	<br><br><img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/wv_filter.png"  alt="" title=""  style="vertical-align: middle;"/><br>
</p>

<p class="OwHelpText">	
	<br><br>Work items/tasks are displayed in lists in all inboxes and views. Depending on 
	their processing priority they can be marked with different colors . A colored mark 
	and/or a priority flag helps you to catch important work items/tasks faster.<br>
	<br>
	You can sort work item/tasks lists by clicking a column header. Next to that, you can 
	modify the column width and order in AJAX lists as known from Windows Explorer. 
</p>

<br><br><br>


<span class="OwHelpTopic2">
    Work Item/Task Processing<br><br>
    <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/wv_filter_context.png" alt="" title=""/>
</span>

<p class="OwHelpText">
    <br>Select one or several work items/tasks to process them. Selected work items are locked 
    for other users till you unlock them, log out or change the view (Only available in FileNet P8 BPM). Locked work items 
    are marked with the symbol 
    <img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_en/images/locked.png"
     alt="<%=m_View.getContext().localize("help.image.locked16.gif","Locked image")%>" 
     title="<%=m_View.getContext().localize("help.image.locked16.gif","Locked image")%>" 
    />.
    <br><br>
    To process work items/tasks, you can use the following basic functions:
    <ul>
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/jspprocessor.png" alt="" title=""/> / <img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/standardprocessor.png" alt="" title=""/>  Process step</li>
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/returntosource.png" alt="" title=""/>  Return</li>
		<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/reassign.png" alt="" title=""/>  Forward</li>
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/note.png" alt="" title=""/>  Insert note</li>				
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/resubmit.png" alt="" title=""/>  Resubmission</li>
	</ul>
	Next to that, you can view a work item's audit trail or search for certain items in the audit trail 
	by clicking 
	<img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_de/images/history.png" alt="" title=""/> (View history) 
	and <img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_en/images/history_search.png" alt="" title=""/> (Search history) [Only available for IBM FileNet P8 BPM]. 
	If you have questions concerning other available functions, please contact you administrator. 
	<br><br>
	To use a function on one or several work items/tasks you can either click the respective icon next to the 
	work item/task title/description or choose it from a context menu.  
	<br><br>        
</p>

<span class="OwHelpTopic3">
	Process Step
</span>

<p class="OwHelpText">
    When you selected "Process step" the marked work items/tasks open in a process form. There you can insert and 
    check data and complete the process step. There is some advice for each step to help you. Please find 
    further information on the help page "Process step". <br>
    The menu bar provides the same symbols for processing the next work item/task. Using this, the work item/task to 
    be processed next will automatically be opened. 
    <br><br>
</p>

<span class="OwHelpTopic3">
	Return
</span>

<p class="OwHelpText">
    If you forwarded work items from a group inbox to your personal one you can undo this 
    step using "Return". <br><br>
</p> 


<span class="OwHelpTopic3">
	Forward
</span>

<p class="OwHelpText">
    Using "Forward" you can move work items/tasks to other group inboxes (only available in IBM FileNet P8 BPM), other user inboxes or your personal one. In the dialog 
    you choose the desired inbox and click "OK". All selected work items/tasks are then forwarded. <br><br>
</p>


<span class="OwHelpTopic3">
	Resubmission
</span>

<p class="OwHelpText">
    You can resubmit work items/tasks on a certain date. These are then not longer displayed in the 
    respective inbox. To ease later processing, a note can be added. <br>
	You can view and edit resubmitted work items/tasks in resubmission view. If you set the resubmission date 
	on today, the work items/tasks will appear in the respective inbox again. <br><br>
</p>


<span class="OwHelpTopic3">
	Insert note
</span>

<p class="OwHelpText">
    One or several notes can be added to every work item/task, e.g. for giving your colleagues 
    some important processing advice. Old notes are still displayed in the audit trail (only available in IBM FileNet P8 BPM). 
    Notes can also be displayed as column in certain inboxes and views.<br><br>
</p>