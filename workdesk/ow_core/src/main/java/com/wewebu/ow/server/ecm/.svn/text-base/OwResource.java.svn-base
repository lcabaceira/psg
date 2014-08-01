package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Base interface for resource objects to identify resources in ECM Adapters that 
 * support multiple resources (Archives, ObjectStore...). <br/>
 * In order to distinguish and select the resources, the OwResource class is used as a placeholder for a 
 * different resources in a ECM Adapter network. <br/><br/>
 * <b>NOTE:</b> OwResource is also used to distinguish other ECM Adapters in a Collection ECM Adapter Network. <br/><br/>
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
public interface OwResource
{
    /**
     *<p>
     * Class description for special classes, which are added manually.
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
    public static class OwSpecialPropertyClass extends OwStandardPropertyClass
    {
        /** construct PropertyClass Object and set members */
        public OwSpecialPropertyClass()
        {
            m_fHidden[CONTEXT_NORMAL] = true;
            m_fHidden[CONTEXT_ON_CREATE] = true;
            m_fHidden[CONTEXT_ON_CHECKIN] = true;

            m_fArray = false;
            m_fRequired = false;
            m_fSystem = true;
            m_fReadOnly[CONTEXT_NORMAL] = true;
            m_fReadOnly[CONTEXT_ON_CREATE] = true;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = true;
        }
    }

    /**
     *<p>
     * Special property class for ClassDescription property.
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
    public static class OwClassDescriptionPropertyClass extends OwSpecialPropertyClass
    {
        /** construct PropertyClass Object and set members */
        public OwClassDescriptionPropertyClass()
        {
            super();

            m_strClassName = "OW_ClassDescription";
            m_DisplayName = new OwString("ecm.OwResource.OwClassDescriptionPropertyClass.name", "Type");
            m_strJavaClassName = "com.wewebu.ow.server.ecm.OwObjectClass";
        }
    }

    /**
     *<p>
     * Special property class for the object name property.
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
    public static class OwObjectNamePropertyClass extends OwSpecialPropertyClass
    {
        /** construct PropertyClass Object and set members */
        public OwObjectNamePropertyClass()
        {
            super();

            m_strClassName = "OW_ObjectName";
            m_DisplayName = new OwString("ecm.OwResource.OwObjectNamePropertyClass.name", "Name");
            m_strJavaClassName = "java.lang.String";
            m_fName = true;
        }
    }

    /**
     *<p>
     * Special property class for the object path property.
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
    public static class OwObjectPathPropertyClass extends OwSpecialPropertyClass
    {
        public OwObjectPathPropertyClass()
        {
            m_strClassName = "OW_ObjectPath";
            m_DisplayName = new OwString("ecm.OwResource.OwObjectPathPropertyClass.objectPath", "Path");
            m_strJavaClassName = "java.lang.String";
            m_fName = false;

            m_fHidden[CONTEXT_NORMAL] = false;
            m_fHidden[CONTEXT_ON_CREATE] = false;
            m_fHidden[CONTEXT_ON_CHECKIN] = false;

            m_fArray = false;
            m_fRequired = false;
            m_fSystem = true;

            m_fReadOnly[CONTEXT_NORMAL] = true;
            m_fReadOnly[CONTEXT_ON_CREATE] = true;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = true;
        }
    }

    /**
     *<p>
     * Special property class for resource property.
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
    public static class OwResourcePropertyClass extends OwSpecialPropertyClass
    {
        /** construct PropertyClass Object and set members */
        public OwResourcePropertyClass()
        {
            super();

            m_strClassName = "OW_Resource";
            m_DisplayName = new OwString("ecm.OwResourcePropertyClass.name", "Resource");
            m_strJavaClassName = "com.wewebu.ow.server.ecm.OwResource";
        }
    }

    /**
     *<p>
     * Special property class for VersionSeries ID property.
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
    public static class OwVersionSeriesPropertyClass extends OwSpecialPropertyClass
    {
        /** construct PropertyClass Object and set members */
        public OwVersionSeriesPropertyClass()
        {
            super();

            m_strClassName = "OW_VersionSeries";
            m_DisplayName = new OwString("ecm.OwVersionSeriesPropertyClass.name", "VersionSeries");
            m_strJavaClassName = "com.wewebu.ow.server.ecm.OwVersionSeries";
        }
    }

    // === special class descriptions    
    /** the one and only instance of the OwStandardPropertyClass for ClassDescription properties, which are added manually
     */
    public static final OwClassDescriptionPropertyClass m_ClassDescriptionPropertyClass = new OwClassDescriptionPropertyClass();
    /** the one and only instance of the OwStandardPropertyClass for Resource properties, which are added manually
     */
    public static final OwResourcePropertyClass m_ResourcePropertyClass = new OwResourcePropertyClass();
    /** the one and only instance of the OwStandardPropertyClass for VersionsSeries properties, which are added manually
     */
    public static final OwVersionSeriesPropertyClass m_VersionSeriesPropertyClass = new OwVersionSeriesPropertyClass();
    /** the one and only instance of the OwStandardPropertyClass property class, which are added manually
     */
    public static final OwObjectNamePropertyClass m_ObjectNamePropertyClass = new OwObjectNamePropertyClass();

    /** the one and only instance of the OwStandardPropertyClass property class, which are added manually
     */
    public static final OwObjectPathPropertyClass m_ObjectPathPropertyClass = new OwObjectPathPropertyClass();

    /** gets a display name for the resource
     *
     * @param locale_p Locale to use
     * @return String
     */
    public abstract String getDisplayName(java.util.Locale locale_p);

    /** gets a description for the resource
     * 
     * @param locale_p Locale to use
     * @return String
     */
    public abstract String getDescription(java.util.Locale locale_p);

    /** gets a unique ID for the resource
     *
     * @return String ID
     */
    public abstract String getID() throws Exception;
}