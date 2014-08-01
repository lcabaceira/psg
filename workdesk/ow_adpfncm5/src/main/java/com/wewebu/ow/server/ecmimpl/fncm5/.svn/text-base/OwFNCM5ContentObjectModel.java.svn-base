package com.wewebu.ow.server.ecmimpl.fncm5;

import java.util.Map;

import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * An object-oriented content meta-model abstraction.<br/>
 * Used for representing object-oriented meta-model content systems partially defined by 
 * independent native systems.
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
public interface OwFNCM5ContentObjectModel
{
    /**
     * 
     * @param propertyClassName_p
     * @return an {@link OwFNCM5PropertyClass} representation of the property class with the given name
     * @throws OwObjectNotFoundException if the property class was not found 
     * @throws OwObjectNotFoundException if the given property is not defined by this object model  
     * @throws OwException if an error occurred when fetching the class   
     */
    OwFNCM5PropertyClass propertyClassForName(String propertyClassName_p) throws OwException, OwObjectNotFoundException;

    /**
     * 
     * @param objectClassName_p
     * @return an {@link OwFNCM5Class} representation of the object class with the given name
     * @throws OwObjectNotFoundException if the given object class is not defined by this object model  
     * @throws OwException if an error occurred when fetching the class   
     */
    OwFNCM5Class<?, ?> objectClassForName(String objectClassName_p) throws OwException, OwObjectNotFoundException;

    /**
     * Maps native object class representations to model representations
     *    
     * @param nativeObject_p the native object class representation
     * @return an {@link OwFNCM5Class} representation of the given native object class representation 
     * @throws OwInvalidOperationException if the given native object class can not be mapped to an {@link OwFNCM5Class}  
     * @throws OwException if an error occurred when fetching the class
     */
    <N> OwFNCM5Class<N, ?> classOf(N nativeObject_p) throws OwException, OwInvalidOperationException;

    /** 
    * Gets the available object class display names 
    *
    * @param iTypes_p int array of Object types as defined in OwObject, if null to retrieve all class names
    * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
    * @param fRootOnly_p true = gets only the root classes if we deal with a class tree, false = gets all classes
    *
    * @return a {@link Map} of class display names mapped by their class names <br/>
    */
    Map<String, String> objectClassNamesWith(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p) throws OwException;

}
