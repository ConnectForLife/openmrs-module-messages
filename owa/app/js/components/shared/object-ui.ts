/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import _ from 'lodash';
import uuid from 'uuid';

export class ObjectUI<T> {
  private _localId: string;
  private _modelKeys: string[];

  constructor(model: T) {
    if (this.constructor === ObjectUI) {
      throw new TypeError("Can not construct abstract class.");
    }

    this._localId = uuid.v4();
    this._modelKeys = [];
    this.setAndAssignKeys(model);
  }

  get localId(): string {
    return this._localId;
  }

  get isNew(): boolean {
    throw new TypeError("Invoked abstract method.");
  }

  toModel(): T {
    const model = _.cloneDeep(_.pick(this, this._modelKeys));
    _.forEach(this._modelKeys, (key: string) => {
      if (model[key] instanceof Array) {
        model[key] = _.map(model[key], (item: any) => {
          return (item instanceof ObjectUI) ? item.toModel() : item;
        });
      }
    });
    return model as T;
  };

  protected getLocalId() {
    return this._localId;
  }

  private setAndAssignKeys(model: T) {
    if (!model) {
      throw new Error('Model not provided');
    }
    const simpleModel = (model instanceof ObjectUI)
      ? model.toModel()
      : model;

    _.assign(this, simpleModel);
    this._modelKeys = _.keys(simpleModel);
  }
}

export const mergeWithObjectUIs = <T>(oldArray: Array<ObjectUI<T>>, newObject: ObjectUI<T>): Array<ObjectUI<T>> => {
  let modifiedAny = false;
  const newArray: Array<ObjectUI<T>> = _.map(
    oldArray,
    oldObject => {
      if (oldObject.localId === newObject.localId) {
        modifiedAny = true;
        return newObject;
      }
      return oldObject;
    }
  );

  if (!modifiedAny) {
    newArray.push(newObject);
  }
  return newArray;
}

export const toModel = <T>(ui: ObjectUI<T>): T => ui.toModel();
