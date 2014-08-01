package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.meta.ClassDescription;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Class specification adapter.<br/>
 * The P8 5.0 engine API has two class declaring interfaces : {@link ClassDescription} and {@link ClassDefinition}.
 * The two interfaces can be uniformly accessed through the OwFNCM5ClassDeclaration.      
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
public interface OwFNCM5EngineClassDeclaration<C, R extends OwFNCM5Resource>
{
    /**
     * 
     * @return the native class specification 
     */
    C getNativeClass();

    /**
     * 
     * Indicates whether an application can create an object instance of the declared class. 
     * If this property is false, only the Content Engine server can instantiate the class.
     *  
     * @return true if an application can create an object instance of a given class <br/>
     *         false otherwise 
     * @see ClassDefinition#get_AllowsInstances() and {@link ClassDescription#get_AllowsInstances()}
     */
    boolean allowsInstances();

    /**
     * Retrieve the programmatic identifier for the declared class.
     * 
     * @return a String symbolic name of the declared class 
     * @see  ClassDefinition#get_SymbolicName() and {@link ClassDescription#get_SymbolicName()}
     */
    String getSymbolicName();

    /**
     * Retrieve the programmatic identifier for the parent class.
     * 
     * @return a String symbolic name of this class' parent
     * @see ClassDescription#get_SuperclassDescription() and {@link ClassDefinition#get_SuperclassDefinition()}  
     * 
     */
    String getSuperclassSymbolicName();

    /**
     * 
     * @param locale_p
     * @return User-readable text that describes the declared class.
     * @see ClassDescription#get_DescriptiveText() and ClassDefinition#get_DescriptiveText()
     */
    String getDescriptiveText(Locale locale_p);

    /**
     * 
     * @param locale_p
     * @return the localized, user-readable, provider-specific name of the declared class. 
     * @see ClassDescription#get_DisplayName() and ClassDefinition#get_DisplayName()
     */
    String getDisplayName(Locale locale_p);

    /**
     * 
     * @return true if objects of the declared class have version series <br/> 
     *         false otherwise  
     */
    boolean hasVersionSeries();

    /**
     * Indicates whether the declared class should be hidden from non-administrative users (true) or not (false).
     *  
     * @return true if the declared class is hidden <br/>
     *         false otherwise
     * @see ClassDescription#get_IsHidden() and ClassDefinition#get_IsHidden()          
     */
    boolean isHidden();

    /**
     * A representation of the Globally Unique Identifier (GUID), a unique 128-bit number, 
     * that is assigned to this Content Engine class-object when the object was created.
     *  
     * @return the GUID  of the  Content Engine {@link ClassDescription} or {@link ClassDefinition} 
     *         object associated with this class declaration 
     */
    String getId();

    /**
     * 
     * @return {@link OwFNCM5PropertyClass} representations of the native properties of 
     *         the declared class mapped by their symbolic names 
     * @throws OwException 
     * 
     */
    Map<String, OwFNCM5EnginePropertyClass<?, ?, ?>> getPropertyClasses() throws OwException;

    /**
     * 
     * @return a List of symbolic names of declared class subclasses 
     */
    List<String> getSubclassesNames();
}
