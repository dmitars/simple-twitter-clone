<#import "parts/common.ftl" as c>
<@c.page>
User editor
    <form action="/user" method="post">
        <input type="hidden" value="${user.id!"null or missing"}" name="userId">
        <input type="text" value="${user.username!"null or missing"}" name="name">
        <#list roles as role>
            <div>
                <label>${role!} <input type="checkbox" name="${role!}" ${user.roles?seq_contains(role)?string("checked","unchecked")}></label>
            </div>
        </#list>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button type="submit">Save</button>
    </form>
</@c.page>