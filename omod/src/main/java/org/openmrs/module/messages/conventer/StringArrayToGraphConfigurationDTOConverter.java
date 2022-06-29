/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.conventer;

import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.module.messages.model.GraphConfigurationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StringArrayToGraphConfigurationDTOConverter implements Converter<String[], GraphConfigurationDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringArrayToGraphConfigurationDTOConverter.class);

    @Override
    public GraphConfigurationDTO convert(String[] source) {
        return source != null && source.length > 0 ? convertObject(source[0]) : null;
    }

    private GraphConfigurationDTO convertObject(String source) {
        ObjectMapper mapper = new ObjectMapper();
        GraphConfigurationDTO result = null;
        try {
            result = mapper.readValue(source, GraphConfigurationDTO.class);
        } catch (IOException ex) {
            LOGGER.error("Cannot convert json to GraphConfigurationDTO. Null was returned");
        }
        return result;
    }
}
