package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwFormat;
import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * OwFNCM5ClassPropertyClass.
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
public class OwFNCM5ClassPropertyClass implements OwFNCM5PropertyClass
{
    private OwFNCM5Class<?, ?> objectClass;
    private OwFNCM5PropertyClass propertyClass;
    private OwRoleManager roleManager;

    public OwFNCM5ClassPropertyClass(OwFNCM5PropertyClass propertyClass_p, OwFNCM5Class<?, ?> objectClass_p, OwRoleManager roleManager_p)
    {
        super();
        this.objectClass = objectClass_p;
        this.propertyClass = propertyClass_p;
        this.roleManager = roleManager_p;
    }

    public Object getNativeType() throws OwException
    {
        return propertyClass.getNativeType();
    }

    public String getClassName()
    {
        return propertyClass.getClassName();
    }

    public String getDisplayName(Locale locale_p)
    {
        return propertyClass.getDisplayName(locale_p);
    }

    public String getDescription(Locale locale_p)
    {
        return propertyClass.getDescription(locale_p);
    }

    public boolean isSystemProperty() throws OwException
    {
        return propertyClass.isSystemProperty();
    }

    public boolean isNameProperty() throws Exception
    {
        return propertyClass.isNameProperty();
    }

    public String getJavaClassName()
    {
        return propertyClass.getJavaClassName();
    }

    public boolean isArray() throws OwException
    {
        return propertyClass.isArray();
    }

    public boolean isReadOnly(int iContext_p) throws OwException
    {
        if (propertyClass.isReadOnly(iContext_p))
        {
            return true;
        }
        else
        {
            return hasAccessMaskRight(iContext_p);
        }
    }

    public boolean isEnum() throws Exception
    {
        return propertyClass.isEnum();
    }

    private boolean hasAccessMaskRight(int iContext_p) throws OwException
    {
        try
        {
            // may override and hide a property
            switch (iContext_p)
            {
                case CONTEXT_NORMAL:
                    return !roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, Integer.toString(OwRoleManager.ROLE_RESOURCE_CONTEXT_VIEW) + "." + objectClass.getClassName() + "." + getClassName(),
                            OwRoleManager.ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_VIEW);
                case CONTEXT_ON_CREATE:
                    return !roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, Integer.toString(OwRoleManager.ROLE_RESOURCE_CONTEXT_CREATE) + "." + objectClass.getClassName() + "." + getClassName(),
                            OwRoleManager.ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_VIEW);
                case CONTEXT_ON_CHECKIN:
                    return !roleManager.hasAccessMaskRight(OwRoleManager.ROLE_CATEGORY_INDEX_FIELDS, Integer.toString(OwRoleManager.ROLE_RESOURCE_CONTEXT_CHECKIN) + "." + objectClass.getClassName() + "." + getClassName(),
                            OwRoleManager.ROLE_ACCESS_MASK_FLAG_INDEX_FIELD_VIEW);
                default:
                    return (false);
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Could not retrieve acces masc data.", e);
        }
    }

    public boolean isHidden(int iContext_p) throws OwException
    {
        if (propertyClass.isHidden(iContext_p))
        {
            return true;
        }
        else
        {
            return hasAccessMaskRight(iContext_p);
        }
    }

    public OwEnumCollection getEnums() throws Exception
    {
        return propertyClass.getEnums();
    }

    public String getCategory() throws Exception
    {
        return propertyClass.getCategory();
    }

    public boolean isRequired() throws Exception
    {
        return propertyClass.isRequired();
    }

    public Object getMaxValue() throws Exception
    {
        return propertyClass.getMaxValue();
    }

    public Object getMinValue() throws Exception
    {
        return propertyClass.getMinValue();
    }

    public Object getDefaultValue() throws Exception
    {
        return propertyClass.getDefaultValue();
    }

    public Object getValueFromNode(Node node_p) throws Exception
    {
        return propertyClass.getValueFromNode(node_p);
    }

    public Object getValueFromString(String text_p) throws Exception
    {
        return propertyClass.getValueFromString(text_p);
    }

    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {
        return propertyClass.getNodeFromValue(value_p, doc_p);
    }

    public OwFormat getFormat()
    {
        return propertyClass.getFormat();
    }

    public Collection getOperators() throws Exception
    {
        return propertyClass.getOperators();
    }

    public List getComplexChildClasses() throws Exception
    {
        return propertyClass.getComplexChildClasses();
    }

    public boolean isComplex()
    {
        return propertyClass.isComplex();
    }

    public OwFNCM5EngineBinding<?, ?, ?> getEngineBinding() throws OwException
    {
        return propertyClass.getEngineBinding();
    }

    public OwFNCM5VirtualBinding getVirtualBinding() throws OwException
    {
        return propertyClass.getVirtualBinding();
    }
}
