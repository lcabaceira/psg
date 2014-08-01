 <%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%> <%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class="OwHelpTopic1">
    Aide relative à «&nbsp;Editer les propriétés&nbsp;»

</span>

<p class="OwHelpIntro">
    <br><br>«&nbsp;Editer les propriétés&nbsp;» permet d'afficher et de modifier les propriétés de documents. Vous pouvez également afficher les droits d'accès et l'historique des documents et voir quelles versions se trouvent dans quels fichiers. <br>
    Parallèlement, les documents sont affichés dans une visionneuse située à côté de «&nbsp;Editer les propriétés&nbsp;». Lorsque vous éditez simultanément les propriétés de plusieurs documents, vous pouvez passer de l'un à l'autre au moyen de flèches. Vous pouvez également alterner l'affichage dans la visionneuse. <br><br>
</p>


<span class="OwHelpTopic2">
    Fonctions

</span>

<br>
<br>
<p class="OwHelpText"> <!-- img src='<%=m_View.getContext().getBaseURL()%>/help_fr/images/documentFunctions.png'><br><br -->
	«&nbsp;Editer les propriétés&nbsp;» propose de multiples fonctions de gestion des versions de documents&nbsp;:
	<br>
     <ul type="square" class="OwHelpText">
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/checkout.png" alt="" title=""/> 
        Réserver&nbsp;: verrouille les documents à éditer. Vous seul avez ainsi accès au document, notamment pour le télécharger et l'éditer.
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/cancelcheckout.png" alt="" title=""/> 
        Annuler la réservation&nbsp;: déverrouille un document réservé sans enregistrer les modifications.  
   	 	</li>
     	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/demoteversion.png" alt="" title=""/> 
        Rétrograder la version&nbsp;: rétrograde la version majeure actuelle à la dernière version mineure possible, par exemple de&nbsp;2.0 à&nbsp;1.1. 
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/promoteversion.png" alt="" title=""/> 
        Promouvoir la version&nbsp;: promeut la dernière version mineure libérée à la version majeure suivante la plus élevée possible, par exemple de&nbsp;3.1 à&nbsp;4.0 (à condition qu'elle ait été préalablement rétrogradée).
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/save.png" alt="" title=""/> 
        Enregistrer&nbsp;: enregistre un document dans le système en tant que nouvelle version mineure. De toute façon, le document restera verrouillé jusqu'à ce que vous le libériez.
    	</li>
    	<li>
        <img src="<%=m_View.getContext().getBaseURL()%>/help_fr/images/checkin.png" alt="" title=""/> 
        Libérer&nbsp;: enregistre un document dans le système en tant que nouvelle version majeure (<%=((OwInfoView)m_View).getLink("owadddocument/default.jsp","how to do ...","OwHelpLink")%>).
    	</li>
	</ul>	
</p>
<br>

<span class="OwHelpTopic2">
    Vues  
  
</span>
<br>
<br>

<span class="OwHelpTopic3">
	Propriétés

</span>

<p class="OwHelpText">	
    Permet de modifier les propriétés d'un document. Saisissez simplement de nouvelles valeurs dans les champs correspondants. Puis cliquez sur «&nbsp;Enregistrer&nbsp;» pour enregistrer vos modifications. <br>
    Remarque&nbsp;: les champs signalés par un astérisque doivent obligatoirement être renseignés. Les propriétés automatiquement définies telles que Date de création ou Numéro de client n'étant pas modifiables, les champs correspondants sont inactifs. <br>
    Dans les longues listes de choix (zones de sélection déroulantes), vous pouvez sélectionner rapidement une propriété en saisissant les premières lettres qui la composent. Le curseur passe à l'entrée suivante correspondante, et la liste de choix se raccourcit. Cette fonction est disponible pour toutes les listes de choix dans Workdesk.   
    <br><br>
</p>


<span class="OwHelpTopic3">
	Propriétés système

</span>

<p class="OwHelpText">
    Les propriétés système informent sur les documents et fichiers automatiquement créés par le système. Vous pouvez les afficher, mais pas les modifier.   <br><br>
</p>

<span class="OwHelpTopic3">
	Droits d'accès (facultatif)

</span>

<p class="OwHelpText">
     ... indique les droits d'accès dont disposent différents utilisateurs sur le document. Seul l'administrateur peut accorder ou refuser explicitement des droits d'accès. Si vous ne disposez pas des droits d'accès dont vous avez besoin, veuillez contacter votre administrateur.<br><br>
</p>

<span class="OwHelpTopic3">
	Fichiers 
 
</span>

<p class="OwHelpText">
    La vue «&nbsp;Fichiers&nbsp;» présente une liste de tous les fichiers électroniques (et dossiers) contenant le document actuel. Un clic sur un lien ouvre le fichier électronique/dossier sélectionné et le sous-dossier contenant le document.<br><br>
</p>

<span class="OwHelpTopic3">
	Versions

</span>

<p class="OwHelpText">
     Toutes les versions disponibles du document sont répertoriées et gérées ici. Si vous cliquez sur une version donnée, seules les propriétés de celle-ci s'affichent.<br><br>
</p>



<span class="OwHelpTopic3">
	Piste d'audit 
 
</span>

<p class="OwHelpText">
     Cette vue montre la piste d'audit du document. Vous pouvez trier les entrées en cliquant sur l'en-tête de colonne correspondant dans toutes les listes affichées dans Workdesk. L'administrateur définit quelles étapes de traitement sont enregistrées dans la piste d'audit.
</p>

<br>

