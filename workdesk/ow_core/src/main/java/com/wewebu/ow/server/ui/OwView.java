package com.wewebu.ow.server.ui;

import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.log.OwLogCore;

/**
 *<p>
 * View Class, which supports Document View Pattern and acts as an Event Target.
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
public abstract class OwView extends OwEventTarget implements OwBaseView, OwUpdateTarget
{
    /** request key for to reference the current module in a JSP redirect */
    public static final String CURRENT_MODULE_KEY = "ow_current_module";

    private static final Logger LOG = OwLogCore.getLogger(OwView.class);
    // === form renderer
    /** start delimiter for the formular placeholders, used in onRenderHTMLFormular. */
    public static final String FORMULAR_PLACEHOLDER_START_DELIMITER = "{#";
    /** end delimiter for the formular placeholders, used in onRenderHTMLFormular. */
    public static final String FORMULAR_PLACEHOLDER_END_DELIMITER = "#}";

    /** list of contained views if any  */
    private LinkedList m_InnerViewList = null;

    /** reference to the document. */
    private OwDocument m_Document;

    /** minimized display mode */
    private boolean m_fMinimized;

    /** the parent view of this view or null if the top view*/
    private OwView m_ParentView;

    /** external form view overrides internal form */
    protected OwEventTarget m_externalFormEventTarget;

    public OwView()
    {
        super();
        m_fMinimized = false;
        m_externalFormEventTarget = this;
    }

    /** get the icon URL for this view to be displayed
    *
    *  @return String icon URL, or null if not defined
    */
    public String getIcon() throws Exception
    {
        return null;
    }

    /** get a iterator object for the child views
     * @return iterator for the child views
     */
    public Iterator getIterator()
    {
        if (m_InnerViewList != null)
        {
            return m_InnerViewList.iterator();
        }
        else
        {
            return Collections.emptyList().iterator();
        }
    }

    /** get the list of views
     */
    public List getViewList()
    {
        if (m_InnerViewList == null)
        {
            m_InnerViewList = new LinkedList();
        }

        return m_InnerViewList;
    }

    /** overridable title of the view
     * @return String localized display name for the view
     * */
    public String getTitle()
    {
        return EMPTY_STRING;
    }

    /** set the parent to this view
     * @param parent_p Parent view
     */
    protected void setParent(OwView parent_p)
    {
        m_ParentView = parent_p;
    }

    /** the parent view of this view or null if the top view 
     * @return the parent view of this view
     */
    public OwView getParent()
    {
        return m_ParentView;
    }

    /** overridable function to set a view as a maximized view, i.e. the submitted view should be drawn maximized.
     * @param view_p OwView that requests to be shown maximized
     */
    protected void setMaximizeView(OwView view_p)
    {
        // default call parent
        OwView parentView = getParent();

        if (parentView != null)
        {
            parentView.setMaximizeView(view_p);
        }
    }

    /** overridable function to retrieve the view that is shown maximized if any
     * @return view_p OwView that is shown maximized, null otherwise
     */
    protected OwView getMaximizeView()
    {
        // default call parent
        OwView parentView = getParent();

        if (parentView != null)
        {
            return parentView.getMaximizeView();
        }
        else
        {
            return null;
        }
    }

    /** maximize the view */
    public void showMaximized()
    {
        setMaximizeView(this);
        m_fMinimized = false;
    }

    /** minimize the view*/
    public void showMinimized()
    {
        if (isShowMaximized())
        {
            setMaximizeView(null);
        }

        m_fMinimized = true;
    }

    /** show the view normal */
    public void showNormal()
    {
        if (isShowMaximized())
        {
            setMaximizeView(null);
        }

        m_fMinimized = false;
    }

    /** checks if the view is shown maximized 
     * @return true, if view is maximized, false otherwise
     */
    public boolean isShowMaximized()
    {
        return (this == getMaximizeView());
    }

    /** checks if the view is shown minimized 
     * @return true, if view is maximized, false otherwise
     */
    public boolean isShowMinimized()
    {
        return m_fMinimized;
    }

    /** checks if the view is shown normal 
     * @return true, if view is maximized, false otherwise
     */
    public boolean isShowNormal()
    {
        return ((!isShowMinimized()) && (!isShowMaximized()));
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ui.OwUpdateTarget#onUpdate(com.wewebu.ow.server.ui.OwEventTarget, int, java.lang.Object)
     */
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        // default do nothing
    }

    /** activate this view, i.e. make it visible by navigating to it and all its parents*/
    public void activate() throws Exception
    {
        // make sure no view is being displayed maximized
        setMaximizeView(null);

        // recurse the parents until a navigation view is found
        OwView parent = getParent();
        if (parent != null)
        {
            // Activate Parent as well
            parent.activate();

            if (parent instanceof OwNavigationView)
            {
                // === parent is navigation view so we can navigate to it.
                ((OwNavigationView) parent).navigate(getID());
            }
        }
    }

    /** add a view and initialize it
     * @param view_p View to add
     * @param strName_p Name / ID of the view, can be null
     */
    public void addView(OwView view_p, String strName_p) throws Exception
    {
        // set document before setContext, so we have the reference when Init is called in setContext.
        if (m_Document != null)
        {
            view_p.setDocument(m_Document);
        }

        view_p.setParent(this);

        // set context, init view
        view_p.attach(getContext(), strName_p);

        // add to inner view list
        getViewList().add(view_p);
    }

    /** 
     * Sets the reference to the document.
     * <p>For safety reasons, the {@link OwDocument} object can be set only once for a view.</p>
     * @param doc_p {@link OwDocument} instance to attach to.
     */
    public void setDocument(OwDocument doc_p)
    {
        if (null != m_Document)
        {
            return;
        }

        m_Document = doc_p;

        // attach view to document as well
        doc_p.attachView(this);

        // Initialize children as well
        Iterator it = getIterator();
        while (it.hasNext())
        {
            OwView View = (OwView) it.next();
            View.setDocument(doc_p);
        }
    }

    /** remove view and all subviews from context
     */
    public void detach()
    {
        super.detach();

        // remove from document as well
        if (m_Document != null)
        {
            m_Document.detachView(this);
        }

        // detach children as well
        Iterator it = getIterator();
        while (it.hasNext())
        {
            OwView View = (OwView) it.next();
            View.detach();
        }
    }

    /** gets the reference to the document
     * @return document instance
     */
    public OwDocument getDocument()
    {
        return m_Document;
    }

    /** render the view and all contained views.
     *  This function is always called in advance to onRender. It is used to do special work around rendering, e.g. set flags.
     * NOTE: Do not inherit this function, nor create HTML code here, this should be done with onRender.
     * @param w_p Writer object to write HTML to
     */
    public final void render(Writer w_p) throws Exception
    {
        // === render this view
        // Dump debug info
        w_p.write("\n<!-- Start View [ID: ");
        w_p.write(getID());
        w_p.write("; Class: ");
        w_p.write(getClass().getName());
        w_p.write("] -->\n");

        // auto begin form tag
        if ((m_externalFormEventTarget == this) && (usesFormWithAttributes() != null))
        {
            String formName = getFormName();
            if (formName == null)
            {
                formName = "null";
                LOG.warn("null value for form name in = " + this.getClass() + " externalForm = " + this.m_externalFormEventTarget);
            }
            w_p.write("<form method=\"post\" name=\"");
            w_p.write(formName);
            w_p.write("\" id=\"");
            w_p.write(formName);
            w_p.write("\" ");
            w_p.write(usesFormWithAttributes());
            w_p.write(">");
            // call overloaded render function
            onRender(w_p);
            w_p.write("</form>");
        }
        else
        {
            // call overloaded render function
            onRender(w_p);
        }

        // Dump debug info
        w_p.write("\n<!-- End View [ID: ");
        w_p.write(getID());
        w_p.write("; Class: ");
        w_p.write(getClass().getName());
        w_p.write("] -->\n");
    }

    /** called when the view should create its HTML content to be displayed
     * @param w_p Writer object to write HTML to
     */
    protected void onRender(Writer w_p) throws Exception
    {
        // === default, just render all children.
        // render children
        Iterator it = getIterator();
        while (it.hasNext())
        {
            OwView View = (OwView) it.next();
            View.render(w_p);
        }
    }

    /** render only a region in the view, used by derived classes
     *
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        throw new OwInvalidOperationException("OwView.renderRegion: Implement renderRegion in derived class");
    }

    /** render the region and return a result String
     * useful function when rendering regions and need the HTML in other functions
     *
     * @param iRegion_p ID of the region to render
     *
     * @return String with regions HTML 
     */
    public String getRenderedRegion(int iRegion_p) throws Exception
    {
        java.io.StringWriter buf = new java.io.StringWriter();
        renderRegion(buf, iRegion_p);
        return buf.toString();
    }

    /** determine if region exists
     *
     * @param iRegion_p ID of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isRegion(int iRegion_p) throws Exception
    {
        // override if regions are defined
        return false;
    }

    /** determine if region exists
     *
     * @param strRegion_p name of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isNamedRegion(String strRegion_p) throws Exception
    {
        // override if regions are defined
        return false;
    }

    /** init the target after the context is set.
     */
    protected void init() throws Exception
    {
        // default does nothing
    }

    /** activate the target from a navigation module. Called when menu item was pressed for this target.
     *
     * @param iIndex_p int tab iIndex of Navigation 
     * @param oReason_p User Object which was submitted when target was attached to the navigation module
     */
    protected void onActivate(int iIndex_p, Object oReason_p) throws Exception
    {
        // default does nothing
    }

    /**  Include the specified URL writing its output to the current Writer object.
     *   The view reference is set in the Request Attribute CURRENT_MODULE_KEY, 
     *   to access the View in the JSP page.
     *   This function can be called from the render method
     *
     *   @param  path_p - The path to the URL to include.
     *   @param  w_p    - The Writer which will be flushed prior to the include.
     *   @throws Exception
     */
    public void serverSideInclude(String path_p, java.io.Writer w_p) throws Exception
    {
        // set new module reference and save previous module reference
        Object previous = setRequestAttribute(CURRENT_MODULE_KEY, this);

        // do a server side include of the given JSP page
        getContext().serverSideInclude(path_p, w_p);

        // set to previous module reference
        setRequestAttribute(CURRENT_MODULE_KEY, previous);
    }

    /**  Set a request attribute on the request that this controller is referencing.
    *
    *
    *   @param   key_p   - The attribute key.
    *   @param   value_p - The attribute value.
    *   @return  The previous value for the key, if it exists, otherwise null.
    */
    private Object setRequestAttribute(String key_p, Object value_p)
    {
        Object previousObject = getContext().getHttpRequest().getAttribute(key_p);

        if (value_p != null)
        {
            getContext().getHttpRequest().setAttribute(key_p, value_p);
        }
        else
        {
            getContext().getHttpRequest().removeAttribute(key_p);
        }

        return (previousObject);
    }

    /**  Include the specified URL writing its output to the current Writer object. Appends the current design directory to the JSP path
     *   The view reference is set in the Request Attribute CURRENT_MODULE_KEY, 
     *   to access the View in the JSP page.
     *   This function can be called from the render method
     *
     *   @param  path_p - The path to the URL to include. (should not start with slash "/")
     *   @param  w_p    - The Writer which will be flushed prior to the include.
     *   @throws Exception
     */
    public void serverSideDesignInclude(String path_p, java.io.Writer w_p) throws Exception
    {
        serverSideInclude(getContext().getDesignDir() + "/" + path_p, w_p);
    }

    /** generic function, which can render HTML forms.
     *  Upon each occurrence of a placeholder (defined as {#...#}) it calls the onRenderFormularPlaceholder function.
     *
     *  @param w_p Writer object to write HTML to
     *  @param strHTMLFormular_p String HTML Formular with {#...#} placeholders
     */
    protected void renderHTMLFormular(Writer w_p, String strHTMLFormular_p) throws Exception
    {
        // === parse the HTML form an render specific content in the placeholders.
        // start and end index of current placeholder
        int iStart = -1;
        int iEnd = 0;

        while (-1 != (iStart = strHTMLFormular_p.indexOf(FORMULAR_PLACEHOLDER_START_DELIMITER, iEnd)))
        {
            // === found placeholder in line
            // === render string before placeholder
            w_p.write(strHTMLFormular_p.substring(iEnd, iStart));

            iEnd = strHTMLFormular_p.indexOf(FORMULAR_PLACEHOLDER_END_DELIMITER, iEnd + 1);
            if (iEnd == -1)
            {
                ///////////////////////////////
                // TODO: Count lines and characters to give more detail error message

                throw new Exception("Formular parser error. Missing placeholder end tag.!");
            }

            // === render views in placeholder
            // get placeholder
            String strPlaceholder = strHTMLFormular_p.substring(iStart + FORMULAR_PLACEHOLDER_START_DELIMITER.length(), iEnd);
            // call substitute function, to replace placeholder with specific content
            renderNamedRegion(w_p, strPlaceholder);

            // advance over the end of the delimiter
            iEnd += FORMULAR_PLACEHOLDER_END_DELIMITER.length();
        }

        // === render rest of the template
        w_p.write(strHTMLFormular_p.substring(iEnd, strHTMLFormular_p.length()));
    }

    /** render only a region in the view, used by derived classes
     *
     * @param w_p Writer object to write HTML to
     * @param strRegion_p named region to render
     */
    public void renderNamedRegion(Writer w_p, String strRegion_p) throws Exception
    {
        // must be overloaded in order to use the form renderer.
        throw new OwInvalidOperationException("OwView.renderNamedRegion: Implement renderRegion in derived class.");
    }

    /** render the region and return a result String
     * useful function when rendering regions and need the HTML in other functions
     *
     * @param strRegion_p name of the region to render
     *
     * @return String with regions HTML 
     */
    public String getRenderedNamedRegion(String strRegion_p) throws Exception
    {
        java.io.StringWriter buf = new java.io.StringWriter();
        renderNamedRegion(buf, strRegion_p);
        return buf.toString();
    }

    /** check if the form target is external
     * 
     * @return boolean true if the form target is external
     */
    public boolean isFormTargetExternal()
    {
        return m_externalFormEventTarget != this;
    }

    /** get the target, that is used for form date and renders form
     */
    public OwEventTarget getFormTarget()
    {
        return m_externalFormEventTarget;
    }

    /** override the internal OwEditable with an external one,
     *  must be called BEFORE view is attached.
     *
     * <br>NOTE:    By default, view will render its own form,
     *              unless you call setEditable
     *              When setting an external OwEditable,
     *              the view will not render a own form,
     *              but use the form name of the given OwEditable.
     *
     *              ==> Several form views can update each other.
     *
     * @param eventtarget_p OwEventTarget to be used, replaces the internal OwEditable
     */
    public void setExternalFormTarget(OwEventTarget eventtarget_p) throws Exception
    {
        m_externalFormEventTarget = eventtarget_p;
    }

    /** the external form target if set
     * @see #setExternalFormTarget(OwEventTarget)
     * @return an {@link OwEventTarget}
     */
    public OwEventTarget getExternalFormEventTarget()
    {
        return m_externalFormEventTarget;
    }

    /** to get additional form attributes used for the form
     *  override if your view needs a form. Base class will then render a form automatically
     *
     * @return String with form attributes, or null if view does not render a form
     */
    protected String usesFormWithAttributes()
    {
        // default does not use form
        return null;
    }

    /** get the form used for the template edit fields
     *  Returns the internal render form or the external one
     *  if you called setFormName.
     *
     * @return String form name
     */
    public String getFormName()
    {
        if (this == m_externalFormEventTarget)
        {
            return "FORM_" + Integer.toHexString(hashCode());
        }
        else
        {
            return m_externalFormEventTarget.getFormName();
        }
    }

    public String getBreadcrumbPart()
    {
        return getTitle();
    }

}