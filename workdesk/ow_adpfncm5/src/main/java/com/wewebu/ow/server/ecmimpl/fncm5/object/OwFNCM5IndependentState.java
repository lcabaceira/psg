package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.LinkedList;
import java.util.List;

import com.filenet.api.core.IndependentObject;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5EngineObjectClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5IndependentState.
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
public class OwFNCM5IndependentState<I extends IndependentObject> extends OwFNCM5EngineState<I>
{

    public OwFNCM5IndependentState(I engineObject_p, OwFNCM5EngineObjectClass<?, ?> engineObjectClass_p)
    {
        super(engineObject_p, engineObjectClass_p);
    }

    @Override
    public synchronized final I refresh() throws OwException
    {
        I independentObject = getEngineObject();
        independentObject.refresh();
        clearCache();
        return super.refresh();
    }

    @Override
    public synchronized final I refresh(String[] propertyNames_p) throws OwException
    {

        PropertyFilter filter = new PropertyFilter();
        filter.setMaxRecursion(0);

        for (int i = 0; i < propertyNames_p.length; i++)
        {
            filter.addIncludeProperty(0, null, null, propertyNames_p[i], null);
        }

        return refresh(filter);
    }

    @Override
    public synchronized final I refresh(PropertyFilter filter_p) throws OwException
    {
        I independentObject = getEngineObject();
        Properties properties = independentObject.getProperties();

        FilterElement[] includeProperties = filter_p.getIncludeProperties();

        PropertyFilter refreshFilter = new PropertyFilter();
        refreshFilter.setMaxRecursion(0);

        PropertyFilter fetchFilter = new PropertyFilter();
        fetchFilter.setMaxRecursion(0);

        List<String> propertyNames = new LinkedList<String>();

        for (int i = 0; i < includeProperties.length; i++)
        {
            String property = includeProperties[i].getValue();
            propertyNames.add(property);
            if (properties.isPropertyPresent(property))
            {
                refreshFilter.addIncludeProperty(includeProperties[i]);
            }
            else
            {
                fetchFilter.addIncludeProperty(includeProperties[i]);
            }
        }

        if (refreshFilter.getIncludeProperties().length > 0)
        {
            independentObject.refresh(refreshFilter);
        }

        final int fetchRetries = 2;

        for (int i = 0; i < fetchRetries; i++)
        {
            try
            {
                independentObject.fetchProperties(fetchFilter);
                break;
            }
            catch (EngineRuntimeException e)
            {
                if ((i < fetchRetries - 1) && ExceptionCode.API_FETCH_MERGE_PROPERTY_ERROR.equals(e.getExceptionCode()))
                {
                    independentObject.refresh();
                    // TODO: this might already update the fetched properties ...  
                }
                else
                {
                    throw e;
                }
            }
        }

        clearCache(propertyNames);

        return super.refresh(filter_p);

    }
}
