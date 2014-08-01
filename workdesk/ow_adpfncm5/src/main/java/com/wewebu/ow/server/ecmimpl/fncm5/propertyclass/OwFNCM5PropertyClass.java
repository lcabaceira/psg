package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import com.filenet.api.constants.PropertyNames;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwResource.OwObjectNamePropertyClass;
import com.wewebu.ow.server.ecm.OwResource.OwObjectPathPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * P8 5.0 adapter extension of the AWD property class.<br/>
 * 
 * Property class bindings describe the class relations to P8 5.0 engine and/or AWD core 
 * property classes that this property class relays on.      
 * 
 * For example a name property could relay on the {@link PropertyNames#NAME}-defined engine property and 
 * the {@link OwObjectNamePropertyClass} AWD property.
 * 
 * P8 5.0 property classes have virtual and engine bindings - at least one of them.<br/>
 * {@link OwFNCM5EngineBinding}s are abstractions of the native P8 5.0 property class definitions that 
 * this property class is bound to.<br/>
 * {@link OwFNCM5VirtualBinding}s are abstractions of the AWD defined property classes (such as 
 * {@link OwObjectNamePropertyClass} or {@link OwObjectPathPropertyClass}) that this property class is bound to.<br/> 
 *  
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
public interface OwFNCM5PropertyClass extends OwPropertyClass
{

    Object getNativeType() throws OwException;

    boolean isArray() throws OwException;

    boolean isHidden(int iContext_p) throws OwException;

    boolean isReadOnly(int iContext_p) throws OwException;

    /**
     * 
     * @return the engine binding or null if none
     * @throws OwException
     */
    OwFNCM5EngineBinding<?, ?, ?> getEngineBinding() throws OwException;

    /**
     * 
     * @return the virtual binding or null if none
     * @throws OwException
     */
    OwFNCM5VirtualBinding getVirtualBinding() throws OwException;

    boolean isSystemProperty() throws OwException;

}
