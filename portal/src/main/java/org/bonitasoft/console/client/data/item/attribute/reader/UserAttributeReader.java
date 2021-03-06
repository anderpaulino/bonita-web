/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.data.item.attribute.reader;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.CompoundAttributeReader;

/**
 * @author Séverin Moussel
 * 
 */
public class UserAttributeReader extends CompoundAttributeReader {

    protected static String TEMPLATE = _("%firstname% %lastname%");

    public UserAttributeReader() {
        this(null);
    }

    public UserAttributeReader(final String leadAttribute) {
        super(leadAttribute, TEMPLATE);
    }

    public static String readUser(final IItem item) {
        return new UserAttributeReader().setDefaultValue(item.getAttributeValue(UserItem.ATTRIBUTE_USERNAME)).read(item);
    }

    @Override
    protected String _read(final IItem item) {
        return super._read(item);
    }

}
