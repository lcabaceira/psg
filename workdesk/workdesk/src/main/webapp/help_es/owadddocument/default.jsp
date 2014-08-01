<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Add Documents
</span>

<p class=OwHelpIntro>
    <br>With "Add Documents" you can add new documents from your local file system to the ECM system. Added documents are then shown 
    in the selected eFile and folder.    <br><br>
</p>

<span class=OwHelpTopic2>
    Steps
</span>

<p class="OwHelpText">
    <br>There are different possibilities to upload documents: <br><br>
    <ol><li>1. Using drag & drop on the "add documents" icon: Select the desired documents in Windows Explorer or at the Windows Desktop, drag them 
    to the "add documents" icon and drop them there.</li></ol>
    <ol><li>2. Using drag & drop on the result list: Select the desired documents in Windows Explorer or at the Windows Desktop, drag them 
    to the result list and drop them there. This functionality is only available if you have enabled the HTML5 document upload in the user settings and you are using one of the supported HTML5 browsers.</li></ol> 
    <ol><li>3. Using copy & paste: Select the desired documents in Windows Explorer or at the Windows Desktop, copy them, 
    click the "add documents" icon with the right mouse button and select "Paste". This functionality is only available if you have disabled the HTML5 document upload in the user settings.</li></ol>
    <ol><li>4. Directly click the "add documents" icon in Wordesk and upload the desired documents one after the other:
    	<ul type="square" class="OwHelpText">        
    	<li>* Local file: Select a document from your local file system using "Browse". </li>
    	<li>* No content: Choose this option, if it is just a collection of properties without a document (e.g. customer data).</li>
    	<li>* Then click "Upload". Uploaded objects are listed. When you uploaded all desired files, click "Next". </li>
		</ul>
    </li></ol>
    
    Afterwards follow the instructions in the "Add documents" dialog:<br><br>
</p>

<span class=OwHelpTopic3>
	1. Select Class
</span>

<p class="OwHelpText">
    Select a document class. Depending on to which eFile you want to add a new document, a standard class is chosen 
    by default or a document class is predefined. After selecting one, you see description and the properties of the class
    on the right. <br>
    If you have any questions concerning the available document classes, please contact 
    your administrator. <br><br>
</p>

<span class=OwHelpTopic3>
	2. Define Access Rights (optional)
</span>

<p class="OwHelpText">
  	If applicable, define the access rights for the added documents. (By default, the access 
  	rights of the respective folder are set.)
  	<br><br>
</p>

<span class=OwHelpTopic3>
	3. Set Properties
</span>

<p class="OwHelpText">
    When the document class is defined by default, you start with this step.
    Here you set the document properties.<br>
    Fields marked with an asterisk <img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/required.png" alt="" title=""/>
    are mandatory fields.
    When indexing several documents, metadata of the first document are inherited to the next 
    ones, but can be modified. The documents are displayed in the respective viewer in order to 
    facilitate indexing. <br>
    Click "Next" to continue.
</p>
<br>

<p class="OwHelpText">                     
    The documents are added to the ECM system.   
</p>
<br>