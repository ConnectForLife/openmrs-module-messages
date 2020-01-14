var changeStatus = changeStatus || {};

changeStatus.updatePersonStatusDialog = null;

changeStatus.handelChangedStatus = function(selectObject) {
    var value = selectObject.value;
    if (value === 'DEACTIVATE') {
        jq('.person-status-reason').show();
    } else {
        jq('.person-status-reason').hide();
    }
}

changeStatus.createPersonStatusUpdateDialog = function() {
    changeStatus.updatePersonStatusDialog = emr.setupConfirmationDialog({
        selector: '#person-status-update-dialog',
        actions: {
            confirm: changeStatus.submit,
            cancel: function() {
                jq('#person-status-update-dialog' + ' .icon-spin').css('display', 'none').parent().removeClass('disabled');
                changeStatus.updatePersonStatusDialog.close();
            }
        }
    });
};

changeStatus.submit = function() {
    emr.loadMessages([
        "person.status.update.successful",
        "person.status.update.unsuccessful"
    ]);
    var statusValue = document.getElementById("person-status-select").value;
    var changeReason = document.getElementById("person-status-reason-field").value.trim();

    if (!statusValue) {
        jq('#person-status-select-empty').css({'color' : 'red', display : 'inline'}).show();
    } else if (statusValue === 'DEACTIVATE' && (!changeReason || changeReason.length === 0)) {
        jq('#person-status-reason-field-empty').css({'color' : 'red', display : 'inline'}).show();
    } else {
        jq('#person-status-update-dialog' + ' .icon-spin').css('display', 'inline-block').parent().addClass('disabled');
        var url = '/' + OPENMRS_CONTEXT_PATH + '/messages/patientdashboard/changeStatus/update.action';
        jq.ajax({
            url: url,
            type: 'POST',
            data: { personId: changeStatus.personId, personStatusValue: statusValue, personStatusReason: changeReason},
            success: function() {
                emr.successMessage('person.status.update.successful');
                jq('#person-status-update-dialog' + ' .icon-spin').css('display', 'none').parent().removeClass('disabled');
                changeStatus.updatePersonStatusDialog.close();
                location.reload();
            },
            error: function() {
                emr.errorMessage("person.status.update.unsuccessful");
                jq('#person-status-update-dialog' + ' .icon-spin').css('display', 'none').parent().removeClass('disabled');
                changeStatus.updatePersonStatusDialog.close();
            }
        });
    }
}

changeStatus.showPersonStatusUpdateDialog = function(personId) {
    changeStatus.personId = personId;
    if (changeStatus.updatePersonStatusDialog == null) {
        changeStatus.createPersonStatusUpdateDialog();
    }
    jq('#person-status-select-empty').hide();
    jq('#person-status-reason-field-empty').hide();
    changeStatus.handelChangedStatus(document.getElementById("person-status-select"));
    changeStatus.updatePersonStatusDialog.show();
};
