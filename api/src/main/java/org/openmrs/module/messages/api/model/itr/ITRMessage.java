package org.openmrs.module.messages.api.model.itr;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * The ITRMessage interface.
 * <p>The representation of a single ITR message.
 */
public interface ITRMessage {
  String TEXT_TYPE = "text";
  String TEMPLATE_TYPE = "template";
  String IMAGE_TYPE = "image";

  /**
   * Gets the type of message.
   * <p>Supported types are:
   * <ul>
   *   <li>text - the text-only message</li>
   *   <li>template - the provider-side template message</li>
   *   <li>image - the image with caption</li>
   * </ul>
   *
   * @return the type of message, never null
   */
  String getType();

  /**
   * @return the name of message, never nul
   */
  String getName();

  /**
   * @return the message UUID, never null
   */
  String getUuid();

  /**
   * @param locale the requested locale, not null
   * @return the message text in {@code locale} or in a default locale, never null
   */
  String getMessageText(Locale locale);

  /**
   * @return optional with a name of provider-side template to use instead of message text, never null
   */
  Optional<String> getProviderTemplateName();

  /**
   * @return optional with an URL for image to sent, never null
   */
  Optional<String> getImageURL();

  /**
   * @return the List of message parameters, never null
   */
  List<ITRMessageGenericParameter> getParameters();

  /**
   * @param contextParameters the Map of context parameters to be used when resolving parameter values, never null
   * @return the List of resolved values of message parameters, never null
   */
  List<String> resolveParameterValues(Map<String, Object> contextParameters);
}
