package com.wewebu.ow.server.plug.owlink;

import java.io.Writer;
import java.util.List;

import com.wewebu.ow.server.app.OwMimeManager;
import com.wewebu.ow.server.app.OwObjectSequenceDialog;
import com.wewebu.ow.server.app.OwSubLayout;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectLinksView;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Implementation of the Links View edit properties Dialog.
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
public class OwShowLinksDialog extends OwObjectSequenceDialog
{
    private OwObjectLinksView view;
    private OwMimeManager mimeManager;

    public OwShowLinksDialog(List<OwObject> objectItems, OwObjectLinksView view)
    {
        super(objectItems);
        this.view = view;

    }

    @Override
    protected void init() throws Exception
    {
        super.init();

        mimeManager = createMimeManager();
        mimeManager.attach(getContext(), null);

        mimeManager.setItemStyle("OwEditPropertiesMimeItem");
        mimeManager.setIconStyle("OwEditPropertiesMimeIcon");

        addView(view, OwSubLayout.MAIN_REGION, null);
        view.getDocument().setObject(getCurrentItem());
    }

    public void detach()
    {
        mimeManager.reset();
        mimeManager.detach();

        super.detach();
    }

    public OwMimeManager getMimeManager()
    {
        return mimeManager;
    }

    /**(overridable)
     * Factory method to create a OwMimeManger to be used by this
     * instance.
     * @return OwMimeManager
     */
    protected OwMimeManager createMimeManager()
    {
        return new OwMimeManager();
    }

    @Override
    protected void initNewItem(Object param) throws OwException
    {
        view.getDocument().setObject(getCurrentItem());
    }

    /**
     * Define a list of columns to be displayed, or delete them (provide null).
     * If null is provided, a default set of columns will be displayed.
     * @param columnNames List of Strings (can be null)
     */
    public void setColumnNames(List<String> columnNames)
    {
        this.view.getDocument().setColumnNames(columnNames);
    }

    /**
     * Get a list of Strings which represents the columns to display. 
     * @return List of Strings, or null if not set
     */
    public List<String> getColumnNames()
    {
        return this.view.getDocument().getColumnNames();
    }

    /** render the title region
     * @param w_p Writer object to write HTML to
     * @since 3.1.0.0
     */
    private void renderTitleRegion(Writer w_p) throws Exception
    {
        // always reset MIME manager !!!
        mimeManager.reset();
        serverSideDesignInclude("dmsdialogs/OwShowLinksDialogTitle.jsp", w_p);
    }

    public boolean isRegion(int iRegion_p)
    {
        switch (iRegion_p)
        {
            case TITLE_REGION:
                return true;

            default:
                return super.isRegion(iRegion_p);
        }
    }

    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {

            case TITLE_REGION:
                renderTitleRegion(w_p);
                break;
            default:
                // render registered views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }
}