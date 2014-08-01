package com.wewebu.ow.server.ecm.ui;

import java.io.PrintWriter;
import java.io.Writer;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwExceptionManager;

/**
 *<p>
 * Base Class for the login sub module to be created in the network (ECM) Adapter. Submodules are used to delegate
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
public class OwUILoginModul<N extends OwNetwork> extends OwUISubModul<N>
{
    /** logger */
    private static Logger LOG = OwLogCore.getLogger(OwUIUserSelectModul.class);

    /**
     * Render login error, by respecting accessibility requests.
     * @param w_p - the {@link Writer} object.
     * @throws Exception
     * @since 3.0.0.0
     */
    public void renderErrors(Writer w_p) throws Exception
    {
        Throwable e = ((OwMainAppContext) getContext()).getError();
        if (e != null)
        {
            w_p.write("<div id=\"OwMainLayout_ERROR\">");
            // print error which occurred upon recent request
            OwExceptionManager.PrintCatchedException(getContext().getLocale(), e, new PrintWriter(w_p), "OwErrorStack");
            w_p.write("</div>");
        }
    }
}