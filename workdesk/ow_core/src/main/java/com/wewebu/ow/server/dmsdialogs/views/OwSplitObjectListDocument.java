package com.wewebu.ow.server.dmsdialogs.views;

import java.util.Collection;
import java.util.List;

import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * Models several object collections called splits.
 * Each split or object collection has an index based associated split name.
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
public class OwSplitObjectListDocument extends OwDocument
{
    public static final String NO_NAME = "";
    private String[] splitNames;
    private OwObjectCollection[] splitObjects;
    private Collection<OwFieldColumnInfo>[] columnInfos;
    private List<OwDocumentFunction> documentFunctions;

    public OwSplitObjectListDocument(String[] splits, Collection<OwFieldColumnInfo>[] columnInfos, List<OwDocumentFunction> documentFunctions)
    {
        splitNames = new String[splits.length];
        System.arraycopy(splits, 0, splitNames, 0, splits.length);

        splitObjects = new OwObjectCollection[splits.length];
        setColumnInfos(columnInfos);
        setDocumentFunctions(documentFunctions);
    }

    public int getSplitCount()
    {
        return splitNames.length;
    }

    public synchronized void setSplits(OwObjectCollection[] splits) throws OwException
    {
        System.arraycopy(splits, 0, this.splitObjects, 0, splits.length);
        updateSplitViews();
    }

    public OwObjectCollection getSplit(int index)
    {
        return splitObjects[index];
    }

    public String getSplitName(int index)
    {
        return splitNames[index];
    }

    public boolean isEmpty()
    {
        for (int i = 0; i < splitObjects.length; i++)
        {
            if (splitObjects[i] != null && !splitObjects[i].isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    protected void updateSplitViews() throws OwException
    {
        try
        {
            update(this, OwUpdateCodes.UPDATE_DEFAULT, null);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Error udating views.", e);
        }
    }

    public Collection<OwFieldColumnInfo>[] getColumnInfos()
    {
        return columnInfos;
    }

    public void setColumnInfos(Collection<OwFieldColumnInfo>[] columnInfos)
    {
        this.columnInfos = columnInfos;
    }

    public List<OwDocumentFunction> getDocumentFunctions()
    {
        return documentFunctions;
    }

    public void setDocumentFunctions(List<OwDocumentFunction> documentFunctions)
    {
        this.documentFunctions = documentFunctions;
    }

}
