<#include "security.ftl">
<#list orders as order>
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
            <a href="/orders/${order.client.id}?order=${order.id}">${order.status}</a>
        </td>
        <td data-type="date">
            <a href="/orders/${order.client.id}?order=${order.id}">${order.date}</a>
        </td>
    </tr>
<#else>
    <tr>
        <td>No orders</td>
    </tr>
</#list>

