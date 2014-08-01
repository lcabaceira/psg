package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwVirtualLinkPropertyClasses;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwStandardFieldColumnInfo;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.ui.OwEventTarget;
import com.wewebu.ow.server.ui.OwLayout;

/**
 *<p>
 * Displays {@link OwObjectLink}s using {@link OwSplitObjectListView}.
 * For each relation specified by the corresponding {@link OwObjectLinksDocument} a  
 * separate object list is displayed as a split (see {@link OwSplitObjectListView}) in the {@link #LINKS_REGION}.
 * A filter is displayed using the {@link #LINKS_FILTER_REGION} handling in sub classes.
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
 *@since 4.1.1.0
 */
public abstract class OwObjectLinksView extends OwLayout
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwObjectLinksView.class);

    public static final int LINKS_REGION = 1;
    public static final int LINKS_FILTER_REGION = 2;

    private boolean noLinks;

    public OwObjectLinksView(OwObjectLinksDocument document)
    {
        setDocument(document);
    }

    @Override
    public OwObjectLinksDocument getDocument()
    {
        return (OwObjectLinksDocument) super.getDocument();
    }

    protected Collection<OwFieldColumnInfo> createColumnInfo(Collection<String> propertyList_p) throws Exception
    {
        List<OwFieldColumnInfo> columnInfoList = new LinkedList<OwFieldColumnInfo>();

        Iterator<String> it = propertyList_p.iterator();

        while (it.hasNext())
        {
            String strPropertyName = it.next();

            OwFieldDefinition fielddef = null;
            try
            {
                fielddef = ((OwMainAppContext) getContext()).getNetwork().getFieldDefinition(strPropertyName, null);
                columnInfoList.add(new OwStandardFieldColumnInfo(fielddef));
            }
            catch (OwObjectNotFoundException e)
            {
                LOG.warn("Property could not be resolved for OwObjectHistoryView list: " + strPropertyName, e);
                continue;
            }
        }

        return columnInfoList;
    }

    protected void init() throws Exception
    {
        super.init();

    }

    /**
     * 
     * @return true if no object links are to be displayed by this view.
     *         false otherwise.
     */
    public boolean isEmpty()
    {
        return noLinks;
    }

    /**
     * Refreshes the contents of this view for the give object link collection array. 
     *            Each element in the array contains the collection of objects for the relation split 
     *            at the same index defined in the document of this view 
     * @param splitLinks
     * @throws Exception
     */
    protected void refresh(OwObjectCollection[] splitLinks) throws Exception
    {
        noLinks = true;
        for (OwObjectCollection links : splitLinks)
        {
            if (links != null && !links.isEmpty())
            {
                noLinks = false;
                break;
            }
        }
    }

    @Override
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        super.renderRegion(w_p, iRegion_p);
    }

    @Override
    public void onUpdate(OwEventTarget caller_p, int iCode_p, Object param_p) throws Exception
    {
        OwObjectLinksDocument document = getDocument();
        refresh(document.getSplits());
    }

    protected void onRender(Writer w_p) throws Exception
    {
        serverSideDesignInclude("dmsdialogs/OwObjectLinksView.jsp", w_p);
    }

    protected List<OwDocumentFunction> getDocumentFunctions() throws OwException
    {
        List<OwDocumentFunction> functions = new LinkedList<OwDocumentFunction>();

        OwObjectLinksDocument document = getDocument();
        List<String> functionIDs = document.getLinkFunctionsIDs();

        if (functionIDs != null)
        {

            for (String id : functionIDs)
            {
                // only add to array if it is an allowed function
                if (((OwMainAppContext) getContext()).getConfiguration().isDocumentFunctionAllowed(id))
                {
                    OwDocumentFunction function;
                    try
                    {
                        function = ((OwMainAppContext) getContext()).getConfiguration().getDocumentFunction(id);
                        functions.add(function);
                    }
                    catch (OwException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new OwConfigurationException("Error creating link functions.", e);
                    }
                }
            }
        }

        return functions;
    }

    /**
     * 
     * @return an {@link OwSplitObjectListView} with the document defined splits and corresponding property column  header information.
     * @throws Exception
     */
    protected OwSplitObjectListView createSplitView() throws Exception
    {
        OwObjectLinksDocument document = getDocument();
        OwObjectLinkRelation[] relations = document.getRelations();

        String[] splitNames = new String[relations.length];
        Collection<OwFieldColumnInfo>[] propertyColumnInfos = new Collection[relations.length];
        for (int i = 0; i < relations.length; i++)
        {
            if (relations[i] != OwObjectLinkRelation.NONE)
            {
                splitNames[i] = relations[i].getDisplayName().getString(getContext().getLocale());
                propertyColumnInfos[i] = createRelationColumnInfo(relations[i]);
            }
            else
            {
                splitNames[i] = OwSplitObjectListDocument.NO_NAME;
                propertyColumnInfos[i] = createColumnInfo(Arrays.asList(new String[] { OwResource.m_ClassDescriptionPropertyClass.getClassName() }));
            }

        }

        return new OwSplitObjectListView(createSplitObjectListDocument(splitNames, propertyColumnInfos, getDocumentFunctions()));
    }

    /**(overridable)
     * Factory to create OwSplitObjectListDocument for internal OwSplitObjectListView .
     * @param splitNames String array of Labels (display names) for the list view(s)
     * @param propertyColumnInfos Collection of FieldColumnInfo objects
     * @param documentFunctions List of document functions (can be null)
     * @return OwSplitObjectListDocument
     */
    protected OwSplitObjectListDocument createSplitObjectListDocument(String[] splitNames, Collection<OwFieldColumnInfo>[] propertyColumnInfos, List<OwDocumentFunction> documentFunctions)
    {
        return new OwSplitObjectListDocument(splitNames, propertyColumnInfos, documentFunctions);
    }

    /**(overridable)
     * Create relation dependent columns to display.
     * <p>
     *  Attention: If columns are defined for any relation kind, the same columns will be displayed.
     * </p>
     * @param relation OwObjectLinkRelation objects which will be rendered.
     * @return Collection of OwFieldColumnInfo
     * @throws Exception
     */
    protected Collection<OwFieldColumnInfo> createRelationColumnInfo(OwObjectLinkRelation relation) throws Exception
    {
        Collection<OwFieldColumnInfo> propertyColumnInfo;
        if (getDocument().getColumnNames() != null)
        {
            propertyColumnInfo = createColumnInfo(getDocument().getColumnNames());
        }
        else
        {
            if (OwObjectLinkRelation.INBOUND == relation)
            {
                propertyColumnInfo = createColumnInfo(Arrays.asList(new String[] { OwVirtualLinkPropertyClasses.LINK_SOURCE.getClassName(), OwResource.m_ClassDescriptionPropertyClass.getClassName() }));
            }
            else if (OwObjectLinkRelation.OUTBOUND == relation)
            {
                propertyColumnInfo = createColumnInfo(Arrays.asList(new String[] { OwVirtualLinkPropertyClasses.LINK_TARGET.getClassName(), OwResource.m_ClassDescriptionPropertyClass.getClassName() }));
            }
            else
            {
                propertyColumnInfo = createColumnInfo(Arrays.asList(new String[] { OwVirtualLinkPropertyClasses.LINK_SOURCE.getClassName(), OwVirtualLinkPropertyClasses.LINK_TARGET.getClassName(),
                        OwResource.m_ClassDescriptionPropertyClass.getClassName() }));
            }
        }
        return propertyColumnInfo;
    }

    /**
     * 
     * @return filter region display name
     */
    public String getFilterDisplayName()
    {
        return "";
    }

}