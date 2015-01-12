/*
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

package org.bonitasoft.web.toolkit.client.common.util;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

/**
 * Created by Vincent Elcrin
 * Date: 11/10/13
 * Time: 14:15
 */
public class StringUtilTest {

    @Test
    public void testWeUseDefaultStringWhenWeEnsureOnBlankString() throws Exception {
        String result = StringUtil.ensure(" ", "default");

        assertEquals("default", result);
    }

    @Test
    public void testWeUseDefaultStringWhenWeEnsureOnNullString() throws Exception {
        String result = StringUtil.ensure(null, "default");

        assertEquals("default", result);
    }

    @Test
    public void testWeCanRetrieveTheStringValueWithEnsure() throws Exception {
        String result = StringUtil.ensure("value", "default");

        assertEquals("value", result);
    }
}
