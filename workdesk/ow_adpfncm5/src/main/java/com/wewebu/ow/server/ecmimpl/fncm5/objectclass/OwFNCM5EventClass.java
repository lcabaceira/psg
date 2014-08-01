package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.filenet.api.constants.PropertyNames;
import com.filenet.api.events.Event;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5ObjectStoreResource;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Event;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5SoftObject;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.history.OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass;

/**
 *<p>
 * {@link Event} AWD representation.
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
public class OwFNCM5EventClass extends OwFNCM5IndependentlyPersistableObjectClass<Event, OwFNCM5ObjectStoreResource>
{
    private static final OwStandardHistoryEntryObjectClass virtualEventClass = new OwStandardHistoryEntryObjectClass();

    private static final Set<String> nonEngineVirtualProperties = new HashSet<String>();
    private static final Map<String, String> virtualEngineBindings = new HashMap<String, String>();

    static
    {
        //non-engine
        nonEngineVirtualProperties.add(OwStandardHistoryEntryObjectClass.OBJECTS_PROPERTY);
        nonEngineVirtualProperties.add(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY);
        nonEngineVirtualProperties.add(OwStandardHistoryEntryObjectClass.PARENT_PROPERTY);
        nonEngineVirtualProperties.add(OwStandardHistoryEntryObjectClass.ID_PROPERTY);

        //bindings
        virtualEngineBindings.put(OwStandardHistoryEntryObjectClass.TIME_PROPERTY, PropertyNames.DATE_CREATED);
        virtualEngineBindings.put(OwStandardHistoryEntryObjectClass.USER_PROPERTY, PropertyNames.CREATOR);
        virtualEngineBindings.put(OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY, PropertyNames.MODIFIED_PROPERTIES);
        virtualEngineBindings.put(OwStandardHistoryEntryObjectClass.STATUS_PROPERTY, PropertyNames.EVENT_STATUS);

    }

    private List<String> localVirtualProperties = null;

    public OwFNCM5EventClass(OwFNCM5EngineClassDeclaration<?, OwFNCM5ObjectStoreResource> declaration_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        super(declaration_p, resourceAccessor_p);
    }

    private synchronized List<String> getLocalVirtualProperties() throws OwException
    {
        if (localVirtualProperties == null)
        {
            try
            {
                localVirtualProperties = new LinkedList<String>();
                Collection standardVirtualClassNames = OwFNCM5EventClass.virtualEventClass.getPropertyClassNames();
                localVirtualProperties.addAll(standardVirtualClassNames);
                localVirtualProperties.removeAll(OwFNCM5EventClass.nonEngineVirtualProperties);
                Set<String> missingBindings = new HashSet<String>();
                for (String localProperty : localVirtualProperties)
                {
                    String binding = virtualEngineBindings.get(localProperty);
                    if (binding != null)
                    {
                        OwFNCM5EnginePropertyClass<?, ?, ?> enginePeer = enginePropertyClass(binding);
                        if (enginePeer == null)
                        {
                            missingBindings.add(localProperty);
                        }
                    }
                }

                localVirtualProperties.removeAll(missingBindings);

            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Error in standard history entry class.", e);
            }
        }

        return localVirtualProperties;
    }

    @Override
    public List<String> getVirtualPropertiesClassNames() throws OwException
    {
        List<String> virtualProperties = super.getVirtualPropertiesClassNames();
        List<String> localProperties = getLocalVirtualProperties();
        virtualProperties.addAll(localProperties);

        return virtualProperties;
    }

    @Override
    protected OwFNCM5VirtualPropertyClass virtualPropertyClass(String className_p) throws OwException
    {
        try
        {
            List<String> localProperties = getLocalVirtualProperties();
            if (localProperties.contains(className_p))
            {
                OwPropertyClass propertyClass = OwFNCM5EventClass.virtualEventClass.getPropertyClass(className_p);
                String engineBinding = OwFNCM5EventClass.virtualEngineBindings.get(className_p);
                if (engineBinding != null)
                {
                    OwFNCM5EnginePropertyClass<?, ?, ?> enginePeer = enginePropertyClass(engineBinding);
                    if (enginePeer != null)
                    {
                        return new OwFNCM5VirtualPropertyClass(propertyClass, enginePeer);
                    }
                }
                else
                {
                    return new OwFNCM5VirtualPropertyClass(propertyClass);
                }
            }

            //            if (OwStandardHistoryEntryObjectClass.TIME_PROPERTY.equals(className_p))
            //            {
            //                OwPropertyClass propertyClass = OwFNCM5EventClass.virtualEventClass.getPropertyClass(className_p);
            //                OwFNCM5EnginePropertyClass<?, ?, ?> timePeer = enginePropertyClass(PropertyNames.DATE_CREATED);
            //                return new OwFNCM5VirtualPropertyClass(propertyClass, timePeer);
            //            }
            //            else if (OwStandardHistoryEntryObjectClass.USER_PROPERTY.equals(className_p))
            //            {
            //                OwPropertyClass propertyClass = OwFNCM5EventClass.virtualEventClass.getPropertyClass(className_p);
            //                OwFNCM5EnginePropertyClass<?, ?, ?> userPeer = enginePropertyClass(PropertyNames.CREATOR);
            //                return new OwFNCM5VirtualPropertyClass(propertyClass, userPeer);
            //            }
            //            else if (OwStandardHistoryEntryObjectClass.NAME_PROPERTY.equals(className_p))
            //            {
            //                //                OwFNCM5EnginePropertyClass<?, ?, ?> namePeer = getEnginePropertyClass(PropertyNames.NAME);
            //                //                return new OwFNCM5SurrogatePropertyClass(propertyClass, namePeer);
            //                OwPropertyClass propertyClass = OwFNCM5EventClass.virtualEventClass.getPropertyClass(className_p);
            //                return new OwFNCM5VirtualPropertyClass(propertyClass);
            //            }
            //            else if (OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY.equals(className_p))
            //            {
            //                OwPropertyClass propertyClass = OwFNCM5EventClass.virtualEventClass.getPropertyClass(className_p);
            //                OwFNCM5EnginePropertyClass<?, ?, ?> modifiedPropertiesPeer = enginePropertyClass(PropertyNames.MODIFIED_PROPERTIES);
            //                if (modifiedPropertiesPeer != null)
            //                {
            //                    return new OwFNCM5VirtualPropertyClass(propertyClass, modifiedPropertiesPeer);
            //                }
            //            }
            //            else if (OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY.equals(className_p))
            //            {
            //                OwPropertyClass propertyClass = OwFNCM5EventClass.virtualEventClass.getPropertyClass(className_p);
            //                return new OwFNCM5VirtualPropertyClass(propertyClass);
            //            }
            //            else if (OwStandardHistoryEntryObjectClass.OBJECTS_PROPERTY.equals(className_p))
            //            {
            //                OwPropertyClass propertyClass = OwFNCM5EventClass.virtualEventClass.getPropertyClass(className_p);
            //                return new OwFNCM5VirtualPropertyClass(propertyClass);
            //            }
            //            else if (OwStandardHistoryEntryObjectClass.TYPE_PROPERTY.equals(className_p))
            //            {
            //                OwPropertyClass propertyClass = OwFNCM5EventClass.virtualEventClass.getPropertyClass(className_p);
            //                return new OwFNCM5VirtualPropertyClass(propertyClass);
            //            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not retrieve standard event properties names.", e);
        }

        return super.virtualPropertyClass(className_p);
    }

    @Override
    public OwFNCM5Object<Event> from(Event nativeObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {
        OwFNCM5Event event = factory_p.create(OwFNCM5Event.class, new Class[] { Event.class, OwFNCM5EventClass.class }, new Object[] { nativeObject_p, this });
        return OwFNCM5SoftObject.asSoftObject(event);
    }
}
