<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<#import "parts/pager.ftl" as p>
<@c.page>

    <#if owner??>
        <#if isEmployee>
            <#assign currentUrl = "/catalog/${owner.id!}">
        <#else>
            <#assign currentUrl = "/my-books">
        </#if>
    <#else>
        <#assign currentUrl = "/catalog">
    </#if>


    <form method="get" action="${currentUrl}">
        <div class="row">
            <div class="col">
                <div class="form-row">
                    <div class="form-group col-md-5">
                        <label for="nameContainsFilterInput">Name Contains:</label>
                        <input id="nameContainsFilterInput" type="text" name="filter"
                               class="form-control" value="${filter!}" placeholder="Name contains"
                        />
                    </div>
                    <div class="col mt-auto">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="form-row">
                    <div class="form-group col-md-5 float-right ml-auto">
                        <label for="genreContainsFilterInput">Genre:</label>
                        <input id="genreContainsFilterInput" type="text" name="genreFilter"
                               class="form-control" value="${genreFilter!}" placeholder="Genres"
                        />
                    </div>
                    <div class="form-group col-md-1 mt-auto mr-md-2">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <#if page.getTotalElements() gt 25>
        <@p.pager currentUrl page/>
    </#if>

    <table class="table table-sm table-striped">
        <thead>
        <tr>
            <th scope="col">Code</th>
            <th scope="col">Name</th>
            <th scope="col">Genre</th>
            <th scope="col"></th>
        </tr>
        </thead>

        <tbody id="books-list">
        <#if isLibrarian && !(owner??)>
            <form method="post" action="/catalog" enctype="multipart/form-data">
                <#include "parts/bookEdit.ftl" />
            </form>
        </#if>
        <#include "parts/bookList.ftl" />
        </tbody>
    </table>

    <#if page.getTotalElements() gt 25>
        <@p.pager currentUrl page/>
    </#if>

</@c.page>