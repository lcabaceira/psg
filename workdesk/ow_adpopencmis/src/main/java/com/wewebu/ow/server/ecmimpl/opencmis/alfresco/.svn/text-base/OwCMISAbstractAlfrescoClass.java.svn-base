package com.wewebu.ow.server.ecmimpl.opencmis.alfresco;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.TransientCmisObject;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.log4j.Logger;

import com.wewebu.ow.csqlc.ast.OwQueryStatement;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectSkeleton;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISConversionParameters;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISRepositoryObjectSkeleton;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISResource;
import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISPreferredPropertyTypeCfg.PropertyType;
import com.wewebu.ow.server.ecmimpl.opencmis.exception.OwCMISRuntimeException;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISAbstractNativeObject;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISTransientObject;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISAbstractObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISPropertySorter;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISQueryResultConverter;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISBoundVirtualProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISNativeProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.property.OwCMISProperty;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISNativePropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISVirtualPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISQualifiedName;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwCMISAbstractAlfrescoClass.
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
public abstract class OwCMISAbstractAlfrescoClass<T extends ObjectType, O extends TransientCmisObject> extends OwCMISAbstractObjectClass implements OwCMISNativeObjectClass<T, O>
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwCMISAbstractNativeObject.class);

    private OwCMISNativeObjectClass<T, O> nativeObjectClass;

    public OwCMISAbstractAlfrescoClass(OwCMISNativeObjectClass<T, O> nativeObjectClass)
    {
        super();
        this.nativeObjectClass = nativeObjectClass;
    }

    protected abstract Collection<OwCMISNativeObjectClass<?, ?>> getAspectsClasses(boolean secure) throws OwException;

    protected abstract boolean areAspectsSecured();

    protected Map<String, ?> addClassParameter(Map<String, ?> conversionParameters)
    {
        HashMap<String, Object> alfrescoConversionParameters = new HashMap<String, Object>();

        if (conversionParameters != null)
        {
            alfrescoConversionParameters.putAll(conversionParameters);
        }

        if (!alfrescoConversionParameters.containsKey(OwCMISConversionParameters.OBJECT_CLASS))
        {
            alfrescoConversionParameters.put(OwCMISConversionParameters.OBJECT_CLASS, this);
        }

        return alfrescoConversionParameters;
    }

    protected OwCMISNativePropertyClass<?, ?, PropertyDefinition<?>> getAspectsNativePropertyClass(String className, boolean secure)
    {
        try
        {
            Collection<OwCMISNativeObjectClass<?, ?>> aspectsClasses = getAspectsClasses(secure);
            OwCMISNativePropertyClass<?, ?, PropertyDefinition<?>> aspectPropertyClass = null;

            for (OwCMISNativeObjectClass<?, ?> aspectObjectClass : aspectsClasses)
            {
                aspectPropertyClass = aspectObjectClass.getNativePropertyClass(className);
                if (aspectPropertyClass != null)
                {
                    break;
                }
            }

            return aspectPropertyClass;
        }
        catch (OwException e)
        {
            LOG.error("Could not retrieve aspects.", e);
        }

        return null;
    }

    protected OwCMISPropertyClass<?> findAspectsPropertyClass(String className, boolean secure) throws OwException
    {
        Collection<OwCMISNativeObjectClass<?, ?>> aspectsClasses = getAspectsClasses(secure);

        for (OwCMISObjectClass aspectObjectClass : aspectsClasses)
        {
            OwCMISPropertyClass<?> aspectPropertyClass = aspectObjectClass.findPropertyClass(className);
            if (aspectPropertyClass != null)
            {
                return aspectPropertyClass;
            }
        }

        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Map<String, OwCMISPropertyClass<?>> getAspectsPropertyClasses() throws OwException
    {
        return (Map) getAspectsNativePropertyClasses(false);
    }

    protected Map<String, OwCMISNativePropertyClass<?, ?, ?>> getAspectsNativePropertyClasses(boolean localOnly) throws OwException
    {
        Collection<OwCMISNativeObjectClass<?, ?>> madatoryAspects = getAspectsClasses(true);
        Map<String, OwCMISNativePropertyClass<? extends Object, ?, ?>> properties = new LinkedHashMap<String, OwCMISNativePropertyClass<? extends Object, ?, ?>>();

        for (OwCMISNativeObjectClass<?, ?> aspectObjectClass : madatoryAspects)
        {
            Map<String, OwCMISNativePropertyClass<? extends Object, ?, ?>> aspectPropertyClasses = aspectObjectClass.getNativePropertyClasses(localOnly);
            properties.putAll(aspectPropertyClasses);
        }

        return properties;
    }

    private <K extends OwCMISPropertyClass<?>> Map<String, K> createUniquePropertyClasses(Map<String, K> mainPropertyClasses, Map<String, K> secondaryPropertyClasses) throws OwException
    {

        Collection<K> objectClasses = mainPropertyClasses.values();
        Set<String> uniqueCmisShortNames = new LinkedHashSet<String>();
        for (OwCMISPropertyClass<?> propertyClass : objectClasses)
        {
            if (propertyClass instanceof OwCMISNativePropertyClass)
            {
                OwCMISNativePropertyClass<?, ?, ?> nativePropertyClass = (OwCMISNativePropertyClass<?, ?, ?>) propertyClass;
                uniqueCmisShortNames.add(nativePropertyClass.getNonQualifiedName());
            }
        }
        Map<String, K> uniquePropertyClasses = new LinkedHashMap<String, K>();
        uniquePropertyClasses.putAll(mainPropertyClasses);

        Collection<Entry<String, K>> secondaryClasses = secondaryPropertyClasses.entrySet();

        for (Entry<String, K> propertyClassEntry : secondaryClasses)
        {
            K propertyClass = propertyClassEntry.getValue();

            if (propertyClass instanceof OwCMISNativePropertyClass)
            {
                OwCMISNativePropertyClass<?, ?, ?> nativePropertyClass = (OwCMISNativePropertyClass<?, ?, ?>) propertyClass;
                if (!uniqueCmisShortNames.contains(nativePropertyClass.getNonQualifiedName()))
                {
                    uniquePropertyClasses.put(propertyClassEntry.getKey(), propertyClass);
                }
            }
            else
            {
                uniquePropertyClasses.put(propertyClassEntry.getKey(), propertyClass);
            }
        }

        return uniquePropertyClasses;
    }

    private Map<String, OwCMISPropertyClass<?>> getUniquePropertyClasses() throws OwException
    {
        Map<String, OwCMISPropertyClass<?>> objectPropertyClasses = nativeObjectClass.getPropertyClasses();
        Map<String, OwCMISPropertyClass<?>> aspectPropertyClasses = getAspectsPropertyClasses();

        return createUniquePropertyClasses(objectPropertyClasses, aspectPropertyClasses);
    }

    public Map<String, OwCMISPropertyClass<?>> getPropertyClasses() throws OwException
    {
        return getUniquePropertyClasses();
    }

    public Set<OwCMISPropertyClass<?>> getQueryablePropertyClasses() throws OwException
    {
        Set<OwCMISPropertyClass<?>> queryableProperties = nativeObjectClass.getQueryablePropertyClasses();
        //filter out Aspect properties
        Map<String, OwCMISPropertyClass<?>> map = getAspectsPropertyClasses();
        if (map != null)
        {
            Iterator<OwCMISPropertyClass<?>> it = queryableProperties.iterator();
            while (it.hasNext())
            {
                OwCMISPropertyClass<?> queryProp = it.next();
                if (map.containsKey(queryProp.getClassName()))
                {
                    it.remove();
                }
            }
        }

        return queryableProperties;
    }

    public OwCMISPropertyClass<?> findPropertyClass(String className)
    {
        OwCMISQualifiedName qName = new OwCMISQualifiedName(className);

        OwCMISPropertyClass<?> propertyClass = null;

        if (qName.getNamespace() != null)
        {

            try
            {
                propertyClass = findAspectsPropertyClass(className, false);
            }
            catch (OwException e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Could not process findAspectsPropertyClass for className = " + className, e);
                }
            }

            if (propertyClass == null)
            {
                propertyClass = nativeObjectClass.findPropertyClass(className);
            }
        }
        else
        {
            propertyClass = nativeObjectClass.findPropertyClass(className);

            if (propertyClass == null)
            {
                try
                {
                    propertyClass = findAspectsPropertyClass(className, false);
                }
                catch (OwException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Could not process findAspectsPropertyClass for className = " + className, e);
                    }
                }
            }

        }

        if (propertyClass == null && !areAspectsSecured())
        {
            try
            {
                propertyClass = findAspectsPropertyClass(className, true);
            }
            catch (OwException e)
            {
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Could not process findAspectsPropertyClass for className = " + className, e);
                }
            }
        }

        return propertyClass;
    }

    protected OwCMISNativeObjectClass<T, O> getNativeObjectClass()
    {
        return nativeObjectClass;
    }

    public T getNativeObject()
    {
        return nativeObjectClass.getNativeObject();
    }

    public Map<String, OwCMISNativePropertyClass<? extends Object, ?, ?>> getNativePropertyClasses(boolean localOnly) throws OwException
    {
        Map<String, OwCMISNativePropertyClass<? extends Object, ?, ?>> objectPropertyClasses = nativeObjectClass.getNativePropertyClasses(localOnly);
        Map<String, OwCMISNativePropertyClass<?, ?, ?>> aspectPropertyClasses = getAspectsNativePropertyClasses(localOnly);

        Map<String, OwCMISNativePropertyClass<? extends Object, ?, ?>> classes = createUniquePropertyClasses(objectPropertyClasses, aspectPropertyClasses);

        List<String> preferredPropertyOrder = getSession().getNetwork().getNetworkConfiguration().getPreferedPropertyOrder();
        Map<String, OwCMISNativePropertyClass<?, ?, ?>> orderedClasses = new OwCMISPropertySorter(preferredPropertyOrder).reorderProperties(classes);

        return orderedClasses;
    }

    public OwCMISNativePropertyClass<?, ?, PropertyDefinition<?>> getNativePropertyClass(String className)
    {
        OwCMISQualifiedName qName = new OwCMISQualifiedName(className);

        OwCMISNativePropertyClass<?, ?, PropertyDefinition<?>> propertyClass = null;

        if (qName.getNamespace() != null)
        {

            propertyClass = getAspectsNativePropertyClass(className, false);

            if (propertyClass == null)
            {
                propertyClass = nativeObjectClass.getNativePropertyClass(className);
            }
        }
        else
        {
            propertyClass = nativeObjectClass.getNativePropertyClass(className);

            if (propertyClass == null)
            {
                propertyClass = getAspectsNativePropertyClass(className, false);
            }

        }

        if (propertyClass == null && areAspectsSecured())
        {
            propertyClass = getAspectsNativePropertyClass(className, true);
        }

        return propertyClass;
    }

    public OwCMISNativeObjectClass<T, O> getParent()
    {
        return nativeObjectClass.getParent();
    }

    public OwCMISNativeSession getSession()
    {
        return nativeObjectClass.getSession();
    }

    public int getType()
    {
        return nativeObjectClass.getType();
    }

    public String getMimetype()
    {
        return nativeObjectClass.getMimetype();
    }

    public Map<String, OwCMISVirtualPropertyClass<?>> getVirtualPropertyClasses(boolean localOnly_p)
    {
        return nativeObjectClass.getVirtualPropertyClasses(localOnly_p);
    }

    public OwCMISVirtualPropertyClass<?> getVirtualPropertyClass(String strClassName_p)
    {
        return nativeObjectClass.getVirtualPropertyClass(strClassName_p);
    }

    public Map<String, Object> convertToNativeProperties(OwPropertyCollection properties_p) throws OwException
    {
        OwPropertyCollection aspectsPropertyCollection = convertAspectPropertiesToNativeProperties(properties_p);
        return nativeObjectClass.convertToNativeProperties(aspectsPropertyCollection);
    }

    public List<OwCMISObjectClass> getChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException
    {
        return nativeObjectClass.getChilds(network_p, fExcludeHiddenAndNonInstantiable_p);
    }

    public Map<String, OwCMISObjectClass> getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws OwException
    {
        return nativeObjectClass.getChildNames(network_p, fExcludeHiddenAndNonInstantiable_p);
    }

    public boolean isContentRequired() throws OwException
    {
        return nativeObjectClass.isContentRequired();
    }

    public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p) throws Exception
    {
        return nativeObjectClass.hasChilds(network_p, fExcludeHiddenAndNonInstantiable_p, context_p);
    }

    public boolean isQueryable()
    {
        return nativeObjectClass.isQueryable();
    }

    public String getQueryName()
    {
        return nativeObjectClass.getQueryName();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String createNewObject(boolean promote_p, Object mode_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p, String strMimeType_p, String strMimeParameter_p,
            boolean keepCheckedOut_p) throws OwException
    {
        OwPropertyCollection aspectsPropertyCollection = convertAspectPropertiesToNativeProperties(properties_p);

        Collection<OwCMISNativeObjectClass<?, ?>> aspects = getAspectsClasses(true);

        StringBuilder alfrescoTypeId = new StringBuilder();
        alfrescoTypeId.append(getNativeObjectClass().getClassName());

        for (OwCMISNativeObjectClass<?, ?> aspect : aspects)
        {
            String aspectClassName = aspect.getClassName();
            if (alfrescoTypeId.indexOf(aspectClassName) < 0)
            {
                alfrescoTypeId.append(",");
                alfrescoTypeId.append(aspect.getClassName());
            }
        }

        OwCMISNativePropertyClass objectTypeClass = getNativePropertyClass(PropertyIds.OBJECT_TYPE_ID);

        aspectsPropertyCollection.remove(PropertyIds.OBJECT_TYPE_ID);
        aspectsPropertyCollection.remove(objectTypeClass.getFullQualifiedName().toString());

        aspectsPropertyCollection.put(PropertyIds.OBJECT_TYPE_ID, objectTypeClass.from(alfrescoTypeId.toString()));

        return nativeObjectClass.createNewObject(promote_p, mode_p, aspectsPropertyCollection, permissions_p, content_p, parent_p, strMimeType_p, strMimeParameter_p, keepCheckedOut_p);
    }

    @SuppressWarnings("unchecked")
    private OwPropertyCollection convertAspectPropertiesToNativeProperties(OwPropertyCollection properties_p) throws OwInvalidOperationException, OwException
    {
        OwPropertyCollection aspectsPropertyCollection = new OwStandardPropertyCollection();
        Set<Entry<String, OwProperty>> entries = properties_p.entrySet();
        for (Entry<String, OwProperty> entry : entries)
        {
            OwProperty property = entry.getValue();
            aspectsPropertyCollection.put(entry.getKey(), convertAspectPropertyToNativeProperty(property));

        }

        return aspectsPropertyCollection;
    }

    @SuppressWarnings("rawtypes")
    private OwProperty convertAspectPropertyToNativeProperty(OwProperty property) throws OwException, OwInvalidOperationException
    {
        OwProperty convertedProperty = null;
        if (property instanceof OwCMISNativeProperty<?, ?>)
        {
            convertedProperty = property;
        }
        else if (property instanceof OwCMISBoundVirtualProperty<?>)
        {
            OwCMISBoundVirtualProperty<?> boundProperty = (OwCMISBoundVirtualProperty<?>) property;
            OwCMISProperty<?> innerProperty = boundProperty.getBoundProperty();
            if (innerProperty instanceof OwCMISBoundVirtualProperty<?>)
            {
                throw new OwInvalidOperationException("This is way too much for me to handle.");
            }
            return convertAspectPropertyToNativeProperty(innerProperty);
        }
        else
        {
            OwPropertyClass propertyClass = null;
            try
            {
                propertyClass = property.getPropertyClass();

                OwCMISNativePropertyClass nativePropertyClass = this.getAspectsNativePropertyClass(propertyClass.getClassName(), true);
                convertedProperty = convertToNativeProperty(property, nativePropertyClass);
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

        return convertedProperty;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private OwProperty convertToNativeProperty(OwProperty currentProperty, OwCMISNativePropertyClass nativePropDef) throws OwException
    {
        try
        {
            if (null != nativePropDef)
            {
                return nativePropDef.from(currentProperty.getValue());
            }
            else
            {
                return currentProperty;
            }
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not convert property, to PropertyClass = " + (nativePropDef != null ? nativePropDef.getClassName() : "null"), e);
        }
    }

    public String getClassName()
    {
        return nativeObjectClass.getClassName();
    }

    public String getDisplayName(Locale locale_p)
    {
        return nativeObjectClass.getDisplayName(locale_p);
    }

    public String getNamePropertyName() throws OwException
    {
        return nativeObjectClass.getNamePropertyName();
    }

    public void subclassedBy(OwCMISObjectClass subclass_p) throws OwInvalidOperationException
    {
        nativeObjectClass.subclassedBy(subclass_p);
    }

    public boolean isAssignableFrom(OwCMISObjectClass class_p) throws OwException
    {
        return nativeObjectClass.isAssignableFrom(class_p);
    }

    public boolean hasVersionSeries() throws Exception
    {
        return nativeObjectClass.hasVersionSeries();
    }

    public List<?> getModes(int operation_p) throws Exception
    {
        return nativeObjectClass.getModes(operation_p);
    }

    public boolean canCreateNewObject() throws OwException
    {
        return nativeObjectClass.canCreateNewObject();
    }

    public String getDescription(Locale locale_p)
    {
        return nativeObjectClass.getDescription(locale_p);
    }

    public boolean isHidden() throws Exception
    {
        return nativeObjectClass.isHidden();
    }

    @Override
    public <N extends TransientCmisObject> OwCMISTransientObject<N> newTransientObject(N cmisObject, OperationContext creationContext)
    {
        return new OwCMISAlfrescoBulkTransientObject<N>(cmisObject, creationContext, getSession().getOpenCMISSession());
    }

    @Override
    public OwCMISQueryResultConverter<O> getQueryResultConverter(QueryResult queryResult, OwQueryStatement statement, OperationContext context) throws OwException
    {
        return new OwCMISAlfrescoQueryResultConverterImpl<O>(getSession().getOpenCMISSession());
    }

    @Override
    public PropertyType getPreferredPropertyType(OwCMISPropertyClass<?> propertyClass) throws OwException
    {
        return nativeObjectClass.getPreferredPropertyType(propertyClass);
    }

    @SuppressWarnings({ "unchecked" })
    public OwObjectSkeleton createSkeletonObject(OwNetwork network_p, OwCMISResource res_p, OwCMISNativeSession ses_p, OwXMLUtil initValues_p) throws Exception
    {
        return new OwCMISRepositoryObjectSkeleton(network_p, new OwCMISAlfrescoSkeletonObjectClass((OwCMISAbstractAlfrescoClass<ObjectType, TransientCmisObject>) this), res_p, ses_p, initValues_p);
    }

    /**
     * Helper for handling OpenCMIS OperationContext.
     * @param conversionParameters Map<String, ?> conversion parameter (internal transformation)
     * @return OperationContext
     */
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
    public String getCopyClassName()
    {
        StringBuilder copyClassName = new StringBuilder(getClassName());
        Collection<OwCMISNativeObjectClass<?, ?>> aspectTypes = null;
        try
        {
            aspectTypes = getAspectsClasses(true);
        }
        catch (OwException e)
        {
            throw new OwCMISRuntimeException("Cannot get Aspects information for copy process", e);
        }
        if (aspectTypes != null)
        {
            for (OwCMISNativeObjectClass<?, ?> type : aspectTypes)
            {
                copyClassName.append(",");
                copyClassName.append(type.getClassName());
            }
        }
        return copyClassName.toString();
    }
}
