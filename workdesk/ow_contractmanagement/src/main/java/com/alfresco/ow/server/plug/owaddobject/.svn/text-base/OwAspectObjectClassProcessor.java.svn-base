package com.alfresco.ow.server.plug.owaddobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectClassProcessor;
import com.wewebu.ow.server.ecm.OwLocation;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwAspectObjectClassProcessor.<br/>
 * Processor for adding aspects as the user selected it.
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
public class OwAspectObjectClassProcessor implements OwObjectClassProcessor
{
    /**
     * Representation of an AspectGroup (as configured in owplugnis.xml - AspectsAssociations Tag.
     * Contains aspect definitions.
     */
    public class OwAspectGroup
    {
        /**
         * If false, only one aspect of this group can be selected.
         */
        private boolean isMultiSelect = true;
        /**
         * Display name of the group
         */
        private String displayName;
        /**
         * Map with the AspectTypeId as key and the display name as value.
         */
        private Map<String, String> aspectMap;

        /**
         * @param displayName name of the group
         * @param isMultiSelect if false only one aspect of the group can be selected.
         */
        public OwAspectGroup(String displayName, boolean isMultiSelect)
        {
            if (displayName == null || displayName.isEmpty())
            {
                displayName = "Aspects";
            }

            this.displayName = displayName;
            this.isMultiSelect = isMultiSelect;
            this.aspectMap = new HashMap<String, String>();
        }

        /**
         * @return the isMultiSelect
         */
        public boolean isMultiSelect()
        {
            return isMultiSelect;
        }

        /**
         * @return the displayName
         */
        public String getDisplayName()
        {
            return displayName;
        }

        /**
         * @param aspectTypeId_p
         * @param displayname_p
         * @return true, if aspect was successfully added
         */
        public boolean addAspect(String aspectTypeId_p, String displayname_p)
        {
            if (aspectTypeId_p == null || aspectTypeId_p.isEmpty())
            {
                return false;
            }

            aspectMap.put(aspectTypeId_p, displayname_p == null ? "" : displayname_p);
            return true;
        }

        /**
         * Returns the aspect type id's of this group
         * @return collection with all aspect types of this group
         */
        public Collection<String> getAspectTyps()
        {
            return aspectMap.keySet();
        }

        /**
         * @param aspectTypeId_p
         * @return aspect display name
         */
        public String getAspectDisplayName(String aspectTypeId_p)
        {
            return aspectMap.get(aspectTypeId_p);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((aspectMap == null) ? 0 : aspectMap.hashCode());
            result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
            result = prime * result + (isMultiSelect ? 1231 : 1237);
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            OwAspectGroup other = (OwAspectGroup) obj;
            if (!getOuterType().equals(other.getOuterType()))
            {
                return false;
            }
            if (aspectMap == null)
            {
                if (other.aspectMap != null)
                {
                    return false;
                }
            }
            else if (!aspectMap.equals(other.aspectMap))
            {
                return false;
            }
            if (displayName == null)
            {
                if (other.displayName != null)
                {
                    return false;
                }
            }
            else if (!displayName.equals(other.displayName))
            {
                return false;
            }
            if (isMultiSelect != other.isMultiSelect)
            {
                return false;
            }
            return true;
        }

        private OwAspectObjectClassProcessor getOuterType()
        {
            return OwAspectObjectClassProcessor.this;
        }
    }

    /**
     * Holds information which aspects from the different groups are selected.
     *
     */
    public class OwAspectGroupSelectionHelper
    {
        private List<OwAspectGroup> aspectGroupList = new ArrayList<OwAspectGroup>();
        private Map<String, OwAspectGroup> selectedAspects = new HashMap<String, OwAspectGroup>();
        private Map<String, OwAspectGroup> aspectToGroupMapping = new HashMap<String, OwAspectGroup>();

        /**
         * @param aspectGroup_p
         */
        public void addAspectGroup(OwAspectGroup aspectGroup_p)
        {
            aspectGroupList.add(aspectGroup_p);
            for (String aspect : aspectGroup_p.getAspectTyps())
            {
                aspectToGroupMapping.put(aspect, aspectGroup_p);
            }
        }

