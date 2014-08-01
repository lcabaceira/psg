package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPropertyClass;

import filenet.vw.api.VWExposedFieldDefinition;

/**
 *<p>
 * Abstraction class for native queues.
 * A new abstraction class to reduce duplicated code 
 * for initialization and filter properties loading.
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
public abstract class OwFNBPM5NativeContainer extends OwFNBPM5BaseContainer
{
    private LinkedList<String> filterProperties;

    public OwFNBPM5NativeContainer(OwFNBPM5Repository repository_p) throws Exception
    {
        super(repository_p);
    }

    @Override
    protected boolean isUserContainer()
    {
        return getType() == OwObjectReference.OBJECT_TYPE_USER_QUEUE_FOLDER;
    }

    /**
     * Helper to initialize the current queue/container filter properties
     * and shown properties/columns. 
     * @param dataFields_p
     * @throws Exception
     */
    protected void initFields(VWExposedFieldDefinition[] dataFields_p) throws Exception
    {
        filterProperties = new LinkedList<String>();
        for (int i = 0; i < dataFields_p.length; i++)
        {
            filterProperties.add(dataFields_p[i].getName());
            getChildObjectClass().addPropertyClass(dataFields_p[i]);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        LinkedList<OwPropertyClass> propCol = new LinkedList<OwPropertyClass>();
        if (propertynames_p != null)
        {
            Iterator<?> it = propertynames_p.iterator();
            while (it.hasNext())
            {
                String name = it.next().toString();
                if (filterProperties.contains(name))
                {
                    propCol.add(getChildObjectClass().getPropertyClass(name));
                }
            }
        }
        else
        {
            Iterator<String> it = filterProperties.iterator();
            while (it.hasNext())
            {
                propCol.add(getChildObjectClass().getPropertyClass(it.next()));
            }
        }
        return propCol;
    }

    /**
     * Getter for the collection containing names
     * of filter properties.
     * @return List of strings
     */
    protected List<String> getFilterCollection()
    {
        return this.filterProperties;
    }
}