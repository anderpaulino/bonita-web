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
package org.bonitasoft.console.client.admin.bpm.cases.filler;

import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.ApiSearchResultPager;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

/**
 * @author Bastien Rohart
 * 
 */
public class OpenTasksFiller extends Filler<Link> {

    private final CaseItem caseItem;

    public OpenTasksFiller(final CaseItem caseItem) {
        this.caseItem = caseItem;
    }

    @Override
    protected void getData(final APICallback callback) {
        new APICaller(HumanTaskDefinition.get()).search(0, 0, null, null,
                MapUtil.asMap(new Arg(HumanTaskItem.ATTRIBUTE_CASE_ID, this.caseItem.getId()),
                        new Arg(HumanTaskItem.ATTRIBUTE_STATE, HumanTaskItem.VALUE_STATE_READY)), callback);
    }

    @Override
    protected void setData(final String json, final Map<String, String> headers) {
        final ApiSearchResultPager resultPager = ApiSearchResultPager.parse(headers);
        this.target.setLabel(String.valueOf(resultPager.getNbTotalResults()));
    }
}
