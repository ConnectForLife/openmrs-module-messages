<%
    ui.includeCss("messages", "personStatus.css")
%>

<% if(personStatusValue) { %>
    <div class="person-status" style="${(personStatusMessageStyle ? personStatusMessageStyle :'')}"
        onClick="changeStatus.showPersonStatusUpdateDialog('${personId}')">
        ${ui.message("person.status.label")} ${ui.message(personStatusValue)}
    </div>
<% } %>
