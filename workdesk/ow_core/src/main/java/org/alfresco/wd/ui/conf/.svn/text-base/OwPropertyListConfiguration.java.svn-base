package org.alfresco.wd.ui.conf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.alfresco.wd.ui.conf.prop.OwPropertyGroup;
import org.alfresco.wd.ui.conf.prop.OwPropertyInfo;
import org.alfresco.wd.ui.conf.prop.OwSimplePropertyGroup;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * PropertyGroups and PropertyList configuration object representation.
 * Can contain a restricted list of properties and/or additionally
 * a grouping of properties which should be shown.
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
 */
public class OwPropertyListConfiguration
{
    private static Logger LOG = OwLogCore.getLogger(OwPropertyListConfiguration.class);

    /**the configuration attribute <code>collapsed</code>*/
    public static final String ATT_COLLAPSED = "collapsed";
    /**the configuration attribute <code>id</code>*/
    public static final String ATT_ID = "id";
    /**the configuration attribute <code>readonly</code>*/
    public static final String ATT_READONLY = "readonly";
    /**the configuration attribute <code>displayname</code>*/
    public static final String ATT_DISPLAY_NAME = "displayname";
    /**Default root configuration node name <code>EditPropertyList<code>*/
    public static final String ELEM_ROOT_NODE = "EditPropertyList";
    /**the configuration element <code>PropertyGroups</code>*/
    public static final String ELEM_PROPERTY_GROUP = "PropertyGroup";
    /**the configuration element <code>property</code>*/
    public static final String ELEM_PROPERTY = "property";

    private List<OwPropertySubregion<OwPropertyInfo>> subregions;
    private Map<String, OwPropertySubregion<OwPropertyInfo>> groupSubregionMap;
    private String groupIdPrefix;

    public OwPropertyListConfiguration()
    {
        this(null);
    }

    public OwPropertyListConfiguration(String groupIdPrefix)
    {
        subregions = new LinkedList<OwPropertySubregion<OwPropertyInfo>>();
        groupSubregionMap = new HashMap<String, OwPropertySubregion<OwPropertyInfo>>();
        this.groupIdPrefix = groupIdPrefix;
    }

    /**
     * Returns a list of Regions configured for rendering.
     * @return List of {@link org.alfresco.wd.ui.conf.OwPropertySubregion OwPropertySubregion} objects.
     */
    public List<OwPropertySubregion<OwPropertyInfo>> getSubregions()
    {
        return subregions;
    }

    /**
     * Get the subregion for specific groupId
     * @param groupId String
     * @return OwPropertySubregion or null if not found
     */
    public OwPropertySubregion<OwPropertyInfo> getGroupRegion(String groupId)
    {
        return groupSubregionMap.get(groupId);
    }

    /**
     * Use to add region to this configuration.
     * Will check if region is based on OwPropertyGroup and add to map for faster access in {@link #getGroupRegion(String)}.<br />
     * Can throw a NullPointerException if provided region object is null.
     * @param region OwPropertySubregion
     */
    public void addRegion(OwPropertySubregion<OwPropertyInfo> region)
    {
        if (region == null)
        {
            throw new NullPointerException("Null region are not allowed to be added.");
        }
        subregions.add(region);
        if (region.isGroup())
        {
            groupSubregionMap.put(region.getPropertyGroup().getGroupId(), region);
        }
    }

    /**
     * Initialize/build instance from provided configuration.
     * @param configuration
     * @throws OwConfigurationException
     */
    public void build(OwXMLUtil configuration) throws OwConfigurationException
    {
        List<?> confUtils = configuration.getSafeUtilList(null);
        Iterator<?> it = confUtils.iterator();
        List<OwPropertyInfo> propLst = null;
        while (it.hasNext())
        {
            OwXMLUtil conf = (OwXMLUtil) it.next();
            if (ELEM_PROPERTY.equals(conf.getName()))
            {
                OwPropertyInfo propInfo = buildPropertyInfo(conf);
                if (propInfo != null)
                {
                    if (propLst == null)
                    {
                        propLst = new LinkedList<OwPropertyInfo>();
                    }
                    propLst.add(propInfo);
                }
            }
            else if (ELEM_PROPERTY_GROUP.equals(conf.getName()))
            {
                OwPropertyGroup<OwPropertyInfo> propGroup = buildPropertyGroup(conf);
                if (propLst != null)
                {
                    OwPropertySubregion<OwPropertyInfo> region = createSubRegion(propLst);
                    addRegion(region);
                    propLst = null;
                }
                OwPropertySubregion<OwPropertyInfo> region = createSubRegion(propGroup);
                addRegion(region);
            }
            else
            {
                LOG.info("OwPropertyListConfiguration.build: unknown/unsupported element found [" + conf.getName() + "] will be ignored.");
            }
        }

        if (propLst != null)
        {
            OwPropertySubregion<OwPropertyInfo> region = createSubRegion(propLst);
            addRegion(region);
        }
    }

    protected OwPropertyInfo buildPropertyInfo(OwXMLUtil conf) throws OwConfigurationException
    {
        boolean readOnly = conf.getSafeBooleanAttributeValue(ATT_READONLY, false);
        String propId = conf.getSafeTextValue(null);
        if (propId == null)
        {
            if (LOG.isInfoEnabled())
            {
                LOG.info("OwPropertyListConfiguration.build: unknown/unsupported element found [" + conf.getName() + "] will be ignored.");
            }
            return null;
        }
        else
        {
            return new OwPropertyInfo(propId, readOnly);
        }
    }

    protected OwPropertyGroup<OwPropertyInfo> buildPropertyGroup(OwXMLUtil conf) throws OwConfigurationException
    {
        Iterator<?> itProp = conf.getSafeUtilList(null).iterator();
        List<OwPropertyInfo> propInfos = new LinkedList<OwPropertyInfo>();
        while (itProp.hasNext())
        {
            OwXMLUtil propUtil = (OwXMLUtil) itProp.next();
            OwPropertyInfo propInfo = buildPropertyInfo(propUtil);
            if (propInfo != null)
            {
                propInfos.add(propInfo);
            }
        }

        String groupId = conf.getSafeStringAttributeValue(ATT_ID, null);
        String displayName = conf.getSafeStringAttributeValue(ATT_DISPLAY_NAME, null);
        boolean collapsed = conf.getSafeBooleanAttributeValue(ATT_COLLAPSED, false);

        if (getGroupIdPrefix() != null)
        {
            groupId = getGroupIdPrefix() + "." + groupId;
        }

        return new OwSimplePropertyGroup<OwPropertyInfo>(groupId, displayName, propInfos, collapsed);
    }

    /**
     * Get a prefix which should be attached to GroupId.<br />
     * <code>null</code> defines to keep the PropertyGroupId as is.
     * @return String or null
     */
    public String getGroupIdPrefix()
    {
        return this.groupIdPrefix;
    }

    /**
     * create a non-grouped subregion instance.
     * @param propInfos List of OwPropertyInfo
     * @return OwPropertySubregion
     */
    protected <T extends OwPropertyInfo> OwPropertySubregion<T> createSubRegion(List<T> propInfos)
    {
        return new OwSimplePropertySubregion<T>(propInfos);
    }

    /**
     * Create a grouped subregion instance.
     * @param propGroup OwPropertyGroup
     * @return OwPropertySubregion
     */
    protected <T extends OwPropertyInfo> OwPropertySubregion<T> createSubRegion(OwPropertyGroup<T> propGroup)
    {
        return new OwSimplePropertySubregion<T>(propGroup);
    }
}
