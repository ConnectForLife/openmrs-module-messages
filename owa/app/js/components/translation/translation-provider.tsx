/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import {addLocaleData, IntlProvider} from 'react-intl';
import React, {PropsWithChildren, useEffect} from 'react';
import {connect} from 'react-redux';
import {getSession} from '../../reducers/session';
import {getMessages} from '../../reducers/messages';
import LocalizationContext from '@openmrs/react-components/lib/components/localization/LocalizationContext';
import moment from 'moment';

interface IStore {
  session: any,
  messages: any
}

const TranslationProvider = ({locale, messages, getSession, getMessages, children}: PropsWithChildren<StateProps & DispatchProps>) => {

  useEffect(() => {
    getSession();
  }, []);

  useEffect(() => {
    if (locale) {
      getMessages(locale);
      addLocaleData(require(`react-intl/locale-data/${locale.split('_')[0]}`));
      moment.locale(locale);
    }
  }, [locale]);

  return (
    <>
      {messages && locale ? (
        <LocalizationContext.Provider value={{intlProviderAvailable: true}}>
          <IntlProvider locale={locale.replace('_', '-')} messages={messages}>
            {children}
          </IntlProvider>
        </LocalizationContext.Provider>
      ) : <></>}
    </>
  );
};

const mapStateToProps = (({session: {session}, messages: {messages}}: IStore) => ({
  locale: session ? session.locale : null,
  messages: messages
}));
type StateProps = ReturnType<typeof mapStateToProps>;

const mapDispatchToProps = {getSession, getMessages};
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TranslationProvider);