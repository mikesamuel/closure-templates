/* ExpressionParserTokenManager.java */
/* Generated By:JavaCC: Do not edit this line. ExpressionParserTokenManager.java */
package com.google.template.soy.exprparse;
import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.template.soy.base.SourceLocation;
import com.google.template.soy.error.ErrorReporter;
import com.google.template.soy.error.SoyError;
import com.google.template.soy.exprtree.BooleanNode;
import com.google.template.soy.exprtree.ExprNode;
import com.google.template.soy.exprtree.ExprNode.OperatorNode;
import com.google.template.soy.exprtree.ExprNode.PrimitiveNode;
import com.google.template.soy.exprtree.FieldAccessNode;
import com.google.template.soy.exprtree.FloatNode;
import com.google.template.soy.exprtree.FunctionNode;
import com.google.template.soy.exprtree.GlobalNode;
import com.google.template.soy.exprtree.IntegerNode;
import com.google.template.soy.exprtree.ItemAccessNode;
import com.google.template.soy.exprtree.ListLiteralNode;
import com.google.template.soy.exprtree.MapLiteralNode;
import com.google.template.soy.exprtree.NullNode;
import com.google.template.soy.exprtree.Operator;
import com.google.template.soy.exprtree.StringNode;
import com.google.template.soy.exprtree.VarNode;
import com.google.template.soy.exprtree.VarRefNode;
import com.google.template.soy.internal.base.UnescapeUtils;
import java.io.StringReader;
import java.util.List;

