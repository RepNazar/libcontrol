<#import "parts/common.ftl" as c>
<#import "parts/loginTemplete.ftl" as l>


<@c.page>
    <div class="mb-1">Add new user</div>
    ${message!}
    <@l.login "/register" true true />
</@c.page>
