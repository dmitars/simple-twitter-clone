<#include "security.ftl">
<#macro logout>
    <form action="/logout" method="post">
        <button class="btn btn-primary" type="submit"><#if user??>Sign Out<#else>Login</#if></button>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>

<#macro login path isRegisterForm>
    <form action="${path}" method="post" class="p-8">
        <div class="row justify-content-center">
            <div class="col-em-3 register-form">
                <div class="register-title mb-3"><b><#if isRegisterForm>Registration<#else>Authorization</#if></b></div>
                <div class="form-group">
                    <label class="col col-form-label"> User Name : </label>
                    <input class="form-control ${(usernameError??)?string('is-invalid','')}"
                           type="text" name="username" value="<#if user??>${user.username}</#if>"
                           placeholder="User name"/>
                    <#if usernameError??>
                        <div class="invalid-feedback">
                            ${usernameError}
                        </div>
                    </#if>
                </div>
                <div class="form-group">
                    <label class="col col-form-label"> Password: </label>
                    <input class="form-control ${(passwordError??)?string('is-invalid','')}"
                           type="password" name="password" placeholder="Password"/>
                    <#if passwordError??>
                        <div class="invalid-feedback">
                            ${passwordError}
                        </div>
                    </#if>
                </div>
                <#if isRegisterForm>
                    <div class="form-group">
                        <label class="col col-form-label"> Repeat password: </label>
                        <input class="form-control ${(password2Error??)?string('is-invalid','')}"
                               type="password" name="password2" placeholder="Repeat password"/>
                        <#if password2Error??>
                            <div class="invalid-feedback">
                                ${password2Error}
                            </div>
                        </#if>
                    </div>
                    <div class="form-group">
                        <label class="col col-form-label"> Email: </label>
                        <input class="form-control ${(emailError??)?string('is-invalid','')}"
                               type="email" value="<#if user??>#{user.email}</#if>" name="email"
                               placeholder="Email"/>
                        <#if emailError??>
                            <div class="invalid-feedback">
                                ${emailError}
                            </div>
                        </#if>
                    </div>
                    <div>
                        <div class="g-recaptcha" data-sitekey="6Ld9uj4aAAAAAJTHAGBbaDFqOJfO7gqygg8INjyP"></div>
                        <#if captchaError??>
                            <div class="danger alert-danger" role="alert">
                                ${captchaError}
                            </div>
                        </#if>
                    </div>
                </#if>
                <div style="text-align: center;">
                    <#if !isRegisterForm><a href="/registration">Add new user</a></#if>
                    <br>
                    <button class="mt-1 btn btn-primary"
                            type="submit"><#if isRegisterForm>Sign Up<#else>Sign In</#if></button>
                </div>

            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>