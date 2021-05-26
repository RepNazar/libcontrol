<#import "parts/common.ftl" as c>

<@c.page>
    <h4>Employees list</h4>

    <table class="table table-sm table-striped">
        <thead>
        <tr>
            <th>Username</th>
            <th>Email</th>
            <th>Role</th>
            <th></th>
        </tr>
        </thead>
        <tbody id="users-list">
        <#list users as user>
            <tr data-id="${user.id}">
                <td data-type="username">${user.username}</td>
                <td data-type="email">${user.email!}</td>
                <td data-type="roles"><#list user.roles as role>${role}<#sep>, </#list></td>
                <td>
                    <a href="/user/${user.id}">
                        <i class="fas fa-edit"></i>
                    </a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>

