<#include "security.ftl">
<#if owner??>
    <h4>${owner.username!} books:</h4>
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

    <tr data-id="${book.id}"
        <#if !owner??>class="${(book.inStock)?string('','bg-secondary')}"</#if>>
        <td data-type="code">
            <a href="${redirectLink}">${book.code}</a>
        </td>
        <td data-type="name">
            <a href="${redirectLink}">${book.name}</a>
        </td>
        <td data-type="genre">
            <a href="${redirectLink}">${book.genre!}</a>
        </td>

        <td>
            <#if isLibrarian && !(owner??)>
                <form method="post" action="/catalog/delete">

                    <input type="hidden" name="id" value="${book.id}"/>
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="form-group m-0">
                        <button type="submit" class="btn btn-link p-0" style="line-height: normal!important;">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </div>
                </form>
            </#if>
            <#if book.inStock>
                <#if isClient>
                    <a href="/order/${book.id}" class="btn btn-link p-0">
                        <i class="fas fa-shopping-cart"></i>
                    </a>
                </#if>
            <#else>
                <#if book.owner?? && book.owner.id == currentUserId>
                    <form method="post" action="/my-books" enctype="multipart/form-data">
                        <input type="hidden" name="book" value="${book.id}"/>
                        <input type="hidden" name="client" value="${currentUserId}"/>
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <div class="form-group m-0">
                            <button type="submit" class="btn btn-link p-0">
                                <i class="fas fa-undo"></i>
                            </button>
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

