<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <table class="table table-sm table-striped">
        <thead>
        <tr>
            <th scope="col">Code</th>
            <th scope="col">Name</th>
            <th scope="col"></th>
        </tr>
        </thead>

        <tbody id="records-list">
        <#if isLibrarian>
            <form method="post" action="/catalog" enctype="multipart/form-data">
                <#include "parts/bookEdit.ftl" />
            </form>
        </#if>
        <#include "parts/bookList_.ftl" />
        </tbody>
    </table>

</@c.page>