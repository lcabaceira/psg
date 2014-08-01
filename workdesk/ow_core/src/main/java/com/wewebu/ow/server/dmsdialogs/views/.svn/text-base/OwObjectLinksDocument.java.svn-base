package com.wewebu.ow.server.dmsdialogs.views;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.ui.OwDocument;

/**
 *<p>
 * Document class of {@link OwObjectLinksView}. 
 * Defines the links and the splits that will be displayed by the view.
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
public class OwObjectLinksDocument extends OwDocument
{
    private OwObjectLinkRelation[] relations;
    private Collection<String> fixedFilterClassNames;
    private OwObject object;
    private List<String> documentFunctionIds;
    private List<String> columNames;

    /**
     * Constructor
     * 
     * @param relation  relation used to split the lists by. The {@link OwObjectLinkRelation#NONE} 
     *                  is interpreted as any relation.Resulted link splits will be displayed preserving
     *                  the order in this array. 
     */
    public OwObjectLinksDocument(OwObjectLinkRelation relation)
    {
        this(relation, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    /**
     * Constructor
     * 
     * @param relation relation used to split the lists by. The {@link OwObjectLinkRelation#NONE} 
     *                 is interpreted as any relation.Resulted link splits will be displayed preserving
     *                 the order in this array. 
     * @param filterClassNames link object class name of object links that will be displayed. 
     *                         If null all object links will be displayed regardless of their class.
     * @param documentFunctionIds document function IDs to be associated with the link object list entries  
     */
    public OwObjectLinksDocument(OwObjectLinkRelation relation, Collection<String> filterClassNames, List<String> documentFunctionIds)
    {
        switch (relation)
        {
            case INBOUND:
                this.relations = new OwObjectLinkRelation[] { OwObjectLinkRelation.INBOUND };
                break;
            case OUTBOUND:
                this.relations = new OwObjectLinkRelation[] { OwObjectLinkRelation.OUTBOUND };
                break;
            case BOTH:
                this.relations = new OwObjectLinkRelation[] { OwObjectLinkRelation.INBOUND, OwObjectLinkRelation.OUTBOUND };
                break;

            default:
                this.relations = new OwObjectLinkRelation[] { OwObjectLinkRelation.NONE };
                break;
        }

        this.fixedFilterClassNames = filterClassNames;
        this.documentFunctionIds = documentFunctionIds;
    }

    protected boolean filterOut(OwObjectLink link)
    {
        return fixedFilterClassNames != null && !fixedFilterClassNames.isEmpty() && !fixedFilterClassNames.contains(link.getClassName());
    }

    public OwObject getObject()
    {
        return object;
    }

    protected OwObjectCollection getLinks() throws OwException
    {
        Collection<String> propertyNames = null;
        OwSort sort = null;
        int maxSize = Integer.MAX_VALUE / 2;
        int versionSelection = 0;
        OwSearchNode filterCriteria = null;
        OwObjectCollection links = null;
        try
        {
            int[] linkTypes = new int[] { OwObjectReference.OBJECT_TYPE_LINK };
            if (object.hasChilds(linkTypes, versionSelection))
            {
                links = object.getChilds(linkTypes, propertyNames, sort, maxSize, versionSelection, filterCriteria);
            }
            return links;
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Could not retrieve object links.", e);
        }
    }

    public OwObjectLinkRelation[] getRelations()
    {
        return relations;
    }

    public void setObject(OwObject object) throws OwException
    {
        this.object = object;
        updateLinkViews();
    }

    protected void updateLinkViews() throws OwException
    {
        try
        {
            update(this, OwUpdateCodes.UPDATE_DEFAULT, null);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException("Error udating views.", e);
        }
    }

    public List<String> getLinkFunctionsIDs()
    {
        return documentFunctionIds;
    }

    /**
     * Set a list of column names, or clear a list (provide null).
     * @param columnNames List of Strings or null
     */
    public void setColumnNames(List<String> columnNames)
    {
        this.columNames = columnNames;
    }

    /**
     * Will return a list of column names, which should be displayed.
     * @return List of String's, or null if not set
     */
    public List<String> getColumnNames()
    {
        return this.columNames;
    }

    /**(overridable)
     * Returns an array of OwObjectCollection's containing objects for specific relation direction.<br />
     * By default the array will have the same order like the relation directions array of {@link #getRelations()}.
     * @return OwObjectCollection array containing OwObjectLink instances
     * @throws OwException
     */
    public OwObjectCollection[] getSplits() throws OwException
    {
        OwObjectCollection[] splits = new OwObjectCollection[getRelations().length];

        OwObjectCollection links = getLinks();
        if (links != null)
        {
            Iterator<?> objectsIt = links.iterator();
            while (objectsIt.hasNext())
            {
                OwObjectLink link = (OwObjectLink) objectsIt.next();
                if (!filterOut(link))
                {
                    for (int i = 0; i < splits.length; i++)
                    {
                        OwObjectLinkRelation relation = getRelations()[i];
                        if (OwObjectLinkRelation.NONE == relation || relation.sameDirection(link, getObject()))
                        {
                            if (splits[i] == null)
                            {
                                splits[i] = new OwStandardObjectCollection();
                            }
                            splits[i].add(link);
                        }
                    }
                }

            }
        }

        return splits;
    }
}
