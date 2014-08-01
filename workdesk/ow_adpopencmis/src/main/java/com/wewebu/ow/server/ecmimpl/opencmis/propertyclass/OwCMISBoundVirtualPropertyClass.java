package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import com.wewebu.ow.server.ecm.OwResource;

/**
 *<p>
 * Virtual property that have and approximate CMIS correspondent object-type.
 * For example {@link OwResource#m_ObjectNamePropertyClass} is partially defined by the 
 * <code>cmis:name</code> property : properties of these classes have always the same value, 
 * but the CMIS ID differs from the corresponding MWD object class name.
 * The approximate CMIS correspondent is called the bound class.  
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
public interface OwCMISBoundVirtualPropertyClass<O> extends OwCMISVirtualPropertyClass<O>
{
    /**
     * 
     * @return the name/ID of the approximate CMIS correspondent native object class / object-type.
     */
    String getBoundPropertyClassName();
}
