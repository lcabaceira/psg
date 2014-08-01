package com.wewebu.ow.server.ecm.ui;

import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwRole;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Base Class for the user / group / role select sub module to be created in the network (ECM) Adapter. 
 * Submodules are used to delegate ECM specific user interactions to the ECM Adapter, which can not be generically solved.<br/>
 * e.g.: Login or Access rights Dialog.<br/><br/>
 * To be implemented with the specific ECM system.
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
public class OwUIUserSelectModul extends OwUISubModul
{
    /** logger */
    private static Logger LOG = OwLogCore.getLogger(OwUIUserSelectModul.class);

    /** filter type users, used in OwNetwork.getUserSelectSubModul */
    public static final int TYPE_USER = 1;
    /** filter type groups, used in OwNetwork.getUserSelectSubModul */
    public static final int TYPE_GROUP = 2;
    /** filter type groups, used in OwNetwork.getUserSelectSubModul */
    public static final int TYPE_ROLE = 3;

    /** the event listener for the sub module */
    private OwEventListner m_listener;

    /** determine if multiple selection is allowed */
    private boolean m_fMultiselect = false;

    /**
     *<p>
     * Event listener interface for the user select module.
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
    public abstract interface OwEventListner extends EventListener
    {
        /** called when user selected a role 
         * @param roleNames_p list of String
         */
        public abstract void onSelectRoles(java.util.List roleNames_p) throws Exception;

        /** called when user selected a group or user 
         * @param users_p List of OwUserInfo
         */
        public abstract void onSelectUsers(java.util.List users_p) throws Exception;
    };

    /** get the multi select property
     * @return true = allow multi selection, false = only single select
     */
    public boolean getMultiselect()
    {
        return m_fMultiselect;
    }

    /** set the multi select property
     * @param fMultiselect_p  true = allow multi selection, false = only single select
     */
    public void setMultiselect(boolean fMultiselect_p)
    {
        m_fMultiselect = fMultiselect_p;
    }

    /** get the event listener for the sub module 
     * @return OwUIUserSelectView.OwEventListner derived listener
     */
    public OwEventListner getEventListner()
    {
        return m_listener;
    }

    /** set the event listener for the sub module 
     * @param listener_p OwUIUserSelectView.OwEventListner derived listener
     */
    public void setEventListner(OwEventListner listener_p)
    {
        m_listener = listener_p;
    }

    /**
     * 
     * @return an array String Alfresco Workdesk group IDs (egg. OW_Authenticated) 
     */
    protected String[] getDefaultRoleNames()
    {
        String[] defaultRoleNames = new String[1];
        defaultRoleNames[0] = OwRole.OW_AUTHENTICATED;
        return defaultRoleNames;
    }

    /**
     * 
     * @param searchCriteria_p search criteria string, must not be null and 
     *                         can contain the '*' (any character sequence) wildcard
     * @return a List of default role names that match the given search criteria
     */
    protected List searchDefaultRoleNames(String searchCriteria_p)
    {
        List defaultRoles = new LinkedList();

        try
        {
            String regExpSearchCriteria = searchCriteria_p == null ? "" : searchCriteria_p;
            regExpSearchCriteria = regExpSearchCriteria.length() == 0 ? regExpSearchCriteria : (regExpSearchCriteria + "*");
            regExpSearchCriteria = regExpSearchCriteria.replaceAll("\\*", ".*");

            Pattern p = Pattern.compile(regExpSearchCriteria);

            String[] defaultRoleNames = getDefaultRoleNames();
            for (int i = 0; i < defaultRoleNames.length; i++)
            {
                Matcher m = p.matcher(defaultRoleNames[i]);
                if (m.matches())
                {
                    defaultRoles.add(defaultRoleNames[i]);
                }
            }
        }
        catch (PatternSyntaxException e)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwUIUserSelectModul.searchRoleNames: Cannot compile the given expression from search criteria. The default roles will be be skipped. Reason: ", e);
            }
            defaultRoles.clear();
        }

        return defaultRoles;
    }
}