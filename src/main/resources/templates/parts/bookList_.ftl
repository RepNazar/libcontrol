<#include "security.ftl">
<#list catalog as book>
    <tr data-id="${book.id}" class="${(book.inStock)?string('','bg-secondary')}">
        <td data-type="code">
            <a href="/catalog?book=${book.id}">${book.code}</a>
        </td>
        <td data-type="name">
            <a href="/catalog?book=${book.id}">${book.name}</a>
        </td>
        <td>
            <#if isLibrarian>
                <form method="post" action="/catalog/delete">

                    <input type="hidden" name="id" value="${book.id}"/>
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="form-group m-0">
                        <button type = "submit" class ="btn btn-link p-0" style="line-height: normal!important;">
                            Delete
                        </button>
                    </div>
                </form>
            </#if>
            <#if isClient>
                <a href="/order/${book.id}" class="btn btn-link">Order</a>
            </#if>
        </td>
    </tr>
<#else>
    <tr>
        <td>No books</td>
    </tr>
</#list>

