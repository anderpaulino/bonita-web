/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.common.json;

import java.util.HashMap;

/**
 * @author Séverin Moussel
 * 
 */
public class JSonUtil {

    public static String quote(final String value) {
        return "\"" + escape(value) + "\"";
    }

    public static String escape(final String string) {
        if (string == null || string.length() == 0) {
            return "";
        }

        char b;
        char c = 0;
        String hhhh;
        int i;
        final int len = string.length();
        String sb = new String();

        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    sb += '\\';
                    sb += c;
                    break;
                case '/':
                    if (b == '<') {
                        sb += '\\';
                    }
                    sb += c;
                    break;
                case '\b':
                    sb += "\\b";
                    break;
                case '\t':
                    sb += "\\t";
                    break;
                case '\n':
                    sb += "\\n";
                    break;
                case '\f':
                    sb += "\\f";
                    break;
                case '\r':
                    sb += "\\r";
                    break;
                default:
                    if (c < ' ' || c >= '\u0080' && c < '\u00a0' ||
                            c >= '\u2000' && c < '\u2100') {
                        hhhh = "000" + Integer.toHexString(c);
                        sb += "\\u" + hhhh.substring(hhhh.length() - 4);
                    } else {
                        sb += c;
                    }
            }
        }

        return sb;
    }

    private static Character next(final String string, int currentPos) {
        currentPos++;
        if (string.length() > currentPos) {
            return string.charAt(currentPos);
        }
        return null;
    }

    private static String next(final String string, Integer currentPos, final int length) {
        currentPos++;
        if (string.length() > currentPos + length) {
            return string.substring(currentPos, length);
        }
        return null;
    }

    public static String unquote(final String string) {
        if (string == null) {
            return string;
        }

        String result = string.trim();
        if (result.startsWith("\"") && result.endsWith("\"")) {
            result = result.substring(1, result.length() - 1);
        }

        return unescape(result);
    }

    public static String unescape(final String string) {
        final int len = string.length();
        String sb = new String();
        int i = -1;
        while (i < len - 1) {
            Character c = next(string, i);
            i++;
            switch (c) {
                case 0:
                case '\n':
                case '\r':
                    // TODO log "Unterminated string";
                    return null;
                case '\\':
                    c = next(string, i);
                    if (c == null) {
                        // TODO log "Unterminated escape";
                        return null;
                    }
                    i++;
                    switch (c) {
                        case 'b':
                            sb += '\b';
                            break;
                        case 't':
                            sb += '\t';
                            break;
                        case 'n':
                            sb += '\n';
                            break;
                        case 'f':
                            sb += '\f';
                            break;
                        case 'r':
                            sb += '\r';
                            break;
                        case 'u':
                            final String cc = next(string, i, 4);
                            if (cc == null) {
                                // TODO log "Unterminated unicode escape";
                                return null;
                            }
                            i += 4;
                            sb += Integer.parseInt(cc, 16);
                            break;
                        case '"':
                        case '\'':
                        case '\\':
                        case '/':
                            sb += c;
                            break;
                        default:
                            // TODO log "Illegal escape character.";
                            return null;
                    }
                    break;
                default:
                    sb += c;
            }
        }
        return sb;
    }

    public static HashMap<String, String> unescape(final HashMap<String, String> values) {
        final HashMap<String, String> unescapeMap = new HashMap<String, String>();
        for (final String valueName : values.keySet()) {
            unescapeMap.put(valueName, unquote(values.get(valueName)));
        }
        return unescapeMap;
    }

}
