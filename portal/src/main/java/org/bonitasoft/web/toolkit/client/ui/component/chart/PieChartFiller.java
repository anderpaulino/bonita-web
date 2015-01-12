/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component.chart;

import java.util.List;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 */
class PieChartFiller extends ChartFiller<PieChart> {

    private String labelAttributeName = null;

    private String valueAttributeName = null;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public PieChartFiller(final ItemDefinition itemDefinition, final String labelAttributeName,
            final String valueAttributeName) {
        super(itemDefinition);
        this.labelAttributeName = labelAttributeName;
        this.valueAttributeName = valueAttributeName;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SET DATA
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void setData(final List<IItem> items) {

        for (final IItem item : items) {
            this.target.addSlice(
                    item.getAttributeValue(this.labelAttributeName),
                    Long.valueOf(item.getAttributeValue(this.valueAttributeName))
                    );
        }

    }

}
