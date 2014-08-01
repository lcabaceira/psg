package com.wewebu.ow.server.plug.owshortcut;

/**
 *<p>
 * {@link OwShortCutDocument} listener interface.
 * Clients of  the {@link OwShortCutDocument} class can be informed through this interface
 * of document state changes (egg. adding or removing shortcuts).
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
 * @see OwShortCutDocument#addEventListener(OwShortcutDocumentEventListner)
 * @see OwShortCutDocument#removeEventListener(OwShortcutDocumentEventListner)
 */
public interface OwShortcutDocumentEventListner
{
    /**
     * Called after the {@link OwShortCutDocument} state was changed   
     * @param document_p the document that triggered the event
     */
    void onShortcutDocumentChaged(OwShortCutDocument document_p);
}
