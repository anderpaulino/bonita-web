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
package org.bonitasoft.web.toolkit.client.ui.component.form.button;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;

/**
 * @author Séverin Moussel
 */
public class FormButtonCancel extends FormButton {

    public FormButtonCancel() {
        this(new JsId("cancel"), _("Cancel"), _("Cancel this form"));
    }

    public FormButtonCancel(final JsId jsid, final String label, final String tooltip) {
        super(jsid, label, tooltip, new HistoryBackAction());
    }

    @Override
    protected void postProcessHtml() {
        this.element.setAttribute("rel", "cancel");
        super.postProcessHtml();
    }

}
