 <%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%> <%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Aide relative aux favoris

</span>

<p class="OwHelpText">
	<br>Vous trouverez ici des liens aux fichiers électroniques/dossiers et documents que vous avez ajoutés dans vos favoris dans <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","eFile Management","OwHelpLink")%>.<br><br>
	<img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/bookmark1.png" alt="" title=""/> <br>
	Cliquez sur un document pour l'ouvrir dans la visionneuse. Si vous cliquez sur un fichier électronique, celui-ci s'ouvre dans Gestion des fichiers électroniques.  
</p>

<p class="OwHelpText">
	L'icône <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/bookmark_delete.png" alt="<%=m_View.getContext().localize("help.images.bookmark_delete.gif","Delete bookmark image")%>" title="<%=m_View.getContext().localize("help.images.bookmark_delete.gif","Delete bookmark image")%>"/> permet de supprimer les signets aux documents et fichiers électroniques/dossiers respectivement.  
	
</p>

<p class="OwHelpText">
    <br>Vous y trouverez par ailleurs vos recherches enregistrées, que vous avez ajoutées dans vos favoris dans <%=((OwInfoView)m_View).getLink("owsearch/default.jsp","Search","OwHelpLink")%>.<br><br>
    <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/bookmark2.png" alt="" title=""/> <br>
    Un clic sur une recherche enregistrée permet de l'exécuter de nouveau.  
</p>

<p class="OwHelpText">
    L'icône <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/storedsearch_delete.png" alt="Supprimer la recherche enregistrée" title="Supprimer la recherche enregistrée"/> permet de supprimer les recherches enregistrées.  
    
</p>