<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
	Help for "Execute Step"
</span>

<p class="OwHelpIntro">
	<br>When you selected work items/tasks in "BPM" repectively "Case Management" and clicked "Process step" 
	these work items/tasks are opened in a processing form. 
	Most processing forms provide work item/task properties and their values. Values 
	that can or should be changed by you appear in check boxes or as input field. If you selected several work items/tasks at once you can switch between them 
	using the arrow symbols.<br>
</p>
<br>

<p class="OwHelpText">
    <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/processstep.png" alt="" title=""/>  
</p>

<span class="OwHelpTopic2">
   <br>Attachments
</span>

<p class="OwHelpText">
    <br>Attachments (folders or documents) belonging to a work item are 
    directly listed in the processing form.  <br><br>
</p>

<span class="OwHelpTopic3">
	Documents as Attachment
</span>

<p class="OwHelpText">
    <br>Clicking on the document's name it is opened in the viewer. <br>
    Similar as in eFile Management, you can edit attachments directly in the 
    processing form. Depending on your user role you can use different document 
    functions there. For example:  <br>
    <br><ul>
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_en/images/edit_properties.png" alt="" title=""/>  Edit properties</li>
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_en/images/copy.png" alt="" title=""/>  Copy</li>
		<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_en/images/download.png" alt="" title=""/>  Download</li>
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_en/images/deletebtn.png" alt="" title=""/>  Delete item (removes item from the attachment list)</li>				
    	<li><img style="vertical-align: middle;" src="<%=m_View.getContext().getBaseURL()%>/help_en/images/add_paste.png" alt="" title=""/>  Paste an object from clipboard</li>
	</ul>
	<br>You can comfortably switch between eFile Management and the processing form, e.g. to copy a document 
	from eFile Management and attach it to a work item/task. When you return to "AWD BPM" your processing form 
	is still opened.   <br><br>    
</p>

<span class="OwHelpTopic3">
	Folders as Attachment
</span>

<p class="OwHelpText">
    <br>Clicking the folder's name the respective eFile is opened in eFile Management. 
    There you can view and edit documents as usual and switch comfortably between 
    eFile Management and your processing form. Returning to "AWD BPM" your processing form 
    is still opened.   <br><br>     
</p>

<span class="OwHelpTopic2">
    Complete Process Step
</span>

<p class="OwHelpText">	
	<br>The lower part of the form provides different options you can select:  
</p>

<span class="OwHelpTopic3">
	<br>Save
</span>

<p class="OwHelpText">	
	Clicking "Save" your changes are saved. But processing is not 
	completed and the form remains open for further processing.
</p>

<span class="OwHelpTopic3">
	<br>Reassign
</span>

<p class="OwHelpText">	
	Select "Reassign", if the previous editor should check his entries again. 
	The work item/task then returns to the previous inbox or view (if applicable with your 
	saved changes). The processing form is closed and you get back to the last view. 
</p>

<span class="OwHelpTopic3">
	<br>Complete
</span>

<p class="OwHelpText">	
	In order to complete a process step select one of the given responses and click "Complete".  
	The work item/task is then automatically forwarded to the next editor or group inbox. The 
	processing form is closed and you return to the last opened view.   
</p>

<span class="OwHelpTopic3">
	<br>Cancel
</span>

<p class="OwHelpText">	
	At any point of time you can cancel processing when clicking on the symbol for "Close Dialog" (mostly 
	top left). Saved changes remain. The processing form is closed and you get back to the recently opened view.   
</p>