/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages;

import com.google.gson.GsonBuilder;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import java.io.IOException;

/**
 * All unit tests must extend this
 * <p/>
 * The statement @PowerMockIgnore("org.jacoco.agent.rt.*") is a workaround to
 * https://code.google.com/p/powermock/issues/detail?id=458 to allow Jacoco agent to run correclty with Power Mock.
 *
 * @author bramak09
 */
@PowerMockIgnore("org.jacoco.agent.rt.*")
public abstract class BaseTest {

    @Rule
    // Warning from check style need to be ignored as the Junit require ExpectedException to be public
    // CHECKSTYLE IGNORE check FOR NEXT 1 LINES
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * Use this method at the beginning of your test case in your //Given part
     * to indicate that you expect the tested method to throw an Exception.
     * <p/>
     * <b>Important :</b> The @Test annotation should NOT contain the expected property for the exception
     *
     * @param clazz the class of the expected exception.
     */
    protected <E extends Exception> void expectException(Class<E> clazz) {
        expectedException.expect(clazz);
    }

    /**
     * Converts the provided object into a JSON string
     * This is used in controller unit tests, but is generic to be used elsewhere as well
     *
     * @param obj to convert
     * @return a json string representation of the object
     * @throws IOException if not able to convert
     */
    protected String json(Object obj) throws IOException {
        return new GsonBuilder().disableHtmlEscaping().create().toJson(obj);
    }

    /**
     * Converts the provided object into a UTF-8 encoded JSON byte array
     * This is used in controller unit tests, but is generic to be used elsewhere as well
     *
     * @param obj to convert
     * @return a json string representation of the object
     * @throws IOException if not able to convert
     */
    protected byte[] jsonBytes(Object obj) throws IOException {
        return json(obj).getBytes("UTF-8");
    }
}
