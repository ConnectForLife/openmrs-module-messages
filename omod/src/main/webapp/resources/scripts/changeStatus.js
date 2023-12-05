const emrVariable = window.emr || {};
const jqVariable = window.jq || {};

let changeStatus = window.changeStatus || {};

changeStatus.updatePersonStatusDialog = null;

changeStatus.handelChangedStatus = function(selectObject) {
  let value = selectObject.value;
  if (value === 'DEACTIVATED') {
    jqVariable('.person-status-reason').show();
  } else {
    jqVariable('.person-status-reason').hide();
  }
};

changeStatus.createPersonStatusUpdateDialog = function() {
  changeStatus.updatePersonStatusDialog = emrVariable.setupConfirmationDialog({
    selector: '#person-status-update-dialog',
    actions: {
      confirm: changeStatus.submit,
      cancel: function() {
        jqVariable('#person-status-update-dialog' + ' .icon-spin').css('display', 'none').parent().removeClass('disabled');
        changeStatus.updatePersonStatusDialog.close();
      }
    }
  });
};

changeStatus.submit = function() {
  emrVariable.loadMessages([
    "person.status.update.successful",
    "person.status.update.unsuccessful"
  ]);
  let statusValue = document.getElementById("person-status-select").value;
  let changeReason = document.getElementById("person-status-reason-select").value.trim();

  if (!statusValue) {
    jqVariable('#person-status-select-empty').css({'color' : 'red', display : 'inline'}).show();
  } else if (statusValue === 'DEACTIVATED' && (!changeReason || changeReason.length === 0)) {
    jqVariable('#person-status-reason-select-empty').css({'color' : 'red', display : 'inline'}).show();
  } else {
    jqVariable('#person-status-update-dialog' + ' .icon-spin').css('display', 'inline-block').parent().addClass('disabled');
    let url = '/openmrs/messages/patientdashboard/changeStatus/update.action';
    jqVariable.ajax({
      url: url,
      type: 'POST',
      data: { personIdOrUuid: changeStatus.personIdOrUuid, personStatusValue: statusValue, personStatusReason: changeReason},
      success: function() {
        emrVariable.successMessage('person.status.update.successful');
        jqVariable('#person-status-update-dialog' + ' .icon-spin').css('display', 'none').parent().removeClass('disabled');
        changeStatus.updatePersonStatusDialog.close();
        location.reload();
      },
      error: function() {
        emrVariable.errorMessage("person.status.update.unsuccessful");
        jqVariable('#person-status-update-dialog' + ' .icon-spin').css('display', 'none').parent().removeClass('disabled');
        changeStatus.updatePersonStatusDialog.close();
      }
    });
  }
}

changeStatus.showPersonStatusUpdateDialog = function(personIdOrUuid) {
  changeStatus.personIdOrUuid = personIdOrUuid;
  if (changeStatus.updatePersonStatusDialog == null) {
    changeStatus.createPersonStatusUpdateDialog();
  }
  jqVariable('#person-status-select-empty').hide();
  jqVariable('#person-status-reason-select-empty').hide();
  changeStatus.handelChangedStatus(document.getElementById("person-status-select"));
  changeStatus.updatePersonStatusDialog.show();
};
