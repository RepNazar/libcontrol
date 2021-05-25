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
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td>${user.email!}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>

