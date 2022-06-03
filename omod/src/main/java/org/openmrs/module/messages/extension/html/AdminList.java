/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.extension.html;

import java.util.HashMap;
import java.util.Map;
import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;

/**
 * This class defines the links that will appear on the administration page under the "sms.title"
 * heading. This extension is enabled by defining (uncommenting) it in the config.xml file.
 */
public class AdminList extends AdministrationSectionExt {

  /**
   * @see org.openmrs.module.web.extension.AdministrationSectionExt#getMediaType()
   */
  @Override
  public Extension.MEDIA_TYPE getMediaType() {
    return Extension.MEDIA_TYPE.html;
  }

  public String getTitle() {
    return "messages.swagger.title";
  }

  public Map<String, String> getLinks() {

    Map<String, String> map = new HashMap<>();
    //map.put("module/biometrics/biometric.form", "biometric.title");
    map.put("/ms/uiframework/resource/messages/swagger/index.html", "messages.swagger.desc");
    return map;
  }

}

