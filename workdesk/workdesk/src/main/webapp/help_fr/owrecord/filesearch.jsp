 <%@ page 
    import="com.wewebu.ow.server.ui.*, com.wewebu.ow.server.app.*" 
    autoFlush   ="true"
 pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"%> <%
    // get a reference to the calling view
    OwView m_View = (OwView)request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>

<span class=OwHelpTopic1>
	Aide à la recherche de fichiers électroniques

</span>

<p class="OwHelpText">
	   
    «&nbsp;Rechercher des fichiers électroniques&nbsp;» montre les documents et données présentant au moins une propriété en commun (numéro de client, classe de document, priorité, date de modification, par exemple). Ces propriétés sont combinées en critères de recherche et affichées dans un dossier sous forme de structure arborescente. Il existe une seule différence par rapport à une structure de fichier habituelle&nbsp;: les dossiers n'existent pas physiquement dans le système ECM, mais virtuellement. 
    <br><br> 
</p>

<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        Vue des fichiers électroniques et dossiers virtuels<br>   
       
    </span>

</a>

<p class="OwHelpText">
     
    La fonction «&nbsp;Rechercher des fichiers électroniques&nbsp;» montre les fichiers électroniques/dossiers de la même manière que les fonctions «&nbsp;Gestion des fichiers électroniques&nbsp;» et «&nbsp;Parcourir&nbsp;». Ces dossiers ne se trouvent toutefois pas physiquement dans le système ECM, mais réunissent simplement divers critères de recherche. Par exemple&nbsp;: le premier dossier (dossier racine) montre tous les documents auxquels est associé un numéro de client donné. Il peut également contenir un sous-dossier montrant tous les documents auxquels est associé un numéro de client donné et appartenant à une classe de document spécifique. Un niveau en dessous, sont affichés les documents auxquels est associé un numéro de client donné et appartenant à une classe de document spécifique, qui ont été libérés dans le système ECM une année donnée.  <br> 
    De la même manière que dans «&nbsp;Gestion des fichiers électroniques&nbsp;» ou dans «&nbsp;Parcourir&nbsp;», vous cliquez sur le signe plus-moins en regard d'un fichier électronique/dossier pour en masquer les sous-dossiers. Pour afficher le contenu d'un (sous)-dossier, cliquez sur son nom. Le dossier affiché est indiqué. 
    <br><br>
	La vue du dossier montre le contenu du dossier sélectionné. Vous pouvez, par un clic de souris, ouvrir des documents dans une visionneuse ou, le cas échéant, dans un programme d'édition. Par ailleurs, comme dans «&nbsp;Gestion des fichiers électroniques&nbsp;», plusieurs fonctions de documents et types de listes sont disponibles. Pour obtenir une description détaillée, veuillez consulter la page d'aide <%=((OwInfoView)m_View).getLink("owrecord/default.jsp","eFile Management","OwHelpLink")%>. Toutefois, du fait de la structure des fichiers électroniques virtuels, il existe quelques différences au niveau des fonctions suivantes&nbsp;:
	<br clear="all">
</p>

<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Créer un nouveau fichier électronique/dossier et <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/add_record.png" alt="" title=""/> 
	Créer un sous-dossier

</span>
<p class="OwHelpText">
    Comme il n'existe aucun fichier électronique ou dossier réel, vous ne pouvez pas créer de nouveaux fichiers électroniques ou de dossiers dans «&nbsp;Recherche de fichiers électroniques&nbsp;». Si vous utilisez fréquemment un ensemble spécifique de critères de recherche et que vous souhaitez les afficher dans un fichier électronique ou dossier virtuel, veuillez contacter votre administrateur.  <br>
</p>


<span class=OwHelpTopic3>
	<br><img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/paste.png" alt="" title=""/> 
	Coller des documents à partir du presse-papiers et <img src="<%=m_View.getContext().getBaseURL()%>/help_de/images/move.png" alt="" title=""/> 
	Déplacer des documents à partir du presse-papiers

</span>
<p class="OwHelpText">
    Même si «&nbsp;Recherche de fichiers électroniques&nbsp;» permet de copier des documents vers le presse-papiers, vous ne pouvez pas les coller dans un fichier électronique ou dossier virtuel ni les y déplacer. Les fichiers électroniques/dossiers virtuels montrent les documents dotés d'une propriété donnée. Afin de déplacer un document vers un autre fichier électronique/dossier virtuel, vous devez modifier ses propriétés (<%=((OwInfoView)m_View).getLink("owdocprops/default.jsp","Edit Properties","OwHelpLink")%>) en conformité avec ce fichier électronique/dossier virtuel. Ce document s'affichera ensuite automatiquement dans le fichier électronique/dossier virtuel correspondant. 
    <br>
</p>


<a name="#DocSearchModule">
    <span class=OwHelpTopic2>
        <br><br>Affinage de la vue<br>   
       
    </span>

</a>

<p class="OwHelpText">
    L'objet que vous recherchez n'apparaît pas dans les résultats&nbsp;? Vous pouvez affiner votre vue. Saisissez d'autres critères de recherche au-dessus de la vue du fichier électronique et cliquez sur «&nbsp;Recherche&nbsp;». <br>
    Pour accélérer votre recherche, vous pouvez définir un «&nbsp;Nombre maximum de résultats&nbsp;» dans les modèles de recherche.  
    <br><br clear="all"><br><br>
</p>
