package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Resource;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Default context dependent {@link OwFNCM5Object} factory.<br/>
 * Delegates creation to standard {@link Class} reflective constructor ( {@link Constructor#newInstance(Object...)}.    
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
public class OwFNCM5DefaultObjectFactory implements OwFNCM5ObjectFactory
{
    public static final OwFNCM5DefaultObjectFactory INSTANCE = new OwFNCM5DefaultObjectFactory();

    public <N, O extends OwFNCM5Object<N>, R extends OwFNCM5Resource> O create(Class<O> objectClass_p, Class<?>[] parameterTypes_p, Object[] parameters_p) throws OwException
    {
        try
        {
            Class<O> clazz = objectClass_p;
            Object versionedObject = null;
            Constructor<? extends OwFNCM5Object> constructor = clazz.getConstructor(parameterTypes_p);
            O instance = (O) constructor.newInstance(parameters_p);

            return instance;
        }
        catch (SecurityException e)
        {
            throw new OwInvalidOperationException("Invalid create-object call.", e);
        }
        catch (NoSuchMethodException e)
        {
            throw new OwInvalidOperationException("No such create-object call.", e);

        }
        catch (IllegalArgumentException e)
        {
            throw new OwInvalidOperationException("Invalid argument in create-object call.", e);
        }
        catch (InstantiationException e)
        {
            throw new OwInvalidOperationException("Object instantiation exception.", e);
        }
        catch (IllegalAccessException e)
        {
            throw new OwInvalidOperationException("Illegal access in create-object call.", e);
        }
        catch (InvocationTargetException e)
        {
            throw new OwInvalidOperationException("Java constructor invocation exception in create-object call.", e);
        }
    }

}
