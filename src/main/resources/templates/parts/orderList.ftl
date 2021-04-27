<#include "security.ftl">
<#list orders as order>
    <tr data-id="${order.id}">
        <td data-type="id">
            <a href="">${order.id}</a>
        </td>
        <td data-type="status">
            <a href="/orders/${order.client.id}?order=${order.id}">${order.status}</a>
        </td>
        <td data-type="dale">
            <a href="/orders/${order.client.id}?order=${order.id}">${order.date}</a>
        </td>
        <td>
            <#if order.client.id == currentUserId>
                <form method="post" action="/user-orders/${order.client.id}/delete">
                    <input type="hidden" name="id" value="${order.id}"/>
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="form-group m-0">
                        <button type = "submit" class ="btn btn-link p-0" style="line-height: normal!important;">
                            Delete
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

