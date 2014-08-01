package org.alfresco.wd.util.loader;

/**
 *<p>
 * Implementation of OwLoader which create an instance only once.
 * Creates a specific type on call of {@link #load()} and will keep it reference for further calls.
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
public class OwSingeltonLoader<T> implements OwLoader<T>
{
    private OwLoader<T> loader;
    private T loadedInstance;

    public OwSingeltonLoader(OwLoader<T> loader)
    {
        this.loader = loader;
    }

    @Override
    public T load()
    {
        if (loadedInstance == null)
        {
            this.loadedInstance = loader.load();
        }
        return loadedInstance;
    }

}
