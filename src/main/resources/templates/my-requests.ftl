<#include "parts/security.ftl">
<#include "parts/security.ftl">
<#import "parts/orderList.ftl" as list>
<#import "parts/common.ftl" as c>
<#import "parts/pager.ftl" as p>

<@c.page>
    <#if page.getTotalElements() gt 25>
        <@p.pager "/my-requests" page/>
    </#if>

    <table class="table table-sm table-striped">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Book</th>
            <th scope="col">Client</th>
            <th scope="col">Status</th>
            <th scope="col">Date</th>
            <th scope="col"></th>
        </tr>
        </thead>

        <tbody id="requests-list">
        <@list.ordersList "/my-requests" isManager isClient />
        </tbody>
    </table>
    <#if page.getTotalElements() gt 25>
        <@p.pager "/my-requests" page/>
    </#if>

</@c.page>