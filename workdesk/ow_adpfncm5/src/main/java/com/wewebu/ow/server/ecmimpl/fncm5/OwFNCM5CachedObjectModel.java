package com.wewebu.ow.server.ecmimpl.fncm5;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.collection.PropertyTemplateSet;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.core.EngineObject;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ObjectStore;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5Class;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5DefaultObjectClassFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ObjectClassFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ResourceAccessor;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5DefaultPropertyClassFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5EnginePropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5PropertyClassFactory;
import com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.OwFNCM5VirtualBinding;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * A content object model implementation with cached object class and 
 * property class information.  
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
public class OwFNCM5CachedObjectModel implements OwFNCM5ContentObjectModel
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5CachedObjectModel.class);

    private Map<String, OwFNCM5Class<?, ?>> classes = new LinkedHashMap<String, OwFNCM5Class<?, ?>>();
    private Map<String, OwFNCM5EnginePropertyClass<PropertyTemplate, ?, ?>> propertyTemplateClasses = null;
    private Map<String, OwFNCM5EnginePropertyClass<PropertyDescription, ?, ?>> basicPropertyClasses = null;
    private Map<String, OwFNCM5PropertyClass> virtualPropertyClasses = null;

    protected OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor;

    protected OwFNCM5PropertyClassFactory propertyClassFactory;
    protected OwFNCM5ObjectClassFactory objectClassFactory;

    public OwFNCM5CachedObjectModel(OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p)
    {
        this(resourceAccessor_p, new OwFNCM5DefaultPropertyClassFactory(resourceAccessor_p));
    }

    public OwFNCM5CachedObjectModel(OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resourceAccessor_p, OwFNCM5PropertyClassFactory propertyClassFactory_p)
    {
        this(resourceAccessor_p, propertyClassFactory_p, new OwFNCM5DefaultObjectClassFactory(propertyClassFactory_p, resourceAccessor_p));
    }

    public OwFNCM5CachedObjectModel(OwFNCM5ResourceAccessor<OwFNCM5ObjectStoreResource> resource_p, OwFNCM5PropertyClassFactory propertyClassFactory, OwFNCM5ObjectClassFactory objectClassFactory_p)
    {
        super();
        this.resourceAccessor = resource_p;
        this.propertyClassFactory = propertyClassFactory;
        this.objectClassFactory = objectClassFactory_p;
    }

    private synchronized void cacheBasicPropertyClasses()
    {
        if (this.basicPropertyClasses == null)
        {
            this.basicPropertyClasses = new HashMap<String, OwFNCM5EnginePropertyClass<PropertyDescription, ?, ?>>();
            cacheEnginePropertyClasses(this.basicPropertyClasses, new String[] { ClassNames.UPDATE_EVENT, ClassNames.DOCUMENT, ClassNames.FOLDER, ClassNames.CUSTOM_OBJECT });
        }
    }

    private synchronized void cacheVirtualPropertyClasses()
    {
        if (this.virtualPropertyClasses == null)
        {
            this.virtualPropertyClasses = new HashMap<String, OwFNCM5PropertyClass>();
            cacheVirtualPropertyClasses(this.virtualPropertyClasses, new String[] { ClassNames.UPDATE_EVENT, ClassNames.DOCUMENT, ClassNames.FOLDER, ClassNames.CUSTOM_OBJECT });
        }
    }

    private synchronized void cacheTemplatePropertyClasses()
    {
        if (this.propertyTemplateClasses == null)
        {
            try
            {
                ObjectStore objectStore = getObjectStore();
                this.propertyTemplateClasses = new HashMap<String, OwFNCM5EnginePropertyClass<PropertyTemplate, ?, ?>>();
                PropertyTemplateSet propertyTemplates = objectStore.get_PropertyTemplates();
                Iterator templateIt = propertyTemplates.iterator();
                while (templateIt.hasNext())
                {
                    PropertyTemplate template = (PropertyTemplate) templateIt.next();
                    try
                    {
                        OwFNCM5EnginePropertyClass<PropertyTemplate, ?, ?> fncmClass = propertyClassFactory.createPropertyClass(template);
                        this.propertyTemplateClasses.put(fncmClass.getClassName(), fncmClass);
                    }
                    catch (Exception e)
                    {
                        LOG.error("Error caching template property template " + template.get_SymbolicName(), e);
                    }

                }
            }
            catch (Exception e)
            {
                LOG.error("Error caching template properties.", e);
            }

        }
    }

    private void cacheEnginePropertyClasses(Map<String, OwFNCM5EnginePropertyClass<PropertyDescription, ?, ?>> propertyClasses_p, String[] baseClasses_p)
    {
        for (String baseClass : baseClasses_p)
        {
            try
            {
                ObjectStore objectStore = getObjectStore();
                ClassDescription basicClassDescription = Factory.ClassDescription.fetchInstance(objectStore, baseClass, null);
                Integer nameIndex = basicClassDescription.get_NamePropertyIndex();
                PropertyDescriptionList basicPropertyDescriptions = basicClassDescription.get_PropertyDescriptions();
                Iterator i = basicPropertyDescriptions.iterator();
                while (i.hasNext())
                {
                    PropertyDescription propertyDescription = (PropertyDescription) i.next();
                    try
                    {
                        OwFNCM5EnginePropertyClass<PropertyDescription, ?, ?> propertyClass = propertyClassFactory.createPropertyClass(propertyDescription, false);
                        String propertyClassName = propertyClass.getClassName();
                        propertyClasses_p.put(propertyClassName, propertyClass);
                    }
                    catch (Exception e)
                    {
                        LOG.error("Error while caching property " + propertyDescription.get_SymbolicName() + " of base class " + baseClass + " .", e);
                    }
                }
            }
            catch (Exception e)
            {
                LOG.error("Error while caching properties of base class " + baseClass + " .", e);
            }
        }

    }

    private void cacheVirtualPropertyClasses(Map<String, OwFNCM5PropertyClass> propertyClasses_p, String[] objectClasses_p)
    {
        for (String objectClassName : objectClasses_p)
        {
            try
            {
                OwFNCM5Class<?, ?> objectClass = objectClassForName(objectClassName);
                List<String> propertyClassNames = objectClass.getPropertyClassNames();
                for (String propertyClassName : propertyClassNames)
                {
                    try
                    {
                        OwFNCM5PropertyClass propertyClass = objectClass.getPropertyClass(propertyClassName);
                        OwFNCM5VirtualBinding virtualBinding = propertyClass.getVirtualBinding();

                        if (virtualBinding != null)
                        {
                            propertyClasses_p.put(propertyClassName, propertyClass);
                        }
                    }
                    catch (Exception e)
                    {
                        LOG.error("Error caching virtual property " + propertyClassName + " of class " + objectClassName, e);
                    }

                }
            }
            catch (Exception e)
            {
                LOG.error("Error caching virtual properties of " + objectClassName, e);
            }
        }
    }

    private synchronized OwFNCM5EnginePropertyClass<?, ?, ?> enginePropertyClassForName(String propertyClassName_p)
    {
        cacheTemplatePropertyClasses();
        OwFNCM5EnginePropertyClass<?, ?, ?> templateClass = this.propertyTemplateClasses.get(propertyClassName_p);
        if (templateClass != null)
        {
            return templateClass;
        }
        else
        {
            cacheBasicPropertyClasses();
            OwFNCM5EnginePropertyClass<?, ?, ?> basicClass = this.basicPropertyClasses.get(propertyClassName_p);
            if (basicClass != null)
            {
                return basicClass;
            }
            else
            {
                return null;
            }
        }
    }

    private synchronized OwFNCM5PropertyClass virtualPropertyClassForName(String propertyClassName_p)
    {
        cacheVirtualPropertyClasses();
        OwFNCM5PropertyClass virtualClass = this.virtualPropertyClasses.get(propertyClassName_p);
        return virtualClass;
    }

    public synchronized OwFNCM5PropertyClass propertyClassForName(String propertyClassName_p) throws OwException
    {

        OwFNCM5PropertyClass propertyClass = enginePropertyClassForName(propertyClassName_p);

        if (propertyClass == null)
        {
            propertyClass = virtualPropertyClassForName(propertyClassName_p);
        }

        if (propertyClass == null)
        {
            throw new OwObjectNotFoundException("Could not find property class " + propertyClassName_p);
        }
        else
        {
            return propertyClass;
        }
    }

    protected ObjectStore getObjectStore() throws OwException
    {
        OwFNCM5ObjectStoreResource resource = this.resourceAccessor.get();
        OwFNCM5ObjectStore objectStoreObject = resource.getObjectStore();
        return objectStoreObject.getNativeObject();
    }

    private synchronized OwFNCM5Class<?, ?> readClassThrough(String objectClassName_p) throws OwException
    {
        OwFNCM5Class<?, ?> objectClass = this.classes.get(objectClassName_p);
        if (objectClass == null)
        {
            ObjectStore os = getObjectStore();
            try
            {
                ClassDescription classDescription = Factory.ClassDescription.fetchInstance(os, objectClassName_p, null);
                objectClass = createObjectClass(classDescription);
                if (objectClass != null)
                {
                    this.classes.put(objectClassName_p, objectClass);
                }
            }
            catch (EngineRuntimeException e)
            {
                String msg = "Could not retrieve object class information for " + objectClassName_p;
                LOG.error(msg, e);
                throw new OwObjectNotFoundException(msg, e);
            }
        }

        return objectClass;
    }

    private synchronized void readTypeThrough(Map<String, String> classNames_p, int type_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p) throws OwException
    {
        String rootSymbolicName = null;

        if (OwObjectReference.OBJECT_TYPE_DOCUMENT == type_p)
        {
            rootSymbolicName = ClassNames.DOCUMENT;
        }
        else if (OwObjectReference.OBJECT_TYPE_FOLDER == type_p)
        {
            rootSymbolicName = ClassNames.FOLDER;
        }
        else if (OwObjectReference.OBJECT_TYPE_CUSTOM == type_p)
        {
            rootSymbolicName = ClassNames.CUSTOM_OBJECT;
        }
        else
        {
            LOG.warn("Cache-read-through request for unsupported AWD type " + type_p);
            return;
        }

        OwFNCM5Class<?, ?> rootClass = readClassThrough(rootSymbolicName);
        String className = rootClass.getClassName();
        String displayName = rootClass.getDisplayName(null);

        classNames_p.put(className, displayName);

        if (!fRootOnly_p)
        {
            OwFNCM5ObjectStoreResource resource = resourceAccessor.get();
            OwFNCM5Network network = resource.getNetwork();

            Stack<OwObjectClass> classStack = new Stack<OwObjectClass>();
            classStack.push(rootClass);
            while (!classStack.isEmpty())
            {
                OwObjectClass currentClass = classStack.pop();
                List subclasses = null;

                try
                {
                    subclasses = currentClass.getChilds(network, fExcludeHiddenAndNonInstantiable_p);
                }
                catch (OwException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                    throw new OwServerException("Could not retrieve subclasses.", e);
                }

                if (subclasses != null)
                {
                    for (Iterator i = subclasses.iterator(); i.hasNext();)
                    {
                        OwObjectClass objectClass = (OwObjectClass) i.next();
                        if (type_p == objectClass.getType())
                        {
                            String subclassName = objectClass.getClassName();
                            String subclassDisplayName = objectClass.getDisplayName(null);
                            classNames_p.put(subclassName, subclassDisplayName);
                        }
                        classStack.push(objectClass);
                    }

                }
                else
                {
                    LOG.warn("Subclasses are not supported for object class :" + rootClass);
                }
            }
        }

    }

    private synchronized void readClassesThrough(Map<String, String> classNames_p, int[] types_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p) throws OwException
    {

        int[] classTypes = types_p;

        if (types_p == null)
        {
            classTypes = new int[] { OwObjectReference.OBJECT_TYPE_DOCUMENT, OwObjectReference.OBJECT_TYPE_FOLDER, OwObjectReference.OBJECT_TYPE_CUSTOM };
        }

        for (int i = 0; i < classTypes.length; i++)
        {
            readTypeThrough(classNames_p, classTypes[i], fExcludeHiddenAndNonInstantiable_p, fRootOnly_p);
        }
    }

    public <O> OwFNCM5Class<O, ?> classOf(O nativeObject_p) throws OwException
    {

        if (nativeObject_p == null)
        {
            throw new OwInvalidOperationException("<null> object class request!");
        }

        if (nativeObject_p instanceof EngineObject)
        {
            EngineObject e = (EngineObject) nativeObject_p;
            ClassDescription classDescription = e.get_ClassDescription();
            String className = classDescription.get_SymbolicName();
            return (OwFNCM5Class<O, ?>) objectClassForName(className);
        }
        else
        {
            throw new OwInvalidOperationException("Usupported native object " + nativeObject_p.getClass());
        }
    }

    public OwFNCM5Class<?, ?> objectClassForName(String objectClassName_p) throws OwException
    {
        return readClassThrough(objectClassName_p);
    }

    private OwFNCM5Class<?, ?> createObjectClass(ClassDescription classDescription_p) throws OwException
    {
        OwFNCM5Class<?, ?> objectClass = this.objectClassFactory.createObjectClass(classDescription_p);

        if (objectClass == null)
        {
            LOG.info("OwFNCM5CachedObjectModel.createObjectClass: Could not create class for class description " + classDescription_p.get_SymbolicName());
        }

        return objectClass;
    }

    public synchronized Map<String, String> objectClassNamesWith(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p) throws OwException
    {

        Map<String, String> objectClassNames = new LinkedHashMap<String, String>();

        readClassesThrough(objectClassNames, iTypes_p, fExcludeHiddenAndNonInstantiable_p, fRootOnly_p);

        return objectClassNames;
    }

    @Override
    public String toString()
    {
        return "OwFNCM5CachedObjectModel( " + propertyClassFactory.toString() + " , " + objectClassFactory.toString() + " )";
    }
}
