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
            <input type="text" class="form-control"
                   value="<#if book??>${book.name}</#if>" name="name" placeholder="Name" />
        </div>
    </td>
    <td>
        <input type="hidden" name="id" value="<#if book??>${book.id!}</#if>" />
        <input type="hidden" name="id" value="<#if book??>${book.inStock!} <#else>true</#if>" />
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <div class="form-group m-0">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </td>
</tr>