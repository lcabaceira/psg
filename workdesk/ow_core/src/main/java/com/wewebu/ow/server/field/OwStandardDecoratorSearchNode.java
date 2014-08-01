package com.wewebu.ow.server.field;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *<p>
 * OwSearchNode wrapper to implement a decorator pattern.
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
public abstract class OwStandardDecoratorSearchNode extends OwSearchNode
{
    /** get the decorated object, to be implemented be overridden classes */
    public abstract OwSearchNode getWrappedSearchNode();

    /** get the decorated object, to be implemented be overridden classes */
    public abstract OwSearchCriteria createWrappedCriteria(OwSearchCriteria criteria_p);

    public OwSearchCriteria getCriteria()
    {
        return createWrappedCriteria(getWrappedSearchNode().getCriteria());
    }

    public List getCriteriaList(int filter_p)
    {
        List ret = new ArrayList();

        // wrap criteria
        Iterator it = getWrappedSearchNode().getCriteriaList(filter_p).iterator();
        while (it.hasNext())
        {
            OwSearchCriteria criteria = (OwSearchCriteria) it.next();

            ret.add(createWrappedCriteria(criteria));
        }

        return ret;
    }

    public Map getCriteriaMap(int filter_p)
    {
        Map ret = new HashMap();

        // wrapp criteria
        Map map = getWrappedSearchNode().getCriteriaMap(filter_p);

        Iterator it = map.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            OwSearchCriteria criteria = (OwSearchCriteria) entry.getValue();

            ret.put(key, createWrappedCriteria(criteria));
        }

        return ret;
    }

    public void add(OwSearchNode search_p) throws Exception
    {

        getWrappedSearchNode().add(search_p);
    }

    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException("OwStandardDecoratorSearchNode: implement in overridden class");
    }

    public void dump(Writer w_p) throws Exception
    {

        getWrappedSearchNode().dump(w_p);
    }

    public OwSearchNode findSearchNode(int nodeType_p)
    {

        return getWrappedSearchNode().findSearchNode(nodeType_p);
    }

    public List getChilds()
    {

        return getWrappedSearchNode().getChilds();
    }

    public OwFieldProvider getFieldProvider()
    {

        return getWrappedSearchNode().getFieldProvider();
    }

    public int getNodeType()
    {

        return getWrappedSearchNode().getNodeType();
    }

    public int getOperator()
    {

        return getWrappedSearchNode().getOperator();
    }

    public Node getPersistentNode(Document doc_p) throws Exception
    {

        return getWrappedSearchNode().getPersistentNode(doc_p);
    }

    public boolean isCriteriaNode()
    {

        return getWrappedSearchNode().isCriteriaNode();
    }

    public boolean isEmpty()
    {

        return getWrappedSearchNode().isEmpty();
    }

    public boolean isValid() throws Exception
    {

        return getWrappedSearchNode().isValid();
    }

    public void reset() throws Exception
    {

        getWrappedSearchNode().reset();
    }

    public void setPersistentNode(Node persistentNode_p) throws Exception
    {

        getWrappedSearchNode().setPersistentNode(persistentNode_p);
    }

}