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
package org.bonitasoft.web.toolkit.client.ui.component.table.formatter;

/**
 * @author Séverin Moussel
 */
public class PrefixSuffixCellFormatter extends DefaultItemTableCellFormatter {

    private final String prefix;

    private final String suffix;

    public PrefixSuffixCellFormatter(final String prefix, final String suffix) {
        super();
        this.prefix = prefix;
        this.suffix = suffix;
    }

    private String concat(final String value) {
        final StringBuilder stringBuilder = new StringBuilder(this.prefix.length() + value.length() + this.suffix.length());

        stringBuilder
                .append(this.prefix)
                .append(value)
                .append(this.suffix);

        return stringBuilder.toString();
    }

    @Override
    protected String getBooleanText(final boolean booleanValue) {
        return concat(super.getBooleanText(booleanValue));
    }

    @Override
    protected String getStringText() {
        return concat(super.getStringText());
    }

    @Override
    protected String getEnumText(final String value) {
        return concat(super.getEnumText(value));
    }

    @Override
    protected String getDateText() {
        return concat(super.getDateText());
    }

    @Override
    protected String getDateTimeText() {
        return concat(super.getDateTimeText());
    }

}
