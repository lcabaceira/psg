package com.wewebu.ow.server.plug.owdms;

import java.util.Collection;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.command.OwCommand;
import com.wewebu.ow.server.command.OwProcessableObjectStrategy;
import com.wewebu.ow.server.ecm.OwObject;

/**
 *<p>
 * Implementation of the command promote.
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
public class OwCommandPromote extends OwCommand
{
    /**
     * Constructor
     * @param objects_p - the list of objects
     * @param appContext_p - the application context
     * @param processableObjectStrategy_p - checker for processability state of an object.
     */
    public OwCommandPromote(Collection objects_p, OwMainAppContext appContext_p, OwProcessableObjectStrategy processableObjectStrategy_p)
    {
        super(objects_p, appContext_p, processableObjectStrategy_p);
    }

    /**
     * Constructor
     * @param object_p - the object to be promoted
     * @param appContext_p - the application context
     * @param processableObjectStrategy_p - checker for processability state of an object.
     */
    public OwCommandPromote(OwObject object_p, OwMainAppContext appContext_p, OwProcessableObjectStrategy processableObjectStrategy_p)
    {
        super(object_p, appContext_p, processableObjectStrategy_p);
    }

    /**
     * Promote an object
     * @param object_p - the object to be promoted.
     */
    protected void processObject(OwObject object_p) throws Exception
    {
        object_p.getVersion().promote();
        object_p.refreshProperties();
    }

}