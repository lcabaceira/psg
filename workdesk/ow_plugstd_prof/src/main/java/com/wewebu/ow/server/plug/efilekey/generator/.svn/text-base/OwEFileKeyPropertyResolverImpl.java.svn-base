package com.wewebu.ow.server.plug.efilekey.generator;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.expression.language.OwExprEvaluationException;
import com.wewebu.expression.language.OwExprExpression;
import com.wewebu.expression.language.OwExprExternalScope;
import com.wewebu.expression.language.OwExprValue;
import com.wewebu.expression.parser.OwExprParser;
import com.wewebu.expression.parser.ParseException;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwPropertiesCollectionScope;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.paramcodec.OwAlphabetCoder;
import com.wewebu.ow.server.util.paramcodec.OwAppCounterBag;
import com.wewebu.ow.server.util.paramcodec.OwParameterMapCodec;
import com.wewebu.ow.server.util.paramcodec.OwTransientCodec;

/**
 *<p>
 * This class creates a map between property names and values.<br>
 * The property can be a property form a given {@link OwObject} or some predefined system property.
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
 *@since 3.1.0.0
 */
public class OwEFileKeyPropertyResolverImpl implements OwKeyPropertyResolver
{
    /** the predefined properties*/
    public static final Set SYSTEM_PROPERTY_NAMES = new HashSet();
    static
    {
        SYSTEM_PROPERTY_NAMES.add(SYS_COUNTER_PROP_NAME);
        SYSTEM_PROPERTY_NAMES.add(SYS_GUID_PROP_NAME);
        SYSTEM_PROPERTY_NAMES.add(SYS_TIME_OF_DAY_PROP_NAME);
        SYSTEM_PROPERTY_NAMES.add(SYS_USER_ID_PROP_NAME);
        SYSTEM_PROPERTY_NAMES.add(SYS_USER_LONG_NAME_PROP_NAME);
        SYSTEM_PROPERTY_NAMES.add(SYS_USER_SHORT_NAME_PROP_NAME);
        SYSTEM_PROPERTY_NAMES.add(SYS_USER_DISPLAY_NAME_PROP_NAME);
        SYSTEM_PROPERTY_NAMES.add(SYS_USER_NAME_PROP_NAME);
    }

    /**class logger*/
    private static Logger LOG = OwLog.getLogger(OwEFileKeyPropertyResolverImpl.class);
    /** a property name - property value map */
    private Map m_systemPropNameValues;

    private OwPropertiesCollectionScope m_propertiesScope;

    /** the locale object*/
    private Locale m_locale;

    /**
     * Constructor.
     * @throws Exception 
     */
    public OwEFileKeyPropertyResolverImpl(OwNetwork network_p, OwPropertyCollection properties_p) throws Exception
    {
        m_systemPropNameValues = new HashMap();
        m_locale = network_p.getLocale();
        m_propertiesScope = new OwPropertiesCollectionScope("p", properties_p);
        resolveSystemPropNames(network_p);
        //                resolveObjectPropertiesNames(properties_p);
    }

    /**
     * Resolve the given properties names
     * @param properties_p
     * @throws Exception 
     */
    private void resolveObjectPropertiesNames(OwPropertyCollection properties_p) throws Exception
    {
        Iterator propsIterator = properties_p.values().iterator();
        while (propsIterator.hasNext())
        {
            OwProperty property = (OwProperty) propsIterator.next();
            m_systemPropNameValues.put(property.getPropertyClass().getClassName(), property.getValue());
        }
    }

