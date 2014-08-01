package com.wewebu.ow.server.ecm.eaop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 * A join point defines when an advice must run .
 * As many advice interfaces can be matched to specific join point their returned 
 * results are merged by a {@link OwJoinPointResultCollector}. 
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
public class OwJoinPoint implements InvocationHandler
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwJoinPoint.class);

    private static boolean areCompatible(Class<?> adviceClass_p, OwJoinPointResultCollector resultColector_p) throws OwException
    {
        Method[] methods = adviceClass_p.getMethods();
        for (Method method : methods)
        {
            Class<?> declaringClass = method.getDeclaringClass();
            if (declaringClass.getAnnotation(OwAdvice.class) != null)
            {
                if (!resultColector_p.canCollect(method))
                {
                    LOG.debug("Incompatible method " + method + " for result collector " + resultColector_p);
                    return false;
                }
            }
        }

        return true;
    }

    public static <A> A joinPoint(Class<A> adviceClass_p, OwJoinPointResultCollector resultColector_p) throws OwException
    {
        if (!areCompatible(adviceClass_p, resultColector_p))
        {
            throw new OwInvalidOperationException("Incompatible collector" + resultColector_p + " for advice " + adviceClass_p + ".");
        }

        OwThreadAdviser adviser = OwThreadAdviser.currentAdviser();

        List<A> advices = adviser.get(adviceClass_p);
        advices = (advices == null) ? Collections.EMPTY_LIST : advices;

        OwJoinPoint point = new OwJoinPoint(advices, resultColector_p);
        return (A) Proxy.newProxyInstance(OwJoinPoint.class.getClassLoader(), new Class<?>[] { adviceClass_p }, point);
    }

    private List<?> advices;
    private OwJoinPointResultCollector resultCollector;

    private OwJoinPoint(List<?> advices_p, OwJoinPointResultCollector resultCollector_p)
    {
        super();
        this.advices = advices_p;
        this.resultCollector = resultCollector_p;
    }

    public Object invoke(Object proxy_p, Method method_p, Object[] args_p) throws Throwable
    {
        for (Object advice : advices)
        {
            Object result = method_p.invoke(advice, args_p);
            resultCollector.collect(method_p, advice, args_p, result);
        }

        return resultCollector.getResult();
    }

}
