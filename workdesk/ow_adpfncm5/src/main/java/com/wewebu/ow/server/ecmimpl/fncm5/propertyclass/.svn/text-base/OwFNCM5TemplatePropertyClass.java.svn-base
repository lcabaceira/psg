package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Locale;

import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.constants.Cardinality;
import com.filenet.api.constants.PropertySettability;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.choice.OwFNCM5EnumCacheHandler;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwFNCM5TemplatePropertyClass.
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
public abstract class OwFNCM5TemplatePropertyClass<C extends PropertyTemplate, N, O> extends OwFNCM5EnginePropertyClass<C, N, O>
{

    public OwFNCM5TemplatePropertyClass(C nativeClass_p, OwFNCM5ValueConverterClass<N, O> converterClass_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, preferredType_p);
    }

    public boolean isOrderable()
    {
        return false;
    }

    public boolean isSelectable()
    {
        return false;
    }

    public boolean isSearchable()
    {
        return false;
    }

    public int getType()
    {
        return this.nativeClass.get_DataType().getValue();
    }

    @Override
    protected boolean isEngineSystemProperty() throws OwException
    {
        return false;
    }

    public boolean isNameProperty() throws Exception
    {
        return this.nativeClass.get_IsNameProperty();
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

    public boolean isReadOnly(int iContext_p) throws OwException
    {
        PropertySettability setability = this.nativeClass.get_Settability();
        return isReadOnly(setability, iContext_p);
    }

    public boolean isHidden(int iContext_p)
    {
        return this.nativeClass.get_IsHidden();
    }

    public String getCategory() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getClassName()
    {
        return this.nativeClass.get_SymbolicName();
    }

    public String getDisplayName(Locale locale_p)
    {
        return OwString.localizeLabel(locale_p, this.nativeClass.get_SymbolicName(), this.nativeClass.get_DisplayName());
    }

    public String getDescription(Locale locale_p)
    {
        return this.nativeClass.get_DescriptiveText();
    }

    public boolean isEnum() throws Exception
    {
        return this.nativeClass.get_ChoiceList() != null;
    }

    public OwEnumCollection getEnums() throws Exception
    {
        OwFNCM5EnumCacheHandler cache = OwFNCM5Network.localNetwork().getNetworkCache().getCacheHandler(OwFNCM5EnumCacheHandler.class);
        return cache.getEnumCollection(this.nativeClass.get_ChoiceList());
    }

    public boolean isRequired() throws Exception
    {
        return this.nativeClass.get_IsValueRequired().booleanValue();
    }

    public Object getMaxValue() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getMinValue() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getDefaultValue() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSymbolicName()
    {
        C template = getEngineClass();
        return template.get_SymbolicName();
    }
}
