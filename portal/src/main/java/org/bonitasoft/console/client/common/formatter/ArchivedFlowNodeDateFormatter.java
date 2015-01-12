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
package org.bonitasoft.console.client.common.formatter;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeItem;
import org.bonitasoft.web.toolkit.client.ui.component.Span;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.ItemTableCellFormatter;

/**
 * Formatter for archived date
 * Add different prefix for task state
 * 
 * @author Colin PUY
 */
public class ArchivedFlowNodeDateFormatter extends ItemTableCellFormatter {

    @Override
    public void execute() {
        ArchivedFlowNodeItem task = (ArchivedFlowNodeItem) getItem();
        if (task.isAborted()) {
            table.addCell(newPrefixSpan(_("Aborted on:")), new Span(getText()));
        } else {
            table.addCell(newPrefixSpan(_("Performed on:")), new Span(getText()));
        }
    }

    private String getText() {
        return attributeReader.read(item);
    }

    protected Component newPrefixSpan(String prefix) {
        return new Span(prefix).addClass("prepend");
    }
}
