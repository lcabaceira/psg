package com.wewebu.ow.server.ui.viewer;

/**
 *<p>
 * Annotation Information Provider Interface.
 * Implementing classes will be used to request access rights
 * on existing annotations.
 *</p>
 *
 *<p>
 * Implementing classes should return answers like follows:<br />
 * <pre><code>
 *   &lt;permissions&gt;
 *      &lt;permission&gt;
 *        &lt;id&gt;An-ID-here&lt;/id&gt;
 *        &lt;view_annotation&gt;allow&lt;/view_annotation&gt;
 *        &lt;modify_annotation&gt;deny&lt;/modify_annotation&gt;
 *        &lt;delete_annotation&gt;deny&lt;/delete_annotation&gt;
 *        &lt;view_acl&gt;deny&lt;view_acl&gt;
 *        &lt;edit_acl&gt;deny&lt;edit_acl&gt;
 *      &lt;/permission&gt;
 *      &lt;permission&gt;
 *      ...
 *      &lt;permission&gt;
 *   &lt;/permissions&gt;
 * </code></pre>
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
 *@since 3.1.0.0
 *@see OwAnnotResultsEnum
 *@see OwAnnotInfoEnum
 */
public interface OwAnnotationInfoProvider extends OwInfoProvider
{
    /**Tag to provide the ID of an Annotation*/
    public static final String ID = "id";

    /**ROOT tag for answer*/
    public static final String PERMISSIONS = "permissions";
    /**Tag to describe permission for an Annotation*/
    public static final String PERMISSION = "permission";
}
