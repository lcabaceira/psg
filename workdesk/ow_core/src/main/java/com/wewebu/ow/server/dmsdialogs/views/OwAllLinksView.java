package com.wewebu.ow.server.dmsdialogs.views;

import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;

/**
 *<p>
 * Displays {@link OwObjectLink}s using {@link OwSplitObjectListView} and no filter.
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
 *@since 4.1.1.0
 */
public class OwAllLinksView extends OwObjectLinksView
{
    protected OwSplitObjectListView splitView;

    public OwAllLinksView(OwObjectLinksDocument document)
    {
        super(document);
    }

    @Override
    protected void init() throws Exception
    {
        super.init();

        splitView = createSplitView();
        addView(splitView, LINKS_REGION, null);
    }

    @Override
    protected void refresh(OwObjectCollection[] splitLinks) throws Exception
    {
        super.refresh(splitLinks);
        splitView.getDocument().setSplits(splitLinks);
    }

}
