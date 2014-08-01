package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResourceInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimpleDMSID;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISResourceObjectClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * OwCMISResourceObjectImpl.
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
public class OwCMISResourceObjectImpl extends OwCMISAbstractSessionObject<OwCMISSession, OwCMISResourceObjectClass> implements OwCMISResourceObject
{

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCMISResourceObjectImpl.class);

    public OwCMISResourceObjectImpl(OwCMISResourceObjectClass class_p) throws OwException
    {
        super(class_p);
    }

    @Override
    public String getDMSID()
    {
        OwCMISSession mySession = getSession();
        OwCMISResourceInfo resourceInfo = getResourceInfo();
        String rootId = "NA";

        try
        {
            //TODO: should not be root folder dependent - should be unique

            OwCMISObject root = getRootFolder();
            rootId = root.getID();
        }
        catch (OwException e)
        {
            LOG.error("Error creating DMSID ", e);
        }

        return OwCMISSimpleDMSID.createDMSID(mySession.getDMSIDDecoder().getDMSIDPrefix(), resourceInfo.getId(), rootId);
    }

    @Override
    public String getName()
    {
        OwCMISResourceInfo resourceInfo = getSession().getResourceInfo();
        return resourceInfo.getDisplayName();

    }

    @Override
    public String getID()
    {
        OwCMISResourceInfo resourceInfo = getResourceInfo();
        return resourceInfo.getId();
    }

    @Override
    public String getNativeID()
    {
        return this.getID();
    }

    @Override
    public int getChildCount(int[] objectTypes_p, int context_p) throws OwException
    {
        OwCMISObject rootFolder = getRootFolder();
        return rootFolder.getChildCount(objectTypes_p, context_p);
    }

    @Override
    public OwObjectCollection getChilds(int[] objectTypes_p, Collection propertyNames_p, OwSort sort_p, int maxSize_p, int versionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        OwCMISObject rootFolder = getRootFolder();
        return rootFolder.getChilds(objectTypes_p, propertyNames_p, sort_p, maxSize_p, versionSelection_p, filterCriteria_p);
    }

    @Override
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws OwException
    {
        OwCMISObject rootFolder = getRootFolder();
        return rootFolder.hasChilds(iObjectTypes_p, iContext_p);
    }

    @Override
    public OwCMISSession getNativeObject()
    {
        return getSession();
    }

    @Override
    public OwCMISObject getRootFolder() throws OwException
    {
        OwCMISSession mySession = getSession();
        return mySession.getRootFolder();
    }

    @Override
    public OwCMISResourceInfo getResourceInfo()
    {
        OwCMISSession mySession = getSession();
        return mySession.getResourceInfo();
    }

    @Override
    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws OwException
    {
        //void
    }

    @Override
    public final OwVersionSeries getVersionSeries() throws Exception
    {
        return null;
    }

    @Override
    public final OwVersion getVersion() throws Exception
    {
        return null;
    }

    @Override
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
        //void
    }

    @Override
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

}