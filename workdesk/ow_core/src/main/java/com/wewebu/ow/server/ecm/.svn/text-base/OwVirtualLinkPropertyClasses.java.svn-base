package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwObjectLink virtual properties constant class. 
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
 *@since 4.1.1.0
 */
public final class OwVirtualLinkPropertyClasses
{
    /**Virtual property single value, where value is enumeration OwObjectLinkRelation
     * @see OwObjectLinkRelation*/
    public static final OwLinkRelationPropertyClass LINK_RELATION = new OwLinkRelationPropertyClass();

    /**Virtual property with multi-value (String) capability, used for filtering/retrieving specific Link types.
     * @see OwObjectLink#OW_LINK_TYPE_FILTER*/
    public static final OwLinkFilterPropertyClass LINK_TYPE_FILTER = new OwLinkFilterPropertyClass();

    /**The virtual property class, which can be used as abstract reference to the source of a Link*/
    public static final OwLinkSourcePropertyClass LINK_SOURCE = new OwLinkSourcePropertyClass();

    /**The virtual property class, which can be used as abstract reference to the target of a Link*/
    public static final OwLinkTargetPropertyClass LINK_TARGET = new OwLinkTargetPropertyClass();

    /**
     *<p>
     * Special PropertyClass for handling OwObjectLink requests.
     * This PropertyClass (FieldDefinition) is used to provide a
     * value for requesting specific Link-Relation.
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
    public static class OwLinkRelationPropertyClass extends OwStandardPropertyClass
    {
        public OwLinkRelationPropertyClass()
        {
            super();
            m_fHidden[CONTEXT_NORMAL] = true;
            m_fHidden[CONTEXT_ON_CREATE] = true;
            m_fHidden[CONTEXT_ON_CHECKIN] = true;

            m_fArray = false;
            m_fRequired = false;
            m_fSystem = true;
            m_fReadOnly[CONTEXT_NORMAL] = true;
            m_fReadOnly[CONTEXT_ON_CREATE] = true;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = true;

            m_DisplayName = new OwString("ecm.OwVirtualLinkPropertyClasses.OwObjectLinkRelation.name", "Link relation");

            this.m_strClassName = OwObjectLink.OW_LINK_RELATION;
            this.m_strJavaClassName = "com.wewebu.ow.server.ecm.OwObjectLinkRelation";
        }
    }

    /**
     *<p>
     * Virtual property to be used for filtering/retrieving specific Link types. 
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
    public static class OwLinkFilterPropertyClass extends OwStandardPropertyClass
    {
        public OwLinkFilterPropertyClass()
        {
            super();
            m_fHidden[CONTEXT_NORMAL] = true;
            m_fHidden[CONTEXT_ON_CREATE] = true;
            m_fHidden[CONTEXT_ON_CHECKIN] = true;

            m_fArray = true;
            m_fRequired = false;
            m_fSystem = true;
            m_fReadOnly[CONTEXT_NORMAL] = true;
            m_fReadOnly[CONTEXT_ON_CREATE] = true;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = true;

            m_DisplayName = new OwString("ecm.OwVirtualLinkPropertyClasses.OwLinkFilterPropertyClass.name", "Link filter");

            this.m_strClassName = OwObjectLink.OW_LINK_TYPE_FILTER;
            this.m_strJavaClassName = "java.lang.String";
        }
    }

    /**
     *<p>
     * PropertyClass for the Link-target definition.
     * Target must be visible and modifiable during create process. 
     * The type of value is be com.wewebu.ow.server.ecm.OwObjectReference.
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
    public static class OwLinkTargetPropertyClass extends OwStandardPropertyClass
    {
        public OwLinkTargetPropertyClass()
        {
            super();
            m_fHidden[CONTEXT_NORMAL] = true;
            m_fHidden[CONTEXT_ON_CREATE] = false;
            m_fHidden[CONTEXT_ON_CHECKIN] = true;

            m_fArray = false;
            m_fRequired = false;
            m_fSystem = true;
            m_fReadOnly[CONTEXT_NORMAL] = true;
            m_fReadOnly[CONTEXT_ON_CREATE] = false;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = true;

            m_DisplayName = new OwString("ecm.OwVirtualLinkPropertyClasses.OwLinkTargetPropertyClass.name", "Target");

            this.m_strClassName = OwObjectLink.OW_LINK_TARGET;
            this.m_strJavaClassName = "com.wewebu.ow.server.ecm.OwObjectReference";
        }
    }

    /**
     *<p>
     * PropertyClass for the Link source definition.
     * The type of value is be com.wewebu.ow.server.ecm.OwObjectReference.
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
    public static class OwLinkSourcePropertyClass extends OwStandardPropertyClass
    {
        public OwLinkSourcePropertyClass()
        {
            super();
            m_fHidden[CONTEXT_NORMAL] = true;
            m_fHidden[CONTEXT_ON_CREATE] = true;
            m_fHidden[CONTEXT_ON_CHECKIN] = true;

            m_fArray = false;
            m_fRequired = false;
            m_fSystem = true;
            m_fReadOnly[CONTEXT_NORMAL] = true;
            m_fReadOnly[CONTEXT_ON_CREATE] = false;
            m_fReadOnly[CONTEXT_ON_CHECKIN] = true;

            m_DisplayName = new OwString("ecm.OwVirtualLinkPropertyClasses.OwLinkSourcePropertyClass.name", "Source");

            this.m_strClassName = OwObjectLink.OW_LINK_SOURCE;
            this.m_strJavaClassName = "com.wewebu.ow.server.ecm.OwObjectReference";
        }
    }
}
