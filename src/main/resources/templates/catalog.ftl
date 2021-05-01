<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<#import "parts/pager.ftl" as p>
<@c.page>

    <#if personalized?? && page.content[0]?? && page.content[0].owner??>
        <#if isEmployee>
            <#assign currentUrl = "/catalog/${page.content[0].owner.id!}">
        <#else>
            <#assign currentUrl = "/my-books">
        </#if>
    <#else>
        <#assign currentUrl = "/catalog">
    </#if>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="${currentUrl}" class="form-inline">
                <input type="text" name="filter" class="form-control"
                       value="${filter!}" placeholder="Name contains"/>
                <button type="submit" class="btn btn-primary ml-2">
                    Search
                </button>
            </form>
        </div>
    </div>

    <#if page.getTotalElements() gt 25>
        <@p.pager currentUrl page/>
    </#if>

    <table class="table table-sm table-striped">
        <thead>
        <tr>
            <th scope="col">Code</th>
            <th scope="col">Name</th>
            <th scope="col"></th>
        </tr>
        </thead>

        <tbody id="records-list">
        <#if isLibrarian && !(personalized??)>
            <form method="post" action="/catalog" enctype="multipart/form-data">
                <#include "parts/bookEdit.ftl" />
            </form>
        </#if>
        <#include "parts/bookList_.ftl" />
        </tbody>
    </table>

    <#if page.getTotalElements() gt 25>
        <@p.pager currentUrl page/>
    </#if>

</@c.page>