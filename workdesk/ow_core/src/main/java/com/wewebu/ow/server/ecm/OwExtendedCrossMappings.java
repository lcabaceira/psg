package com.wewebu.ow.server.ecm;

import java.util.Collection;

import com.wewebu.ow.server.exceptions.OwConfigurationException;

/**
 *<p>
 * Extended interface for ECM adapter mapping or sort, search, properties and login.
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
 *@since 4.0.0.0
 */
public interface OwExtendedCrossMappings extends OwCrossMappings
{
    /** maps the given classes to their configured X peers
     * 
     * @param classes_p
     * @return a {@link OwClass}
     * @throws OwConfigurationException
     */
    OwClass[] getXClasses(OwClass[] classes_p) throws OwConfigurationException;

    /** maps the given classes to their configured X peers
     * 
     * @param className_p
     * @return an X object class name 
     * @throws OwConfigurationException
     */
    String getXClass(String className_p) throws OwConfigurationException;

    /** maps the given classes to their configured I peers
     * 
     * @param className_p
     * @return an I object class name 
     * @throws OwConfigurationException
     */
    String getIClass(String className_p) throws OwConfigurationException;

    /** maps the given X classes to their configured I(nternal) peers
     * 
     * @param xPropertyName_p
     * @return an X object class name 
     * @throws OwConfigurationException
     */
    String getIProperty(String xPropertyName_p) throws OwConfigurationException;

    /** maps the given X classes to their configured I(nternal) peers
     * 
     * @param propertyNames_p
     * @return an X object class name 
     * @throws OwConfigurationException
     */
    Collection getIProperties(Collection propertyNames_p) throws OwConfigurationException;
}
