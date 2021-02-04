<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
    <#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
        <div class="danger alert-danger" role="alert">
            ${Session.SPRING_SECURITY_LAST_EXCEPTION}
        </div>
    </#if>
    <#if message??>
        <div class="danger alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>
    <@l.login "/login" false/>
</@c.page>
