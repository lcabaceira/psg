package com.wewebu.ow.server.ecmimpl.alfresco.bpm.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Wrapper over an {@link OwObjectClass} that adds some extra properties.
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
 *@since 4.0.0.0
 */
@SuppressWarnings({ "rawtypes" })
public abstract class OwAlfrescoBPMObjectClass implements OwObjectClass
{
    public static final String PROP_STATE = "state";
    public static final String PROP_IS_CLAIMABLE = "isClaimable";
    public static final String PROP_IS_RELEASABLE = "isReleasable";
    public static final String PROP_IS_POOLED = "isPooled";

    public static final String PROP_OW_RESUBMIT_DATE = "owdbpm:resubmissionDate";
    public static final String PROP_OW_STEPPROCESSOR_JSP_PAGE = "owdbpm:jspStepProcessor";

    public static final String PROP_CM_CREATED = "cm:created";

    public static final String PROP_BPM_PACKAGE = "bpm:package";
    public static final String PROP_BPM_TASK_ID = "bpm:taskId";

    // bpm_*assignee properties are defined as Associations. The CMIS adapter does not yet handle Associations.
    public static final String PROP_BPM_ASSIGNEE = "bpm:assignee";
    public static final String PROP_BPM_ASSIGNEES = "bpm:assignees";
    public static final String PROP_BPM_GROUP_ASSIGNEE = "bpm:groupAssignee";
    public static final String PROP_BPM_GROUP_ASSIGNEES = "bpm:groupAssignees";

    public static final String PROP_OW_ATTACHMENTS = "OW_ATTACHMENTS";
    public static final String PROP_OW_TASK_TITLE = "OW_TASK_TITLE";
    public static final String PROP_OW_ASSIGNEE = "OW_ASSIGNEE";

    public static final String PROP_CM_OWNER = "P:cm:ownable.cm:owner";

    protected OwObjectClass wrappedObjectClass;
    protected Map<String, OwPropertyClass> virtualProperties = new HashMap<String, OwPropertyClass>();

    public OwAlfrescoBPMObjectClass(OwObjectClass wrappedObjectClass)
    {
        this.wrappedObjectClass = wrappedObjectClass;
    }

    public int getType()
    {
        return wrappedObjectClass.getType();
    }

    public List getChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws Exception
    {
        return wrappedObjectClass.getChilds(network_p, fExcludeHiddenAndNonInstantiable_p);
    }

    public Map getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws Exception
    {
        return wrappedObjectClass.getChildNames(network_p, fExcludeHiddenAndNonInstantiable_p);
    }

    public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p) throws Exception
    {
        return wrappedObjectClass.hasChilds(network_p, fExcludeHiddenAndNonInstantiable_p, context_p);
    }

    public String getClassName()
    {
        return wrappedObjectClass.getClassName();
    }

    public String getDisplayName(Locale locale_p)
    {
        return wrappedObjectClass.getDisplayName(locale_p);
    }

    public OwPropertyClass getPropertyClass(String strClassName_p) throws Exception
    {
        OwPropertyClass result = null;
        try
        {
            result = wrappedObjectClass.getPropertyClass(strClassName_p);
        }
        catch (Exception e)
        {
            result = this.virtualProperties.get(strClassName_p);
        }
        if (null == result)
        {
            throw new OwObjectNotFoundException("Could not find property definition for " + strClassName_p);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Collection getPropertyClassNames() throws Exception
    {
        ArrayList<String> properties = new ArrayList<String>();
        properties.addAll(wrappedObjectClass.getPropertyClassNames());
        properties.addAll(this.virtualProperties.keySet());
        return properties;
    }

    public String getNamePropertyName() throws Exception
    {
        return wrappedObjectClass.getNamePropertyName();
    }

    public boolean canCreateNewObject() throws Exception
    {
        return wrappedObjectClass.canCreateNewObject();
    }

    public boolean hasVersionSeries() throws Exception
    {
        return wrappedObjectClass.hasVersionSeries();
    }

    public List getModes(int operation_p) throws Exception
    {
        return wrappedObjectClass.getModes(operation_p);
    }

    public String getDescription(Locale locale_p)
    {
        return wrappedObjectClass.getDescription(locale_p);
    }

    public boolean isHidden() throws Exception
    {
        return wrappedObjectClass.isHidden();
    }

    public OwObjectClass getParent() throws Exception
    {
        return wrappedObjectClass.getParent();
    }
}
