package com.wewebu.ow.server.ui;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Layout Class, which can add further Views to regions and act as a container.
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
public abstract class OwLayout extends OwView
{
    public static interface OwLayoutViewRefrence extends OwBaseView
    {
        boolean isEnabled();
    }

    /** map of the contained views, keyed by Region Name */
    protected HashMap m_Regions = new HashMap();

    /** adds a region out of the given view with a name key to the layout
     *  NOTE: Does not attach the view. In order to attach a view use the addView method with the OwView parameter.
     *
     * @param view_p OwBaseView to add
     * @param iRegion_p ID of the region
     * @param iDispatchRegion_p ID of the sub region i.e. this will call renderRegion(iDispatchRegion_p) instead of render(...)
     */
    public void addRegion(OwBaseView view_p, int iRegion_p, int iDispatchRegion_p) throws Exception
    {
        // === add region to the map
        m_Regions.put(Integer.valueOf(iRegion_p), new OwDispatchRegion(view_p, iDispatchRegion_p));
    }

    /** adds a view with a name key to the layout and attaches the view
     *
     * @param view_p the view to add
     * @param strName_p Name / ID of the view, can be null
     * @param iRegion_p ID of the region
     */
    public void addView(OwView view_p, int iRegion_p, String strName_p) throws Exception
    {
        // set document before setContext, so we have the reference when Init is called in setContext.
        if (getDocument() != null)
        {
            view_p.setDocument(getDocument());
        }

        // set context to view, also initializes the view
        view_p.attach(getContext(), strName_p);

        view_p.setParent(this);

        // === add view to the map
        m_Regions.put(Integer.valueOf(iRegion_p), view_p);
    }

    /** add a view and initialize it
      *
      * @param view_p View to add
      * @param strName_p Name / ID of the view, can be null
      */
    public void addView(OwView view_p, String strName_p) throws Exception
    {
        throw new Exception("addView without Region not allowed in OwLayout, call addView(OwView View_p,int iRegion_p,String strName_p) instead.");
    }

    /** adds a OwBaseView with a name key to the layout, but does not attach it. The must have been attached already.
     *  NOTE: Does not attach the view. In order to attach a view use the addView method with the OwView parameter.
     *
     * @param view_p the view to add
     * @param iRegion_p ID of the region
     */
    public void addViewReference(OwBaseView view_p, int iRegion_p) throws Exception
    {
        // === add view 
        m_Regions.put(Integer.valueOf(iRegion_p), view_p);
    }

    /** get a iterator object for the child views
     * @return iterator for the child OwBaseView objects
     */
    public Iterator getIterator()
    {
        List retList = new LinkedList();

        // detach children as well
        Iterator it = m_Regions.values().iterator();
        while (it.hasNext())
        {
            try
            {
                OwView View = (OwView) it.next();
                retList.add(View);
            }
            catch (ClassCastException e)
            {
                // ignore
            }
        }

        return retList.iterator();
    };

    /** get the registered view reference
     * 
     * @param iRegion_p ID of the region to render
     * 
     * @return OwView
     * @throws OwObjectNotFoundException 
     * */
    public OwView getViewRegion(int iRegion_p) throws OwObjectNotFoundException
    {
        try
        {
            return (OwView) m_Regions.get(Integer.valueOf(iRegion_p));
        }
        catch (Exception e)
        {
            throw new OwObjectNotFoundException("OwLayout.getViewRegion: Region = " + String.valueOf(iRegion_p), e);
        }
    }

    /** determine if region exists
     *
     * @param iRegion_p ID of the region to render
     * @return true if region contains anything and should be rendered
     */
    public boolean isRegion(int iRegion_p)
    {
        OwBaseView region = (OwBaseView) m_Regions.get(Integer.valueOf(iRegion_p));
        if (region instanceof OwLayoutViewRefrence)
        {
            OwLayoutViewRefrence reference = (OwLayoutViewRefrence) region;
            return reference.isEnabled();
        }
        return (region != null);
    }

    /** checks if the region is shown maximized 
     * @return true, if region is maximized, false otherwise
     */
    public boolean isRegionMaximized(int iRegion_p)
    {
        try
        {
            OwView View = (OwView) m_Regions.get(Integer.valueOf(iRegion_p));
            return View.isShowMaximized();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** checks if the region is shown minimized 
     * @return true, if region is minimized, false otherwise
     */
    public boolean isRegionMinimized(int iRegion_p)
    {
        try
        {
            OwView View = (OwView) m_Regions.get(Integer.valueOf(iRegion_p));
            return View.isShowMinimized();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** checks if the region is shown normal 
     * @return true, if region is normal, false otherwise
     */
    public boolean isRegionNormal(int iRegion_p)
    {
        try
        {
            OwView View = (OwView) m_Regions.get(Integer.valueOf(iRegion_p));
            return View.isShowNormal();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** render only a region in the view, used by derived classes
     *
     * @param w_p Writer object to write HTML to
     * @param strRegion_p named region to render
     */
    public void renderNamedRegion(Writer w_p, String strRegion_p) throws Exception
    {
        renderRegion(w_p, Integer.parseInt(strRegion_p));
    }

    /** render the views of the region
     *
     * @param w_p Writer object to write HTML to
     * @param iRegion_p ID of the region to render
     */
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        OwBaseView View = (OwBaseView) m_Regions.get(Integer.valueOf(iRegion_p));
        if (View != null)
        {
            View.render(w_p);
        }
    }

    /** dispatch region wrapper used by addRegion to dispatch to a region in the attached Renderable
     */
    protected static class OwDispatchRegion implements OwBaseView
    {
        private int m_iDispatchRegion;

        private OwBaseView m_View;

        public OwDispatchRegion(OwBaseView view_p, int iDispatchRegion_p)
        {
            m_View = view_p;
            m_iDispatchRegion = iDispatchRegion_p;
        }

        /** determine if region exists
         *
         * @param strRegion_p name of the region to render
         * @return true if region contains anything and should be rendered
         */
        public boolean isNamedRegion(String strRegion_p) throws Exception
        {
            throw new OwInvalidOperationException("OwLayout$OwDispatchRegion.renderNamedRegion: renderRegion must not be called in Dispatch Regions");
        }

        /** determine if region exists
         *
         * @param iRegion_p ID of the region to render
         * @return true if region contains anything and should be rendered
         */
        public boolean isRegion(int iRegion_p) throws Exception
        {
            // ignore
            return false;
        }

        public void render(Writer w_p) throws Exception
        {
            // dispatch to the region in the attached view
            m_View.renderRegion(w_p, m_iDispatchRegion);
        }

        public String getTitle()
        {
            return m_View == null ? OwBaseView.EMPTY_STRING : m_View.getTitle();
        }

        /** render only a region in the view, used by derived classes
         *
         * @param w_p Writer object to write HTML to
         * @param strRegion_p named region to render
         */
        public void renderNamedRegion(Writer w_p, String strRegion_p) throws Exception
        {
            throw new OwInvalidOperationException("OwLayout$OwDispatchRegion.renderNamedRegion: ...must not be called in Dispatch Regions.");
        }

        public void renderRegion(Writer w_p, int iRegion_p) throws Exception
        {
            throw new OwInvalidOperationException("OwLayout$OwDispatchRegion.renderRegion: ...must not be called in Dispatch Regions.");
        }

        public String getBreadcrumbPart()
        {
            return getTitle();
        }
    }
}