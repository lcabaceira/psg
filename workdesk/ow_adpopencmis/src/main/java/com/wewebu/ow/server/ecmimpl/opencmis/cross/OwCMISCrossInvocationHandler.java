package com.wewebu.ow.server.ecmimpl.opencmis.cross;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwCrossMappings;
import com.wewebu.ow.server.ecm.OwExtendedCrossMappings;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Network and property mapping {@link Proxy} based handler.
 * In cross network scenario object , object classes and property with their 
 * classes must select/replace network parameters and perform property mapping at call time.
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
 *@since 4.1.1.0
 */
public class OwCMISCrossInvocationHandler implements InvocationHandler
{
    private static final Logger LOG = OwLog.getLogger(OwCMISCrossInvocationHandler.class);

    private static Method GETPROPETYCLASS;
    private static Method GETPROPETY;
    private static Method GETPROPETIES;
    private static Method CLASS_GETCLASSNAME;
    private static Method OBJECT_GETCLASSNAME;
    private static Method NETWORK_CREATEOBJECTCOPY;
    private static Method CLASS_GETCHILDS;
    private static Method OBJECT_GETCHILDS;

    private static Method CLASS_GETPROPERTYCLASSNAMES;

    private static Method PROPERTYCLASS_GETCLASSNAME;

    private static Method NETWORK_CREATENEWOBJECT_1;

    private static Method NETWORK_CREATENEWOBJECT_2;

    static
    {
        try
        {
            GETPROPETYCLASS = OwObjectClass.class.getMethod("getPropertyClass", new Class[] { String.class });
            CLASS_GETCLASSNAME = OwObjectClass.class.getMethod("getClassName", new Class[] {});
            OBJECT_GETCLASSNAME = OwObject.class.getMethod("getClassName", new Class[] {});

            GETPROPETY = OwObject.class.getMethod("getProperty", new Class[] { String.class });

            GETPROPETIES = OwObject.class.getMethod("getProperties", new Class[] { Collection.class });

            CLASS_GETCHILDS = OwObjectClass.class.getMethod("getChilds", new Class[] { OwNetwork.class, Boolean.TYPE });

            CLASS_GETPROPERTYCLASSNAMES = OwObjectClass.class.getMethod("getPropertyClassNames", new Class[] {});

            PROPERTYCLASS_GETCLASSNAME = OwPropertyClass.class.getMethod("getClassName", new Class[] {});

            OBJECT_GETCHILDS = OwObject.class.getMethod("getChilds", new Class[] { int[].class, Collection.class, OwSort.class, int.class, int.class, OwSearchNode.class });

            NETWORK_CREATEOBJECTCOPY = OwNetwork.class.getMethod("createObjectCopy", new Class[] { OwObject.class, OwPropertyCollection.class, OwPermissionCollection.class, OwObject.class, int[].class });

            NETWORK_CREATENEWOBJECT_1 = OwNetwork.class.getMethod("createNewObject", new Class[] { Boolean.TYPE, Object.class, OwResource.class, String.class, OwPropertyCollection.class, OwPermissionCollection.class, OwContentCollection.class,
                    OwObject.class, String.class, String.class });

            NETWORK_CREATENEWOBJECT_2 = OwNetwork.class.getMethod("createNewObject", new Class[] { OwResource.class, String.class, OwPropertyCollection.class, OwPermissionCollection.class, OwContentCollection.class, OwObject.class, String.class,
                    String.class });
        }
        catch (Exception e)
        {
            LOG.error("Inavlid java code base.", e);
        }
    }

    private static final Map<Class<?>, Class<?>[]> interfaces = new HashMap<Class<?>, Class<?>[]>();

    private static synchronized Class[] allInterfacesOf(Class<?> class_p)
    {
        Class<?>[] classInterfaces = interfaces.get(class_p);
        if (classInterfaces == null)
        {
            Set<Class<?>> interfacesList = new HashSet<Class<?>>();
            Class<?> current = class_p;

            while (current != null)
            {
                Class[] currentInterfaces = current.getInterfaces();
                for (int i = 0; i < currentInterfaces.length; i++)
                {
                    interfacesList.add(currentInterfaces[i]);
                }

                current = current.getSuperclass();
            }
            classInterfaces = interfacesList.toArray(new Class<?>[interfacesList.size()]);
            interfaces.put(class_p, classInterfaces);
        }
        return classInterfaces;
    }

