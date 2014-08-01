package com.wewebu.ow.server.ui.button;

/**
 *<p>
 * POJO for image link/button rendering.
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
 *@since 4.1.0.0
 */
public class OwImageButton
{
    private String tooltip;
    private String eventString;
    private String imageLink;
    private String designClass;

    public OwImageButton()
    {
    }

    public OwImageButton(String eventString, String imageLink)
    {
        this(eventString, imageLink, null);
    }

    public OwImageButton(String eventString, String imageLink, String tooltip)
    {
        this(eventString, imageLink, tooltip, null);
    }

    public OwImageButton(String eventString, String imageLink, String tooltip, String designClass)
    {
        this.eventString = eventString;
        this.imageLink = imageLink;
        this.tooltip = tooltip;
        this.designClass = designClass;
    }

    /**
     * Get text which should be used as tooltip.
     * Can return null if not set.
     * @return String or null
     */
    public String getTooltip()
    {
        return tooltip;
    }

    public void setTooltip(String tooltip)
    {
        this.tooltip = tooltip;
    }

    /**
     * Get String which represent the event/action
     * to be fired when clicked.
     * @return String or null
     */
    public String getEventString()
    {
        return eventString;
    }

    public void setEventString(String eventString)
    {
        this.eventString = eventString;
    }

    /**
     * Get an URI/URL to the image
     * which should be used as background for the button.
     * @return String
     */
    public String getImageLink()
    {
        return imageLink;
    }

    public void setImageLink(String imageLink)
    {
        this.imageLink = imageLink;
    }

    /**
     * Design class which can be set for this button instance.
     * @return String or null
     */
    public String getDesignClass()
    {
        return designClass;
    }

    public void setDesignClass(String designClass)
    {
        this.designClass = designClass;
    }

}
