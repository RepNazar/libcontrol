<#include "security.ftl">
<#import "loginTemplete.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">libcontrol</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/catalog">Catalog</a>
            </li>
            <#if isClient>
                <li class="nav-item">
                    <a class="nav-link" href="/my-orders">My orders</a>
                </li>
            </#if>
            <#if isManager>
                <li class="nav-item">
                    <a class="nav-link" href="/orders">Orders</a>
                </li>
            </#if>
        </ul>
        <#if user??>
            <div class="navbar-text mr-3">${name}</div>
            <@l.logout />
        <#else>
            <a class="btn btn-primary" href="/login">Log in</a>
        </#if>
    </div>
</nav>
