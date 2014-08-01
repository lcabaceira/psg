package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwContentElement;

/**
 *<p>
 * Implementation of {!link {@link OwContentCollection} to be used with alfresco BPM work items.
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
public class OwAlfrescoBPMContentCollection implements OwContentCollection
{

    OwAlfrescoBPMContentElement contentElement;

    @Override
    public int getPageCount() throws Exception
    {
        return 0;
    }

    @Override
    public Collection<Integer> getContentTypes() throws Exception
    {
        List<Integer> list = new ArrayList<Integer>(1);
        list.add(Integer.valueOf(OwContentCollection.CONTENT_REPRESENTATION_TYPE_STREAM));
        return list;
    }

    @Override
    public OwContentElement getContentElement(int iContentType_p, int iPage_p) throws Exception
    {

        return this.contentElement;
    }

    /**
     * Add content to OwAlfrescoBPMContentCollection
     * @param contentElement
     */
    protected void addContentElement(OwAlfrescoBPMContentElement contentElement)
    {
        this.contentElement = contentElement;

    }

}
