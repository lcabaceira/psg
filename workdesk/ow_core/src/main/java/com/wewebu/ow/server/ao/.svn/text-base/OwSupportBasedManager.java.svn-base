package com.wewebu.ow.server.ao;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Base class for application object managers that relay on 
 * {@link OwAOSupport}s for persistence.
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
public abstract class OwSupportBasedManager implements OwAOManager, OwConfigurableManager
{

    private static final String BASEPATH_PARAMETER = "basePath";

    private static final String SUPPORT_PARAMETER = "support";

    private OwAOSupport aoSupport;
    private String basePath;

    public OwSupportBasedManager()
    {

    }

    /**
     * 
     * @param aoSupport_p the underlying persistence support
     * @param basePath_p  path relative to the persistence support root of 
     *                   the managed objects' container   
     */
    public OwSupportBasedManager(OwAOSupport aoSupport_p, String basePath_p)
    {
        super();
        this.aoSupport = aoSupport_p;
        this.basePath = basePath_p;
    }

    /**
     * 
     * @param path_p
     * @return the base path relative form of the given path
     */
    private String toBasePathName(String path_p)
    {
        if (this.basePath != null && this.basePath.length() > 0)
        {
            return basePath + "/" + path_p;
        }
        else
        {
            return path_p;
        }
    }

    /**
     * 
     * @param strName_p
     * @param forceUserspecificObject_p
     * @param createIfNotExist_p
     * @return the persistent {@link OwObject} as provided by the current {@link OwAOSupport} 
     *         form a {@link #basePath} based location 
     * @throws OwException
     */
    protected OwObject getAOSupportObject(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return aoSupport.getSupportObject(toBasePathName(strName_p), forceUserspecificObject_p, createIfNotExist_p);
    }

    /**
     * 
     * @param strName_p
     * @param forceUserspecificObject_p
     * @param createIfNotExist_p
     * @return the persistent {@link OwObject} as provided by the current {@link OwAOSupport} 
     *         form a {@link #basePath} based location 
     * @throws OwException
     */
    protected OwObject[] getAOSupportObjects(String strName_p, boolean forceUserspecificObject_p, boolean createIfNotExist_p) throws OwException
    {
        return aoSupport.getSupportObjects(toBasePathName(strName_p), forceUserspecificObject_p, createIfNotExist_p);
    }

    @Override
    public String toString()
    {
        return "SupportBasedAOManager(basePath=" + this.basePath + ")->" + this.aoSupport + "";
    }

    @Override
    public void init(OwAOType<?> type, OwManagerConfiguration configuration, OwSupportsConfiguration supports, OwAOContext context) throws OwConfigurationException
    {
        try
        {
            String supportId = configuration.getParameterValue(SUPPORT_PARAMETER);
            aoSupport = supports.getSupport(supportId, OwAOSupport.class);
            basePath = configuration.getParameterValue(BASEPATH_PARAMETER);
        }
        catch (OwInvalidOperationException e)
        {
            throw new OwConfigurationException("Bad sdupport reference.", e);
        }
    }
}
