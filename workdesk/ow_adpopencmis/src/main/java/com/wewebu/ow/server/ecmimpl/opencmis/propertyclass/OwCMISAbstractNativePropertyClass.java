package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.chemistry.opencmis.client.api.ObjectFactory;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.definitions.Choice;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.Cardinality;
import org.apache.chemistry.opencmis.commons.enums.Updatability;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISValueConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISNativeProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISNativePropertyImpl;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwStandardEnum;
import com.wewebu.ow.server.field.OwStandardEnumCollection;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwCMISAbstractNativePropertyClass.
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
public abstract class OwCMISAbstractNativePropertyClass<O, N, P extends PropertyDefinition<N>> extends OwCMISAbstractPropertyClass<O, OwCMISNativeObjectClass<?, ?>> implements OwCMISNativePropertyClass<O, N, P>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractNativePropertyClass.class);

    private static final List<String> NAME_PROPERTIES = new ArrayList<String>();

    static
    {
        NAME_PROPERTIES.add(PropertyIds.NAME);
        NAME_PROPERTIES.add("bpm:description");

    }

    private P propertyDefinition;
    private OwCMISValueConverter<N, O> converter;
    private Class<O> javaClass;
    private Collection<Integer> operators;

    public OwCMISAbstractNativePropertyClass(String className, P propertyDefinition, OwCMISValueConverter<N, O> converter, Class<O> javaClass, Collection<Integer> operators, OwCMISNativeObjectClass<?, ?> objectClass)
    {
        super(className, objectClass);
        this.propertyDefinition = propertyDefinition;
        this.converter = converter;
        this.javaClass = javaClass;
        this.operators = operators;
    }

    @Override
    public final Collection<Integer> getOperators() throws OwException
    {
        return operators;
    }

    protected OwCMISValueConverter<N, O> getConverter()
    {
        return converter;
    }

    protected Property<N> newProperty(List<N> nativeValues_p)
    {
        OwCMISNativeSession session = getObjectClass().getSession();
        Session nativeSession = session.getOpenCMISSession();
        ObjectFactory objectFactory = nativeSession.getObjectFactory();
        return objectFactory.createProperty(getNativeType(), nativeValues_p);
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwCMISNativeProperty<O, N> from(O... value_p) throws OwException
    {

        List<N> nativeValues = new LinkedList<N>();
        if (null != value_p)
        {
            if (isArray())
            {
                if (1 == value_p.length)
                {
                    nativeValues = converter.fromArrayValue((O[]) value_p[0]);
                }
                else
                {
                    nativeValues = converter.fromArrayValue(value_p);
                }
            }
            else
            {
                O singleValue = null;
                if (value_p.length > 0)
                {
                    singleValue = value_p[0];
                }
                nativeValues = converter.fromValue(singleValue);
            }
        }

        return new OwCMISNativePropertyImpl<O, N>(this, newProperty(nativeValues), converter);
    }

    @Override
    public OwCMISNativeProperty<O, N> from(Property<N> property_p) throws OwException
    {
        if (property_p == null)
        {
            property_p = newProperty(new LinkedList<N>());
        }
        return new OwCMISNativePropertyImpl<O, N>(this, property_p, converter);
    }

    @Override
    public Object getDefaultValue() throws OwException
    {
        List<N> nativeDefault = getNativeType().getDefaultValue();
        if (isArray())
        {
            return converter.toArrayValue(nativeDefault);
        }
        else
        {
            return converter.toValue(nativeDefault);
        }
    }

    @Override
    public boolean isSystem() throws OwException
    {

        return propertyDefinition.getUpdatability().equals(Updatability.READONLY);
        //            String id = propertyDefinition.getId();
        //            return OwCMISPropertyId.isDefined(id) && OwCMISPropertyId.fromId(id).hasAttribute(OwCMISPropertyAttribute.SYSTEM);
    }

    @Override
    public boolean isNameProperty() throws OwException
    {
        return NAME_PROPERTIES.contains(propertyDefinition.getId());
    }

    @Override
    public boolean isReadOnly(int context_p) throws OwException
    {
        switch (propertyDefinition.getUpdatability())
        {
            case READONLY:
                return true;

            case READWRITE:
                return false;

            case WHENCHECKEDOUT:
                return context_p != OwPropertyClass.CONTEXT_ON_CHECKIN;

            case ONCREATE:
                return context_p != OwPropertyClass.CONTEXT_ON_CREATE;

            default:
                return false;
        }

    }

    @Override
    public String getDisplayName(Locale locale_p)
    {
        return OwString.localizeLabel(locale_p, getClassName(), propertyDefinition.getDisplayName());
    }

    @Override
    public String getDescription(Locale locale_p)
    {
        return OwString.localizeLabel(locale_p, getClassName() + ".Desc", propertyDefinition.getDescription());
    }

    @Override
    public P getNativeType()
    {
        return propertyDefinition;
    }

    @Override
    public boolean isRequired() throws OwException
    {
        Boolean val = propertyDefinition.isRequired();
        if (val != null)
        {
            return val.booleanValue();
        }
        else
        {
            LOG.warn("Undefined isRequired state for propDef = " + propertyDefinition.getId());
            return false;
        }
    }

    @Override
    public boolean isArray()
    {
        return Cardinality.MULTI.equals(propertyDefinition.getCardinality());
    }

    @Override
    public String getNonQualifiedName()
    {
        return propertyDefinition.getId();
    }

    @Override
    public String getQueryName()
    {
        return propertyDefinition.getQueryName();
    }

    @Override
    public boolean isQueryable()
    {
        Boolean val = propertyDefinition.isQueryable();
        if (val != null)
        {
            return val.booleanValue();
        }
        else
        {
            LOG.warn("Undefined isQueryable state for propDef = " + propertyDefinition.getId());
            return false;
        }
    }

    @Override
    public boolean isOrderable()
    {
        Boolean val = propertyDefinition.isOrderable();
        if (val != null)
        {
            return val.booleanValue();
        }
        else
        {
            LOG.warn("Undefined isOrderable state for propDef = " + propertyDefinition.getId());
            return false;
        }
    }

    @Override
    public OwEnumCollection getEnums() throws OwException
    {
        List<Choice<N>> nativeChoices = getNativeType().getChoices();
        OwStandardEnumCollection enumCollection = new OwStandardEnumCollection();
        if (nativeChoices != null)
        {
            for (Choice<N> choice : nativeChoices)
            {
                List<N> nativeChoiceValue = choice.getValue();
                Object value = null;
                value = converter.toValue(nativeChoiceValue);

                String localizeKey = getFullQualifiedName() + "." + choice.getDisplayName();
                OwStandardEnum stdEnum = new OwStandardEnum(value, choice.getDisplayName(), localizeKey);
                enumCollection.add(stdEnum);
            }
        }

        return enumCollection;
    }

    @Override
    public final String getJavaClassName()
    {
        return javaClass.getName();
    }

    @Override
    public boolean isHidden(int iContext_p) throws OwException
    {
        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("class(native,");
        builder.append(getClassName());
        builder.append(",");
        builder.append(getClass().getSimpleName());
        String hiddenString = "<err-hidden-status>";

        try
        {
            hiddenString = isHidden(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS) ? "hidden" : "visible";
        }
        catch (Exception e)
        {
            LOG.error("Could not retrieve the hidden status of the class !", e);
        }
        builder.append(",");
        builder.append(hiddenString);
        builder.append(",queryable=");
        builder.append(isQueryable());
        builder.append(")");
        return builder.toString();
    }
}
