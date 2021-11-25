package org.openmrs.module.messages.bundle;

import org.apache.commons.lang.StringUtils;
import org.openmrs.ConceptAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.customdatatype.CustomDatatype;
import org.openmrs.customdatatype.datatype.BooleanDatatype;
import org.openmrs.customdatatype.datatype.FreeTextDatatype;
import org.openmrs.customdatatype.datatype.LongFreeTextDatatype;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

/**
 * The MetadataBundle which installs Concept Attribute Types related to the Interactive Text Response system.
 * The installed attribute types are automatically configured in the corresponding Global Parameters.
 */
public class ITRConceptAttributeTypesBundle extends VersionedMetadataBundle {
  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  protected void installEveryTime() {
    // nothing to do
  }

  @Override
  protected void installNewVersion() {
    setUuidInGlobalProperty(installConceptAttributeType("ITR Answer Regex",
        "The regex applied on the received text to determine if this message is a correct response.",
        FreeTextDatatype.class), ConfigConstants.ITR_ANSWER_REGEX_CONCEPT_ATTR_TYPE_UUID);

    installConceptAttributeType("ITR Independent Message", "Whether this message can be sent by the CfL system " +
        "independently (e.g.: by a doctor clicking on some UI) or not.", BooleanDatatype.class);

    setUuidInGlobalProperty(
        installConceptAttributeType("ITR Message Text", "The text of the message.", LongFreeTextDatatype.class),
        ConfigConstants.ITR_MESSAGE_TEXT_ATTR_TYPE_UUID);

    setUuidInGlobalProperty(installConceptAttributeType("ITR Provider Template Name",
        "The fully name of provider-side template to use instead of the message text.", FreeTextDatatype.class),
        ConfigConstants.ITR_PROVIDER_TEMPLATE_NAME_CONCEPT_ATTR_TYPE_UUID);

    setUuidInGlobalProperty(installConceptAttributeType("ITR Message Image URL", "The URL to an image to send as message.",
        FreeTextDatatype.class), ConfigConstants.ITR_IMAGE_URL_CONCEPT_ATTR_TYPE_UUID);
  }

  private ConceptAttributeType installConceptAttributeType(String name, String description,
                                                           Class<? extends CustomDatatype> datatype) {
    final ConceptAttributeType conceptAttributeType = new ConceptAttributeType();
    conceptAttributeType.setName(name);
    conceptAttributeType.setDescription(description);
    conceptAttributeType.setDatatypeClassname(datatype.getName());
    conceptAttributeType.setMinOccurs(0);
    conceptAttributeType.setMaxOccurs(1);
    return this.install(conceptAttributeType);
  }

  private void setUuidInGlobalProperty(ConceptAttributeType conceptAttributeType, String globalPropertyName) {
    final String currentValue = Context.getAdministrationService().getGlobalProperty(globalPropertyName);

    if (StringUtils.isBlank(currentValue)) {
      Context.getAdministrationService().setGlobalProperty(globalPropertyName, conceptAttributeType.getUuid());
    }
  }
}
