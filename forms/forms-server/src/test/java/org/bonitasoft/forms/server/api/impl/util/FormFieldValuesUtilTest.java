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
package org.bonitasoft.forms.server.api.impl.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormWidget;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 */
public class FormFieldValuesUtilTest {

    FormFieldValuesUtil util = new FormFieldValuesUtil();

    @Test
    public void testWeRetrieveExpressionOfDisplayedWidgetOnly() {
        final List<FormWidget> widgets = Arrays.asList(
                aWidgetWithLabelExpression("widget1"),
                aWidgetWithLabelExpression("widget2"));

        final Map<String, Serializable> resolvedDisplayExp = new HashMap<String, Serializable>();
        resolvedDisplayExp.put(new WidgetExpressionEntry("widget1", ExpressionId.WIDGET_DISPLAY_CONDITION)
                .toString(), true);
        resolvedDisplayExp.put(new WidgetExpressionEntry("widget2", ExpressionId.WIDGET_DISPLAY_CONDITION)
                .toString(), false);

        final List<Expression> expressions = util.getExpressionsToEvaluation(
                widgets,
                resolvedDisplayExp,
                new HashMap<String, Object>());

        assertEquals(1, expressions.size());
        assertEquals("widget1:label", expressions.get(0).getName());
    }

    @Test
    public void testWeRetrieveExpressionOfWidgetWithoutDisplayExpressions() {
        final List<FormWidget> widgets = Arrays.asList(aWidgetWithLabelExpression("widget"));

        final List<Expression> expressions = util.getExpressionsToEvaluation(
                widgets,
                new HashMap<String, Serializable>(),
                new HashMap<String, Object>());

        assertEquals(1, expressions.size());
        assertEquals("widget:label", expressions.get(0).getName());
    }

    @Test
    public void testWeDoNotRetrieveDisplayExpressionOfWidgetNotDisplayed() {
        final FormWidget widget = new FormWidget();
        widget.setId("widget");
        final Expression expression = new Expression();
        expression.setName("expression");
        widget.setDisplayConditionExpression(expression);

        final Map<String, Serializable> resolvedDisplayExp = new HashMap<String, Serializable>();
        resolvedDisplayExp.put(new WidgetExpressionEntry("widget", ExpressionId.WIDGET_DISPLAY_CONDITION)
                .toString(), false);

        final List<Expression> expressions = util.getExpressionsToEvaluation(
                Arrays.asList(widget),
                resolvedDisplayExp,
                new HashMap<String, Object>());

        assertTrue(expressions.isEmpty());
    }

    FormWidget aWidgetWithLabelExpression(final String id) {
        final FormWidget fw = new FormWidget();
        fw.setId(id);
        fw.setLabelExpression(new Expression());
        return fw;
    }
}
