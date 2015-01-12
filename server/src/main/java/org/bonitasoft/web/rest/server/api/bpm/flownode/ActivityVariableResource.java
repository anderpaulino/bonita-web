package org.bonitasoft.web.rest.server.api.bpm.flownode;

import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.resource.Get;

public class ActivityVariableResource extends CommonResource {

    public static final String ACTIVITYDATA_ACTIVITY_ID = "activityid";
    public static final String ACTIVITYDATA_DATA_NAME = "dataname";

    @Get("json")
    public DataInstance getTaskVariable() {
        try {
            final String taskId = getAttribute(ACTIVITYDATA_ACTIVITY_ID);
            final String dataName = getAttribute(ACTIVITYDATA_DATA_NAME);
            return getTaskVariableInstance(dataName, Long.valueOf(taskId));
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    private DataInstance getTaskVariableInstance(final String dataName, final Long activityInstanceId) throws DataNotFoundException {
        return getEngineProcessAPI().getActivityDataInstance(dataName, activityInstanceId);
    }
}
