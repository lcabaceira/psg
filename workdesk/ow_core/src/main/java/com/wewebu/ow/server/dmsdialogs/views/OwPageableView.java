package com.wewebu.ow.server.dmsdialogs.views;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.ui.OwAppContext;
import com.wewebu.ow.server.ui.OwBaseView;

/**
 *<p>
 * Base interface for the views that intends to use different paging components.
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
 *@since 2.5.2.0
 */
public interface OwPageableView extends OwBaseView
{
    /** display absolute page of objects */
    public void onPageAbsolut(HttpServletRequest request_p) throws Exception;

    /** display next page of objects */
    public void onPageNext(HttpServletRequest request_p) throws Exception;

    /** display previous page of objects */
    public void onPagePrev(HttpServletRequest request_p) throws Exception;

    /** 
     * Retrieve number of available pages:
     * <ul>
     * <li> pageCount > 0: available pages</li>
     * <li> pageCount = 0: no pages/elements available</li>
     * <li> pageCount < 0: unknown amount of pages</li>
     * </ul> 
     * @return integer number of pages (or -1 if unknown)
     */
    public int getPageCount();

    /**
     * Get the current page, will not exceed the {@link #getPageCount()}.
     * <p>
     * A zero based page representation, which will go up to <code>{@link #getPageCount()} - 1</code>
     * </p>
     * @return integer representing current page number
     * @throws Exception
     */
    public int getCurrentPage() throws Exception;

    /** 
     * get the context reference
     * @return OwAppContext Application context
     */
    public OwAppContext getContext();

    /** 
     * Check if there is a previous page
     * @return boolean true = there is a previous page, pagePrev is possible
     */
    public boolean canPagePrev();

    /**
     * Check if there is a next page
     * @return boolean true = there is a next page, pageNext is possible
     */
    public boolean canPageNext() throws Exception;

    /** 
     * Get total number of items.
     * <ul>
     *  <li>count >= 0: defined/known amount of items</li>
     *  <li>count = -1: unknown amount of items</li>
     * </ul> 
     * @return integer - the number of items
     */
    public int getCount();

    /**
     * Get the previous page URL.
     * @return the page previous URL
     */
    public String getPagePrevEventURL();

    /**
     * Get the next page URL.
     * @return the next page URL
     */
    public String getPageNextEventURL();

    /**
     * Check if the current view must show the paging component.
     * @return <code>true</code> if the current view must show the paging component.
     */
    public boolean hasPaging();

    /**
     * Get URL for a given page number
     * @param aditionalParameters_p - query parameters in a http link
     * @return the URL
     */
    public String getPageAbsolutEventURL(String aditionalParameters_p);

    /**
     * Create the paging component, according with the configuration setting. In case 
     * that no paging element is set, the direct input page selector component is used.
     * @return an instance of OwPageSelectorComponent. 
     */
    public OwPageSelectorComponent createPageSelector() throws Exception;

}