package com.wewebu.ow.server.ui;

/**
 *<p>
 * Interface for update events that where sent from documents.
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
public interface OwUpdateTarget
{
    /** called by the framework to update the view when OwDocument.Update was called
    *
    *  NOTE:   We can not use the onRender method to update,
    *          because we do not know the call order of onRender.
    *          onUpdate is always called before all onRender methods.
    *
    *  @param caller_p OwEventTarget target that called update
    *  @param iCode_p int optional reason code
    *  @param param_p Object optional parameter representing the refresh, depends on the value of iCode_p, can be null
    */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception;
}