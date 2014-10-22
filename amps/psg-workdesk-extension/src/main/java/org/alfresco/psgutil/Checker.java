package org.alfresco.psgutil;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.log.OwLogCore;

public class Checker
{
    /** package logger for the class */
    @SuppressWarnings("unused")
    private static final Logger LOG = OwLogCore.getLogger(Checker.class);

    /**
     * Checks and returns if OwObject has in- or out-bound links
     * @param object_p OwObject to check
     * @return true if object has links, false else
     * @throws Exception 
     */
    public static boolean hasLinks(OwObject object_p) throws Exception
    {
        if (null != object_p)
        {
            OwObjectCollection col = object_p.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_LINK }, null, null, Integer.MAX_VALUE / 2, 0, null);
            return col.size() > 0;
        }
        return false;
    }
}
