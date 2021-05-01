<#include "security.ftl">
<#if personalized?? && page.content[0]?? && page.content[0].owner??>
    <h4>${page.content[0].owner.username!} books:</h4>
</#if>
<#list page.content as book>
    <#if book.owner??>
        <#if book.owner.id == currentUserId>
            <#assign redirectLink="my-books">
        <#elseif isEmployee>
            <#assign redirectLink="/catalog/${book.owner.id}">
        <#else>
            <#assign redirectLink="">
        </#if>
    <#elseif isLibrarian>
        <#assign redirectLink="/catalog?book=${book.id}">
    <#else>
        <#assign redirectLink="">
    </#if>

    <#if !personalized?? || !book.inStock>
        <tr data-id="${book.id}"
            <#if !personalized??>class="${(book.inStock)?string('','bg-secondary')}"</#if>>
            <td data-type="code">
                <a href="${redirectLink}">${book.code}</a>
            </td>
            <td data-type="name">
                <a href="${redirectLink}">${book.name}</a>
            </td>
            <td>
                <#if isLibrarian && !(personalized??)>
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
    </#if>
<#else>
    <tr>
        <td>No books</td>
    </tr>
</#list>

