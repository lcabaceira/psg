package com.wewebu.ow.server.ecmimpl.fncm5.object;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5EngineObjectClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * OwFNCM5PersistentState.
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
public class OwFNCM5PersistentState<P extends IndependentlyPersistableObject> extends OwFNCM5IndependentState<P>
{

    public OwFNCM5PersistentState(P engineObject_p, OwFNCM5EngineObjectClass<?, ?> engineObjectClass_p)
    {
        super(engineObject_p, engineObjectClass_p);
    }

    @Override
    public void save(RefreshMode mode_p) throws OwException
    {
        super.save(mode_p);
        P persistableObject = getEngineObject();
        if (mode_p != null)
        {
            persistableObject.save(mode_p);
        }
        else
        {
            persistableObject.save(RefreshMode.REFRESH);
        }
    }

}
