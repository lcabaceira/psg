/* Generated By:JavaCC: Do not edit this line. OwKeyParserConstants.java */
package com.wewebu.ow.server.plug.efilekey.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface OwKeyParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 8;
  /** RegularExpression Id. */
  int FORMAL_COMMENT = 9;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 10;
  /** RegularExpression Id. */
  int NULL = 12;
  /** RegularExpression Id. */
  int TRUE = 13;
  /** RegularExpression Id. */
  int FALSE = 14;
  /** RegularExpression Id. */
  int AND = 15;
  /** RegularExpression Id. */
  int OR = 16;
  /** RegularExpression Id. */
  int XOR = 17;
  /** RegularExpression Id. */
  int NOT = 18;
  /** RegularExpression Id. */
  int STRING_LITERAL = 19;
  /** RegularExpression Id. */
  int PROPERTY_NAME = 20;
  /** RegularExpression Id. */
  int CHAR = 21;
  /** RegularExpression Id. */
  int QCHAR = 22;
  /** RegularExpression Id. */
  int SPECIAL_CHAR = 23;
  /** RegularExpression Id. */
  int USER_DEFINED_SEQUENCE = 24;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int IN_FORMAL_COMMENT = 1;
  /** Lexical state. */
  int IN_MULTI_LINE_COMMENT = 2;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "<token of kind 6>",
    "\"/*\"",
    "<SINGLE_LINE_COMMENT>",
    "\"*/\"",
    "\"*/\"",
    "<token of kind 11>",
    "\"null\"",
    "\"true\"",
    "\"false\"",
    "\"and\"",
    "\"or\"",
    "\"xor\"",
    "\"not\"",
    "<STRING_LITERAL>",
    "<PROPERTY_NAME>",
    "<CHAR>",
    "<QCHAR>",
    "<SPECIAL_CHAR>",
    "<USER_DEFINED_SEQUENCE>",
    "\"{\"",
    "\",\"",
    "\"}\"",
  };

}
