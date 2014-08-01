package com.wewebu.ow.server.plug.owaddmultidocuments;

import java.util.EventListener;

/**
 *<p>
 * Listener for save event.
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
public interface OwMultiDocumentObjectPropertyViewListner extends EventListener
{

    /** overridable called when user clicks save and all properties are correct
    */
    public abstract void onSaveDocument() throws Exception;

    /** overridable called when user clicks save and all properties are correct
     */
    public abstract void onSaveAllDocument() throws Exception;

    /** overridable called when user clicks cancel button, 
     * and returns to last opened view.
     * <p>
     * <b>Attention this interface will be not deleted, but
     * it will be refactored for consistent rendering and working 
     * of cancel operations.</b></p>
    */
    void onCancel() throws Exception;

}