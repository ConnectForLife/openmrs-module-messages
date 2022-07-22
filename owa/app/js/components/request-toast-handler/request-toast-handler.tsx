/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { toast } from 'react-toastify';
import { ToastStatusContent, CloseButton } from '../toast-handler/toast-builder';
import { ToastType } from '../toast-handler/models';
import { GENERIC_PROCESSING, TOAST_CLASS, CLOSE_DELAY, GENERIC_FAILURE } from '../toast-handler/constants';
import '../toast-handler/toast.scss';

export const initRequestHandling = () => {
  return toast(
    <ToastStatusContent message={GENERIC_PROCESSING} type={ToastType.NOTICE} />,
    {
      autoClose: false,
      closeButton: false,
      className: TOAST_CLASS,
      hideProgressBar: true
    }
  );
}

export const continueRequestHandling = async (toastId: string | number, dispatch: (e: any) => void, body: any, successMessage: string, errorMessage: any) => {
  try {
    await dispatch(body);
    toast.update(toastId, {
      render: <ToastStatusContent message={successMessage} type={ToastType.SUCCESS} />,
      autoClose: CLOSE_DELAY,
      closeButton: <CloseButton />,
      className: TOAST_CLASS,
      hideProgressBar: true
    });
  } catch (e) {
    handleRequestFailure(e, toastId, errorMessage);
  }
}

// Propagates request send action and displays response notification 
export const handleRequest = async (dispatch: (e: any) => void, body: any, successMessage: string, errorMessage: any) => {
  var toastId = toast(
    <ToastStatusContent message={GENERIC_PROCESSING} type={ToastType.NOTICE} />,
    {
      autoClose: false,
      closeButton: false,
      className: TOAST_CLASS,
      hideProgressBar: true
    }
  );
  await continueRequestHandling(toastId, dispatch, body, successMessage, errorMessage);
}

export const handleRequestFailure = (e: any, toastId: string | number, errorMessage: string) => {
  try {
    const responseData = e.response.data;
    if (responseData.constraintViolations !== undefined) {
      toast.update(toastId, {
        render: <ToastStatusContent message={[errorMessage, Object["values"](responseData.constraintViolations).join('\n')].join('\n')} type={ToastType.ERROR} />,
        autoClose: CLOSE_DELAY,
        closeButton: <CloseButton />,
        className: TOAST_CLASS,
        hideProgressBar: true
      });
    } else {
      toast.update(toastId, {
        render: <ToastStatusContent message={[errorMessage, responseData.errorMessages.map(e => e.message)].join('\n')} type={ToastType.ERROR} />,
        autoClose: CLOSE_DELAY,
        closeButton: <CloseButton />,
        className: TOAST_CLASS,
        hideProgressBar: true
      });
    }
  } catch (e) {
    toast.update(toastId, {
      render: <ToastStatusContent message={GENERIC_FAILURE} type={ToastType.ERROR} />,
      autoClose: CLOSE_DELAY,
      closeButton: <CloseButton />,
      className: TOAST_CLASS,
      hideProgressBar: true
    });
  }
}
