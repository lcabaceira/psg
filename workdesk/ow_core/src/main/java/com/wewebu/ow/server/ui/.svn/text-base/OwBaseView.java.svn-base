package com.wewebu.ow.server.ui;

import java.io.Writer;

/**
 *<p>
 * Base View functionality, which supports rendering and regions.
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
public interface OwBaseView
{

    public static final String EMPTY_STRING = "";

    /** render the object
     *
     * @param w_p Writer object to write HTML to
     */
    public abstract void render(Writer w_p) throws Exception;

    /** render only a region in the view, used by derived classes
     *
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public abstract void renderRegion(Writer w_p, int iRegion_p) throws Exception;

    /** render only a region in the view, used by derived classes
     *
     * @param w_p Writer object to write HTML to
     * @param strRegion_p named region to render
     */
    public abstract void renderNamedRegion(Writer w_p, String strRegion_p) throws Exception;

    /** determine if region exists
     *
     * @param iRegion_p ID of the region to render
     * @return true if region contains anything and should be rendered
     */
    public abstract boolean isRegion(int iRegion_p) throws Exception;

    /** determine if region exists
     *
     * @param strRegion_p name of the region to render
     * @return true if region contains anything and should be rendered
     */
    public abstract boolean isNamedRegion(String strRegion_p) throws Exception;

    /**
     * Returns a title for this view or an empty String
     * @return String representing the title of this view
     * @since 3.0.0.0
     */
    public String getTitle();

    /**
     * This method is used to create a &quot;Breadcrumb&quot;
     * navigation view. The view is just attaching it's known
     * title/name to the part which is returned from it child.
     * <p>
     * Example: We have a View A which contains a View B which
     * has a many views contained.
     * <code><pre>
     *      A.getBreadcrumbPart()
     *      {
     *          return <b>this</b>.getTitle() + " - " + B.getBreadcrumbPart();
     *      }
     *      B.getBreadcrumbPart()
     *      {
     *          return <b>this</b>.getName() + " - " + getMainView().getBreadcrumbPart();
     *      }
     * </pre></code>
     * </p>
     * So every view is just returning the part of the navigation which is known 
     * in it's context. This method should not return <b><code>null</code></b> at
     * least the {@link #EMPTY_STRING} should be returned.
     * @return String representing the current bread crumb part
     * @since 3.0.0.0
     */
    public String getBreadcrumbPart();
}