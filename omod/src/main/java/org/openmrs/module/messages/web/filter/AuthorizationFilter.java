package org.openmrs.module.messages.web.filter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter intended for all /ws/messages calls that allows the user to authenticate via Basic
 * authentication. (It will not fail on invalid or missing credentials. We count on the API to throw
 * exceptions if an unauthenticated user tries to do something they are not allowed to do.) <br/>
 */
public class AuthorizationFilter implements Filter {

    private static final Log LOGGER = LogFactory.getLog(AuthorizationFilter.class);

    private static final String BASIC_KEYWORD = "Basic ";

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        LOGGER.debug("Initializing Messages Authorization filter");
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        LOGGER.debug("Destroying Messages Authorization filter");
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        // skip if the session has timed out, we're already authenticated, or it's not an HTTP request
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            if (!httpRequest.isRequestedSessionIdValid()) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session timed out");
            }
            String authorization = httpRequest.getHeader("Authorization");
            if (authorization != null && authorization.contains(BASIC_KEYWORD)) {
                performBasicAuth(authorization);
            }
        }

        chain.doFilter(request, response);
    }

    private void performBasicAuth(String authorization) {
        // this is "Basic ${base64encode(username + ":" + password)}"
        try {
            String credentials = authorization.replace(BASIC_KEYWORD, "");
            String decoded = new String(Base64.decodeBase64(credentials), Charset.forName("UTF-8"));
            String[] userAndPass = decoded.split(":");
            Context.authenticate(userAndPass[0], userAndPass[1]);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("authenticated " + userAndPass[0]);
            }
        } catch (ContextAuthenticationException ex) {
            Context.logout();
        }
    }
}
