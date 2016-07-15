/* Generated By:JavaCC: Do not edit this line. ExpressionParserConstants.java */
package com.google.template.soy.exprparse;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ExpressionParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int NULL = 1;
  /** RegularExpression Id. */
  int BOOLEAN = 2;
  /** RegularExpression Id. */
  int INTEGER = 3;
  /** RegularExpression Id. */
  int FLOAT = 4;
  /** RegularExpression Id. */
  int STRING = 5;
  /** RegularExpression Id. */
  int DEC_DIGITS = 6;
  /** RegularExpression Id. */
  int HEX_DIGIT = 7;
  /** RegularExpression Id. */
  int UNARY_OP = 8;
  /** RegularExpression Id. */
  int PRECEDENCE_2_OP = 9;
  /** RegularExpression Id. */
  int PRECEDENCE_3_OP = 10;
  /** RegularExpression Id. */
  int PRECEDENCE_4_OP = 11;
  /** RegularExpression Id. */
  int PRECEDENCE_5_OP = 12;
  /** RegularExpression Id. */
  int PRECEDENCE_6_OP = 13;
  /** RegularExpression Id. */
  int PRECEDENCE_7_OP = 14;
  /** RegularExpression Id. */
  int IDENT = 15;
  /** RegularExpression Id. */
  int DOLLAR_IDENT = 16;
  /** RegularExpression Id. */
  int DOT_IDENT = 17;
  /** RegularExpression Id. */
  int QUESTION_DOT_IDENT = 18;
  /** RegularExpression Id. */
  int WS = 19;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"null\"",
    "<BOOLEAN>",
    "<INTEGER>",
    "<FLOAT>",
    "<STRING>",
    "<DEC_DIGITS>",
    "<HEX_DIGIT>",
    "\"not\"",
    "\"or\"",
    "\"and\"",
    "<PRECEDENCE_4_OP>",
    "<PRECEDENCE_5_OP>",
    "\"+\"",
    "<PRECEDENCE_7_OP>",
    "<IDENT>",
    "<DOLLAR_IDENT>",
    "<DOT_IDENT>",
    "<QUESTION_DOT_IDENT>",
    "<WS>",
    "\",\"",
    "\"?:\"",
    "\"?\"",
    "\":\"",
    "\"-\"",
    "\"[\"",
    "\"]\"",
    "\"?[\"",
    "\"(\"",
    "\")\"",
    "\"$ij.\"",
    "\"$ij?.\"",
  };

}