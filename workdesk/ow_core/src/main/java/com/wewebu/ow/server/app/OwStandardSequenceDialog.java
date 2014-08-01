package com.wewebu.ow.server.app;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

/**
 *<p>
 * Extends the Standard Dialog with a prev / next button, as well as a overridden listener interface
 * to handle sequences.
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
public abstract class OwStandardSequenceDialog extends OwStandardDialog implements OwSequenceView
{
    /** called when the Dialog needs to know if there is a next item
     *
     */
    public boolean hasNext() throws Exception
    {
        return false;
    }

    /** called when the Dialog needs to know if there is a prev item
     *
     */
    public boolean hasPrev() throws Exception
    {
        return false;
    }

    /** render the views of the region
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        switch (iRegion_p)
        {
        // === render internal regions here
            case CLOSE_BTN_REGION:
            {
                // render navigation buttons
                super.renderRegion(w_p, iRegion_p);
                renderNavigationButtons(w_p);
            }
                break;

            default:
                // render registered views
                super.renderRegion(w_p, iRegion_p);
                break;
        }
    }

    /**
     * render Prev and Next buttons
     * @param w_p
     * @throws Exception
     * @since 3.1.0.0
     */
    public void renderNavigationButtons(Writer w_p) throws Exception
    {
        if (hasPrev() || hasNext())
        {
            // === render prev next button
            String prevTooltip = getContext().localize("app.OwStandardSequenceDialog.prevelement", "Previous Item");
            String nextTooltip = getContext().localize("app.OwStandardSequenceDialog.nextelement", "Next Item");

            w_p.write("&nbsp;");

            // render prev / next button
            if (hasPrev())
            {
                w_p.write("<a href=\"" + getPrevHREF() + "\" title=\"" + prevTooltip + "\"><img class=\"OwStandardDialog_icon\" src=\"" + getContext().getDesignURL() + "/images/OwStandardDialog/prev.png\" border=\"0\" alt=\"" + prevTooltip
                        + "\" title=\"" + prevTooltip + "\"/></a>");
            }
            else
            {
                w_p.write("<img class=\"OwStandardDialog_icon\" src=\"" + getContext().getDesignURL() + "/images/OwStandardDialog/prev_disabled.png\" alt=\"\" title=\"\" border=\"0\"/>");
            }

            w_p.write("&nbsp;");

            if (hasNext())
            {
                w_p.write("<a href=\"" + getNextHREF() + "\" title=\"" + nextTooltip + "\"><img class=\"OwStandardDialog_icon\" src=\"" + getContext().getDesignURL() + "/images/OwStandardDialog/next.png\" border=\"0\" alt=\"" + nextTooltip
                        + "\" title=\"" + nextTooltip + "\"></a>");
            }
            else
            {
                w_p.write("<img class=\"OwStandardDialog_icon\" src=\"" + getContext().getDesignURL() + "/images/OwStandardDialog/next_disabled.png\" border=\"0\" alt=\"\" title=\"\"/>");
            }
        }

    }

    /**
     * 
     * @return previous button anchor HREF value
     * @since 4.2.0.0 
     */
    protected String getPrevHREF()
    {
        return getEventURL("Prev", null);
    }

    /**
     * 
     * @return next button anchor HREF value
     * @since 4.2.0.0 
     */
    protected String getNextHREF()
    {
        return getEventURL("Next", null);
    }

    /** event called when user clicked a close on the dialog
     *   @param request_p  HttpServletRequest
     */
    public void onPrev(HttpServletRequest request_p) throws Exception
    {
        prev(false);
    }

    /** event called when user clicked a close on the dialog
     *   @param request_p  HttpServletRequest
     */
    public void onNext(HttpServletRequest request_p) throws Exception
    {
        next(false);
    }
}