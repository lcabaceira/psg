package com.wewebu.ow.server.plug.owefileref;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * eFile-OwObject filter implementation based on a predefined set of DMSIDs.
 * Only allows {@link OwObject} with DMSIDs in the initial configured set.
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
 *@since 3.1.0.0
 */
public class OwEFileObjectCollectionFilter implements OwEFileFilter
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwEFileObjectCollectionFilter.class);

    private List allowedObjectsDMSIDs = new LinkedList();

    /**
     * Constructor
     * @param allowedObjects_p collection of objects that will provide 
     *                         the initial DMSID set
     * @throws OwException
     */
    public OwEFileObjectCollectionFilter(OwObjectCollection allowedObjects_p) throws OwException
    {
        super();
        try
        {
            if (allowedObjects_p != null)
            {
                for (Iterator i = allowedObjects_p.iterator(); i.hasNext();)
                {
                    OwObject allowedObject = (OwObject) i.next();
                    if (allowedObject != null)
                    {
                        String dmsid = allowedObject.getDMSID();
                        if (dmsid != null)
                        {
                            this.allowedObjectsDMSIDs.add(dmsid);
                        }
                    }
                }
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwEFileObjectCollectionFilter.init(): could not create filter ", e);
            throw new OwInvalidOperationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.error", "eFile references error!"), e);
        }
    }

    /**
     * 
     * @param eFile_p
     * @return <code>true</code> if the given object's DMSID is in the initial DMSID set
     *         <code>false</code> otherwise 
     * @throws OwException
     */
    public boolean allows(OwObject eFile_p) throws OwException
    {
        try
        {
            if (eFile_p == null)
            {
                return false;
            }
            String eFileDMSID = eFile_p.getDMSID();
            return this.allowedObjectsDMSIDs.contains(eFileDMSID);

        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwEFileObjectCollectionFilter.allows(): could not enforce filter ", e);
            throw new OwInvalidOperationException(new OwString("owefileref.OwEFileReferenceDocumentFunction.error", "eFile references error!"), e);

        }
    }
}
