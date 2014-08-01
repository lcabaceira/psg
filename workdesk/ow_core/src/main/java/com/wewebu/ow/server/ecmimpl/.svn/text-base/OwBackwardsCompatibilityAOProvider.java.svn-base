package com.wewebu.ow.server.ecmimpl;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.ao.OwAOConfigurableProvider;
import com.wewebu.ow.server.ao.OwAOContext;
import com.wewebu.ow.server.ao.OwAOProvider;
import com.wewebu.ow.server.ao.OwAOType;
import com.wewebu.ow.server.ao.OwRoleManagedAOType;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.exceptions.OwAccessDeniedException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 *  AOProvider bound to the old, deprecated network application objects 
 *  retrieval methods implementation.
 *  Will work like it was defined by delegating to network
 *  to provide the application objects. 
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
 *@since 4.0.0.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OwBackwardsCompatibilityAOProvider implements OwAOConfigurableProvider
{
    /**
     *<p>
     * Compatible legacy networks should implement this interface.
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
     *@deprecated this interface is meant to support old network adapters, it should not be used for 
     *            for other purposes
     */
    public static interface OwLegacyAONetwork
    {
        /**
         *@deprecated this interface is meant to support old network adapters, it should not be used for 
         *            for other purposes
         * @return a non context based AO provider adaptation
         * @throws OwException
         */
        OwAOProvider getSafeAOProvider() throws OwException;
    }

    private OwAOProvider safeProvider;
    private OwRoleManager roleManager;
    private OwNetwork<?> network;

    public OwBackwardsCompatibilityAOProvider()
    {

    }

    public OwBackwardsCompatibilityAOProvider(OwNetwork adaptor, OwRoleManager roleManager) throws OwException
    {
        this.network = adaptor;
        this.roleManager = roleManager;
    }

    public <T> List<T> getApplicationObjects(OwAOType<T> type, String name, boolean forceSpecificObj) throws OwException
    {
        Collection col = null;
        try
        {
            col = getSafeProvider().getApplicationObjects(type, getDefaultLocation(type, name, null), forceSpecificObj);
            if (type instanceof OwRoleManagedAOType)
            {
                Collection<T> allowedCollection = new LinkedList<T>();
                OwRoleManagedAOType<T> roleManagedType = (OwRoleManagedAOType<T>) type;
                for (Iterator i = col.iterator(); i.hasNext();)
                {
                    T object = (T) i.next();
                    if (roleManager == null || roleManagedType.isAllowed(object, roleManager))
                    {
                        allowedCollection.add(object);
                    }
                }
                col = allowedCollection;
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Unknown Problem during application object retrieval", e);
        }

        return new LinkedList<T>(col);
    }

    public <T> T getApplicationObject(OwAOType<T> type, String name, boolean forceSpecificObj, boolean createNonExisting) throws OwException
    {
        Object answer = null;
        try
        {
            answer = getSafeProvider().getApplicationObject(type, name, forceSpecificObj, createNonExisting);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Unknown Problem during application object retrieval", e);
        }

        T tAnswer = (T) answer;
        if (type instanceof OwRoleManagedAOType)
        {
            OwRoleManagedAOType<T> roleManagedType = (OwRoleManagedAOType<T>) type;
            if (roleManager != null && !roleManagedType.isAllowed(tAnswer, roleManager))
            {
                throw new OwAccessDeniedException(new OwString1("app.OwAOProvider.acces.denied", "Access to the application object %1 was denied", name));
            }
        }

        return tAnswer;
    }

    public <T> T getApplicationObject(OwAOType<T> aoType, String name, List<Object> params, boolean forceSpecificObj, boolean createNonExisting) throws OwException
    {
        Object answer = null;
        try
        {
            answer = getSafeProvider().getApplicationObject(aoType, name, params, forceSpecificObj, createNonExisting);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Unknown Problem during application object retrieval", e);
        }
        return (T) answer;
    }

    /**
     * Return the default location for specific application object
     * @param aoType OwAOType specific application object type
     * @param name String name or location defined by retrieval (can be null)
     * @param params List of objects for additional information (can be null)
     * @return String location/path for specific objects
     */
    protected String getDefaultLocation(OwAOType<?> aoType, String name, List<Object> params)
    {
        String location = name;
        if (name == null)
        {
            int theType = aoType.getType();
            if (theType == OwAOTypesEnum.SEARCHTEMPLATE.type)
            {
                location = "owsearchtemplates";
            }
            else if (theType == OwAOTypesEnum.VIRTUAL_FOLDER.type)
            {
                location = "other";
            }
            else
            {
                location = name;
            }
        }
        return location;
    }

    private synchronized OwAOProvider getSafeProvider() throws OwConfigurationException
    {
        if (this.safeProvider == null)
        {
            try
            {
                this.safeProvider = ((OwLegacyAONetwork) network).getSafeAOProvider();
            }
            catch (Exception e)
            {
                throw new OwConfigurationException("Invalid AO configuration.", e);
            }
        }

        return this.safeProvider;
    }

    @Override
    public void init(OwXMLUtil configuration, OwAOContext context) throws OwConfigurationException
    {
        this.network = context.getNetwork();
        this.roleManager = context.isRoleManaged() ? context.getNetwork().getRoleManager() : null;
    }

}
