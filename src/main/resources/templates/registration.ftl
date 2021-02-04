<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
    <div style="color: red; text-align: center; font-size: 2em"> ${message!}</div>
<@l.login "/registration" true/>
</@c.page>