<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
    import="java.util.Iterator,java.util.*,
	       com.wewebu.ow.server.ui.*,
	       com.wewebu.ow.server.ecm.*,
	       com.wewebu.ow.server.app.*,
	       com.wewebu.ow.server.conf.*,
	       com.wewebu.ow.server.dmsdialogs.views.OwPrivilegesView,
	       com.wewebu.ow.server.dmsdialogs.views.OwPrivilegeSetEditorDocument,
	       com.wewebu.ow.server.dmsdialogs.views.OwPrivilegeSetEditor"
	autoFlush="true"
%><%
    // get a reference to the calling view
    OwPrivilegeSetEditor m_View = (OwPrivilegeSetEditor) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    OwPrivilegeSetEditorDocument document = m_View.getDocument();
%>
<script type="text/javascript">
	function openRoleDialog() {
		navigateHREF(window,'<%=m_View.getEventURL("OpenRoleDialog", null)%>');
	}
	function changeToRecentRole() {
		var recentId = document.getElementById('owrolerecent').value;
		var url = appendToUrl('<%=m_View.getEventURL("ChangeToRecentRole", null)%>','&recentRoleId=' + recentId);
		navigateHREF(window, url);
	}
</script>

<div class="OwPrivilegesSetEditor" id="OwMainContent">
	<div class="block">
		<div style="float: left; padding-right: 5px;">
			<label for="owrolerecent"><%=m_View.getContext().localize("jsp.OwPrivilegeSetEditor.rolename", "Role:")%></label>
		</div>
<%
		    List recentRoleItems = new LinkedList();
		    List recent = m_View.getRecentRoleNames();
		    for (int i = recent.size() - 1; i >= 0; i--)
		    {
		        String recentRoleName = (String) recent.get(i);
		        OwComboItem item = new OwDefaultComboItem("" + i, recentRoleName);
		        recentRoleItems.add(item);
		    }
		    OwComboModel recentRolesModel = new OwDefaultComboModel(false, false, document.getCurrentRoleName(), recentRoleItems);
		    OwComboboxRenderer renderer = ((OwMainAppContext) m_View.getContext()).createComboboxRenderer(recentRolesModel, "owrolerecent", null, null, null);
		    renderer.addEvent("onchange", "changeToRecentRole()");
%>
		<div style="float: left; padding-right: 5px;">
			<div style="float: left; padding-right: 5px;">
<%
                String changeToRecentRoleDisplayName=m_View.getContext().localize("OwPrivilegeSetEditor.changeToRecentRoleDisplayName", "Change to recent role");;
                OwInsertLabelHelper.insertLabelValue(out, changeToRecentRoleDisplayName, "owrolerecent");
			    renderer.renderCombo(out);
%>
			</div>
			<div style="float: left; padding-right: 5px; padding-top:5px; clear:left;">
				<select name="<%=m_View.PRIVILEGES_PARAMETER%>" multiple="multiple"  style="width:184px" size="20">
<%
					    Collection<OwPrivilege> availablePrivileges = document.getAvailableRolePrivileges();
					    for (OwPrivilege privilege : availablePrivileges)
					    {
%>
					<option value="<%=document.privilegeToGuid(privilege.getName())%>"><%=document.displayNameOf(privilege)%></option>
<%
					    }
%>
				</select>
			</div>

			<div style="clear:left;float:left;padding-top: 5px;">
				<input type="button" name="selectUser" value="<%=m_View.getContext().localize("jsp.OwPrivilegeSetEditor.add","Add Privilege")%>" 
					onkeydown="event.cancelBubble=true" 
					onclick="<%=m_View.getFormEventURL("AddPrivilege", null)%>">
			</div>	
		</div>
		<div style="float: left">
			<input type="button" name="owroleselect" value="..." onClick="openRoleDialog()" readonly />
		</div>
	</div>
</div>