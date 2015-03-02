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
package org.bonitasoft.console.common.server.login.filter;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.HttpServletResponseAccessor;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.LoginManagerFactory;
import org.bonitasoft.console.common.server.login.LoginManagerNotFoundException;
import org.bonitasoft.console.common.server.login.TenantIdAccessor;
import org.bonitasoft.console.common.server.login.impl.jaas.cas.TenantIsPausedRedirectionToMaintenancePageException;
import org.bonitasoft.console.common.server.login.localization.LoginUrl;
import org.bonitasoft.console.common.server.login.localization.LoginUrlException;
import org.bonitasoft.console.common.server.login.localization.RedirectUrl;
import org.bonitasoft.console.common.server.login.localization.RedirectUrlBuilder;
import org.bonitasoft.console.common.server.utils.SessionUtil;
/**
 * @author Vincent Elcrin
 */
public class AuthenticationFilter implements Filter {

    protected static final String MAINTENANCE_JSP = "/maintenance.jsp";
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());

    private static final String AUTHENTICATION_FILTER_EXCLUDED_PAGES_PATTERN = "^/(bonita/)?(portal/themeResource$)|(portal/scripts)|(portal/formsService)";

    /** the Pattern of url not to filter */
    protected Pattern excludePattern = null;

    private final LinkedList<AuthenticationRule> rules = new LinkedList<AuthenticationRule>();

    public AuthenticationFilter() {
        addRules();
    }

    protected void addRules() {
        addRule(new AlreadyLoggedInRule());
        addRule(new AutoLoginRule());
    }

    protected void addRule(final AuthenticationRule rule) {
        rules.add(rule);
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

        final String contextPath = filterConfig.getServletContext().getContextPath();
        String webappName;
        if (contextPath.length() > 0) {
            webappName = contextPath.substring(1);
        } else {
            webappName = "";
        }
        excludePattern = compilePattern(StringUtils.isBlank(filterConfig.getInitParameter("excludePattern")) ? getDefaultExcludedPages()
                .replace("bonita", webappName)
                : filterConfig.getInitParameter("excludePattern"));
    }

    protected String getDefaultExcludedPages() {
        return AUTHENTICATION_FILTER_EXCLUDED_PAGES_PATTERN;
    }

    protected Pattern compilePattern(final String stringPattern) {
        if (StringUtils.isNotBlank(stringPattern)) {
            try {
                return Pattern.compile(stringPattern);
            } catch (final Exception e) {
                LOGGER.log(Level.SEVERE, " impossible to create pattern from [" + stringPattern + "] : " + e);
            }
        }
        return null;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor((HttpServletRequest) request);
        final String url = ((HttpServletRequest) request).getRequestURL().toString();
        if (matchExcludePatterns(url)) {
            chain.doFilter(request, response);
        } else {
            doAuthenticationFiltering(requestAccessor,
                    new HttpServletResponseAccessor((HttpServletResponse) response),
                    createTenantAccessor(requestAccessor), chain);
        }
    }

    protected TenantIdAccessor createTenantAccessor(final HttpServletRequestAccessor requestAccessor) throws ServletException {
        return new TenantIdAccessor(requestAccessor);
    }

    protected void doAuthenticationFiltering(final HttpServletRequestAccessor requestAccessor,
            final HttpServletResponseAccessor responseAccessor,
            final TenantIdAccessor tenantIdAccessor,
            final FilterChain chain) throws ServletException, IOException {

        if (!isAuthorized(requestAccessor, responseAccessor, tenantIdAccessor, chain)) {

            cleanHttpSession(requestAccessor.getHttpSession());
            responseAccessor.redirect(createLoginUrl(requestAccessor, tenantIdAccessor));
        }
    }

    /**
     * @return true if one of the rules pass false otherwise
     */
    protected boolean isAuthorized(final HttpServletRequestAccessor requestAccessor,
            final HttpServletResponseAccessor responseAccessor,
            final TenantIdAccessor tenantIdAccessor,
            final FilterChain chain) throws ServletException, IOException {

        for (final AuthenticationRule rule : getRules()) {
            try {
                if (rule.doAuthorize(requestAccessor, tenantIdAccessor)) {
                    chain.doFilter(requestAccessor.asHttpServletRequest(), responseAccessor.asServletResponse());
                    return true;
                }
            } catch (final ServletException e) {
                if (e.getCause() instanceof TenantIsPausedRedirectionToMaintenancePageException) {
                    return handleTenantPausedException(requestAccessor, responseAccessor, e);
                } else {
                    throw e;
                }
            }
        }
        return false;
    }

    protected LinkedList<AuthenticationRule> getRules() {
        return rules;
    }

    @Override
    public void destroy() {
    }

    // protected for test stubbing
    protected LoginManager getLoginManager(final TenantIdAccessor tenantIdAccessor) throws ServletException {
        try {
            return LoginManagerFactory.getLoginManager(tenantIdAccessor.ensureTenantId());
        } catch (final LoginManagerNotFoundException e) {
            throw new ServletException(e);
        }
    }

    /**
     * check the given url against the local url exclude pattern
     *
     * @param url
     *        the url to check
     * @return true if the url match the pattern
     */
    protected boolean matchExcludePatterns(final String url) {
        try {
            final boolean isExcluded = excludePattern != null && excludePattern.matcher(new URL(url).getPath()).find();
            if (LOGGER.isLoggable(Level.FINE)) {
                if (isExcluded) {
                    LOGGER.log(Level.FINE, " Exclude pattern match with this url:" + url);
                } else {
                    LOGGER.log(Level.FINE, " Exclude pattern does not match with this url:" + url);
                }
            }
            return isExcluded;
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "impossible to get URL from given input [" + url + "]:" + e);
            }
            return excludePattern.matcher(url).find();

        }
    }

    protected RedirectUrl makeRedirectUrl(final HttpServletRequestAccessor httpRequest) {
        final RedirectUrlBuilder builder = new RedirectUrlBuilder(httpRequest.getRequestedUri());
        builder.appendParameters(httpRequest.getParameterMap());
        return builder.build();
    }

    protected LoginUrl createLoginUrl(final HttpServletRequestAccessor requestAccessor, final TenantIdAccessor tenantIdAccessor) throws ServletException {
        try {
            return new LoginUrl(getLoginManager(tenantIdAccessor),
                    tenantIdAccessor.getRequestedTenantId(),
                    makeRedirectUrl(requestAccessor).getUrl(), requestAccessor.asHttpServletRequest());
        } catch (final LoginUrlException e) {
            throw new ServletException(e);
        }
    }

    protected void cleanHttpSession(final HttpSession session) {
        SessionUtil.sessionLogout(session);
    }

    protected boolean handleTenantPausedException(final HttpServletRequestAccessor requestAccessor, final HttpServletResponseAccessor responseAccessor,
            final ServletException e) throws ServletException {
        final TenantIsPausedRedirectionToMaintenancePageException tenantIsPausedException = (TenantIsPausedRedirectionToMaintenancePageException) e.getCause();
        if (AuthenticationFilter.LOGGER.isLoggable(Level.FINE)) {
            AuthenticationFilter.LOGGER.log(Level.FINE, "redirection to maintenance page : " + e.getMessage(), e);
        }
        redirectToMaintenance(requestAccessor, responseAccessor, tenantIsPausedException.getTenantId());
        return false;
    }

    /**
     * manage redirection to maintenance page
     *
     * @param request
     * @param response
     */
    protected void redirectToMaintenance(final HttpServletRequestAccessor request, final HttpServletResponseAccessor response, final long tenantId)
            throws ServletException {
        try {
            ((HttpServletResponse) response.asServletResponse()).sendRedirect(request.asHttpServletRequest().getContextPath()
                    + AuthenticationFilter.MAINTENANCE_JSP);
        } catch (final IOException e) {
            if (AuthenticationFilter.LOGGER.isLoggable(Level.INFO)) {
                AuthenticationFilter.LOGGER.log(Level.INFO, e.getMessage());
            }
        }
    }
}
