package com.wewebu.ow.server.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * EventTarget Base Class. Events are generated through requests and dispatched through the targets.
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
public abstract class OwEventTarget
{
    /** reference to the app context, all views belong to one context. */
    private OwAppContext m_Context = null;

    /** ID (handle of the target) each target has a unique ID */
    private String m_strTargetID = null;

    /** init the target after the context is set.
     */
    protected abstract void init() throws Exception;

    /** attach the event target to the context
    * @param context_p Application context to attach to
    * @param strName_p Name / ID under which the Target gets registered
    * @throws Exception
    */
    public void attach(OwAppContext context_p, String strName_p) throws Exception
    {
        m_Context = context_p;

        m_strTargetID = context_p.registerTarget(this, strName_p);

        init();
    }

    /** 
     * register the event target to receive onRequest events
     * the target must already be registered
     * @throws OwException if registration fails
     */
    public void enableRequestListener() throws OwException
    {
        m_Context.registerRequestTarget(this);
    }

    /** remove from request listener registry current event target,
     *  the target must be already registered as event and request
     *  listener.
     *  @throws OwException if de-register fails
     */
    public void disableRequestListener() throws OwException
    {
        m_Context.unregisterRequestTarget(this);
    }

    /** get the context reference
     * @return OwAppContext Application context
     */
    public OwAppContext getContext()
    {
        return m_Context;
    }

    /** remove target*/
    public void detach()
    {
        // remove this from context
        getContext().removeTarget(this);
    }

    /** get the ID of the target, every target has its unique id, 
     *  used to dispatch requests among the targets in a context.
     *  @return String representing current event target Id
     */
    public String getID()
    {
        return m_strTargetID;
    }

    /** get a URL for the requested event
     *
     * @param strEventName_p Function name to be called upon event fired
     * @param strAdditionalParameters_p additional query string with parameters.
     * @return String representing the event URL
     */
    public String getEventURL(String strEventName_p, String strAdditionalParameters_p)
    {
        return m_Context.getEventURL(this, strEventName_p, strAdditionalParameters_p);
    }

    /** get a URL for the requested AJAX event without rendering
    *
    * @param strEventName_p Function name to be called upon event fired
    * @param strAdditionalParameters_p additional query string with parameters.
    * @return String representing an AJAX URL
    */
    public String getAjaxEventURL(String strEventName_p, String strAdditionalParameters_p)
    {
        return m_Context.getAjaxEventURL(this, strEventName_p, strAdditionalParameters_p);
    }

    /** get a URL for the requested event sending the form data defined in the event target with getFormName()
     *
     * @param strEventName_p Function name to be called upon event fired
     * @param strAdditionalParameters_p additional query string with parameters.
     * @return String URL for current form
     */
    public String getFormEventURL(String strEventName_p, String strAdditionalParameters_p)
    {
        return m_Context.getFormEventURL(this, strEventName_p, strAdditionalParameters_p, getFormName());
    }

    /** get a script function for the requested event sending the form data defined in the event target with getFormName()
    *
    * @param strEventName_p Function name to be called upon event fired
    * @param strAdditionalParameters_p additional query string with parameters.
    * @return String function for form submit
    */
    public String getFormEventFunction(String strEventName_p, String strAdditionalParameters_p)
    {
        return m_Context.getFormEventFunction(this, strEventName_p, strAdditionalParameters_p, getFormName());
    }

    /** Get the form used for the edit fields.<br />
     * <b>By default return null, should be overridden by derived classes</b>
     * @return String form name, or null if not attached to form
     */
    public String getFormName()
    {
        // target has no form defined by default, override in derived class
        return null;
    }

    /** override the internal form with an external one,
     *  must be called BEFORE event target is attached.
     *
     * <br>NOTE:    By default, view will render its own form,
     *              unless you call setFormTarget
     *              When setting an external form,
     *              the view will not render a own form,
     *              but use the form name of the given OwEditable.
     *
     *              ==> Several form-views can update each other.
     *
     * @param eventtarget_p OwEventTarget to be used for form data
     * @throws Exception
     */
    public void setExternalFormTarget(OwEventTarget eventtarget_p) throws Exception
    {
        throw new OwInvalidOperationException("OwEventTarget.setExternalFormTarget: Override setFormTarget in your eventtarget.");
    }

    /** update the target after a form event, so it can set its form fields
     *
     * @param request_p HttpServletRequest
     * @param fSave_p boolean true = save the changes of the form data, false = just update the form data, but do not save
     *
     * @return true = field data was valid, false = field data was invalid
     * @throws Exception
     */
    public boolean updateExternalFormTarget(javax.servlet.http.HttpServletRequest request_p, boolean fSave_p) throws Exception
    {
        return true;
    }

    /** called before a form event is caught.
     *  Method gets called before the event handler to inform neighbor controls / views
     *
     * @param request_p HttpServletRequest
     * @throws Exception if {@link #getFormTarget()} update call fails
     */
    public void onFormEvent(javax.servlet.http.HttpServletRequest request_p) throws Exception
    {
        // update all neighbor fields
        if (null != getFormTarget())
        {
            getFormTarget().updateExternalFormTarget(request_p, false);
        }
    }

    /** get the target, that is used for form data and renders form
     * @return OwEventTarget
     */
    public OwEventTarget getFormTarget()
    {
        return null;
    }

    /** overridable to receive request notifications
     *  to receive onRequest, the target must be registered with OwAppContext.registerRequestTarget
     *
     * @param request_p  HttpServletRequest
     * @param response_p HttpServletResponse
     * 
     * @return boolean true = continue with request and render, false = request is already finished, return to client
     * @throws Exception
     */
    public boolean onRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // ignore
        return true;
    }

    /** overridable to receive request notifications from external sources / links
     *
     * @param request_p  HttpServletRequest
     * @param response_p HttpServletResponse
     * @throws Exception
     */
    public void onExternalRequest(HttpServletRequest request_p, HttpServletResponse response_p) throws Exception
    {
        // ignore
    }

}