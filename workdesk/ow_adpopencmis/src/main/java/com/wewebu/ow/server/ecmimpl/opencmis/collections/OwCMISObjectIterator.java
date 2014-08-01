package com.wewebu.ow.server.ecmimpl.opencmis.collections;

import java.util.Iterator;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * ........
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
 *@since 4.2.0.0
 */
public class OwCMISObjectIterator implements Iterator<OwCMISObject>
{

    private Iterator<? extends CmisObject> iterator;
    private OwCMISNativeSession session;

    public OwCMISObjectIterator(Iterator<? extends CmisObject> iterator, OwCMISNativeSession session)
    {
        this.iterator = iterator;
        this.session = session;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public OwCMISObject next()
    {
        CmisObject child = this.iterator.next();
        return from(child);
    }

    private OwCMISObject from(CmisObject child)
    {
        try
        {
            TransientCmisObject transinetChild = child.getTransientObject();
            OwCMISNativeObject<TransientCmisObject> childObject = this.session.from(transinetChild, null);
            return childObject;
        }
        catch (OwException owe)
        {
            throw new RuntimeException(owe);
        }
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove()
    {
        throw new RuntimeException("Not supported yet!");
    }

}
