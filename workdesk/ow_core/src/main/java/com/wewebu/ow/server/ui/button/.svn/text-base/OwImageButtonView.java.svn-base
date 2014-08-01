package com.wewebu.ow.server.ui.button;

import java.io.Writer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.ui.OwView;

/**
 *<p>
 * Simple view which provides additional handling,
 * for ImageButton rendering.
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
public class OwImageButtonView extends OwView
{
    private List<String> designClasses;
    private String htmlId;

    @Override
    protected void init() throws Exception
    {
        super.init();
        this.designClasses = new LinkedList<String>();
    }

    @Override
    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("OwImageButtonView.jsp", w_p);
    }

    /**
     * List of design classes, use also for
     * direct attaching new classes.
     * <p>
     * <code>
     *  getDesignClasses().add("MyNewDesignClass");
     * </code>
     * </p>
     * @return List (by default empty list)
     */
    public List<String> getDesignClasses()
    {
        return this.designClasses;
    }

    /**
     * Get the id for UI (HTML)
     * @return String (can be null)
     */
    public String getHtmlId()
    {
        return htmlId;
    }

    /**
     * Set an Id which will be used to 
     * identify the component in UI (HTML) 
     * @param id String (can be null)
     */
    public void setHtmlId(String id)
    {
        this.htmlId = id;
    }

    /**
     * Get a list of buttons which should be rendered. 
     * @return List
     */
    public List<OwImageButton> getButtonList()
    {
        return Collections.emptyList();
    }
}
