<#include "security.ftl">
<#list catalog as book>
    <#if book.owner??>
        <#if book.owner.id == currentUserId>
            <#assign redirectLink="my-books">
        <#elseif isManager || isDirector>
            <#assign redirectLink="/catalog/${book.owner.id}">
        </#if>
    <#else>
        <#assign redirectLink="">
    </#if>

    <tr data-id="${book.id}" class="${(book.inStock)?string('','bg-secondary')}">
        <td data-type="code">
            <a href="${redirectLink}">${book.code}</a>
        </td>
        <td data-type="name">
            <a href="${redirectLink}">${book.name}</a>
        </td>
        <td>
            <#if isLibrarian>
                <form method="post" action="/catalog/delete">

                    <input type="hidden" name="id" value="${book.id}"/>
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="form-group m-0">
                        <button type="submit" class="btn btn-link p-0" style="line-height: normal!important;">
                            Delete
                        </button>
                    </div>
                </form>
            </#if>
            <#if book.inStock>
                <#if isClient>
                    <a href="/order/${book.id}" class="btn btn-link">Order</a>
                </#if>
            <#else>
                <#if book.owner?? && book.owner.id == currentUserId>
                <form method="post" action="/my-books" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="<#if book??>${book.id!}</#if>"/>
                    <input type="hidden" name="code" value="${book.code}">
                    <input type="hidden" name="name" value="${book.name}">
                    <input type="hidden" name="inStock" <#if book??>value="${book.inStock?string('yes','no')}"
                           <#else>value="true"</#if> />
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="form-group m-0">
                        <button type="submit" class="btn btn-primary">Return</button>
                    </div>
                </form>
                </#if>
            </#if>
        </td>
    </tr>
<#else>
    <tr>
        <td>No books</td>
    </tr>
</#list>

