/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.common.server;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * @author Fabio Lombardi
 */

// DO NOT DELETE: Even if it could seem useless, we use this class in order to add profile description values inside Crowdin
public class ProfileTypesI18N {

    // /////////////////////////////////////////
    // // USER
    // /////////////////////////////////////////
    protected static final String USER = _("User");

    protected static final String USER_DESCRIPTION = _("The user can view and perform tasks and can start a new case of a process.");

    // /////////////////////////////////////////
    // // ADMINISTRATOR
    // /////////////////////////////////////////
    protected static final String ADMINISTRATOR = _("Administrator");

    protected static final String ADMINISTRATOR_DESCRIPTION = _("The administrator can install a process, manage the organization, and handle some errors (for example, by replaying a task).");

    // /////////////////////////////////////////
    // // PROCESS MANAGER
    // /////////////////////////////////////////
    protected static final String PROCESS_MANAGER = _("Process manager");

    protected static final String PROCESS_MANAGER_DESCRIPTION = _("The Process manager can supervise designated processes, and manage cases and tasks of those processes.");
}
