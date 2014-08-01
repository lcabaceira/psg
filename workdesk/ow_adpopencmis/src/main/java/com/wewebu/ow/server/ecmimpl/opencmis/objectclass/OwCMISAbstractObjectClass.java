package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.opencmis.commons.PropertyIds;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISObjectNamePropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPathPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISVirtualPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwCMISAbstractObjectClass.
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
public abstract class OwCMISAbstractObjectClass implements OwCMISObjectClass
{
    private Map<String, OwCMISVirtualPropertyClass<?>> virtualProperties;

    protected void initializeAsHierarchyRoot()
    {
        OwCMISVirtualPropertyClass<String> objectNameProperty = new OwCMISObjectNamePropertyClass(this);
        addVirtualPropertyClass(objectNameProperty);

        OwCMISVirtualPropertyClass<String> pathPropertyClass = new OwCMISPathPropertyClass(this);
        addVirtualPropertyClass(pathPropertyClass);
    }

    @Override
    public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p) throws Exception
    {
        return false;
    }

    protected final <V> V getParameterValue(Map<String, ?> conversionParameters, String parameter, V defaultValue)
    {
        if (conversionParameters != null)
        {
            if (conversionParameters.containsKey(parameter))
            {
                return (V) conversionParameters.get(parameter);
            }
        }

        return defaultValue;
    }

    @Override
    public boolean hasVersionSeries() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List getModes(int operation_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDescription(Locale locale_p)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isHidden() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public OwCMISPropertyClass<?> findPropertyClass(String className_p)
    {
        return getVirtualPropertyClass(className_p);
    }

    @Override
    public final OwCMISPropertyClass<?> getPropertyClass(String className_p) throws OwException
    {
        OwCMISPropertyClass<?> propertyClass = findPropertyClass(className_p);

        if (propertyClass != null)
        {
            return propertyClass;
        }
        else
        {
            throw new OwObjectNotFoundException("No such property " + className_p + " defined by " + getClassName());
        }
    }

    @Override
    public final Collection<String> getPropertyClassNames() throws OwException
    {
        return getPropertyClasses().keySet();
    }

    @Override
    public Map<String, OwCMISPropertyClass<?>> getPropertyClasses() throws OwException
    {
        Map<String, OwCMISPropertyClass<?>> properties = new LinkedHashMap<String, OwCMISPropertyClass<?>>();

        OwCMISObjectClass parent = getParent();
        if (parent != null)
        {
            properties.putAll(parent.getPropertyClasses());
        }

        Map<String, OwCMISVirtualPropertyClass<?>> virtualProperties = getVirtualPropertyClasses(true);

        properties.putAll(virtualProperties);

        return properties;
    }

    public OwCMISVirtualPropertyClass<?> getVirtualPropertyClass(String className_p)
    {
        OwCMISQualifiedName qName = new OwCMISQualifiedName(className_p);
        String thisClassName = getClassName();
        OwCMISVirtualPropertyClass<?> propertyClass = null;
        if (qName.getNamespace() == null || thisClassName.equals(qName.getNamespace()))
        {
            Map<String, OwCMISVirtualPropertyClass<?>> virtualProperties = getVirtualPropertyClasses(false);
            propertyClass = virtualProperties.get(new OwCMISQualifiedName(thisClassName, qName.getName()).toString());
            if (propertyClass != null && qName.getNamespace() == null)
            {
                propertyClass = propertyClass.createProxy(className_p);
            }

        }

        if (propertyClass == null)
        {

            OwCMISObjectClass parent = getParent();
            if (parent != null)
            {
                propertyClass = parent.getVirtualPropertyClass(className_p);
            }
            else
            {
                propertyClass = getVirtualPropertyClasses(false).get(className_p);
            }
        }

        return propertyClass;
    }

    public synchronized Map<String, OwCMISVirtualPropertyClass<?>> getVirtualPropertyClasses(boolean localOnly_p)
    {
        Map<String, OwCMISVirtualPropertyClass<?>> properties = new HashMap<String, OwCMISVirtualPropertyClass<?>>();

        if (!localOnly_p)
        {
            OwCMISObjectClass parent = getParent();
            if (parent != null)
            {
                properties.putAll(parent.getVirtualPropertyClasses(false));
            }
        }

        if (virtualProperties != null)
        {
            properties.putAll(virtualProperties);
        }

        return properties;
    }

    public synchronized void addVirtualPropertyClass(OwCMISVirtualPropertyClass<?> virtualPropertyClass_p)
    {
        if (virtualProperties == null)
        {
            virtualProperties = new HashMap<String, OwCMISVirtualPropertyClass<?>>();
        }
        virtualProperties.put(virtualPropertyClass_p.getClassName(), virtualPropertyClass_p);
    }

    @Override
    public List<OwCMISObjectClass> getChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, OwCMISObjectClass> getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isContentRequired() throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isQueryable()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getQueryName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String createNewObject(boolean promote_p, Object mode_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p, String strMimeParameter_p,
            boolean keepCheckedOut_p) throws OwException
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("Subclass responsibility.");
    }

    @Override
    public Set<OwCMISPropertyClass<?>> getQueryablePropertyClasses() throws OwException
    {
        Set<OwCMISPropertyClass<?>> qProps = new HashSet<OwCMISPropertyClass<?>>();
        Map<String, OwCMISPropertyClass<?>> allProperties = this.getPropertyClasses();
        for (OwCMISPropertyClass<?> propertyClass : allProperties.values())
        {
            if (propertyClass.isQueryable())
            {
                qProps.add(propertyClass);
            }
        }
        return qProps;
    }

    @Override
    public String getNamePropertyName() throws OwException
    {
        return getPropertyClass(PropertyIds.NAME).getFullQualifiedName().toString();
    }

    @Override
    public void subclassedBy(OwCMISObjectClass subclass_p) throws OwInvalidOperationException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isAssignableFrom(OwCMISObjectClass class_p) throws OwException
    {
        OwCMISObjectClass fromClass = class_p;
        while (fromClass != null)
        {
            if (fromClass.getClassName().equals(getClassName()) && fromClass.getType() == getType())
            {
                return true;
            }
            else
            {
                fromClass = fromClass.getParent();
            }
        }

        return false;
    }

    @Override
    public boolean canCreateNewObject() throws OwException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getCopyClassName()
    {
        return getClassName();
    }

    public OwObjectSkeleton createSkeletonObject(OwNetwork network_p, OwCMISResource res_p, OwCMISNativeSession session_p, OwXMLUtil initValues_p) throws Exception
    {
        throw new OwInvalidOperationException(new OwString("opencmis.OwCMISAbstractObjectClass.createSkeleton.invalidOperation", "This is a non instationable object type."));
    }
}
