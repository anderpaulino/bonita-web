/**
 * Copyright (C) 2009 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.preferences.properties;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

/**
 * Utility class for security properties access
 *
 * @author Anthony Birembaut
 */
public class SecurityProperties {

    /**
     * Default name of the form definition file
     */
    public static final String SECURITY_DEFAULT_CONFIG_FILE_NAME = "security-config.properties";

    /**
     * property for the robustness of the password
     */
    public static final String PASSWORD_VALIDATOR_CLASSNAME = "security.password.validator";

    /**
     * property for the CSRF protection activation
     */
    public static final String CSRF_PROTECTION = "security.csrf.enabled";

    /**
     * property for the REST API Authorization checks activation
     */
    public static final String API_AUTHORIZATIONS_CHECK = "security.rest.api.authorizations.check.enabled";

    /**
     * Custom page debug mode
     */
    public static final String API_AUTHORIZATIONS_CHECK_DEBUG = "security.rest.api.authorizations.check.debug";

    /**
     * property for the auto login mechanism activation
     */
    public static final String AUTO_LOGIN_PROPERTY = "forms.application.login.auto";

    /**
     * property for the login to use for auto login
     */
    public static final String AUTO_LOGIN_USERNAME_PROPERTY = "forms.application.login.auto.username";

    /**
     * property for the password to use for auto login
     */
    public static final String AUTO_LOGIN_PASSWORD_PROPERTY = "forms.application.login.auto.password";

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(SecurityProperties.class.getName());

    /**
     * Instances attribute
     */
    private static Map<String, SecurityProperties> INSTANCES = new ConcurrentHashMap<String, SecurityProperties>();

    /**
     * default properties
     */
    protected Properties defaultProperties = new Properties();

    /**
     * Separator for the key of the instances map
     */
    protected final static String INSTANCES_MAP_SEPERATOR = "@";

    protected final static String TENANT_SCOPE_CONFIG_ID = "tenant";

    protected final static String PLATFORM_SCOPE_CONFIG_ID = "platform";

    /**
     * @return the {@link SecurityProperties} instance
     */
    public static SecurityProperties getInstance() {
        SecurityProperties securityProperties = INSTANCES.get(PLATFORM_SCOPE_CONFIG_ID);
        if (securityProperties == null) {
            securityProperties = new SecurityProperties(WebBonitaConstantsUtils.getInstance(), PLATFORM_SCOPE_CONFIG_ID);
            INSTANCES.put(PLATFORM_SCOPE_CONFIG_ID, securityProperties);
        }
        return securityProperties;
    }

    /**
     * @return the {@link SecurityProperties} instance
     */
    public static SecurityProperties getInstance(final long tenantId) {
        final String instanceKey = generateInstanceKey(tenantId, TENANT_SCOPE_CONFIG_ID);
        SecurityProperties securityProperties = INSTANCES.get(instanceKey);
        if (securityProperties == null) {
            securityProperties = new SecurityProperties(WebBonitaConstantsUtils.getInstance(tenantId), TENANT_SCOPE_CONFIG_ID);
            INSTANCES.put(instanceKey, securityProperties);
        }
        return securityProperties;
    }

    /**
     * @param tenantID
     * @param id
     * @return the {@link SecurityProperties} instance
     */
    public static SecurityProperties getInstance(final long tenantID, final ProcessIdentifier id) {
        final String instanceKey = generateInstanceKey(tenantID, id.getIdentifier());
        SecurityProperties securityProperties = INSTANCES.get(instanceKey);
        if (securityProperties == null) {
            securityProperties = new SecurityProperties(WebBonitaConstantsUtils.getInstance(tenantID), id.getIdentifier());
            INSTANCES.put(instanceKey, securityProperties);
        }
        return securityProperties;
    }

    /**
     * @param tenantID
     * @param processDefinitionId
     */
    public static void cleanProcessConfig(final long tenantID, final ProcessIdentifier processDefinitionId) {
        final String instanceKey = generateInstanceKey(tenantID, processDefinitionId.getIdentifier());
        INSTANCES.remove(instanceKey);
    }

    /**
     * Generate SecurityProperties INSTANCES key from ProcessDefinitionUUID
     *
     * @param tenantId
     * @param processDefinitionId
     * @return
     */
    private static String generateInstanceKey(final long tenantId, final String processDefinitionId) {
        return processDefinitionId + INSTANCES_MAP_SEPERATOR + tenantId;
    }

