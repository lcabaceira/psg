package com.wewebu.ow.server.app;

/**
 *<p>
 * Base Interface for sequence views.
 * A sequence view can browse back and forth through a sequence of items.
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
public interface OwSequenceView
{
    /** called when the view needs to know if there is a next item
    */
    public abstract boolean hasNext() throws Exception;

    /** called when the view needs to know if there is a prev item
    */
    public abstract boolean hasPrev() throws Exception;

    /** move to prev item 
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the prev item, if this is the last item, closes the view
     */
    public abstract void prev(boolean fRemoveCurrent_p) throws Exception;

    /** move to next item
     * @param fRemoveCurrent_p true = remove the current sequence item and then move to the next item, if this is the last item, closes the view
     */
    public abstract void next(boolean fRemoveCurrent_p) throws Exception;

    /** get the number of sequence items in the view */
    public abstract int getCount();
}