<%@page import="com.wewebu.ow.server.plug.owconfig.OwRoleConfigurationView"%>
<%@page import="com.wewebu.ow.server.role.OwRoleManager"%>
<%@ page
	import="com.wewebu.ow.server.plug.owconfig.OwConfigurationDocument.OwCategoryInfo,com.wewebu.ow.server.plug.owconfig.OwConfigurationDocument.OwResourceInfo,com.wewebu.ow.server.ui.*,com.wewebu.ow.server.app.*,com.wewebu.ow.server.plug.owconfig.OwResourceInfoGroup,java.util.*,java.util.Map.Entry,com.wewebu.ow.server.plug.owconfig.OwRoleConfigurationAccessRightsInfoView"
	autoFlush="true" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8" language="java"%>

<%
    // get a reference to the calling view
    OwRoleConfigurationAccessRightsInfoView m_View = (OwRoleConfigurationAccessRightsInfoView) request.getAttribute(OwView.CURRENT_MODULE_KEY);
%>



<div class="OwRoleConfigurationAccessRightsInfoView">

	<%
	    OwCategoryInfo category = m_View.getCategory();
	    Collection resources = category.getResources();
	    Iterator resourceInfoIterator = resources.iterator();
	    String resourceId = "";
	    String resourceDisplayName = "";
	    while (resourceInfoIterator.hasNext())
	    {
	        OwResourceInfo resInfo = (OwResourceInfo) resourceInfoIterator.next();
	        int right = resInfo.getAccessRights();
	        int accessMask = resInfo.getAccessMask();
	        int viewMask = accessMask & OwRoleManager.ROLE_ACCESS_MASK_FLAG_DYNAMIC_RESOURCE_MODIFY;
	        if ((right == OwRoleManager.ROLE_ACCESS_RIGHT_ALLOWED) && (viewMask == OwRoleManager.ROLE_ACCESS_MASK_FLAG_DYNAMIC_RESOURCE_MODIFY))
	        {
	            resourceId = resInfo.getId();
	            resourceDisplayName = resInfo.getDisplayName();
	        }
	    }
	    
	    String resourceParamenterName=OwRoleConfigurationView.RESOURCE_PREFIX+category.getId();
	%>

	<div class="OwPropertyBlockLayout">
		<div class="OwBlockOdd OwPropertyBlock">
			<div class="OwPropertyLabel"><label for="<%=resourceParamenterName%>"><%=category.getDisplayName()%></label></div>
			<div class="OwPropertyValue">
				<div class="OwPropertyControl" >
					<input class="OwInputControl"  size="60" type="text" value="<%=resourceDisplayName%>"
						name="<%=resourceParamenterName%>" id="<%=resourceParamenterName%>" title="<%=category.getDisplayName()%>">
				</div>
				<div class="OwPropertyError" style="max-width: 270px;"></div>
			</div>
		</div>
	</div>

</div>