<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <h4>You made an order for book:</h4>
    <div class="h3" data-type="name">${book.name}</div>
    <h4 class="mt-4">Book code is:</h4>
    <div class="h3" data-type="code">${book.code}</div>
    <h4 class="mt-4">Confirm order?</h4>
    <form method="post" action="/order/${book.id}" enctype="multipart/form-data">
        <input type="hidden" name="book" value="${book.id}"/>
        <input type="hidden" name="client" value="${currentUserId}"/>
        <input type="hidden" name="id" value="<#if order??>${order.id!}</#if>"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div class="form-group m-0">
            <button type="submit" class="btn btn-primary">
                Confirm
            </button>
        </div>
    </form>
</@c.page>