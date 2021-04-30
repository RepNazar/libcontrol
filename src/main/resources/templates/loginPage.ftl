<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>
<#import "parts/loginTemplete.ftl" as l>

<@c.page>
    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>

    <@l.login "/login" false false />
</@c.page>