<%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Help for Demo
</span>
<p class="OwHelpIntro">
	<br>The plug-in "Demo" shall help software developers to get started with programming the Alfresco Wordesk Framework.
	It shows how to use views/layouts and documents with the Wordesk Framework.<br><br>
</p>

<span class=OwHelpTopic2>
	Class: OwDemoView
</span>
<p class="OwHelpText">
<br>Class "OwDemoView" is derived from class "OwMainView".
The "init" method of "OwDemoView" includes the following views:
<br><br></p>
<ul type="square" class="OwHelpText">
    <li>OwDemoLayoutView</li>
	<li>OwDemoMenuView</li>
	<li>OwDemoDisplayView</li>
	<li>OwDemoColorLabelView</li>
</ul>

<br><img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/demo.gif" alt="" title=""/>
<br>
<br>
<span class=OwHelpTopic3>
	Class: OwDemoLayoutView
</span>
<p class="OwHelpText">
Class "OwDemoLayoutView" serves as table grid and places the other views into the generated site.
The "onRender" method creates a table with HTML tags.
By calling the "renderRegion" method a view which is defined as region can be rendered.
</p>
<br>

<span class=OwHelpTopic3>
	Class: OwDemoMenuView
</span>
<p class="OwHelpText">
The classes "OwDemoMenuView", "OwDemoDisplayView" and "OwDemoColorLabelView" which are used for the demo plug-in
explain how the separate views interact with each other. 
You can change the color properties of a "DisplayView" cell, if you click on one of the links ("Red", "Green" and "Blue").
The "onClick" method calls the "setColor" method of "OwDemoDocument".
</p>
<br>

<span class=OwHelpTopic3>
	Class: OwDemoDisplayView
</span>
<p class="OwHelpText">
The class "OwDemoDisplayView" renders a table which background cell color is dynamically generated.
The color information is saved in a variable of class "OwDemoDocument". 
You can request it with the "getColor" method from "OwDemoDocument". 
</p>
<br>

<span class=OwHelpTopic3>
	Class: OwDemoColorLabelView
</span>
<p class="OwHelpText">
With its "onRender" method, the class "OwDemoColorLabelView" creates a table 
which displays the background color of the display view.
In this case, the value of the background color is not changed by the "onClick" method of "OwDemoMenuView".
Instead it is changed with an update event which calls the "setColor" method of "OwDemoDocument". 
</p>
<br>

<span class=OwHelpTopic3>
	Class: OwSubMenuView
</span>
<p class="OwHelpText">
The class "OwSubMenuView" creates the horizontally aligned menu. 
Single menu entries are added with the "addMenuItem" method which is called by the "init" method of class "OwDemoMenuView".
</p>
<br>

<span class=OwHelpTopic2>
	Class: OwDemoDocument
</span>
<p class="OwHelpText">
<br>The class "OwDemoDocument" is derived from class "OwMainDocument". The following additional methods are implemented:
</p>
<ul type="square" class="OwHelpText">
    <li>setColor()</li>
	<li>getColor()</li>
</ul>
