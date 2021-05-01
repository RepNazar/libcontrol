<#import "parts/common.ftl" as c>
<#import "parts/loginTemplete.ftl" as l>

<@c.page>
    <div class="mb-1">Sign Up</div>
    ${message!}
    <@l.login "/registration" true false />
</@c.page>
