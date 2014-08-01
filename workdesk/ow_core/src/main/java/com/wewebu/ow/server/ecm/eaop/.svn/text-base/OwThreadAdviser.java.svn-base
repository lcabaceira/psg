package com.wewebu.ow.server.ecm.eaop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 * A thread localized advice collection.
 *    
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 *@since 4.0.0.0
 */
public class OwThreadAdviser implements OwAdviser
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwThreadAdviser.class);

    private static ThreadLocal<OwThreadAdviser> localAdviser = new ThreadLocal<OwThreadAdviser>();

    public static OwThreadAdviser currentAdviser()
    {
        OwThreadAdviser adviser = localAdviser.get();
        if (adviser == null)
        {
            adviser = new OwThreadAdviser();
            localAdviser.set(adviser);
        }

        return adviser;
    }

    private Map<Class, List<Object>> advices = new HashMap<Class, List<Object>>();

    private OwThreadAdviser()
    {
        //void
    }

    private Set<Class<?>> findAdvices(Class<?> class_p) throws OwInvalidOperationException
    {
        HashSet<Class<?>> advisers = new HashSet<Class<?>>();
        findAdvices(class_p, advisers);

        return advisers;
    }

    private void findAdvices(Class<?> class_p, Set<Class<?>> advices_p) throws OwInvalidOperationException
    {
        OwAdvice advice = class_p.getAnnotation(OwAdvice.class);
        if (advice != null)
        {
            if (class_p.isInterface())
            {
                Method[] methods = class_p.getMethods();
                advices_p.add(class_p);
            }
            else
            {
                String msg = "Invalid advice " + class_p.toString() + ". Only interface advices are allowed.";
                LOG.error(msg);

                throw new OwInvalidOperationException(msg);
            }
        }

        Class<?> superClass = class_p.getSuperclass();
        if (superClass != null)
        {
            findAdvices(superClass, advices_p);
        }

        Class[] interfaces = class_p.getInterfaces();

        for (int i = 0; i < interfaces.length; i++)
        {
            findAdvices(interfaces[i], advices_p);
        }
    }

    public synchronized void add(Object... advices_p) throws OwInvalidOperationException
    {
        for (Object advice : advices_p)
        {
            Class<? extends Object> clazz = advice.getClass();
            Set<Class<?>> classAdvices = findAdvices(clazz);
            for (Class<?> adviceClass : classAdvices)
            {
                List<Object> adviceList = advices.get(adviceClass);
                if (adviceList == null)
                {
                    adviceList = new LinkedList<Object>();
                    advices.put(adviceClass, adviceList);
                }

                adviceList.add(advice);
            }
        }
    }

    public synchronized void remove(Object... advices_p) throws OwInvalidOperationException
    {
        for (Object advice : advices_p)
        {
            Class<? extends Object> clazz = advice.getClass();
            Set<Class<?>> classAdvices = findAdvices(clazz);
            for (Class<?> adviceClass : classAdvices)
            {
                List<Object> adviceList = advices.get(adviceClass);
                if (adviceList != null)
                {
                    adviceList.remove(advice);
                }

            }
        }
    }

    public synchronized <A> List<A> get(Class<A> advicerClass_p) throws OwInvalidOperationException
    {
        return (List<A>) advices.get(advicerClass_p);
    }

}
