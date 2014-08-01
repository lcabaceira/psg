package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISPreferredPropertyTypeCfg;
import com.wewebu.ow.server.ecmimpl.opencmis.field.OwCMISFormat;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString2;

/**
 *<p>
 * OwCMISAbstractPropertyClass.
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
public abstract class OwCMISAbstractPropertyClass<V, C extends OwCMISObjectClass> implements OwCMISPropertyClass<V>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractPropertyClass.class);

    private C objectClass;

    private String className;

    public OwCMISAbstractPropertyClass(String className, C objectClass)
    {
        super();
        this.objectClass = objectClass;
        this.className = className;
    }

    @Override
    public String getCategory()
    {
        return "";
    }

    @Override
    public final String getClassName()
    {
        if (className != null)
        {
            return className;
        }
        else
        {
            return getFullQualifiedName().toString();
        }
    }

    @Override
    public boolean isEnum() throws OwException
    {
        OwEnumCollection myEnums = getEnums();
        return myEnums != null && !myEnums.isEmpty();
    }

    @Override
    public Object getValueFromNode(Node node_p) throws OwException
    {
        if (null == node_p)
        {
            // === a empty node
            return null;
        }
        else if (node_p.getNodeType() == Node.TEXT_NODE)
        {
            // === a text node
            return getValueFromString(node_p.getNodeValue());
        }
        else
        {
            LOG.error("OwCMISAbstractPropertyClass.getValueFromNode(): Unsupported node type : " + node_p.getNodeType());
            throw new OwNotSupportedException(new OwString("opencmis.propertyclass.OwCMISAbstractPropertyClass.unsupported.object.operation", "Unsupported CMIS adapter object operation!"));
        }
    }

    @Override
    public Object getValueFromString(String text_p) throws OwException
    {
        OwCMISFormat format = getFormat();

        if (format == null)
        {
            String msg = "No OwFormat found for property class " + getClassName() + " @ java implementation " + getClass().getName();
            LOG.error("OwCMISAbstractPropertyClass.getValueFromString():" + msg);
            throw new OwInvalidOperationException(new OwString2("opencmis.propertyclass.OwCMISAbstractPropertyClass.noformat.error", "No OwFormat found for property class %1  @ java implementation %2", getClassName(), getClass().getName()));
        }

        return format.parse(text_p, isArray());
    }

    @Override
    public Node getNodeFromValue(Object value_p, Document doc_p)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<?> getComplexChildClasses()
    {
        return null;
    }

    @Override
    public boolean isComplex()
    {
        return false;
    }

    @Override
    public C getObjectClass()
    {
        return objectClass;
    }

    @Override
    public OwCMISQualifiedName getFullQualifiedName()
    {
        OwCMISObjectClass objectClass = getObjectClass();
        String objectClassName = objectClass.getClassName();
        return new OwCMISQualifiedName(objectClassName, getNonQualifiedName());
    }

    @Override
    public OwCMISPropertyClass<V> createProxy(String className)
    {
        return new OwCMISPropertyClassProxy<V, C>(className, this, getObjectClass());
    }

    protected abstract boolean isSystem() throws OwException;

    @Override
    public final boolean isSystemProperty() throws OwException
    {
        OwCMISPreferredPropertyTypeCfg.PropertyType prefferedType = getPreferredPropertyType();

        if (OwCMISPreferredPropertyTypeCfg.PropertyType.UNSET == prefferedType)
        {
            return isSystem();
        }

        return OwCMISPreferredPropertyTypeCfg.PropertyType.SYSTEM == prefferedType;
    }

    protected OwCMISPreferredPropertyTypeCfg.PropertyType getPreferredPropertyType() throws OwException
    {
        return objectClass.getPreferredPropertyType(this);
    }

}
