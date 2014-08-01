package com.wewebu.ow.server.plug.owutil;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwVersionSeries;

/**
 *<p>
 * Static class with utility functions to access OwObjects.
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
public class OwObjectUtils
{
    /** get the latest version collection of the given collection
     * 
     * @param objects_p Collection of OwObject
     * @return Collection of OwObject
     * @throws Exception
     */
    public static Collection<OwObject> getLatesVersionObjects(@SuppressWarnings("rawtypes") final Collection objects_p) throws Exception
    {
        // === make sure we deal with the latest version
        LinkedList<OwObject> objects = new LinkedList<OwObject>();

        Iterator<?> it = objects_p.iterator();
        while (it.hasNext())
        {
            OwObject object = (OwObject) it.next();
            if (object.hasVersionSeries())
            {
                // === get latest version
                OwVersionSeries vseries = object.getVersionSeries();
                OwObject objectlatest = vseries.getObject(vseries.getLatest());
                objectlatest.refreshProperties();
                objects.add(objectlatest);
            }
            else
            {
                // === no versions
                objects.add(object);
            }
        }

        return objects;
    }
}
