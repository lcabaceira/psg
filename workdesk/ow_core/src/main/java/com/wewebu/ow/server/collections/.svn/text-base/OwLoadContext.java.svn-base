package com.wewebu.ow.server.collections;

import java.util.Collection;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

/**
 *<p>
 * Used to filter out objects retrieved from the back-end. 
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
 *@see OwPageableObject
 */
public class OwLoadContext implements OwLoadContextInfo
{
    public static final int DEFAULT_PAGE_SIZE = 50;
    private static final int[] NO_TYPES = new int[0];

    private int[] objectTypes;
    private Collection<String> propertyNames;
    private OwSort sorting;
    private long maxSize, skipCount;
    private int versionSelection, pageSize;
    private OwSearchNode filter;

    public OwLoadContext()
    {
        pageSize = DEFAULT_PAGE_SIZE;
    }

    public OwLoadContext(OwLoadContextInfo ctxInfo)
    {
        setObjectTypes(ctxInfo.getObjectTypes());
        setSorting(ctxInfo.getSorting());
        setFilter(ctxInfo.getFilter());
        setMaxSize(ctxInfo.getMaxSize());
        setPropertyNames(ctxInfo.getPropertyNames());
        setVersionSelection(ctxInfo.getVersionSelection());

        if (ctxInfo.getPageSize() <= 0)
        {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        else
        {
            pageSize = ctxInfo.getPageSize();
        }
    }

    /**
     * @see OwObjectReference
     * @return the types of objects to be selected by this filter.
     */
    public int[] getObjectTypes()
    {
        return objectTypes == null ? NO_TYPES : objectTypes;
    }

    /**
     * @param objectTypes
     * @see OwObjectReference
     */
    public void setObjectTypes(int... objectTypes)
    {
        if (objectTypes != null)
        {
            this.objectTypes = new int[objectTypes.length];
            System.arraycopy(objectTypes, 0, this.objectTypes, 0, objectTypes.length);
        }
        else
        {
            this.objectTypes = NO_TYPES;
        }
    }

    public Collection<String> getPropertyNames()
    {
        return propertyNames;
    }

    public void setPropertyNames(Collection<String> propertyNames)
    {
        this.propertyNames = propertyNames;
    }

    @Override
    public OwSort getSorting()
    {
        return sorting;
    }

    public void setSorting(OwSort sort)
    {
        this.sorting = sort;
    }

    public long getMaxSize()
    {
        return maxSize;
    }

    public void setMaxSize(long maxSize)
    {
        this.maxSize = maxSize;
    }

    /**
     * @see OwSearchTemplate
     * @return the version of the objects that will be selected by this filter.
     */
    public int getVersionSelection()
    {
        return versionSelection;
    }

    /**
     * @see OwSearchTemplate
     * @param versionSelection
     */
    public void setVersionSelection(int versionSelection)
    {
        this.versionSelection = versionSelection;
    }

    @Override
    public OwSearchNode getFilter()
    {
        return filter;
    }

    public void setFilter(OwSearchNode filterCriteria)
    {
        this.filter = filterCriteria;
    }

    public boolean includeFolders()
    {
        for (int type : this.objectTypes)
        {
            if (type == OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS || type == OwObjectReference.OBJECT_TYPE_FOLDER)
            {
                return true;
            }
        }
        return false;
    }

    public boolean includeDocuments()
    {
        for (int type : this.objectTypes)
        {
            if (type == OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS || type == OwObjectReference.OBJECT_TYPE_DOCUMENT)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public long getSkipCount()
    {
        return skipCount;
    }

    public void setSkipCount(long skipCount)
    {
        this.skipCount = skipCount;
    }

    public int getPageSize()
    {
        return this.pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }
}
