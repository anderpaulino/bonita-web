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
package org.bonitasoft.web.rest.server.api.bpm.process;

import org.bonitasoft.web.rest.model.bpm.process.ProcessCategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessCategoryItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.bpm.process.ProcessCategoryDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.Datastore;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 */
public class APIProcessCategory extends ConsoleAPI<ProcessCategoryItem> implements
        APIHasAdd<ProcessCategoryItem>,
        APIHasDelete {

    @Override
    protected ItemDefinition defineItemDefinition() {
        return ProcessCategoryDefinition.get();
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new ProcessCategoryDatastore(getEngineSession());
    }

}
