package com.wewebu.ow.server.plug.owlink;

import java.util.Collection;
import java.util.List;

import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectLinksDocument;
import com.wewebu.ow.server.dmsdialogs.views.OwSplitObjectListDocument;
import com.wewebu.ow.server.dmsdialogs.views.OwTypedLinksView;
import com.wewebu.ow.server.field.OwFieldColumnInfo;

/**
 *<p>
 * Show typed links with special {@link OwSplitObjectListDocument}.
 * Special document will resolves the links to specific OwObject representations.
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
public class OwReferencedObjectTypedLinksView extends OwTypedLinksView
{

    public OwReferencedObjectTypedLinksView(OwObjectLinksDocument document)
    {
        super(document);
    }

    protected OwSplitObjectListDocument createSplitObjectListDocument(String[] splitNames, Collection<OwFieldColumnInfo>[] propertyColumnInfos, List<OwDocumentFunction> documentFunctions)
    {
        OwReferencedObjectSplitDocument doc = new OwReferencedObjectSplitDocument(splitNames, propertyColumnInfos, documentFunctions);
        doc.setRelations(getDocument().getRelations());
        return doc;
    }
}
