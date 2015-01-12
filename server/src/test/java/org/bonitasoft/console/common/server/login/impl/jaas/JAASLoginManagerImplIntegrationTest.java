/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login.impl.jaas;

import static org.junit.Assert.fail;

import org.bonitasoft.console.common.server.AbstractJUnitWebTest;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.login.datastore.UserCredentials;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.test.toolkit.server.MockHttpServletRequest;
import org.junit.Test;

/**
 * @author Rohart Bastien
 */
public class JAASLoginManagerImplIntegrationTest extends AbstractJUnitWebTest {

    static {
        setSystemPropertyIfNotSet("java.security.auth.login.config", "src/test/resources/jaas-standard.cfg");
    }

    protected final static String TECH_USERNAME = "install";
    protected final static String TECH_PASSWORD = "install";

    private JAASLoginManagerImpl jaasLoginManagerImpl;

    @Override
    public void webTestSetUp() throws Exception {
        jaasLoginManagerImpl = new JAASLoginManagerImpl();
    }

    @Test
    public void testLogin() {
        HttpServletRequestAccessor requestAccessor = buildMockHttpServletAccessor();
        try {
            jaasLoginManagerImpl.login(requestAccessor, new UserCredentials(TECH_USERNAME, TECH_PASSWORD, -1L));
        } catch (LoginFailedException e) {
            fail("Cannot login " + e);
        }
    }

    protected HttpServletRequestAccessor buildMockHttpServletAccessor() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setParameter(HttpServletRequestAccessor.USERNAME_PARAM, TECH_USERNAME);
        httpServletRequest.setParameter(HttpServletRequestAccessor.PASSWORD_PARAM, TECH_PASSWORD);
        HttpServletRequestAccessor request = new HttpServletRequestAccessor(httpServletRequest);
        return request;
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }
}
