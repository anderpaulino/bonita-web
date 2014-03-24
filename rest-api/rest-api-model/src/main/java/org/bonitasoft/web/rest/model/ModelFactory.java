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
package org.bonitasoft.web.rest.model;

import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCommentDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDocumentDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CommentDefinition;
import org.bonitasoft.web.rest.model.bpm.connector.ArchivedConnectorInstanceDefinition;
import org.bonitasoft.web.rest.model.bpm.connector.ConnectorInstanceDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedActivityDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedUserTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HiddenUserTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.UserTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ActorDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ActorMemberDefinition;
import org.bonitasoft.web.rest.model.bpm.process.CategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.DelegationDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessActorPrivilegeDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessCategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorDependencyDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemDefinition;
import org.bonitasoft.web.rest.model.document.ArchivedDocumentDefinition;
import org.bonitasoft.web.rest.model.document.DocumentDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoAssociationDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionDefinition;
import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.MembershipDefinition;
import org.bonitasoft.web.rest.model.identity.PersonalContactDataDefinition;
import org.bonitasoft.web.rest.model.identity.ProfessionalContactDataDefinition;
import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.monitoring.report.ReportDefinition;
import org.bonitasoft.web.rest.model.platform.PlatformDefinition;
import org.bonitasoft.web.rest.model.portal.profile.BonitaPageDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberDefinition;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.common.session.SessionDefinition;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Yongtao Guo
 */
public class ModelFactory extends ItemDefinitionFactory {

    @Override
    public ItemDefinition<?> defineItemDefinitions(final String token) {

        // organization
        if (UserDefinition.TOKEN.equals(token)) {
            return new UserDefinition();
        } else if (PersonalContactDataDefinition.TOKEN.equals(token)) {
            return new PersonalContactDataDefinition();
        } else if (ProfessionalContactDataDefinition.TOKEN.equals(token)) {
            return new ProfessionalContactDataDefinition();
        } else if (RoleDefinition.TOKEN.equals(token)) {
            return new RoleDefinition();
        } else if (GroupDefinition.TOKEN.equals(token)) {
            return new GroupDefinition();
        } else if (MembershipDefinition.TOKEN.equals(token)) {
            return new MembershipDefinition();
        } else if (CustomUserInfoDefinition.TOKEN.equals(token)) {
            return new CustomUserInfoDefinition();
        } else if (CustomUserInfoDefinitionDefinition.TOKEN.equals(token)) {
            return new CustomUserInfoDefinitionDefinition();
        } else if (CustomUserInfoAssociationDefinition.TOKEN.equals(token)) {
            return new CustomUserInfoAssociationDefinition();
        }

        // bpm.process
        else if (ProcessDefinition.TOKEN.equals(token)) {
            return new ProcessDefinition();
        } else if (ProcessConnectorDefinition.TOKEN.equals(token)) {
            return new ProcessConnectorDefinition();
        } else if (ProcessConnectorDependencyDefinition.TOKEN.equals(token)) {
            return new ProcessConnectorDependencyDefinition();
        } else if (ProcessCategoryDefinition.TOKEN.equals(token)) {
            return new ProcessCategoryDefinition();
        } else if (ActorDefinition.TOKEN.equals(token)) {
            return new ActorDefinition();
        } else if (ActorMemberDefinition.TOKEN.equals(token)) {
            return new ActorMemberDefinition();
        } else if (CategoryDefinition.TOKEN.equals(token)) {
            return new CategoryDefinition();
        } else if (DelegationDefinition.TOKEN.equals(token)) {
            return new DelegationDefinition();
        } else if (ProcessActorPrivilegeDefinition.TOKEN.equals(token)) {
            return new ProcessActorPrivilegeDefinition();
        } else if (ProcessResolutionProblemDefinition.TOKEN.equals(token)) {
            return new ProcessResolutionProblemDefinition();
        }

        // bpm.cases
        else if (CaseDefinition.TOKEN.equals(token)) {
            return new CaseDefinition();
        } else if (CommentDefinition.TOKEN.equals(token)) {
            return new CommentDefinition();
        } else if (ArchivedCommentDefinition.TOKEN.equals(token)) {
            return new ArchivedCommentDefinition();
        } else if (ArchivedCaseDefinition.TOKEN.equals(token)) {
            return new ArchivedCaseDefinition();
        } else if (CaseVariableDefinition.TOKEN.equals(token)) {
            return new CaseVariableDefinition();
        } else if (CaseDocumentDefinition.TOKEN.equals(token)) {
            return new CaseDocumentDefinition();
        }

        // bpm.flownode
        else if (FlowNodeDefinition.TOKEN.equals(token)) {
            return new FlowNodeDefinition();
        } else if (ActivityDefinition.TOKEN.equals(token)) {
            return new ActivityDefinition();
        } else if (TaskDefinition.TOKEN.equals(token)) {
            return new TaskDefinition();
        } else if (HumanTaskDefinition.TOKEN.equals(token)) {
            return new HumanTaskDefinition();
        } else if (UserTaskDefinition.TOKEN.equals(token)) {
            return new UserTaskDefinition();
        } else if (HiddenUserTaskDefinition.TOKEN.equals(token)) {
            return new HiddenUserTaskDefinition();
        } else if (ConnectorInstanceDefinition.TOKEN.equals(token)) {
            return new ConnectorInstanceDefinition();
        }

        // bpm.flownode.archive
        else if (ArchivedFlowNodeDefinition.TOKEN.equals(token)) {
            return new ArchivedFlowNodeDefinition();
        } else if (ArchivedActivityDefinition.TOKEN.equals(token)) {
            return new ArchivedActivityDefinition();
        } else if (ArchivedTaskDefinition.TOKEN.equals(token)) {
            return new ArchivedTaskDefinition();
        } else if (ArchivedHumanTaskDefinition.TOKEN.equals(token)) {
            return new ArchivedHumanTaskDefinition();
        } else if (ArchivedUserTaskDefinition.TOKEN.equals(token)) {
            return new ArchivedUserTaskDefinition();
        } else if (ArchivedConnectorInstanceDefinition.TOKEN.equals(token)) {
            return new ArchivedConnectorInstanceDefinition();
        }

        // system
        else if (ProfileDefinition.TOKEN.equals(token)) {
            return new ProfileDefinition();
        } else if (BonitaPageDefinition.TOKEN.equals(token)) {
            return new BonitaPageDefinition();
        } else if (ProfileEntryDefinition.TOKEN.equals(token)) {
            return new ProfileEntryDefinition();
        } else if (ProfileMemberDefinition.TOKEN.equals(token)) {
            return new ProfileMemberDefinition();
        } else if (SessionDefinition.TOKEN.equals(token)) {
            return new SessionDefinition();
        }

        // monitoring
        else if (ReportDefinition.TOKEN.equals(token)) {
            return new ReportDefinition();
        }

        // platform
        else if (PlatformDefinition.TOKEN.equals(token)) {
            return new PlatformDefinition();
        }

        // documents
        else if (DocumentDefinition.TOKEN.equals(token)) {
            return new DocumentDefinition();
        } else if (ArchivedDocumentDefinition.TOKEN.equals(token)) {
            return new ArchivedDocumentDefinition();
        }

        // default
        else {
            return null;
        }
    }

}
