package com.wewebu.ow.server.field;

import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Interface for objects that provide field definitions, like ECM Adapters.
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
public interface OwFieldDefinitionProvider
{
    /** get a field definition for the given name and resource
     * 
     * NOTE: Since there is no knowledge about the object class the field is attached to, the returned field definition represents an estimation.
     *       Access rights may not be represented correctly.
     *       
     *       To retrieve a precise representation with correct access right's, use the getPropertyClass method of OwObjectClass @see {@link OwObjectClass#getPropertyClass(String)}
     *
     * @param strFieldDefinitionName_p Name of the field definition class
     * @param strResourceName_p optional name of the resource if there are several different resources for field definitions, can be null 
     *
     * @return OwFieldDefinition or throws OwObjectNotFoundException
     */
    public abstract OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException;

    /** get a collection of wild card definitions that are allowed for the given field, resource and search operator
     * 
     * @param strFieldDefinitionName_p Name of the field definition class
     * @param strResourceName_p optional name of the resource if there are several different resources for field definitions, can be null
     * @param iOp_p search operator as defined in OwSearchOperator CRIT_OP_...
     * 
     * @return Collection of OwWildCardDefinition, or null if no wildcards are defined
     * @throws Exception
     */
    public abstract java.util.Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int iOp_p) throws Exception;
}