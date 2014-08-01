package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.Iterator;
import java.util.Locale;

import com.filenet.api.admin.LocalizedString;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.collection.LocalizedStringList;
import com.filenet.api.constants.Cardinality;
import com.filenet.api.constants.PropertySettability;
import com.filenet.api.constants.TypeID;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.choice.OwFNCM5EnumCacheHandler;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwFNCM5DefinitionPropertyClass.
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
public abstract class OwFNCM5DefinitionPropertyClass<C extends PropertyDefinition, N, O> extends OwFNCM5EnginePropertyClass<C, N, O>
{
    private boolean orderable;
    private boolean searchable;
    private boolean selectable;

    public OwFNCM5DefinitionPropertyClass(C nativeClass_p, OwFNCM5ValueConverterClass<N, O> converterClass_p, boolean orderable_p, boolean searchable_p, boolean selectable_p, String preferredType_p)
    {
        super(nativeClass_p, converterClass_p, preferredType_p);
        this.orderable = orderable_p;
        this.searchable = searchable_p;
        this.selectable = selectable_p;
    }

    public boolean isOrderable()
    {
        return orderable;
    }

    public boolean isSearchable()
    {
        return searchable;
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

    public int getType()
    {
        TypeID dataType = this.nativeClass.get_DataType();
        return dataType.getValue();
    }

    @Override
    protected boolean isEngineSystemProperty() throws OwException
    {
        return this.nativeClass.get_IsSystemOwned();
    }

    public boolean isNameProperty() throws Exception
    {
        return this.nativeClass.get_IsNameProperty();
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

    private String localizedString(LocalizedStringList list_p, Locale locale_p)
    {
        String lString = null;
        if (locale_p != null)
        {

            Iterator i = list_p.iterator();
            while (i.hasNext())
            {
                LocalizedString string = (LocalizedString) i.next();
                if (locale_p.getLanguage().equals(string.get_LocaleName()))
                {
                    lString = string.get_LocalizedText();
                    break;
                }
            }
        }

        return lString;
    }

    public String getDisplayName(Locale locale_p)
    {
        String displayName = null;

        if (locale_p != null)
        {
            // Try AWD localization
            displayName = OwString.localizeLabel(locale_p, this.nativeClass.get_SymbolicName(), null);
            if (null == displayName)
            {
                // Try native localization
                PropertyTemplate template = this.nativeClass.get_PropertyTemplate();
                LocalizedStringList names = template.get_DisplayNames();
                displayName = localizedString(names, locale_p);
            }
        }

        if (displayName == null)
        {
            displayName = this.nativeClass.get_DisplayName();
        }

        return displayName;
    }

    public String getDescription(Locale locale_p)
    {
        String description = null;

        if (locale_p != null)
        {
            PropertyTemplate template = this.nativeClass.get_PropertyTemplate();
            LocalizedStringList texts = template.get_DescriptiveTexts();
            description = localizedString(texts, locale_p);
        }

        if (description == null)
        {
            description = this.nativeClass.get_DescriptiveText();
        }

        return description;
    }

    public boolean isEnum() throws OwException
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
        return this.nativeClass.get_IsValueRequired();
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

    public boolean isSelectable()
    {
        return selectable;
    }

    public String getSymbolicName()
    {
        C definition = getEngineClass();
        return definition.get_SymbolicName();
    }
}
