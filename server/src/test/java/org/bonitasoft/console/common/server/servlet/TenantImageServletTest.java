package org.bonitasoft.console.common.server.servlet;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.engine.session.APISession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class TenantImageServletTest {

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse res;

    @Mock
    HttpSession httpSession;

    @Mock
    APISession session;

    @Test
    public void should_verify_authorisation_for_the_given_location_param() throws
    Exception {

        final TenantImageServlet tenantImageServlet = spy(new TenantImageServlet());
        System.setProperty("bonita.home", "target/bonita-home/bonita");
        when(req.getParameter(AttachmentImageServlet.SRC_PARAM)).thenReturn("../../../file.txt");

        tenantImageServlet.setDirectoryPath("./temp");

        when(req.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(LoginManager.API_SESSION_PARAM_KEY)).thenReturn(session);
        when(session.getTenantId()).thenReturn(1L);

        try {
            tenantImageServlet.doGet(req, res);
        } catch (final ServletException e) {
            assertTrue(e.getMessage().startsWith("For security reasons, access to this file paths"));
        }
    }
}