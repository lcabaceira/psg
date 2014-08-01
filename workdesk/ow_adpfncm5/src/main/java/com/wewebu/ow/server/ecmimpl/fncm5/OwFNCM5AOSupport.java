package com.wewebu.ow.server.ecmimpl.fncm5;

import com.filenet.api.core.Document;
import com.wewebu.ow.server.ao.OwRepositoryAOSupport;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5PreferencesDocument;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * A support class for {@link OwFNCM5PreferencesDocument}.
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
 *@since 3.2.0.2
 */
public class OwFNCM5AOSupport extends OwRepositoryAOSupport
{

    public OwFNCM5AOSupport(OwNetwork network_p, String path_p, int[] objectTypes_p, int versionSelection_p, String aoClassName_p)
    {
        this(network_p, path_p, objectTypes_p, versionSelection_p, aoClassName_p, Integer.MAX_VALUE);
    }

    public OwFNCM5AOSupport(OwNetwork network_p, String path_p, int[] objectTypes_p, int versionSelection_p, String aoClassName_p, int maxSize_p)
    {
        super(network_p, path_p, objectTypes_p, versionSelection_p, aoClassName_p, maxSize_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ao.OwRepositoryAOSupport#getSupportObject(java.lang.String, boolean, boolean)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public OwObject getSupportObject(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        try
        {
            OwObject supportObject = super.getSupportObject(strName_p, forceUserspecificObject_p, createIfNotExist_p);
            OwFNCM5Class objectClass = (OwFNCM5Class) supportObject.getObjectClass();
            OwFNCM5ObjectFactory factory = new OwFNCM5PreferencesObjectFactory();
            Document nativeObject = (Document) supportObject.getNativeObject();

            OwFNCM5Object result = objectClass.from(nativeObject, factory);
            return result;
        }
        catch (Exception ex)
        {
            throw new OwServerException("Could not create Suport Object.", ex);
        }
    }
}
