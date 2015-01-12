package org.bonitasoft.forms.server.api.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.identity.UserNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.server.api.IFormExpressionsAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FormWorkflowAPIImplTest {

    @Mock
    private APISession session;

    private final Locale locale = new Locale("en");

    @Mock
    private Map<String, Serializable> context;

    @Mock
    private List<FormAction> actions;

    @Mock
    private ProcessAPI processApi;

    @Mock
    private IFormExpressionsAPI formExpressionsAPI;
    @Spy
    private FormWorkflowAPIImpl formWorkflowAPIImpl;

    @Mock
    private List<Expression> expressions;
    private long userId;
    private long processInstanceId;

    @Before
    public void setUp() throws Exception {
        formWorkflowAPIImpl = spy(new FormWorkflowAPIImpl());
        expressions = new ArrayList<Expression>();
        doReturn(processApi).when(formWorkflowAPIImpl).getProcessApi(session);
    }

    @Test
    public void should_canUserSeeProcessInstance_call_engine_api() throws Exception {
        final boolean expected = true;
        checkWhenApiReturn(expected);
    }

    @Test
    public void it_should_call_evaluateActivityInitialExpressions() throws Exception {
        // Given
        formWorkflowAPIImpl = spy(new FormWorkflowAPIImpl());
        final long processDefinitionID = -1;
        final long activityInstanceID = 1;
        expressions = new ArrayList<Expression>();
        // When
        formWorkflowAPIImpl.getEvaluateConditionExpressions(session, actions, locale, context,
                processDefinitionID, activityInstanceID, formExpressionsAPI);

        // Then
        verify(formExpressionsAPI).evaluateActivityInitialExpressions(session, activityInstanceID, expressions, locale, true, context);
        verify(formExpressionsAPI, never()).evaluateProcessInitialExpressions(session, processDefinitionID, expressions, locale, context);
    }

    @Test
    public void should_canUserSeeProcessInstance_call_engine_api_false() throws Exception {
        final boolean expected = false;
        checkWhenApiReturn(expected);
    }

    @Test
    public void it_should_call_evaluateProcessInitialExpressions() throws Exception {
        // Given
        final long processDefinitionID = 1;
        final long activityInstanceID = -1;
        // When
        formWorkflowAPIImpl.getEvaluateConditionExpressions(session, actions, locale, context,
                processDefinitionID, activityInstanceID, formExpressionsAPI);

        // Then
        verify(formExpressionsAPI).evaluateProcessInitialExpressions(session, processDefinitionID, expressions, locale, context);
        verify(formExpressionsAPI, never()).evaluateActivityInitialExpressions(session, activityInstanceID, expressions, locale, true, context);
    }

    private void checkWhenApiReturn(final boolean expected) throws ProcessInstanceNotFoundException, UserNotFoundException, BPMEngineException,
            ProcessDefinitionNotFoundException {
        userId = 25L;
        processInstanceId = 1L;
        //given
        doReturn(userId).when(session).getUserId();
        doReturn(expected).when(processApi).isInvolvedInProcessInstance(userId, processInstanceId);
        //when
        final boolean canUserSeeProcessInstance = formWorkflowAPIImpl.canUserSeeProcessInstance(session, processInstanceId);

        //then
        verify(processApi).isInvolvedInProcessInstance(userId, processInstanceId);
        assertThat(canUserSeeProcessInstance).as("should return " + expected).isEqualTo(expected);
    }

    @Test
    public void it_should_not_call_evaluateProcessInitialExpressions_nor_evaluateActivityInitialExpressions() throws Exception {
        // Given
        final long processDefinitionID = -1;
        final long activityInstanceID = -1;
        // When
        formWorkflowAPIImpl.getEvaluateConditionExpressions(session, actions, locale, context,
                processDefinitionID, activityInstanceID, formExpressionsAPI);
        // Then
        verify(formExpressionsAPI, never()).evaluateProcessInitialExpressions(session, processDefinitionID, expressions, locale, context);
        verify(formExpressionsAPI, never()).evaluateActivityInitialExpressions(session, activityInstanceID, expressions, locale, true, context);
    }

}
