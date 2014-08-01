package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.LocalizedString;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.VersionableClassDefinition;
import com.filenet.api.collection.LocalizedStringList;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.util.Id;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClassFactory;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 *{@link ClassDefinition} class declaration.
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
public class OwFNCM5ClassDefinition implements OwFNCM5EngineClassDeclaration<ClassDefinition, OwFNCM5ObjectStoreResource>
{

    static String descriptiveText(ClassDefinition definition_p, Locale locale_p)
    {
        LocalizedStringList texts = definition_p.get_DescriptiveTexts();
        Iterator ls = texts.iterator();
        while (ls.hasNext())
        {
            LocalizedString string = (LocalizedString) ls.next();
            if (locale_p.getLanguage().equals(string.get_LocaleName()))
            {
                return string.get_LocalizedText();
            }
        }
        return null;
    }

    static String displayName(ClassDefinition definition_p, Locale locale_p)
    {
        LocalizedStringList names = definition_p.get_DisplayNames();
        Iterator ls = names.iterator();
        while (ls.hasNext())
        {
            LocalizedString string = (LocalizedString) ls.next();
            if (locale_p.getLanguage().equals(string.get_LocaleName()))
            {
                return string.get_LocalizedText();
            }
        }
        return null;
    }

    private ClassDefinition definition;
    private OwFNCM5PropertyClassFactory propertyClassFactory;

    public OwFNCM5ClassDefinition(ClassDefinition definition_p, OwFNCM5PropertyClassFactory propertyClassFactory_p)
    {
        super();
        this.definition = definition_p;
        this.propertyClassFactory = propertyClassFactory_p;
    }

    public ClassDefinition getNativeClass()
    {
        return definition;
    }

    public boolean allowsInstances()
    {
        return definition.get_AllowsInstances();
    }

    public String getSymbolicName()
    {
        return definition.get_SymbolicName();
    }

    public String getSuperclassSymbolicName()
    {
        ClassDefinition superclassDefinition = definition.get_SuperclassDefinition();
        if (null != superclassDefinition)
        {
            return superclassDefinition.get_SymbolicName();
        }
        return null;
    }

    public String getDescriptiveText(Locale locale_p)
    {
        return descriptiveText(definition, locale_p);
    }

    public String getDisplayName(Locale locale_p)
    {
        return displayName(definition, locale_p);
    }

    public boolean hasVersionSeries()
    {
        return definition instanceof VersionableClassDefinition;
    }

    public boolean isHidden()
    {
        return definition.get_IsHidden();
    }

    public String getId()
    {
        Id id = definition.get_Id();
        return id.toString();
    }

    public Map<String, OwFNCM5EnginePropertyClass<?, ?, ?>> getPropertyClasses() throws OwException
    {
        Map<String, OwFNCM5EnginePropertyClass<?, ?, ?>> classes = new HashMap<String, OwFNCM5EnginePropertyClass<?, ?, ?>>();

        PropertyDefinitionList definitionsList = definition.get_PropertyDefinitions();
        Iterator propertyIt = definitionsList.iterator();
        while (propertyIt.hasNext())
        {
            PropertyDefinition propertyDefintion = (PropertyDefinition) propertyIt.next();

            OwFNCM5EnginePropertyClass<?, ?, ?> propertyClass = this.propertyClassFactory.createPropertyClass(propertyDefintion, true, true, true);
            classes.put(propertyClass.getClassName(), propertyClass);
        }

        return classes;
    }

    public List<String> getSubclassesNames()
    {
        return Collections.EMPTY_LIST;
    }
}
