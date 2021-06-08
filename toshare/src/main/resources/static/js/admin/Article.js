$(function (){
    $(".nav-item.top").click(function (){
        $(this).addClass("activeB").siblings().removeClass("activeB")
        $(this).addClass("active").siblings().removeClass("active")
    })
    $("#review").addClass("activeB").addClass("active")
    //back to top
    $(window).scroll(function() {
        if($(window).scrollTop()>=600){
            $(".toolbar").css('display','block');
        }else{
            $(".toolbar").css('display','none');
        }
    });
    $('.tool-item').click(function(){$('html,body').animate({scrollTop: '0px'}, 500);});
    //pass
    $('#btn-pass').click(function () {
        $('[name="pass"]').val(1);
        $('[name="delete"]').val(0);
        $('#form-post').submit()
    });
    //no pass
    $('#btn-no').click(function () {
        $('[name="pass"]').val(0);
        $('[name="delete"]').val(0);
        const mode="nopass-reason"
        if (validate(mode)){$('#form-post').submit()}
    });
    //delete
    $('#btn-delete').click(function () {
        $('[name="pass"]').val(0);
        $('[name="delete"]').val(1);
        const mode="delete-reason"
        if (validate(mode)){$('#form-post').submit()}
    });

    $('#btn-change').click(function () {
        const supertype=$("#supertype").text()
        const pid=$("#editModal #pid").attr("data-pid")
        const author=$("#author").text()
        const title=$("#title").text()
        changeType(supertype,pid,author,title,0)
    });
    cascadeMenu()

    $("#nopass-reason .nopass-reason").on("input propertychange", function() {
        let str = $(this).val()
        const countData=computedLen(str)
        $(this).attr({maxlength:countData.maxLen})
        $(".text-count").text(countData.leftLen)
        $(".text-max").text("/"+countData.maxLen)
    })
    $("#delete-reason .delete-reason").on("input propertychange", function() {
        let str = $(this).val()
        const countData=computedLen(str)
        $(this).attr({maxlength:countData.maxLen})
        $(".text-count").text(countData.leftLen)
        $(".text-max").text("/"+countData.maxLen)
    })
    $("#nopass-reason textarea").focus(function (){
        $(this).removeClass("is-error")
        const errorId="nopass-"+$(this).attr("name")+"Error"
        $("#"+errorId).remove()
    })
    $("#delete-reason textarea").focus(function (){
        $(this).removeClass("is-error")
        const errorId="delete-"+$(this).attr("name")+"Error"
        // console.log(errorId)
        $("#"+errorId).remove()
    })

})

function validate(mode){
    $("."+mode).removeClass("is-error")
    const reason=$("."+mode).val().replace(/[ ]/g,"")
    $("#reasonError").remove()
    if (reason.length===0){
        // $('[name="reason"]').addClass("is-error")
        $("."+mode).addClass("is-error")
        $("#"+mode).before("<div id='"+mode+"Error' class='form-group w-75 errorMessage'><span>* reason is null</span></div>")
        return false
    }else {
        return true
    }
}
function computedLen(str){
    const repOne = /[0-9a-zA-Z|\s]/;

    const repEnter = /[&]/;

    str = str.replace(/[\r\n]/g , '&')
    const totalLen = 60;
    let maxLen = 60;
    const strAttr = str.split('');
    let len = 0;
    if (strAttr.length === 0){
        return {
            len : len ,
            leftLen : totalLen ,
            maxLen: maxLen
        }
    }else{
        strAttr.forEach(function(val, key){
            if ( repOne.test(val) ) {
                len+=0.5
                maxLen+=0.5
            }

            else if (repEnter.test(val) ){

                maxLen++
            }
            else{

                len++
            }
        })
        return {
            len: Math.ceil( len ),
            leftLen: Math.floor(totalLen - len ),
            maxLen: Math.ceil( maxLen )
        }
    }
}