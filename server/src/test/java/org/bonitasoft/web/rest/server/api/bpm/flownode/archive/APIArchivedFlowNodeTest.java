package org.bonitasoft.web.rest.server.api.bpm.flownode.archive;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.web.rest.server.APITestWithMock;
import org.junit.Test;

public class APIArchivedFlowNodeTest extends APITestWithMock {

    @Test
    public void should_have_default_search_order() throws Exception {
        final APIArchivedFlowNode apiActivity = new APIArchivedFlowNode();

        final String defineDefaultSearchOrder = apiActivity.defineDefaultSearchOrder();

        assertThat(defineDefaultSearchOrder).as("sould have a default search order").isEqualTo("displayName ASC");
    }
}
