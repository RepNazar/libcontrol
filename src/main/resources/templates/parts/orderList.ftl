<#macro ordersList path isEmployee>
    <#list page.content as order>
        <tr data-id="${order.id}"
                <#if order.finished>
                    class="${(order.confirmed)?string('bg-secondary','bg-danger')}"
                <#else>
                    class="${(order.approved)?string('bg-success','bg-warning')}"
                </#if>
        >
            <td data-type="id">
                <a href="">${order.id}</a>
            </td>
            <td data-type="status">
                <a href="">${order.status}</a>
            </td>
            <td data-type="date">
                <a href="">${order.date}</a>
            </td>
            <td>
                <#if !order.finished && (isEmployee != order.approved)>
                        <form method="post" action="${path}">
                            <input type="hidden" name="order_id" value="${order.id}"/>
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <div class="form-group m-0">
                                <button type="submit" class="btn btn-link p-0" style="line-height: normal!important;">
                                    <#if isEmployee>Approve<#else>Confirm</#if>
                                </button>
                            </div>
                        </form>
                </#if>
            </td>
        </tr>
    <#else>
        <tr>
            <td>No orders</td>
        </tr>
    </#list>

</#macro>