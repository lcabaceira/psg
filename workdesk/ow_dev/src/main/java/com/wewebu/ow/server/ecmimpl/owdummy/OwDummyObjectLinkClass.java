package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwVirtualLinkPropertyClasses;
import com.wewebu.ow.server.field.OwEnum;

/**
 *<p>
 * Dummy link class implementation.
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
 *@since 4.1.1.0
 */
public final class OwDummyObjectLinkClass implements OwObjectClass
{

    public static final String DEFAULT_LINK_CLASS = "Link";

    private static Map<String, OwDummyObjectLinkClass> instances = new HashMap<String, OwDummyObjectLinkClass>();

    public static final synchronized OwDummyObjectLinkClass getNamedInstance(String className)
    {
        OwDummyObjectLinkClass clazz = instances.get(className);
        if (clazz == null)
        {
            clazz = new OwDummyObjectLinkClass(className);
            instances.put(className, clazz);

        }

        return clazz;
    }

    public static final synchronized OwDummyObjectLinkClass getDefaultInstance()
    {
        return getNamedInstance(DEFAULT_LINK_CLASS);
    }

    /** map containing the property class descriptions of the class */
    protected Map<String, OwPropertyClass> propertyClassesMap = new HashMap<String, OwPropertyClass>();
    private String name;

    private OwDummyObjectLinkClass(String name)
    {
        this.name = name;
        propertyClassesMap.put(OwResource.m_ClassDescriptionPropertyClass.getClassName(), OwResource.m_ClassDescriptionPropertyClass);
        propertyClassesMap.put(OwResource.m_ObjectNamePropertyClass.getClassName(), OwResource.m_ObjectNamePropertyClass);
        propertyClassesMap.put(OwVirtualLinkPropertyClasses.LINK_SOURCE.getClassName(), OwVirtualLinkPropertyClasses.LINK_SOURCE);
        propertyClassesMap.put(OwVirtualLinkPropertyClasses.LINK_TARGET.getClassName(), OwVirtualLinkPropertyClasses.LINK_TARGET);
    }

    @Override
    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_LINK;
    }

    @Override
    public List<OwObjectClass> getChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws Exception
    {
        return null;
    }

    @Override
    public Map<String, OwObjectClass> getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p) throws Exception
    {
        return false;
    }

    @Override
    public String getClassName()
    {
        return name;
    }

    @Override
    public String getDisplayName(Locale locale_p)
    {
        return getClassName();
    }

    @Override
    public OwPropertyClass getPropertyClass(String strClassName_p) throws Exception
    {
        return propertyClassesMap.get(strClassName_p);
    }

    @Override
    public Collection<String> getPropertyClassNames() throws Exception
    {
        return propertyClassesMap.keySet();
    }

    @Override
    public String getNamePropertyName() throws Exception
    {
        return OwResource.m_ObjectNamePropertyClass.getClassName();
    }

    @Override
    public boolean canCreateNewObject() throws Exception
    {
        return false;
    }

    @Override
    public boolean hasVersionSeries() throws Exception
    {
        return false;
    }

    @Override
    public List<OwEnum> getModes(int operation_p) throws Exception
    {
        return null;
    }

    @Override
    public String getDescription(Locale locale_p)
    {
        return getClassName();
    }

    @Override
    public boolean isHidden() throws Exception
    {
        return false;
    }

    @Override
    public OwObjectClass getParent() throws Exception
    {
        return null;
    }

    @Override
    public String toString()
    {
        return name;
    }
}