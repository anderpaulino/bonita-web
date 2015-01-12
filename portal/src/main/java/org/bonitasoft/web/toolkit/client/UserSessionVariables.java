/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Paul AMAR
 */
public class UserSessionVariables {

    public static final String API_TOKEN = "token_api";

    private static Map<String, String> userVariables = new HashMap<String, String>();

    public static void addUserVariable(String name, String value) {
        userVariables.put(name, value);
    }

    public static String getUserVariable(String name) {
        return userVariables.get(name);
    }

    public static void removeUserVariable(String name) {
        userVariables.remove(name);
    }
}
