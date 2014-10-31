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
package org.bonitasoft.web.rest.server.datastore.organization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.impl.SearchResultImpl;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverterException;
import org.bonitasoft.web.rest.server.engineclient.ProcessEngineClient;
import org.bonitasoft.web.rest.server.engineclient.UserEngineClient;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDatastoreTest {

    @Mock
    private ProcessAPI processAPI;

    @Mock
    private ProcessEngineClient processEngineClient;

    @Mock
    private IdentityAPI mockedIdentityAPI;

    @Mock
    UserItemConverter userItemConverter;

    @Spy
    @InjectMocks
    private final UserDatastore datastore = new UserDatastore(null);

    @Before
    public void init() {
        final UserEngineClient userEngineClient = new UserEngineClient(mockedIdentityAPI);
        doReturn(userEngineClient).when(datastore).getUserEngineClient();
        doReturn(processEngineClient).when(datastore).getProcessEngineClient();
        when(processEngineClient.getProcessApi()).thenReturn(processAPI);
    }

    @Test
    public void testSearchWithMultipleSortOrderDontThrowException() throws Exception {
        final String sort = UserItem.ATTRIBUTE_FIRSTNAME + "," + UserItem.ATTRIBUTE_LASTNAME;
        Mockito.doReturn(new SearchResultImpl<User>(0, Collections.<User> emptyList())).when(mockedIdentityAPI).searchUsers(Mockito.any(SearchOptions.class));

        try {
            datastore.search(0, 1, "search", Collections.<String, String> emptyMap(), sort);

        } catch (final AttributeConverterException e) {
            Assert.fail("Search should be able to handle multple sort");
        }
    }

    @Test
    public void testSearchUsersWhoCanPerformTask_with_should_return_nothing() {
        when(processAPI.searchUsersWhoCanExecutePendingHumanTask(eq(0L), any(SearchOptions.class))).thenReturn(mock(SearchResult.class));
        final ItemSearchResult<UserItem> results = datastore.searchUsersWhoCanPerformTask("0", 0, 10, "jan", Collections.<String, String> emptyMap(), "");
        verify(processAPI, times(1)).searchUsersWhoCanExecutePendingHumanTask(anyLong(), any(SearchOptions.class));
        assertThat(results.getLength()).isEqualTo(10);
        assertThat(results.getPage()).isEqualTo(0);
        assertThat(results.getTotal()).isEqualTo(0);
        assertThat(results.getResults()).isEmpty();
    }

    @Test
    public void testSearchUsersWhoCanPerformTask_with_should_return_one_result() {
        @SuppressWarnings("rawtypes")
        final SearchResult engineSearchResults = mock(SearchResult.class);
        final long expected = 1;
        when(engineSearchResults.getCount()).thenReturn(expected);
        final User user = mock(User.class);
        final String firstname = "Chuck";
        when(user.getFirstName()).thenReturn(firstname);
        final String lastname = "Norris";
        when(user.getLastName()).thenReturn(lastname);
        final List<User> userList = Lists.newArrayList(user);
        when(engineSearchResults.getResult()).thenReturn(userList);
        when(processAPI.searchUsersWhoCanExecutePendingHumanTask(eq(18L), any(SearchOptions.class))).thenReturn(engineSearchResults);
        final UserItem userItem = mock(UserItem.class);
        // when(userItem.getAttributeValue("firstname")).thenReturn(firstname);
        // when(userItem.getAttributeValue("lastname")).thenReturn(lastname);
        final List<UserItem> userItemList = Lists.newArrayList(userItem);
        when(userItemConverter.convert(userList)).thenReturn(userItemList);
        final int page = 1;
        final int resultsByPage = 8;
        final ItemSearchResult<UserItem> results = datastore.searchUsersWhoCanPerformTask("18", page, resultsByPage, "jan",
                Collections.<String, String> emptyMap(), "");
        assertThat(results.getLength()).isEqualTo(resultsByPage);
        assertThat(results.getPage()).isEqualTo(page);
        assertThat(results.getTotal()).isEqualTo(expected);
        assertThat(results.getResults()).isNotEmpty().hasSize(1).containsExactly(userItem);
        verify(processAPI, times(1)).searchUsersWhoCanExecutePendingHumanTask(anyLong(), any(SearchOptions.class));
        verify(userItemConverter, times(1)).convert(userList);
    }
}
