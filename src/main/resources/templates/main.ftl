<#import "parts/common.ftl" as c>
<#import "parts/messageEdit.ftl" as me>
<@c.page>
    <div class="row p-2 bg-secondary text-white">
        <div class="col">
            <div class="row justify-content-between">
                <div class="col-4">
                    <@me.editButton/>
                </div>
                <div class="col-4 ">
                    <form class="form-inline" method="get" action="/main">
                        <input type="text" name="filter" class="form-control" value="${filter!}"
                               placeholder="Search by tag"/>
                        <button class="btn btn-primary ml-2" type="submit">Search</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <@me.editBlock/>
    <hr>
    <#include "parts/messageList.ftl">
</@c.page>