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
package org.bonitasoft.console.client.common.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;

/**
 * @author Séverin Moussel
 */
public abstract class SelectMembershipAndDoPageOnItem<T extends IItem> extends SelectItemAndDoPageOnItem<T> {

    protected static final String GROUP_ID = "group_id";

    protected static final String ROLE_ID = "role_id";

    public SelectMembershipAndDoPageOnItem(final APIID itemId, final ItemDefinition itemDefinition) {
        super(itemId, itemDefinition);
    }

    public SelectMembershipAndDoPageOnItem(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    public SelectMembershipAndDoPageOnItem(final String itemId, final ItemDefinition itemDefinition) {
        super(itemId, itemDefinition);
    }

    /**
     * Override this method to change the name of the parameter passed to the callback action
     * 
     * @return This method must return the name of the parameter
     */
    protected String defineRoleIdParameterName() {
        return ROLE_ID;
    }

    /**
     * Override this method to change the name of the parameter passed to the callback action
     * 
     * @return This method must return the name of the parameter
     */
    protected String defineGroupIdParameterName() {
        return GROUP_ID;
    }

    @Override
    protected List<SelectItemAndDoEntry> defineEntries() {
        return Arrays.asList(
                new SelectItemAndDoEntry(
                        defineGroupIdParameterName(),
                        _("Select a group"),
                        _("Select a group by typing a part of its name."),
                        GroupDefinition.get(),
                        new AttributeReader(GroupItem.ATTRIBUTE_DISPLAY_NAME),
                        GroupItem.ATTRIBUTE_ID),
                new SelectItemAndDoEntry(
                        defineRoleIdParameterName(),
                        _("Select a role"),
                        _("Select a role by typing a part of its name."),
                        RoleDefinition.get(),
                        new AttributeReader(RoleItem.ATTRIBUTE_DISPLAY_NAME),
                        RoleItem.ATTRIBUTE_ID)
                );
    }
}
