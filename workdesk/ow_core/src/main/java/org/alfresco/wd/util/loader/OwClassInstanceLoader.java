package org.alfresco.wd.util.loader;

import com.wewebu.ow.server.exceptions.OwRuntimeException;

/**
 *<p>
 * Loader based on Class.newInstance handling.
 * Simple loader which will use reflection to initialize the corresponding class,
 * using the default constructor.
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
 *@since 4.2.0.0
 */
public class OwClassInstanceLoader<T, O extends T> implements OwLoader<T>
{
    private Class<O> type;

    public OwClassInstanceLoader(Class<O> type)
    {
        this.type = type;
    }

    @Override
    public T load()
    {
        try
        {
            return type.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new OwClassInstanceLoaderException(e.getMessage(), e);
        }
        catch (IllegalAccessException e)
        {
            throw new OwClassInstanceLoaderException(e.getMessage(), e);
        }
    }

    private static class OwClassInstanceLoaderException extends OwRuntimeException
    {
        /** */
        private static final long serialVersionUID = 8787642640104197533L;

        public OwClassInstanceLoaderException(String message_p, Throwable cause_p)
        {
            super(message_p, cause_p);
        }

        public String getModulName()
        {
            return "owd.core";
        }
    }
}
