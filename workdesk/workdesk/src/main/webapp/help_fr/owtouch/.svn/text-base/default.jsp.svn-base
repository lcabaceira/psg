 <%@ page import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%> <%
    // get a reference to the calling view
    OwView m_View = (OwView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1"> Modifications  </span>

<p class="OwHelpIntro">
	<br/>Ce module d'extension applicable aux documents indique l'état d'un document, selon les opérations effectuées dessus. 
	<br/>Cet état est représenté par une icône, ou plusieurs icônes, à proximité du document, selon les opérations effectuées par l'utilisateur sur celui-ci. Aucune icône n'apparaît dans le module d'extension si l'utilisateur n'a apporté aucune modification dans le document. <br/>
	Les informations relatives à l'état du document ne sont disponibles que lorsque l'utilisateur est connecté&nbsp;; dès lors qu'il est déconnecté, les informations relatives aux modifications sont supprimées.<br/>
	L'image ci-dessous montre le module d'extension <b>Modifications</b>.
	<br/>
	<br/>
	<img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/touch.png"
		alt="<%=m_View.getContext().localize("help.image.touch.gif","Touch plugin in action image")%>"
		title="<%=m_View.getContext().localize("help.image.touch.gif","Touch plugin in action image")%>"/>
	<br/>
	<br/>

</p>

<p class="OwHelpIntro">
	L'image montre que&nbsp;: 
	<ul class="OwHelpText">
		<li>
			le document «&nbsp;Adresse&nbsp;» n'a pas été «&nbsp;modifié&nbsp;»&nbsp;;
		</li>
		<li>
			le document «&nbsp;Aktien&nbsp;» a été édité&nbsp;;
		</li>
		<li>
			le document «&nbsp;Baufinazierung&nbsp;» a été visualisé&nbsp;;
		</li>
		<li>
			le document «&nbsp;Depotverwaltung&nbsp;» a été visualisé et édité.
		</li>
	</ul>
</p>