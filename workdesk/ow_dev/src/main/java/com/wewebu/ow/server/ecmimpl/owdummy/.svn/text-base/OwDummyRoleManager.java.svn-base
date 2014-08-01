package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecmimpl.owdummy.log.OwLog;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.roleimpl.simplerole.OwSimpleRoleManager;
import com.wewebu.ow.server.util.OwStandardXMLUtil;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Dummy implementation for the RoleManager.<br/>
 * Used to simulate some roles / master roles.
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
public class OwDummyRoleManager extends OwSimpleRoleManager
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwDummyRoleManager.class);

    /** master roles defined ? */
    private boolean m_fsupportMasterRoles = false;

    /** collection of master role names */
    private Collection m_masterrolenames;

    /** filtered the roles through the selected master role group */
    private Collection m_filteredRoles;

    /** the role that is currently selected for the user */
    private String m_strMasterRole;

    /** init the manager, set context
     * 
     * optionally set a prefix to distinguish several different applications.
     * The rolemanager will filter the allowed plugins, MIME settings and design with the prefix.
     * The default is empty.
     * 
     * e.g. used for the Zero-Install Desktop Integration (ZIDI) to display a different set of plugins, MIME table and design for the Zero-Install Desktop Integration (ZIDI)
     * 
     * @param configNode_p OwXMLUtil node with configuration information
     * @param mainContext_p reference to the main app context of the application 
     * 
     * @exception OwException
     */
    public void init(OwRoleManagerContext mainContext_p, OwXMLUtil configNode_p) throws OwException
    {
        super.init(mainContext_p, configNode_p);

        try
        {
            m_fsupportMasterRoles = OwXMLDOMUtil.getSafeBooleanAttributeValue(getConfigNode().getSubNode("MasterRoles"), "enable", false);
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not create dummy role manager.", e);
        }
    }

    public void loginInit() throws Exception
    {
        super.loginInit();

        updateDesign();
    }

    /** get filtered roles through the selected master role group
     * 
     * 
     * @return Collection of String with filtered roles according to selected master role
     * */
    public Collection getFilteredRoles() throws Exception
    {
        if (null == m_filteredRoles)
        {
            if (hasMasterRoles())
            {
                // roles for current user
                Collection userroles = getRoles(getCurrentUser());

                Set allrolesdefinedbymaster = new HashSet();
                List masterroles = null;

                m_filteredRoles = new ArrayList();

                // === get the master role definition from bootstrap,
                // collect only those master roles
                // that the given user has roles defined for.
                List mastergroupnodes = getConfigNode().getSafeNodeList("MasterRoles");

                Iterator it = mastergroupnodes.iterator();
                while (it.hasNext())
                {
                    OwXMLUtil mastergroupwrapper = new OwStandardXMLUtil((Node) it.next());

                    // get the master role
                    String strMasterRoleName = mastergroupwrapper.getSafeStringAttributeValue("name", null);
                    if (strMasterRoleName == null)
                    {
                        String msg = "OwDummyRoleManager.getFilteredRoles: Define name in MasterRoleGroup.";
                        LOG.fatal(msg);
                        throw new OwConfigurationException(msg);
                    }

                    // get the group of roles defined with the master role
                    List roles = mastergroupwrapper.getSafeStringList();

                    allrolesdefinedbymaster.addAll(roles);

                    if (strMasterRoleName.equals(getMasterRole()))
                    {
                        masterroles = roles;
                    }
                }

                if (masterroles == null)
                {
                    String msg = "OwDummyRoleManager.getFilteredRoles: Masterrole is not defined, name = " + getMasterRole();
                    LOG.fatal(msg);
                    throw new OwConfigurationException(msg);
                }

                // get filtered roles
                Iterator itUser = userroles.iterator();
                while (itUser.hasNext())
                {
                    String strRole = (String) itUser.next();

                    if (masterroles.contains(strRole) || (!allrolesdefinedbymaster.contains(strRole)))
                    {
                        m_filteredRoles.add(strRole);
                    }
                }

            }
            else
            {
                m_filteredRoles = getRoles(getCurrentUser());
            }
        }

        return m_filteredRoles;
    }

    /**
     * checks if the current user designated by its credentials is allowed to use the given resource / function
     * 
     * @param iCategory_p of the requested function
     * @param strResourceID_p String ID of the requested resource (function / plugin id)
     *
     * @return true if user has permission, false if permission is denied
     */
    public boolean isAllowed(int iCategory_p, String strResourceID_p) throws Exception
    {
        Collection filteredmasterRoles = getFilteredRoles();

        switch (iCategory_p)
        {
            case ROLE_CATEGORY_STANDARD_FUNCTION:
                if (strResourceID_p.equals(STD_FUNC_CAN_EDIT_SITE_SETTINGS))
                {
                    // dummy implementation to simulate admin who can edit site settings
                    if (filteredmasterRoles.contains("Guest"))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }

                return true;

            case ROLE_CATEGORY_PLUGIN:
            {
                // allow Workdesk plugin for all
                if (strResourceID_p.equals("com.wewebu.ow.Workdesk"))
                {
                    return true;
                }
                // dummy implementation to simulate admin who can edit site settings
                if (filteredmasterRoles.contains("Administrators"))
                {
                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.admin"))
                    {
                        return true;
                    }

                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.Help"))
                    {
                        return true;
                    }

                    return false;
                }

                if (filteredmasterRoles.contains("Guest"))
                {
                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.admin"))
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }

                if (filteredmasterRoles.contains("Application_A"))
                {
                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.admin"))
                    {
                        return false;
                    }

                    if (strResourceID_p.startsWith("com.wewebu.ow.Record"))
                    {
                        return false;
                    }

                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.owinterop.jspview"))
                    {
                        return false;
                    }

                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.Demo"))
                    {
                        return false;
                    }

                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.bpm"))
                    {
                        return false;
                    }

                    return true;
                }

                if (filteredmasterRoles.contains("Application_B"))
                {
                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.admin"))
                    {
                        return false;
                    }

                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.owinterop.jspview"))
                    {
                        return false;
                    }

                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.Demo"))
                    {
                        return false;
                    }

                    if (strResourceID_p.equalsIgnoreCase("com.wewebu.ow.bpm"))
                    {
                        return false;
                    }

                    return true;
                }
            }

            case ROLE_CATEGORY_SELECTIVE_CONFIGURATION:
            {
                int lastPoint = strResourceID_p.lastIndexOf('.');
                if (lastPoint > -1)
                {
                    String optionId = strResourceID_p.substring(lastPoint);
                    if (optionId.equals(".B"))
                    {
                        return (filteredmasterRoles.contains("Application_B"));
                    }
                    else
                    {
                        return (false);
                    }
                }
                return (false);
            }

            default:
                return true;
        }
    }

    /**
     * get all resources the current user has access to for a given category
     * 
     * @param category_p int category for given user
     * 
     * @return Collection of String (resource IDs)
     */
    public Collection getAllowedResources(int category_p) throws Exception
    {
        // retrieve all resources in that category
        Collection allResources = getResources(category_p);

        // list of resources the user has access to
        ArrayList allowedResources = new ArrayList();

        // iterate over all resources and filter out those, the user has no access to
        Iterator resIt = allResources.iterator();
        while (resIt.hasNext())
        {
            String resourceName = (String) resIt.next();
            if (isAllowed(category_p, resourceName))
            {
                allowedResources.add(resourceName);
            }
        }

        // return list of allowed resources
        return (allowedResources);
    }

    /** get a list of roles that are assigned for the given user
     * 
     * @param userinfo_p OwBaseUserInfo
     * @return Collection of String, or null if user has no roles
     * */
    private Collection getRoles(OwBaseUserInfo userinfo_p) throws Exception
    {
        return userinfo_p.getRoleNames();
    }

    //  === Master role support.    
    /** get a list of roles that can be selected for the current user
     * 
     * @return Collection of String, or null if no roles can be selected
     * */
    public Collection getMasterRoles() throws Exception
    {
        if (m_fsupportMasterRoles && (m_masterrolenames == null))
        {
            // roles for current user
            Collection userroles = getRoles(getCurrentUser());

            m_masterrolenames = new ArrayList();

            // === get the master role definition from bootstrap,
            // collect only those master roles
            // that the given user has roles defined for.
            List mastergroupnodes = getConfigNode().getSafeNodeList("MasterRoles");

            Iterator it = mastergroupnodes.iterator();
            while (it.hasNext())
            {
                OwXMLUtil mastergroupwrapper = new OwStandardXMLUtil((Node) it.next());

                // get the master role
                String strMasterRoleName = mastergroupwrapper.getSafeStringAttributeValue("name", null);
                if (strMasterRoleName == null)
                {
                    String msg = "OwDummyRoleManager.getMasterRoles: Define name in MasterRoleGroup.";
                    LOG.fatal(msg);
                    throw new OwConfigurationException(msg);
                }

                // get the group of roles defined with the master role
                List roles = mastergroupwrapper.getSafeStringList();

                // get master roles for current user
                if (userroles.containsAll(roles))
                {
                    m_masterrolenames.add(strMasterRoleName);
                }
            }
        }

        return m_masterrolenames;
    }

    /** get a displayname for the given role
     * 
     * @param locale_p Locale to use
     * @param strRole_p String
     * @return String displayname for given role
     * */
    public String getMasterRoleDisplayName(java.util.Locale locale_p, String strRole_p) throws Exception
    {
        return strRole_p;
    }

    /** get the role that is currently selected for the user
     * 
     *  @return String role, or null if no roles can be selected
     * */
    public String getMasterRole() throws Exception
    {
        if (m_strMasterRole == null)
        {
            // === set default
            Collection masterRoles = getMasterRoles();
            if ((masterRoles != null) && (masterRoles.size() != 0))
            {
                m_strMasterRole = (String) masterRoles.iterator().next();
            }
        }

        return m_strMasterRole;
    }

    /** update the design name from db */
    protected void updateDesign() throws Exception
    {
        Collection filteredmasterRoles = getFilteredRoles();
        if (filteredmasterRoles.contains("Application_A"))
        {
            m_strDesign = "default41";
        }

        if (filteredmasterRoles.contains("Application_B"))
        {
            m_strDesign = "default41"; //was default20
        }
    }

    /** set the role that is currently selected for the user
     * 
     * @param strRole_p String
     * @return boolean true = role changed
     * */
    public boolean setMasterRole(String strRole_p) throws Exception
    {
        if (m_fsupportMasterRoles)
        {
            // change master role
            if (!strRole_p.equals(m_strMasterRole))
            {
                // check if master role is defined
                if (getMasterRoles().contains(strRole_p))
                {
                    m_strMasterRole = strRole_p;

                    // recreate filtered roles
                    m_filteredRoles = null;

                    // update design
                    updateDesign();

                    // update plugins
                    updateAllowedPlugins();

                    return true;
                }
                else
                {
                    LOG.error("OwDummyRoleManager.setMasterRole: Masterrole is not defined: " + strRole_p);
                }
            }
        }

        return false;
    }

    public boolean canExplicitDeny()
    {
        return true;
    }

    /** check if selectable roles are supported by rolemanager for the current user
     * 
     * @return boolean true = given user has roles
     * */
    public boolean hasMasterRoles()
    {
        try
        {
            return m_fsupportMasterRoles && (getMasterRoles().size() > 1);
        }
        catch (Exception e)
        {
            return false;
        }
    }
}