    SecurityProperties(final WebBonitaConstantsUtils webBonitaConstantsUtils, final String processDefinitionId) {
        InputStream inputStream = null;
        try {
            if (isValidProcessDefinition(processDefinitionId)) {
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(SECURITY_DEFAULT_CONFIG_FILE_NAME);
                if (inputStream == null) {
                    inputStream = new FileInputStream(getSecurityPropertyFile(webBonitaConstantsUtils, processDefinitionId));
                }
            } else {
                inputStream = new FileInputStream(getSecurityPropertyFile(webBonitaConstantsUtils));
            }
            defaultProperties.load(inputStream);
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "default security config file " + SECURITY_DEFAULT_CONFIG_FILE_NAME + " is missing from the forms conf directory");
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, "default security config file " + SECURITY_DEFAULT_CONFIG_FILE_NAME + " stream could not be closed.", e);
                    }
                }
            }
        }
    }

    private File getSecurityPropertyFile(final WebBonitaConstantsUtils webBonitaConstantsUtils, final String processDefinitionId) {
        File securityPropertiesFile = getProcessSecurityPropertiesFile(webBonitaConstantsUtils, processDefinitionId);
        if (securityPropertiesFile == null) {
            securityPropertiesFile = getSecurityPropertyFile(webBonitaConstantsUtils);
        }
        return securityPropertiesFile;
    }

    private File getSecurityPropertyFile(final WebBonitaConstantsUtils webBonitaConstantsUtils) {
        return new File(webBonitaConstantsUtils.getConfFolder(), SECURITY_DEFAULT_CONFIG_FILE_NAME);
    }

    private boolean isValidProcessDefinition(final String processDefinitionId) {
        return processDefinitionId != null && !TENANT_SCOPE_CONFIG_ID.equals(processDefinitionId) && !PLATFORM_SCOPE_CONFIG_ID.equals(processDefinitionId);
    }

    /**
     * Retrieve the config file in the extracted business archive
     *
     * @param webBonitaConstantsUtils
     * @param processDefinitionId
     *        the process definition ID
     * @return the config file or null if it doesn't exists
     * @throws IOException
     */
    protected File getProcessSecurityPropertiesFile(final WebBonitaConstantsUtils webBonitaConstantsUtils, final String processDefinitionId) {
        File securityPropertiesFile = null;
        final File processWorkFolder = getProcessWorkFolder(webBonitaConstantsUtils, processDefinitionId);
        if (processWorkFolder.exists()) {
            final long lastDeployementDate = getLastDeployementDate(
                    listProcessDeployementFolders(processWorkFolder));
            if (lastDeployementDate != 0L) {
                final File file = new File(processWorkFolder, lastDeployementDate + File.separator + SECURITY_DEFAULT_CONFIG_FILE_NAME);
                if (file.exists()) {
                    securityPropertiesFile = file;
                }
            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                            "Process resources deployment folder contains no directory that match a process deployment timestamp.");
                }
            }
        }
        return securityPropertiesFile;
    }

    private long getLastDeployementDate(final File[] directories) {
        long lastDeployementDate = 0L;
        for (final File directory : directories) {
            try {
                final long deployementDate = Long.parseLong(directory.getName());
                if (deployementDate > lastDeployementDate) {
                    lastDeployementDate = deployementDate;
                }
            } catch (final Exception e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                            "Process resources deployment folder contains a directory that does not match a process deployment timestamp: "
                                    + directory.getName(), e);
                }
            }
        }
        return lastDeployementDate;
    }

    private File getProcessWorkFolder(final WebBonitaConstantsUtils webBonitaConstantsUtils, final String processDefinitionId) {
        return new File(webBonitaConstantsUtils.getFormsWorkFolder(), processDefinitionId);
    }

    private File[] listProcessDeployementFolders(final File processDir) {
        return processDir.listFiles(new FileFilter() {

            @Override
            public boolean accept(final File pathname) {
                return pathname.isDirectory();
            }
        });
    }

    /**
     * @return the application form auto-login property
     */
    public boolean allowAutoLogin() {
        final String useAutoLogin = defaultProperties.getProperty(AUTO_LOGIN_PROPERTY);
        try {
            return Boolean.parseBoolean(useAutoLogin);
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * @return the auto-login username property
     */
    public String getAutoLoginUserName() {
        return defaultProperties.getProperty(AUTO_LOGIN_USERNAME_PROPERTY);
    }

    /**
     * @return the password validator property
     */
    public String getPasswordValidator() {
        return defaultProperties.getProperty(PASSWORD_VALIDATOR_CLASSNAME);
    }

    /**
     * @return the value to allow or not API authorization checks
     */
    public boolean isAPIAuthorizationsCheckEnabled() {
        final String res = defaultProperties.getProperty(API_AUTHORIZATIONS_CHECK);
        return res != null && res.equals("true");
    }

    /**
     * @return the value allow permission properties file debug
     */
    public boolean isAPIAuthorizationsCheckInDebugMode() {
        final String debugMode = defaultProperties.getProperty(API_AUTHORIZATIONS_CHECK_DEBUG);
        return Boolean.parseBoolean(debugMode);
    }

    /**
     * @return the value to allow or not CSRF protection
     */
    public boolean isCSRFProtectionEnabled() {
        final String res = defaultProperties.getProperty(CSRF_PROTECTION);
        return res != null && res.equals("true");
    }

    /**
     * @return the auto-login password property
     */
    public String getAutoLoginPassword() {
        return defaultProperties.getProperty(AUTO_LOGIN_PASSWORD_PROPERTY);
    }
}
