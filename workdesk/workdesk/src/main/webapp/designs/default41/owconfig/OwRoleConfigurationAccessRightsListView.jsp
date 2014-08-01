<%@page
	import="com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*,com.wewebu.ow.server.plug.owconfig.OwResourceInfoGroup,java.util.*,java.util.Map.Entry,com.wewebu.ow.server.plug.owconfig.OwRoleConfigurationAccessRightsListView"
	autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
%><%
    // get a reference to the calling view
    OwRoleConfigurationAccessRightsListView m_View = (OwRoleConfigurationAccessRightsListView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>
<script type="text/javascript">
function selectGroup()
{
	var elemId = 'owgroup';
    var groupId = null;
    if (Ext &&  Ext.getCmp(elemId))
    {
        var combo = Ext.getCmp(elemId);
        groupId = combo.getValue();
    }
    else
    {
    	groupId = document.getElementById(elemId).value;
    }
    
  var url = appendToUrl('<%=m_View.getEventURL("SelectGroup", null)%>','&groupId='+groupId);
  navigateHREF(window,url);
}
</script>
<div class="OwRoleConfigurationAccessRightsListView">
<%
    if (m_View.hasMultipleAccessGroups())
    {
%>
	<div class="block blockLabel">
		<label for="owgroup"><%=m_View.getGroupSelectionTitle()%>:</label>
<%
		    List groups = m_View.getGroups();
		        Iterator groupsIterator = groups.iterator();
		        OwResourceInfoGroup currentGroup = m_View.getCurrentGroup();
		        List groupItems = new LinkedList();
		        while (groupsIterator.hasNext())
		        {
		            OwResourceInfoGroup groupInfo = (OwResourceInfoGroup) groupsIterator.next();
		            OwComboItem item = new OwDefaultComboItem("" + groupInfo.getGroupId(), groupInfo.getGroupDisplayName());
		            groupItems.add(item);
		        }
		        OwComboModel groupsComboModel = new OwDefaultComboModel(false, false, "" + currentGroup.getGroupId(), groupItems);
		        OwComboboxRenderer groupRenderer = ((OwMainAppContext) m_View.getContext()).createComboboxRenderer(groupsComboModel, "owgroup",null,null,null);
		        groupRenderer.addEvent("onchange", "selectGroup()");
		        groupRenderer.renderCombo(out);
%>
	</div>
<%
	}//end hasMultipleAccessGroups
%>
	<table class="OwGeneralList_table">
		<caption>
<%          m_View.renderTableCaption(out);%>
		</caption>

		<thead>
			<tr class="OwGeneralList_header">
				<td><%String title = m_View.getContext().localize("app.OwObjectListView.toggleselectall", "Alles (de)selektieren");%>
					<a href="javascript:toggleAccessRightsRadio('<%=m_View.getParent().getFormName()%>','allowdeny_','allow');">
					   <img	src="<%=m_View.getContext().getDesignURL()%>/images/ok_btn16.png" title="<%=title%>" alt="<%=title%>">
					</a><%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationAccessRightsListView.allow", "Allow")%></td>
<%
				    if (m_View.getSupportDeny())
				    {
%>
					<td>
						<a href="javascript:toggleAccessRightsRadio('<%=m_View.getParent().getFormName()%>','allowdeny_','deny');">
					   		<img	src="<%=m_View.getContext().getDesignURL()%>/images/ok_btn16.png" title="<%=title%>" alt="<%=title%>">
						</a><%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationAccessRightsListView.deny", "Deny")%>
					</td>
					
					<td>
						<a href="javascript:toggleAccessRightsRadio('<%=m_View.getParent().getFormName()%>','allowdeny_','impldeny');">
						   <img	src="<%=m_View.getContext().getDesignURL()%>/images/ok_btn16.png" title="<%=title%>" alt="<%=title%>">
						</a><%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationAccessRightsListView.implicit.deny", "Implicit Deny")%>
					</td>
					
<%
				    }
%>
				<td><%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationAccessRightsListView.name", "Name")%>&nbsp;
				</td>
				<td><%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationAccessRightsListView.id", "ID")%>&nbsp;
				</td>
				<td><%=m_View.getContext().localize("jsp.owconfig.OwRoleConfigurationAccessRightsListView.category", "Category")%>&nbsp;
				</td>
<%				    // render all access mask falgs
				    Map accessMaskFlags = m_View.getAccessMaskDescriptions();
				    Iterator itAccessMaskFlags = accessMaskFlags.entrySet().iterator();
				    while (itAccessMaskFlags.hasNext())
				    {
				        Entry accessMaskFlagEntry = (Entry) itAccessMaskFlags.next();
				        String displayname = (String) accessMaskFlagEntry.getValue();
%>
				<td><%=displayname%></td>
<%
				    }
%>
			</tr>
		</thead>

		<tbody id="OwRoleConfigurationAccessRightsListView_body">
<%
			    m_View.renderRows(out);
%>
		</tbody>
	</table>
</div>