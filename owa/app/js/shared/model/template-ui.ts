/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import _ from 'lodash'

import { ObjectUI } from './object-ui';
import { ITemplate, getDefaultValue } from './template.model';
import { TemplateFieldUI } from './template-field-ui';
import * as Yup from 'yup';
import * as Default from '../../shared/utils/messages';
import { getIntl } from '@openmrs/react-components/lib/components/localization/withLocalization';
import { IActorType } from './actor-type.model';
import { findDefaultValue } from '../utils/end-date-util';
import { TemplateFieldType } from './template-field-type';

export class TemplateUI extends ObjectUI<ITemplate> implements ITemplate {
  id: number | null;
  name: string | null;
  templateFields: Array<TemplateFieldUI>;

  constructor(model: ITemplate) {
    super(model);

    this.templateFields = _.map(model.templateFields,
      templateField => new TemplateFieldUI(templateField));
  }

  get localId(): string {
    return this.id ? this.id.toString() : super.getLocalId();
  }

  get isNew(): boolean {
    return !this.id;
  }

  getValidationSchema(validateNotTouched: boolean, locale: string | undefined): Yup.ObjectSchema {
    const validators = {};
    this.templateFields.forEach((templateField: TemplateFieldUI) => {
      if (!templateField.id) {
        throw new Error('Id must be from DB to use the TemplateField entry in the validation process.');
      }
      validators[templateField.id] = Yup.mixed()
        .test('mandatory check',
          getIntl(locale).formatMessage({ id: 'MESSAGES_FIELD_REQUIRED', defaultMessage: Default.FIELD_REQUIRED }),
          templateFieldValue => {
            if (templateField.mandatory && (templateFieldValue.isTouched || validateNotTouched)) {
              return !!(templateFieldValue.value && templateFieldValue.value.trim());
            }
            return true;
          })
    });

    return Yup.object().shape(validators);
  }

  static getNew(): TemplateUI {
    return new TemplateUI(getDefaultValue());
  }

  static fromModel(model: ITemplate) {
    return new TemplateUI(model);
  }

  static fromModelWithActorTypesExcludingStartOfMessagesField(model: ITemplate, actorTypes: ReadonlyArray<IActorType>) {
    if (!!actorTypes) {
      model.templateFields = _.filter(model.templateFields, (f) => f.type != TemplateFieldType.START_OF_MESSAGES);
      actorTypes.forEach(actorType => {
        model.templateFields.forEach(tf => {
          const defaultValue = findDefaultValue(tf.defaultValues, actorType.relationshipTypeId,
            actorType.relationshipTypeDirection, tf.id!);
          if (!defaultValue) {
            tf.defaultValues.push({
              defaultValue: tf.defaultValue,
              templateFieldId: tf.id!,
              relationshipTypeId: actorType.relationshipTypeId,
              direction: actorType.relationshipTypeDirection
            });
          }
        })
      })
    }
    return new TemplateUI(model);
  }
}
