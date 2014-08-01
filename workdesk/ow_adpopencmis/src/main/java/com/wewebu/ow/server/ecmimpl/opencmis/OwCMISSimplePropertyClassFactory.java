package com.wewebu.ow.server.ecmimpl.opencmis;

import org.apache.chemistry.opencmis.commons.definitions.PropertyBooleanDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDateTimeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDecimalDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyHtmlDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyIdDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyIntegerDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyStringDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyUriDefinition;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISDateConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISIdObjectConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISSameTypeConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.converter.OwCMISValueConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISBooleanPropertyClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISDateTimePropertyClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISDecimalPropertyClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISHtmlPropertyClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISIdPropertyClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISIntegerPropertyClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISNativePropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISStringPropertyClassImpl;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISUriPropertyClassImpl;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Simple implementation of property class factory.
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
public class OwCMISSimplePropertyClassFactory implements OwCMISNativePropertyClassFactory
{
    private static final Logger LOG = OwLog.getLogger(OwCMISSimplePropertyClassFactory.class);

    private OwCMISSession session;

    public OwCMISSimplePropertyClassFactory(OwCMISSession session_p) throws OwInvalidOperationException
    {
        this.session = session_p;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public OwCMISNativePropertyClass<?, ?, PropertyDefinition<?>> createPropertyClass(String className, PropertyDefinition<?> propertyDefinition, OwCMISNativeObjectClass<?, ?> objectClass) throws OwException
    {
        OwCMISNativePropertyClass propertyClass = null;
        try
        {
            switch (propertyDefinition.getPropertyType())
            {
                case BOOLEAN:
                    propertyClass = new OwCMISBooleanPropertyClassImpl(className, (PropertyBooleanDefinition) propertyDefinition, objectClass);
                    break;
                case DATETIME:
                    propertyClass = new OwCMISDateTimePropertyClassImpl(className, (PropertyDateTimeDefinition) propertyDefinition, new OwCMISDateConverter(session.getTimeZone()), objectClass);
                    break;
                case DECIMAL:
                    propertyClass = new OwCMISDecimalPropertyClassImpl(className, (PropertyDecimalDefinition) propertyDefinition, objectClass);
                    break;
                case INTEGER:
                    propertyClass = new OwCMISIntegerPropertyClassImpl(className, (PropertyIntegerDefinition) propertyDefinition, objectClass);
                    break;
                case STRING:
                    propertyClass = new OwCMISStringPropertyClassImpl(className, (PropertyStringDefinition) propertyDefinition, objectClass, session.getLocale());
                    break;
                case ID:
                    String propertyID = propertyDefinition.getId();
                    OwCMISValueConverter<String, OwObjectReference> objectConverter = getIdConverter(propertyID, objectClass);
                    if (null == objectConverter)
                    {
                        propertyClass = new OwCMISIdPropertyClassImpl<String>(className, (PropertyIdDefinition) propertyDefinition, new OwCMISSameTypeConverter<String>(String.class), String.class, objectClass);
                    }
                    else
                    {
                        propertyClass = new OwCMISIdPropertyClassImpl<OwObjectReference>(className, (PropertyIdDefinition) propertyDefinition, objectConverter, OwObjectReference.class, objectClass);
                    }
                    break;
                case HTML:
                    propertyClass = new OwCMISHtmlPropertyClassImpl(className, (PropertyHtmlDefinition) propertyDefinition, objectClass);
                    break;
                case URI:
                    propertyClass = new OwCMISUriPropertyClassImpl(className, (PropertyUriDefinition) propertyDefinition, objectClass);
                    break;
                default:
                    LOG.fatal("OwCMISStandardPropertyClassFactory.createPropertyClass(): Unknown property type " + propertyDefinition.getPropertyType());
                    throw new OwInvalidOperationException("Unknown property type : " + propertyDefinition.getPropertyType());
            }

            return propertyClass;
        }
        catch (ClassCastException castEx)
        {
            String msg = "Type of property = " + objectClass.getQueryName() + " property " + propertyDefinition.getId() + " not matching instance! type = " + propertyDefinition.getPropertyType().value();
            LOG.fatal("OwCMISSimplePropertyClassFactory.createPropertyClass():" + msg, castEx);
            throw new OwServerException(msg + " but is: " + castEx.getMessage(), castEx);
        }
    }

    /**(overridable)
     * Method to explicitly define a Converter for Id properties.
     * If Id should be handled as simple String, the return value must be <b>null</b>. 
     * @param propertyId String name of property
     * @param objectClass OwCMISNativeObjectClass type where property will be contained
     * @return OwCMISValueConverter from String to OwObjectReference (can return null)
     * @throws OwException
     */
    protected OwCMISValueConverter<String, OwObjectReference> getIdConverter(String propertyId, OwCMISNativeObjectClass<?, ?> objectClass) throws OwException
    {
        OwCMISNetworkCfg netCfg = this.session.getNetwork().getNetworkConfiguration();
        if (netCfg.getNonSerializedPropertyIds().contains(propertyId))
        {
            return null;
        }
        else
        {
            return createIdConverter(this.session);
        }
    }

    public OwCMISValueConverter<String, OwObjectReference> createIdConverter(OwCMISSession currentSession) throws OwException
    {
        String resourceId = currentSession.getResourceInfo().getId();
        OwCMISNetworkCfg configuration = currentSession.getNetwork().getNetworkConfiguration();
        OwCMISValueConverter<String, String> converter = configuration.getIdDMSIDConverter(resourceId);
        return new OwCMISIdObjectConverter(currentSession, converter);
    }
}