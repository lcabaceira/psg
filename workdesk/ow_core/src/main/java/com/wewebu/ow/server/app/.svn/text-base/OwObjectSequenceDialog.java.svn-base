package com.wewebu.ow.server.app;

import java.util.List;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;

/**
 *<p>
 * Object collection nase implementation.
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
public abstract class OwObjectSequenceDialog extends OwStandardSequenceDialog
{
    private List<OwObject> objectItems;
    private int index;

    public OwObjectSequenceDialog(List<OwObject> objectItems)
    {
        super();
        this.objectItems = objectItems;
    }

    @Override
    public void prev(boolean fRemoveCurrent_p) throws Exception
    {
        if (fRemoveCurrent_p)
        {
            throw new OwNotSupportedException("OwEditPropertiesDialog.prev(fRemoveCurrent_p==true) not supported.");
        }

        if (hasPrev())
        {
            index--;
        }
        else
        {
            index = (objectItems.size() - 1);
        }

        initPrevtNewItem();

    }

    protected void initPrevtNewItem() throws OwException
    {
        initNewItem(false);
    }

    @Override
    public void next(boolean fRemoveCurrent_p) throws Exception
    {
        if (fRemoveCurrent_p)
        {
            // === remove the current item and move to the next
            if (getCount() == 1)
            {
                // === only one item left
                // close dialog
                super.closeDialog();
                return;
            }
            else
            {
                objectItems.remove(index);
                if (index >= objectItems.size())
                {
                    index = 0;
                }
            }
        }
        else
        {
            // === move to the next item
            if (hasNext())
            {
                index++;
            }
            else
            {
                index = 0;
            }
        }

        initNextNewItem();
    }

    protected void initNextNewItem() throws OwException
    {
        initNewItem(true);
    }

    protected abstract void initNewItem(Object paramerters) throws OwException;

    protected int getIndex()
    {
        return index;
    }

    public OwObject getCurrentItem()
    {
        return objectItems.get(index);
    }

    @Override
    public int getCount()
    {
        return objectItems.size();
    }

    public boolean hasNext() throws Exception
    {
        return (index < (objectItems.size() - 1));
    }

    public boolean hasPrev() throws Exception
    {
        return (index > 0);
    }
}
