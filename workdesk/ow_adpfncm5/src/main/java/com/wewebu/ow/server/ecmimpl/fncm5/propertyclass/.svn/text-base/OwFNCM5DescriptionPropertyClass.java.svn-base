package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Locale;

import com.filenet.api.constants.Cardinality;
import com.filenet.api.constants.PropertySettability;
import com.filenet.api.meta.PropertyDescription;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.choice.OwFNCM5EnumCacheHandler;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Base class for native P8 PropertyDescription abstraction.
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
public abstract class OwFNCM5DescriptionPropertyClass<C extends PropertyDescription, N, O> extends OwFNCM5EnginePropertyClass<C, N, O>
{
    private boolean nameProperty;

    public OwFNCM5DescriptionPropertyClass(C propertyDescription_p, OwFNCM5ValueConverterClass<N, O> converterClass_p, boolean nameProperty_p, String preferredType_p)
    {
        super(propertyDescription_p, converterClass_p, preferredType_p);
        this.nameProperty = nameProperty_p;
        //TODO dummy solution, must be refactored
        //        setNameProperty(propertyDesc.get_SymbolicName().equals("DocumentTitle") || propertyDesc.get_SymbolicName().equals("FolderName"));
    }

    @Override
    protected boolean isEngineSystemProperty() throws OwException
    {
        return this.nativeClass.get_IsSystemGenerated();
    }

    public boolean isNameProperty() throws OwException
    {
        return this.nameProperty;
    }

    public boolean isReadOnly(int iContext_p) throws OwException
    {
        if (this.nativeClass.get_IsReadOnly())
        {
            return true;
        }
        else
        {
            PropertySettability setability = this.nativeClass.get_Settability();
            return isReadOnly(setability, iContext_p);
        }

    }

    public boolean isHidden(int iContext_p)
    {
        return this.nativeClass.get_IsHidden();
    }

    public String getCategory() throws OwException
    {
        return null;
    }

    public String getClassName()
    {
        return this.nativeClass.get_SymbolicName();
    }

    public String getDisplayName(Locale locale_p)
    {
        return OwString.localizeLabel(locale_p, getClassName(), this.nativeClass.get_DisplayName());
    }

    public String getDescription(Locale locale_p)
    {
        return OwString.localize(locale_p, OwString.LABEL_PREFIX + getClassName() + ".Desc", this.nativeClass.get_DescriptiveText());
    }

    public boolean isEnum() throws OwException
    {
        return this.nativeClass.get_ChoiceList() != null;
    }

    public OwEnumCollection getEnums() throws OwException
    {
        OwFNCM5EnumCacheHandler cache = OwFNCM5Network.localNetwork().getNetworkCache().getCacheHandler(OwFNCM5EnumCacheHandler.class);
        return cache.getEnumCollection(this.nativeClass.get_ChoiceList());
    }

    public boolean isRequired() throws OwException
    {
        return this.nativeClass.get_IsValueRequired().booleanValue();
    }

    public Object getMaxValue() throws OwException
    {
        return null;
    }

    public Object getMinValue() throws OwException
    {
        return null;
    }

    public Object getDefaultValue() throws OwException
    {
        return null;
    }

    public boolean isNativeList()
    {
        Cardinality cardinality = this.nativeClass.get_Cardinality();
        return Cardinality.LIST.equals(cardinality);
    }

    public boolean isNativeEnum()
    {
        Cardinality cardinality = this.nativeClass.get_Cardinality();
        return Cardinality.ENUM.equals(cardinality);
    }

    public boolean isOrderable()
    {
        return this.nativeClass.get_IsOrderable();
    }

    public boolean isSelectable()
    {
        return this.nativeClass.get_IsSelectable();
    }

    public boolean isSearchable()
    {
        return this.nativeClass.get_IsSearchable();
    }

    public int getType()
    {
        return this.nativeClass.get_DataType().getValue();
    }

    @Override
    public int hashCode()
    {
        return getClassName().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj != null)
        {
            if (obj instanceof OwFNCM5DescriptionPropertyClass<?, ?, ?>)
            {
                OwFNCM5DescriptionPropertyClass<?, ?, ?> compare = (OwFNCM5DescriptionPropertyClass<?, ?, ?>) obj;
                if (compare.nativeClass instanceof PropertyDescription)
                {
                    return ((PropertyDescription) compare.nativeClass).get_Id().equals(this.nativeClass.get_Id());
                }
            }
        }
        return false;
    }

    public String getSymbolicName()
    {
        C description = getEngineClass();
        return description.get_SymbolicName();
    }
}
