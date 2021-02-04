<#import "parts/common.ftl" as c>
<@c.page>
    <div style="text-align: center">
        <h2>${username}</h2>
        <hr>
    </div>
    <div style="color: red"> ${message!}</div>
    <form method="post">
        <div class="register-form row justify-content-center">
            <div class="col-sm-5">
                <div class="form-group">
                    <label class="col col-form-label">
                        <b>Current email:   </b><i>${email!'Email not found'}</i> </label>
                    <br>
                    <label class="col col-form-label">New email: </label>
                    <div class="col">
                        <input class="form-control" type="email" name="email" placeholder="Email"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col col-form-label">New password: </label>
                    <div class="col">
                        <input class="form-control" type="password" name="password" placeholder="Password"/>
                    </div>
                </div>
                <br>
                <div style="text-align: center">
                    <button class="btn btn-primary" type="submit">Save</button>
                </div>
            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</@c.page>
