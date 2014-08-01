package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.EngineObject;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5EngineObjectClass;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5Property;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EngineBinding;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * State handling facade.<br/>
 * Used as a single storage point for native-state-caching {@link EngineObject}s for 
 * improved memory usage and access synchronization with an AWD property representations cache. 
 *   
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
public class OwFNCM5EngineState<E extends EngineObject>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5EngineState.class);

    private OwFNCM5EngineObjectClass<?, ?> engineObjectClass;
    private Map<String, OwFNCM5Property> propertyCache = new LinkedHashMap<String, OwFNCM5Property>();
    private E engineObject;

    public OwFNCM5EngineState(E engineObject_p, OwFNCM5EngineObjectClass<?, ?> engineObjectClass_p)
    {
        super();
        this.engineObjectClass = engineObjectClass_p;
        this.engineObject = engineObject_p;
    }

    /**
     * 
     * @return the underlying {@link EngineObject}
     */
    public E getEngineObject()
    {
        return engineObject;
    }

    /**
     * Retrieves the native property cache of an {@link IndependentObject}'s state.<br/>
     * Clears the AWD property representations cache if new native properties or values 
     * are fetched.
     * 
     * @return the underlying {@link EngineObject} 
     * @throws OwException
     * @see IndependentObject#refresh()
     */
    public E refresh() throws OwException
    {
        return engineObject;
    }

    /**
     * Retrieves the native values of the properties indicated by the given property names
     * for an {@link IndependentObject}'s state.<br/>
     * Clears the AWD property representations cache if new native properties or values 
     * are fetched.
     * 
     * @param propertyNames_p names of the properties to refresh 
     * @return the underlying {@link EngineObject}
     * @throws OwException
     * @see IndependentObject#refresh(String[])
     */
    public E refresh(String[] propertyNames_p) throws OwException
    {
        return engineObject;
    }

    /**
     * Retrieves the native values of the properties indicated by the given filter 
     * for an {@link IndependentObject}'s state.<br/>
     * Clears the AWD property representations cache if new native properties or values 
     * are fetched.
     * 
     * @param filter_p native property filter of the refreshed properties
     * @return the underlying {@link EngineObject}
     * @throws OwException
     * @see IndependentObject#refresh(PropertyFilter)
     */
    public E refresh(PropertyFilter filter_p) throws OwException
    {
        return engineObject;
    }

    /**
     * Tries to refreshes the given property.
     * If the property was not successfully refreshed the method throws an {@link OwException}  
     *  
     * @param propertyName_p name of the ensured property 
     * @return the underlying {@link EngineObject}
     * @throws OwException if the given property was not successfully refreshed 
     */
    public E ensure(String propertyName_p) throws OwException
    {
        Properties properties = engineObject.getProperties();
        if (!properties.isPropertyPresent(propertyName_p))
        {
            refresh(new String[] { propertyName_p });
            properties = engineObject.getProperties();
            if (!properties.isPropertyPresent(propertyName_p))
            {
                throw new OwServerException("Could not ensure " + propertyName_p + " for engine object class " + engineObject.getClassName() + " with java engine-object-class " + engineObject.getClass().getName());
            }
        }

        return engineObject;
    }

    /**
     * Tries to refreshes the given property.
     * If any of the given properties was not successfully refreshed the method throws an {@link OwException}  
     *  
     * @param propertyNames_p names of the ensured property 
     * @return the underlying {@link EngineObject}
     * @throws OwException if any of the given properties was not successfully refreshed 
     */
    public E ensure(String[] propertyNames_p) throws OwException
    {
        List<String> missingProperties = new LinkedList<String>();
        Properties properties = engineObject.getProperties();

        for (String property : propertyNames_p)
        {
            if (!properties.isPropertyPresent(property))
            {
                missingProperties.add(property);
            }
        }

        if (!missingProperties.isEmpty())
        {
            refresh(missingProperties.toArray(new String[missingProperties.size()]));
            for (String property : missingProperties)
            {
                if (!properties.isPropertyPresent(property))
                {
                    throw new OwServerException("Could not ensure " + property + " for engine object class " + engineObject.getClassName() + " with java engine-object-class " + engineObject.getClass().getName());
                }
            }
        }

        return engineObject;
    }

    /**
     * Saves (persists it on the ECM engine) this state.
     *  
     * @param mode_p the native save-refresh mode
     * @throws OwException
     */
    public void save(RefreshMode mode_p) throws OwException
    {
        //void  
    }

    @SuppressWarnings("unchecked")
    private synchronized OwPropertyCollection readThroughPropertyCache(String[] properties_p) throws OwException
    {
        OwPropertyCollection properties = new OwStandardPropertyCollection();
        List<String> missingProperties = new LinkedList<String>();

        for (int i = 0; i < properties_p.length; i++)
        {
            String propName = properties_p[i];
            OwFNCM5Property property = this.propertyCache.get(propName);
            if (property == null)
            {
                Property natProp = getEngineObject().getProperties().find(propName);
                if (natProp != null)
                {
                    OwFNCM5PropertyClass propertyClass = engineObjectClass.getPropertyClass(propName);
                    OwFNCM5EngineBinding<?, ?, ?> binding = propertyClass.getEngineBinding();
                    if (binding != null)
                    {
                        property = binding.from(natProp);
                        propertyCache.put(propertyClass.getClassName(), property);
                    }
                    else
                    {
                        throw new OwObjectNotFoundException("Could not retrieve engine property " + propName);
                    }
                }
                else
                {
                    missingProperties.add(properties_p[i]);
                }
            }

            properties.put(properties_p[i], property);//keep order of properties as requested
        }

        int missingCount = missingProperties.size();
        if (missingCount > 0)
        {
            E object = refresh(missingProperties.toArray(new String[missingCount]));
            Properties missingNativeProperties = object.getProperties();

            for (int i = 0; i < missingCount; i++)
            {
                String missingPropertyName = missingProperties.get(i);
                if (missingNativeProperties.isPropertyPresent(missingPropertyName))
                {
                    Property missingNativeProperty = missingNativeProperties.get(missingPropertyName);
                    OwFNCM5PropertyClass propertyClass = engineObjectClass.getPropertyClass(missingPropertyName);
                    OwFNCM5EngineBinding<?, ?, ?> binding = propertyClass.getEngineBinding();
                    if (binding != null)
                    {

                        OwFNCM5Property property = binding.from(missingNativeProperty);
                        this.propertyCache.put(propertyClass.getClassName(), property);
                        properties.put(missingPropertyName, property);
                    }
                    else
                    {
                        properties.remove(missingPropertyName);//avoid null pointer
                        LOG.warn("Refresh request for property of unknown type! Class name " + missingPropertyName + " of java class " + propertyClass.getClass());
                    }
                }
                else
                {
                    throw new OwObjectNotFoundException("Could not retrieve engine property " + missingPropertyName);
                }

            }
        }

        return properties;
    }

    /**
     * Reads the given properties.
     * The retrieved {@link OwProperty}s are cached. 
     *    
     * @param propertyNames_p
     * @return an {@link OwPropertyCollection} 
     * @throws OwException
     */
    public OwPropertyCollection get(String[] propertyNames_p) throws OwException
    {
        return readThroughPropertyCache(propertyNames_p);
    }

    /**
     * Reads the given properties.
     * The retrieved {@link OwFNCM5Property} is cached.
     * 
     * @param propertyName_p
     * @return the requested {@link OwFNCM5Property}
     * @throws OwException
     */
    public OwFNCM5Property get(String propertyName_p) throws OwException
    {
        OwPropertyCollection singleValueMap = readThroughPropertyCache(new String[] { propertyName_p });
        return (OwFNCM5Property) singleValueMap.get(propertyName_p);
    }

    /**
     * State store utility.
     * Sets the  given properties and stores this state on the ECM engine.
     * Equivalent to :
     * <p><blockquote><pre> 
     *  state.set(properties_p);
     *  state.save((RefreshMode) mode_p);
     * </pre></blockquote></p>
     * 
     * @param properties_p
     * @param mode_p 
     * @throws OwException
     */
    public synchronized void store(OwPropertyCollection properties_p, Object mode_p) throws OwException
    {
        set(properties_p);
        save((RefreshMode) mode_p);
    }

    /**
     * Property collection setter.
     * The given properties are cached by this state.
     * The given properties are NOT stored/persisted on the ECM engine. 
     * 
     * @param properties_p
     * @throws OwException
     */
    public synchronized void set(OwPropertyCollection properties_p) throws OwException
    {
        if (properties_p != null && !properties_p.isEmpty())
        {
            Iterator<?> it = properties_p.values().iterator();
            while (it.hasNext())
            {
                OwProperty inProperty = (OwProperty) it.next();
                set(inProperty);
            }
        }
    }

    /**
     * State store utility.
     * Sets the  given property and stores this state on the ECM engine.
     * Equivalent to : 
     * <p><blockquote><pre> 
     *  state.set(property_p);
     *  state.save((RefreshMode) mode_p);
     * </pre></blockquote></p>
     * 
     * @param property_p
     * @param mode_p 
     * @throws OwException
     */
    public synchronized void store(OwProperty property_p, Object mode_p) throws OwException
    {
        set(property_p);
        save((RefreshMode) mode_p);
    }

    /**
     * Property setter.
     * The given property is cached by this state.
     * The given property is NOT stored/persisted on the ECM engine.
     *  
     * @param property_p
     * @throws OwException
     */
    public synchronized void set(OwProperty property_p) throws OwException
    {
        try
        {

            OwPropertyClass inPropertyClass = property_p.getPropertyClass();
            String inPropertyClassName = inPropertyClass.getClassName();

            OwFNCM5PropertyClass storedPropertyClass = engineObjectClass.getPropertyClass(inPropertyClassName);

            OwFNCM5EngineBinding<?, ?, ?> binding = storedPropertyClass.getEngineBinding();

            if (binding != null)
            {
                Properties properties = engineObject.getProperties();

                Object value = property_p.getValue();

                Object nativeValue = binding.toNativeValue(value);

                String symbolicName = binding.getSymbolicName();
                properties.putObjectValue(symbolicName, nativeValue);

                propertyCache.remove(storedPropertyClass.getClassName());
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not store engine property values.", e);
        }
    }

    /**
     * Clears the AWD property representations cache.
     * 
     */
    public synchronized void clearCache()
    {
        propertyCache.clear();
    }

    /**
     * Removes the properties with the given names from the AWD 
     * property representations cache.
     * 
     * @param propertyNames_p
     */
    public synchronized void clearCache(String[] propertyNames_p)
    {
        for (String property : propertyNames_p)
        {
            propertyCache.remove(property);
        }
    }

    /**
     * Removes the properties with the given names from the AWD 
     * property representations cache.
     * 
     * @param propertyNames_p
     */
    public synchronized void clearCache(Collection<String> propertyNames_p)
    {
        Set<String> cacheKeys = propertyCache.keySet();
        cacheKeys.removeAll(propertyNames_p);
    }

    /**
     * Sets the given content on this native state for {@link Document} states.
     * The state is NOT persisted on the ECM engine.  
     * 
     * @param content_p
     * @param strMimeType_p
     * @param strMimeParameter_p
     * @return true if the given content was successfully set     
     * @throws OwException
     */
    public synchronized boolean upload(OwContentCollection content_p, String strMimeType_p, String strMimeParameter_p) throws OwException
    {
        return false;
    }
}
