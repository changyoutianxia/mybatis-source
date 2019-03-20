/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.parsing;

/**
 * @author Clinton Begin
 */
public class GenericTokenParser {
  /**
   * 开始标志 ${
   */
  private final String openToken;
  /**
   * 结束标值 }
   */
  private final String closeToken;
  /**
   * 用于把parse 解析的字面量，将其获取实际的结果，并且重新返回拼装后的结果
   * 该token使用的是VariableTokenHandler
   */
  private final TokenHandler handler;

  public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
    this.openToken = openToken;
    this.closeToken = closeToken;
    this.handler = handler;
  }

  /**
   * 解析占位符返回结果
   * @param text
   * @return
   */
  public String parse(String text) {
    if (text == null || text.isEmpty()) {
      return "";
    }
    /**
     * 查找开始的位置 ${
     */
    // search open token
    int start = text.indexOf(openToken);
    if (start == -1) {
      return text;
    }
    char[] src = text.toCharArray();
    int offset = 0;
    /**
     * 用于记录解析后的字符串
     */
    final StringBuilder builder = new StringBuilder();
    /**
     * 用于记录占位符的字面量
     */
    StringBuilder expression = null;
    while (start > -1) {
      if (start > 0 && src[start - 1] == '\\') {
        // this open token is escaped. remove the backslash and continue.
        /**
         * 遇见转移开始的标记，直接将前面的字符串以及开始标记追缴到builder中
         */
        builder.append(src, offset, start - offset - 1).append(openToken);
        offset = start + openToken.length();
      } else {
        /**
         * 查找开始标记，并且没有转移，然后寻找结束token
         */
        // found open token. let's search close token.
        if (expression == null) {
          expression = new StringBuilder();
        } else {
          expression.setLength(0);
        }
        /**
         * 追加到builder中
         */
        builder.append(src, offset, start - offset);
        offset = start + openToken.length();
        /**
         * 查找结束标值位置
         * 同查找开始标记位置一样
         */
        int end = text.indexOf(closeToken, offset);
        while (end > -1) {
          if (end > offset && src[end - 1] == '\\') {
            // this close token is escaped. remove the backslash and continue.
            expression.append(src, offset, end - offset - 1).append(closeToken);
            offset = end + closeToken.length();
            end = text.indexOf(closeToken, offset);
          } else {
            /**
             * 如果找到了结束标记，将其中间的字符串加到expression中
             */
            expression.append(src, offset, end - offset);
            offset = end + closeToken.length();
            break;
          }
        }
        /**
         * 如果没有结束符，将开始到结束，全部进行追加
         */
        if (end == -1) {
          // close token was not found.
          builder.append(src, start, src.length - start);
          offset = src.length;
        } else {
          /**
           *  如果找到了结束符则将expression交给tokenHandler去解析出实际的值
           * 并且追加到结果中
           */
          builder.append(handler.handleToken(expression.toString()));
          offset = end + closeToken.length();
        }
      }
      /**
       * 移动start位置
       */
      start = text.indexOf(openToken, offset);
    }
    if (offset < src.length) {
      builder.append(src, offset, src.length - offset);
    }
    return builder.toString();
  }
}
