package org.bonitasoft.console.common.server.login.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class URLProtectorTest {

    URLProtector urlProtecter = new URLProtector();

    @Test
    public void testProtectRedirectUrlShouldRemoveHTTPFromURL() {
        assertEquals("google", urlProtecter.protectRedirectUrl("httpgoogle"));
    }

    @Test
    public void testProtectRedirectUrlShouldRemoveHTTPSFromURL() {
        assertEquals("google", urlProtecter.protectRedirectUrl("httpsgoogle"));
    }

    @Test
    public void testProtectRedirectUrlShouldNotChangeURL() {
        assertEquals("mobile/#home", urlProtecter.protectRedirectUrl("mobile/#home"));
    }

    @Test
    public void testProtectRedirectUrlRedirectingtoCaseListingUser() {
        assertEquals("#?_p=caselistinguser", urlProtecter.protectRedirectUrl("#?_p=caselistinguser"));
    }

    @Test
    public void testProtectRedirectUrlIsNotChangedIfStartingWithBonitaPortal() {
        assertEquals("portal/homepage#?_p=caselistinguser&test=http://www.google.fr",
                urlProtecter.protectRedirectUrl("portal/homepage#?_p=caselistinguser&test=http://www.google.fr"));
    }

}
