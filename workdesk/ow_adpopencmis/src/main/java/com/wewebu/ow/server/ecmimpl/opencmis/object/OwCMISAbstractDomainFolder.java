package com.wewebu.ow.server.ecmimpl.opencmis.object;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimpleDMSIDDecoder;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISDomainFolderClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Domain folder implementation base class.
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
public abstract class OwCMISAbstractDomainFolder extends OwCMISAbstractObject<OwCMISDomainFolderClass> implements OwCMISDomainFolder
{

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractDomainFolder.class);

    private OwPropertyCollection m_properties;

    /** String representing DMSID, which is create on request
     * @see #getDMSID() getDMSID() */
    private String m_dmsid;

    public OwCMISAbstractDomainFolder(OwCMISDomainFolderClass class_p) throws OwException
    {
        super(class_p);
        m_properties = new OwStandardPropertyCollection();
    }

    /**
     * Creates a DMSID for this domain folder.<br />
     * The ID of this object is used {@link #getID()} and attached as hash code
     * to the DMSID representation.<br />
     * Like: <code>"DMS_PREFIX,ResourceID,"+Integer.toString(getID().hashCode())</code>
     * @return String representing an DMSID.
     */
    public synchronized String getDMSID()
    {
        if (m_dmsid == null)
        {
            m_dmsid = OwCMISSimpleDMSIDDecoder.DMS_PREFIX + "," + getName() + "," + Integer.toString(getID().hashCode());
        }

        return m_dmsid;

    }

    public final String getID()
    {
        //TODO: ?!?!?
        return getName(); //+ super.hashCode();
    }

    @Override
    public String getNativeID()
    {
        return getID();
    }

    public OwPropertyCollection getProperties(Collection propertyNames_p) throws OwException
    {
        OwPropertyCollection properties = super.getVirtualProperties(propertyNames_p);
        OwStandardPropertyCollection allProperties = new OwStandardPropertyCollection();
        allProperties.putAll(properties);
        allProperties.putAll(m_properties);
        checkPropertyCollection(propertyNames_p, allProperties);
        return allProperties;
    }

    @Override
    public OwCMISProperty<?> getProperty(String propertyName_p) throws OwException
    {
        OwCMISProperty<?> property = (OwCMISProperty<?>) m_properties.get(propertyName_p);
        if (property == null)
        {
            return super.getProperty(propertyName_p);
        }
        else
        {
            return property;
        }
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
    public boolean canDelete(int iContext_p) throws Exception
    {
        return false;
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

    @Override
    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return false;
    }

    @Override
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws OwException
    {
        // void

    }

    @Override
    public OwCMISResource getResource()
    {
        return null;
    }

    @Override
    public String getResourceID()
    {
        return null;
    }

}
