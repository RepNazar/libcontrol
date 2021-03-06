<#import "parts/common.ftl" as c>

<@c.page>
    <h5 class="mb-4">${username}</h5>
    ${message!}
    <form method="post" action="/user/profile">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">New Password:</label>
            <div class="col-sm-4">
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       placeholder="Password" />
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Retype Password:</label>
            <div class="col-sm-4">
                <input type="password" name="password2" class="form-control"
                       placeholder="Retype password" />
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Email:</label>
            <div class="col-sm-4">
                <input type="email" name="email" id="email-profile"
                       class="form-control ${(emailError??)?string('is-invalid', '')}"
                       placeholder="some@some.com"
                       value="${email!''}" />
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit">Save</button>
    </form>
</@c.page>