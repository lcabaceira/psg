package com.wewebu.ow.server.dmsdialogs.views;

import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * {@link OwPermissionCollection} policy facade document for {@link OwPolicyLevelView}
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
public class OwPolicyLevelDocument extends OwDocument
{
    private OwPermissionsDocument permissionsDocument;

    public OwPolicyLevelDocument(OwPermissionsDocument permissionsDocument_p)
    {
        this.permissionsDocument = permissionsDocument_p;
    }

    public OwPermissionCollection getPermissions()
    {
        return permissionsDocument.getPermissions();
    }
}
