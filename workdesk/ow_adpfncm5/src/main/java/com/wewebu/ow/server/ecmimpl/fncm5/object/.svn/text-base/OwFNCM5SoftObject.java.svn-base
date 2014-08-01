package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.dmsid.OwFNCM5DMSID;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwToP8Type;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * OwFNCM5SoftObject.
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
public final class OwFNCM5SoftObject<N> extends OwFNCM5DelegateObject<N> implements InvocationHandler
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5SoftObject.class);

    private SoftReference<OwFNCM5Object<N>> softObject;
    private String dmsid;
    private OwToP8Type type;
    private OwFNCM5Network network;

    private static Map<Class<?>, Class<?>[]> interfaces = new HashMap<Class<?>, Class<?>[]>();

    private static synchronized Class[] allInterfacesOf(Class<?> class_p)
    {
        Class<?>[] classInterfaces = interfaces.get(class_p);
        if (classInterfaces == null)
        {
            List<Class<?>> interfacesList = new ArrayList<Class<?>>();
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

    public static final <N> OwFNCM5Object<N> asSoftObject(OwFNCM5Object<N> object_p) throws OwException
    {
        if (object_p != null)
        {
            OwFNCM5SoftObject<N> softHandler = new OwFNCM5SoftObject<N>(object_p);
            Class<? extends OwFNCM5Object> javaClass = object_p.getClass();

            Class[] interfaces = allInterfacesOf(javaClass);
            return (OwFNCM5Object<N>) Proxy.newProxyInstance(javaClass.getClassLoader(), interfaces, softHandler);
        }
        else
        {
            return null;
        }
    }

    private OwFNCM5SoftObject(OwFNCM5Object<N> object_p) throws OwException
    {
        this.softObject = new SoftReference<OwFNCM5Object<N>>(object_p);
        try
        {
            this.dmsid = object_p.getDMSID();
            this.type = OwToP8Type.getFromType(object_p.getType());
            this.network = object_p.getNetwork();
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not create soft object!", e);
        }
    }

    protected OwFNCM5Object<N> get()
    {
        OwFNCM5Object<N> object = softObject.get();
        if (object == null)
        {
            try
            {
                object = type != null ? concreteRestore() : restore();
            }
            catch (Exception e)
            {
                LOG.error("Could not restore soft object!", e);
                throw new RuntimeException(e);
            }
            softObject = new SoftReference<OwFNCM5Object<N>>(object);
        }
        return object;
    }

    private OwFNCM5Object<N> restore() throws Exception
    {
        return (OwFNCM5Object<N>) this.network.getObjectFromDMSID(this.dmsid, true);
    }

    private OwFNCM5Object<N> concreteRestore() throws Exception
    {
        OwFNCM5DMSID dId = this.network.getDmsIdDeconder().getDmsidObject(this.dmsid);
        OwFNCM5ObjectStore os = (this.network.getResource(dId.getResourceID())).getObjectStore();
        return (OwFNCM5Object<N>) os.getObjectFromId(dId.getObjectID(), type.getP8Type());
    }

    public N getNativeObject() throws OwException
    {
        return get().getNativeObject();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        OwFNCM5Object<?> delegate = get();
        try
        {
            return method.invoke(delegate, args);
        }
        catch (InvocationTargetException e)
        {
            throw e.getTargetException();
        }
    }
}
