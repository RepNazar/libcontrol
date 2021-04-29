<#include "parts/security.ftl">
<#import "parts/orderList.ftl" as list>
<#import "parts/common.ftl" as c>
<@c.page>

    <table class="table table-sm table-striped">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Status</th>
            <th scope="col">Date</th>
            <th scope="col"></th>
        </tr>
        </thead>

        <tbody id="orders-list">
        <@list.ordersList "/return-requests"  true/>
        </tbody>
    </table>

</@c.page>