        /**
         * Returns the selected aspect group.
         * @param pos selector
         * @return the selected aspect group.
         */
        public OwAspectGroup getAspectGroup(int pos)
        {
            return aspectGroupList.get(pos);
        }

        /**
         * Returns the size of the collection.
         * @return the size of the collection.
         */
        public int size()
        {
            return aspectGroupList.size();
        }

        /**
         * Sets a selected aspect
         * @param aspect_p Aspect name to set
         */
        public void setSelectedAspect(String aspect_p)
        {
            if (aspectToGroupMapping.containsKey(aspect_p))
            {
                selectedAspects.put(aspect_p, this.aspectToGroupMapping.get(aspect_p));
            }
        }

        /**
         * Removes a selected aspect
         * @param aspect_p aspect name to remove
         */
        public void removeSelectedAspect(String aspect_p)
        {
            if (aspectToGroupMapping.containsKey(aspect_p))
            {
                selectedAspects.remove(aspect_p);
            }
        }

        /**
         * Returns a key set of the selected aspects
         * @return a key set of the selected aspects
         */
        public Collection<String> getSeletedAspects()
        {
            return selectedAspects.keySet();
        }

        /**
         * Returns a key set of all aspect groups
         * @returna key set of all aspect groups
         */
        public Set<String> getAllAspects()
        {
            return aspectToGroupMapping.keySet();
        }

        /**
         * Checks if aspect is selected.
         * @param aspect_p aspect name
         * @return if aspect is selected.
         */
        public boolean isSelectedAspect(String aspect_p)
        {
            return selectedAspects.containsKey(aspect_p);
        }

        /**
         * Returns an aspect group.
         * @param aspect_p
         * @return an aspect group.
         */
        public OwAspectGroup getAspectGroup(String aspect_p)
        {
            return aspectToGroupMapping.get(aspect_p);
        }

        /**
         * Checks if selected aspect contains group
         * @param group_p
         * @return if selected aspect contains group
         */
        public boolean hasSelectedAspects(OwAspectGroup group_p)
        {
            return selectedAspects.containsValue(group_p);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((aspectGroupList == null) ? 0 : aspectGroupList.hashCode());
            result = prime * result + ((aspectToGroupMapping == null) ? 0 : aspectToGroupMapping.hashCode());
            result = prime * result + ((selectedAspects == null) ? 0 : selectedAspects.hashCode());
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            OwAspectGroupSelectionHelper other = (OwAspectGroupSelectionHelper) obj;
            if (!getOuterType().equals(other.getOuterType()))
            {
                return false;
            }
            if (aspectGroupList == null)
            {
                if (other.aspectGroupList != null)
                {
                    return false;
                }
            }
            else if (!aspectGroupList.equals(other.aspectGroupList))
            {
                return false;
            }
            if (aspectToGroupMapping == null)
            {
                if (other.aspectToGroupMapping != null)
                {
                    return false;
                }
            }
            else if (!aspectToGroupMapping.equals(other.aspectToGroupMapping))
            {
                return false;
            }
            if (selectedAspects == null)
            {
                if (other.selectedAspects != null)
                {
                    return false;
                }
            }
            else if (!selectedAspects.equals(other.selectedAspects))
            {
                return false;
            }
            return true;
        }

        private OwAspectObjectClassProcessor getOuterType()
        {
            return OwAspectObjectClassProcessor.this;
        }

        /**
         * Resets the selection status in the helper.
         */
        public void reset()
        {
            selectedAspects.clear();
        }
    }

    /** Logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwAspectObjectClassProcessor.class);

    /** Map as configured to use for processing */
    private Map<String, OwAspectGroupSelectionHelper> m_AspectGroupMapping;
    private Map<String, List<String>> m_FixedAssociationMapping;

    /** Final map to use for processing */
    private Map<String, String> m_FinalMapping = null;

    /** the separator which severs the aspect names in a class configuration */
    private final char SEPARATOR = ',';

    /** stores if the aspect association is editable by the user */
    private boolean m_bEdit = false;

    /**
     * resets the processor for next cycle
     */
    public void reset()
    {
        // delete final mapping
        this.m_FinalMapping = null;

        // reset aspect groups
        for (OwAspectGroupSelectionHelper helper : this.m_AspectGroupMapping.values())
        {
            helper.reset();
        }
    }

