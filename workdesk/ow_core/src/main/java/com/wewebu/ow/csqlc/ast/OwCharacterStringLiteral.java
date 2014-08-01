package com.wewebu.ow.csqlc.ast;

/**
 *<p>
 * SQL AST node : &lt;character string literal&gt; syntax terminal as defined by the SQL grammar.<br/> 
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
 *@since 3.2.0.0
 */
public class OwCharacterStringLiteral implements OwLiteral
{

    private String m_characterStringSQLString;

    /**
     * Constructor
     * @param characterString_p the String value of this literal , can be null
     */
    public OwCharacterStringLiteral(String characterString_p)
    {
        this.m_characterStringSQLString = characterString_p;
    }

    public StringBuilder createLiteralSQLString()
    {
        StringBuilder builder = new StringBuilder(m_characterStringSQLString);
        builder.insert(0, '\'');
        builder.append('\'');
        return builder;

    }

    private StringBuilder createBuilder()
    {
        String current = m_characterStringSQLString == null ? "" : m_characterStringSQLString;
        return new StringBuilder(current);
    }

    /**
     *Appends the specified string to this character sequence String contents  
     *and returns new {@link OwCharacterStringLiteral} based on the resulted
     *string literal contents. Does not change this character string literal.
     *
     * @param characterString_p
     * @return a new {@link OwCharacterStringLiteral} having its content 
     *         formed by appending the given String to the contents of this literal
     *@since 3.2.0.0  
     */
    public OwCharacterStringLiteral append(String characterString_p)
    {
        StringBuilder builder = createBuilder();
        builder.append(characterString_p);
        return new OwCharacterStringLiteral(builder.toString());
    }

    /**
     *Inserts the specified string at the given offset into this character sequence String 
     *contents and returns new {@link OwCharacterStringLiteral} based on the resulted
     *string literal contents. Does not change this character string literal.
     *
     * @param characterString_p
     * @return a new {@link OwCharacterStringLiteral} having its content 
     *         formed by inserting the given String at the given offset into the contents 
     *         of this literal
     *@throws StringIndexOutOfBoundsException if the offset is invalid
     *@since 3.2.0.0  
     */
    public OwCharacterStringLiteral insert(int offset_p, String characterString_p)
    {
        StringBuilder builder = createBuilder();
        builder.insert(offset_p, characterString_p);
        return new OwCharacterStringLiteral(builder.toString());
    }

    /**
     *Replaces the specified range from this character sequence String contents with 
     *the specified String and returns new {@link OwCharacterStringLiteral} based 
     *on the resulted string literal contents. 
     *Does not change this character string literal.
     *
     * @param characterString_p
     * @return a new {@link OwCharacterStringLiteral} having its content 
     *         formed by replacing the specified range from this character sequence String 
     *         contents with the given character String
     *@throws StringIndexOutOfBoundsException if start is negative, greater than the length of the 
     *                                        contents of this literal, or greater than end.
     *@since 3.2.0.0  
     */
    public OwCharacterStringLiteral replace(int start_p, int end_p, String characterString_p)
    {
        StringBuilder builder = createBuilder();
        builder.replace(start_p, end_p, characterString_p);
        return new OwCharacterStringLiteral(builder.toString());
    }

    /**
     * Search the given character in the contents of this String literal.
     *  
     * @param c_p
     * @param escaped_p if true only escaped occurrences of the given character are searched
     *                  The  backslash character (\) will be used to escape characters within quoted strings in the query as follows:<br>
     *                  1. \&#39; will represent a single-quote(&#39;) character <br>
     *                  2. \ \ will represent a backslash (\) character <br>
     *                  3. Within a LIKE string, \% and \_ will represent the literal characters % and _, respectively. <br>
     *                  4. Although other instances of a \ are errors they are still considered valid by this method  
     *                  
     * @return <code>true</code> if the given character is found considering escaping
     *         <code>false</code> otherwise  
     * @since 3.2.0.0
     */
    public boolean contains(char c_p, boolean escaped_p)
    {
        boolean contains = false;

        if (m_characterStringSQLString != null)
        {
            boolean escaping = false;
            for (int index = 0; index < m_characterStringSQLString.length(); index++)
            {
                char c = m_characterStringSQLString.charAt(index);
                if (c == c_p && escaping == escaped_p)
                {
                    contains = true;
                    break;
                }

                if (c == '\\' && !escaping)
                {
                    escaping = true;
                }
                else
                {
                    escaping = false;
                }

            }
        }

        return contains;
    }

    /**
     * 
     * @return <code>true</code> if the String value of this literal is null<br>
     *         <code>false</code> otherwise
     */
    public boolean isNull()
    {
        return m_characterStringSQLString == null || m_characterStringSQLString.length() == 0;
    }

}
