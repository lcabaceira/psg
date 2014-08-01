package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.field.OwField;

/**
 *<p>
 * Base interface for object properties. A property contains the name and value of a object property. <br/><br/>
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
public interface OwProperty extends java.lang.Comparable, OwField
{
    /** get the class description of the property
     */
    public abstract OwPropertyClass getPropertyClass() throws Exception;

    /** check if property is read only on the instance level
     *  NOTE:   isReadOnly is also defined in OwPropertyClass on the class level.
     *          I.e. A Property might be defined as readable on the class level, but still be write protected on a specific instance.
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

    /** overridden from java.lang.Object, make sure clone is implemented in subclasses
     *
     * @return OwProperty copy of this object
     */
    public abstract Object clone() throws CloneNotSupportedException;

    /** get the native object from the ECM system 
     *
     *  WARNING: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return no native object available
     */
    public abstract Object getNativeObject() throws Exception;
}