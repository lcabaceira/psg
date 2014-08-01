package com.wewebu.ow.server.ecmimpl.opencmis.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.util.OwNetworkConfiguration;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Accessor for the &lt;PreferredPropertyType&gt; configuration from owbootstrap.xml.
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
public class OwCMISPreferredPropertyTypeCfg
{
    private static final Logger LOG = OwLog.getLogger(OwCMISPreferredPropertyTypeCfg.class);

    private static final String ATT_TYPE = "type";
    private static final String PROPERTY_TYPE_USER = "user";
    private static final String PROPERTY_TYPE_SYSTEM = "system";
    private Map<String, PropertyType> preferredPropertyTypeMap = new HashMap<String, PropertyType>();

    /**
     * @param configNode
     */
    @SuppressWarnings("rawtypes")
    public OwCMISPreferredPropertyTypeCfg(OwXMLUtil configNode)
    {
        List properties = configNode.getSafeNodeList(OwNetworkConfiguration.EL_PREFERREDPROPERTYTYPE);
        for (Object object : properties)
        {
            Element propDefElem = (Element) object;
            String propType = OwXMLDOMUtil.getSafeStringAttributeValue(propDefElem, ATT_TYPE, null);
            String propName = OwXMLDOMUtil.getElementText(propDefElem);
            if ((propName != null) && (propType != null))
            {
                propType = propType.trim();
                propName = propName.trim();

                PropertyType propTypeValue = null;
                if (propType.equalsIgnoreCase(PROPERTY_TYPE_USER))
                {
                    propTypeValue = PropertyType.USER;
                }
                else if (propType.equalsIgnoreCase(PROPERTY_TYPE_SYSTEM))
                {
                    propTypeValue = PropertyType.SYSTEM;
                }
                else
                {
                    LOG.error(String.format("The value '%s' for the '%s' attribute is invalid.", propType, ATT_TYPE));
                }
                preferredPropertyTypeMap.put(propName, propTypeValue);
            }
        }
    }

    public PropertyType getPreferredType(OwCMISPropertyClass propertyClass) throws OwException
    {
        PropertyType preferredType = this.getPreferredType(propertyClass.getNonQualifiedName(), propertyClass.getObjectClass());
        if (null == preferredType)
        {
            preferredType = PropertyType.UNSET;
        }
        this.preferredPropertyTypeMap.put(propertyClass.getFullQualifiedName().toString(), preferredType);
        return preferredType;
    }

    private PropertyType getPreferredType(String shortName, OwCMISObjectClass inClass) throws OwException
    {
        PropertyType prefferedType = null;
        OwCMISPropertyClass property = null;

        try
        {
            property = inClass.getPropertyClass(shortName);
        }
        catch (OwObjectNotFoundException nfe)
        {
            return PropertyType.UNSET;
        }

        OwCMISQualifiedName qName = property.getFullQualifiedName();

        String longName = qName.toString();

        prefferedType = this.getPropertyTypeFor(longName);

        //Try the short name
        if (null == prefferedType)
        {
            prefferedType = this.getPropertyTypeFor(shortName);
        }

        //Try the super classes
        if (null == prefferedType)
        {
            OwCMISObjectClass parentClass = inClass.getParent();
            if (null != parentClass)
            {
                prefferedType = getPreferredType(shortName, parentClass);
            }
        }

        return prefferedType;
    }

    protected PropertyType getPropertyTypeFor(String propName)
    {
        return this.preferredPropertyTypeMap.get(propName);
    }

    public enum PropertyType
    {
        SYSTEM, USER, UNSET
    }
}