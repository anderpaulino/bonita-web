/*
 * Copyright (C) 2014 BonitaSoft S.A.
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



import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.Logger
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.api.permission.APICallContext
import org.bonitasoft.engine.api.permission.PermissionRule
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstanceNotFoundException
import org.bonitasoft.engine.bpm.flownode.UserTaskInstance
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.impl.SearchResultImpl
import org.bonitasoft.engine.session.APISession
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner.class)
public class TaskPermissionRuleTest {

    @Mock
    def APISession apiSession
    @Mock
    def APICallContext apiCallContext
    @Mock
    def APIAccessor apiAccessor
    @Mock
    def Logger logger
    def PermissionRule rule = new TaskPermissionRule()
    @Mock
    def ProcessAPI processAPI
    @Mock
    def IdentityAPI identityAPI
    @Mock
    def User user
    def long currentUserId = 16l

    @Before
    public void before() {
        doReturn(processAPI).when(apiAccessor).getProcessAPI()
        doReturn(identityAPI).when(apiAccessor).getIdentityAPI()
        doReturn(user).when(identityAPI).getUser(currentUserId)
        doReturn(currentUserId).when(apiSession).getUserId()
    }


    def havingFilters(Map filters) {
        doReturn(true).when(apiCallContext).isGET()
        doReturn(filters).when(apiCallContext).getFilters()
    }

    @Test
    public void should_check_verify_filters_on_GET_with_same_user_involved() {
        //given
        havingFilters([user_id: "16"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_filters_on_GET_with_same_hidden_user_involved() {
        //given
        havingFilters([hidden_user_id: "16"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_filters_on_GET_with_same_assigned_user_involved() {
        //given
        havingFilters([assigned_id: "16"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_filters_on_GET_with_diff_user_involved() {
        //given
        havingFilters([user_id: "17"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_filters_on_GET_with_diff_hidden_user_involved() {
        //given
        havingFilters([hidden_user_id: "17"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_get_on_a_archived_task_is_ok() {
        //given
        havingResource("archivedElement")
        def archivedTask = mock(ArchivedHumanTaskInstance.class)
        doReturn(currentUserId).when(archivedTask).getAssigneeId()
        doReturn(archivedTask).when(processAPI).getArchivedActivityInstance(458)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_get_on_a_archived_task_is_not_the_assignee() {
        //given
        havingResource("archivedElement")
        def archivedTask = mock(ArchivedHumanTaskInstance.class)
        doReturn(58l).when(archivedTask).getAssigneeId()
        doReturn(archivedTask).when(processAPI).getArchivedActivityInstance(458)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_get_on_a_archived_task_is_not_found() {
        //given
        havingResource("archivedElement")
        doThrow(new ActivityInstanceNotFoundException(458)).when(processAPI).getArchivedActivityInstance(458)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }


    @Test
    public void should_get_on_a_assigned_human_task_is_ok() {
        //given
        havingResource("humanTask")
        def instance = mock(UserTaskInstance.class)
        doReturn(currentUserId).when(instance).getAssigneeId()
        doReturn(instance).when(processAPI).getFlowNodeInstance(458)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_get_on_a_assigned_human_task_is_not_ok() {
        //given
        havingResource("humanTask")
        def instance = mock(UserTaskInstance.class)
        doReturn(59l).when(instance).getAssigneeId()
        doReturn(instance).when(processAPI).getFlowNodeInstance(458)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_get_on_an_unexisting_flow_node() {
        //given
        havingResource("humanTask")
        doThrow(new FlowNodeInstanceNotFoundException(new Exception())).when(processAPI).getFlowNodeInstance(458)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }


    @Test
    public void should_get_on_a_pending_task_is_ok() {
        //given
        havingResource("humanTask")
        def instance = mock(UserTaskInstance.class)
        doReturn(-1l).when(instance).getAssigneeId()
        doReturn(instance).when(processAPI).getFlowNodeInstance(458)
        doReturn(new SearchResultImpl(1,[])).when(processAPI).searchUsersWhoCanExecutePendingHumanTask(eq(458l), any(SearchOptions.class))
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_get_on_a_pending_task_is_not_ok() {
        //given
        havingResource("humanTask")
        def instance = mock(UserTaskInstance.class)
        doReturn(-1l).when(instance).getAssigneeId()
        doReturn(instance).when(processAPI).getFlowNodeInstance(458)
        doReturn(new SearchResultImpl(0,[])).when(processAPI).searchUsersWhoCanExecutePendingHumanTask(eq(458l), any(SearchOptions.class))
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_PUT_on_assigned_human_task_is_ok() {
        //given
        doReturn(true).when(apiCallContext).isPUT()
        doReturn("458").when(apiCallContext).getResourceId()
        doReturn("humanTask").when(apiCallContext).getResourceName()
        def instance = mock(UserTaskInstance.class)
        doReturn(currentUserId).when(instance).getAssigneeId()
        doReturn(instance).when(processAPI).getFlowNodeInstance(458)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }



    def havingResource(String resourceName) {
        doReturn(true).when(apiCallContext).isGET()
        doReturn("458").when(apiCallContext).getResourceId()
        doReturn(resourceName).when(apiCallContext).getResourceName()
    }
}
