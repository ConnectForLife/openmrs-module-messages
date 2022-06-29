<%
    ui.includeCss("messages", "personStatus.css")
%>

${ui.includeFragment("messages", "patientdashboard/changeStatus")}
<% if(personStatusValue) { %>
    <div class="person-status" style="${(personStatusMessageStyle ? personStatusMessageStyle :'')}"
        onClick="changeStatus.showPersonStatusUpdateDialog('${personIdOrUuid}')">
        ${ui.message("person.status.label")} ${ui.message(personStatusValue)}
    </div>
<% } %>
