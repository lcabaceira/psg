package com.wewebu.ow.server.ecmimpl.opencmis.collections;

import java.util.Iterator;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ItemIterable;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwIterableAttributeBag;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.object.OwCMISObject;

/**
 *<p>
 * {@link OwIterable} over a native CMIS iterable.
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
public class OwCMISObjectIterable extends OwIterableAttributeBag<OwCMISObject>
{
    private ItemIterable<? extends CmisObject> iterable;
    private OwCMISNativeSession session;
    private long skipCount = 0;

    public OwCMISObjectIterable(ItemIterable<? extends CmisObject> iterable, OwCMISNativeSession session)
    {
        this.iterable = iterable;
        this.session = session;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#skipTo(long)
     */
    @Override
    public OwIterable<OwCMISObject> skipTo(long position)
    {
        OwCMISObjectIterable skippedIterable = new OwCMISObjectIterable(this.iterable.skipTo(position), this.session);
        skippedIterable.skipCount = position;
        return skippedIterable;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPage()
     */
    @Override
    public OwIterable<OwCMISObject> getPage()
    {
        OwCMISObjectIterable page = new OwCMISObjectIterable(this.iterable.getPage(), this.session);
        page.isPage = true;
        return page;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPage(int)
     */
    @Override
    public OwIterable<OwCMISObject> getPage(int maxNumItems)
    {
        OwCMISObjectIterable page = new OwCMISObjectIterable(this.iterable.getPage(maxNumItems), this.session);
        page.isPage = true;
        return page;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#iterator()
     */
    @Override
    public Iterator<OwCMISObject> iterator()
    {
        if (isPage)
        {
            return new OwCMISObjectIterator(this.iterable.getPage().iterator(), this.session);
        }
        else
        {
            return new OwCMISObjectIterator(this.iterable.skipTo(skipCount).iterator(), this.session);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getPageNumItems()
     */
    @Override
    public long getPageNumItems()
    {
        return this.iterable.getPageNumItems();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getHasMoreItems()
     */
    @Override
    public boolean getHasMoreItems()
    {
        return this.iterable.getHasMoreItems();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.collections.OwIterable#getTotalNumItems()
     */
    @Override
    public long getTotalNumItems()
    {
        return this.iterable.getTotalNumItems();
    }

}
