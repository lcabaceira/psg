package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISVirtualProperty;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Virtual property classes are abstractions of property classes that 
 * do not have a direct CMIS repository representation 
 * (no exact corresponding CMIS object-type exist).
 * 
 * See also :   
 *     {@link OwResource#m_ObjectNamePropertyClass}, 
 *     {@link OwResource#m_ObjectPathPropertyClass}, 
 *     {@link OwResource#m_ClassDescriptionPropertyClass},
 *     {@link OwResource#m_ResourcePropertyClass} or 
 *     {@link OwResource#m_VersionSeriesPropertyClass}.  
 *       
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
public interface OwCMISVirtualPropertyClass<V> extends OwCMISPropertyClass<V>
{
    /**
     * Creates a virtual property instance of this class for a given object.
     *   
     * @param object_p
     * @return a {@link OwCMISVirtualProperty} of this class for the given {@link OwCMISObject}  
     * @throws OwException
     */
    OwCMISVirtualProperty<V> from(OwCMISObject object_p) throws OwException;

    @Override
    OwCMISVirtualPropertyClass<V> createProxy(String className);
}
