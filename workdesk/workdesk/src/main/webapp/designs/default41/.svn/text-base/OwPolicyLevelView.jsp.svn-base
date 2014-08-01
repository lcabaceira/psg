<%@ page autoFlush="true" pageEncoding="utf-8" contentType="text/html; charset=utf-8" language="java"
	import="java.util.Iterator,java.util.*,
	      com.wewebu.ow.server.ui.*,
	      com.wewebu.ow.server.app.*,
	      com.wewebu.ow.server.dmsdialogs.views.OwPolicyLevelView,
	      com.wewebu.ow.server.dmsdialogs.views.OwPolicyLevelView.OwPolicyViewItem"
%><% // get a reference to the calling view
    OwPolicyLevelView m_View = (OwPolicyLevelView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
    boolean useLiveUpdate=m_View.isForceLiveUpdate();
%>
<!-- <div class='OwMainColumnHeader'>&nbsp;</div> -->
<script type="text/javascript">
function policyChanged()
{
<%if (useLiveUpdate){
    out.write(m_View.createAddEventURL());
}else{
    out.write("document.getElementById('allpidChanged').style.visibility = 'visible';");
}%>
}
</script>
<div class="OwPolicyLevelView">
	<div class="OwPolicyLevelView_message">
<%          m_View.renderRegion(out,OwPolicyLevelView.MESSAGE_REGION); %>
	</div>
<%     if (m_View.canAddMultiPolicy()){ %>
		<div class="OwPolicyLevelView_policySet">
			<p><label for="avpid">
				<%=m_View.getContext().localize("app.OwPolicyLevelView.available.policies","Available policies:")%>
			</label>
			</p>
			<select id="avpid" name="<%=m_View.getAvailableListId()%>" multiple="multiple">
<%
			  List availablePolicies=m_View.getAvailablePolicyItems();
			  for (Iterator i = availablePolicies.iterator(); i.hasNext();)
	          {
	              OwPolicyLevelView.OwPolicyViewItem policyItem = (OwPolicyLevelView.OwPolicyViewItem) i.next();
%>
	            <option value="<%=policyItem.getId()%>"><%=policyItem.getName()%></option>
<%     
	          }
%>
			</select>
		</div>
	
		<div class="OwPolicyLevelView_addRemove">
<%       if (m_View.canSetPolicies()){ %>
			<a class="OwPolicyLevelView_add" href="javascript:{<%=m_View.createAddEventURL()%>;}" title="<%=m_View.getContext().localize("app.OwPolicyLevelView.add.policies.title","Add selected policies")%>"></a>
			<a class="OwPolicyLevelView_remove" href="javascript:{<%=m_View.createRemoveEventURL()%>;}" title="<%=m_View.getContext().localize("app.OwPolicyLevelView.remove.policies.title","Remove selected policies")%>"></a>
<%       } %>
		</div>
		<div class="OwPolicyLevelView_policySet">
			<p>
			  <label for="efpid">
				<%=m_View.getContext().localize("app.OwPolicyLevelView.effective.policies","Effective policies:")%>
			  </label>
			</p>
			<select id="efpid" name="<%=m_View.getEffectiveListId()%>" multiple="multiple">
<%
	          List effectivePolicies=m_View.getEffectivePolicyItems();
	          for (Iterator i = effectivePolicies.iterator(); i.hasNext();)
	          {
	              OwPolicyLevelView.OwPolicyViewItem policyItem = (OwPolicyLevelView.OwPolicyViewItem) i.next();
%>
	            <option value="<%=policyItem.getId()%>"><%=policyItem.getName()%></option>
<%     
	          }
%>
			</select>
		</div>
<%    } else {%> 
              <div class="OwInlineMenu">
<%                m_View.renderRegion(out,OwPolicyLevelView.SELECT_ALL_POLICIES_REGION); %>
              </div>
<%              if (!useLiveUpdate) { %>
              <div id="allpidChanged" style="float:left;padding-left:10px;padding-top:10px;visibility:hidden;overflow:hidden;">
                <p style="font-size: 24pt;font-weight: bold;">*</p>
              </div>
<%              }
    }%>
	<div style="width:10px;float:left;clear:left;"/>
</div>