    /**
     * Processes the object class and returns it. 
     */
    public OwObjectClass process(OwObjectClass objCls_p, OwLocation location_p, OwAppContext contex_p)
    {
        OwObjectClass processed = objCls_p;
        if (getFinalMapping() != null)
        {
            String secTypes = getFinalMapping().get(objCls_p.getClassName());
            if (secTypes != null)
            {
                String objClass = objCls_p.getClassName() + "," + secTypes;
                OwRoleManagerContext roleCtx = contex_p.getRegisteredInterface(OwRoleManagerContext.class);
                if (roleCtx != null)
                {
                    try
                    {
                        processed = roleCtx.getNetwork().getObjectClass(objClass, location_p.getResource());
                    }
                    catch (Exception e)
                    {
                        LOG.warn("Could not retrieve specific object class = " + objClass, e);
                    }
                }
            }
        }

        return processed;
    }

    /** 
     * Returns if the aspect association is editable by the user.
     * @return if the aspect association is editable by the user.
     */
    public boolean getIsEditable()
    {
        return this.m_bEdit;
    }

    /**
     * Initializes the map to process. 
     */
    public void init(OwXMLUtil configNode_p) throws OwException
    {
        m_AspectGroupMapping = getAspectGroupMapping(configNode_p);
        m_FixedAssociationMapping = getFixedAssociationMapping(configNode_p);

        // read "edit" flag. If true, final mapping is null until user has passed the dialog. 
        String strEdit = configNode_p.getSafeStringAttributeValue("edit", "false");
        this.m_bEdit = (strEdit != null && strEdit.toLowerCase().equals("true"));
    }

    /**
     * Getter for current mapping, can be null if not initialized.
     * @return Map (ObjectType to SecondaryTypes) or null.
     */
    public Map<String, OwAspectGroupSelectionHelper> getAspectGroupMapping()
    {
        return this.m_AspectGroupMapping;
    }

    /** Returns the final mapping to process aspects - null if editable by user and not processed!
     * @return the final mapping to process aspects - null if editable by user and not processed!
     */
    public Map<String, String> getFinalMapping()
    {
        if (null == this.m_FinalMapping && !this.m_bEdit)
        {
            Map<String, String> retMap = new HashMap<String, String>();
            for (String objectClassId : this.m_AspectGroupMapping.keySet())
            {
                OwAspectGroupSelectionHelper col = this.m_AspectGroupMapping.get(objectClassId);
                Set<String> aspectGroupSet = col.getAllAspects();
                StringBuilder aspects = new StringBuilder();
                for (String aspect : aspectGroupSet)
                {
                    aspects.append(aspect);
                    aspects.append(",");
                }
                aspects.deleteCharAt(aspects.length() - 1);
                retMap.put(objectClassId, aspects.toString());
            }
            return retMap;
        }
        return this.m_FinalMapping;
    }

    /**
     * Sets the final mapping to apply. 
     * @param map_p Map to use for processing. 
     */
    public void setFinalMapping(Map<String, String> map_p)
    {
        this.m_FinalMapping = map_p;
    }

    /**
     * Returns a collection with the names of the aspects configured for the given class name.
     * @param className_p class name to get aspect names for
     * @return  a collection with the names of the aspects configured for the given class name.
     */
    public OwAspectGroupSelectionHelper getConfiguredAspects(String className_p)
    {
        Map<String, OwAspectGroupSelectionHelper> map = this.getAspectGroupMapping();

        if (map.containsKey(className_p))
        {
            return map.get(className_p);
        }

        return new OwAspectGroupSelectionHelper();
    }

