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
package org.bonitasoft.console.client.admin.bpm.cases.action;

import org.bonitasoft.console.client.admin.bpm.task.view.TaskMoreDetailsAdminPage;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableActionSet;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;

/**
 * Redirection to the right page for an ArchivedTask
 * Only redirect ArchivedHumanTask
 * 
 * @author Colin PUY
 */
public class ArchivedTaskRedirectionAction extends ItemTableActionSet<ArchivedFlowNodeItem> {

    @Override
    protected void defineActions(ArchivedFlowNodeItem flowNode) {
        addAction(new ItemTableAction("", "",
                new ActionShowView(createView(flowNode.getId()))),
                true);
    }

    private RawView createView(APIID apiid) {
        RawView page = getTaskMoreDetailsPage();
        page.addParameter(PageOnItem.PARAMETER_ITEM_ID, apiid.toString());
        return page;
    }

    protected RawView getTaskMoreDetailsPage() {
        TaskMoreDetailsAdminPage page = new TaskMoreDetailsAdminPage(true);
        return page;
    }

}
