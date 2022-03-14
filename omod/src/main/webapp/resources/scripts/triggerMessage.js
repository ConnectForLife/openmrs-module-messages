triggerMessage = triggerMessage || {};
emr = emr || {};
jq = jq || {};
OPENMRS_CONTEXT_PATH = OPENMRS_CONTEXT_PATH || '/openmrs';

triggerMessage.triggerMessageCreationDialog = null;

triggerMessage.createTriggerMessageCreationDialog = function() {
    emr.loadMessages([
        "messages.triggerMessage.success",
        "messages.triggerMessage.failed"
    ]);

    triggerMessage.triggerMessageCreationDialog = emr.setupConfirmationDialog({
        selector: '#trigger-message-dialog',
        actions: {
            confirm: function() {
                jq.ajax({
                    url: `/${OPENMRS_CONTEXT_PATH}/ws/messages/triggerMessage/${triggerMessage.personUuid}/${triggerMessage.templateNames}/${triggerMessage.channelType}`,
                    type: 'POST',
                    success: function() {
                        emr.successMessage("messages.triggerMessage.success");
                        triggerMessage.triggerMessageCreationDialog.close();
                    },
                    error: function(data) {
                        emr.errorMessage("messages.triggerMessage.failed");
                        triggerMessage.triggerMessageCreationDialog.close();
                        if (data.status == 403) {
                            window.location.href = `/${OPENMRS_CONTEXT_PATH}/login.htm`;
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
    emr.navigateTo({ applicationUrl: emr.applyContextModel(triggerMessage.returnUrl)});
};

triggerMessage.enableConfirmButton = function() {
    jq('#trigger-message-dialog' + ' .icon-spin').css('display', 'inline-block').parent().addClass('enabled');
};