import { sagas as openmrs } from '@openmrs/react-components';
import { all, fork } from "redux-saga/effects";

export default function* () {
  yield all([
    fork(openmrs)
  ]);
}
