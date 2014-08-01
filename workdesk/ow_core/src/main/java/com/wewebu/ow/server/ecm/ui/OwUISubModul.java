package com.wewebu.ow.server.ecm.ui;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Base Class for submodules to be created in the network (ECM) Adapter. Submodules are used to delegate
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
public abstract class OwUISubModul<N extends OwNetwork> extends OwView
{
    /** reference to the ECM Adapter */
    private N network;

    /** get network reference */
    public N getNetwork()
    {
        return network;
    }

    /** set the base URL to the page 
     * @param theNetwork_p reference to the ECM Adapter
     */
    public void init(N theNetwork_p) throws Exception
    {
        network = theNetwork_p;
    }

}