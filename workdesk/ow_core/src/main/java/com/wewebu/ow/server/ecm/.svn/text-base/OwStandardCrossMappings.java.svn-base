package com.wewebu.ow.server.ecm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwAuthenticationException;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwSort.OwSortCriteria;
import com.wewebu.ow.server.field.OwStandardDecoratorSearchCriteria;
import com.wewebu.ow.server.field.OwStandardDecoratorSearchNode;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implements a configurable mapping of user's and properties for federated cross adapter scenarios.
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
public class OwStandardCrossMappings implements OwExtendedCrossMappings
{

    public static class OwCrossSort extends OwSort
    {

        private OwSort sort;

        public OwCrossSort(OwSort sort)
        {
            super();
            this.sort = sort;
        }

    }

    /**
     *<p>
     * Wrapper class to map the property names.
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
    public static class OwCrossDecoratorSearchNode extends OwStandardDecoratorSearchNode
    {
        /**
         *<p>
         * OwWrappedSearchCriteria.
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
        public static class OwWrappedSearchCriteria extends OwStandardDecoratorSearchCriteria
        {
            private OwSearchCriteria m_searchcriteria;
            private OwCrossMappings m_crossMappings;
            private OwNetwork m_xNetwork;
            private String m_xResourceID;

            public OwWrappedSearchCriteria(OwSearchCriteria searchcriteria_p, OwCrossMappings crossMappings_p, OwNetwork xNetwork_p, String xResourceID_p)
            {
                super();
                m_searchcriteria = searchcriteria_p;
                m_crossMappings = crossMappings_p;
                m_xNetwork = xNetwork_p;
                m_xResourceID = xResourceID_p;
            }

            public OwSearchCriteria getWrappedCriteria()
            {
                return m_searchcriteria;
            }

            @Override
            public OwFieldDefinition getFieldDefinition() throws Exception
            {
                OwFieldDefinition superFieldDefinition = super.getFieldDefinition();
                String superClassName = superFieldDefinition.getClassName();
                String xClassName = m_crossMappings.getXProperty(superClassName);
                if (!superClassName.equals(xClassName))
                {
                    return this.m_xNetwork.getFieldDefinition(xClassName, m_xResourceID);
                }
                else
                {
                    LOG.warn("Cross incompatible field definition " + superFieldDefinition.getClassName() + " " + superFieldDefinition.getClass());
                    return superFieldDefinition;
                }
            }

            public OwSearchCriteria getSecondRangeCriteria()
            {
                return new OwWrappedSearchCriteria(super.getSecondRangeCriteria(), m_crossMappings, m_xNetwork, m_xResourceID);
            }

            @Override
            public Object getValue()
            {
                Object superValue = super.getValue();
                if (getUniqueName().equals(OwStandardClassSelectObject.CLASS_NAME))
                {

                    Object[] classes = (Object[]) superValue;
                    Object[] classesValue = new Object[classes.length];
                    for (int i = 0; i < classesValue.length; i++)
                    {
                        if (classes[i] instanceof OwClass)
                        {
                            OwClass clazz = (OwClass) classes[i];
                            try
                            {
                                String crossBaseClassName = ((OwExtendedCrossMappings) m_crossMappings).getXClass(clazz.getBaseClassName());
                                String crossClassName = ((OwExtendedCrossMappings) m_crossMappings).getXClass(clazz.getClassName());
                                classesValue[i] = new OwClass(clazz.getObjectType(), crossClassName, clazz.isEnabled(), clazz.isIncludeSubclasses());
                            }
                            catch (OwConfigurationException e)
                            {
                                LOG.error("Could not cross convert search class " + clazz.getClassName(), e);
                                classesValue[i] = clazz;
                            }
                        }
                        else
                        {
                            classesValue[i] = classes[i];
                        }
                    }

                    return classesValue;

                }
                else
                {
                    try
                    {
                        OwFieldDefinition superFieldDefinition = super.getFieldDefinition();
                        OwFieldDefinition thisFieldDefinition = getFieldDefinition();

                        return m_crossMappings.convert(superValue, superFieldDefinition, thisFieldDefinition);

                    }
                    catch (Exception e)
                    {
                        LOG.error("Error converting search value .", e);
                        return superValue;
                    }
                }

            }

            public String getClassName()
            {
                try
                {
                    String className = super.getClassName();
                    return m_crossMappings.getXProperty(className);
                }
                catch (OwConfigurationException e)
                {
                    return super.getClassName();
                }
            }

            public String getUniqueName()
            {
                try
                {
                    String uName = super.getUniqueName();
                    return m_crossMappings.getXProperty(uName);
                }
                catch (OwConfigurationException e)
                {
                    return super.getUniqueName();
                }
            }
        }

        private OwSearchNode m_searchnode;
        private OwCrossMappings m_crossMappings;
        private OwNetwork m_xNetwork;
        private String m_xResourceID;

        public OwCrossDecoratorSearchNode(OwSearchNode searchnode_p, OwCrossMappings crossMappings_p, OwNetwork xNetwork_p, String xResourceID_p)
        {
            super();
            m_searchnode = searchnode_p;
            m_crossMappings = crossMappings_p;
            m_xNetwork = xNetwork_p;
            m_xResourceID = xResourceID_p;
        }

        public OwSearchNode getWrappedSearchNode()
        {
            return m_searchnode;
        }

        public OwSearchNode findSearchNode(int nodeType_p)
        {
            return createCrossDecoratorSearchNode(super.findSearchNode(nodeType_p), m_crossMappings, m_xNetwork, m_xResourceID);
        }

        public List getChilds()
        {
            List ret = new ArrayList();

            List children = super.getChilds();
            if (children != null)
            {
                // wrap all children
                Iterator it = children.iterator();
                while (it.hasNext())
                {
                    OwSearchNode n = (OwSearchNode) it.next();

                    ret.add(createCrossDecoratorSearchNode(n, m_crossMappings, m_xNetwork, m_xResourceID));
                }
            }
            return ret;
        }

        public OwSearchCriteria createWrappedCriteria(OwSearchCriteria criteria_p)
        {
            if (criteria_p == null)
            {
                return null;
            }
            else
            {
                return new OwWrappedSearchCriteria(criteria_p, m_crossMappings, m_xNetwork, m_xResourceID);
            }
        }

        /**
         * factory method for decorator search node.
         * @param searchnode_p
         * @param crossMappings_p
         * @return new OwStandardDecoratorSearchNode
         */
        public OwStandardDecoratorSearchNode createCrossDecoratorSearchNode(OwSearchNode searchnode_p, OwCrossMappings crossMappings_p, OwNetwork xNetwork_p, String xResourceID_p)
        {
            if (searchnode_p == null)
            {
                return null;
            }
            else
            {
                return new OwCrossDecoratorSearchNode(searchnode_p, crossMappings_p, xNetwork_p, xResourceID_p);
            }
        }
    }

    /** wrapper class to map the property names
     *
     */
    private static class OwCrossDecoratorObject extends OwStandardDecoratorObject
    {

        private OwObject m_object;
        private OwCrossMappings m_crossMappings;

        public OwCrossDecoratorObject(OwObject object_p, OwCrossMappings crossMappings_p)
        {
            super();
            m_object = object_p;
            m_crossMappings = crossMappings_p;
        }

        public OwObject getWrappedObject()
        {
            return m_object;
        }

        public OwProperty getProperty(String strPropertyName_p) throws Exception
        {
            String xprop = m_crossMappings.getXProperty(strPropertyName_p);
            return super.getProperty(xprop);
        }

    }

    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardCrossMappings.class);

    /**
     *<p>
     * OwCrossLogin.
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
    private static class OwCrossLogin
    {
        private String m_user;
        private String m_password;

        public OwCrossLogin(String user_p, String password_p)
        {
            m_user = user_p;
            m_password = password_p;
        }

        public String getUser()
        {
            return m_user;
        }

        public String getPassword()
        {
            return m_password;
        }

    }

    private OwXMLUtil m_confignode;

    /** map of property names */
    private Map m_propertymappings;

    /** the user mappings from configuration */
    private Map m_usermappings;

    /** the group mappings from configuration */
    private Map m_groupmappings;

    /** the default login for all unmapped users, groups */
    private OwCrossLogin m_defaultlogin;

    /** map of property names */
    private Map<String, String> m_xClassMappings;

    private Map<String, String> m_internalPropertyMappings;

    private Map<String, String> m_iClassMappings;

    /** creates a mapping with given configuration
     * 
     * @param config_p
     */
    public OwStandardCrossMappings(OwXMLUtil config_p)
    {
        m_confignode = config_p;
    }

    protected OwXMLUtil getConfigNode()
    {
        return m_confignode;
    }

    /** the default login for all unmapped users, groups
     * 
     * @return an {@link com.wewebu.ow.server.ecm.OwStandardCrossMappings.OwCrossLogin}
     */
    protected OwCrossLogin getDefaultlogin()
    {
        return m_defaultlogin;
    }

    /** get the user mappings from configuration
     * 
     * @return Map of user names to x-user name and x-password tuple
     */
    protected Map getUsermappings()
    {
        if (null == m_usermappings)
        {
            createUserGroupMappings();
        }

        return m_usermappings;
    }

    /** get the group mappings from configuration
     * 
     * @return Map of group names to x-user name and x-password tuple
     * @throws Exception 
     */
    protected Map getGroupmappings() throws Exception
    {
        if (null == m_groupmappings)
        {
            createUserGroupMappings();
        }

        return m_groupmappings;
    }

    /** map of property names
     * 
     * @return a {@link Map}
     * @throws OwConfigurationException 
     */
    protected Map getPropertyMappings() throws OwConfigurationException
    {
        if (null == m_propertymappings)
        {
            m_propertymappings = new HashMap();

            Iterator it = getConfigNode().getSafeUtilList("propertymappings", "propertymap").iterator();
            while (it.hasNext())
            {
                OwXMLUtil propertymap = (OwXMLUtil) it.next();

                String name = propertymap.getSafeStringAttributeValue("name", null);
                String xname = propertymap.getSafeTextValue(null);

                if ((name == null) || (xname == null))
                {
                    String msg = "OwStandardCrossMappings.getPropertyMappings: name undefined, please define name attribute and x-namevalue in propertymap tag.";
                    LOG.fatal(msg);
                    throw new OwConfigurationException(msg);
                }

                m_propertymappings.put(name, xname);
            }
        }

        return m_propertymappings;
    }

    /** create maps for group and user logins
     * 
     */
    private void createUserGroupMappings()
    {
        m_usermappings = new HashMap();
        m_groupmappings = new HashMap();

        Iterator it = getConfigNode().getSafeUtilList("usermappings", "usermap").iterator();
        while (it.hasNext())
        {
            OwXMLUtil usermapp = (OwXMLUtil) it.next();

            String user = usermapp.getSafeStringAttributeValue("name", null);
            String group = usermapp.getSafeStringAttributeValue("group", null);

            String xuser = usermapp.getSafeTextValue("user", null);
            String xpassword = usermapp.getSafeTextValue("password", null);

            // add to map
            if (null != user)
            {
                m_usermappings.put(user, new OwCrossLogin(xuser, xpassword));
            }
            else if (null != group)
            {
                m_groupmappings.put(group, new OwCrossLogin(xuser, xpassword));
            }
            else
            {
                m_defaultlogin = new OwCrossLogin(xuser, xpassword);
            }
        }
    }

    /** map the given property names
     * 
     * @param propertyNames_p
     * @return a {@link Collection}
     * @throws OwConfigurationException 
     */
    public Collection getXProperties(Collection propertyNames_p) throws OwConfigurationException
    {
        Collection ret = new Vector();

        Iterator it = propertyNames_p.iterator();
        while (it.hasNext())
        {
            String name = (String) it.next();
            String xname = (String) getPropertyMappings().get(name);

            if (null == xname)
            {
                ret.add(name);
            }
            else
            {
                ret.add(xname);
            }
        }

        return ret;
    }

    /** Login to the X network.
     * Checking first the definition of user to user mapping for x-Network login, if no user to user mapping could be found
     * the configuration of user group to user is searched.
     * 
     * @param xnetwork_p OwNetwork reference where should be logged in, using the mappings
     * @param parentnetwork_p OwNetwork where to retrieve additional information about the user
     * @param parentuser_p String representing the user
     * @param parentpassword_p String containing the user password
     * @throws OwAuthenticationException
     * @throws OwServerException
     */
    public void doXLogin(OwNetwork xnetwork_p, OwNetwork parentnetwork_p, String parentuser_p, String parentpassword_p) throws OwAuthenticationException, OwServerException
    {
        OwCrossLogin login = (OwCrossLogin) getUsermappings().get(parentuser_p);

        if (login == null)
        {
            LOG.info("OwStandardCrossMappings.doXLogin: No usermapping found, searching through group mapping.");
            try
            {
                OwUserInfo user = parentnetwork_p.getCredentials().getUserInfo();
                Iterator it = user.getGroups().iterator();
                while (it.hasNext() && login == null)
                {
                    OwUserInfo group = (OwUserInfo) it.next();
                    LOG.debug("OwStandardCrossMappings.doXLogin: Request group mapping displayname = " + group.getUserDisplayName());
                    login = (OwCrossLogin) getGroupmappings().get(group.getUserDisplayName());
                    if (login == null)
                    {//look for long name definition
                        LOG.debug("OwStandardCrossMappings.doXLogin: Request group mapping long name = " + group.getUserLongName());
                        login = (OwCrossLogin) getGroupmappings().get(group.getUserLongName());
                    }
                    if (login == null)
                    {//look for short name definition
                        LOG.debug("OwStandardCrossMappings.doXLogin: Request group mapping short name = " + group.getUserShortName());
                        login = (OwCrossLogin) getGroupmappings().get(group.getUserShortName());
                    }
                    if (login == null)
                    {//fall back for older UserInfo implementation
                        LOG.debug("OwStandardCrossMappings.doXLogin: Request group mapping getUserName = " + group.getUserName());
                        login = (OwCrossLogin) getGroupmappings().get(group.getUserName());
                    }
                }
            }
            catch (Exception e)
            {
                LOG.error("Could not retrieve user info for groupinformation", e);
            }
        }

        if (login == null)
        {
            try
            {
                if (getDefaultlogin() == null && getUsermappings().isEmpty() && getGroupmappings().isEmpty())
                {
                    LOG.debug("OwStandardCrossMappings.doXLogin: No mappings defined pass through credentials to XNetwork.");
                    login = new OwCrossLogin(parentuser_p, parentpassword_p);
                }
                else
                {
                    LOG.debug("OwStandardCrossMappings.doXLogin: No mappings found getting default login.");
                    login = getDefaultlogin();
                }
            }
            catch (Exception e)
            {
                String msg = "OwStandardCrossMappings.doXLogin: Cannot retrieve group mappings!";
                LOG.fatal(msg, e);
                throw new OwServerException(msg, e);
            }
        }

        if (login == null)
        {
            String msg = "OwStandardCrossMappings.doXLogin: No login found for X adapter = " + xnetwork_p.getDMSPrefix();
            LOG.fatal(msg);
            throw new OwServerException(msg);
        }

        try
        {
            xnetwork_p.loginDefault(login.getUser(), login.getPassword());
        }
        catch (Exception e)
        {
            String msg = "Your login information is invalid or the X adapter = " + xnetwork_p.getDMSPrefix() + " is not available. Please make sure that name and password are correct and the X adapter is running. "
                    + "Please also check the <usermappings> configuration entry for user Administrator, member of groups...";
            LOG.debug(msg, e);
            throw new OwAuthenticationException(msg, e);
        }
    }

    /** map the given property name
     * 
     * @param name_p
     * @return a {@link String}
     * @throws OwConfigurationException
     */
    public String getXProperty(String name_p) throws OwConfigurationException
    {
        String ret = (String) getPropertyMappings().get(name_p);

        if (null == ret)
        {
            return name_p;
        }
        else
        {
            return ret;
        }
    }

    /** replace the objects of the given collection with mapped wrapper objects
     * 
     * @param objects_p
     */
    public void mapXObjectCollection(OwObjectCollection objects_p)
    {
        // wrap the returned objects so properties can be mapped
        for (int i = 0; i < objects_p.size(); i++)
        {
            // exchange object by cross wrapper
            OwObject obj = (OwObject) objects_p.get(i);

            OwObject crossobj = new OwCrossDecoratorObject(obj, this);

            objects_p.set(i, crossobj);
        }

    }

    /** map the given search
     * 
     * @param searchNode_p
     * @return an {@link OwSearchNode}
     * @throws CloneNotSupportedException 
     */
    public OwSearchNode getXSearch(OwSearchNode searchNode_p, String xRepositoryID_p, OwNetwork xNetwork_p) throws CloneNotSupportedException
    {
        return new OwCrossDecoratorSearchNode(searchNode_p, this, xNetwork_p, xRepositoryID_p);
    }

    public OwSort getXSort(OwSort sort_p) throws CloneNotSupportedException, OwConfigurationException
    {
        Collection<OwSortCriteria> criteriaCollection = sort_p.getCriteriaCollection();
        OwSort xSort = new OwSort(sort_p.getMaxSize(), sort_p.getDefaultAsc());
        for (OwSortCriteria sortCriteria : criteriaCollection)
        {
            String sortPropertyName = sortCriteria.getPropertyName();
            sortPropertyName = getXProperty(sortPropertyName);

            xSort.addCriteria(new OwSortCriteria(sortPropertyName, sortCriteria.getAscFlag()));
        }
        return xSort;
    }

    private Map<String, String>[] loadConfigMapping(String configNodeName_p, String configItemName_p) throws OwConfigurationException
    {
        HashMap<String, String>[] twoWayMaps = new HashMap[2];

        HashMap<String, String> xMappings = new HashMap<String, String>();
        HashMap<String, String> iMappings = new HashMap<String, String>();

        Iterator it = getConfigNode().getSafeUtilList(configNodeName_p, configItemName_p).iterator();
        while (it.hasNext())
        {
            OwXMLUtil propertymap = (OwXMLUtil) it.next();

            String name = propertymap.getSafeStringAttributeValue("name", null);
            String xname = propertymap.getSafeTextValue(null);

            if ((name == null) || (xname == null))
            {
                String msg = "loadConfigMapping: name undefined, please define name attribute and x-namevalue in propertymap tag.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }

            xMappings.put(name, xname);
            iMappings.put(xname, name);
        }

        twoWayMaps[0] = xMappings;
        twoWayMaps[1] = iMappings;
        return twoWayMaps;
    }

    private synchronized void loadPropertyMappings() throws OwConfigurationException
    {
        Map<String, String>[] twoWayMappings = loadConfigMapping("propertymappings", "propertymap");
        m_propertymappings = twoWayMappings[0];
        m_internalPropertyMappings = twoWayMappings[1];
    }

    protected synchronized Map getInternalPropertyMappings() throws OwConfigurationException
    {
        if (null == m_internalPropertyMappings)
        {
            loadPropertyMappings();
        }

        return m_internalPropertyMappings;

    }

    /** map of class names
     * 
     * @return a {@link Map}
     * @throws OwConfigurationException
     * @since 4.0.0.0
     */
    protected Map<String, String> getXClassMappings() throws OwConfigurationException
    {
        if (null == m_xClassMappings)
        {
            Map<String, String>[] twoWayMappings = loadConfigMapping("classmappings", "classmap");
            m_xClassMappings = twoWayMappings[0];
        }

        return m_xClassMappings;
    }

    /** map of class names
     * 
     * @return a {@link Map}
     * @throws OwConfigurationException
     * @since 4.0.0.0
     */
    protected Map<String, String> getIClassMappings() throws OwConfigurationException
    {
        if (null == m_iClassMappings)
        {
            Map<String, String>[] twoWayMappings = loadConfigMapping("classmappings", "classmap");
            m_iClassMappings = twoWayMappings[1];
        }

        return m_iClassMappings;
    }

    public OwClass[] getXClasses(OwClass[] classes_p) throws OwConfigurationException
    {
        OwClass[] xClasses = new OwClass[classes_p.length];
        for (int i = 0; i < xClasses.length; i++)
        {
            String xClassName = getXClassMappings().get(classes_p[i].getClassName());
            xClasses[i] = new OwClass(classes_p[i].getObjectType(), xClassName, classes_p[i].isEnabled(), classes_p[i].isIncludeSubclasses());

        }
        return xClasses;
    }

    public String getXClass(String className_p) throws OwConfigurationException
    {
        String ret = getXClassMappings().get(className_p);

        if (null == ret)
        {
            return className_p;
        }
        else
        {
            return ret;
        }
    }

    public String getIClass(String className_p) throws OwConfigurationException
    {
        String ret = getIClassMappings().get(className_p);

        if (null == ret)
        {
            return className_p;
        }
        else
        {
            return ret;
        }
    }

    /** maps the given X classes to their configured I(nternal) peers
     * 
     * @param propertyNames_p
     * @return a {@link Collection}
     * @throws OwConfigurationException 
     * @since 3.1.0.0
     */
    public Collection getIProperties(Collection propertyNames_p) throws OwConfigurationException
    {
        Collection iProperties = null;
        if (propertyNames_p != null)
        {
            iProperties = new Vector();

            Iterator it = propertyNames_p.iterator();
            while (it.hasNext())
            {
                String xName = (String) it.next();
                String iName = (String) getInternalPropertyMappings().get(xName);

                if (null == iName)
                {
                    iProperties.add(xName);
                }
                else
                {
                    iProperties.add(iName);
                }
            }
        }
        return iProperties;
    }

    /** maps the given X classes to their configured I(nternal) peers
     * 
     * @param xPropertyName_p
     * @return a {@link String}
     * @throws OwConfigurationException
     * @since 3.1.0.0
     */
    public String getIProperty(String xPropertyName_p) throws OwConfigurationException
    {
        String iName = (String) getInternalPropertyMappings().get(xPropertyName_p);

        if (null == iName)
        {
            return xPropertyName_p;
        }
        else
        {
            return iName;
        }
    }

    public Object convert(Object value_p, OwFieldDefinition from_p, OwFieldDefinition to_p) throws OwException
    {
        if (from_p.getJavaClassName().equals(String.class.getName()) && to_p.getJavaClassName().equals(BigInteger.class.getName()))
        {
            return value_p == null ? null : new BigInteger(value_p.toString());
        }
        else if (to_p.getJavaClassName().equals(String.class.getName()) && from_p.getJavaClassName().equals(BigInteger.class.getName()))
        {
            return value_p == null ? null : value_p.toString();
        }
        else
        {
            return value_p;
        }
    }

}
