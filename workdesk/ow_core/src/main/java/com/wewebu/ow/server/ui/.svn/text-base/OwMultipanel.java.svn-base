package com.wewebu.ow.server.ui;

/**
 *<p>
 * Interface for multi panel views, which build up a continues flow from the first to the last view with validation.
 * Implement this class in your OwView and use it together with the OwNavigationView to build multi panel chains.
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
public interface OwMultipanel
{
    /** set the view that is next to this view, displays a next button to activate
      *
      * @param nextView_p OwView
      *
      */
    public abstract void setNextActivateView(OwView nextView_p) throws Exception;

    /** set the view that is prev to this view, displays a prev button to activate
      *
      * @param prevView_p OwView
      *
      */
    public abstract void setPrevActivateView(OwView prevView_p) throws Exception;

    /** check if view has validated its data and the next view can be enabled
      *
      * @return boolean true = can forward to next view, false = view has not yet validated
      *
      */
    public abstract boolean isValidated() throws Exception;
}