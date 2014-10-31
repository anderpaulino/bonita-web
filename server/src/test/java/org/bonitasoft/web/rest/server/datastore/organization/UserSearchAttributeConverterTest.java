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
package org.bonitasoft.web.rest.server.datastore.organization;

import static org.junit.Assert.assertEquals;

import org.bonitasoft.engine.identity.UserSearchDescriptor;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Colin PUY
 */
public class UserSearchAttributeConverterTest {

    private UserSearchAttributeConverter converter;

    @Before
    public void initConverter() {
        converter = new UserSearchAttributeConverter();
    }

    @Test
    public void convertFistName() {
        final String convert = converter.convert(UserItem.ATTRIBUTE_FIRSTNAME);

        assertEquals(UserSearchDescriptor.FIRST_NAME, convert);
    }

    @Test
    public void convertLastName() {
        final String convert = converter.convert(UserItem.ATTRIBUTE_LASTNAME);

        assertEquals(UserSearchDescriptor.LAST_NAME, convert);
    }

    @Test
    public void convertUserName() {
        final String convert = converter.convert(UserItem.ATTRIBUTE_USERNAME);

        assertEquals(UserSearchDescriptor.USER_NAME, convert);
    }

    @Test
    public void convertGroupId() {
        final String convert = converter.convert(UserItem.FILTER_GROUP_ID);

        assertEquals(UserSearchDescriptor.GROUP_ID, convert);
    }

    @Test
    public void convertManagerId() {
        final String convert = converter.convert(UserItem.ATTRIBUTE_MANAGER_ID);

        assertEquals(UserSearchDescriptor.MANAGER_USER_ID, convert);
    }

    @Test
    public void convertRoleId() {
        final String convert = converter.convert(UserItem.FILTER_ROLE_ID);

        assertEquals(UserSearchDescriptor.ROLE_ID, convert);
    }
}
