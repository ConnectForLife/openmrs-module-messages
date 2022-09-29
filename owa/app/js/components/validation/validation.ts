/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

// Form validation method based on Yup schema validation
import * as Yup from "yup";

export const validateFormSafely = (form: Object, schema: Yup.ObjectSchema): Promise<{[key: string]: string}> => {
  return validateForm(form, schema)
    .then(() => ({}))
    .catch((errors) => errors);
}

export const validateForm = (form, schema) => new Promise<Map<String, String>>((resolve, reject) => {
  schema.validate(form, {
      abortEarly: false
    })
    .then(() => {
      resolve(form);
    })
    .catch(function (ex) {
      const errors = {};
      ex.inner.map((e) => {
        if (!!e.path) {
          errors[e.path] = e.errors[0];
        }
      });
      reject(errors);
    });
});

export const validateField  = (form, fieldPath, schema) => new Promise((resolve, reject) => {
  schema.validateAt(fieldPath, form)
    .then(() => {
      resolve(fieldPath);
    })
    .catch(function (ex) {
      let errors = {};
      errors[ex.path] = ex.errors[0];
      reject(errors);
    });
});
