<#import "parts/common.ftl" as c>
<h3>You made an order for book:</h3>
<div data-type="name">${book.name}</div>
<h3>Book code is:</h3>
<div data-type="code">${book.code}</div>
<h3>Confirm order?</h3>
<form method="post" action="/order/${book.id}">
    <input type="hidden" name="id" value="${book.id}"/>
    <input type="hidden" name="inStock" value="${book.inStock?string('yes','no')}"/>
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <div class="form-group m-0">
        <button type = "submit" class ="btn btn-link p-0" style="line-height: normal!important;">
            Confirm
        </button>
    </div>
</form>

