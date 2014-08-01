<%@ page
	import="java.util.*,com.wewebu.ow.server.app.*,com.wewebu.ow.server.ui.*,com.wewebu.ow.server.plug.owconfig.*"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
%><%
    // get a reference to the calling view
    OwRoleConfigurationView m_View = (OwRoleConfigurationView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    // detect if a role is selected (and we can define access rights for it)
    boolean isRoleSelected = (m_View.getCurrentRoleName() != null);
%>
<script type="text/javascript">
function openRoleDialog()
{
  navigateHREF(window,'<%=m_View.getEventURL("OpenRoleDialog", null)%>');
}
function changeToRecentRole()
{
	var elemId = 'owrolerecent';
    var recentId = null;
    if (Ext &&  Ext.getCmp(elemId))
    {
        var combo = Ext.getCmp(elemId);
        recentId = combo.getValue();
    }
    else
    {
    	recentId = document.getElementById(elemId).value;
    }
    
  var url = appendToUrl('<%=m_View.getEventURL("ChangeToRecentRole", null)%>','&recentRoleId='+recentId);
  navigateHREF(window,url);
}
function selectCategory()
{
	var elemId = 'owcategory';
    var categoryId = null;
    if (Ext &&  Ext.getCmp(elemId))
    {
        var combo = Ext.getCmp(elemId);
        categoryId = combo.getValue();
    }
    else
    {
    	categoryId = document.getElementById(elemId).value;
    }
    
  var url = appendToUrl('<%=m_View.getEventURL("SelectCategory", null)%>','&categoryId='+categoryId);
  navigateHREF(window,url);
}

function selectApplication()
{
	var elemId = 'owiapp';
    var appId = null;
    if (Ext &&  Ext.getCmp(elemId))
    {
        var combo = Ext.getCmp(elemId);
        appId = combo.getValue();
    }
    else
    {
    	appId = document.getElementById(elemId).value;
    }
    
  var url = appendToUrl('<%=m_View.getEventURL("SelectApplication", null)%>','&appId='+appId);
  navigateHREF(window,url);
}
</script>
<div class="OwRoleConfigurationView blockLabel">
	<h2><%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationView.caption", "Resources by Role")%></h2>
	<div class="block">
		<label for="owrolerecent"><%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationView.rolename", "Role:")%></label>
<%
            String currentSelectedRoleName = m_View.getCurrentRoleName();
            int currentSelectedId = 0;
		    List recentRoleItems = new LinkedList();
		    List recent = m_View.getRecentRoleNames();
		    for (int i = recent.size() - 1; i >= 0; i--)
		    {
		        String recentRoleName = (String) recent.get(i);
		        if(recentRoleName.equals(currentSelectedRoleName)) {
		            currentSelectedId = i;
		        }
		        OwComboItem item = new OwDefaultComboItem("" + i, recentRoleName);
		        recentRoleItems.add(item);
		    }
		    OwComboModel recentRolesModel = new OwDefaultComboModel(false, false, "" + currentSelectedId, recentRoleItems);
		    OwComboboxRenderer renderer = ((OwMainAppContext) m_View.getContext()).createComboboxRenderer(recentRolesModel, "owrolerecent",null,null,null);
		    renderer.addEvent("onchange", "changeToRecentRole()");
%>
		<div id="RecentRole" style="float: left; padding-right:5px;">
<%
			    renderer.renderCombo(out);
%>
		</div>
		<div style="float: left">
			<input type="button" name="owroleselect" value="..." onClick="openRoleDialog()" readonly />
		</div>
	</div>
<%      if (isRoleSelected)
	    {
	        Map<String,String> applications=m_View.getIntegratedApplicationsNames();
	        if (applications.size()>1)
	        { 
%>
		<div class="block">
		<label for="owiapp"><%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationView.integrated.application", "Application :")%></label>
<%
		    	List<OwComboItem> applicationsItems = new LinkedList<OwComboItem>();
		        Iterator<String> applicationIt = applications.keySet().iterator();

		        String currentApplication="all";
		        while (applicationIt.hasNext())
		        {
		            String applicationId=applicationIt.next();
		            OwComboItem item = new OwDefaultComboItem("" + applicationId, applications.get(applicationId));
		            applicationsItems.add(item);
		        }
		        
		        OwComboModel applicationsModel = new OwDefaultComboModel(false, false, m_View.getCurrentApplicationId(), applicationsItems);
		        OwComboboxRenderer applicationsRenderer = ((OwMainAppContext) m_View.getContext()).createComboboxRenderer(applicationsModel, "owiapp",null,null,null);
		        applicationsRenderer.addEvent("onchange", "selectApplication()");
		        applicationsRenderer.renderCombo(out);
%>
	</div>
		<%}%>	
	<div class="block">
		<label for="owcategory"><%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationView.category", "Resource Category:")%></label>
<%
		    	List categoryItems = new LinkedList();
		        Map categories = m_View.getCategoryInfos();
		        Iterator catIt = categories.values().iterator();

		        OwConfigurationDocument.OwCategoryInfo currentCategory = m_View.getCurrentCategory();
		        while (catIt.hasNext())
		        {
		            OwConfigurationDocument.OwCategoryInfo catInfo = (OwConfigurationDocument.OwCategoryInfo) catIt.next();
		            OwComboItem item = new OwDefaultComboItem("" + catInfo.getId(), catInfo.getDisplayName());
		            categoryItems.add(item);
		        }
		        OwComboModel categoriesModel = new OwDefaultComboModel(false, false, m_View.getCurrentCategory() == null ? null : "" + m_View.getCurrentCategory().getId(), categoryItems);
		        OwComboboxRenderer categoriesRenderer = ((OwMainAppContext) m_View.getContext()).createComboboxRenderer(categoriesModel, "owcategory",null,null,null);
		        categoriesRenderer.addEvent("onchange", "selectCategory()");
		        categoriesRenderer.renderCombo(out);
%>
	</div>

	<div class="block OwRoleConfigurationView_access_rights">
<%
		    m_View.renderRegion(out, OwRoleConfigurationView.ACCESS_RIGHTS_REGION);
%>
	</div>

	<div class="block OwRoleConfigurationView_menu">
<%
		    m_View.renderRegion(out, OwRoleConfigurationView.MENU_REGION);
%>
	</div>
<%
	    } // end of isRoleSelected
	    else
	    {
%>
	<div class="block">
		<%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationView.selectrolefirst", "Please select a role.")%>
	</div>
<%
	    }
%>
</div>