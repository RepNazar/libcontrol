<tr>
    <td>
        <div class="form-group m-0">
            <input type="text" name="code"
                   value="<#if book??>${book.code}</#if>"
                   class="form-control ${(codeError??)?string('is-invalid', '')}"
                   placeholder="Code"/>
            <#if codeError??>
                <div class="invalid-feedback">
                    ${codeError}
                </div>
            </#if>
        </div>
    </td>
    <td>
        <div class="form-group m-0">
            <input type="text" name="name"
                   value="<#if book??>${book.name}</#if>"
                   class="form-control ${(nameError??)?string('is-invalid', '')}"
                   placeholder="Name"/>
            <#if nameError??>
                <div class="invalid-feedback">
                    ${nameError}
                </div>
            </#if>
        </div>
    </td>
    <td>
        <div class="form-group m-0">
            <input type="text" name="genre"
                   value="<#if book??>${book.genre}</#if>"
                   class="form-control"
                   placeholder="Genre"/>
        </div>
    </td>
    <td>
        <input type="hidden" name="id" value="<#if book??>${book.id!}</#if>"/>
        <input type="hidden" name="inStock"
                <#if book??>
                    value="${book.inStock?string('yes','no')}"
                <#else>
                    value="true"
                </#if> />
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div class="form-group m-0">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </td>
</tr>