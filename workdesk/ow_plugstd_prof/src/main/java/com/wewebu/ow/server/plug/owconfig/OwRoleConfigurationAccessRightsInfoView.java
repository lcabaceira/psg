package com.wewebu.ow.server.plug.owconfig;

/**
 *<p>
 * {@link com.wewebu.ow.server.plug.owconfig.OwConfigurationDocument.OwCategoryInfo OwCategoryInfo} based view for single value , dynamic resource categories.
 * 
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 *@since 4.0.0.0
 */

public class OwRoleConfigurationAccessRightsInfoView extends OwRoleConfigurationAccessRightsView
{

    /** render the view
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(java.io.Writer w_p) throws Exception
    {
        serverSideDesignInclude("owconfig/OwRoleConfigurationAccessRightsInfoView.jsp", w_p);
    }
}
