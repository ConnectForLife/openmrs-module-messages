/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.messages.model.MessageHttpRequest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class AuthorizationFilterTest {

    public static final String TEST_USERNAME = "TestUsername";

    public static final String TEST_PASSWORD = "TestPassword123";

    @Before
    public void setUp() throws Exception {
        mockStatic(Context.class);
        doNothing().when(Context.class, "authenticate",
                Mockito.any(String.class), Mockito.any(String.class));
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void initShouldInitWithoutExceptions() throws ServletException {
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        authorizationFilter.init(new MockFilterConfig());
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void destroyShouldDestroyWithoutExceptions() {
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        authorizationFilter.destroy();
    }

    @Test
    public void doFilterReturnForbiddenWhenSessionInvalid() throws IOException, ServletException {
        FilterChain filterChain = new MockFilterChain();
        ServletRequest request = new MessageHttpRequest()
                .setValidSessionId(false);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        authorizationFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus(), is(HttpServletResponse.SC_UNAUTHORIZED));
        assertThat(response.getErrorMessage(), is("Session timed out"));
    }

    @Test
    public void doFilterAuthorizeSuccessfully() throws IOException, ServletException {
        FilterChain filterChain = new MockFilterChain();
        ServletRequest request = new MessageHttpRequest()
                .setValidSessionId(true)
                .setAuthorization(TEST_USERNAME, TEST_PASSWORD);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        authorizationFilter.doFilter(request, response, filterChain);
        verifyStatic();
    }

    @Test
    public void doFilterUnauthorizedSuccessfully() throws Exception {
        doThrow(new ContextAuthenticationException()).when(Context.class, "authenticate",
                Mockito.any(String.class), Mockito.any(String.class));
        FilterChain filterChain = new MockFilterChain();
        ServletRequest request = new MessageHttpRequest()
                .setValidSessionId(true)
                .setAuthorization(TEST_USERNAME, TEST_PASSWORD);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        authorizationFilter.doFilter(request, response, filterChain);
        verifyStatic();
    }
}
