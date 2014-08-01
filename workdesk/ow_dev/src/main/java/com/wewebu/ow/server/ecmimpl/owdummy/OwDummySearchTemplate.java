package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.Collection;
import java.util.Vector;

import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStandardSearchTemplate;

/**
 *<p>
 * Search Template implementation, parses search template XML nodes and creates a search template.
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
public class OwDummySearchTemplate extends OwStandardSearchTemplate
{

    private Collection m_savedsearches = new Vector();

    private String m_setsavedsearch;

    /** Creates a new instance of m_Search tree
     *
     *  NOTE:   The syntax of the XML Template is compatible with FileNet P8 Search designer.
     *          I.e. this function can read FileNet P8 SearchDesigner created templates.
     *
     * @param obj_p OwObject that contains the search template information
     *
     */
    public OwDummySearchTemplate(OwNetworkContext context_p, OwObject obj_p) throws Exception
    {
        super(context_p, obj_p);

        m_savedsearches.add("Gespeicherte Test Suche 1");
        m_savedsearches.add("Gespeicherte Test Suche 2");
        m_savedsearches.add("Gespeicherte Test Suche 3");
    }

    /** Creates a new instance of OwSearchTemplate 
     * @param xmlSearchTemplateNode_p XML Search Template DOM Node to be wrapped
     * @param strName_p Name of the search
     * @param strResourceName_p name of a resource to use for field definition resolving
     */
    public OwDummySearchTemplate(OwNetworkContext context_p, org.w3c.dom.Node xmlSearchTemplateNode_p, String strName_p, String strResourceName_p) throws Exception
    {
        super(context_p, xmlSearchTemplateNode_p, strName_p, strResourceName_p);

    }

    /** get a collection of saved searches names
     * 
     * @return Collection of String names, or null if nothing is available
     * @throws Exception 
     */
    public Collection getSavedSearches() throws Exception
    {
        return m_savedsearches;
    }

    /** init the search template with a saved search
     * 
     * @param name_p
     */
    public void setSavedSearch(String name_p) throws Exception
    {
        m_setsavedsearch = name_p;
    }

    /** get the name of the current set search, or null if no saved search is set
     * 
     */
    public String getSavedSearch() throws Exception
    {
        return m_setsavedsearch;
    }

    /** save the current search
     * 
     * @param name_p
     */
    public void saveSearch(String name_p) throws Exception
    {
        m_savedsearches.add(name_p);
    }

    /** check if searches can be saved and loaded
     * 
     */
    public boolean canSaveSearch()
    {
        return true;
    }

    /** check if saved searches can be deleted
     * 
     */
    public boolean canDeleteSearch()
    {
        return true;
    }

    /** check if saved searches can be updated
     * 
     */
    public boolean canUpdateSearch()
    {
        return true;
    }

    /** delete the saved search
     * 
     * @param name_p
     */
    public void deleteSavedSearch(String name_p) throws Exception
    {
        m_savedsearches.remove(name_p);

        if (name_p.equals(m_setsavedsearch))
        {
            m_setsavedsearch = null;
        }
    }

    public int hashCode()
    {
        return m_object.hashCode();
    }

    public boolean equals(Object obj_p)
    {
        if (obj_p == this)
        {
            return true;
        }

        if (!(obj_p instanceof OwDummySearchTemplate))
        {
            return false;
        }

        OwDummySearchTemplate o2 = (OwDummySearchTemplate) obj_p;
        //boolean saved = false;
        if (m_object != null && m_object.equals(o2.m_object))
        {
            return true;
        }
        //        try
        //        {
        //            if (o2.getSavedSearch() == m_setsavedsearch)
        //                saved = true;
        //        }
        //        catch (Exception e)
        //        {
        //
        //        }

        return false;

    }
}