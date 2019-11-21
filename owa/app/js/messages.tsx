/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { render } from 'react-dom';
import { HashRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { ConnectedRouter } from 'connected-react-router';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '@openmrs/style-referenceapplication/lib/referenceapplication.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import initStore from './config/redux-store';

import { history } from './config/redux-store';
import { loadIcons } from './config/icon-loader';
import routes from './routes';
import 'codemirror/lib/codemirror.css';
import 'codemirror/mode/velocity/velocity';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/mode/xml/xml';
import 'codemirror/mode/sql/sql';
import 'codemirror/addon/display/fullscreen';
import 'codemirror/addon/display/fullscreen.css';

loadIcons();
toast.configure();
const store = initStore();

render((
  <Provider store={store}>
    <ConnectedRouter history={history}>
      <HashRouter>
        {routes(store)}
      </HashRouter>
    </ConnectedRouter>
  </Provider>
), document.getElementById('app'));