    /**
     * Sets the final mapping for the object class.
     * @param objectClassName_p class name of the object to set
     * @param aspectNames_p aspect names to set
     */
    public void setFinalAspects(String objectClassName_p, List<String> aspectNames_p)
    {
        // check input params
        if (null == objectClassName_p || objectClassName_p.isEmpty())
        {
            String message = "Parameter 'objectClassName_p' is a required value!";
            LOG.error(message);
            throw new NullPointerException(message);
        }

        // create string for map
        StringBuilder aspects = new StringBuilder();
        if (null != aspectNames_p && !aspectNames_p.isEmpty())
        {
            for (String aspectName : aspectNames_p)
            {
                if (null != aspectName && !aspectName.isEmpty())
                {
                    if (aspects.length() > 0)
                    {
                        aspects.append(this.SEPARATOR);
                    }
                    aspects.append(aspectName.trim());
                }
            }
        }

        //add fixed associated mappings
        if (m_FixedAssociationMapping != null && !m_FixedAssociationMapping.isEmpty() && m_FixedAssociationMapping.containsKey(objectClassName_p))
        {
            List<String> aspectList = m_FixedAssociationMapping.get(objectClassName_p);
            for (String aspectName : aspectList)
            {
                if (null != aspectName && !aspectName.isEmpty() && !aspectNames_p.contains(aspectName))
                {
                    if (aspects.length() > 0)
                    {
                        aspects.append(this.SEPARATOR);
                    }
                    aspects.append(aspectName.trim());
                }
            }
        }

        // set final mapping
        Map<String, String> map = new HashMap<String, String>();
        map.put(objectClassName_p, aspects.toString());
        this.setFinalMapping(map);
    }

    /**
     * Returns aspect group mapping
     * @param configuration XML util
     * @return aspect group mapping
     */
    @SuppressWarnings("unchecked")
    public Map<String, OwAspectGroupSelectionHelper> getAspectGroupMapping(OwXMLUtil configuration)
    {
        Map<String, OwAspectGroupSelectionHelper> retMap = new HashMap<String, OwAspectGroupSelectionHelper>();

        List associationGroupList = configuration.getSafeUtilList("AssociationGroup");
        for (Iterator iterator = associationGroupList.iterator(); iterator.hasNext();)
        {
            OwXMLUtil aspectGroupUtil = (OwXMLUtil) iterator.next();
            String objectTypeId = aspectGroupUtil.getSafeStringAttributeValue("objectTypeId", "");
            if (objectTypeId.isEmpty())
            {
                continue;
            }

            String displayName = aspectGroupUtil.getSafeStringAttributeValue("displayname", "Aspects");
            boolean isMultiSelect = aspectGroupUtil.getSafeBooleanAttributeValue("multiselect", true);
            List aspectUtilList = aspectGroupUtil.getSafeUtilList("Aspect");

            //create new aspect group
            OwAspectGroup aspectGroup = new OwAspectGroup(displayName, isMultiSelect);

            for (Iterator iterator2 = aspectUtilList.iterator(); iterator2.hasNext();)
            {
                OwXMLUtil aspectUtil = (OwXMLUtil) iterator2.next();
                String value = aspectUtil.getSafeTextValue("");
                if (value.isEmpty())
                {
                    continue;
                }

                String name = aspectUtil.getSafeStringAttributeValue("displayname", value);
                aspectGroup.addAspect(value, name);
            }

            if (aspectGroup.getAspectTyps().isEmpty())
            {
                continue;
            }

            if (retMap.containsKey(objectTypeId))
            {
                retMap.get(objectTypeId).addAspectGroup(aspectGroup);
            }
            else
            {
                OwAspectGroupSelectionHelper col = new OwAspectGroupSelectionHelper();
                col.addAspectGroup(aspectGroup);
                retMap.put(objectTypeId, col);
            }
        }
        return retMap;
    }

    /**
     * Returns  aspect group mapping
     * @param configuration
     * @return  aspect group mapping
     */
    public Map<String, List<String>> getFixedAssociationMapping(OwXMLUtil configuration)
    {
        Map<String, List<String>> retMap = new HashMap<String, List<String>>();
        List<OwXMLUtil> associations = configuration.getSafeUtilList("Association");
        for (OwXMLUtil asUtil : associations)
        {
            String combinedValue = asUtil.getSafeTextValue("").trim();
            if (!combinedValue.isEmpty())
            {
                String[] map = combinedValue.split("=");
                if (map.length == 2)
                {
                    String[] strValArr = map[1].split("[\\s,]+");
                    List<String> values = new ArrayList<String>(strValArr.length);
                    for (String strVal : strValArr)
                    {
                        values.add(strVal.trim());
                    }
                    retMap.put(map[0], values);
                }
            }
        }
        return retMap;
    }
}
