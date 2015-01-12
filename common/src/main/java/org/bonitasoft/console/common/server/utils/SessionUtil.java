/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.utils;

import java.util.Set;

import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;

/**
 * @author Ruiheng.Fan
 * @author Baptiste Mesta: add tenant id in session
 */
public class SessionUtil {

    public static void sessionLogin(final User user, final APISession apiSession, final Set<String> permissions, final HttpSession session) {
        session.setAttribute(LoginManager.USERNAME_SESSION_PARAM, user.getUsername());
        session.setAttribute(LoginManager.USER_SESSION_PARAM_KEY, user);
        session.setAttribute(LoginManager.API_SESSION_PARAM_KEY, apiSession);
        session.setAttribute(LoginManager.PERMISSIONS_SESSION_PARAM_KEY, permissions);
    }

    public static void sessionLogout(final HttpSession session) {
        session.removeAttribute(LoginManager.API_SESSION_PARAM_KEY);
        session.removeAttribute(LoginManager.USERNAME_SESSION_PARAM);
        session.removeAttribute(LoginManager.USER_SESSION_PARAM_KEY);
        session.removeAttribute(LoginManager.PERMISSIONS_SESSION_PARAM_KEY);
        session.invalidate();
    }
}
