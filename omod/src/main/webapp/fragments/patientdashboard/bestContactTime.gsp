<%
    ui.includeCss("messages", "bestContactTime.css")
    ui.includeJavascript("messages", "moment-with-locales.min.js")
    def editIcon = config.editIcon ?: "icon-pencil"
%>
<div class="info-section">
    <div class="info-header" style="padding-bottom: 4px;">
        <i class="${config.icon}"></i>
        <h3>${ ui.message("messages.title") }</h3>
        <% if (editIcon && config.detailsUrl) { %>
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
                if ("${timezone}" !== localZone) {
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
                    <input
                        id="time-value-${ it.label.replaceAll(" ", "-") }"
                        class="time-value"
                        type="text"
                        disabled />
                    <script type="text/javascript">
                        jq( document ).ready(function() {
                            let value = "${ ui.message("messages.dashboard.noBestContactTime") }";
                            let time = moment("${ it.time }", "HH:mm");
                            if (time.isValid()) {
                                value = time.format('HH:mm');
                            }
                            jq("#time-value-${ it.label.replaceAll(" ", "-") }").val(value);
                        });
                    </script>
                </div>
            <% } %>
        </div>
    </div>
</div>
