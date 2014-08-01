package com.wewebu.ow.server.ecmimpl.fncm5;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ObjectClassFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ResourceAccessor;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClassFactory;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * The default-configurable model builder.
 * A {@link OwFNCM5CachedObjectModel} is built using bootstrap configured property 
 * and object class factory.
 * <br/>
 * The {@link OwFNCM5PropertyClassFactory} concrete class is indicated by 
 * the <i>PropertyClassFactory</i> configuration element. 
 * Property class factory java-class must have a {@link OwFNCM5ResourceAccessor} single argument constructor.
 * <br/>
 * The {@link OwFNCM5ObjectClassFactory} concrete class is indicated by 
 * the <i>ObjectClassFactory</i> configuration element. 
 * Property class factory java-class must have a 2 arguments ( {@link OwFNCM5PropertyClassFactory} and {@link OwFNCM5ResourceAccessor}) constructor.
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
public class OwFNCM5DefaultModelBuilder implements OwFNCM5ObjectModelBuilder
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5DefaultModelBuilder.class);

    private static final String OBJECTCLASSFACTORY_CONFIG_ELEMENT = "ObjectClassFactory";
    private static final String PROPERTYCLASSFACTORY_CONFIG_ELEMENT = "PropertyClassFactory";

    public OwFNCM5ContentObjectModel build(OwFNCM5Network network_p, OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p, OwXMLUtil modelConfiguration_p) throws OwException
    {
        String propertyClassFactoryName = modelConfiguration_p.getSafeTextValue(PROPERTYCLASSFACTORY_CONFIG_ELEMENT, null);
        String objectClassFactoryName = modelConfiguration_p.getSafeTextValue(OBJECTCLASSFACTORY_CONFIG_ELEMENT, null);

        if (propertyClassFactoryName == null)
        {
            String message = "Invalid OwFNCM5DefaultModelBuilder configuration. Missing " + PROPERTYCLASSFACTORY_CONFIG_ELEMENT + " .";
            LOG.error("OwFNCM5DefaultModelBuilder.build : " + message);
            throw new OwConfigurationException(message);
        }

        if (objectClassFactoryName == null)
        {
            String message = "Invalid OwFNCM5DefaultModelBuilder configuration. Missing " + OBJECTCLASSFACTORY_CONFIG_ELEMENT + " .";
            LOG.error("OwFNCM5DefaultModelBuilder.build : " + message);
            throw new OwConfigurationException(message);
        }

        try
        {
            Class<?> propertyClassFactoryClass = Class.forName(propertyClassFactoryName);
            Class<?> objectClassFactoryClass = Class.forName(objectClassFactoryName);

            Constructor<?> pcfConstructor = propertyClassFactoryClass.getConstructor(new Class[] { OwFNCM5ResourceAccessor.class });
            Constructor<?> ocfConstructor = objectClassFactoryClass.getConstructor(new Class[] { OwFNCM5PropertyClassFactory.class, OwFNCM5ResourceAccessor.class });

            OwFNCM5PropertyClassFactory propertyClassFactory = (OwFNCM5PropertyClassFactory) pcfConstructor.newInstance(new Object[] { resourceAccessor_p });
            OwFNCM5ObjectClassFactory classFactory = (OwFNCM5ObjectClassFactory) ocfConstructor.newInstance(new Object[] { propertyClassFactory, resourceAccessor_p });

            return new OwFNCM5CachedObjectModel(resourceAccessor_p, propertyClassFactory, classFactory);
        }
        catch (Exception e)
        {
            String message = "Could not build default object model based on " + propertyClassFactoryName + " and " + objectClassFactoryName;
            LOG.error("OwFNCM5DefaultModelBuilder.build : " + message);
            throw new OwConfigurationException(message, e);
        }

    }

}
