package com.wewebu.ow.server.field;

/**
 *<p>
 * Column Description for object lists. Holds a property name and the corresponding displayname.
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
public interface OwFieldColumnInfo
{
    /** alignment to use for the column: use the default of the list */
    public static final int ALIGNMENT_DEFAULT = 0;
    /** alignment to use for the column: left alignment */
    public static final int ALIGNMENT_LEFT = 1;
    /** alignment to use for the column: center alignment */
    public static final int ALIGNMENT_CENTER = 2;
    /** alignment to use for the column: right alignment */
    public static final int ALIGNMENT_RIGHT = 3;

    /** alignment to use for the column: left alignment, no wrap of text */
    public static final int ALIGNMENT_LEFT_NOWRAP = 4;
    /** alignment to use for the column: center alignment, no wrap of text  */
    public static final int ALIGNMENT_CENTER_NOWRAP = 5;
    /** alignment to use for the column: right alignment, no wrap of text  */
    public static final int ALIGNMENT_RIGHT_NOWRAP = 6;

    /** max number of defined alignments */
    public static final int ALIGNMENT_MAX = 7;

    /** get the string to display in the header
     *
     * @param locale_p Locale to use
     * @return String display name
     */
    public abstract String getDisplayName(java.util.Locale locale_p);

    /** get the object property name this column is referring to
     *
     * @return String property name
     */
    public abstract String getPropertyName();

    /** get the object property name this column is referring to
     *
     * @return int alignment as defined with OwFieldColumnInfo.ALIGNMENT_...
     */
    public abstract int getAlignment();

    /** get column width in % or 0 if undefined
     * @return int width [%]
     * */
    public abstract int getWidth();

    /**
     * Returns <code>true</code> if the column can be sorted.
     * @return <code>true</code> if the column can be sorted.
     * @since 2.5.3.0
     */
    public boolean isSortable();
}