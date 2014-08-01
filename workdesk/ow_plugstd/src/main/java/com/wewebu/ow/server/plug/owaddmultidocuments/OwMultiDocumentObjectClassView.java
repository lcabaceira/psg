package com.wewebu.ow.server.plug.owaddmultidocuments;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassView;
import com.wewebu.ow.server.dmsdialogs.views.classes.OwObjectClassSelectionCfg;
import com.wewebu.ow.server.ecm.OwResource;

/**
 *<p>
 * Special class overwriting {@link OwObjectClassView},
 * creating special validation for extended DocumentImporter-Interface.
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
 *@since 2.5.2.0
 */
public class OwMultiDocumentObjectClassView extends OwObjectClassView
{

    public OwMultiDocumentObjectClassView(OwResource resource_p, int objectType_p)
    {
        super(resource_p, objectType_p);
    }

    /**
     * @deprecated you'd better use {@link #OwMultiDocumentObjectClassView(OwResource, int, OwObjectClassSelectionCfg)}
     * @param resource_p
     * @param iObjectType_p
     * @param strParentObjectClass_p
     */
    public OwMultiDocumentObjectClassView(OwResource resource_p, int iObjectType_p, String strParentObjectClass_p)
    {
        super(resource_p, iObjectType_p, strParentObjectClass_p);
    }

    /**
     * 
     * @param resource_p
     * @param iObjectType_p
     * @param classSelectionCfg
     * @since 4.1.0.0
     */
    public OwMultiDocumentObjectClassView(OwResource resource_p, int iObjectType_p, OwObjectClassSelectionCfg classSelectionCfg)
    {
        super(resource_p, iObjectType_p, classSelectionCfg);
    }

    public boolean isValidated() throws Exception
    {
        OwAddMultiDocumentsDocument doc = (OwAddMultiDocumentsDocument) getDocument();

        if (doc.getImportedDocumentsCount() == 0)
        {
            return false;
        }

        return doc.hasUndefinedObjectClass() ? doc.getUserSelectedClassName() != null || doc.getClassName() != null : true;
    }

}