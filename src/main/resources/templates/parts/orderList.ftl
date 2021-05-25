<#macro ordersList path isManager isClient>
    <#list page.content as order>
        <tr data-id="${order.id}"
                <#if order.finished>
                    class="${(order.confirmed)?string('bg-secondary','bg-danger')}"
                <#else>
                    class="${(order.approved)?string('bg-success','bg-warning')}"
                </#if>
        >
            <td data-type="id">
                <span>${order.id}</span>
            </td>
            <td data-type="book-code">
                <span>${order.book.code}</span>
            </td>
            <td data-type="username">
                <a class="text-dark" href="/catalog/${order.client.id}">
                    ${order.client.username}
                </a>
            </td>
            <td data-type="status">
                <span>${order.status!}</span>
            </td>
            <td data-type="date">
                <span>${order.date!}</span>
            </td>
            <td>
                <#if !order.finished &&
                (isManager && !order.approved || isClient && order.approved)>
                    <form method="post" action="${path}">
                        <input type="hidden" name="order_id" value="${order.id}"/>
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <div class="form-group m-0">
                            <button type="submit" class="btn btn-link p-0" style="line-height: normal!important;color: black">
                                <i class="fas fa-check-double"></i>
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