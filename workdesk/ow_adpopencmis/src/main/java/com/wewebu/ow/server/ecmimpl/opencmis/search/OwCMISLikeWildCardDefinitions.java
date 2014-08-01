package com.wewebu.ow.server.ecmimpl.opencmis.search;

import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.ecm.OwRepositoryContext;
import com.wewebu.ow.server.field.OwStandardWildCardDefinition;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Wild card definitions as specified in CMIS spec. V 1.0.
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
public class OwCMISLikeWildCardDefinitions
{
    /**String representing wild card for multiple character*/
    public static final String MULTI_CHARACTER = "%";
    /**String representing wild card for single character*/
    public static final String SINGLE_CHARACTER = "_";

    private List<OwWildCardDefinition> definitionList;

    public OwCMISLikeWildCardDefinitions(OwRepositoryContext repoCtx)
    {
        this.definitionList = new LinkedList<OwWildCardDefinition>();
        String appDef = repoCtx.getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR);
        this.definitionList.add(new OwStandardWildCardDefinition(MULTI_CHARACTER, appDef, OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR, new OwString1("opencmis.search.OwCMISLikeWildCardDefinitions.WILD_CARD_TYPE_MULTI_CHAR",
                "(%1) replaces multiple characters", appDef)));
        appDef = repoCtx.getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR);
        this.definitionList.add(new OwStandardWildCardDefinition(SINGLE_CHARACTER, appDef, OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR, new OwString1("opencmis.search.OwCMISLikeWildCardDefinitions.WILD_CARD_TYPE_SINGLE_CHAR",
                "(%1) replaces any character", appDef)));
    }

    /**
     * Return the list of defined/available wild cards
     * @return List of OwWildCardDefinition's
     */
    public List<OwWildCardDefinition> getWildCardDefinitions()
    {
        return this.definitionList;
    }

}
