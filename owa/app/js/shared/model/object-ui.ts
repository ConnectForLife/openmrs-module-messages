import _ from 'lodash';
import uuid from 'uuid';

export class ObjectUI<T> {
  private _localId: string;
  private _modelKeys: Array<String>;

  constructor(model: T) {
    if (this.constructor === ObjectUI) {
      throw new TypeError("Can not construct abstract class.");
    }

    this.setAndAssignKeys(model);
    this._localId = uuid.v4();
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
    _.assign(this, model);
    this._modelKeys = _.keys(model);
  }
}

export const toModel = <T>(ui: ObjectUI<T>): T => ui.toModel();

// TODO: CFLM-302 object merging
// TODO: CFLM-302 validatation https://github.com/jquense/yup/issues/368