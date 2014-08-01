package com.wewebu.ow.server.ecm.ui;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ui.OwMenu;

/**
 *<p>
 * Base Class for the access rights submodule to be created in the network (ECM) Adapter. Submodules are used to delegate
 * ECM specific user interactions to the ECM Adapter, which can not be generically solved.<br/> 
 * e.g.: Login or Access rights Dialog.<br/><br/>
 * To be implemented with the specific ECM system.
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
 */
public abstract class OwUIAccessRightsModul<N extends OwNetwork> extends OwUISubModul<N>
{
    private boolean forceLiveUpdate = false;

    /** make the access rights view read-only 
     * @param fReadOnly_p true = user can only view the access rights, false = user can edit the access rights (default)
     */
    public abstract void setReadOnly(boolean fReadOnly_p);

    /**
     * 
     * @param fLiveUpdate_p true = the view must update any data change through its own triggers, false = the view can 
     *        update data by submit triggers
     * @since 3.1.0.0
     */
    public void setLiveUpdate(boolean fLiveUpdate_p)
    {
        this.forceLiveUpdate = fLiveUpdate_p;
    }

    /**
     *
     * @return true if this UI module must update any data change through its own triggers<br>
     *         false if the view may update data by submit triggers (the use of submit triggers 
     *         is not mandatory)  
     * @since 3.1.0.0
     */
    public boolean getLiveUpdate()
    {
        return this.forceLiveUpdate;
    }

    /** get the menu of the access rights module 
     * 
     * @return OwMenu or null if not defined
     */
    public OwMenu getMenu()
    {
        // default returns null
        return null;
    }
}