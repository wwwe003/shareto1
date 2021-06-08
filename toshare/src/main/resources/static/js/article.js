$(function (){
    followState();
    likeState();
    favoriteState();

    isLogin();

    $(window).scroll(function() {
        if($(window).scrollTop()>=600){
            $(".toolbar").css('display','block');
        }else{
            $(".toolbar").css('display','none');
        }
    });
    $('.tool-item').click(function(){$('html,body').animate({scrollTop: '0px'}, 500);});

    $("#comment-area").on('focus','[name="content"]',function (){
        $(this).removeClass()
    }).on('click','.btn-send',function (){
        const content=$(this).siblings('[name="content"]').val().replace(/[ ]/g,"")
        if (commentValidate(this,content)){
            postData(this)

            // $(this).parent().parent('[name="comments"]').submit()
            //console.log($(this).parent().parent('[name="comments"]').attr("name"))
        } else {
            return false
        }
    }).on('click','.list-item .con .info .reply',function (){
        const cid=$(this).data("cid")
        // console.log(cid)
        const replyname=$(this).data("replyname")
        // console.log(username)

        $("[name='content']").attr("placeholder","@"+replyname).focus()
        $("[name='parent_id']").val(cid)
    }).on('click','.list-item .con .reply-box .info .reply',function (){
        const cid=$(this).data("cid")
        // console.log(cid)
        const pname=$(this).data("pname")
        const replyname=$(this).data("replyname")
        const pno=$(this).data("pno")
        // console.log(pname)

        $("[name='content']").attr("placeholder","@"+replyname).focus()
        $("[name='parent_id']").val(cid)
        $("[name='pname']").val(pname)
        $("[name='pno']").val(pno)
    })
});
function isLogin(){
    const account=$("#navbarNav2 .dropdown-item").text()
    if (account!==""){
        $("#comment-area .user-comment .comment-send").removeClass("no-login")
        $("#comment-area .user-comment .comment-send .baffle-wrap").empty()
        $("#comment-area .user-comment .comment-send button").removeAttr("disabled")
    } else {
        $("#comment-area .user-comment .baffle .btn-login-modal").click(function (){

            $("#loginModal").modal()
        })
    }
}
// function isLogin(){
//     if ( $("#comment-area .user-comment .comment-send button").hasClass("disabled")){
//     $.ajax({
//         url:"ifLogin",
//         type:"POST",
//         data:{method:"ifLogin"},
//         dataType:"json",
//         async:false,
//         cache:false,
//         success:function (data){
//             if (data==="login"){
//                 console.log(data)
//                 $("#comment-area .user-comment .comment-send").removeClass("no-login")
//                 $("#comment-area .user-comment .comment-send .baffle-wrap").empty()
//                 $("#comment-area .user-comment .comment-send button").removeAttr("disabled")
//             }
//         },
//         error:function (){
//             $("#comment-area .user-comment .baffle .btn-login-modal").click(function (){
//                 $("#loginModal").modal()
//             })
//         }
//     })
//     }
// }
function postData(obj) {
    $("#comment-list").load("/toshare/comments",{
        "author" : $(obj).siblings("[name='author']").val(),
        "authorno" : $(obj).siblings("[name='authorno']").val(),
        "username" : $(obj).siblings("[name='username']").val(),
        "userno" : $(obj).siblings("[name='userno']").val(),
        "parent_id" : $(obj).siblings("[name='parent_id']").val(),
        "pname" : $(obj).siblings("[name='pname']").val(),
        "pno" : $(obj).siblings("[name='pno']").val(),
        "post_id" : $(obj).siblings("[name='post_id']").val(),
        "content" : $(obj).siblings("[name='content']").val()
    },function (responseTxt, statusTxt, xhr) {
//        $(window).scrollTo($('#comment-container'),500);
        if (statusTxt==="error"){
            $("#loginModal").modal()
        }
        clearContent();
    });
}
function clearContent() {
    $("[name='content']").val('');
    $("[name='parent_id']").val();
    $("[name='content']").attr("placeholder", "comment...");
}
function commentValidate(obj,content) {
    // console.log(obj)
    // $("#titleError").remove()
    if (content.length===0){
        $(obj).siblings('[name="content"]').addClass("is-error")
        // $('[name="content"]').addClass("is-error")
        //$("#check-bar").after("<div id='titleError' class='form-group w-75 errorMessage'><span>* title is null</span></div>")
        return false
    }else {
        $('[name="content"]').removeClass("is-error")
        return true
    }
}
function followState(){
    const state=$("#userid").attr("name");
    const Num=$(".content-action").attr("id")
    let followNum=Num.substring(0,Num.indexOf("_"))

    const author=$(".article-content .article-meta span:eq(0)").text();
    const follower_uid=$("#userid").attr('value')

    if ($(".content-action").attr("name")===$(".content-action").attr("value")){
        $("#action-follow").hide()
        $("#action-followed").hide()
        if (parseInt(followNum)>1){
            followNum=calFollower(followNum);
        $(".user-name #follow-num").prepend("<b>"+followNum+"</b>"+" followers")
        }else {
            $(".user-name #follow-num").prepend("<b>"+followNum+"</b>"+" follower")
        }
    }else {
        followNum=calFollower(followNum);
        $(".user-name #follow").prepend(followNum+" ")
        $(".user-name #following").prepend(followNum+" ")
    //给未关注和已关注的样式都绑定点击函数
    $("#action-follow").click(function (){
        changeFollow(author,follower_uid);
    })
    $(".follow_dropdown li").click(function (){
        changeFollow(author,follower_uid);
    })

    if (state==="true"){  //followed
        $(".media-info-wrapper #action-followed").attr("class","user-subscribe-followed").show();//添加已关注样式
        $("#action-followed").hover(function(){//添加下拉菜单
            $(".followed").show();
        },function (){
            $(".followed").hide();
        })
        $(".media-info-wrapper #action-follow").hide();
    }else {
        $(".media-info-wrapper #action-follow").attr("class","user-subscribe-follow").show();//未关注样式
        $("#action-follow i").text("+");
        $(".media-info-wrapper #action-followed").hide();
    }
    }
}
function changeFollow(author,follower_uid){
    const update_time=new Date().getTime();
    const d =new Date(parseInt(update_time));
    const date = (d.getFullYear()) + "-" +
        (d.getMonth() + 1) + "-" +
        (d.getDate()) + " " +
        (d.getHours()) + ":" +
        (d.getMinutes()) + ":" +
        (d.getSeconds());
    $.ajax({
        url:"follow",
        type:"POST",
        data:{method:"followAuthor",author:author, follower_uid:follower_uid, update_time:date},
        dataType:"json",
        async:false,
        cache:false,
        success:function (data){
            let followerNum=calFollower(data.followerNum)
            if (data.currentState){
                $("#following").replaceWith(followerNum+" following")
                $("#action-followed").attr("class","user-subscribe-followed").show().hover(function(){
                    $(".followed").show();
                },function (){
                    $(".followed").hide();
                });
                $("#action-follow i").text("");
                $("#action-follow").hide();
            }else {
                $("#action-follow").attr("class","user-subscribe-follow").show().unbind('mouseenter mouseleave');
                $("#action-follow i").text("+");
                $("#action-followed").hide();
                $("#follow").replaceWith(followerNum+" follow")
            }
        },
        error:function (data){
            $("#loginModal").modal()
        }
    })
}

