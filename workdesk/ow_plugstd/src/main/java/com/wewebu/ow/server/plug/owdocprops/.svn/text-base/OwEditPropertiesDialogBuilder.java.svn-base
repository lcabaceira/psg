package com.wewebu.ow.server.plug.owdocprops;

import java.util.Collection;
import java.util.Map;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.field.OwSearchTemplate;

/**
 *<p>
 * OwEditPropertiesDialog builder.
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
public class OwEditPropertiesDialogBuilder
{
    private Collection<OwObject> items;
    private int index;
    private OwObject parentObject;
    private int maxElementSize;
    private Collection<String> versionColumnInfo;
    private OwSearchTemplate historyViewSearchTemplate;
    private Collection<String> historyViewColumnInfo;
    private Collection<String> batchIndexProperties;
    private OwObjectLinkRelation relationSplit;
    private boolean displayLinksByType;
    private Collection<String> linkClassNames;
    private Map<Integer, Map<String, String>> layoutRegionAttributes;

    public OwEditPropertiesDialog build()
    {
        return new OwEditPropertiesDialog(this);
    }

    public OwEditPropertiesDialogBuilder items(Collection<OwObject> items)
    {
        this.items = items;
        return this;
    }

    public OwEditPropertiesDialogBuilder index(int index)
    {
        this.index = index;
        return this;
    }

    public OwEditPropertiesDialogBuilder parentObject(OwObject parentObject)
    {
        this.parentObject = parentObject;
        return this;
    }

    public OwEditPropertiesDialogBuilder maxElementSize(int maxElementSize)
    {
        this.maxElementSize = maxElementSize;
        return this;
    }

    public OwEditPropertiesDialogBuilder versionColumnInfo(Collection<String> versionColumnInfo)
    {
        this.versionColumnInfo = versionColumnInfo;
        return this;
    }

    public OwEditPropertiesDialogBuilder historyViewSearchTemplate(OwSearchTemplate historyViewSearchTemplate)
    {
        this.historyViewSearchTemplate = historyViewSearchTemplate;
        return this;
    }

    public OwEditPropertiesDialogBuilder historyViewColumnInfo(Collection<String> historyViewColumnInfo)
    {
        this.historyViewColumnInfo = historyViewColumnInfo;
        return this;
    }

    public OwEditPropertiesDialogBuilder batchIndexProperties(Collection batchIndexProperties)
    {
        this.batchIndexProperties = batchIndexProperties;
        return this;
    }

    public OwEditPropertiesDialogBuilder relationSplit(OwObjectLinkRelation relationSplit)
    {
        this.relationSplit = relationSplit;
        return this;
    }

    public OwEditPropertiesDialogBuilder displayLinksByType(boolean displayLinksByType)
    {
        this.displayLinksByType = displayLinksByType;
        return this;
    }

    public OwEditPropertiesDialogBuilder linkClassNames(Collection<String> linkClassNames)
    {
        this.linkClassNames = linkClassNames;
        return this;
    }

    public OwEditPropertiesDialogBuilder layoutRegionAttributes(Map<Integer, Map<String, String>> attributes)
    {
        this.layoutRegionAttributes = attributes;
        return this;
    }

    Collection<OwObject> getItems()
    {
        return items;
    }

    int getIndex()
    {
        return index;
    }

    OwObject getParentObject()
    {
        return parentObject;
    }

    int getMaxElementSize()
    {
        return maxElementSize;
    }

    Collection<String> getVersionColumnInfo()
    {
        return versionColumnInfo;
    }

    OwSearchTemplate getHistoryViewSearchTemplate()
    {
        return historyViewSearchTemplate;
    }

    Collection<String> getHistoryViewColumnInfo()
    {
        return historyViewColumnInfo;
    }

    Collection<String> getBatchIndexProperties()
    {
        return batchIndexProperties;
    }

    OwObjectLinkRelation getRelationSplit()
    {
        return relationSplit;
    }

    boolean isDisplayLinksByType()
    {
        return displayLinksByType;
    }

    Collection<String> getLinkClassNames()
    {
        return linkClassNames;
    }

    Map<Integer, Map<String, String>> getLayoutRegionAttributes()
    {
        return layoutRegionAttributes;
    }

}
