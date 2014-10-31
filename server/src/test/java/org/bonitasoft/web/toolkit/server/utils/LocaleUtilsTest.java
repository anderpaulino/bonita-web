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

package org.bonitasoft.web.toolkit.server.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by Vincent Elcrin
 * Date: 10/10/13
 * Time: 10:25
 */
public class LocaleUtilsTest {

    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testLocalCanBeRetrieveFromCookie() {
        final Cookie[] cookies = {
                new Cookie("aCookie", "value"),
                new Cookie(LocaleUtils.BOS_LOCALE, "fr"),
                new Cookie("aNullCookie", null),
        };

        final String locale = LocaleUtils.getUserLocale(cookies);

        assertEquals("fr", locale);
    }

    @Test
    public void testANullCookieResultWithANull() {
        final Cookie[] cookies = null;

        final String locale = LocaleUtils.getUserLocale(cookies);

        assertEquals(null, locale);
    }

    @Test
    public void testWeCanRetrieveCookiesLocaleFromRequest() {
        final Cookie[] cookies = {
                new Cookie(LocaleUtils.BOS_LOCALE, "en_US"),
        };
        doReturn(cookies).when(request).getCookies();

        final String locale = LocaleUtils.getUserLocale(request);

        assertEquals("en_US", locale);
    }

    @Test
    public void testWeGetLocaleFromRequestWhenNotPresentInCookie() {
        doReturn(null).when(request).getCookies();
        doReturn(Locale.CHINA).when(request).getLocale();

        final String locale = LocaleUtils.getUserLocale(request);

        assertEquals(Locale.CHINA.toString(), locale);
    }
}
