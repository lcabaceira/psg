package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecmimpl.owdummy.log.OwLog;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.mandator.OwMandator;
import com.wewebu.ow.server.mandator.OwMandatorManagerContext;

/**
 *<p>
 * Dummy implementation for mandators to specify the mandator configuration data.<br/><br/>
 * You get a instance of the mandator manager by calling getContext().getMandatorManager().getUserMandator() .
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
public class OwDummyMandator implements OwMandator
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwDummyMandator.class);

    private OwDummyMandatorManager m_manager;
    private OwBaseUserInfo m_owBaseUserInfo;

    /** construct simple mandator
     * 
     * @param manager_p
     * @param owBaseUserInfo_p 
     */
    public OwDummyMandator(OwDummyMandatorManager manager_p, OwBaseUserInfo owBaseUserInfo_p)
    {
        m_manager = manager_p;
        m_owBaseUserInfo = owBaseUserInfo_p;
    }

    /** get the context
     * 
     * @return an {@link OwMandatorManagerContext}
     */
    public OwMandatorManagerContext getContext()
    {
        return m_manager.getContext();
    }

    /** get a unique ID for this mandator
     * 
     * @return the <code>String</code> ID 
     */
    public String getID()
    {
        return m_owBaseUserInfo.getUserID();
    }

    /** get a name for this mandator
     * 
     * @return a <code>String</code>
     */
    public String getName()
    {
        try
        {
            return "DUMMY-MANDATOR-[" + m_owBaseUserInfo.getUserName() + "]";
        }
        catch (Exception e)
        {
            return "?";
        }
    }

    /** get a description for this mandator
     * 
     * @return a <cod>String</code>
     */
    public String getDescription()
    {
        return getContext().localize("app.OwDummyMandator.description", "Simple default client without attributes");
    }

    public int attributecount()
    {
        return 0;
    }

    public Object getAttribute(int index_p) throws Exception
    {
        String msg = "OwDummyMandator.getAttribute(int index_p): Not found Exception, index = " + index_p;
        LOG.debug(msg);
        throw new OwObjectNotFoundException(msg);
    }

    public Object getAttribute(String strName_p) throws Exception
    {
        // some test values for mandator manager dummy test
        if (getID().equals("B"))
        {
            if (strName_p.equals("BrowseTitle"))
            {
                return "Im Mandant B durchsuchen";
            }

            if (strName_p.equals("BrowseStartupFolder"))
            {
                return "/MandantB/";
            }

            if (strName_p.equals("Icon"))
            {
                return "dummy/folder.png";
            }

            if (strName_p.equals("RecentRecordList"))
            {
                return "false";
            }

            if (strName_p.equals("workonroot"))
            {
                return "true";
            }

            if (strName_p.equals("UseJSDateControl"))
            {
                return "false";
            }

            if (strName_p.equals("AdvancedChoiceLists"))
            {
                return "false";
            }

        }
        else
        {
            if (strName_p.equals("BrowseTitle"))
            {
                return "Durchsuchen";
            }

            if (strName_p.equals("BrowseStartupFolder"))
            {
                return "/";
            }

            if (strName_p.equals("Icon"))
            {
                return "dummy/folder.png";
            }

            if (strName_p.equals("RecentRecordList"))
            {
                return "true";
            }

            if (strName_p.equals("workonroot"))
            {
                return "false";
            }

            if (strName_p.equals("UseJSDateControl"))
            {
                return "true";
            }

            if (strName_p.equals("AdvancedChoiceLists"))
            {
                return "true";
            }
        }

        throw new OwObjectNotFoundException("OwDummyMandator.getAttribute(String strName_p): strName_p = " + strName_p);
    }

    public Collection getAttributeNames()
    {
        return new Vector();
    }

    public Object getSafeAttribute(String strName_p, Object default_p)
    {
        return default_p;
    }

    public boolean hasAttribute(String strName_p)
    {
        return false;
    }

    /** create a unique role name for the given role name 
     * 
     * @param rolename_p
     * @return String role name
     */
    public String filterRoleName(String rolename_p)
    {
        if (getID().equals("B"))
        {
            return "B." + rolename_p;
        }
        else
        {
            return rolename_p;
        }
    }
}