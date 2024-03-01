<%
    def MODAL_TITLE_TEXT_KEY = config.modalTitleText
    def MODAL_BODY_TEXT_KEY = config.modalBodyText

    ui.includeJavascript("messages", "triggerMessage.js")
%>

<div id="trigger-message-dialog" class="dialog" style="display: none">
    <div class="dialog-header">
        <h3>
            ${ ui.message(MODAL_TITLE_TEXT_KEY) }
        </h3>
    </div>
    <div class="dialog-content">
        <p class="dialog-instructions">${ ui.message(MODAL_BODY_TEXT_KEY) }</p>
        <button class="confirm right">${ ui.message("coreapps.confirm") }
            <i class="icon-spinner icon-spin icon-2x" style="display: none; margin-left: 10px;"></i>
        </button>
        <button class="cancel">${ ui.message("coreapps.cancel") }</button>
    </div>
</div>
