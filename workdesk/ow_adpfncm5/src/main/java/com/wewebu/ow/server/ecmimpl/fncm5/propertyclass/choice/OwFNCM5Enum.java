package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.choice;

import java.util.Iterator;
import java.util.Locale;

import com.filenet.api.admin.Choice;
import com.filenet.api.admin.LocalizedString;
import com.filenet.api.collection.ChoiceList;
import com.filenet.api.collection.LocalizedStringList;
import com.filenet.api.constants.ChoiceType;
import com.wewebu.ow.server.field.OwEnum;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * FileNet P8 5 wrapper for Choice.
 * Implementation for Choice which will be representing
 * a simple entry or a sub-choice list.
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
public class OwFNCM5Enum implements OwEnum
{
    private LocalizedStringList localizedList;
    private String localizedString;
    private String displayName;
    private OwEnumCollection childs;
    private Object value;

    public OwFNCM5Enum(Choice c, String localizePrefix)
    {
        localizedList = c.get_DisplayNames();
        displayName = c.get_DisplayName();
        int type = c.get_ChoiceType().getValue();
        if (ChoiceType.MIDNODE_STRING_AS_INT == type || ChoiceType.MIDNODE_INTEGER_AS_INT == type)
        {
            localizedString = localizePrefix + c.get_Id().toString();
            childs = createEnumCollection(c.get_ChoiceValues(), localizedString);
        }
        else
        {
            localizedString = localizePrefix + c.get_Name();
            switch (type)
            {
                case ChoiceType.INTEGER_AS_INT:
                    localizedString = localizePrefix + Integer.toString(c.get_ChoiceIntegerValue());
                    value = c.get_ChoiceIntegerValue();
                    break;
                case ChoiceType.STRING_AS_INT:
                    localizedString = localizePrefix + c.get_ChoiceStringValue();
                    value = c.get_ChoiceStringValue();
                    break;
                default:/*unknown, unsupported by P8 API*/
                    ;
            }
        }
    }

    public Object getValue()
    {
        return value;
    }

    public String getDisplayName(Locale local_p)
    {
        String label = OwString.localizeLabel(local_p, getLocalizedString(), null);
        if (label == null)
        {
            String local = local_p.getLanguage();
            Iterator it = getLocalizedList().iterator();
            while (it.hasNext())
            {
                LocalizedString fnStr = (LocalizedString) it.next();
                if (fnStr.get_LocaleName().startsWith(local))
                {
                    label = fnStr.get_LocalizedText();
                    break;
                }
            }
        }
        return label == null ? this.displayName : label;
    }

    public boolean hasChildEnums()
    {
        return childs == null;
    }

    public OwEnumCollection getChilds()
    {
        return childs;
    }

    public String getLocalizedString()
    {
        return localizedString;
    }

    protected LocalizedStringList getLocalizedList()
    {
        return this.localizedList;
    }

    public OwEnumCollection createEnumCollection(ChoiceList lst, String prefix)
    {
        return new OwFNCM5EnumCollection(lst, prefix);
    }

}
