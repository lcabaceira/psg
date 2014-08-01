package com.wewebu.ow.server.ecmimpl.fncm5.viewer.info;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.Annotation;
import com.filenet.api.core.Containable;
import com.filenet.api.property.PropertyFilter;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5Object;
import com.wewebu.ow.server.ecmimpl.fncm5.perm.OwFNCM5PermissionHelper;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.ui.viewer.OwAbstractAnnotationInfoProvider;
import com.wewebu.ow.server.ui.viewer.OwAnnotInfoEnum;
import com.wewebu.ow.server.ui.viewer.OwAnnotResultsEnum;
import com.wewebu.ow.server.ui.viewer.OwInfoRequest;

/**
 *<p>
 * P8 5.0 Annotation InfoProvider.
 * Used to get the available possible actions of advanced viewing,
 * for Annotations.
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
public class OwFNCM5AnnotationInfoProvider extends OwAbstractAnnotationInfoProvider<Annotation>
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5AnnotationInfoProvider.class);

    private OwFNCM5Network network;
    private ThreadLocal<Integer> accessMask;

    public OwFNCM5AnnotationInfoProvider(OwFNCM5Network network)
    {
        this.network = network;
        accessMask = new ThreadLocal<Integer>();
    }

    @Override
    public void handleRequest(OwInfoRequest req, OutputStream answer) throws OwException, IOException
    {
        try
        {
            super.handleRequest(req, answer);
        }
        finally
        {
            accessMask.remove();
        }
    }

    public String getContext()
    {
        return getNetwork().getDMSPrefix();
    }

    @Override
    protected String getId(Annotation annotObj)
    {
        PropertyFilter propFilter = new PropertyFilter();
        propFilter.setMaxRecursion(5);
        propFilter.addIncludeProperty(0, null, null, PropertyNames.PERMISSIONS, null);
        annotObj.fetchProperties(propFilter);
        try
        {
            accessMask.set(OwFNCM5PermissionHelper.getAccumulatedUserPermission(annotObj.get_Permissions(), network.getCredentials().getUserInfo()));
        }
        catch (Exception e)
        {
            LOG.error("Could not accumulate user permission mask, setting per default denied", e);
            accessMask.set(Integer.valueOf(0));
        }
        return annotObj.get_Id().toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<Annotation> getAnnotations(String objDMSID)
    {
        LinkedList<Annotation> lst = new LinkedList<Annotation>();
        try
        {
            OwFNCM5Object<Containable> obj = (OwFNCM5Object<Containable>) network.getObjectFromDMSID(objDMSID, true);
            Iterator<?> it = obj.getNativeObject().get_Annotations().iterator();
            while (it.hasNext())
            {
                Annotation anno = (Annotation) it.next();
                lst.add(anno);
            }
        }
        catch (OwException e)
        {
            String msg = "Could not retrieve Annotations form obj! DMSID = " + objDMSID;
            if (LOG.isDebugEnabled())
            {
                LOG.warn(msg, e);
            }
            else
            {
                LOG.warn(msg);
            }
        }
        return lst;
    }

    protected OwFNCM5Network getNetwork()
    {
        return network;
    }

    @Override
    protected String getAnnotationInfo(Annotation annotation, String annotInfoType)
    {
        OwAnnotResultsEnum result = OwAnnotResultsEnum.VALUE_DENY;
        try
        {
            switch (OwAnnotInfoEnum.getEnumByType(annotInfoType))
            {
                case VIEW_ANNOTATION:
                    result = OwAnnotResultsEnum.VALUE_ALLOW;
                    break;
                case DELETE_ANNOTATION:
                {
                    if ((accessMask.get().intValue() & AccessRight.DELETE_AS_INT) > 0)
                    {
                        result = OwAnnotResultsEnum.VALUE_ALLOW;
                    }
                }
                    break;
                case MODIFY_ANNOTATION:
                {
                    if ((accessMask.get().intValue() & AccessRight.WRITE_AS_INT) > 0)
                    {
                        result = OwAnnotResultsEnum.VALUE_ALLOW;
                    }
                }
                    break;
                case EDIT_ANNOTATION_ACL:
                {
                    if ((accessMask.get().intValue() & AccessRight.WRITE_ACL_AS_INT) > 0)
                    {
                        result = OwAnnotResultsEnum.VALUE_ALLOW;
                    }
                }
                    break;
                case VIEW_ANNOTATION_ACL:
                {
                    if ((accessMask.get().intValue() & AccessRight.READ_ACL_AS_INT) > 0)
                    {
                        result = OwAnnotResultsEnum.VALUE_ALLOW;
                    }
                }
            }
        }
        catch (Exception e)
        {
            String msg = "Could not investigate of permission for = " + annotInfoType;
            if (LOG.isDebugEnabled())
            {
                LOG.warn(msg, e);
            }
            else
            {
                LOG.warn(msg);
            }
        }
        return result.getValue();
    }

}