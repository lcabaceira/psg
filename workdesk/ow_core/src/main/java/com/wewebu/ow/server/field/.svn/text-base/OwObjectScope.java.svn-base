package com.wewebu.ow.server.field;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * OwObject based scope for Expression Language expressions.<br/>
 * Rules Engine for Highlighting in Hit List.
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
public class OwObjectScope extends OwPropertiesContainerScope
{
    private OwObject m_object;

    public OwObjectScope(String name_p, OwObject object_p)
    {
        super(name_p);
        this.m_object = object_p;
    }

    public String toString()
    {
        String objectDMSID = "<noDMSID>";
        try
        {
            objectDMSID = m_object.getDMSID();
        }
        catch (Exception e)
        {
            objectDMSID += ":" + e.getMessage();
        }

        return super.toString() + " aka OwObject DMSID=" + objectDMSID;
    }

    @Override
    protected OwProperty getProperty(String propertyName_p) throws OwException
    {
        try
        {
            return m_object.getProperty(propertyName_p);
        }
        catch (Exception e)
        {
            //avoid excessive logging
            throw new OwObjectNotFoundException(e.getMessage(), e);
        }
    }
}
