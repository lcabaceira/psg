package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import com.filenet.api.property.Property;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5EngineProperty;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Abstraction of the P8 5.0 ECM binding of a property class.<br/>
 * Implementations expose P8 5.0 defined attributes of a property class like 
 * symbolic name , selectability and orderability flags. 
 * Conversion from AWD values to engine/native values is also 
 * implemented engine property bindings.        
 * @see OwFNCM5PropertyClass
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
public interface OwFNCM5EngineBinding<C, N, O>
{
    /**
     * Return the native P8 5.0 Data-Type representation as integer.
     * <p>Should be only used in adaptor only.</p>
     * @return int representing the property value type
     */
    int getType();

    /**
     * 
     * @param owdValue_p
     * @return an Object representing the P8 5.0 engine correspondent of 
     *         the given AWD value with respect to this property class and its value type    
     * @throws OwException
     */
    Object toNativeValue(Object owdValue_p) throws OwException;

    /**
     * 
     * @return P8 5.0 symbolic name of this property 
     */
    String getSymbolicName();

    /**
     * 
     * @param nativeProperty_p
     * @return am {@link OwFNCM5EngineProperty} representation of the given native {@link Property}
     * @throws OwException
     */
    OwFNCM5EngineProperty<N, O> from(Property nativeProperty_p) throws OwException;

    /**
     * 
     * @return property class that is engine bound by this binding
     * @throws OwException
     */
    C getEngineClass() throws OwException;

    /**
     * Can this property be used for ordering.
     * @return boolean specified by the P8 5.0 system
     */
    boolean isOrderable();

    /**
     * Can this property be used in search SELECT clause.
     * @return boolean defined by P8 5.0 system
     */
    boolean isSelectable();

    /**
     * Can this property be used in search WHERE clause.
     * @return boolean defined by P8 5.0 system
     */
    boolean isSearchable();
}
