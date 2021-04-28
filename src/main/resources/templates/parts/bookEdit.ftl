<tr>
    <td>
        <div class="form-group m-0">
            <input type="text" name="code"
                   value="<#if book??>${book.code}</#if>"
                   class="form-control" placeholder="Code" />
        </div>
    </td>
    <td>
        <div class="form-group m-0">
            <input type="text" name="name"
                   value="<#if book??>${book.name}</#if>"
                   class="form-control" placeholder="Name" />
        </div>
    </td>
    <td>
        <input type="hidden" name="id" value="<#if book??>${book.id!}</#if>" />
        <input type="hidden" name="inStock" <#if book??>value="${book.inStock?string('yes','no')}" <#else>value="true"</#if> />
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <div class="form-group m-0">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </td>
</tr>