package com.wewebu.ow.server.dmsdialogs.views;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwFieldManager;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.collections.OwEmptyIterable;
import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwObjectCollectionIterableAdapter;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * Abstraction of paging handling for ListViews.
 * Provide new helper and consolidate methods defined by OwPageableView interface.
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
public abstract class OwPageableListView extends OwObjectListView
{
    private static final Logger LOG = OwLogCore.getLogger(OwPageableListView.class);

    private OwFieldManager fieldManager;
    private OwClientRefreshContext refreshContext;
    private OwSort sort;
    private Collection<? extends OwFieldColumnInfo> columnInfo;

    /** current zero based page number in multiples of getPageSize() for paging the results */
    private int currentPage;

    private OwObjectCollection objectList;
    private OwIterable<OwObject> objectIterable;

    private OwIterable<OwObject> page;

    protected OwPageableListView()
    {
        super();
    }

    public OwPageableListView(int viewMask)
    {
        super(viewMask);
    }

    @Override
    public void detach()
    {
        super.detach();
        if (getFieldManager() != null)
        {
            this.fieldManager.detach();
            this.fieldManager = null;
        }
        this.columnInfo = null;
        this.sort = null;
        this.objectList = null;
        this.refreshContext = null;
        this.objectIterable = null;
        this.page = null;
    }

    @Override
    public void setObjectList(OwObjectCollection objectList_p, OwObject parentObject_p) throws Exception
    {
        this.objectList = objectList_p;
        this.objectIterable = null;
        this.page = null;
        setParentObject(parentObject_p);
    }

    @Override
    public void setObjectIterable(OwIterable<OwObject> iterable, OwObject parentObject_p) throws Exception
    {
        this.objectIterable = iterable;
        this.objectList = null;
        this.page = null;
        setParentObject(parentObject_p);
    }

    @Override
    public OwObjectCollection getObjectList()
    {
        return this.objectList;
    }

    @Override
    public OwIterable<OwObject> getObjectIterable()
    {
        return this.objectIterable;
    }

    @Override
    public OwFieldManager getFieldManager()
    {
        return fieldManager;
    }

    @Override
    public void setFieldManager(OwFieldManager fieldmanager_p)
    {
        this.fieldManager = fieldmanager_p;
    }

    @Override
    public void setRefreshContext(OwClientRefreshContext refreshContext)
    {
        this.refreshContext = refreshContext;
    }

    @Override
    protected OwClientRefreshContext getRefreshContext()
    {
        return this.refreshContext;
    }

    @Override
    public void setColumnInfo(Collection<? extends OwFieldColumnInfo> columnInfo)
    {
        this.columnInfo = columnInfo;
    }

    @Override
    public Collection<? extends OwFieldColumnInfo> getColumnInfo()
    {
        return this.columnInfo;
    }

    @Override
    public void setSort(OwSort sort)
    {
        this.sort = sort;
    }

    @Override
    public OwSort getSort()
    {
        return this.sort;
    }

    @Override
    public Collection<String> getRetrievalPropertyNames() throws Exception
    {
        return null;
    }

    public void onPageAbsolut(HttpServletRequest request_p) throws Exception
    {
        pageAbsolut(Integer.parseInt(request_p.getParameter(QUERY_KEY_PAGE)));
    }

    public void onPageNext(HttpServletRequest request_p) throws Exception
    {
        setCurrentPage(getCurrentPage() + 1);
    }

    public void onPagePrev(HttpServletRequest request_p) throws Exception
    {
        setCurrentPage(getCurrentPage() - 1);
    }

