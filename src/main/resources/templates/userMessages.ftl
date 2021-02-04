<#import "parts/common.ftl" as c>
<#import "parts/messageEdit.ftl" as me>
<@c.page>
    <div class="row justify-content-between">
        <div class="col-md-2">
            <div class="col-md-2 mb-6 ml-4" style="text-align: center">
                <h3>${userChannel.username}</h3>
            </div>
        </div>
        <div class="col-md-2">
            <#if !isCurrentUser>
                <#if isSubscriber>
                    <a class="btn btn-info" href="/user/unsubscribe/${userChannel.id}">Unsubscribe</a>
                <#else>
                    <a class="btn btn-info" href="/user/subscribe/${userChannel.id}">Subscribe</a>
                </#if>
            </#if>
        </div>
    </div>

    <div class="container mb-8 p-3">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscriptions</div>
                        <h3 class="card-text">
                            <a href="/user/subscriptions/${userChannel.id}/list">${subscriptionsCount}</a>
                        </h3>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscribers</div>
                        <h3 class="card-text">
                            <a href="/user/subscribers/${userChannel.id}/list">${subscribersCount}</a>
                        </h3>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div class="mt-8">
        <#if isCurrentUser && message??>
            <@me.editButton/>
            <@me.editBlock/>
        </#if>
        <#include "parts/messageList.ftl">
    </div>
</@c.page>