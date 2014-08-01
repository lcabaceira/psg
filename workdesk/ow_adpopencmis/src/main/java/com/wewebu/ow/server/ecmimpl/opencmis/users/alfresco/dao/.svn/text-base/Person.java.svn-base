package com.wewebu.ow.server.ecmimpl.opencmis.users.alfresco.dao;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *<p>
 * A person as result from /service/api/people/{userName}.
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person
{
    @JsonProperty
    private String userName;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String email;

    @JsonProperty
    private Group groups[];

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * @return the groups
     */
    public Group[] getGroups()
    {
        return groups;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Group
    {
        @JsonProperty
        private String itemName;
        @JsonProperty
        private String displayName;

        /**
         * @return the itemName
         */
        public String getItemName()
        {
            return itemName;
        }

        /**
         * @return the displayName
         */
        public String getDisplayName()
        {
            return displayName;
        }
    }
}
