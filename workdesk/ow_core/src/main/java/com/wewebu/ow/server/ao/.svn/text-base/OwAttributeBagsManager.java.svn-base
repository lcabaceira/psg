package com.wewebu.ow.server.ao;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwAttributeBagsSupport;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Manager for {@link OwAttributeBag} application objects.<br/><br/>
 * Returns {@link OwAttributeBag} objects.<br/>
 * Retrieves only single objects.<br/>
 * Supports parameterized retrieval through 
 * {@link #getApplicationObject(String, Object, boolean, boolean)}- parameter must be 
 * the String user ID.<br/>
 * Only user specific retrievals are supported.
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
public class OwAttributeBagsManager implements OwAOManager, OwConfigurableManager
{
    private static final String INVERTED_PARAMETER = "inverted";

    private static final String SUPPORT_PARAMETER = "support";

    private static final Logger LOG = OwLogCore.getLogger(OwAttributeBagsManager.class);

    private OwAOType<?> aoType;
    private OwAttributeBagsSupport attributeBagsSupport;
    private boolean nameKey;

    private OwNetworkContext networkContext;

    public OwAttributeBagsManager()
    {
        //void
    }

    /**
     * Constructor
     * @param type_p integer application objects code (usually one 
     * of {@link OwNetwork#APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE} or {@link OwNetwork#APPLICATION_OBJECT_TYPE_INVERTED_ATTRIBUTE_BAG}). 
     * @param nameKey_p if <code>true</code>  retrieve inverted writable attribute bags based an a attribute name key<br>
     *                  if <code>false</code> retrieve writable attribute bags based an a user key
     * @param attributeBagsSupport_p the attribute bag persistence support to be used
     */
    public OwAttributeBagsManager(OwNetworkContext networkContext_p, OwAOType<?> type_p, boolean nameKey_p, OwAttributeBagsSupport attributeBagsSupport_p)
    {
        initialize(networkContext_p, type_p, nameKey_p, attributeBagsSupport_p);
    }

    private void initialize(OwNetworkContext networkContext_p, OwAOType<?> type_p, boolean nameKey_p, OwAttributeBagsSupport attributeBagsSupport_p)
    {
        this.aoType = type_p;
        this.nameKey = nameKey_p;
        this.attributeBagsSupport = attributeBagsSupport_p;
        this.networkContext = networkContext_p;
    }

    public Object getApplicationObject(String strName_p, Object param_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        if (!forceUserSpecificObject_p)
        {
            LOG.error("OwSimpleAttributeBagManager.getApplicationObject(): Attribute Bags are always user specific, set forceUserSpecificObject_p = true");
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwAOManager.invalid.application.object.request", "Invalid application object request!"));
        }
        try
        {
            if (this.nameKey)
            {
                return this.attributeBagsSupport.getNameKeyAttributeBag(this.networkContext, strName_p, (String) param_p);
            }
            else
            {
                return this.attributeBagsSupport.getUserKeyAttributeBagWriteable(this.networkContext, strName_p, (String) param_p);
            }
        }
        catch (Exception e)
        {
            LOG.error("OwSimpleAttributeBagManager.getApplicationObject(): Attribute Bags are always user specific, set forceUserSpecificObject_p = true", e);
            throw new OwInvalidOperationException(new OwString("ecmimpl.OwAOManager.invalid.application.object.request", "Invalid application object request!"), e);
        }
    }

    public Object getApplicationObject(String strName_p, boolean forceUserSpecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return getApplicationObject(strName_p, null, forceUserSpecificObject_p, createIfNotExist_p);
    }

    public Collection<?> getApplicationObjects(String strName_p, boolean forceUserSpecificObject_p) throws OwException
    {
        LOG.fatal("OwAttributeBagsManager.getApplicationObjects():Not supported application object request!Could not retrieve attribute bag objects - not implemented !");
        throw new OwNotSupportedException(new OwString("ecmimpl.OwAOManager.not.supported.application.object.request", "Not supported application object request!"));
    }

    @Override
    public final int getManagedType()
    {
        return this.aoType.getType();
    }

    //    @Override     will be introduced together when AOW managers refactoring
    public final OwAOType<?> getType()
    {
        return aoType;
    }

    @Override
    public String toString()
    {
        return "OwSimpleAttributeBagManager(type=" + this.aoType.getType() + ")->" + this.attributeBagsSupport;
    }

    @Override
    public void init(OwAOType<?> type, OwManagerConfiguration configuration, OwSupportsConfiguration supports, OwAOContext context) throws OwConfigurationException
    {
        try
        {
            String supportId = configuration.getParameterValue(SUPPORT_PARAMETER);
            OwAttributeBagsSupport support = supports.getSupport(supportId, OwAttributeBagsSupport.class);
            String invertedValue = configuration.getParameterValue(INVERTED_PARAMETER);
            boolean inverted = Boolean.parseBoolean(invertedValue);
            initialize(context.getNetwork().getContext(), type, inverted, support);

        }
        catch (OwInvalidOperationException e)
        {
            throw new OwConfigurationException("Bad sdupport reference.", e);
        }
    }

}
