<%
    ui.includeCss("messages", "bestContactTime.css")
    ui.includeJavascript("messages", "moment-with-locales.min.js")
%>
<div class="info-section">
    <div class="info-header">
        <i class="${config.icon}"></i>
        <h3>${ ui.message("messages.title") }</h3>
    </div>

    <div class="info-body">
        <span class="best-contact-title">
            ${ ui.message("messages.dashboard.bestContactTime") }
        </span>
        <div id="best-contact-times">
            <% bestContactTimes.each { %>
                <div id="time-entry-${ it.label.replaceAll(" ", "-") }" class="time-entry">
                    <label for="time-label-${ it.label.replaceAll(" ", "-") }" class="time-label">
                        ${ it.label.replaceAll(" ", "-")}
                    </label>
                    <input
                        id="time-value-${ it.label.replaceAll(" ", "-") }"
                        class="time-value"
                        type="time"
                        disabled />
                    <script type="text/javascript">
                        jq("#time-value-${ it.label.replaceAll(" ", "-") }")
                            .val(moment.utc("${ it.time }", "HH:mm").local().format('HH:mm'));
                    </script>
                </div>
            <% } %>
        </div>
    </div>
</div>
