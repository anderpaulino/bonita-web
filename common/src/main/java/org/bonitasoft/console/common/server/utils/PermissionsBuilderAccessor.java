package org.bonitasoft.console.common.server.utils;

import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.CustomPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;

public class PermissionsBuilderAccessor {

    public static PermissionsBuilder createPermissionBuilder(final APISession session) throws LoginFailedException {
        ProfileAPI profileAPI;
        try {
            profileAPI = TenantAPIAccessor.getProfileAPI(session);
        } catch (final BonitaException e) {
            throw new LoginFailedException(e);
        }
        final SecurityProperties securityProperties = PropertiesFactory.getSecurityProperties(session.getTenantId());
        final CustomPermissionsMapping customPermissionsMapping = PropertiesFactory.getCustomPermissionsMapping(session.getTenantId());
        final CompoundPermissionsMapping compoundPermissionsMapping = PropertiesFactory.getCompoundPermissionsMapping(session.getTenantId());
        return new PermissionsBuilder(session, profileAPI, customPermissionsMapping, compoundPermissionsMapping, securityProperties);
    }
}
