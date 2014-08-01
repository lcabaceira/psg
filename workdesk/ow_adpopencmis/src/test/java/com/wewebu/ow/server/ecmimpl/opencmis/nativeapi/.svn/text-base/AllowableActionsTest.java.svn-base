package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.data.AllowableActions;
import org.apache.chemistry.opencmis.commons.enums.Action;

public class AllowableActionsTest extends AbstractNativeTest
{

    public AllowableActionsTest()
    {
        super(true);
    }

    public void testAllowableActions()
    {
        Document document = (Document) getSession().getObjectByPath("/JUnitTest/CheckedOutDocument (Working Copy)");
        AllowableActions actions = document.getAllowableActions();
        for (Action a : actions.getAllowableActions())
        {
            System.out.println(a.name());
        }
    }
}
