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
package org.bonitasoft.console.client.admin.organization.users.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Map;

import org.bonitasoft.console.client.admin.organization.users.view.UserMoreDetailsAdminPage;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.action.form.AddItemFormAction;

/**
 * @author Yongtao Guo
 */
public class AddUserFormAction extends AddItemFormAction<UserItem> {

    public AddUserFormAction() {
        super(UserDefinition.get());
    }

    @Override
    public void execute() {
        final String password = this.getParameter(new JsId(UserItem.ATTRIBUTE_PASSWORD));
        final String confirmPassword = this.getParameter(new JsId(UserItem.ATTRIBUTE_PASSWORD + "_confirm"));

        if (!password.equals(confirmPassword)) {
            getForm().addError(new JsId(UserItem.ATTRIBUTE_PASSWORD + "_confirm"), _("Passwords don't match"));
        } else {
            super.execute(createAPIAddCallback());
        }
    }

    private APICallback createAPIAddCallback() {
        return new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                if (!StringUtil.isBlank(response)) {
                    redirectToUsersMoreDetailsPage(parseJsonToUserItem(response).getId());
                }
            }

            @Override
            public void onError(final String message, final Integer errorCode) {
                getForm().addError(new JsId(UserItem.ATTRIBUTE_PASSWORD), getMessageFromErrorMessage(message));
            }
        };
    }

    private String getMessageFromErrorMessage(String message) {
        int indexStartMessage = message.indexOf("\"message\"");
        if (indexStartMessage >= 0) {
            indexStartMessage = indexStartMessage + 11;

            message = message.substring(indexStartMessage);
            final int endOfMessage = message.indexOf("\"");
            if (endOfMessage >= 0) {
                message = message.substring(0, endOfMessage);
                message = message.replace("\\r\\n", ", ");
                message = message.substring(0, message.length() - 2);
                return message;
            }
            return message;
        }
        return message;
    }

    private void redirectToUsersMoreDetailsPage(final APIID userId) {
        if (userId != null) {
            final RedirectionAction redirectionAction = new RedirectionAction(UserMoreDetailsAdminPage.TOKEN);
            redirectionAction.addParameter("id", userId.toString());
            redirectionAction.execute();
        }
    }

    private UserItem parseJsonToUserItem(final String json) {
        final UserItem user = JSonItemReader.parseItem(json, UserDefinition.get());
        if (user != null) {
            return user;
        }
        throw new RuntimeException("Couldn't parse json into user properly. <" + json + ">");
    }
}
