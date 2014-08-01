package com.wewebu.ow.server.ao;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Manager for plain {@link OwObject} application objects.<br/><br/>
 * Returns {@link OwObject} objects.<br/>
 * Retrieves only single objects and collection of objects.<br/>
 * Does not support parameterized retrieval through {@link #getApplicationObject(String, Object, boolean, boolean)}.
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
public class OwDefaultAOManager extends OwSupportBasedManager
{
    private static final Logger LOG = OwLogCore.getLogger(OwDefaultAOManager.class);

    private OwAOType<?> aoType;

    public OwDefaultAOManager()
    {

    }

    /**
     * Constructor
     * @param type_p integer application objects code  
     * @param aoSupport_p application {@link OwObject} persistence support
     * @param basePath_p path relative to the persistence support root of 
     *                   the managed objects' container  
     */
    public OwDefaultAOManager(OwAOType<?> type_p, OwAOSupport aoSupport_p, String basePath_p)
    {
        super(aoSupport_p, basePath_p);
        this.aoType = type_p;
    }

    public OwObject getApplicationObject(String strName_p, Object param_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        if (param_p != null)
        {
            LOG.fatal("OwDefaultAOManager.getApplicationObject():Not supported application object request!Could not process non null default object parameter - not implemented !");
            throw new OwNotSupportedException(new OwString("ecmimpl.OwAOManager.not.supported.application.object.request", "Not supported application object request!"));
        }
        else
        {
            return getAOSupportObject(strName_p, forceUserSpecificObject_p, createIfNotExist_p);
        }
    }

    public OwObject getApplicationObject(String strName_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return getApplicationObject(strName_p, null, forceUserSpecificObject_p, createIfNotExist_p);
    }

    public List<OwObject> getApplicationObjects(String strName_p, boolean forceUserSpecificObject_p) throws OwException
    {
        OwObject[] supportObjects = getAOSupportObjects(strName_p, forceUserSpecificObject_p, false);
        List<OwObject> objectCollection = new LinkedList<OwObject>();

        for (int i = 0; i < supportObjects.length; i++)
        {
            if (supportObjects[i] != null)
            {
                objectCollection.add(supportObjects[i]);
            }
        }

        return objectCollection;
    }

    @Override
    public final int getManagedType()
    {
        return this.aoType.getType();
    }

    //    @Override  will be introduced together when AOW managers refactoring
    public final OwAOType<?> getType()
    {
        return aoType;
    }

    @Override
    public String toString()
    {
        return "OwDefaultAOManager->" + super.toString();
    }
}
