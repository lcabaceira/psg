package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.util.TimeZone;

import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyDefinitionBinary;
import com.filenet.api.admin.PropertyDefinitionBoolean;
import com.filenet.api.admin.PropertyDefinitionDateTime;
import com.filenet.api.admin.PropertyDefinitionFloat64;
import com.filenet.api.admin.PropertyDefinitionId;
import com.filenet.api.admin.PropertyDefinitionInteger32;
import com.filenet.api.admin.PropertyDefinitionObject;
import com.filenet.api.admin.PropertyDefinitionString;
import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.admin.PropertyTemplateBinary;
import com.filenet.api.admin.PropertyTemplateBoolean;
import com.filenet.api.admin.PropertyTemplateDateTime;
import com.filenet.api.admin.PropertyTemplateFloat64;
import com.filenet.api.admin.PropertyTemplateId;
import com.filenet.api.admin.PropertyTemplateInteger32;
import com.filenet.api.admin.PropertyTemplateObject;
import com.filenet.api.admin.PropertyTemplateString;
import com.filenet.api.constants.TypeID;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.meta.PropertyDescriptionBinary;
import com.filenet.api.meta.PropertyDescriptionBoolean;
import com.filenet.api.meta.PropertyDescriptionDateTime;
import com.filenet.api.meta.PropertyDescriptionFloat64;
import com.filenet.api.meta.PropertyDescriptionId;
import com.filenet.api.meta.PropertyDescriptionInteger32;
import com.filenet.api.meta.PropertyDescriptionObject;
import com.filenet.api.meta.PropertyDescriptionString;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.converter.OwFNCM5DateValueConverterClass;
import com.wewebu.ow.server.ecmimpl.fncm5.converter.OwFNCM5IdValueConverterClass;
import com.wewebu.ow.server.ecmimpl.fncm5.converter.OwFNCM5SameTypeConverterClass;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ObjectValueConverterClass;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ResourceAccessor;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5DefaultPropertyClassFactory.
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
public class OwFNCM5DefaultPropertyClassFactory implements OwFNCM5PropertyClassFactory
{
    private OwFNCM5ResourceAccessor<? extends OwFNCM5Resource> resourceAccessor;

    public OwFNCM5DefaultPropertyClassFactory(OwFNCM5ResourceAccessor<? extends OwFNCM5Resource> resourceAccessor_p)
    {
        this.resourceAccessor = resourceAccessor_p;
    }

    protected OwFNCM5Network getNetwork() throws OwException
    {
        OwFNCM5Resource resource = this.resourceAccessor.get();
        return resource.getNetwork();
    }

    protected OwNetworkContext getContext() throws OwException
    {
        OwFNCM5Network network = getNetwork();
        return network.getContext();
    }

    protected OwFNCM5DateValueConverterClass createDateConverter() throws OwException
    {
        OwFNCM5Network network = getNetwork();
        OwNetworkContext context = network.getContext();
        TimeZone clientTimeZone = context.getClientTimeZone();
        OwFNCM5DateValueConverterClass dateConverter = new OwFNCM5DateValueConverterClass(TimeZone.getDefault(), clientTimeZone);
        return dateConverter;
    }

    protected String getPreferredPropertyType(String propertyName_p) throws OwException
    {
        OwFNCM5Network network = getNetwork();
        return network.getPreferredPropertyType(propertyName_p);
    }

