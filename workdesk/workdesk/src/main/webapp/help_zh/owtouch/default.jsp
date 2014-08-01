<%@ page import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1"> Touch </span>

<p class="OwHelpIntro">
	<br/>This document function plugin shows the status of a document, 
	depending on operations	made on it. 
	<br/>The status is displayed as an icon or more near the document, according with the operations made by the user on the document.
	The plugin shows no icon when document wasn't modified by the user. <br/>
	The status information is available only when user is logged in, at logout time the touch information 
	is deleted.<br/>
	The image below shows the <b>touch</b> plugin in action.
	<br/>
	<br/>
	<img src="<%=m_View.getContext().getBaseURL()%>/help_en/images/touch.png"
		alt="<%=m_View.getContext().localize("help.image.touch.gif","Touch plugin in action image")%>"
		title="<%=m_View.getContext().localize("help.image.touch.gif","Touch plugin in action image")%>"
	/>
	<br/>
	<br/>

</p>

<p class="OwHelpIntro">
	In the image, we can see that 
	<ul class="OwHelpText">
		<li>
			the document "Adresse" was not "touched"
		</li>
		<li>
			the document "Aktien" was edited
		</li>
		<li>
			the document "Baufinazierung" was  viewed
		</li>
		<li>
			the document "Depotverwaltung" was viewed and edited.
		</li>
	</ul>
</p>