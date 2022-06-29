const emrVar = window.emr || {};
const jqVar = window.jq || {};

let triggerMessage = window.triggerMessage || {};

triggerMessage.triggerMessageCreationDialog = null;

triggerMessage.createTriggerMessageCreationDialog = function() {
    emrVar.loadMessages([
        "messages.triggerMessage.success",
        "messages.triggerMessage.failed"
    ]);

    triggerMessage.triggerMessageCreationDialog = emrVar.setupConfirmationDialog({
        selector: '#trigger-message-dialog',
        actions: {
            confirm: function() {
                jqVar.ajax({
                    url: `/openmrs/ws/messages/triggerMessage/${triggerMessage.personUuid}/${triggerMessage.templateNames}/${triggerMessage.channelType}`,
                    type: 'POST',
                    success: function() {
                        emrVar.successMessage("messages.triggerMessage.success");
                        triggerMessage.triggerMessageCreationDialog.close();
                    },
                    error: function(data) {
                        emrVar.errorMessage("messages.triggerMessage.failed");
                        triggerMessage.triggerMessageCreationDialog.close();
                        if (data.status == 403) {
                            window.location.href = `/openmrs/login.htm`;
                        }
                    },
                    final: function() {
                        triggerMessage.enableConfirmButton();
                    }
                });
            },
            cancel: function() {
                triggerMessage.triggerMessageCreationDialog.close();
            }
        }
    });
};

triggerMessage.showTriggerMessageCreationDialog = function(personUuid, templateNames, channelType) {
    triggerMessage.personUuid = personUuid;
    triggerMessage.templateNames = templateNames;
    triggerMessage.channelType = channelType;
    if (triggerMessage.triggerMessageCreationDialog == null) {
        triggerMessage.createTriggerMessageCreationDialog();
    }
    triggerMessage.triggerMessageCreationDialog.show();
};

triggerMessage.goToReturnUrl = function() {
    emrVar.navigateTo({ applicationUrl: emrVar.applyContextModel(triggerMessage.returnUrl)});
};

triggerMessage.enableConfirmButton = function() {
    jqVar('#trigger-message-dialog' + ' .icon-spin').css('display', 'inline-block').parent().addClass('enabled');
};
