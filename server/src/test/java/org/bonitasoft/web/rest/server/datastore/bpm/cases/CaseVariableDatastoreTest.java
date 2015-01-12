/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Colin PUY
 */
public class CaseVariableDatastoreTest extends APITestWithMock {

    private CaseVariableDatastore datastore;

    @Mock
    private ProcessAPI processAPI;

    @Before
    public void initializeMocks() {
        initMocks(this);

        datastore = spy(new CaseVariableDatastore(null));
        doReturn(processAPI).when(datastore).getEngineProcessAPI();
    }

    @Test
    public void testUpdateVariableValue() throws Exception {
        long caseId = 1L;
        String name = "aName";
        String newValue = "newValue";

        datastore.updateVariableValue(caseId, name, String.class.getName(), newValue);

        verify(processAPI).updateProcessDataInstance(name, caseId, newValue);
    }

    @Test
    @Ignore("can't instanciate DataInstance - see engine to see how")
    public void findByCaseIdReturnCaseVariablesforASpecifiedCaseId() throws Exception {

        //        datastore.findByCaseId(1L, page, resultsByPage)
    }
}
