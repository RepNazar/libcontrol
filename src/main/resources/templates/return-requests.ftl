<#include "parts/security.ftl">
<#import "parts/orderList.ftl" as list>
<#import "parts/common.ftl" as c>

<@c.page>

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
        <@list.ordersList "/return-requests" isManager isClient />
        </tbody>
    </table>

</@c.page>