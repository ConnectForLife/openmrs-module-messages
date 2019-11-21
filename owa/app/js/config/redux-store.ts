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
import promise from 'redux-promise-middleware';
import thunkMiddleware from 'redux-thunk';
import { createHashHistory } from 'history';
import { routerMiddleware } from 'connected-react-router';
import rootSaga from '../sagas';
import loggerMiddleware from './logger-middleware';
import DevTools from './devtools';
import rootReducer from '../reducers';

const sagaMiddleware = createSagaMiddleware();

export const history = createHashHistory({
  basename: '/',
});

const defaultMiddlewares = [
  routerMiddleware(history),
  thunkMiddleware,
  promise,
  sagaMiddleware
];

const composedMiddlewares = () =>
  process.env.NODE_ENV !== 'production'
    ? compose(
        applyMiddleware(...defaultMiddlewares, loggerMiddleware),
        DevTools.instrument()
      )
    : compose(applyMiddleware(...defaultMiddlewares));

const initialize = () => {
  const store = createStore(rootReducer(history), composedMiddlewares());
  sagaMiddleware.run(rootSaga)
  return store;
}

export default initialize;
