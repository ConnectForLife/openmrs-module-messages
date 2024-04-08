<%
    ui.includeJavascript("messages", "changeStatus.js")
%>

<style>
  .required {
     color: #737373;
     font-weight: normal;
     font-family: CircularStd-Book;
  }

  #changeStatusCancelButton {
    background: transparent;
    border: #000000 1px solid !important;
    color: #00455c;
    margin: 0;
    font-size: 11px;
    text-transform: uppercase;
  }

  #changeStatusConfirmButton {
    background-image: none;
    background-color: #00455c;
    border-color: #00455c;
    margin: 0;
    font-size: 11px;
    text-transform: uppercase;
  }

  #person-status-select {
    color: black;
  }

  .form-label .label-required {
    display: inline;
    color: #737373;
    font-weight: normal;
  }
</style>

<div id="person-status-update-dialog" class="dialog" style="display: none">
    <div class="dialog-header">
        <i class="icon-user"></i>
            ${ ui.message("person.status.update.title") }
        </h3>
    </div>
    <div class="dialog-content">
        <p class="dialog-instructions">${ ui.message("person.status.update.instructions") }</p>
        <ul>
            <span class="form-label">
              <label for="person-status-select" class="control-label">${ ui.message("person.status.update.label") }:
                <p class="label-required">(${ ui.message("common.required") })</p>
              </label>
            </span>
            <li>
                <select id="person-status-select" class="required focused" onchange="changeStatus.handelChangedStatus(this)">
                    <% for(item in personStatusValues) { %>
                        <option value="${item.name()}"
                        style="font-size: 16px;"
                        ${ (personStatusValue.equals(item.name()) ? 'selected' : '') }>
                            ${ ui.message(item.getTitleKey())}
                        </option>
                    <% } %>
                </select>
                <h6 id="person-status-select-empty">${ ui.message("common.required") }</h6>
            </li>
            <li class="info person-status-reason">
                <label for="person-status-reason-select">
                    ${ ui.message("person.status.update.reason.label") }:
                </label>
            </li>
            <li class="person-status-reason">
                <select id="person-status-reason-select">
                    <% for(item in possibleReasons) { %>
                        <option value="${item}"
                        style="font-size: 16px;"
                        ${ (personStatusReason.equals(item) ? 'selected' : '') }>
                            ${ ui.message(item)}
                        </option>
                    <% } %>
                </select>
                <br>
                <h6 id="person-status-reason-select-empty">${ ui.message("messages.required") }</h6>
            </li>
        </ul>

        <button id="changeStatusConfirmButton" class="confirm right">
            ${ ui.message("messages.confirm") }
            <i class="icon-spinner icon-spin icon-2x" style="display: none; margin-left: 10px;"></i>
        </button>
        <button id="changeStatusCancelButton" class="cancel">${ ui.message("messages.cancel") }</button>
    </div>
</div>
