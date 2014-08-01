package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.filenet.api.collection.StringList;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.TypeID;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.events.Event;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ContentObjectModel;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5EngineObjectClass;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5EventClass;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5EngineProperty;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5Property;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5SimpleProperty;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EngineBinding;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.history.OwHistoryModifiedPropertyValue;
import com.wewebu.ow.server.history.OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass;

/**
 *<p>
 * OwFNCM5Event.
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
public class OwFNCM5Event extends OwFNCM5IndependentlyPersistableObject<Event>
{
    /** package logger for the class */
    static final Logger LOG = OwLog.getLogger(OwFNCM5Event.class);

    public OwFNCM5Event(Event nativeObject_p, OwFNCM5EventClass clazz_p)
    {
        super(nativeObject_p, clazz_p);
    }

    @Override
    public OwProperty createVirtualProperty(OwFNCM5VirtualPropertyClass propertyClass_p) throws OwException
    {
        String className = propertyClass_p.getClassName();
        if (OwStandardHistoryEntryObjectClass.NAME_PROPERTY.equals(className))
        {
            //                OwPropertyClass propertyClass = OwFNCM5EventClass.virtualEventClass.getPropertyClass(propertyClass_p);
            //                return new OwFNCM5VirtualPropertyClass(propertyClass);

            OwFNCM5EngineObjectClass<Event, OwFNCM5ObjectStoreResource> objectClass = getObjectClass();
            OwFNCM5Network network = getNetwork();
            Locale locale = network.getLocale();
            String classDisplayName = objectClass.getDisplayName(locale);
            return new OwFNCM5SimpleProperty(classDisplayName, propertyClass_p);
        }
        else if (OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY.equals(className))
        {
            Object[] modifiedProperties = retrieveModifiedProperties();
            return new OwFNCM5SimpleProperty(modifiedProperties, propertyClass_p);
        }
        else if (OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY.equals(className))
        {
            String summary = retrieveSummary();
            return new OwFNCM5SimpleProperty(summary, propertyClass_p);
        }
        else
        {
            return super.createVirtualProperty(propertyClass_p);
        }
    }

    private OwFNCM5EngineProperty<?, ?> toEngineProperty(Property property_p, OwFNCM5Class<?, ?> objectClass_p) throws OwException
    {
        OwFNCM5PropertyClass newPropertyClass = objectClass_p.getPropertyClass(property_p.getPropertyName());
        OwFNCM5EngineBinding<?, ?, ?> propertyBinding = newPropertyClass.getEngineBinding();
        return propertyBinding.from(property_p);
    }

    private String retrieveSummary()
    {
        // === get the virtual summary property

        // get the p8 modified properties
        StringBuffer value = new StringBuffer();
        try
        {
            OwFNCM5Network network = getNetwork();
            Locale locale = network.getLocale();

            value.append(this.getObjectClass().getDisplayName(locale));

            OwFNCM5Property modifiedProperties = null;

            try
            {
                modifiedProperties = super.getProperty(PropertyNames.MODIFIED_PROPERTIES);
            }
            catch (OwObjectNotFoundException e)
            {
                LOG.debug("No modified properties found while creating event summary.");
            }
            if (modifiedProperties != null)
            {
                value.append(": ");

                Object[] p8modifiedProperties = (Object[]) modifiedProperties.getValue();
                boolean fDelimiter = false;
                for (int i = 0; i < (p8modifiedProperties).length; i++)
                {
                    if (fDelimiter)
                    {
                        value.append(", ");
                    }

                    value.append(p8modifiedProperties[i].toString());

                    fDelimiter = true;
                }
            }

        }
        catch (Exception e)
        {
            // ignore
            LOG.debug("Could not retrieve modified properties summary.", e);
        }

        return value.toString();
    }

    private Object[] retrieveModifiedProperties() throws OwException
    {
        OwFNCM5EngineState<Event> myself = getSelf();
        Event event = myself.refresh(new String[] { PropertyNames.CLASS_DESCRIPTION, PropertyNames.MODIFIED_PROPERTIES, PropertyNames.ORIGINAL_OBJECT, PropertyNames.SOURCE_OBJECT });
        Properties engineProperties = event.getProperties();

        StringList p8modifiedProperties = null;
        IndependentObject originalObject = null;
        IndependentObject sourceObject = null;
        try
        {
            if (engineProperties.isPropertyPresent(PropertyNames.SOURCE_OBJECT))
            {
                sourceObject = (IndependentObject) engineProperties.getObjectValue(PropertyNames.SOURCE_OBJECT); // super.getProperty("ModifiedProperties").getValue();
            }
            else
            {
                LOG.debug("OwFNCM5Event.retrieveModifiedProperties: non SourceObject event found.");
            }

            if (engineProperties.isPropertyPresent(PropertyNames.ORIGINAL_OBJECT))
            {
                originalObject = (IndependentObject) engineProperties.getObjectValue(PropertyNames.ORIGINAL_OBJECT);
            }
            else
            {
                LOG.debug("OwFNCM5Event.retrieveModifiedProperties: non OriginalObject event found.");
            }

            if (engineProperties.isPropertyPresent(PropertyNames.MODIFIED_PROPERTIES))
            {
                p8modifiedProperties = engineProperties.getStringListValue(PropertyNames.MODIFIED_PROPERTIES); // super.getProperty("ModifiedProperties").getValue();
            }
            else
            {
                LOG.debug("OwFNCM5Event.retrieveModifiedProperties: non ModifidProperties event found.");
            }

        }
        catch (Exception e)
        {
            LOG.debug("Error while retrieveing event data.", e);
            return null;
        }

        Object[] modifiedPropertyValues = null;

        if (p8modifiedProperties != null)
        {
            // convert the value to a standard OwHistoryModifiedPropertyValue
            List<String> propertyNames = new LinkedList<String>();
            // Convert to string array
            OwFNCM5Network network = getNetwork();
            OwFNCM5ObjectStoreResource resource = getResource();
            OwFNCM5ContentObjectModel objectModel = resource.getObjectModel();

            for (Iterator i = p8modifiedProperties.iterator(); i.hasNext();)
            {
                String strPropName = (String) i.next();

                // first get the field definition for the requested property
                OwFNCM5PropertyClass propertyClass = objectModel.propertyClassForName(strPropName);
                OwFNCM5EngineBinding<?, ?, ?> engineBinding = propertyClass.getEngineBinding();
                if (engineBinding != null)
                {

                    switch (engineBinding.getType())
                    {
                        case TypeID.OBJECT_AS_INT:
                            continue; // Ignore object properties

                        default:
                            propertyNames.add(strPropName);
                            break;
                    }
                }
                else
                {
                    LOG.error("OwFNCM5Event.retrieveModifiedProperties: non engine history property found " + strPropName);
                }
            }

            // Convert to array - use the end index for class description
            String[] requestPropertyNames = new String[propertyNames.size()];
            for (int i = 0; i < propertyNames.size(); i++)
            {
                requestPropertyNames[i] = propertyNames.get(i);
            }

            Properties sourceProperties = null;
            Properties originalProperties = null;

            OwFNCM5Class<?, ?> sourceObjectClass = null;
            OwFNCM5Class<?, ?> originalObjectClass = null;

            if (sourceObject != null)
            {
                //                sourceObject.refresh(requestPropertyNames);
                sourceProperties = sourceObject.getProperties();
                String className = sourceObject.getClassName();
                sourceObjectClass = network.getObjectClass(className, resource);
            }

            if (originalObject != null)
            {
                //                originalObject.refresh(requestPropertyNames);
                originalProperties = originalObject.getProperties();
                String className = sourceObject.getClassName();
                originalObjectClass = network.getObjectClass(className, resource);
            }

            List<OwHistoryModifiedPropertyValue> modifiedPropertyValuesList = new LinkedList<OwHistoryModifiedPropertyValue>();

            for (int i = 0; i < requestPropertyNames.length; i++)
            {
                OwFNCM5EngineProperty<?, ?> newProperty = null;
                OwFNCM5EngineProperty<?, ?> oldProperty = null;

                if (sourceProperties != null && sourceObjectClass != null)
                {
                    Property sourceEngineProperty = sourceProperties.get(requestPropertyNames[i]);
                    newProperty = toEngineProperty(sourceEngineProperty, sourceObjectClass);
                }
                if (originalProperties != null && originalObjectClass != null)
                {
                    Property originalEngineProperty = originalProperties.get(requestPropertyNames[i]);
                    oldProperty = toEngineProperty(originalEngineProperty, originalObjectClass);
                }

                if (newProperty != null)
                {
                    OwHistoryModifiedPropertyValue modifiedPropertyValue = createFNCMModifiedPropertyValue(newProperty, oldProperty);
                    modifiedPropertyValuesList.add(modifiedPropertyValue);
                }
            }

            modifiedPropertyValues = modifiedPropertyValuesList.toArray(new OwHistoryModifiedPropertyValue[modifiedPropertyValuesList.size()]);
        }

        return modifiedPropertyValues;
    }

    protected OwHistoryModifiedPropertyValue createFNCMModifiedPropertyValue(OwFNCM5EngineProperty<?, ?> newProperty_p, OwFNCM5EngineProperty<?, ?> oldProperty_p) throws OwException
    {
        return new OwFNCM5ModifiedPropertyValue(newProperty_p, oldProperty_p);
    }
}
