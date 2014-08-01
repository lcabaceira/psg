package com.wewebu.ow.server.ecm.eaop;

import java.lang.reflect.Method;

import com.wewebu.ow.server.exceptions.OwException;

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
public interface OwJoinPointResultCollector
{
    boolean canCollect(Method method_p) throws OwException;

    void collect(Method method_p, Object advice_p, Object[] args_p, Object result_p) throws OwException;

    Object getResult();
}
