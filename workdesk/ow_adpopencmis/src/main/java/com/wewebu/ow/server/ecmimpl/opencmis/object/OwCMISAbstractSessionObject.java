package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISCapabilities;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResourceInfo;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISSessionObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Base object class for all CMIS objects.
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
public abstract class OwCMISAbstractSessionObject<S extends OwCMISSession, C extends OwCMISSessionObjectClass<S>> extends OwCMISAbstractObject<C> implements OwCMISSessionObject
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractSessionObject.class);

    private S session;

    public OwCMISAbstractSessionObject(C class_p) throws OwException
    {
        this(class_p.getSession(), class_p);
    }

    public OwCMISAbstractSessionObject(S session_p, C class_p) throws OwException
    {
        super(class_p);
        this.session = session_p;
    }

    public boolean canGetPermissions() throws OwException
    {
        OwCMISResourceInfo resourceInfo = session.getResourceInfo();
        OwCMISCapabilities capabilities = resourceInfo.getCapabilities();
        return capabilities.isCapabilityPermissions();
    }

    public boolean canSetPermissions() throws OwException
    {
        OwCMISResourceInfo resourceInfo = session.getResourceInfo();
        OwCMISCapabilities capabilities = resourceInfo.getCapabilities();
        return capabilities.isCapabilityManagePermissions();
    }

    public OwCMISObject createCopy(OwCMISObject copyParent_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, int[] childTypes_p) throws OwException
    {
        try
        {
            OwPropertyCollection propertiesToSet = createCopyProperties(properties_p, true);

            OwContentCollection contentCollection = null;
            if (hasContent(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                contentCollection = getContentCollection();
            }

            boolean major = true, keepCheckOut = false;
            if (getVersionSeries() != null)
            {
                major = getVersion().isMajor(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                keepCheckOut = getVersionSeries().getReservation() != null;
            }

            String copyDMSID = session.createObject(major, null, getCopyClassName(), propertiesToSet, permissions_p, contentCollection, copyParent_p, getMIMEType(), getMIMEParameter(), keepCheckOut);

            return session.getObject(copyDMSID, true);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {//Catching everything, maybe improve handling to inform of concrete problem?
            LOG.error("OwCMISObjectBase.createCopy(): Could not create basic object copy!", e);
            throw new OwServerException(new OwString("opencmis.object.OwCMISAbstractSessionObject.err.copy", "Copy error!"), e);
        }
    }

    protected OwPropertyCollection createCopyProperties(OwPropertyCollection newProperties_p, boolean all_p) throws OwException
    {
        if (!all_p && newProperties_p == null)
        {
            return null;
        }

        OwPropertyCollection properties = getProperties(null);
        OwPropertyCollection propertiesToSet = new OwStandardPropertyCollection();

        Set<?> propertiesEntries = all_p ? properties.entrySet() : newProperties_p.entrySet();
        for (Iterator<?> i = propertiesEntries.iterator(); i.hasNext();)
        {
            Map.Entry propertyEntry = (Entry) i.next();
            Object propertyKey = propertyEntry.getKey();
            OwProperty property = (OwProperty) propertyEntry.getValue();
            try
            {
                if (!property.isReadOnly(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) && !property.getPropertyClass().isSystemProperty())
                {

                    if (property.getValue() != null)
                    {
                        propertiesToSet.put(propertyKey, property);
                    }

                    if (all_p)
                    {
                        if (newProperties_p != null)
                        {

                            OwCMISPropertyClass propertyClass = (OwCMISPropertyClass) property.getPropertyClass();

                            OwProperty newProperty = null;

                            if (newProperties_p.containsKey(propertyClass.getFullQualifiedName().toString()))
                            {
                                newProperty = (OwProperty) newProperties_p.get(propertyClass.getFullQualifiedName().toString());
                            }
                            else if (newProperties_p.containsKey(propertyClass.getNonQualifiedName()))
                            {
                                newProperty = (OwProperty) newProperties_p.get(propertyClass.getNonQualifiedName());
                            }

                            if (newProperty != null)
                            {
                                propertiesToSet.put(propertyKey, newProperty);
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
                throw new OwServerException("Could not filter copy properties.", e);
            }
        }
        return propertiesToSet;
    }

    /**(overridable)
     * Helper to define corresponding type definition for new copy object.
     * @return String name of the type/object class which will be used for copy object.
     * @since 4.1.1.1
     */
    protected String getCopyClassName()
    {
        return this.getObjectClass().getCopyClassName();
    }

    @Override
    public OwCMISResource getResource()
    {
        return getSession().getResource();
    }

    @Override
    public S getSession()
    {
        return session;
    }

}