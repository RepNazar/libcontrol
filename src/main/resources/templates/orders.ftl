<#import "parts/common.ftl" as c>
<@c.page>

    <table class="table table-sm table-striped">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Status</th>
            <th scope="col">Date</th>
        </tr>
        </thead>

        <tbody id="orders-list">
        <#include "parts/orderList.ftl">
        </tbody>
    </table>

</@c.page>