    /**
     * Resolve the system property names, as defined in {@link OwKeyPropertyResolver} interface.
     * This method has a special error handling.<br>
     * E.g: if the user name cannot be resolved, the value for
     * {@link OwKeyPropertyResolver#SYS_USER_NAME_PROP_NAME} is set to <code>null</code>, no exception is thrown.
     * It is possible that the key pattern will not need the {@link OwKeyPropertyResolver#SYS_USER_NAME_PROP_NAME} 
     * property, so it makes no sense to treat here this error. In case the pattern needs the property mentioned above, 
     * at key generation time an {@link OwInvalidOperationException} will be thrown, when the value for the property 
     * will be requested. 
     * @param network_p - the {@link OwNetwork} object
     */
    private void resolveSystemPropNames(OwNetwork network_p) throws OwException
    {
        String userName = null;
        String userLongName = null;
        String userID = null;
        String userShortName = null;
        String userDisplayName = null;
        try
        {
            OwUserInfo userInfo = network_p.getCredentials().getUserInfo();
            try
            {
                userName = userInfo.getUserName();
            }
            catch (Exception e)
            {
                LOG.error("Cannot resolve user name. User <null> for user name!");
            }
            try
            {
                userLongName = userInfo.getUserLongName();
            }
            catch (Exception e)
            {
                LOG.error("Cannot resolve user long name. User <null> for user long name!");
            }
            try
            {
                userDisplayName = userInfo.getUserDisplayName();
            }
            catch (Exception e)
            {
                LOG.error("Cannot resolve user long name. User <null> for user display name!");
            }
            try
            {
                userShortName = userInfo.getUserShortName();
            }
            catch (Exception e)
            {
                LOG.error("Cannot resolve user long name. User <null> for user short name!");
            }
            try
            {
                userID = userInfo.getUserID();
            }
            catch (Exception e)
            {
                LOG.error("Cannot resolve user id. User <null> for user id!");
            }
        }
        catch (Exception e)
        {
            LOG.error("Exception in getting USER INFO. Maybe is not implemented?", e);
        }
        //for SYS_COUNTER_PROP_NAME - put 0 by default, until will be clearly specified
        m_systemPropNameValues.put(OwKeyPropertyResolver.SYS_COUNTER_PROP_NAME, generateSystemCounterValue(network_p));
        Date today = new Date();
        m_systemPropNameValues.put(OwKeyPropertyResolver.SYS_TIME_OF_DAY_PROP_NAME, today);
        m_systemPropNameValues.put(OwKeyPropertyResolver.SYS_USER_NAME_PROP_NAME, userName);
        m_systemPropNameValues.put(OwKeyPropertyResolver.SYS_USER_ID_PROP_NAME, userID);
        m_systemPropNameValues.put(OwKeyPropertyResolver.SYS_USER_LONG_NAME_PROP_NAME, userLongName);
        m_systemPropNameValues.put(OwKeyPropertyResolver.SYS_USER_DISPLAY_NAME_PROP_NAME, userDisplayName);
        m_systemPropNameValues.put(OwKeyPropertyResolver.SYS_USER_SHORT_NAME_PROP_NAME, userShortName);
        String guidValue = null;
        try
        {
            synchronized (OwKeyPropertyResolver.class)
            {
                OwAlphabetCoder coder = new OwAlphabetCoder();
                guidValue = coder.encode(System.currentTimeMillis());
                Thread.sleep(100);
            }
        }
        catch (Exception e)
        {
            LOG.error("Cannot generate to guid!", e);
        }
        m_systemPropNameValues.put(OwKeyPropertyResolver.SYS_GUID_PROP_NAME, guidValue);
    }

    /** Generate the value for system counter 
     * @throws OwException */
    private Long generateSystemCounterValue(OwNetwork network_p) throws OwException
    {
        OwParameterMapCodec counter = getApplicationCounter(network_p);
        return Long.valueOf(counter.getNextUnqiueNameIndex());
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.plug.efilekey.generator.OwKeyPropertyResolver#getPropertyValue(java.lang.String)
     */
    public Object getPropertyValue(String propertyExpression_p) throws OwInvalidOperationException
    {
        Object result = m_systemPropNameValues.get(propertyExpression_p);
        if (result == null)
        {
            String propertyExpression = "p." + propertyExpression_p;
            OwExprParser parser = new OwExprParser(new StringReader(propertyExpression));
            try
            {
                OwExprExpression expression = parser.ExprExpression();
                OwExprValue evalResult = expression.evaluate(new OwExprExternalScope[] { m_propertiesScope });
                result = evalResult.toJavaObject(Object.class);
            }
            catch (ParseException e)
            {
                LOG.error("Could not parse internal expression : " + propertyExpression, e);
            }
            catch (OwExprEvaluationException e)
            {
                LOG.error("Could not evaluate internal expression : " + propertyExpression, e);
            }

            if (result == null)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.error("The value for property with name: " + propertyExpression_p + " is invalid(null)");
                }
                OwString1 msg = new OwString1("plug.efilekey.propertyresolver.invalid.value", "Invalid value for property %1", propertyExpression_p);
                throw new OwInvalidOperationException(msg.getString1(getLocale(), propertyExpression_p));
            }
        }
        return result;
    }

    /**
     * Get the application counter.
     * @param network_p - the {@link OwNetwork} object.
     * @return the {@link OwParameterMapCodec} object (counter) 
     * @throws OwInvalidOperationException
     */
    private static synchronized OwParameterMapCodec getApplicationCounter(OwNetwork network_p) throws OwInvalidOperationException
    {
        //are network based attribute bags usable ?

        boolean canUseAttributeBags = false;

        try
        {
            network_p.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, "testBag", "testUser", true, true);
            canUseAttributeBags = true;
        }
        catch (Exception e)
        {
            //attribute bags are not available
            LOG.debug("OwEFileKeyPropertyResolverImpl.getUrlParameterCodec(): attribute bags seem to be inaccessible ! The OwTransientCodec will be used ...", e);
        }

        if (canUseAttributeBags)
        {
            return new OwAppCounterBag(network_p);
        }
        else
        {
            return new OwTransientCodec();
        }
    }

    /**
     * Getter for current {@link Locale} object.
     */
    public Locale getLocale()
    {
        return m_locale;
    }
}
