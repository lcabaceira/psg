package com.wewebu.ow.server.ecm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wewebu.ow.server.ao.OwAOSupport;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwTestAOSupport.
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
public class OwTestAOSupport implements OwAOSupport
{

    private Map<String, List<OwObject>> multipleObjects = new HashMap<String, List<OwObject>>();
    private Map<String, OwObject> objects = new HashMap<String, OwObject>();

    public OwTestAOSupport()
    {
        super();
    }

    public void add(String name_p, OwObject object_p)
    {
        List<OwObject> namedObjects = multipleObjects.get(name_p);
        if (namedObjects == null)
        {
            namedObjects = new ArrayList<OwObject>();
            multipleObjects.put(name_p, namedObjects);
        }
        namedObjects.add(object_p);
    }

    public void put(String name_p, OwObject object_p)
    {
        objects.put(name_p, object_p);
    }

    public OwObject[] getSupportObjects(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        List<OwObject> namedObjects = multipleObjects.get(strName_p);
        if (namedObjects != null)
        {
            return namedObjects.toArray(new OwObject[] {});
        }
        else
        {
            return new OwObject[] {};
        }
    }

    public OwObject getSupportObject(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return objects.get(strName_p);
    }

}
