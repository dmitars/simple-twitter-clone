<#macro editButton>
<a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
    Message editor
</a>
</#macro>

<#macro editBlock>
<div class="collapse" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group">
                <input class="form-control ${(textError??)?string('is-invalid','')}"
                       type="text" name="text" value="<#if message??>${message.text}</#if>" placeholder="enter your message"/>
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input class="form-control" value="<#if message??>${message.tag}</#if>"
                       type="text" name="tag" placeholder="enter your tags"/>
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile"/>
                    <label class="custom-file-label" for="customFile">Select file</label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <input type="hidden" name="id"  value="<#if message??>${message.id}</#if>" />
            <div class="form-group">
                <button class="btn btn-primary ml-2" type="submit">Save</button>
            </div>
        </form>
    </div>
</div>
</#macro>