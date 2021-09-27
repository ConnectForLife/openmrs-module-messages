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
