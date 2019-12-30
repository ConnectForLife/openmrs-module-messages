package org.openmrs.module.messages.api.util;

import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.MessageDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.dto.UserDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.builder.TemplateBuilder;

public class MessageDetailsUtilTest {

    private static final String EXPECTED_DEACTIVATED_STATE = "DEACTIVATED";

    @Test
    public void shouldAddDefaultTemplate() {

        MessageDetailsDTO messageDetailsDTO = new MessageDetailsDTO();
        List<Template> templates = Collections.singletonList(new TemplateBuilder().build());

        messageDetailsDTO =
            MessageDetailsUtil.attachDefaultTemplates(messageDetailsDTO, templates);

        Assert.assertEquals(1, messageDetailsDTO.getMessages().size());
        Assert.assertEquals(EXPECTED_DEACTIVATED_STATE,
            messageDetailsDTO.getMessages().get(0).getActorSchedules().get(0).getSchedule());
    }

    @Test
    public void shouldNotAddDefaultTemplate() {

        final String name = "template name 1";
        final UserDTO user = new UserDTO("test user");

        MessageDetailsDTO messageDetailsDTO =
            new MessageDetailsDTO(Collections.singletonList(new MessageDTO(name, null, user,
                Collections.emptyList())));
        List<Template> templates = Collections.singletonList(new TemplateBuilder()
            .withName(name)
            .build());

        messageDetailsDTO =
            MessageDetailsUtil.attachDefaultTemplates(messageDetailsDTO, templates);

        Assert.assertEquals(1, messageDetailsDTO.getMessages().size());
        Assert.assertEquals(user, messageDetailsDTO.getMessages().get(0).getAuthor());
    }
}
