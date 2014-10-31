package org.bonitasoft.console.common.server.login.impl.standard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.login.datastore.UserLogger;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StandardLoginManagerImplTest {

    @Spy
    StandardLoginManagerImpl standardLoginManagerImpl = new StandardLoginManagerImpl();

    @Mock
    UserLogger userLogger;

    @Mock
    APISession apiSession;

    @Mock
    HttpServletRequest request;

    @Before
    public void setUp() {
        doReturn(userLogger).when(standardLoginManagerImpl).getUserLogger();
        doReturn(true).when(standardLoginManagerImpl).useCredentialsTransmission(apiSession);
        when(request.getContextPath()).thenReturn("bonita");
    }

    @Test
    public void testGetSimpleLoginpageURL() {
        final long tenantId = 1;
        final String redirectUrl = "%2Fportal%2Fhomepage";
        final String loginURL = standardLoginManagerImpl.getLoginpageURL(request, tenantId, redirectUrl);
        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?tenant=1&redirectUrl=%2Fportal%2Fhomepage");
    }

    @Test
    public void testGetLoginpageURLFromPortal() {
        final long tenantId = 1;
        final String redirectUrl = "%2Fportal%2Fhomepage";
        when(request.getServletPath()).thenReturn("/portal/");
        final String loginURL = standardLoginManagerImpl.getLoginpageURL(request, tenantId, redirectUrl);
        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?tenant=1&redirectUrl=%2Fportal%2Fhomepage");
    }

    @Test
    public void testGetLoginpageURLFromMobile() {
        final long tenantId = 1;
        final String redirectUrl = "%2Fmobile%2F";
        when(request.getServletPath()).thenReturn("/mobile/#login");
        final String loginURL = standardLoginManagerImpl.getLoginpageURL(request, tenantId, redirectUrl);
        assertThat(loginURL).isEqualToIgnoringCase("bonita/mobile/login.jsp?tenant=1&redirectUrl=%2Fmobile%2F");
    }

}
