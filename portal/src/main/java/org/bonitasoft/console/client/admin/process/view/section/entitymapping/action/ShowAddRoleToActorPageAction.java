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
package org.bonitasoft.console.client.admin.process.view.section.entitymapping.action;

import org.bonitasoft.console.client.admin.process.view.section.entitymapping.SelectRoleForActorPage;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

/**
 * @author Colin PUY
 */
public final class ShowAddRoleToActorPageAction extends Action {

    /**
     * 
     */
    private final String actorId;

    public ShowAddRoleToActorPageAction(String actorId) {
        this.actorId = actorId;
    }

    @Override
    public void execute() {
        final SelectRoleForActorPage popup = new SelectRoleForActorPage();
        popup.addParameter("id", actorId);
        ViewController.showPopup(popup);
    }
}
