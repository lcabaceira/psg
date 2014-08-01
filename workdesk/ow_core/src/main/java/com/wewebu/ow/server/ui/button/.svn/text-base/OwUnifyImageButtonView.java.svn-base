package com.wewebu.ow.server.ui.button;

import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * Helper class to summarize many OwImageButtonView instances
 * and render them as one list of ImageButton's.
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
 *@since 4.1.0.0
 */
public class OwUnifyImageButtonView extends OwImageButtonView
{
    private List<OwImageButtonView> buttonProvider;

    public OwUnifyImageButtonView()
    {
        super();
        buttonProvider = new LinkedList<OwImageButtonView>();
    }

    public OwUnifyImageButtonView(List<OwImageButtonView> buttonProvider)
    {
        this();
        buttonProvider.addAll(buttonProvider);
    }

    /**
     * Add a button provider which should be handled during rendering.
     * @param buttonProvider OwImageButtonView
     */
    public void addButtonProvider(OwImageButtonView buttonProvider)
    {
        getButtonProvider().add(buttonProvider);
    }

    /**
     * Remove a specific button provider.
     * @param buttonProvider OwImageButtonView to be removed
     */
    public void removeButtonProvider(OwImageButtonView buttonProvider)
    {
        getButtonProvider().remove(buttonProvider);
    }

    @Override
    public List<OwImageButton> getButtonList()
    {
        if (getButtonProvider().isEmpty())
        {
            return super.getButtonList();
        }
        else
        {
            List<OwImageButton> lst = new LinkedList<OwImageButton>();
            for (OwImageButtonView provider : getButtonProvider())
            {
                lst.addAll(provider.getButtonList());
            }
            return lst;
        }
    }

    /**
     * Getter for the list of button provider.
     * @return List of OwImageButtonView
     */
    protected List<OwImageButtonView> getButtonProvider()
    {
        return this.buttonProvider;
    }

    //    @Override
    //    protected void init() throws Exception
    //    {
    //        super.init();
    //    }
    //
    //    @Override
    //    public void attach(OwAppContext context_p, String strName_p) throws Exception
    //    {
    //        super.attach(context_p, strName_p);
    //        for (OwImageButtonView prov : getButtonProvider())
    //        {
    //            if (getDocument() != null)
    //            {
    //                prov.setDocument(getDocument());
    //            }
    //            prov.attach(context_p, null);
    //        }
    //    }
    //
    //    @Override
    //    public void detach()
    //    {
    //        for (OwImageButtonView prov : getButtonProvider())
    //        {
    //            prov.detach();
    //        }
    //        super.detach();
    //    }

}
