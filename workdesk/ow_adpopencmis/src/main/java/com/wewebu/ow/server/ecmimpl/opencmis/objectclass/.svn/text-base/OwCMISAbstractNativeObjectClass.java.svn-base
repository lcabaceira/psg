package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.client.api.TransientFolder;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.log4j.Logger;

import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISConversionParameters;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativePropertyClassFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISRepositoryObjectSkeleton;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.content.OwCMISContentFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISBulkTransientObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISTransientObject;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISAclDiff;
import com.wewebu.ow.server.ecmimpl.opencmis.permission.OwCMISPermissionCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISBoundVirtualProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISNativeProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISClassDescriptionPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISNativePropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwPropertyDefinitionHelper;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Abstract part implementation of PropertyDefinition and MIME+OwObjectType handling.
 * Implements methods in general for all ObjectType.
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
public abstract class OwCMISAbstractNativeObjectClass<T extends ObjectType, O extends TransientCmisObject> extends OwCMISAbstractSessionObjectClass<OwCMISNativeSession> implements OwCMISNativeObjectClass<T, O>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractNativeObjectClass.class);

    private T objectType;
    private Map<String, OwCMISNativePropertyClass<?, ?, ?>> localProps;

    public OwCMISAbstractNativeObjectClass(T objectType_p, OwCMISNativeSession session_p)
    {
        super(session_p);
        objectType = objectType_p;
        if (objectType_p.getParentTypeId() == null)
        {
            //            List<Integer> nameOperators = Arrays.asList(OwSearchOperator.CRIT_OP_LIKE, OwSearchOperator.CRIT_OP_NOT_LIKE, OwSearchOperator.CRIT_OP_EQUAL, OwSearchOperator.CRIT_OP_NOT_EQUAL);
            //            OwCMISVirtualPropertyClass<String> objectNameProperty = new OwCMISBoundVirtualPropertyClassImpl<String>(new OwObjectNamePropertyClass(), "cmis:name", this, nameOperators);
            //            addVirtualPropertyClass(objectNameProperty);

            //            OwCMISVirtualPropertyClass<String> pathPropertyClass = new OwCMISPathPropertyClass(this);
            //            addVirtualPropertyClass(pathPropertyClass);

            initializeAsHierarchyRoot();

            OwCMISClassDescriptionPropertyClass classDescription = new OwCMISClassDescriptionPropertyClass(this);
            addVirtualPropertyClass(classDescription);

        }
    }

    @Override
    public final int getType()
    {
        BaseTypeId baseTypeId = objectType.getBaseTypeId();
        switch (baseTypeId)
        {
            case CMIS_DOCUMENT:
                return OwObjectReference.OBJECT_TYPE_DOCUMENT;

            case CMIS_FOLDER:
                return OwObjectReference.OBJECT_TYPE_FOLDER;

            case CMIS_POLICY:
                return OwObjectReference.OBJECT_TYPE_CUSTOM;

            case CMIS_RELATIONSHIP:
                return OwObjectReference.OBJECT_TYPE_LINK;

            default:
                return 0;
        }

    }

    @Override
    public String getClassName()
    {
        return objectType.getId();
    }

    @Override
    public String getDescription(Locale locale_p)
    {
        return OwString.localize(locale_p, getClassName() + ".Desc", objectType.getDescription());
    }

    @Override
    public String getMimetype()
    {
        BaseTypeId baseTypeId = objectType.getBaseTypeId();
        switch (baseTypeId)
        {
            case CMIS_DOCUMENT:
                return null;

            case CMIS_FOLDER:
                return OwMimeManager.MIME_TYPE_PREFIX_OW_FOLDER + this.objectType.getId();

            case CMIS_POLICY:
                return OwCMISObjectClass.MIME_TYPE_PREFIX_OW_POLICY + this.objectType.getId();

            case CMIS_RELATIONSHIP:
                return OwCMISObjectClass.MIME_TYPE_PREFIX_OW_RELATIONSHIP + this.objectType.getId();

            default:
                return null;
        }
    }

    @Override
    public boolean isQueryable()
    {
        return objectType.isQueryable();
    }

    @Override
    public String getQueryName()
    {
        return objectType.getQueryName();
    }

    @Override
    public String getDisplayName(Locale locale_p)
    {
        return OwString.localizeLabel(locale_p, getClassName(), objectType.getDisplayName());
    }

    @Override
    public boolean canCreateNewObject() throws OwException
    {
        return objectType.isCreatable();
    }

    @Override
    public T getNativeObject()
    {
        return objectType;
    }

    @Override
    public Map<String, OwCMISPropertyClass<?>> getPropertyClasses() throws OwException
    {
        Map<String, OwCMISPropertyClass<?>> classes = super.getPropertyClasses();
        classes.putAll(getNativePropertyClasses(true));
        return classes;
    }

    @Override
    public Map<String, OwCMISNativePropertyClass<?, ?, ?>> getNativePropertyClasses(boolean localOnly_p) throws OwException
    {
        Map<String, OwCMISNativePropertyClass<?, ?, ?>> classes = new LinkedHashMap<String, OwCMISNativePropertyClass<?, ?, ?>>();

        OwCMISNativeSession session = getSession();
        OwCMISNativePropertyClassFactory propertyClassFactory = session.getNativePropertyClassFactory();

        Map<String, PropertyDefinition<?>> propertyDefinitions = objectType.getPropertyDefinitions();
        Set<Entry<String, PropertyDefinition<?>>> entries = Collections.EMPTY_SET;
        if (propertyDefinitions != null)
        {
            entries = propertyDefinitions.entrySet();
        }
        if (!localOnly_p)
        {
            OwCMISNativeObjectClass<T, O> parent = getParent();
            if (parent != null)
            {
                Map<String, OwCMISNativePropertyClass<?, ?, ?>> parentClasses = parent.getNativePropertyClasses(false);
                classes.putAll(parentClasses);
            }
        }

        if (localProps == null)
        {
            if (LOG.isTraceEnabled())
            {
                LOG.trace("Creating local property classes for = " + getClassName());
            }
            OwCMISNativeObjectClass<T, O> parent = getParent();
            localProps = new LinkedHashMap<String, OwCMISNativePropertyClass<?, ?, ?>>();
            for (Entry<String, PropertyDefinition<?>> entry : entries)
            {
                PropertyDefinition<?> propDef = entry.getValue();
                if (propDef.isInherited())
                {
                    if (parent != null)
                    {
                        PropertyDefinition<?> parentProp = parent.getNativeObject().getPropertyDefinitions().get(entry.getKey());
                        if (OwPropertyDefinitionHelper.isInheritanceDifferent(propDef, parentProp))
                        {
                            if (LOG.isTraceEnabled())
                            {
                                LOG.trace("OwCMISAbstractNativeObjectClass.getNativePropertyClasses: Inherited but different prop-definition: " + propDef);
                            }
                            OwCMISNativePropertyClass<?, ?, ?> propertyClass = propertyClassFactory.createPropertyClass(null, propDef, this);
                            localProps.put(propertyClass.getClassName(), propertyClass);
                        }
                    }
                }
                else
                {
                    OwCMISNativePropertyClass<?, ?, ?> propertyClass = propertyClassFactory.createPropertyClass(null, propDef, this);
                    if (LOG.isTraceEnabled())
                    {
                        LOG.trace("Local property class = " + propertyClass.getClassName());
                    }
                    localProps.put(propertyClass.getClassName(), propertyClass);
                }
            }
        }
        classes.putAll(localProps);
        List<String> preferredPropertyOrder = getSession().getNetwork().getNetworkConfiguration().getPreferedPropertyOrder();
        Map<String, OwCMISNativePropertyClass<?, ?, ?>> orderedClasses = new OwCMISPropertySorter(preferredPropertyOrder).reorderProperties(classes);
        return orderedClasses;
    }

    @Override
    public OwCMISPropertyClass<?> findPropertyClass(String className_p)
    {
        OwCMISPropertyClass<?> propertyClass = getNativePropertyClass(className_p);

        if (propertyClass == null)
        {
            propertyClass = super.findPropertyClass(className_p);
        }

        return propertyClass;
    }

    public OwCMISNativePropertyClass<?, ?, PropertyDefinition<?>> getNativePropertyClass(String className_p)
    {
        OwCMISQualifiedName qName = new OwCMISQualifiedName(className_p);
        OwCMISNativeSession session = getSession();
        OwCMISNativePropertyClassFactory propertyClassFactory = session.getNativePropertyClassFactory();
        Map<String, PropertyDefinition<?>> propertyDefinitions = objectType.getPropertyDefinitions();
        PropertyDefinition<?> definition = null;
        if (propertyDefinitions != null)
        {
            definition = propertyDefinitions.get(qName.getName());
        }
        OwCMISNativeObjectClass<T, O> parent = getParent();

        if (getClassName().equals(qName.getNamespace()))
        {
            if (definition != null)
            {
                try
                {
                    return propertyClassFactory.createPropertyClass(className_p, definition, this);
                }
                catch (OwException e)
                {
                    LOG.error("Could not create property class " + className_p, e);
                    return null;
                }
            }
        }

        if (qName.getNamespace() == null)
        {
            if (definition != null && !definition.isInherited())
            {
                try
                {
                    return propertyClassFactory.createPropertyClass(className_p, definition, this);
                }
                catch (OwException e)
                {
                    LOG.error("Could not create property class " + className_p, e);
                    return null;
                }
            }
        }

        if (parent != null)
        {
            return parent.getNativePropertyClass(className_p);
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwCMISNativeObjectClass<T, O> getParent()
    {
        if (objectType.getParentTypeId() != null)
        {
            try
            {
                return (OwCMISNativeObjectClass<T, O>) getSession().getNativeObjectClass(objectType.getParentTypeId());
            }
            catch (OwException e)
            {
                LOG.error("Could not retrieve parent class of " + getClassName(), e);
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISAbstractObjectClass#createNewObject(boolean, java.lang.Object, com.wewebu.ow.server.ecm.OwPropertyCollection, com.wewebu.ow.server.ecm.OwPermissionCollection, com.wewebu.ow.server.ecm.OwContentCollection, com.wewebu.ow.server.ecm.OwObject, java.lang.String, java.lang.String, boolean)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public String createNewObject(boolean promote_p, Object mode_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String mimeType_p, String mimeParameter_p,
            boolean keepCheckedOut_p) throws OwException
    {
        ObjectId nativeParent = getNativeParentFromObject(parent_p);

        Map<String, Object> cmisProperties = convertToNativeProperties(properties_p);
        OwCMISContentFactory contentFactory = new OwCMISContentFactory(getSession());
        ContentStream contentStream = contentFactory.createContentStream(content_p);

        if (!cmisProperties.containsKey(PropertyIds.OBJECT_TYPE_ID))
        {
            OwCMISNativePropertyClass objectTypeIdPropClass = getNativePropertyClass(PropertyIds.OBJECT_TYPE_ID);
            OwCMISNativeProperty objectTypeIdProp = objectTypeIdPropClass.from(this.getClassName());
            Property nativeProp = objectTypeIdProp.getNativeObject();
            cmisProperties.put(nativeProp.getId(), nativeProp.getValue());
        }
        List<Policy> policies = null;
        List<Ace> addAce = null, removeAce = null;
        if (permissions_p != null)
        {
            OwCMISPermissionCollection perms = (OwCMISPermissionCollection) permissions_p;
            OwCMISAclDiff diff = perms.getDiff();
            addAce = diff.getAdded();
            removeAce = diff.getDeleted();
        }

        ObjectId newId = createNativeObject(cmisProperties, nativeParent, contentStream, promote_p, keepCheckedOut_p, policies, addAce, removeAce);
        return newId.getId();
    }

    /**
     * Get the native parent/representation from provide OwObject.
     * Can throw exception if parent is required, in case parent is required or restricted in any case 
     * and does not match an OwInvalidOperationException should be thrown.
     * @param parent_p OwObject (can be null)
     * @return ObjectId or derived type, can return null in some cases like unfiled object creation
     * @throws OwException
     */
    protected ObjectId getNativeParentFromObject(OwObject parent_p) throws OwException
    {
        TransientFolder nativeParentFolder = null;
        if (null != parent_p)
        {
            if (parent_p instanceof OwCMISNativeObject)
            {
                OwCMISNativeObject parent = (OwCMISNativeObject) parent_p;
                TransientCmisObject natObj = parent.getNativeObject();
                if (natObj instanceof TransientFolder)
                {
                    nativeParentFolder = (TransientFolder) natObj;
                }
                else
                {
                    String msg = "Invalid parent type provided, native java-class = " + (natObj != null ? natObj.getClass() : "null");
                    LOG.warn(msg);
                    throw new OwInvalidOperationException(msg);
                }
            }
            else
            {
                String msg = "Unsupported/Invalid parent type provided, ow java-class = " + parent_p.getClass();
                LOG.warn(msg);
                throw new OwInvalidOperationException(msg);
            }
        }
        return nativeParentFolder;
    }

    /**
     * To be implemented for specific object type, providing already native value representations 
     * @param properties Map of property names (String) to corresponding values (extend Object)
     * @param nativeParentFolder ObjectId native parent folder id representation
     * @param contentStream ContentStream (can be null)
     * @param majorVersion boolean version state (ignored if checkdedOut = true or non versionable type is created)
     * @param checkedOut boolean create checked-out (ignored for non versionable types)
     * @param policies List of org.apache.chemistry.opencmis.client.api.Policy objects (can be null)
     * @param addAce List of ACE's which should be added (can be null)
     * @param removeAce List of ACE's which should be removed (can be null)
     * @return native Id of currently created object
     */
    protected abstract ObjectId createNativeObject(Map<String, Object> properties, ObjectId nativeParentFolder, ContentStream contentStream, boolean majorVersion, boolean checkedOut, List<Policy> policies, List<Ace> addAce, List<Ace> removeAce);

    @SuppressWarnings({ "unchecked" })
    public Map<String, Object> convertToNativeProperties(OwPropertyCollection properties_p) throws OwException
    {
        Map<String, Object> cmisProperties = new HashMap<String, Object>();
        if (properties_p != null)
        {
            Set<Entry<String, OwProperty>> entries = properties_p.entrySet();
            for (Entry<String, OwProperty> entry : entries)
            {
                OwProperty property = entry.getValue();
                Property<?> cmisProperty = convertToNativeProperty(property);
                if (null != cmisProperty)
                {
                    cmisProperties.put(cmisProperty.getId(), cmisProperty.getValue());
                }
            }
        }
        return cmisProperties;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Property<?> convertToNativeProperty(OwProperty property) throws OwException, OwInvalidOperationException
    {
        Property<?> cmisProperty = null;
        if (property instanceof OwCMISNativeProperty<?, ?>)
        {
            OwCMISNativeProperty<?, ?> nativeProperty = (OwCMISNativeProperty<?, ?>) property;
            cmisProperty = nativeProperty.getNativeObject();
        }
        else if (property instanceof OwCMISBoundVirtualProperty<?>)
        {
            OwCMISBoundVirtualProperty<?> boundProperty = (OwCMISBoundVirtualProperty<?>) property;
            OwCMISProperty<?> innerProperty = boundProperty.getBoundProperty();
            if (innerProperty instanceof OwCMISBoundVirtualProperty<?>)
            {
                throw new OwInvalidOperationException("This is way too much for me to handle.");
            }
            return convertToNativeProperty(innerProperty);
        }
        else
        {
            OwPropertyClass propertyClass = null;
            try
            {
                propertyClass = property.getPropertyClass();

                OwCMISNativePropertyClass nativePropertyClass = this.getNativePropertyClass(propertyClass.getClassName());
                if (null != nativePropertyClass)
                {
                    OwCMISNativeProperty nativeProperty = nativePropertyClass.from(property.getValue());
                    cmisProperty = nativeProperty.getNativeObject();
                }
            }
            catch (OwException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new OwInvalidOperationException("Could not convert properties. PropertyClass = " + (propertyClass != null ? propertyClass.getClassName() : "null"), e);
            }
        }
        return cmisProperty;
    }

    protected OperationContext createContext(Map<String, ?> conversionParameters)
    {
        OperationContext creationContext = null;

        if (conversionParameters != null)
        {
            creationContext = (OperationContext) conversionParameters.get(OwCMISConversionParameters.CREATION_CONTEXT);
        }

        if (creationContext == null)
        {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            LOG.warn("Creating default OperationContext because of null creation-OperationContext in call stack " + Arrays.asList(stackTrace));

            creationContext = getSession().getOpenCMISSession().createOperationContext();
        }

        return creationContext;
    }

    @Override
    public List<OwCMISObjectClass> getChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException
    {
        List<OwCMISObjectClass> children = new LinkedList<OwCMISObjectClass>();
        ItemIterable<ObjectType> itemIt = getNativeObject().getChildren();
        Iterator<ObjectType> it = itemIt.iterator();
        while (it.hasNext())
        {
            ObjectType type = it.next();
            children.add(getSession().getObjectClass(type.getId()));
        }
        return children;
    }

    @Override
    public <N extends TransientCmisObject> OwCMISTransientObject<N> newTransientObject(N cmisObject, OperationContext creationContext)
    {
        return new OwCMISBulkTransientObject<N>(cmisObject, creationContext, getSession().getOpenCMISSession());
    }

    @Override
    public OwCMISQueryResultConverter<O> getQueryResultConverter(QueryResult queryResult, OwQueryStatement statement, OperationContext context) throws OwException
    {
        return new OwCMISQueryResultConverterImpl<O>(getSession().getOpenCMISSession());
    }

    public OwObjectSkeleton createSkeletonObject(OwNetwork network_p, OwCMISResource res_p, OwCMISNativeSession ses_p, OwXMLUtil initValues_p) throws Exception
    {
        return new OwCMISRepositoryObjectSkeleton(network_p, this, res_p, ses_p, initValues_p);
    }
}
