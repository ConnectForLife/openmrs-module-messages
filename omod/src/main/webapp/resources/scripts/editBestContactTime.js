/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
jq(document).ready(function () {
  emr.loadMessages([
    "messages.dashboard.bestContactTime.update.successful",
    "messages.dashboard.bestContactTime.update.unsuccessful",
    "messages.dashboard.bestContactTime.invalidTimeFormat"
  ]);

  jq('.edit-best-contact-time').timepicker({
    timeFormat: 'HH:mm',
    interval: 1,
    dynamic: true,
    dropdown: true,
    scrollbar: false
  });
});

function onSaveTimeButtonClick(personId) {
  const selectedInput = jq('.edit-best-contact-time');
  validateTimeInput(selectedInput);
  const selectedTime = selectedInput.val();

  jq.ajax({
      url: `/${OPENMRS_CONTEXT_PATH}/ws/messages/actor/contact-times`,
      method: 'POST',
      data: JSON.stringify([{
        personId: personId,
        time: selectedTime
      }]),
      headers: {
        'Content-type': 'application/json; charset=utf-8'
      },
      success: function () {
        emr.successMessage("messages.dashboard.bestContactTime.update.successful");
        jq('.save-time-button').hide();
        location.reload();
      },
      error: function () {
        emr.errorMessage("messages.dashboard.bestContactTime.update.unsuccessful");
      }
  });
}

function handleBestContactTimeOnChange(event, originalTime) {
  setTimeout(() => {
    const currentTime = jq('.edit-best-contact-time').val();
    if (currentTime !== originalTime) {
      showSaveBestContactTimeButton();
    } else {
      hideSaveBestContactTimeButton();
    }
  }, 100);
}

function validateTimeInput(inputElement) {
  const errorMessageId = 'error-message-' + inputElement.attr('id').replace('time-input-', '');
  const errorMessageElement = jq('#' + errorMessageId);

  if (!isValidTimeInput(inputElement)) {
    const errorMessage = emr.message("messages.dashboard.bestContactTime.invalidTimeFormat");
    errorMessageElement.text(errorMessage);
    errorMessageElement.show();
    return;
  } else {
    errorMessageElement.hide();
  }
}

function isValidTimeInput(inputElement) {
  const bestContactTimeRegex = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/;

  return bestContactTimeRegex.test(inputElement.val());
}

function showSaveBestContactTimeButton() {
  jq('.save-time-button').show();
}

function hideSaveBestContactTimeButton() {
  jq('.save-time-button').hide();
}