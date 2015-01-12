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
package org.bonitasoft.web.rest.server.api.bpm.connector;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.web.rest.server.BonitaRestAPIServlet;
import org.bonitasoft.web.rest.server.datastore.bpm.connector.ConnectorInstanceDatastore;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

/**
 * @author Vincent Elcrin
 */
public class APIConnectorInstanceTest {

    @Spy
    private APIConnectorInstance spiedAPIConnectorInstance;

    @Mock
    private ConnectorInstanceDatastore mockedConnectorInstanceDatastore;

    @BeforeClass
    public static void initEnvironnement() {
        I18n.getInstance();
        new BonitaRestAPIServlet();
    }

    @Before
    public void init() {
        initMocks(this);
        doReturn(this.mockedConnectorInstanceDatastore).when(this.spiedAPIConnectorInstance).defineDefaultDatastore();
    }

    @Test
    public void testDatastoreSearchIsCalled() {
        final int page = 1;
        final int resultsByPage = 2;
        final String search = "search";
        final String orders = "orders";
        final Map<String, String> filters = new HashMap<String, String>();

        this.spiedAPIConnectorInstance.search(page, resultsByPage, search, orders, filters);

        verify(this.mockedConnectorInstanceDatastore).search(page, resultsByPage, search, orders, filters);
    }

}
