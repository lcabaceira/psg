package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.Locale;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession;

/**
 *<p>
 * OwCMISResourceObjectClassImpl.
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
 */
public class OwCMISResourceObjectClassImpl extends OwCMISAbstractSessionObjectClass<OwCMISSession> implements OwCMISResourceObjectClass
{
    private static final String CLASS_NAME = "ResourceObject";

    public OwCMISResourceObjectClassImpl(OwCMISSession session)
    {
        super(session);
        initializeAsHierarchyRoot();
    }

    @Override
    public String getMimetype()
    {
        return "ow_root/cmis_obst";
    }

    @Override
    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_ECM_ROOT_FOLDER;
    }

    @Override
    public String getClassName()
    {
        return CLASS_NAME;
    }

    @Override
    public String getDisplayName(Locale locale_p)
    {
        return CLASS_NAME;
    }

    @Override
    public String getDescription(Locale locale_p)
    {
        return CLASS_NAME;
    }

    @Override
    public OwCMISObjectClass getParent()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