/** Token Manager. */
@SuppressWarnings("unused")public class ExpressionParserTokenManager implements ExpressionParserConstants {

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0){
   switch (pos)
   {
      case 0:
         if ((active0 & 0x702L) != 0L)
         {
            jjmatchedKind = 15;
            return 31;
         }
         if ((active0 & 0x8600000L) != 0L)
            return 41;
         if ((active0 & 0xc0000000L) != 0L)
            return 33;
         return -1;
      case 1:
         if ((active0 & 0xc0000000L) != 0L)
         {
            jjmatchedKind = 16;
            jjmatchedPos = 1;
            return 34;
         }
         if ((active0 & 0x200L) != 0L)
            return 31;
         if ((active0 & 0x502L) != 0L)
         {
            jjmatchedKind = 15;
            jjmatchedPos = 1;
            return 31;
         }
         return -1;
      case 2:
         if ((active0 & 0xc0000000L) != 0L)
         {
            jjmatchedKind = 16;
            jjmatchedPos = 2;
            return 34;
         }
         if ((active0 & 0x500L) != 0L)
            return 31;
         if ((active0 & 0x2L) != 0L)
         {
            jjmatchedKind = 15;
            jjmatchedPos = 2;
            return 31;
         }
         return -1;
      case 3:
         if ((active0 & 0x2L) != 0L)
            return 31;
         if ((active0 & 0x80000000L) != 0L)
         {
            if (jjmatchedPos < 2)
            {
               jjmatchedKind = 16;
               jjmatchedPos = 2;
            }
            return -1;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0){
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0(){
   switch(curChar)
   {
      case 36:
         return jjMoveStringLiteralDfa1_0(0xc0000000L);
      case 40:
         return jjStopAtPos(0, 28);
      case 41:
         return jjStopAtPos(0, 29);
      case 43:
         return jjStopAtPos(0, 13);
      case 44:
         return jjStopAtPos(0, 20);
      case 45:
         return jjStopAtPos(0, 24);
      case 58:
         return jjStopAtPos(0, 23);
      case 63:
         jjmatchedKind = 22;
         return jjMoveStringLiteralDfa1_0(0x8200000L);
      case 91:
         return jjStopAtPos(0, 25);
      case 93:
         return jjStopAtPos(0, 26);
      case 97:
         return jjMoveStringLiteralDfa1_0(0x400L);
      case 110:
         return jjMoveStringLiteralDfa1_0(0x102L);
      case 111:
         return jjMoveStringLiteralDfa1_0(0x200L);
      default :
         return jjMoveNfa_0(3, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0){
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 58:
         if ((active0 & 0x200000L) != 0L)
            return jjStopAtPos(1, 21);
         break;
      case 91:
         if ((active0 & 0x8000000L) != 0L)
            return jjStopAtPos(1, 27);
         break;
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0xc0000000L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x400L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L);
      case 114:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(1, 9, 31);
         break;
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 100:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(2, 10, 31);
         break;
      case 106:
         return jjMoveStringLiteralDfa3_0(active0, 0xc0000000L);
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x2L);
      case 116:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(2, 8, 31);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 46:
         if ((active0 & 0x40000000L) != 0L)
            return jjStopAtPos(3, 30);
         break;
      case 63:
         return jjMoveStringLiteralDfa4_0(active0, 0x80000000L);
      case 108:
         if ((active0 & 0x2L) != 0L)
            return jjStartNfaWithStates_0(3, 1, 31);
         break;
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0){
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 46:
         if ((active0 & 0x80000000L) != 0L)
            return jjStopAtPos(4, 31);
         break;
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec2 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 63;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 3:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 3)
                        kind = 3;
                     { jjCheckNAddStates(0, 4); }
                  }
                  else if ((0x100002600L & l) != 0L)
                  {
                     if (kind > 19)
                        kind = 19;
                  }
                  else if ((0x842000000000L & l) != 0L)
                  {
                     if (kind > 14)
                        kind = 14;
                  }
                  else if ((0x5000000000000000L & l) != 0L)
                  {
                     if (kind > 12)
                        kind = 12;
                  }
                  else if (curChar == 46)
                     { jjCheckNAddStates(5, 7); }
                  else if (curChar == 36)
                     jjstateSet[jjnewStateCnt++] = 33;
                  else if (curChar == 33)
                     { jjCheckNAdd(22); }
                  else if (curChar == 61)
                     { jjCheckNAdd(22); }
                  else if (curChar == 39)
                     { jjCheckNAddStates(8, 11); }
                  else if (curChar == 63)
                     jjstateSet[jjnewStateCnt++] = 41;
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 49;
                  else if (curChar == 62)
                     { jjCheckNAdd(26); }
                  else if (curChar == 60)
                     { jjCheckNAdd(26); }
                  else if (curChar == 48)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 9:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjstateSet[jjnewStateCnt++] = 9;
                  break;
               case 10:
                  if (curChar == 48)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 11:
                  if (curChar == 39)
                     { jjCheckNAddStates(8, 11); }
                  break;
               case 12:
                  if ((0xffffff7fffffdbffL & l) != 0L)
                     { jjCheckNAddStates(8, 11); }
                  break;
               case 14:
                  if ((0x8400000000L & l) != 0L)
                     { jjCheckNAddStates(8, 11); }
                  break;
               case 16:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 17;
                  break;
               case 17:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 18;
                  break;
               case 18:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 19;
                  break;
               case 19:
                  if ((0x3ff000000000000L & l) != 0L)
                     { jjCheckNAddStates(8, 11); }
                  break;
               case 21:
                  if (curChar == 39 && kind > 5)
                     kind = 5;
                  break;
               case 22:
                  if (curChar == 61 && kind > 11)
                     kind = 11;
                  break;
               case 23:
                  if (curChar == 61)
                     { jjCheckNAdd(22); }
                  break;
               case 24:
                  if (curChar == 33)
                     { jjCheckNAdd(22); }
                  break;
               case 25:
                  if ((0x5000000000000000L & l) != 0L && kind > 12)
                     kind = 12;
                  break;
               case 26:
                  if (curChar == 61 && kind > 12)
                     kind = 12;
                  break;
               case 27:
                  if (curChar == 60)
                     { jjCheckNAdd(26); }
                  break;
               case 28:
                  if (curChar == 62)
                     { jjCheckNAdd(26); }
                  break;
               case 29:
                  if ((0x842000000000L & l) != 0L && kind > 14)
                     kind = 14;
                  break;
               case 31:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 15)
                     kind = 15;
                  jjstateSet[jjnewStateCnt++] = 31;
                  break;
               case 32:
                  if (curChar == 36)
                     jjstateSet[jjnewStateCnt++] = 33;
                  break;
               case 34:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjstateSet[jjnewStateCnt++] = 34;
                  break;
               case 35:
                  if (curChar == 46)
                     { jjCheckNAddStates(5, 7); }
                  break;
               case 36:
                  if ((0x100002600L & l) != 0L)
                     { jjCheckNAddStates(5, 7); }
                  break;
               case 37:
                  if (curChar == 10)
                     { jjCheckNAddStates(5, 7); }
                  break;
               case 38:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 37;
                  break;
               case 40:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 17)
                     kind = 17;
                  jjstateSet[jjnewStateCnt++] = 40;
                  break;
               case 41:
                  if (curChar == 46)
                     { jjCheckNAddStates(12, 14); }
                  break;
               case 42:
                  if ((0x100002600L & l) != 0L)
                     { jjCheckNAddStates(12, 14); }
                  break;
               case 43:
                  if (curChar == 10)
                     { jjCheckNAddStates(12, 14); }
                  break;
               case 44:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 43;
                  break;
               case 46:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 18)
                     kind = 18;
                  jjstateSet[jjnewStateCnt++] = 46;
                  break;
               case 47:
                  if (curChar == 63)
                     jjstateSet[jjnewStateCnt++] = 41;
                  break;
               case 48:
                  if ((0x100002600L & l) != 0L && kind > 19)
                     kind = 19;
                  break;
               case 49:
                  if (curChar == 10 && kind > 19)
                     kind = 19;
                  break;
               case 50:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 49;
                  break;
               case 51:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  { jjCheckNAddStates(0, 4); }
                  break;
               case 52:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  { jjCheckNAdd(52); }
                  break;
               case 53:
                  if ((0x3ff000000000000L & l) != 0L)
                     { jjCheckNAddTwoStates(53, 54); }
                  break;
               case 54:
                  if (curChar == 46)
                     { jjCheckNAdd(55); }
                  break;
               case 55:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 4)
                     kind = 4;
                  { jjCheckNAddTwoStates(55, 56); }
                  break;
               case 57:
                  if ((0x280000000000L & l) != 0L)
                     { jjCheckNAdd(58); }
                  break;
               case 58:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 4)
                     kind = 4;
                  { jjCheckNAdd(58); }
                  break;
               case 59:
                  if ((0x3ff000000000000L & l) != 0L)
                     { jjCheckNAddTwoStates(59, 60); }
                  break;
               case 61:
                  if ((0x280000000000L & l) != 0L)
                     { jjCheckNAdd(62); }
                  break;
               case 62:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 4)
                     kind = 4;
                  { jjCheckNAdd(62); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 3:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 15)
                        kind = 15;
                     { jjCheckNAdd(31); }
                  }
                  if (curChar == 102)
                     jjstateSet[jjnewStateCnt++] = 6;
                  else if (curChar == 116)
                     jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 0:
                  if (curChar == 101 && kind > 2)
                     kind = 2;
                  break;
               case 1:
                  if (curChar == 117)
                     { jjCheckNAdd(0); }
                  break;
               case 2:
                  if (curChar == 114)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 4:
                  if (curChar == 115)
                     { jjCheckNAdd(0); }
                  break;
               case 5:
                  if (curChar == 108)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 6:
                  if (curChar == 97)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 7:
                  if (curChar == 102)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 8:
                  if (curChar == 120)
                     { jjCheckNAdd(9); }
                  break;
               case 9:
                  if ((0x7eL & l) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  { jjCheckNAdd(9); }
                  break;
               case 12:
                  if ((0xffffffffefffffffL & l) != 0L)
                     { jjCheckNAddStates(8, 11); }
                  break;
               case 13:
                  if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 14;
                  break;
               case 14:
                  if ((0x14404410000000L & l) != 0L)
                     { jjCheckNAddStates(8, 11); }
                  break;
               case 15:
                  if (curChar == 117)
                     jjstateSet[jjnewStateCnt++] = 16;
                  break;
               case 16:
                  if ((0x7eL & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 17;
                  break;
               case 17:
                  if ((0x7eL & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 18;
                  break;
               case 18:
                  if ((0x7eL & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 19;
                  break;
               case 19:
                  if ((0x7eL & l) != 0L)
                     { jjCheckNAddStates(8, 11); }
                  break;
               case 20:
                  if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 30:
               case 31:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 15)
                     kind = 15;
                  { jjCheckNAdd(31); }
                  break;
               case 33:
               case 34:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  { jjCheckNAdd(34); }
                  break;
               case 39:
               case 40:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 17)
                     kind = 17;
                  { jjCheckNAdd(40); }
                  break;
               case 45:
               case 46:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 18)
                     kind = 18;
                  { jjCheckNAdd(46); }
                  break;
               case 56:
                  if (curChar == 101)
                     { jjAddStates(15, 16); }
                  break;
               case 60:
                  if (curChar == 101)
                     { jjAddStates(17, 18); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 12:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     { jjAddStates(8, 11); }
                  break;
               default : if (i1 == 0 || l1 == 0 || i2 == 0 ||  l2 == 0) break; else break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 63 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   52, 53, 54, 59, 60, 36, 38, 39, 12, 13, 20, 21, 42, 44, 45, 57, 
   58, 61, 62, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec2[i2] & l2) != 0L);
      default :
         if ((jjbitVec0[i1] & l1) != 0L)
            return true;
         return false;
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", "\156\165\154\154", null, null, null, null, null, null, "\156\157\164", 
"\157\162", "\141\156\144", null, null, "\53", null, null, null, null, null, null, "\54", 
"\77\72", "\77", "\72", "\55", "\133", "\135", "\77\133", "\50", "\51", 
"\44\151\152\56", "\44\151\152\77\56", };
protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(Exception e)
   {
      jjmatchedKind = 0;
      jjmatchedPos = -1;
      matchedToken = jjFillToken();
      return matchedToken;
   }
   image = jjimage;
   image.setLength(0);
   jjimageLen = 0;

   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         TokenLexicalActions(matchedToken);
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

void TokenLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      case 17 :
        image.append(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1)));
    matchedToken.image = CharMatcher.WHITESPACE.removeFrom(image.toString());
         break;
      case 18 :
        image.append(input_stream.GetSuffix(jjimageLen + (lengthOfMatch = jjmatchedPos + 1)));
    matchedToken.image = CharMatcher.WHITESPACE.removeFrom(image.toString());
         break;
      default :
         break;
   }
}
private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

    /** Constructor. */
    public ExpressionParserTokenManager(SimpleCharStream stream){

      if (SimpleCharStream.staticFlag)
            throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");

    input_stream = stream;
  }

  /** Constructor. */
  public ExpressionParserTokenManager (SimpleCharStream stream, int lexState){
    ReInit(stream);
    SwitchTo(lexState);
  }

  /** Reinitialise parser. */
  public void ReInit(SimpleCharStream stream)
  {
	
    jjmatchedPos = jjnewStateCnt = 0;
    curLexState = defaultLexState;
    input_stream = stream;
    ReInitRounds();
  }

  private void ReInitRounds()
  {
    int i;
    jjround = 0x80000001;
    for (i = 63; i-- > 0;)
      jjrounds[i] = 0x80000000;
  }

  /** Reinitialise parser. */
  public void ReInit( SimpleCharStream stream, int lexState)
  {
  
    ReInit( stream);
    SwitchTo(lexState);
  }

  /** Switch to specified lex state. */
  public void SwitchTo(int lexState)
  {
    if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
    else
      curLexState = lexState;
  }

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0xfff7ff3fL, 
};
static final long[] jjtoSkip = {
   0x80000L, 
};
    protected SimpleCharStream  input_stream;

    private final int[] jjrounds = new int[63];
    private final int[] jjstateSet = new int[2 * 63];

    private final StringBuilder jjimage = new StringBuilder();
    private StringBuilder image = jjimage;
    private int jjimageLen;
    private int lengthOfMatch;
    
    protected int curChar;
}
