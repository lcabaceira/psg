package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.Collection;
import java.util.List;

import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * Displays several object collections called splits.
 * Each split or object collection is displayed using an {@link OwObjectListView} in 
 * stacked labeled regions.
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
public class OwSplitObjectListView extends OwLayout
{
    private OwObjectListView[] objectListViews;

    /**
     * Constructor
     * @param splitNames label names of the splits that will be displayed 
     * @param columnInfos column information for all {@link OwObjectListView}s 
     */
    public OwSplitObjectListView(String[] splitNames, Collection<OwFieldColumnInfo>[] columnInfos, List<OwDocumentFunction> documentFunctions)
    {
        this(new OwSplitObjectListDocument(splitNames, columnInfos, documentFunctions));
    }

    public OwSplitObjectListView(OwSplitObjectListDocument document)
    {
        super();
        if (document != null)
        {
            setDocument(document);
        }
    }

    @Override
    protected void init() throws Exception
    {
        super.init();
        OwSplitObjectListDocument document = getDocument();
        int splitCount = document.getSplitCount();
        objectListViews = new OwObjectListView[splitCount];
        for (int i = 0; i < splitCount; i++)
        {
            objectListViews[i] = createObjectListView(document.getColumnInfos()[i]);
            addView(objectListViews[i], i, null);
            objectListViews[i].setExternalFormTarget(getExternalFormEventTarget());
        }
    }

    public int getSplitCount()
    {
        return getDocument().getSplitCount();
    }

    public boolean displaySplitName(int index)
    {
        return getDocument().getSplitName(index) != OwSplitObjectListDocument.NO_NAME;
    }

    public String getSplitName(int index)
    {
        return getDocument().getSplitName(index);
    }

    protected OwObjectListView createObjectListView(Collection<OwFieldColumnInfo> viewColumnInfos)
    {
        OwObjectListViewRow objectListView = new OwObjectListViewRow(0);

        objectListView.setViewMask(OwObjectListView.VIEW_MASK_USE_DOCUMENT_PLUGINS | OwObjectListView.VIEW_MASK_MULTI_SELECTION | OwObjectListView.VIEW_MASK_INSTANCE_PLUGINS);

        objectListView.setColumnInfo(viewColumnInfos);
        objectListView.setSort(new OwSort());

        if (getDocument().getDocumentFunctions() != null)
        {
            objectListView.setDocumentFunctionPluginList(getDocument().getDocumentFunctions());
        }

        return objectListView;
    }

    @Override
    public OwSplitObjectListDocument getDocument()
    {
        return (OwSplitObjectListDocument) super.getDocument();
    }

    @Override
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        OwSplitObjectListDocument document = getDocument();
        for (int i = 0; i < objectListViews.length; i++)
        {
            objectListViews[i].setObjectList(document.getSplit(i), null);
        }

    }

    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwSplitObjectListView.jsp", w_p);
    }
}
