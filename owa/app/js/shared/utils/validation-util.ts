
// TODO: CFLM-302 it will be moved to component UI repo in next PR

// Form validation method based on Yup schema validation
import * as Yup from "yup";

export const validateFormAsSuccess = (form: Object, schema: Yup.ObjectSchema): Promise<{[key: string]: string}> => {
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
