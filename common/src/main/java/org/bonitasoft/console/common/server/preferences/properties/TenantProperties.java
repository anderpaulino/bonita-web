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
package org.bonitasoft.console.common.server.preferences.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

/**
 * @author Ruiheng.Fan
 */
public class TenantProperties {

    /**
     * Default name of the preferences file
     */
    public static final String PROPERTIES_FILENAME = "bonita-web-preferences.properties";

    /**
     * Indicates that the preferences have been loaded
     */
    public static boolean preferencesLoaded = false;

    /**
     * Instances attribute
     */
    private static Map<Long, TenantProperties> INSTANCES = new HashMap<Long, TenantProperties>();

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(TenantProperties.class.getName());

    /**
     * The loaded properties
     */
    protected Properties properties = new Properties();

    /**
     * The properties file
     */
    protected File propertiesFile;

    /**
     * @return the {@link SecurityProperties} instance
     */
    protected static synchronized TenantProperties getInstance(final long tenantId) {
        TenantProperties tenancyProperties = INSTANCES.get(tenantId);
        if (tenancyProperties == null) {
            tenancyProperties = new TenantProperties(tenantId);
            INSTANCES.put(tenantId, tenancyProperties);
        }
        return tenancyProperties;
    }

    /**
     * Private contructor to prevent instantiation
     *
     * @throws IOException
     */
    protected TenantProperties(final long tenantId) {
        propertiesFile = new File(WebBonitaConstantsUtils.getInstance(tenantId).getConfFolder(), PROPERTIES_FILENAME);
        InputStream inputStream = null;
        try {
            if (!propertiesFile.exists()) {
                initProperties(propertiesFile);
            }
            inputStream = new FileInputStream(propertiesFile);
            properties.load(inputStream);
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Bonita web preferences file " + propertiesFile.getPath() + " could not be loaded.", e);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "Bonita web preferences file stream " + propertiesFile.getPath() + " could not be closed.", e);
                    }
                }
            }
        }
    }

    protected void initProperties(final File aPropertiesFile) throws IOException {
        // Create the file.
        aPropertiesFile.createNewFile();
    }

    public String getProperty(final String propertyName) {
        if (properties == null) {
            return null;
        }
        return properties.getProperty(propertyName);
    }

    public String getProperty(final String propertyName, final String defaultValue) {
        if (properties == null) {
            return defaultValue;
        }
        return properties.getProperty(propertyName, defaultValue);
    }

    public void removeProperty(final String propertyName) {
        if (properties != null) {
            properties.remove(propertyName);
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(propertiesFile);
                properties.store(outputStream, null);
            } catch (final IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Bonita web preferences file " + propertiesFile.getPath() + " could not be loaded.", e);
                }
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (final IOException e) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, "Bonita web preferences file stream " + propertiesFile.getPath() + " could not be closed.", e);
                        }
                    }
                }
            }
        }
    }

    public void setProperty(final String propertyName, final String propertyValue) {
        if (properties != null) {
            properties.setProperty(propertyName, propertyValue);
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(propertiesFile);
                properties.store(outputStream, null);
            } catch (final IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Bonita web preferences file " + propertiesFile.getPath() + " could not be loaded.", e);
                }
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (final IOException e) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, "Bonita web preferences file stream " + propertiesFile.getPath() + " could not be closed.", e);
                        }
                    }
                }
            }
        }
    }
}