    public <C extends PropertyDescription> OwFNCM5EnginePropertyClass<C, ?, ?> createPropertyClass(C propertyDescription_p, boolean nameProperty_p) throws OwException
    {
        /*TODO check if it reference a choicelist, create objectstore base choicelist cache*/
        OwFNCM5PropertyClass fncmClass = null;
        TypeID dataType = propertyDescription_p.get_DataType();
        int type = dataType.getValue();
        String preferredType = getPreferredPropertyType(propertyDescription_p.get_SymbolicName());
        switch (type)
        {

        /*TODO: Should binary be handled as Content stream?*/
            case TypeID.BINARY_AS_INT:
                fncmClass = new OwFNCM5ByteDescription((PropertyDescriptionBinary) propertyDescription_p, OwFNCM5SameTypeConverterClass.BYTE_CONVERTER, preferredType);
                break;
            case TypeID.BOOLEAN_AS_INT:
                fncmClass = new OwFNCM5BooleanDescription((PropertyDescriptionBoolean) propertyDescription_p, OwFNCM5SameTypeConverterClass.BOOLEAN_CONVERTER, preferredType);
                break;
            case TypeID.DATE_AS_INT:
            {
                OwFNCM5DateValueConverterClass dateConverter = createDateConverter();
                fncmClass = new OwFNCM5DateDescription((PropertyDescriptionDateTime) propertyDescription_p, dateConverter, nameProperty_p, preferredType);
            }
                break;
            case TypeID.DOUBLE_AS_INT:
                fncmClass = new OwFNCM5DoubleDescription((PropertyDescriptionFloat64) propertyDescription_p, OwFNCM5SameTypeConverterClass.DOUBLE_CONVERTER, nameProperty_p, preferredType);
                break;
            case TypeID.LONG_AS_INT:
                fncmClass = new OwFNCM5IntegerDescription((PropertyDescriptionInteger32) propertyDescription_p, OwFNCM5SameTypeConverterClass.INTEGER_CONVERTER, nameProperty_p, preferredType);
                break;
            case TypeID.GUID_AS_INT:
                fncmClass = new OwFNCM5IdDescription((PropertyDescriptionId) propertyDescription_p, OwFNCM5IdValueConverterClass.CLASS, nameProperty_p, preferredType);
                break;
            case TypeID.STRING_AS_INT:
                fncmClass = new OwFNCM5StringDescription((PropertyDescriptionString) propertyDescription_p, OwFNCM5SameTypeConverterClass.STRING_CONVERTER, nameProperty_p, preferredType);
                break;
            default:
            {
                OwFNCM5ObjectValueConverterClass converter = new OwFNCM5ObjectValueConverterClass(resourceAccessor);
                fncmClass = new OwFNCM5ObjectDescription((PropertyDescriptionObject) propertyDescription_p, converter, nameProperty_p, preferredType);
            }
                break;
        }

        return (OwFNCM5EnginePropertyClass<C, ?, ?>) fncmClass;
    }

    public <T extends PropertyTemplate> OwFNCM5EnginePropertyClass<T, ?, ?> createPropertyClass(T propertyTemplate_p) throws OwException
    {

        OwFNCM5PropertyClass fncmClass = null;
        TypeID dataType = propertyTemplate_p.get_DataType();
        int type = dataType.getValue();
        String preferredType = getPreferredPropertyType(propertyTemplate_p.get_SymbolicName());

        switch (type)
        {

            case TypeID.BINARY_AS_INT:
                fncmClass = new OwFNCM5ByteTemplate((PropertyTemplateBinary) propertyTemplate_p, OwFNCM5SameTypeConverterClass.BYTE_CONVERTER, preferredType);
                break;
            case TypeID.BOOLEAN_AS_INT:
                fncmClass = new OwFNCM5BooleanTemplate((PropertyTemplateBoolean) propertyTemplate_p, OwFNCM5SameTypeConverterClass.BOOLEAN_CONVERTER, preferredType);
                break;
            case TypeID.DATE_AS_INT:
            {
                OwFNCM5DateValueConverterClass dateConverter = createDateConverter();
                fncmClass = new OwFNCM5DateTemplate((PropertyTemplateDateTime) propertyTemplate_p, dateConverter, preferredType);
            }
                break;
            case TypeID.DOUBLE_AS_INT:
                fncmClass = new OwFNCM5DoubleTemplate((PropertyTemplateFloat64) propertyTemplate_p, OwFNCM5SameTypeConverterClass.DOUBLE_CONVERTER, preferredType);
                break;
            case TypeID.GUID_AS_INT:
                fncmClass = new OwFNCM5IdTemplate((PropertyTemplateId) propertyTemplate_p, OwFNCM5IdValueConverterClass.CLASS, preferredType);
                break;
            case TypeID.LONG_AS_INT:
                fncmClass = new OwFNCM5IntegerTemplate((PropertyTemplateInteger32) propertyTemplate_p, OwFNCM5SameTypeConverterClass.INTEGER_CONVERTER, preferredType);
                break;
            case TypeID.STRING_AS_INT:
                fncmClass = new OwFNCM5StringTemplate((PropertyTemplateString) propertyTemplate_p, OwFNCM5SameTypeConverterClass.STRING_CONVERTER, preferredType);
                break;
            default:
            {
                OwFNCM5ObjectValueConverterClass converter = new OwFNCM5ObjectValueConverterClass(resourceAccessor);
                fncmClass = new OwFNCM5ObjectTemplate((PropertyTemplateObject) propertyTemplate_p, converter, preferredType);
            }
                break;

        }

        return (OwFNCM5EnginePropertyClass<T, ?, ?>) fncmClass;
    }

