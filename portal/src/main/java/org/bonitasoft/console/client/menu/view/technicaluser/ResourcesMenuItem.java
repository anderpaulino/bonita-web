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
package org.bonitasoft.console.client.menu.view.technicaluser;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.page.view.PageListingPage;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuFolder;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuLink;

/**
 * @author Colin PUY
 *
 */
public class ResourcesMenuItem extends MenuFolder {

    public ResourcesMenuItem() {
        super(new JsId("Resources"), _("Resources"));
        addMenuItem(new MenuLink(new JsId("businessdatamodelimport"),_("Business Data Model"),  _("Business Data Model"), "businessdatamodelimport"));
//      TO DO: addMenuItem(new MenuLink(new JsId(BDMImportPage.TOKEN), _("Business Data Model"), _("Business Data Model"), BDMImportPage.TOKEN));
        addMenuItem(new MenuLink(new JsId(PageListingPage.TOKEN), _("Custom Pages"), _("Manage custom pages"), PageListingPage.TOKEN));
    }

}
