package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * Base interface for property class descriptions.
 * Class descriptions are defined by the ECM System, the contain information about
 * the property type. <br/><br/>
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
public interface OwPropertyClass extends OwFieldDefinition
{
    /** context for is... functions */
    public static final int CONTEXT_NORMAL = 0;
    /** context for is... functions */
    public static final int CONTEXT_ON_CREATE = 1;
    /** context for is... functions */
    public static final int CONTEXT_ON_CHECKIN = 2;
    /** maximum context value for is... functions */
    public static final int CONTEXT_MAX = 3;

    /** check if property is a internal system property and contains no custom object information
     */
    public abstract boolean isSystemProperty() throws Exception;

    /** check if property is identical the the OwObject.getName Property
     */
    public abstract boolean isNameProperty() throws Exception;

    /** check if property is read only on the class level.
     *  NOTE:   isReadOnly is also defined in OwProperty on the instance level.
     *          I.e. A Property might be defined as readable on the class level, but still be write protected on a specific instance
     *
     * @param iContext_p Context in which the property is read-only as defined by CONTEXT_...
     * @return true if property is read only
     */
    public abstract boolean isReadOnly(int iContext_p) throws Exception;

    /** check if property is visible to the user
     *
     * @param iContext_p Context in which the property is read-only as defined by CONTEXT_...
     *
     * @return true if property is visible to the user
     */
    public abstract boolean isHidden(int iContext_p) throws Exception;

    /** get the property category, or an empty string of no category is set
     * 
     * @return Category of Property
     * @throws Exception
     */
    public abstract String getCategory() throws Exception;
}