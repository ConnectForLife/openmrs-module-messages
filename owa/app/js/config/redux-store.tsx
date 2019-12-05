/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import {
  createStore, applyMiddleware, compose,
} from 'redux';
import createSagaMiddleware from 'redux-saga';
import promiseMiddleware from 'redux-promise-middleware';
import thunkMiddleware from 'redux-thunk';
// @ts-ignore
import { createLogger } from 'redux-logger';
import { createHashHistory, History } from 'history';
import { connectRouter, routerMiddleware } from 'connected-react-router';

import reducers from '../reducers';
import initSagas from '../sagas';

const sagaMiddleware = createSagaMiddleware();

export const history: History = createHashHistory({
  basename: '/',
});

const middlewares = [
  routerMiddleware(history),
  thunkMiddleware,
  promiseMiddleware(),
  sagaMiddleware
];

// tslint:disable-next-line
// @ts-ignore
if (process.env.NODE_ENV !== 'production') {
  middlewares.push(createLogger({ collapsed: true }));
}

export default function () {
  const store = createStore(
    connectRouter(history)(reducers),
    compose(
      applyMiddleware(...middlewares),
      // tslint:disable-next-line
      // @ts-ignore
      window.devToolsExtension && process.env.NODE_ENV !== 'production' ? window.devToolsExtension() : f => f,
    ),
  );
  initSagas(sagaMiddleware);
  return store;
}
