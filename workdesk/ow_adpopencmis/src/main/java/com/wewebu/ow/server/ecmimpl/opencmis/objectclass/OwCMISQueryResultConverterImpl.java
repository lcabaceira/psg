package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.LinkedList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.Cardinality;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ObjectDataImpl;
import org.apache.chemistry.opencmis.commons.spi.BindingsObjectFactory;
import org.apache.log4j.Logger;

import com.wewebu.ow.csqlc.ast.OwColumnQualifier;
import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwCMISQueryResultConverterImpl.
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
public class OwCMISQueryResultConverterImpl<O extends TransientCmisObject> implements OwCMISQueryResultConverter<O>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISQueryResultConverterImpl.class);

    private Session session;

    public OwCMISQueryResultConverterImpl(Session session)
    {
        super();
        this.session = session;
    }

    public Session getSession()
    {
        return session;
    }

    protected String getQueryResultObjectTypeId(OwQueryStatement statement, QueryResult queryResult)
    {
        PropertyData<String> objctTypeId = queryResult.getPropertyById(PropertyIds.OBJECT_TYPE_ID);
        if (objctTypeId == null)
        {
            String qualifier = statement.getMainTableQualifier().getQualifierString();
            objctTypeId = queryResult.getPropertyById(qualifier + "." + PropertyIds.OBJECT_TYPE_ID);
        }

        if (objctTypeId != null)
        {
            return objctTypeId.getFirstValue();
        }
        else
        {
            return null;
        }
    }

    protected ObjectType getMainObjectClass(OwQueryStatement statement, QueryResult queryResult) throws OwException
    {
        String typeId = getQueryResultObjectTypeId(statement, queryResult);
        return session.getTypeDefinition(typeId);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void addQueryResultProperties(ObjectDataImpl objectData, OwQueryStatement statement, QueryResult queryResult) throws OwException
    {
        List<PropertyData<?>> properties = new LinkedList<PropertyData<?>>();
        List<PropertyData<?>> propertiesData = queryResult.getProperties();
        Session nativeSession = session;

        BindingsObjectFactory bindingsObjectFactory = nativeSession.getBinding().getObjectFactory();
        ObjectType mainObjectType = getMainObjectClass(statement, queryResult);
        OwColumnQualifier mainTableQualifier = statement.getMainTableQualifier();

        for (PropertyData<?> propertyData : propertiesData)
        {
            String id = propertyData.getQueryName();
            OwCMISQualifiedName qName = new OwCMISQualifiedName(id);
            ObjectType objectType = null;

            if (qName.getNamespace() == null || qName.getNamespace().equals(mainTableQualifier.getQualifierString()))
            {
                objectType = mainObjectType;
                PropertyDefinition propertyDefinition = objectType.getPropertyDefinitions().get(propertyData.getId());

                Object value = null;
                if (propertyDefinition != null)
                {
                    if (Cardinality.SINGLE.equals(propertyDefinition.getCardinality()))
                    {
                        value = propertyData.getFirstValue();
                    }
                    else
                    {
                        value = propertyData.getValues();
                    }
                    PropertyData convertedPropertyData = bindingsObjectFactory.createPropertyData(propertyDefinition, value);

                    properties.add(convertedPropertyData);
                }
                else
                {
                    LOG.warn("Conversion not possible ====== PropertyDef not found id = " + propertyData.getId() + " queryName = " + qName.getName());
                }
            }

        }

        PropertyDefinition propDef = mainObjectType.getPropertyDefinitions().get(PropertyIds.BASE_TYPE_ID);
        if (!properties.contains(propDef))
        {
            PropertyData convertedPropertyData = bindingsObjectFactory.createPropertyData(propDef, mainObjectType.getBaseTypeId().value());
            properties.add(convertedPropertyData);
        }

        Properties objectDataProperties = session.getBinding().getObjectFactory().createPropertiesData(properties);
        objectData.setProperties(objectDataProperties);
    }

    @Override
    public O toCmisObject(QueryResult queryResult, OwQueryStatement statement, OperationContext operationContext) throws OwException
    {
        ObjectDataImpl data = new ObjectDataImpl();
        data.setAllowableActions(queryResult.getAllowableActions());
        addQueryResultProperties(data, statement, queryResult);

        CmisObject obj = session.getObjectFactory().convertObject(data, operationContext);

        return (O) obj.getTransientObject();
    }

}
