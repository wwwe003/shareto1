$(function (){
    // console.log($('[name="draft"]').val())
    // console.log($('[name="status"]').val())
    $("#btn-manage").click(function (){
        const username = $(this).val()
        window.location.href="/toshare/"+username+"/myPost"
    })
    $("#btn-return-modal").click(function (){
        const username = $("#btn-manage").val()
        window.location.href="/toshare/"+username+"/myPost"
    })
    selectFunc()
    $('#btn-save').click(function () {
        $('[name="draft"]').val(1);
        $('[name="state"]').val(0);

        $('#confirmModal').modal()
        $("#confirm").click(function (){
            if (validate()){
                $('#form-post').submit()
            } else {
                $("#confirmModal").modal('hide')
                return false
            }
        })
    });

    $('#btn-post').click(function () {
        $('[name="draft"]').val(0);
        $('[name="state"]').val(1);

        $('#confirmModal').modal()
        $("#confirm").click(function (){
            if (validate()){
                $('#form-post').submit()
            } else {
                $("#confirmModal").modal('hide')
                return false
            }
        })
    });


    $("[name='description']").on("input propertychange", function() {
        let str = $(this).val()
        const countData=computedLen(str)
        $(this).attr({maxlength:countData.maxLen})
        $("#text-count").text(countData.leftLen)
        $("#text-max").text("/"+countData.maxLen)
    })

    $(".form-control").focus(function (){
        $(this).removeClass("is-error")
        const errorId=$(this).attr("name")+"Error"
        $("#"+errorId).remove()
    })
})
function validate() {
        let bool = true;
        if(!validateType()) {
            bool = false;
        }
        if(!validateDescription()) {
            bool = false;
        }
        if(!validateContent()) {
            bool = false;
        }
        if(!validateTitle()) {
            bool = false;
        }
        return bool;
    }
function validateTitle(){
    $("#titleError").remove()
    const title=$('[name="title"]').val().replace(/[ ]/g,"")
    if (title.length===0){
        $('[name="title"]').addClass("is-error")
        $("#check-bar").after("<div id='titleError' class='form-group w-75 errorMessage'><span>* title is null</span></div>")
        return false
    }else {
        $('[name="title"]').removeClass("is-error")
        return true
    }
}
function validateContent(){
    const content=$('[name="markdown"]').val().replace(/[ ]/g,"")
    $("#contentError").remove()
    if (content.length===0){
        $("#check-bar").after("<div id='contentError' class='form-group w-75 errorMessage'><span>* content is null</span></div>")
        return false
    }else {
        return true
    }
}
function validateDescription(){
    $('[name="description"]').removeClass("is-error")
    const description=$('[name="description"]').val().replace(/[ ]/g,"")
    $("#descriptionError").remove()
    if (description.length===0){
        $('[name="description"]').addClass("is-error")
        $("#check-bar").after("<div id='descriptionError' class='form-group w-75 errorMessage'><span>* description is null</span></div>")
        return false
    }else {
        return true
    }
}
function validateType(){
    const type1=$('#inputType1').val()
    console.log(type1)
    $("#type_first_nameError").remove()
    if (type1==="Type1"||type1===null){
        $('#inputType1').addClass("is-error")
        $('#inputType2').addClass("is-error")
        $("#check-bar").after("<div id='type_first_nameError' class='form-group w-75 errorMessage'><span>* type is not selected</span></div>")
        return false
    }else {
        $('#inputType1').removeClass("is-error")
        $('#inputType2').removeClass("is-error")
        return true
    }
}

function selectFunc() {
    $("#inputType1").change(function () {
        const type1 = $(this).val();
        const username=$("#btn-manage").val()
        if (type1 !== "Type1") {
            $.ajax({
                url: "/toshare/"+username+"/getType2",
                type: "POST",
                data: {method: "typeSeconds"},
                dataType: "json",
                async: true,
                cache: false,
                success: function (data) {
                    $.each(data, function (key, value) {
                        if (type1 === key) {
                            $("#inputType2").empty()
                            $.each(value, function (i, item) {
                                $("#inputType2").append("<option>" + item + "</option>")
                            })
                        }
                    })
                }
            })
        } else {
            $("#inputType2").empty().append("<option selected>Type2</option>")
        }
    })
}

function computedLen(str){
    // ??????
    const repOne = /[0-9a-zA-Z|\s]/;  // ????????????

    // ??????split ???????????????????????? ??????????????? ??????????????? ?????? ('ab\nc').split('') = ['a','b\n','c']
    const repEnter = /[&]/;  //  & ??????

    // console.log(str)
    // ???& ?????? ?????????
    str = str.replace(/[\r\n]/g , '&')

    // ??????????????????????????????
    const totalLen = 120;
    // ??????maxLen
    let maxLen = 120;
    // ???????????? ???????????????
    const strAttr = str.split('');
    let len = 0;
    // ???????????? ???????????????
    if (strAttr.length === 0){
        return {
            len : len ,
            leftLen : totalLen ,
            //tmp: tmp ,
            maxLen: maxLen
        }
    }else{
        //
        strAttr.forEach(function(val, key){
            // ??????????????????
            if ( repOne.test(val) ) {
                len+=0.5 // ?????? ??????0.5?????????
                maxLen+=0.5 // ?????????????????? 0.5?????????
            }
                // ?????????  ????????? len ?????????
            // ?????? &
            else if (repEnter.test(val) ){

                maxLen++ // ?????????????????? 1?????????
            }
            else{
                // ??????????????????????????????
                len++ // ?????? 1?????????
            }
        })
        return {
            len: Math.ceil( len ), // ???????????????
            leftLen: Math.floor(totalLen - len ), // ????????????????????? ????????? ???????????? ????????????
            maxLen: Math.ceil( maxLen ) , // ????????? textarea????????????
        }
    }
}



