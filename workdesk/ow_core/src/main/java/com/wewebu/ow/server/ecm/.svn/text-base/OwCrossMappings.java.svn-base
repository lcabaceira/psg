package com.wewebu.ow.server.ecm;

import java.util.Collection;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Interface for ECM adapter mapping or sort, search, properties and login.
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
public interface OwCrossMappings
{
    /** map the given property names
     * 
     * @param propertyNames_p
     * @return a {@link Collection}
     * @throws OwConfigurationException 
     */
    public Collection getXProperties(Collection propertyNames_p) throws OwConfigurationException;

    /** login to the X network
     * 
     * @param xnetwork_p
     * @param parentnetwork_p
     * @param parentuser_p
     * @param parentpassword_p
     * @throws Exception 
     */
    public void doXLogin(OwNetwork xnetwork_p, OwNetwork parentnetwork_p, String parentuser_p, String parentpassword_p) throws Exception;

    /** map the given property name
     * 
     * @param name_p
     * @return a {@link String}
     * @throws OwConfigurationException
     */
    public String getXProperty(String name_p) throws OwConfigurationException;

    /** map the given search
     * 
     * @param searchNode_p
     * @return an {@link OwSearchNode}
     * @throws CloneNotSupportedException 
     */
    public OwSearchNode getXSearch(OwSearchNode searchNode_p, String xRepositoryID_p, OwNetwork xNetwork_p) throws CloneNotSupportedException;

    /** map the given search
     * 
     * @param searchNode_p
     * @return an {@link OwSearchNode}
     * @throws CloneNotSupportedException 
     * @since 4.0.0.0
     */
    public OwSort getXSort(OwSort searchNode_p) throws CloneNotSupportedException, OwConfigurationException;

    /** replace the objects of the given collection with mapped wrapper objects
     * 
     * @param objects_p
     */
    public void mapXObjectCollection(OwObjectCollection objects_p);

    public Object convert(Object value_p, OwFieldDefinition from_p, OwFieldDefinition to_p) throws OwException;
}