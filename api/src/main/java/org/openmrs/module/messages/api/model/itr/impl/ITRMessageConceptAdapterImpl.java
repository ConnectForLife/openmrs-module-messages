package org.openmrs.module.messages.api.model.itr.impl;

import org.openmrs.Concept;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.model.itr.ITRMessage;
import org.openmrs.module.messages.api.model.itr.ITRMessageGenericParameter;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * The immutable Concept adapter for ITRMessage.
 */
public final class ITRMessageConceptAdapterImpl extends ITRMessageBase implements ITRMessage {
  private final String name;
  private final String uuid;
  private final String messageText;
  private final String providerTemplateName;
  private final List<ITRMessageGenericParameter> parameters;

  public ITRMessageConceptAdapterImpl(Concept messageConcept) {
    this.name = messageConcept.getName().getName();
    this.uuid = messageConcept.getUuid();
    this.messageText = getStringAttribute(ConfigConstants.ITR_MESSAGE_TEXT_ATTR_TYPE_UUID, messageConcept);
    this.providerTemplateName =
        getStringAttribute(ConfigConstants.ITR_PROVIDER_TEMPLATE_NAME_CONCEPT_ATTR_TYPE_UUID, messageConcept);
    this.parameters = getParameters(messageConcept);
  }

  private List<ITRMessageGenericParameter> getParameters(Concept messageConcept) {
    return messageConcept.getSetMembers().stream().map(ITRMessageGenericParameterConceptAdapterImpl::new).collect(toList());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getUuid() {
    return uuid;
  }

  @Override
  public String getMessageText(Locale locale) {
    return messageText != null ? removeNewLines(messageText) : "";
  }

  @Override
  public Optional<String> getProviderTemplateName() {
    return Optional.ofNullable(providerTemplateName);
  }

  @Override
  public List<ITRMessageGenericParameter> getParameters() {
    return parameters;
  }

  @Override
  public List<String> resolveParameterValues(Map<String, Object> contextParameters) {
    return getParameters().stream().map(parameter -> parameter.getValue(contextParameters)).collect(toList());
  }

  // Remove new line characters, otherwise they will break the SMS Request template creation
  private static String removeNewLines(String text) {
    return text.replaceAll("\r?\n", "");
  }
}
