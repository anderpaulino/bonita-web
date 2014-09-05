/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.server.rest.resources;

import java.util.List;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.contract.Input;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;

@Path("tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    private ProcessAPI processAPI;

    @Inject
    public TaskResource(ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @GET
    @Path("{taskId}/contract")
    public ContractDefinition getContract(@PathParam("taskId") long taskId) throws UserTaskNotFoundException {
        return processAPI.getUserTaskContract(taskId);
    }

    @POST
    @Path("{taskId}/execute")
    public void executeTask(@PathParam("taskId") long taskId, List<Input> inputs) throws UserTaskNotFoundException,
            FlowNodeExecutionException, ContractViolationException {
        processAPI.executeUserTask(taskId, inputs);
    }
    
    @POST
    @Path("poc")
    public Response poc() {
        return Response.ok().entity("Hello POC").build();
    }
    
    @POST
    @Path("poc/json")
    public Response json(JsonObject o) {
        System.out.println(o.toString());
        return Response.ok().entity(o).build();
    }
    
}