    public <D extends PropertyDefinition> OwFNCM5EnginePropertyClass<D, ?, ?> createPropertyClass(D propertyDefinition_p, boolean orderable_p, boolean searchable_p, boolean selectable_p) throws OwException
    {
        OwFNCM5PropertyClass fncmClass = null;
        TypeID dataType = propertyDefinition_p.get_DataType();
        int type = dataType.getValue();
        String preferredType = getPreferredPropertyType(propertyDefinition_p.get_SymbolicName());

        switch (type)
        {

            case TypeID.BINARY_AS_INT:
                fncmClass = new OwFNCM5ByteDefinition((PropertyDefinitionBinary) propertyDefinition_p, OwFNCM5SameTypeConverterClass.BYTE_CONVERTER, orderable_p, searchable_p, selectable_p, preferredType);
                break;
            case TypeID.BOOLEAN_AS_INT:
                fncmClass = new OwFNCM5BooleanDefinition((PropertyDefinitionBoolean) propertyDefinition_p, OwFNCM5SameTypeConverterClass.BOOLEAN_CONVERTER, orderable_p, searchable_p, selectable_p, preferredType);
                break;
            case TypeID.DATE_AS_INT:
            {
                OwFNCM5DateValueConverterClass dateConverter = createDateConverter();
                fncmClass = new OwFNCM5DateDefinition((PropertyDefinitionDateTime) propertyDefinition_p, dateConverter, orderable_p, searchable_p, selectable_p, preferredType);
            }
                break;
            case TypeID.DOUBLE_AS_INT:
                fncmClass = new OwFNCM5DoubleDefinition((PropertyDefinitionFloat64) propertyDefinition_p, OwFNCM5SameTypeConverterClass.DOUBLE_CONVERTER, orderable_p, searchable_p, selectable_p, preferredType);
                break;
            case TypeID.GUID_AS_INT:
                fncmClass = new OwFNCM5IdDefinition((PropertyDefinitionId) propertyDefinition_p, OwFNCM5IdValueConverterClass.CLASS, orderable_p, searchable_p, selectable_p, preferredType);
                break;
            case TypeID.LONG_AS_INT:
                fncmClass = new OwFNCM5IntegerDefinition((PropertyDefinitionInteger32) propertyDefinition_p, OwFNCM5SameTypeConverterClass.INTEGER_CONVERTER, orderable_p, searchable_p, selectable_p, preferredType);
                break;
            case TypeID.STRING_AS_INT:
                fncmClass = new OwFNCM5StringDefinition((PropertyDefinitionString) propertyDefinition_p, OwFNCM5SameTypeConverterClass.STRING_CONVERTER, orderable_p, searchable_p, selectable_p, preferredType);
                break;
            default:
            {
                OwFNCM5ObjectValueConverterClass converter = new OwFNCM5ObjectValueConverterClass(resourceAccessor);
                fncmClass = new OwFNCM5ObjectDefinition((PropertyDefinitionObject) propertyDefinition_p, converter, orderable_p, searchable_p, selectable_p, preferredType);
            }
                break;

        }

        return (OwFNCM5EnginePropertyClass<D, ?, ?>) fncmClass;
    }

}
