package com.wewebu.ow.server.history;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * Interface for a property change property used by OwHistoryEtnry.<br/><br/>
 * Keeps information about a modified property and its previous and new value.
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
public interface OwHistoryModifiedPropertyValue
{
    /** get the modified property class name
     * 
     * @return String classname of property
     */
    public abstract String getClassName();

    /** get the modified property field definition
     * 
     * @return OwFieldDefinition field definition of the property
     * @throws OwObjectNotFoundException if value could not be resolved
     */
    public abstract OwFieldDefinition getFieldDefinition() throws OwObjectNotFoundException, Exception;

    /** try to get the old value before the modification happens
     * 
     * @return OwField
     * @throws OwObjectNotFoundException if value could not be resolved
     */
    public abstract OwField getOldValue() throws OwObjectNotFoundException, Exception;

    /** try to get the old value before the modification happens
     * 
     * @return OwField
     * @throws OwObjectNotFoundException if value could not be resolved
     */
    public abstract OwField getNewValue() throws OwObjectNotFoundException, Exception;

    /** get a string representation of the old value
     * 
     * @return String
     */
    public abstract String getOldValueString() throws OwObjectNotFoundException, Exception;;

    /** get a string representation of the new value
     * 
     * @return String
     */
    public abstract String getNewValueString();
}
