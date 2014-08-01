package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.List;
import java.util.Set;

import com.filenet.api.core.Document;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5DomainResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Domain;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * An AWD object class template extension for the P8 5.0 adaptor.<br/>
 * The N template parameter is the native type of this class content-objects 
 * (for example N is {@link Document} for Document AWD classes).<br/>
 *The R resource parameter is the type of the resource that defines this class 
 *(for example {@link OwFNCM5ObjectStoreResource} for all {@link IndependentlyPersistableObject}s
 * and {@link OwFNCM5DomainResource} for {@link OwFNCM5Domain}s objects).      
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
public interface OwFNCM5Class<N, R extends OwFNCM5Resource> extends OwObjectClass
{
    /**
     * Identifier of the content object representation of this class.
     * 
     * @return the class-object identifier 
     */
    String getId();

    /**
     *Native to AWD object converter method. 
     * 
     * @param nativeObject_p 
     * @param factory_p the non-null object factory
     * @return a new {@link OwFNCM5Object} instance based on the given native object 
     * @throws OwException
     */
    OwFNCM5Object<N> from(N nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException;

    OwFNCM5PropertyClass getPropertyClass(String propertyClassName_p) throws OwException;

    List<String> getPropertyClassNames() throws OwException;

    OwFNCM5Class<N, R> getParent() throws OwException;

    String getNamePropertyName() throws OwException;

    /**
     * 
     * @return the {@link OwFNCM5Resource} that defines this object class
     * @throws OwException
     */
    R getResource() throws OwException;

    boolean canCreateNewObject() throws OwException;

    /**
     * 
     * @return the content object constructor of this content object class 
     * @throws OwException
     * @see OwNetwork#createNewObject(boolean, Object, OwResource, String, OwPropertyCollection, OwPermissionCollection, OwContentCollection, OwObject, String, String)
     * @see OwNetwork#createNewObject(boolean, Object, OwResource, String, OwPropertyCollection, OwPermissionCollection, OwContentCollection, OwObject, String, String, boolean)
     * @see OwNetwork#createNewObject(OwResource, String, OwPropertyCollection, OwPermissionCollection, OwContentCollection, OwObject, String, String)
     */
    OwFNCM5Constructor<N, R> getConstructor() throws OwException;

    boolean hasVersionSeries() throws OwException;

    /**
     * 
     * @return a {@link Set} of {@link OwFNCM5Object} representations of workflows  
     *         that can have objects of this content-object-class as target 
     * @throws OwException
     */
    Set<OwFNCM5Object<?>> getWorkflowDescriptions() throws OwException;
}
