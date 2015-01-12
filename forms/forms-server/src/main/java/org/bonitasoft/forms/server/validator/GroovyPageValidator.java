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
package org.bonitasoft.forms.server.validator;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormWorkflowAPI;

/**
 * @author Anthony Birembaut
 */
public class GroovyPageValidator extends AbstractFormValidator implements IFormPageValidator {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(GroovyPageValidator.class.getName());

    /**
     * {@inheritDoc}
     */
    public boolean validate(final Map<String, FormFieldValue> fieldValues, final Locale locale) {

        fieldValues.put(CLICKED_BUTTON_VARNAME, new FormFieldValue(getSubmitButtonId(), String.class.getName()));
        final IFormWorkflowAPI formWorkflowAPI = FormAPIFactory.getFormWorkflowAPI();
        boolean valid = false;
        final Expression expression = getParameter();
        if (expression == null || expression.getContent().length() <= 0) {
            LOGGER.log(Level.SEVERE, "The expression used in the groovy validator of the page is empty.");
        }
        final Map<String, Serializable> context = getTransientDataContext();
        context.put(CLICKED_BUTTON_VARNAME, getSubmitButtonId());
        try {
            final long activityInstanceID = getActivityInstanceID();
            if (activityInstanceID != -1) {
                valid = (Boolean) formWorkflowAPI.getActivityFieldValue(getSession(), activityInstanceID, expression, fieldValues, locale, true, context);
            } else {
                final long processDefinitionID = getProcessDefinitionID();
                if (processDefinitionID != -1) {
                    valid = (Boolean) formWorkflowAPI.getProcessFieldValue(getSession(), processDefinitionID, expression, fieldValues, locale, context);
                } else {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "The process definition UUID and activity definition UUID are undefined.");
                    }
                }
            }
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error while validating with a groovy expression", e);
            }
        }
        return valid;
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayName() {
        return "Groovy page validator";
    }

}
