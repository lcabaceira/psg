<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
	Help for Administration
</span>

<p class="OwHelpIntro">
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/administration.png" alt="" title=""/><br><br> 
	<em>Administration</em> provides a user interface to Wordesk's DB Role Manager and hosts links to infrastructure modelling tools from the according ECM vendor, if configured.  
</p>
<br>


<span class="OwHelpTopic2"> 
    DB Role Manager 
</span>

<p class="OwHelpText">	 
	<br> 
	In order to assign resources to a role, select a role by clicking on the button with the tree dots. If you previously already chose a role you may select it using the drop down box. <br><br> 
</p>

<span class="OwHelpTopic3"> 
    Object Class 
</span>

<p class="OwHelpText"> 
	Assign object classes to a role. Important: Per default, no object classes are assigned to any role. We recommend to first allow all object classes for all roles and then, in a second step, remove unwanted object classes.  
	<br><br>
</p> 
 
<span class="OwHelpTopic3">Supplementary Function 
</span>

<p class="OwHelpText">
	In <%=((OwInfoView)m_View).getLink("owsettings/default.jsp","Settings","OwHelpLink")%>, an Administrator assigns supplementary functionality to a role. An example would be to allow users of a role to have more configuration options in their <em>Settings</em>.  
	<br><br>
</p>

<span class="OwHelpTopic3">
    Plugin
</span>

<p class="OwHelpText"> 
	Assign plugins to a role. <br><br>
</p>

<span class="OwHelpTopic3"> 
    Virtual Queue 
</span>

<p class="OwHelpText"> 
	Assign preconfigured virtual queues to a role. <br><br>
</p>

<span class="OwHelpTopic3">
    Design
</span>

<p class="OwHelpText"> 
	Assign a design to a role. <br><br>
</p>

<span class="OwHelpTopic3">
    Index Fields
</span>

<p class="OwHelpText"> 
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/indexfields.png" alt="" title=""/><br><br>Set Wordesk's handling of index fields. They are displayed by object class. Every index field has three entries corresponding to the context the index field may occur in Wordesk. These contexts are <em>Create</em>, <em>Checkin</em>, and <em>View</em>.</p><p class="OwHelpText">For each context, an Administrator may define if the according index field is supposed to be presented to the user, by setting or removing the checkmarks <em>Allow </em>or <em>Deny</em>. The default value for every index field in every context is <em>Allow</em>. However, if role inherits allow settings for index fields / contexts that are not supposed to be visible to users, an explicit <em>Deny </em>can be set. 
	<br><br> 
</p>

<span class="OwHelpTopic3">
	Search Template
</span>

<p class="OwHelpText"> 
	Assign search templates to a role. <br><br>
</p>
	
<p class="OwHelpText">	
	You can allow or explicitly deny the access for all items of the selected category. 
	Click "Save" to save the changes. These will be available after the next login. <br>
	The checkmarks next to the check boxes show which settings are currently saved. 
	If one user has several roles at the same time, denied items apply first.<br><br>
</p>


<span class="OwHelpTopic2">
    Tools
</span>

<p class="OwHelpText">	
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/tools.png" alt="" title=""/><br><br>
	<br>In the tab &quot;Tools&quot; you can find links to external tools, e.g. Search Designer or Process Designer from IBM FileNet P8. Click the  
	link to launch the desired tool. <br><br>
</p>