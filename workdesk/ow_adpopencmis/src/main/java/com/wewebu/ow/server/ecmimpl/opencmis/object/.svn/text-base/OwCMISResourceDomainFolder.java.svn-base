package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDomainFolderClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDomainFolderClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISResourceObjectClassImpl;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Root folder of standard CMIS repositories.
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
@SuppressWarnings("unchecked")
public class OwCMISResourceDomainFolder extends OwCMISAbstractDomainFolder
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCMISResourceDomainFolder.class);

    private String name;
    private Collection<OwCMISSession> sessions;

    public OwCMISResourceDomainFolder(String name_p, Collection<OwCMISSession> sessions_p) throws OwException
    {
        this(new OwCMISDomainFolderClassImpl(), name_p, sessions_p);
    }

    /** constructs a CMIS domain root folder object wrapper
     *
     */
    public OwCMISResourceDomainFolder(OwCMISDomainFolderClass class_p, String name_p, Collection<OwCMISSession> sessions_p) throws OwException
    {
        super(class_p);
        this.name = name_p;
        this.sessions = sessions_p;
    }

    public String getName()
    {
        return name;
    }

    public String getMIMEType()
    {
        return "ow_root/cmis_domain";
    }

    public OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws OwException
    {
        for (int iType = 0; iType < iObjectTypes_p.length; iType++)
        {
            switch (iObjectTypes_p[iType])
            {
                case OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS:
                case OwObjectReference.OBJECT_TYPE_FOLDER:
                {
                    OwStandardObjectCollection children = new OwStandardObjectCollection();
                    children.addAll(createResourceObjects());
                    return children;
                }
            }
        }
        LOG.info("OwCMISDomainFolder.getChilds(...) returns null...");
        return null;
    }

    public int getChildCount(int[] objectTypes_p, int context_p) throws OwException
    {
        if (sessions == null || sessions.isEmpty())
        {
            throw new OwStatusContextException("");
        }
        else
        {
            return sessions.size();
        }
    }

    protected List<OwCMISObject> createResourceObjects() throws OwException
    {
        List<OwCMISObject> objects = new LinkedList<OwCMISObject>();
        for (OwCMISSession session : sessions)
        {
            objects.add(new OwCMISResourceObjectImpl(new OwCMISResourceObjectClassImpl(session)));
        }

        return objects;

    }

    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws OwException
    {

        return sessions != null && !sessions.isEmpty();
    }

    @Override
    public Collection<OwCMISSession> getNativeObject()
    {
        return sessions;
    }

    @Override
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws OwException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String getPath() throws OwException
    {
        return "/";
    }
}