    /**
     *<p>
     * Cross Call Interface
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
    public static interface CrossCall
    {
        Object call() throws Exception;
    }

    public static Object call(CrossCall call, String callClientResourceID) throws Exception
    {
        String stackedResource = clientResourceID.get();
        try
        {
            clientResourceID.set(callClientResourceID);
            return call.call();
        }
        finally
        {
            clientResourceID.set(stackedResource);
        }
    }

    public static <O> O createCrossNetworkObject(OwCMISCrossNetwork network_p, OwCrossMappings mappings_p, O object_p, String serverResourceID_p)
    {
        Class<? extends Object> clazz = object_p.getClass();
        Class[] interfaces = allInterfacesOf(clazz);
        return (O) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new OwCMISCrossInvocationHandler(network_p, mappings_p, object_p, serverResourceID_p));
    }

    private OwCMISCrossNetwork crossNetwork;
    private OwCrossMappings mappings;
    private Object object;
    private static ThreadLocal<String> clientResourceID = new ThreadLocal<String>();
    private String serverResourceID;

    public OwCMISCrossInvocationHandler(OwCMISCrossNetwork crossNetwork_p, OwCrossMappings mappings_p, Object object_p, String serverResourceID_p)
    {
        super();
        this.crossNetwork = crossNetwork_p;
        this.mappings = mappings_p;
        this.object = object_p;
        this.serverResourceID = serverResourceID_p;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        Object[] crossArgs = toCrossArguments(method, args);

        if (crossArgs != null)
        {
            for (int i = 0; i < crossArgs.length; i++)
            {
                if (crossArgs[i] instanceof OwNetwork)
                {
                    crossArgs[i] = crossNetwork.getNetwork(serverResourceID);
                }
                if (crossArgs[i] instanceof OwObject)
                {
                    OwObject objectArgument = (OwObject) crossArgs[i];
                    crossArgs[i] = mirrorObject(objectArgument);
                }
            }
        }

        String stackedResource = clientResourceID.get();
        try
        {

            clientResourceID.set(serverResourceID);
            Object returnValue = null;
            if (method.getName().equals("equals") && (1 == crossArgs.length))
            {
                returnValue = callEquals(crossArgs[0]);
            }
            else
            {
                returnValue = method.invoke(this.object, crossArgs);
            }
            clientResourceID.set(stackedResource);

            //TODO : getXClass extraction
            if (crossConversionRequired())
            {
                if ((overrides(CLASS_GETCLASSNAME, method) || overrides(OBJECT_GETCLASSNAME, method)))
                {
                    if (isExternal(clientResourceID.get()))
                    {
                        return ((OwExtendedCrossMappings) mappings).getXClass((String) returnValue);
                    }
                    else
                    {
                        return ((OwExtendedCrossMappings) mappings).getIClass((String) returnValue);
                    }
                }
                //                else if (overrides(CLASS_GETPROPERTYCLASSNAMES, method))
                //                {
                //                    return toCrossPropertyNames((Collection<String>) returnValue, serverResourceID, new HashSet<String>());
                //                }
                //                else if (overrides(PROPERTYCLASS_GETCLASSNAME, method))
                //                {
                //                    return toCrossPropertyName((String) returnValue, serverResourceID);
                //                }
                else
                {
                    return toCrossReturnValue(returnValue);
                }
            }
            else if (overrides(CLASS_GETCHILDS, method))
            {
                return crossConvertReturnValue(returnValue);
            }
            else
            {
                return returnValue;
            }
        }
        catch (InvocationTargetException e)
        {
            //            LOG.debug("Cross Invokation "+e.getCause().getMessage(),e);
            throw e.getCause();
        }
        finally
        {
            clientResourceID.set(stackedResource);
        }
    }

    private boolean overrides(Method overriden_p, Method method_p) throws OwException
    {
        if (overriden_p == null || method_p == null)
        {
            throw new OwInvalidOperationException("null method comparison " + (overriden_p == null ? "null" : overriden_p.toString()) + " vs. " + (method_p == null ? "null" : method_p.toString()));
        }

        if (overriden_p.getDeclaringClass().isAssignableFrom(method_p.getDeclaringClass()))
        {
            if (overriden_p.getName().equals(method_p.getName()))
            {
                if (Arrays.equals(overriden_p.getParameterTypes(), method_p.getParameterTypes()))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private OwObject mirrorObject(OwObject object_p) throws OwInvalidOperationException
    {
        String resourceID = null;

        try
        {
            if (object_p.getResource() != null)
            {
                resourceID = object_p.getResourceID();
            }
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Invalidobject argument", e);
        }
        if ((resourceID == null && serverResourceID != null) || (resourceID != null && !resourceID.equals(serverResourceID)))
        {
            return createCrossNetworkObject(crossNetwork, mappings, object_p, resourceID);
        }
        else
        {
            return object_p;
        }
    }

    private boolean crossConversionRequired() throws Exception
    {
        if (clientResourceID.get() == null)
        {

            return serverResourceID != null && !this.crossNetwork.getResource(clientResourceID.get()).getID().equals(serverResourceID);
        }
        else
        {
            return !clientResourceID.get().equals(serverResourceID);
        }
    }

    private boolean isExternal(String repositoryID_p) throws OwException
    {
        return crossNetwork.getNetwork(repositoryID_p) == crossNetwork.getExternalNetwork();
    }

    private Object[] toCrossArguments(Method method_p, Object[] args_p) throws Exception
    {
        if (crossConversionRequired())
        {
            return crossConvertMehtodArguments(method_p, args_p);
        }
        else
        {
            return args_p;
        }
    }

    private String toCrossPropertyName(String propertyName_p, String fromRepositoryID_p) throws OwException
    {
        if (isExternal(fromRepositoryID_p))
        {
            return ((OwExtendedCrossMappings) this.mappings).getIProperty(propertyName_p);
        }
        else
        {
            return ((OwExtendedCrossMappings) this.mappings).getXProperty(propertyName_p);
        }
    }

    private Collection<String> toCrossPropertyNames(Collection propertyNames_p, String fromResourceID_p) throws OwException
    {
        return toCrossPropertyNames(propertyNames_p, fromResourceID_p, new LinkedList<String>());
    }

    private Collection<String> toCrossPropertyNames(Collection propertyNames_p, String fromResourceID_p, Collection<String> crosssNames_p) throws OwException
    {
        if (propertyNames_p != null)
        {
            Iterator i = propertyNames_p.iterator();
            while (i.hasNext())
            {
                String name = (String) i.next();
                crosssNames_p.add(toCrossPropertyName(name, fromResourceID_p));
            }
            return crosssNames_p;
        }
        else
        {
            return null;
        }
    }

    private Object[] crossConvertMehtodArguments(Method method_p, Object[] args_p) throws Exception
    {
        Object[] crossArgs = null;
        if (args_p != null)
        {
            crossArgs = new Object[args_p.length];
            System.arraycopy(args_p, 0, crossArgs, 0, args_p.length);

            if (overrides(GETPROPETYCLASS, method_p) || overrides(GETPROPETY, method_p))
            {
                crossArgs[0] = toCrossPropertyName((String) crossArgs[0], clientResourceID.get());

            }
            else if (overrides(GETPROPETIES, method_p))
            {
                crossArgs[0] = toCrossPropertyNames((Collection) crossArgs[0], clientResourceID.get());
            }
            else if (overrides(OBJECT_GETCHILDS, method_p))
            {
                crossArgs[1] = toCrossPropertyNames((Collection) crossArgs[1], clientResourceID.get());
            }
            else if (overrides(NETWORK_CREATEOBJECTCOPY, method_p))
            {
                crossArgs[1] = toCrossProperties((OwPropertyCollection) crossArgs[1], clientResourceID.get(), serverResourceID);
            }
            else if (overrides(NETWORK_CREATENEWOBJECT_1, method_p))
            {
                crossArgs[3] = toCrossClassName((String) crossArgs[3], clientResourceID.get());
            }
            else if (overrides(NETWORK_CREATENEWOBJECT_2, method_p))
            {
                crossArgs[1] = toCrossClassName((String) crossArgs[1], clientResourceID.get());
            }

        }

        return crossArgs;
    }

    private Object toCrossReturnValue(Object object_p) throws Exception
    {
        if (crossConversionRequired())
        {
            return crossConvertReturnValue(object_p);
        }
        else
        {
            return object_p;
        }
    }

    private Object crossConvertReturnValue(Object object_p) throws Exception
    {
        if (object_p == null)
        {
            return null;
        }

        if (object_p instanceof OwObject || object_p instanceof OwFieldDefinition || object_p instanceof OwObjectClass || object_p instanceof OwPropertyClass || object_p instanceof OwProperty)
        {
            return createCrossNetworkObject(this.crossNetwork, this.mappings, object_p, this.serverResourceID);
        }
        else if (object_p instanceof List)
        {
            List list = (List) object_p;
            int listSize = list.size();
            for (int i = 0; i < listSize; i++)
            {
                Object iObject = list.get(i);
                Object iCrossObject = crossConvertReturnValue(iObject);
                if (iObject != iCrossObject)
                {
                    list.remove(i);
                    list.add(i, iCrossObject);
                }
            }
            return list;
        }
        else if (object_p instanceof Collection)
        {
            Collection collection = (Collection) object_p;
            Iterator i = collection.iterator();
            List toBeCrossed = new ArrayList();
            while (i.hasNext())
            {
                Object iObject = i.next();
                Object iCrossObject = crossConvertReturnValue(iObject);
                if (iObject != iCrossObject)
                {
                    i.remove();
                    toBeCrossed.add(iCrossObject);
                }
            }
            for (Object crossObject : toBeCrossed)
            {
                collection.add(crossObject);
            }
            return collection;
        }
        else if (object_p instanceof OwPropertyCollection)
        {
            OwPropertyCollection propertyCollection = (OwPropertyCollection) object_p;
            OwPropertyCollection interceptedCollection = new OwStandardPropertyCollection();

            Set<Entry<String, OwProperty>> propertyEntries = propertyCollection.entrySet();
            for (Entry<String, OwProperty> propEntry : propertyEntries)
            {
                OwProperty property = propEntry.getValue();

                try
                {
                    OwProperty crossProperty = toCrossProperty(property, serverResourceID, clientResourceID.get());

                    interceptedCollection.put(crossProperty.getPropertyClass().getClassName(), crossProperty);
                }
                catch (OwObjectNotFoundException e)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Invalid property cross-conversion for " + propEntry.getKey() + ". ", e);
                    }
                    else
                    {
                        LOG.warn("Invalid property cross-conversion for " + propEntry.getKey() + ". " + e.getMessage());
                    }
                }

            }

            return interceptedCollection;

        }
        else
        {
            return object_p;
        }
    }

    private OwPropertyCollection toCrossProperties(OwPropertyCollection properties, String fromRepositoryID, String toResourceID) throws Exception
    {
        if (properties != null)
        {
            Set<Entry<String, OwProperty>> propertyEntries = properties.entrySet();
            OwPropertyCollection crossPropertyCollection = new OwStandardPropertyCollection();

            for (Entry<String, OwProperty> entry : propertyEntries)
            {
                OwProperty crossProperty = toCrossProperty(entry.getValue(), fromRepositoryID, toResourceID);
                crossPropertyCollection.put(crossProperty.getPropertyClass().getClassName(), crossProperty);
            }

            return crossPropertyCollection;
        }
        else
        {
            return null;
        }
    }

    private OwProperty toCrossProperty(OwProperty property, String fromRepositoryID, String toResourceID) throws Exception
    {
        OwPropertyClass propertyClass = property.getPropertyClass();
        String propertyClassName = propertyClass.getClassName();
        String crossPropertyClassName = toCrossPropertyName(propertyClassName, fromRepositoryID);

        //TODO : avoid OwObject RTTI
        //        String crossClassName = toCrossClass(((OwObject) object).getClassName(), fromRepositoryID);

        OwNetwork clientNetwrok = crossNetwork.getNetwork(toResourceID);
        OwResource clientResource = crossNetwork.getResource(toResourceID);

        //        OwObjectClass crossClass =
        //TODO: avoid breaking contract of get Field ...
        OwPropertyClass crossPropertyClass = (OwPropertyClass) clientNetwrok.getFieldDefinition(crossPropertyClassName, clientResource.getID());

        return convert(property, crossPropertyClass);
    }

    private String toCrossClassName(String className, String fromRepositoryID) throws OwException
    {
        if (isExternal(fromRepositoryID))
        {
            return ((OwExtendedCrossMappings) this.mappings).getIClass(className);
        }
        else
        {
            return ((OwExtendedCrossMappings) this.mappings).getXClass(className);
        }
    }

    private OwProperty convert(OwProperty property, OwPropertyClass toClass) throws Exception
    {
        Object convertedValue = this.mappings.convert(property.getValue(), property.getPropertyClass(), toClass);
        return new OwStandardProperty(convertedValue, toClass);
    }

    private boolean callEquals(Object other)
    {
        Object naked = other;
        while (Proxy.isProxyClass(naked.getClass()) && Proxy.getInvocationHandler(naked).getClass() == OwCMISCrossInvocationHandler.class)
        {
            naked = ((OwCMISCrossInvocationHandler) Proxy.getInvocationHandler(naked)).object;
        }
        return this.object.equals(naked);
    }
}
