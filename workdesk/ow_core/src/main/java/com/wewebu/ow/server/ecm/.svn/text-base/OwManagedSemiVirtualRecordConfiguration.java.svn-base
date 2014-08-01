package com.wewebu.ow.server.ecm;

import java.util.Collection;
import java.util.Map;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.role.OwRoleManager;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Cached collection of &lt;SemiVirtualRecordClassName&gt; XML configuration nodes 
 * as described by the EcmAdapter node in owbootstrap.xml.
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
 * @since 4.0.0.0
 */
public class OwManagedSemiVirtualRecordConfiguration
{

    private static Collection<String> findSelectiveConfigurationIds(OwRoleManager roleManager_p) throws OwConfigurationException
    {
        try
        {
            return roleManager_p.getAllowedResources(OwRoleManager.ROLE_CATEGORY_SELECTIVE_CONFIGURATION);
        }
        catch (OwConfigurationException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Could not create semi virtual folder classes.", e);
        }
    }

    public OwManagedSemiVirtualRecordConfiguration() throws OwConfigurationException
    {
    }

    /**
     * Retrieve the virtual folder configuration for a given object class.
     * 
     *  
     * @param className_p object class name  
     * @param roleManager_p
     * @param configuration_p
     * @return the {@link OwSemiVirtualRecordClass} corresponding to the given object class 
     *         or null if none was found or access to it was denied  
     * @throws OwConfigurationException
     * @throws OwException
     */
    public OwSemiVirtualRecordClass semiVirtualFolderForObjectClass(String className_p, OwRoleManager roleManager_p, OwXMLUtil configuration_p) throws OwException
    {

        Map<String, OwSemiVirtualRecordClass> semivirtualfolderMap = OwSemiVirtualRecordClass.createVirtualRecordClasses(configuration_p, findSelectiveConfigurationIds(roleManager_p));

        OwSemiVirtualRecordClass virtualFolderEntry = semivirtualfolderMap.get(className_p);
        if (virtualFolderEntry != null)
        {
            String virtualFolder = virtualFolderEntry.getVirtualFolder();
            try
            {
                if (roleManager_p != null)
                {
                    if (!roleManager_p.isAllowed(OwRoleManager.ROLE_CATEGORY_VIRTUAL_FOLDER, virtualFolder))
                    {
                        virtualFolderEntry = null;
                    }
                }
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not search semivirtual folder for object class " + className_p, e);
            }
        }

        return virtualFolderEntry;
    }
}
