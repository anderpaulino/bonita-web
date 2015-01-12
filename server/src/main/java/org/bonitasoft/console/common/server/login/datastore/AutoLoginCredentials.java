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
package org.bonitasoft.console.common.server.login.datastore;

import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;

/**
 * @author Vincent Elcrin
 */
public class AutoLoginCredentials implements Credentials {

    private SecurityProperties properties;

    private long tenantId;

    public AutoLoginCredentials(SecurityProperties properties, long tenantId) {
        this.properties = properties;
        this.tenantId = tenantId;
    }

    @Override
    public String getName() {
        return getSecurityProperties().getAutoLoginUserName();
    }

    @Override
    public String getPassword() {
        return getSecurityProperties().getAutoLoginPassword();
    }

    @Override
    public long getTenantId() {
        return tenantId;
    }

    private SecurityProperties getSecurityProperties() {
        return properties;
    }

}
