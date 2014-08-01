package com.wewebu.ow.server.dmsdialogs.views;

import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wewebu.ow.server.app.OwComboModel;
import com.wewebu.ow.server.app.OwComboboxRenderer;
import com.wewebu.ow.server.app.OwDefaultComboModel;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectLink;
import com.wewebu.ow.server.ecm.OwObjectLinkRelation;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwVirtualLinkPropertyClasses;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldColumnInfo;

/**
 *<p>
 * Displays {@link OwObjectLink}s using {@link OwSplitObjectListView} and one 
 * link object class combo box filter.
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
public class OwTypedLinksView extends OwObjectLinksView
{

    public static final String LINK_CLASS_ID = "lkclp";

    private Map<String, OwObjectCollection[]> linkObjects;
    private Map<String, OwObjectClass> linkClasses;
    private String currentClassName;

    public OwTypedLinksView(OwObjectLinksDocument document)
    {
        super(document);
    }

    @Override
    protected String usesFormWithAttributes()
    {
        return "";
    }

    @Override
    protected void init() throws Exception
    {
        super.init();
        setExternalFormTarget(this);

        OwSplitObjectListView typeView = createSplitView();
        addView(typeView, LINKS_REGION, null);

    }

    protected OwSplitObjectListView getSplitLinksView() throws OwObjectNotFoundException
    {
        return (OwSplitObjectListView) getViewRegion(LINKS_REGION);
    }

    @Override
    public String getFilterDisplayName()
    {
        return getContext().localize("OwTypedLinksView.link.class", "Link class");
    }

    @Override
    public boolean isRegion(int iRegion_p)
    {
        if (LINKS_FILTER_REGION == iRegion_p)
        {
            return true;
        }
        else
        {
            return super.isRegion(iRegion_p);
        }
    }

    @Override
    public void renderRegion(Writer w_p, int iRegion_p) throws Exception
    {
        if (LINKS_FILTER_REGION == iRegion_p)
        {

            Collection<OwObjectClass> classes = linkClasses.values();
            Locale locale = getContext().getLocale();
            String[] classDisplayNames = new String[linkClasses.size()];
            String[] classValues = new String[linkClasses.size()];
            int count = 0;
            for (OwObjectClass linkClass : classes)
            {
                classDisplayNames[count] = linkClass.getDisplayName(locale);
                classValues[count] = linkClass.getClassName();
                count++;
            }

            OwComboModel comboModel = new OwDefaultComboModel(false, false, "" + currentClassName, classValues, classDisplayNames);
            OwComboboxRenderer renderer = ((OwMainAppContext) getContext()).createComboboxRenderer(comboModel, LINK_CLASS_ID, null, null, null);
            renderer.setEnabled(!isEmpty());

            if (!classes.isEmpty())
            {
                StringBuffer linkClassChanged = new StringBuffer();
                linkClassChanged.append("<script>\n");
                linkClassChanged.append("function linkClassChanged(value) {\n");
                linkClassChanged.append("document.");
                linkClassChanged.append(getFormName());
                linkClassChanged.append(".action=\"");
                linkClassChanged.append(getEventURL("LinkClassChanged", null));
                linkClassChanged.append("\";document.");
                linkClassChanged.append(getFormName());
                linkClassChanged.append(".submit();\n");
                linkClassChanged.append("}\n");
                linkClassChanged.append("</script>\n");
                w_p.write(linkClassChanged.toString());

                renderer.addEvent("onchange", "linkClassChanged()");
            }

            renderer.renderCombo(w_p);
        }
        else
        {
            super.renderRegion(w_p, iRegion_p);
        }
    }

    public void onLinkClassChanged(HttpServletRequest request_p) throws Exception
    {
        String linkClass = request_p.getParameter(LINK_CLASS_ID);
        select(linkClass);
    }

    protected void select(String className) throws Exception
    {
        if (className != null)
        {
            OwSplitObjectListView linksView = getSplitLinksView();
            linksView.getDocument().setSplits(linkObjects.get(className));
            currentClassName = className;
        }

    }

    @Override
    protected void refresh(OwObjectCollection[] splitLinks) throws Exception
    {
        super.refresh(splitLinks);
        postRefresh(splitLinks);
    }

    protected void postRefresh(OwObjectCollection[] splitLinks) throws Exception
    {
        OwObjectCollection[] allLinks = splitLinks;

        linkObjects = new HashMap<String, OwObjectCollection[]>();
        linkClasses = new HashMap<String, OwObjectClass>();
        for (int i = 0; i < allLinks.length; i++)
        {
            if (allLinks[i] != null)
            {
                Iterator linksIt = allLinks[i].iterator();
                while (linksIt.hasNext())
                {
                    OwObject link = (OwObject) linksIt.next();
                    OwObjectClass linkClass = getLinkClass(link);

                    OwObjectCollection[] classLinks = linkObjects.get(linkClass.getClassName());

                    if (classLinks == null)
                    {
                        linkClasses.put(linkClass.getClassName(), linkClass);
                        classLinks = new OwObjectCollection[allLinks.length];
                        linkObjects.put(linkClass.getClassName(), classLinks);

                    }

                    if (classLinks[i] == null)
                    {
                        classLinks[i] = new OwStandardObjectCollection();
                    }

                    classLinks[i].add(link);
                }
            }
        }

        if (!linkClasses.containsKey(currentClassName) && !linkClasses.isEmpty())
        {
            currentClassName = linkClasses.keySet().iterator().next();
        }
        else if (linkClasses.isEmpty())
        {
            currentClassName = null;
        }

        select(currentClassName);
    }

    protected OwObjectClass getLinkClass(OwObject object)
    {
        return object.getObjectClass();
    }

    @Override
    protected Collection<OwFieldColumnInfo> createRelationColumnInfo(OwObjectLinkRelation relation) throws Exception
    {
        if (getDocument().getColumnNames() != null)
        {
            return super.createRelationColumnInfo(relation);
        }
        else
        {
            Collection<OwFieldColumnInfo> propertyColumnInfo;
            if (OwObjectLinkRelation.INBOUND == relation)
            {
                propertyColumnInfo = createColumnInfo(Arrays.asList(new String[] { OwVirtualLinkPropertyClasses.LINK_SOURCE.getClassName() }));
            }
            else if (OwObjectLinkRelation.OUTBOUND == relation)
            {
                propertyColumnInfo = createColumnInfo(Arrays.asList(new String[] { OwVirtualLinkPropertyClasses.LINK_TARGET.getClassName() }));
            }
            else
            {
                propertyColumnInfo = createColumnInfo(Arrays.asList(new String[] { OwVirtualLinkPropertyClasses.LINK_SOURCE.getClassName(), OwVirtualLinkPropertyClasses.LINK_TARGET.getClassName() }));
            }
            return propertyColumnInfo;
        }
    }
}
