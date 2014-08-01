package com.wewebu.ow.server.ecmimpl.fncm5.objectclass;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.filenet.api.core.EngineObject;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineObject;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineState;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5ClassPropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.virtual.OwFNCM5VirtualPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * Base of the P8 5.0 engine defined content-object class hierarchy.
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
public class OwFNCM5EngineObjectClass<E extends EngineObject, R extends OwFNCM5Resource> extends OwFNCM5GenericClass<E, R>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5EngineObjectClass.class);

    private OwFNCM5EngineClassDeclaration<?, R> declaration;
    private Map<String, OwFNCM5EnginePropertyClass<?, ?, ?>> enginePropertiesCache;
    private String namePropertyName;

    public OwFNCM5EngineObjectClass(OwFNCM5EngineClassDeclaration<?, R> declaration_p, OwFNCM5ResourceAccessor<R> resourceAccessor_p)
    {
        super(resourceAccessor_p);
        this.declaration = declaration_p;
    }

    protected OwFNCM5EngineClassDeclaration<?, R> getClassDeclaration()
    {
        return this.declaration;
    }

    public boolean canCreateNewObject() throws OwException
    {
        if (declaration.allowsInstances())
        {
            return super.canCreateNewObject();
        }
        else
        {
            return false;
        }
    }

    public String getClassName()
    {
        return declaration.getSymbolicName();
    }

    public String getDescription(Locale locale_p)
    {
        return declaration.getDescriptiveText(locale_p);
    }

    public String getDisplayName(Locale locale_p)
    {
        return declaration.getDisplayName(locale_p);
    }

    public boolean hasVersionSeries() throws OwException
    {
        return declaration.hasVersionSeries();
    }

    public boolean isHidden() throws Exception
    {
        return declaration.isHidden();
    }

    public String getId()
    {
        return declaration.getId();
    }

    protected synchronized void cacheEnginePropertyClasses() throws OwException
    {
        if (this.enginePropertiesCache == null)
        {
            this.enginePropertiesCache = this.declaration.getPropertyClasses();
            Set<String> cachedKeys = this.enginePropertiesCache.keySet();
        }
    }

    public String getNamePropertyName() throws OwException
    {
        if (this.namePropertyName == null)
        {
            cacheEnginePropertyClasses();
            Set<Entry<String, OwFNCM5EnginePropertyClass<?, ?, ?>>> properties = this.enginePropertiesCache.entrySet();
            for (Entry<String, OwFNCM5EnginePropertyClass<?, ?, ?>> property : properties)
            {
                OwFNCM5EnginePropertyClass<?, ?, ?> engineProperty = property.getValue();
                try
                {
                    if (engineProperty.isNameProperty())
                    {
                        this.namePropertyName = engineProperty.getClassName();
                        break;
                    }
                }
                catch (OwException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                    throw new OwInvalidOperationException("Could not retrieve the name property", e);

                }
            }
        }

        if (this.namePropertyName != null)
        {
            return this.namePropertyName;
        }
        else
        {
            return "Id";
        }
    }

    public final OwFNCM5PropertyClass getPropertyClass(String strClassName_p) throws OwException
    {
        OwFNCM5PropertyClass propertyClass = enginePropertyClass(strClassName_p);
        if (propertyClass == null)
        {
            propertyClass = virtualPropertyClass(strClassName_p);
        }

        if (propertyClass != null)
        {
            R resource = getResource();
            OwFNCM5Network network = resource.getNetwork();
            OwRoleManager roleManager = network.getRoleManager();
            return new OwFNCM5ClassPropertyClass(propertyClass, this, roleManager);
        }
        else
        {
            throw new OwObjectNotFoundException("Could not find property description " + strClassName_p);
        }

    }

    protected OwFNCM5EnginePropertyClass<?, ?, ?> enginePropertyClass(String className_p) throws OwException
    {
        cacheEnginePropertyClasses();
        OwFNCM5EnginePropertyClass<?, ?, ?> enginePropertyClass = enginePropertiesCache.get(className_p);
        return enginePropertyClass;
    }

    protected OwFNCM5VirtualPropertyClass virtualPropertyClass(String className_p) throws OwException
    {
        if (OwResource.m_ClassDescriptionPropertyClass.getClassName().equals(className_p))
        {
            //            OwFNCM5EnginePropertyClass<?, ?, ?> classDescriptionClass = getEnginePropertyClass(PropertyNames.CLASS_DESCRIPTION);
            return new OwFNCM5VirtualPropertyClass(OwResource.m_ClassDescriptionPropertyClass);
        }
        else if (OwResource.m_ResourcePropertyClass.getClassName().equals(className_p))
        {
            return new OwFNCM5VirtualPropertyClass(OwResource.m_ResourcePropertyClass);
        }
        else if (OwResource.m_ObjectNamePropertyClass.getClassName().equals(className_p))
        {
            String namePropertyClassName = getNamePropertyName();
            OwFNCM5EnginePropertyClass<?, ?, ?> namePropertyClass = enginePropertyClass(namePropertyClassName);
            return new OwFNCM5VirtualPropertyClass(OwResource.m_ObjectNamePropertyClass, namePropertyClass);
        }
        else
        {
            return null;
        }
    }

    public List<String> getEnginePropertiesClassNames() throws OwException
    {
        cacheEnginePropertyClasses();
        LinkedList<String> names = new LinkedList<String>();
        names.addAll(enginePropertiesCache.keySet());

        return names;
    }

    public List<String> getVirtualPropertiesClassNames() throws OwException
    {
        List<String> names = new LinkedList<String>();

        names.add(OwResource.m_ClassDescriptionPropertyClass.getClassName());
        names.add(OwResource.m_ObjectNamePropertyClass.getClassName());
        names.add(OwResource.m_ResourcePropertyClass.getClassName());

        return names;
    }

    public final List<String> getPropertyClassNames() throws OwException
    {
        List<String> names = getEnginePropertiesClassNames();
        List<String> virtualNames = getVirtualPropertiesClassNames();

        names.addAll(virtualNames);

        R resource = getResource();
        OwFNCM5Network network = resource.getNetwork();

        // order the  property list
        List<?> preferedPropertyOrder = null;

        try
        {
            preferedPropertyOrder = network.getPreferedPropertyOrder();
        }
        catch (Exception e)
        {
            LOG.warn("Could not retrieve prefered property order", e);
        }

        names = reorderProperties(names, preferedPropertyOrder);

        return names;
    }

    /**
     * order the given property key list in a given preferred order
     * @param propertyKeyList_p Collection of available property names
     * @param preferedPropertyOrder_p List of String representing property names (can be null)
     * @return the list of ordered properties
     */
    private List<String> reorderProperties(Collection<?> propertyKeyList_p, List<?> preferedPropertyOrder_p)
    {
        List<String> orderedProperties = new LinkedList<String>();

        if (preferedPropertyOrder_p != null)
        {
            // first add the preferred properties which are contained in the propertyKeyList
            for (Iterator<?> it = preferedPropertyOrder_p.iterator(); it.hasNext();)
            {
                String preferedOrderProperty = (String) it.next();
                if (propertyKeyList_p.contains(preferedOrderProperty))
                {
                    orderedProperties.add(preferedOrderProperty);
                }
            }
        }

        // then add the rest
        for (Iterator<?> it = propertyKeyList_p.iterator(); it.hasNext();)
        {
            String propertyKey = (String) it.next();
            if (!orderedProperties.contains(propertyKey))
            {
                orderedProperties.add(propertyKey);
            }
        }
        return orderedProperties;
    }

    @Override
    public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p) throws OwException
    {
        List<?> children = getChilds(network_p, fExcludeHiddenAndNonInstantiable_p);
        return !children.isEmpty();
    }

    public List<OwObjectClass> getChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException
    {
        try
        {

            List<String> subclassNames = declaration.getSubclassesNames();
            List<OwObjectClass> subclasses = new LinkedList<OwObjectClass>();
            for (String subclassName : subclassNames)
            {
                OwObjectClass subclass = network_p.getObjectClass(subclassName, getResource());
                subclasses.add(subclass);
            }

            return subclasses;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not retrieve subclasses.", e);
        }

    }

    public OwFNCM5Object<E> from(E engineObject_p, OwFNCM5ObjectFactory factory_p) throws OwException
    {

        //        if (nativeObject_p instanceof IndependentObject)
        //        {
        //            defaultObject = new OwFNCM5DefaultIndependentObject((IndependentObject) nativeObject_p, this);
        //        }

        //        return new OwFNCM5EngineObject<E, R>(engineObject_p, this);

        return factory_p.create(OwFNCM5EngineObject.class, new Class[] { EngineObject.class, OwFNCM5EngineObjectClass.class }, new Object[] { engineObject_p, this });
    }

    /**
     * Self-state factory.
     * An object's self is a reference to its own state (similar to Java's <i>this</i> ). 
     *   
     * @param engineObject_p
     * @return a new state object for the given {@link EngineObject}
     */
    public OwFNCM5EngineState<E> createSelf(E engineObject_p)
    {
        return new OwFNCM5EngineState<E>(engineObject_p, this);
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_CUSTOM;
    }

    public OwFNCM5Constructor<E, R> getConstructor() throws OwException
    {
        throw new OwInvalidOperationException("Unimplemented constructor for Engine Class " + getClassName());
    }

    @SuppressWarnings("unchecked")
    public OwFNCM5Class<E, R> getParent() throws OwException
    {
        String superName = getClassDeclaration().getSuperclassSymbolicName();
        if (null != superName)
        {
            return (OwFNCM5Class<E, R>) getResource().getObjectModel().objectClassForName(superName);
        }
        else
        {
            return null;
        }
    }

    public Set<OwFNCM5Object<?>> getWorkflowDescriptions() throws OwException
    {
        //default implementation
        return Collections.emptySet();
    }
}
