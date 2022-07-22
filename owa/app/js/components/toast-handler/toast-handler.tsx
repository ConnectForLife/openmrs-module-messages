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
import { ToastStatusContent, CloseButton } from './toast-builder';
import { toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import './toast.scss';
import { ToastType } from './models';
import { CLOSE_DELAY, TOAST_CLASS } from './constants';

export const successToast = (message: string) => displayToast(message, ToastType.SUCCESS);

export const errorToast = (message: string) => displayToast(message, ToastType.ERROR);

export const displayToast = (message: string, type: ToastType) =>
  toast(
    <ToastStatusContent {... { message, type }} />,
    {
      autoClose: CLOSE_DELAY,
      closeButton: <CloseButton />,
      className: TOAST_CLASS,
      hideProgressBar: true
    });