    public int getPageCount()
    {
        if (getObjectList() == null && getObjectIterable() == null)
        {
            return 1;
        }
        else
        {
            int count = getCount();
            if (count < 0)
            {
                return -1;
            }
            else
            {
                int pageSize = getPageSize();
                if (pageSize > 0)
                {
                    int modulo = count % pageSize;
                    int pageCount = count / pageSize;
                    if (modulo != 0 || (pageCount == 0 && count != 0))
                    {
                        return pageCount + 1;
                    }
                    else
                    {
                        return pageCount;
                    }
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    public int getCurrentPage()
    {
        if (isShowMaximized())
        {
            return 0;
        }
        else
        {
            int pageCount = getPageCount();
            if (this.currentPage >= pageCount && !(pageCount < 0))
            {
                this.currentPage = pageCount - 1;
                this.page = null;
            }
            if (this.currentPage < 0)
            {
                this.currentPage = 0;
                this.page = null;
            }
            return this.currentPage;
        }
    }

    public boolean canPagePrev()
    {
        if (getCount() >= 0)
        {
            return getCurrentPage() == 0 ? false : getCurrentPage() < getPageCount();
        }
        else
        {
            return getCurrentPage() == 0 ? false : true;
        }
    }

    public boolean canPageNext()
    {
        int pageCount = getPageCount();
        if (pageCount < 0)
        {
            return getDisplayedPage().getHasMoreItems();
        }
        else
        {
            return pageCount > 1 ? getCurrentPage() < pageCount - 1 : false;
        }
    }

    @Override
    protected boolean isPagingEnabled()
    {
        int pageCount = getPageCount();
        return pageCount > 1 || pageCount < 0;
    }

    @Override
    public String getPagePrevEventURL()
    {
        return getEventURL("PagePrev", null);
    }

    @Override
    public String getPageNextEventURL()
    {
        return getEventURL("PageNext", null);
    }

    @Override
    public String getPageAbsolutEventURL(String aditionalParameters_p)
    {
        return getEventURL("PageAbsolut", aditionalParameters_p);
    }

    protected void pageAbsolut(int currentPage)
    {
        int pageCount = getPageCount();
        if (currentPage < 0 || (currentPage > pageCount && pageCount > 0))
        {
            throw new IllegalArgumentException("Page index out of bound: " + currentPage);
        }
        else
        {
            if (currentPage != this.currentPage)
            {
                this.page = null;
            }
            if (pageCount == 0)
            {
                this.currentPage = 0;
            }
            else
            {
                this.currentPage = currentPage;
            }
        }
    }

    /**
     * Get the items for current displayed page.
     * @return OwIterable current page
     */
    public OwIterable<OwObject> getDisplayedPage()
    {
        if (page == null)
        {
            OwIterable<OwObject> root;
            if (getObjectList() != null)
            {
                root = new OwObjectCollectionIterableAdapter<OwObject>(getObjectList());
            }
            else
            {
                root = getObjectIterable();
            }

            if (root != null)
            {
                int idxPage = getCurrentPage();
                int pageSize = getPageSize();
                page = root.skipTo(idxPage * pageSize).getPage(pageSize);
            }
            else
            {
                page = new OwEmptyIterable<OwObject>();
            }
        }
        return page;
    }

    /**
     * Helper to identify if list is completely retrieved or there are more results. 
     * @return boolean
     */
    public boolean isCollectionComplete()
    {
        Object complete = null;
        try
        {
            complete = getDisplayedPage().getAttribute(OwObjectCollection.ATTRIBUTE_IS_COMPLETE);
        }
        catch (Exception e)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.warn("Could not identify attribute IS_COMPLETE returning true", e);
            }
            else
            {
                LOG.warn("OwPageableLIstView.isComplete: Could not identify attribute IS_COMPLETE returning true");
            }
        }
        if (complete == null)
        {
            return true;
        }
        else
        {
            return Boolean.valueOf("" + complete).booleanValue();
        }
    }

    /**
     * Return the configured PageSize for lists.
     * @return integer UI page list size
     */
    public int getPageSize()
    {
        if (isShowMaximized())
        {
            return getCount();
        }
        else
        {
            return ((OwMainAppContext) getContext()).getPageSizeForLists();
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.app.OwClientRefreshContext#onClientRefreshContextUpdate(int, java.lang.Object)
     */
    @Override
    public void onClientRefreshContextUpdate(int iReason_p, Object param_p) throws Exception
    {
        this.page = null;
    }
}