function likeState(){
    const likeState=$(".action-list input").attr("name");
    const Num=$(".content-action").attr("id")
    let likeNum=Num.substring(Num.indexOf("_")+1,Num.lastIndexOf("_"))
    likeNum=calFollower(likeNum);
    $(".action-item-like span").prepend(likeNum+" ")
    $(".action-item-liked span").prepend(likeNum+" ")
    $(".action-item-like").click(function (){
        changeLike();
    })
    $(".action-item-liked").click(function (){
        changeLike();
    })
    if (likeState==="true"){
        $(".content-action .action-item-like").hide()
        $(".content-action .action-item-liked").show()

    }else {
        $(".content-action .action-item-like").show()
        $(".content-action .action-item-liked").hide()
    }
}
function changeLike(){
    const like_postid=$(".action-list input").attr('id');
    const update_time=new Date().getTime();
    const like_uid=$(".action-list input").attr('value')

    const d =new Date(parseInt(update_time));
    const date = (d.getFullYear()) + "-" +
        (d.getMonth() + 1) + "-" +
        (d.getDate()) + " " +
        (d.getHours()) + ":" +
        (d.getMinutes()) + ":" +
        (d.getSeconds());
    $.ajax({
        url:"like",
        type:"POST",
        data:{method:"likePost",like_postid:like_postid, like_uid:like_uid, update_time:date},
        dataType:"json",
        async:false,
        cache:false,
        success:function (data){
            let likeNum=calFollower(data.likeNum)
            if (data.currentState){
                console.log("successLike")
                $(".action-item-liked span").replaceWith(likeNum+" like")
                $(".content-action .action-item-like").hide()
                $(".content-action .action-item-liked").show()
            }else {
                $(".action-item-like span").replaceWith(likeNum+" like")
                $(".content-action .action-item-like").show()
                $(".content-action .action-item-liked").hide()
            }
        },
        error:function (data){
            $("#loginModal").modal()
        }
    })
}
function favoriteState(){
    const favoriteState=$(".action-list input").attr("class");
    const Num=$(".content-action").attr("id")
    let favoriteNum=Num.substring(Num.lastIndexOf("_")+1)
    favoriteNum=calFollower(favoriteNum);
    $(".action-item-favorite span").prepend(favoriteNum+" ")
    $(".action-item-favorited span").prepend(favoriteNum+" ")
    $(".action-item-favorite").click(function (){
        changeFavorite();
    })
    $(".action-item-favorited").click(function (){
        changeFavorite();
    })

    if (favoriteState==="true"){
        $(".content-action .action-item-favorite").hide()
        $(".content-action .action-item-favorited").show()

    }else {
        $(".content-action .action-item-favorite").show()
        $(".content-action .action-item-favorited").hide()
    }
}
function changeFavorite(){
    const favorite_postid=$(".action-list input").attr('id');
    const update_time=new Date().getTime();
    const favorite_uid=$(".action-list input").attr('value')

    const d =new Date(parseInt(update_time));
    const date = (d.getFullYear()) + "-" +
        (d.getMonth() + 1) + "-" +
        (d.getDate()) + " " +
        (d.getHours()) + ":" +
        (d.getMinutes()) + ":" +
        (d.getSeconds());

    $.ajax({
        url:"favorite",
        type:"POST",
        data:{method:"favoritePost",favorite_postid:favorite_postid, favorite_uid:favorite_uid, update_time:date},
        dataType:"json",
        async:false,
        cache:false,
        success:function (result){
            let favoriteNum=calFollower(result.favoriteNum)
            if (result.currentState){
                console.log("successFavorite")
                $(".action-item-favorited span").replaceWith(favoriteNum+" favorite")
                $(".content-action .action-item-favorite").hide()
                $(".content-action .action-item-favorited").show()
            }else {
                $(".action-item-favorite span").replaceWith(favoriteNum+" favorite")
                $(".content-action .action-item-favorite").show()
                $(".content-action .action-item-favorited").hide()
            }
        },
        error:function (data){
            $("#loginModal").modal()
        }
    })
}
function calFollower(followNum){
    if (parseInt(followNum)>=1000){
        followNum=Math.floor(parseInt(followNum)/100)/10+"k"
        return followNum;
    }else {return followNum;}
}