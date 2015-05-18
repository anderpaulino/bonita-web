package org.bonitasoft.livingapps;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.page.CustomPageRequestModifier;
import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.console.common.server.page.ResourceRenderer;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.business.application.ApplicationPageNotFoundException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.page.PageNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.livingapps.exception.CreationException;

public class LivingApplicationServlet extends HttpServlet {

    private static final long serialVersionUID = -3911437607969651000L;

    protected CustomPageRequestModifier customPageRequestModifier = new CustomPageRequestModifier();


    @Override
    protected void doGet(final HttpServletRequest hsRequest, final HttpServletResponse hsResponse)
            throws ServletException, IOException {

        final APISession session = getSession(hsRequest);

        // Check if requested URL is missing final slash (necessary in order to be able to use relative URLs for resources)
        if (isPageUrlWithoutFinalSlash(hsRequest)) {
            customPageRequestModifier.redirectToValidPageUrl(hsRequest, hsResponse);
            return;
        }

        try {
            if(!createApplicationRouter(session).route(hsRequest, hsResponse, session, getPageRenderer(), getResourceRenderer(), new BonitaHomeFolderAccessor())) {
                hsResponse.sendError(404);
            }
        } catch (final ApplicationPageNotFoundException e) {
            hsResponse.sendError(404);
        } catch (final PageNotFoundException e) {
            hsResponse.sendError(404);
        } catch (final BonitaHomeNotSetException e) {
            hsResponse.sendError(500);
        } catch (final ServerAPIException e) {
            hsResponse.sendError(500);
        } catch (final UnknownAPITypeException e) {
            hsResponse.sendError(500);
        } catch (final CreationException e) {
            hsResponse.sendError(404);
        } catch (BonitaException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    ApplicationRouter createApplicationRouter(final APISession session) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return new ApplicationRouter(new ApplicationModelFactory(
                TenantAPIAccessor.getLivingApplicationAPI(session),
                TenantAPIAccessor.getCustomPageAPI(session),
                TenantAPIAccessor.getProfileAPI(session)));
    }

    private boolean isPageUrlWithoutFinalSlash(final HttpServletRequest request) {
        return request.getPathInfo().matches("/[^/]+/[^/]+")
                ||request.getPathInfo().matches("/[^/]+");
    }

    APISession getSession(final HttpServletRequest hsRequest) {
        return (APISession) hsRequest.getSession().getAttribute("apiSession");
    }

    PageRenderer getPageRenderer(){
        return new PageRenderer(getResourceRenderer());
    }

    ResourceRenderer getResourceRenderer(){
        return new ResourceRenderer();
    }
}
