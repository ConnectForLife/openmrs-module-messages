<%
    ui.includeCss("messages", "bestContactTime.css")
    ui.includeJavascript("messages", "editBestContactTime.js")
    ui.includeJavascript("messages", "moment-with-locales.min.js")
    ui.includeJavascript("messages", "moment-timezone-with-data-10-year-range.min.js")
    def editIcon = config.editIcon ?: "icon-pencil"
%>

<head>
  <!-- Timepicker JS -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/timepicker/1.3.5/jquery.timepicker.min.js"></script>
</head>

<style>
.disabled-time {
    border: 3px solid red;
}

.time-entry-wrapper .tooltiptext {
    visibility: hidden;
    width: 200px;
    background-color: #555;
    color: #fff;
    text-align: center;
    border-radius: 6px;
    padding: 5px 0;
    position: absolute;
    z-index: 1;
    bottom: 125%;
    left: 20%;
    margin-left: -60px;
    opacity: 0;
    transition: opacity 0.3s;
}

.time-entry-wrapper:hover .tooltiptext {
    visibility: visible;
    opacity: 1;
}

.time-entry-wrapper {
    position: relative;
    display: inline-block;
}
</style>

<div class="info-section">
    <div class="info-header" style="padding-bottom: 4px;">
        <i class="${config.icon}"></i>
        <h3>${ ui.message("messages.title") }</h3>
        <% if (config.widgetPencilIconVisible && config.detailsUrl) { %>
            <i class="${editIcon} edit-action right" title="${ ui.message("coreapps.edit") }"
               onclick="location.href='${ ui.urlBind("/" + contextPath + "/" + config.detailsUrl,
                        [
                            "person.uuid": personUuid,
                            "personUuid": personUuid,
                            "person.id": personId,
                            "personId": personId
                        ]) }';"></i>
        <% } %>
    </div>

    <div class="info-body">
        <span class="best-contact-title">
            ${ ui.message("messages.dashboard.bestContactTime") }
        </span>
        <div class="different-timezone">
            <span>${ ui.message("messages.dashboard.differentTimezone") + " " + timezone }</span>
            <script type="text/javascript">
                jq(".different-timezone").hide();
                const localZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
                const localZoneOffset = moment().tz(localZone).utcOffset();
                const serverTimezoneOffset = moment().tz("${timezone}").utcOffset();
                if (localZoneOffset !== serverTimezoneOffset) {
                    jq(".different-timezone").show();
                }
            </script>
        </div>
        <div id="best-contact-times">
            <% bestContactTimes.each { %>
                <div id="time-entry-${ it.label.replaceAll(" ", "-") }" class="time-entry">
                    <label for="time-label-${ it.label.replaceAll(" ", "-") }" class="time-label">
                        ${ it.label.replaceAll(" ", "-")}
                    </label>
                    <span>
                        <input
                            class="edit-best-contact-time"
                            id="time-input-${ it.label.replaceAll(" ", "-") }"
                            type="text"
                            value="${ it.time }"
                            onBlur="handleBestContactTimeOnChange(this, '${ it.time }')"
                        />
                        <button 
                            class="save-time-button" 
                            id="save-button-${ it.label.replaceAll(" ", "-") }"
                            onClick="onSaveTimeButtonClick(${personId}, 'time-input-${ it.label.replaceAll(" ", "-") }')" 
                            data-target="time-input-${ it.label.replaceAll(" ", "-") }">Save</button>
                    </span>
                    <span class="error-message" id="error-message-${ it.label.replaceAll(" ", "-") }"></span>
                </div>
            <% } %>
        </div>
    </div>
</div>
