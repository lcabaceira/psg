package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.alfresco.cmis.client.impl.AlfrescoUtils;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.Cardinality;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.CmisExtensionElementImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ObjectDataImpl;
import org.apache.chemistry.opencmis.commons.spi.BindingsObjectFactory;

import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISQueryResultConverterImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwCMISAlfrescoQueryResultConverterImpl.
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
public class OwCMISAlfrescoQueryResultConverterImpl<O extends TransientCmisObject> extends OwCMISQueryResultConverterImpl<O>
{
    private static final String PROPERTIES = "properties";
    private static final String ASPECTS = "aspects";
    private static final String PROPERTY = "property";
    private static final String VALUE = "value";

    public OwCMISAlfrescoQueryResultConverterImpl(Session session)
    {
        super(session);
    }

    @Override
    protected void addQueryResultProperties(ObjectDataImpl objectData, OwQueryStatement statement, QueryResult queryResult) throws OwException
    {
        super.addQueryResultProperties(objectData, statement, queryResult);
        try
        {

            LinkedList<PropertyData<?>> aspectProperties = new LinkedList<PropertyData<?>>();
            Map<String, PropertyDefinition<?>> aspectPropertyDefinitions = new HashMap<String, PropertyDefinition<?>>();

            List<PropertyData<?>> propertiesData = queryResult.getProperties();
            Session nativeSession = getSession();

            BindingsObjectFactory bindingsObjectFactory = nativeSession.getBinding().getObjectFactory();
            OwColumnQualifier mainTableQualifier = statement.getMainTableQualifier();
            List<OwColumnQualifier> columnQualifiers = statement.getColumnQualifiers();

            Set<String> aspects = new HashSet<String>();
            for (PropertyData<?> propertyData : propertiesData)
            {
                String id = propertyData.getQueryName();
                OwCMISQualifiedName qName = new OwCMISQualifiedName(id);
                ObjectType objectType = null;

                if (qName.getNamespace() != null)
                {
                    Map<String, OwColumnQualifier> qualifiers = statement.getNormalizedQualifiers();
                    OwColumnQualifier objectTypeQualifier = qualifiers.get(qName.getNamespace());
                    objectType = nativeSession.getTypeDefinition(objectTypeQualifier.getTargetObjectType());

                    if (!qName.getNamespace().equals(mainTableQualifier.getQualifierString()))
                    {
                        for (OwColumnQualifier columnQualifier : columnQualifiers)
                        {
                            if (qName.getNamespace().equals(columnQualifier.getQualifierString()))
                            {
                                aspects.add(columnQualifier.getTargetObjectType());

                                PropertyDefinition propertyDefinition = objectType.getPropertyDefinitions().get(qName.getName());

                                Object value = null;

                                if (Cardinality.SINGLE.equals(propertyDefinition.getCardinality()))
                                {
                                    value = propertyData.getFirstValue();
                                }
                                else
                                {
                                    value = propertyData.getValues();
                                }

                                PropertyData convertedPropertyData = bindingsObjectFactory.createPropertyData(propertyDefinition, value);

                                aspectProperties.add(convertedPropertyData);
                                aspectPropertyDefinitions.put(convertedPropertyData.getId(), propertyDefinition);
                            }
                        }

                    }
                }
            }

            List<CmisExtensionElement> appliedAspectsExtension = new LinkedList<CmisExtensionElement>();
            for (String string : aspects)
            {
                appliedAspectsExtension.add(new CmisExtensionElementImpl(AlfrescoUtils.ALFRESCO_NAMESPACE, AlfrescoUtils.APPLIED_ASPECTS, null, string));
            }

            List<CmisExtensionElement> aspectsPropertiesExtensions = new LinkedList<CmisExtensionElement>();

            DatatypeFactory df = null;

            for (PropertyData<?> aspectProperty : aspectProperties)
            {
                PropertyDefinition<?> propertyDefinition = aspectPropertyDefinitions.get(aspectProperty.getId());
                List<?> values = aspectProperty.getValues();

                List<CmisExtensionElement> children = null;

                if (values != null)
                {

                    children = new LinkedList<CmisExtensionElement>();
                    for (Object object : values)
                    {

                        switch (propertyDefinition.getPropertyType())
                        {
                            case DATETIME:
                            {
                                if (df == null)
                                {
                                    df = DatatypeFactory.newInstance();
                                }
                                XMLGregorianCalendar value = df.newXMLGregorianCalendar((GregorianCalendar) object);
                                children.add(new CmisExtensionElementImpl(AlfrescoUtils.ALFRESCO_NAMESPACE, VALUE, null, value.toString()));
                            }
                                break;

                            default:
                            {
                                String value = object.toString();
                                children.add(new CmisExtensionElementImpl(AlfrescoUtils.ALFRESCO_NAMESPACE, VALUE, null, value));
                            }
                        }
                    }

                    Map<String, String> attributes = new HashMap<String, String>();
                    attributes.put("propertyDefinitionId", propertyDefinition.getId());
                    CmisExtensionElement property = new CmisExtensionElementImpl(AlfrescoUtils.ALFRESCO_NAMESPACE, PROPERTY, attributes, children);

                    aspectsPropertiesExtensions.add(property);
                }
            }

            CmisExtensionElement propertiesExtension = new CmisExtensionElementImpl(AlfrescoUtils.ALFRESCO_NAMESPACE, PROPERTIES, null, aspectsPropertiesExtensions);

            List<CmisExtensionElement> aspectsChildren = new LinkedList<CmisExtensionElement>();
            aspectsChildren.addAll(appliedAspectsExtension);
            aspectsChildren.add(propertiesExtension);

            CmisExtensionElement aspectsExtension = new CmisExtensionElementImpl(AlfrescoUtils.ALFRESCO_NAMESPACE, ASPECTS, null, aspectsChildren);

            Properties objectDataProperties = objectData.getProperties();
            objectDataProperties.setExtensions(Arrays.asList(aspectsExtension, OwCMISSecureAspectsExtensionUtil.MUST_SECURE_ASPECTS));
            objectData.setProperties(objectDataProperties);

        }
        catch (DatatypeConfigurationException e)
        {
            throw new OwInvalidOperationException("Could not convert alfresco aspect properties", e);
        }
    }
}
