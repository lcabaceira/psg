package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.filenet.api.collection.ClassDescriptionSet;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.core.EngineObject;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5DefaultPropertyClassFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClassFactory;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 *{@link ClassDescription}  class declaration.
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
public class OwFNCM5ClassDescription<R extends OwFNCM5Resource> implements OwFNCM5EngineClassDeclaration<ClassDescription, R>
{
    private ClassDescription description;
    private OwFNCM5PropertyClassFactory propertyClassFactory;

    public static <R extends OwFNCM5Resource> OwFNCM5ClassDescription<R> from(EngineObject object_p, OwFNCM5ResourceAccessor<R> resourceAccessor_p)
    {
        ClassDescription classDescription = object_p.get_ClassDescription();
        OwFNCM5DefaultPropertyClassFactory factory = new OwFNCM5DefaultPropertyClassFactory(resourceAccessor_p);
        return new OwFNCM5ClassDescription<R>(classDescription, factory);
    }

    public OwFNCM5ClassDescription(ClassDescription description_p, OwFNCM5PropertyClassFactory propertyClassFactory_p)
    {
        super();
        this.description = description_p;
        this.propertyClassFactory = propertyClassFactory_p;
    }

    public ClassDescription getNativeClass()
    {
        return description;
    }

    public boolean allowsInstances()
    {
        return description.get_AllowsInstances();
    }

    public String getSymbolicName()
    {
        return description.get_SymbolicName();
    }

    public String getSuperclassSymbolicName()
    {
        ClassDescription superclassDescription = description.get_SuperclassDescription();
        if (null != superclassDescription)
        {
            return superclassDescription.get_SymbolicName();
        }
        return null;
    }

    public String getDescriptiveText(Locale locale_p)
    {
        ClassDescription clazz = getNativeClass();
        return clazz.get_DescriptiveText();
    }

    public String getDisplayName(Locale locale_p)
    {
        ClassDescription clazz = getNativeClass();
        return clazz.get_DisplayName();
    }

    public boolean hasVersionSeries()
    {
        ClassDescription clazz = getNativeClass();
        return clazz.describedIsOfClass(ClassNames.DOCUMENT);
    }

    public boolean isHidden()
    {
        return this.description.get_IsHidden();
    }

    public String getId()
    {
        Id id = this.description.get_Id();
        return id.toString();
    }

    public final Map<String, OwFNCM5EnginePropertyClass<?, ?, ?>> getPropertyClasses() throws OwException
    {
        LinkedHashMap<String, OwFNCM5EnginePropertyClass<?, ?, ?>> propertyClasses = new LinkedHashMap<String, OwFNCM5EnginePropertyClass<?, ?, ?>>();

        PropertyDescriptionList descriptionsList = this.description.get_PropertyDescriptions();
        Iterator<?> propertyIt = descriptionsList.iterator();
        int index = 0;
        Integer nameIndex = this.description.get_NamePropertyIndex();
        while (propertyIt.hasNext())
        {
            PropertyDescription propertyDefintion = (PropertyDescription) propertyIt.next();
            OwFNCM5EnginePropertyClass<?, ?, ?> fncmClass = this.propertyClassFactory.createPropertyClass(propertyDefintion, nameIndex != null ? nameIndex.intValue() == index : false);
            propertyClasses.put(fncmClass.getClassName(), fncmClass);
            index++;
        }

        return propertyClasses;
    }

    public List<String> getSubclassesNames()
    {
        List<String> subclassNames = new LinkedList<String>();
        ClassDescription classDescription = getNativeClass();
        ClassDescriptionSet subclasses = classDescription.get_ImmediateSubclassDescriptions();
        Iterator si = subclasses.iterator();
        while (si.hasNext())
        {
            ClassDescription subclass = (ClassDescription) si.next();
            String subclassSymbolicName = subclass.get_SymbolicName();
            subclassNames.add(subclassSymbolicName);
        }
        return subclassNames;
    }
}
