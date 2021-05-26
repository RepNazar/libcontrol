<#import "parts/common.ftl" as c>

<@c.page>
    <h5>Changing data of the user:</h5>
    <h4>${user.username!}</h4>

    <form action="/users" method="post">
        <div class="form-group row my-3">
            <label class="col-sm-2 col-form-label">User role:</label>
            <label class="col-sm-4 col-form-label">
                <#list user.roles as role>${role}<#sep>, </#list>
            </label>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-4">
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       placeholder="Password"/>
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
                <input type="password" name="password2" class="form-control" placeholder="Retype password"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Email:</label>
            <div class="col-sm-4">
                <input type="email" name="email" id="email-profile"
                       class="form-control ${(emailError??)?string('is-invalid', '')}"
                       placeholder="some@some.com"
                       value="<#if user??>${user.email!}</#if>"/>
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
        </div>

        <input type="hidden" value="${user.id}" name="userId" />
        <input type="hidden" value="${_csrf.token}" name="_csrf" />
        <button type="submit" class="btn btn-primary">Save</button>
    </form>
</@c.page>
