$(function (){
    changeTitle();
    ifFollow()
    showTimeline(1);

    $(".Tabs-milestone #follower").click(function (){
        $(".Tabs-item span").removeClass("active")
        $(".Tabs-milestone #follower").addClass("active").siblings().removeClass("active")
         showFollower(1);
    })
    $(".Tabs-milestone #totallikes").click(function (){
        $(".Tabs-item span").removeClass("active")
        $(".Tabs-milestone #totallikes").addClass("active").siblings().removeClass("active")
        showTotallikes(1);
        $(".follow-header #high").addClass("active")
    })
$(".Tabs-item #timeline").click(function (){
    $(".Tabs-item #timeline").addClass("active").siblings().removeClass("active")
    $(".Tabs-milestone span").removeClass("active")
    showTimeline(1);
}).addClass("active")
$(".Tabs-item #dynamic").click(function (){
    $(".Tabs-item #dynamic").addClass("active").siblings().removeClass("active")
    $(".Tabs-milestone span").removeClass("active")
    showDynamic(1);
})
$(".Tabs-item #favorite").click(function (){
    $(".Tabs-item #favorite").addClass("active").siblings().removeClass("active")
    $(".Tabs-milestone span").removeClass("active")
    showFavorite(1);
})
$(".Tabs-item #follow").click(function (){
    $(".Tabs-item #follow").addClass("active").siblings().removeClass("active")
    $(".Tabs-milestone span").removeClass("active")
    showFollow(1);
})
    $("#userList").on('click','.follow_dropdown li',function (){
        const author=$(this).parents(".user-info").find(".name").text()
        const follower_uid=$(".Tabs input").attr("id")
        // if (follower_uid===undefined){
        //     follower_uid==="null";
        // }

        let followState=followChange(author,follower_uid)
        if (followState===false){
            $(this).parents(".fans-action").empty().append(followE)
            // $(this).parents(".user-info").find("b").text()
        }
    }).on('click','.user-subscribe-follow',function (){
        const author=$(this).parents(".user-info").find(".name").text()
        const follower_uid=$(".Tabs input").attr("id")
        let followState= followChange(author,follower_uid)
        if (followState===true){
            $(this).parents(".fans-action").empty().append(followingE)
        }
    }).on('mouseenter','.user-subscribe-followed',function (){
        $(this).children(".followed").show();
    }).on('mouseleave','.user-subscribe-followed',function(){
        $(".followed").hide();
    }).on('click','#high',function (){
        showTotallikes(1);
        $(".follow-header #high").addClass("active")
        $(".follow-header #low").removeClass("active")
    }).on('click','#low',function (){
        showTotallikesLow(1);
        $(".follow-header #low").addClass("active")
        $(".follow-header #high").removeClass("active")
    })
});

const followingE= "<div class='user-subscribe-followed'><span>following</span>"
    +"<div class='followed'><ul class='follow_dropdown'><li>cancel</li></ul></div></div>";
const followE="<div class='user-subscribe-follow'><i class='bui-icon'>+&nbsp;</i><span>follow</span></div>";
let followerNum, currentState;
const AllFollowing="<div class='follow-header follow-header-info'><p>All Following</p></div>";
const AllFollower="<div class='follow-header follow-header-info'><p>All Follower</p></div>";
const Pagination="<div id='pagination'><nav aria-label='...' class='paginationNav'>"+
    "<ul class='pagination justify-content-center' id='pageUL'></ul></nav></div>"


function changeTitle(){
    const userno=$(".Tabs input").attr("value");
    const otherUserNo=location.href.substring(location.href.lastIndexOf("/")+1);
    if (userno!==otherUserNo){
        const pageName=$(".username").text()
        $(document).attr("title",pageName+"'s HomePage")
    }else {
        $(document).attr("title","My HomePage")
    }
}
function ifFollow(){
    const userno=$(".Tabs input").attr("value");
    const otherUserNo=location.href.substring(location.href.lastIndexOf("/")+1);
    const username=$(".Tabs input").attr("name")
    const postButton="<div class='postButton'><a href='/toshare/"+username+"/myPost' target='_blank'>manage post</a></div>"
    if (userno!==otherUserNo){
        const followState=$(".Tabs .followState").attr("id");
        const followNum=$(".Tabs .followState").attr("name");
        $("#followPageUser").on('click','.follow_dropdown li',function (){
            const author=$("#username").text()
            const follower_uid=$(".Tabs input").attr("id")
            let followState=followChange(author,follower_uid)
            if (followState===false){
                $("#followPageUser").empty().append(followE)
            }
        }).on('click','.user-subscribe-follow',function (){
            const author=$("#username").text()
            const follower_uid=$(".Tabs input").attr("id")
            let followState= followChange(author,follower_uid)
            console.log(followState)
            if (followState===true){
                $("#followPageUser").empty().append(followingE)
            }
        }).on('mouseenter','.user-subscribe-followed',function (){
            $(this).children(".followed").show();
        }).on('mouseleave','.user-subscribe-followed',function(){
            $(".followed").hide();
        })
        if (followState==="0"){
            $("#followPageUser").append(followE)
        }else {
            $("#followPageUser").append(followingE)
        }
    }else{
        $("#followPageUser").append(postButton)
    }
}
function showTotallikes(pageCode){
    $("#userList").empty()
    const userno=$(".Tabs input").attr("value");
    const otherUserNo=location.href.substring(location.href.lastIndexOf("/")+1);
    $.ajax({
        url: "totallikes",
        type: "POST",
        data: {method: "showTotallikes", currentPage: pageCode, userno: otherUserNo},
        dataType: "json",
        async: false,
        cache: false,
        success: function (likeResult) {
            if (likeResult != null) {
                for (let i = 0; i < likeResult.beanList.length; i++) {
                    const date=createTime(likeResult.beanList[i].update_time)
                    const like= calFollower(likeResult.beanList[i].likes)
                    const e="<div class='favoritePost'><div class='post_title' ><a href='/toshare/"+likeResult.beanList[i].post_id+"'>" +
                        "<h4>"+likeResult.beanList[i].title+"</h4></a><p>"+likeResult.beanList[i].description+"</p></div><div class='post_footer'>" +
                        "<div class='user-avatar-small'><a href='/toshare/user/"+likeResult.beanList[i].userno+"' target='_blank'><img src='/toshare/images/3342271.svg' alt='avatar'></a></div>"+
                        "<a href='/toshare/user/"+likeResult.beanList[i].userno+"' target='_blank'>"+likeResult.beanList[i].author+"</a><span class='postDate'>post in:&nbsp;"+date+"</span><span class='like'>"+like+"&nbsplike</span></div></div>"
                    $("#userList").append(e)
                }
               // $("#userList").prepend("<div class='follow-header order'><p>high</p><p>low</p></div>")
                if (userno===otherUserNo){
                    $("#userList").prepend("<div class='follow-header follow-header-info'><div class='title'><p>My post has received like</p></div><div class='follow-header order'><p id='high'>high</p><p id='low'>low</p></div></div>").append(Pagination)
                }else {
                    const pageName=$(".username").text()
                    $("#userList").prepend("<div class='follow-header follow-header-info'><div class='title'><p>"+pageName+"'s post has received like"+"</p></div><div class='follow-header order'><p id='high'>high</p><p id='low'>low</p></div></div>").append(Pagination)
                }
                pagination(likeResult.pageCode, likeResult.pageSize, likeResult.totalRecord, showTotallikes)
            }
        }
    });
}
function showTotallikesLow(pageCode){
    $("#userList").empty()
    const userno=$(".Tabs input").attr("value");
    const otherUserNo=location.href.substring(location.href.lastIndexOf("/")+1);
    $.ajax({
        url: "totallikesLow",
        type: "POST",
        data: {method: "showTotallikesLow", currentPage: pageCode, userno: otherUserNo},
        dataType: "json",
        async: false,
        cache: false,
        success: function (likeResult) {
            if (likeResult != null) {
                for (let i = 0; i < likeResult.beanList.length; i++) {
                    const date=createTime(likeResult.beanList[i].update_time)
                    const like= calFollower(likeResult.beanList[i].likes)
                    const e="<div class='favoritePost'><div class='post_title' ><a href='/toshare/"+likeResult.beanList[i].post_id+"'>" +
                        "<h4>"+likeResult.beanList[i].title+"</h4></a><p>"+likeResult.beanList[i].description+"</p></div><div class='post_footer'>" +
                        "<div class='user-avatar-small'><a href='/toshare/user/"+likeResult.beanList[i].userno+"' target='_blank'><img src='/toshare/images/3342271.svg' alt='avatar'></a></div>"+
                        "<a href='/toshare/user/"+likeResult.beanList[i].userno+"' target='_blank'>"+likeResult.beanList[i].author+"</a><span class='postDate'>post in:&nbsp;"+date+"</span><span class='like'>"+like+"&nbsplike</span></div></div>"
                    $("#userList").append(e)
                }
                if (userno===otherUserNo){
                    $("#userList").prepend("<div class='follow-header follow-header-info'><div class='title'><p>My post has received like</p></div><div class='follow-header order'><p id='high'>high</p><p id='low'>low</p></div></div>").append(Pagination)
                }else {
                    const pageName=$(".username").text()
                    $("#userList").prepend("<div class='follow-header follow-header-info'><div class='title'><p>"+pageName+"'s post has received like"+"</p></div><div class='follow-header order'><p id='high'>high</p><p id='low'>low</p></div></div>").append(Pagination)
                }
                pagination(likeResult.pageCode, likeResult.pageSize, likeResult.totalRecord, showTotallikesLow)
            }
        }
    });
}
function showFollower(pageCode){
    $("#userList").empty()
    const userno=$(".Tabs input").attr("value");
    const otherUserNo=location.href.substring(location.href.lastIndexOf("/")+1);
        $.ajax({
            url:"follower",
            type:"POST",
            data:{method:"showFollower",currentPage:pageCode,userno:otherUserNo},
            dataType:"json",
            async:true,
            cache:false,
            success:function (result){
                if (result!=null){
                    for (let i=0;i<result.beanList.length;i++){
                        let followerNum=result.beanList[i].followernum;
                        let userNO=result.beanList[i].userno;
                        followerNum=calFollower(followerNum);
                        const e="<div class='user-info'><a href='/toshare/user/"+userNO+"' class='user-avatar' target='_blank'><img src='/toshare/images/3342271.svg' style='max-width: 70px;max-height: 70px' alt='avatar'></a>"+
                            "<div class='user-name'><a href='/toshare/user/"+userNO+"' class='name' target='_blank'>"+result.beanList[i].username+"</a>" +
                            "<div><span><b>"+followerNum+"</b> following</span></div></div>"+
                            "<div class='fans-action' id="+result.beanList[i].username+">"+followE+"</div></div>"
                        $("#userList").append(e)
                        if (result.followingUsername!==null){
                            for (let j=0;j<result.followingUsername.length;j++){
                                if (result.beanList[i].username===result.followingUsername[j]){
                                    $("#"+result.beanList[i].username).empty().append(followingE)
                                }if (result.beanList[i].username===$(".Tabs input").attr('name')){
                                    $("#"+result.beanList[i].username).empty()
                                }
                            }
                        }
                    }
                    $("#userList").prepend(AllFollower).append(Pagination)
                    pagination(result.pageCode,result.pageSize,result.totalRecord,showFollower)
                }
            }
        })
}
function showTimeline(pageCode){
    $("#userList").empty()
    const userno=$(".Tabs input").attr("value");
    const otherUserNo=location.href.substring(location.href.lastIndexOf("/")+1);
    $.ajax({
        url: "timeline",
        type: "POST",
        data: {method: "showTimeline", currentPage: pageCode, userno: otherUserNo},
        dataType: "json",
        async: false,
        cache: false,
        success: function (timeResult) {
            if (timeResult != null) {
                for (let i = 0; i < timeResult.beanList.length; i++) {
                    const date=createTime(timeResult.beanList[i].update_time)
                    const e="<div class='favoritePost'><div class='post_title' ><a href='/toshare/"+timeResult.beanList[i].post_id+"'>" +
                        "<h4>"+timeResult.beanList[i].title+"</h4></a><p>"+timeResult.beanList[i].description+"</p></div><div class='post_footer'>" +
                        "<div class='user-avatar-small'><a href='/toshare/user/"+timeResult.beanList[i].userno+"' target='_blank'><img src='/toshare/images/3342271.svg' alt='avatar'></a></div>"+
                        "<a href='/toshare/user/"+timeResult.beanList[i].userno+"' target='_blank'>"+timeResult.beanList[i].author+"</a><span>post in:&nbsp;"+date+"</span></div></div>"
                    $("#userList").append(e)
                }
                if (userno===otherUserNo){
                    $("#userList").prepend("<div class='follow-header follow-header-info'><p>My post</p></div>").append(Pagination)
                }else {
                    const pageName=$(".username").text()
                    $("#userList").prepend("<div class='follow-header follow-header-info'><p>"+pageName+"'s post"+"</p></div>").append(Pagination)
                }
                pagination(timeResult.pageCode, timeResult.pageSize, timeResult.totalRecord, showTimeline)
            }
        }
    });
}
function showDynamic(pageCode){
    $("#userList").empty()
    const otherUserNo=location.href.substring(location.href.lastIndexOf("/")+1);
    $.ajax({
        url: "dynamic",
        type: "POST",
        data: {method: "showDynamic", currentPage: pageCode, userno: otherUserNo},
        dataType: "json",
        async: false,
        cache: false,
        success: function (dynaResult) {
            if (dynaResult != null) {
                for (let i = 0; i < dynaResult.beanList.length; i++) {
                    const date=createTime(dynaResult.beanList[i].update_time)
                    const e="<div class='favoritePost'><div class='post_title' ><a href='/toshare/"+dynaResult.beanList[i].post_id+"'>" +
                        "<h4>"+dynaResult.beanList[i].title+"</h4></a><p>"+dynaResult.beanList[i].description+"</p></div><div class='post_footer'>" +
                        "<div class='user-avatar-small'><a href='/toshare/user/"+dynaResult.beanList[i].userno+"' target='_blank'><img src='/toshare/images/3342271.svg' alt='avatar'></a></div>"+
                        "<a href='/toshare/user/"+dynaResult.beanList[i].userno+"' target='_blank'>"+dynaResult.beanList[i].author+"</a><span>post in:&nbsp;"+date+"</span></div></div>"
                    $("#userList").append(e)
                }
                $("#userList").append(Pagination)
                pagination(dynaResult.pageCode, dynaResult.pageSize, dynaResult.totalRecord, showDynamic)
            }
        }
    });
}
function showFavorite(pageCode) {
    $("#userList").empty()
    const userno = $(".Tabs input").attr("value");
    const otherUserNo = location.href.substring(location.href.lastIndexOf("/") + 1);
        $.ajax({
            url: "favorite",
            type: "POST",
            data: {method: "showFavorite", currentPage: pageCode, userno: otherUserNo},
            dataType: "json",
            async: true,
            cache: false,
            success: function (favResult) {
                if (favResult != null) {
                    for (let i = 0; i < favResult.beanList.length; i++) {
                        const date = createTime(favResult.beanList[i].update_time)
                        const e = "<div class='favoritePost'><div class='post_title' ><a href='/toshare/" + favResult.beanList[i].post_id + "'>" +
                            "<h4>" + favResult.beanList[i].title + "</h4></a><p>" + favResult.beanList[i].description + "</p></div><div class='post_footer'>" +
                            "<a href='/toshare/user/" + favResult.beanList[i].userno + "' target='_blank'>" + favResult.beanList[i].author + "</a><span>favorite in:&nbsp;" + date + "</span></div></div>"
                        $("#userList").append(e)
                    }
                    if (userno === otherUserNo) {
                        $("#userList").prepend("<div class='follow-header follow-header-info'><p>My favorite</p></div>").append(Pagination)
                    } else {
                        const pageName = $(".username").text()
                        $("#userList").prepend("<div class='follow-header follow-header-info'><p>" + pageName + "'s favorite" + "</p></div>").append(Pagination)
                    }
                    pagination(favResult.pageCode, favResult.pageSize, favResult.totalRecord, showFavorite)
                }
            }
        });
}
function showFollow(pageCode){

    $("#userList").empty()
    const userno=$(".Tabs input").attr("value");
    const otherUserNo=location.href.substring(location.href.lastIndexOf("/")+1);
    if (userno===otherUserNo){
        $.ajax({
        url:"following",
        type:"POST",
        data:{method:"showFollow",currentPage:pageCode,userno:userno},
        dataType:"json",
        async:true,
        cache:false,
        success:function (result){
            for (let i = 0; i < result.beanList.length; i++) {
                let followerNum = result.beanList[i].followernum;
                let userNO = result.beanList[i].userno;
                followerNum = calFollower(followerNum);

                const e = "<div class='user-info'><a href='/toshare/user/" + userNO + "' class='user-avatar' target='_blank'><img src='/toshare/images/3342271.svg' alt='avatar'></a>" +
                    "<div class='user-name'><a href='/toshare/user/" + userNO + "' class='name' target='_blank'>" + result.beanList[i].username + "</a>" +
                    "<div><span><b>" + followerNum + "</b> following</span></div></div>" +
                    "<div class='fans-action'>" + followingE + "</div></div>"
                $("#userList").append(e)
            }
            $("#userList").prepend(AllFollowing).append(Pagination)
            pagination(result.pageCode, result.pageSize, result.totalRecord, showFollow)
        }
        })
    }else{
        $.ajax({
            url:"following",
            type:"POST",
            data:{method:"showFollow",currentPage:pageCode,userno:otherUserNo},
            dataType:"json",
            async:true,
            cache:false,
            success:function (result){
                if (result!=null){
                    for (let i=0;i<result.beanList.length;i++){
                        let followerNum=result.beanList[i].followernum;
                        let userNO=result.beanList[i].userno;
                        followerNum=calFollower(followerNum);

                        const e="<div class='user-info'><a href='/toshare/user/"+userNO+"' class='user-avatar' target='_blank'><img src='/toshare/images/3342271.svg' style='max-width: 70px;max-height: 70px' alt='avatar'></a>"+
                            "<div class='user-name'><a href='/toshare/user/"+userNO+"' class='name' target='_blank'>"+result.beanList[i].username+"</a>" +
                            "<div><span><b>"+followerNum+"</b> following</span></div></div>"+
                            "<div class='fans-action' id="+result.beanList[i].username+">"+followE+"</div></div>"
                        $("#userList").append(e)
                        if (result.followingUsername!==null){
                        for (let j=0;j<result.followingUsername.length;j++){
                            if (result.beanList[i].username===result.followingUsername[j]){
                                $("#"+result.beanList[i].username).empty().append(followingE)
                            }if (result.beanList[i].username===$(".Tabs input").attr('name')){
                                $("#"+result.beanList[i].username).empty()
                            }
                        }
                        }
                    }
                    $("#userList").prepend(AllFollowing).append(Pagination)
                    pagination(result.pageCode,result.pageSize,result.totalRecord,showFollow)
                }
            }
        })
    }
}
function followChange(author,follower_uid){
    const update_time=new Date().getTime();
    const d =new Date(parseInt(update_time));
    const date = (d.getFullYear()) + "-" +
        (d.getMonth() + 1) + "-" +
        (d.getDate()) + " " +
        (d.getHours()) + ":" +
        (d.getMinutes()) + ":" +
        (d.getSeconds());

    $.ajax({
        url:"/toshare/follow",
        type:"POST",
        data:{method:"followAuthor",author:author, follower_uid:follower_uid, update_time:date},
        dataType:"json",
        async:false,
        cache:false,
        success:function (data){
            followerNum=calFollower(data.followerNum)
            currentState = data.currentState
        },error:function (data){
            $("#loginModal").modal()
        }
    })
    return currentState;
}

function pagination(pageCode, pageSize, totalRecords, functionType){

    let beginPage, endPage;
    const totalPages = Math.ceil(totalRecords / pageSize);
    beginPage = parseInt(pageCode) - 2;
    endPage = parseInt(pageCode) + 2;

    if (pageCode !== undefined && pageSize !== undefined && totalRecords !== undefined) {
        if (totalPages <= 5) {
            beginPage = 1;
            endPage = totalPages;
        } else {
            if (beginPage < 1) {
                beginPage = 1;
                endPage = 5;
            }
            if (endPage > totalPages) {
                beginPage = pageCode - 4;
                beginPage+=endPage-totalPages;
                endPage = totalPages;
            }
        }
        if(beginPage!==endPage&&endPage!==0){
            if (pageCode === 1) {
                //=1, 首页高亮，previous灰
                const itemFirst="<li class='page-item active'><span class='page-link'>First<span class='sr-only'>(current)</span></span></li>"
                const itemPrevious="<li class='page-item disabled'><span class='page-link'>&lt;Previous</span></li>"
                $("#pageUL").append(itemFirst).append(itemPrevious);
            } else {
                //当前页不为1时，设置 首页,Previous 点击函数
                const itemFirst="<li class='page-item addCursor' id='first'><a class='page-link'>First</a></li>"
                const itemPrevious="<li class='page-item addCursor' id='previous'><a class='page-link'>&lt;Previous</a></li>"
                $("#pageUL").append(itemFirst).append(itemPrevious);
                $("#first").click(function (){
                    functionType(1)
                })
                $("#previous").click(function (){
                    functionType(pageCode-1)
                })
            }
            for (let i = beginPage; i <= endPage; i++) {
                    if (i === pageCode) {
                        //设置当前页高亮
                        $("#pageUL").append("<li class='page-item active'><span class='page-link' >" + i + "<span class='sr-only'>(current)</span></span></li>");
                    } else {
                        $("#pageUL").append("<li class='page-item addCursor' id="+"page"+i+"><a class='page-link'>" + i + "</a></li>");
                        $("#page"+i).click(function (){
                            functionType(i)
                        })
                    }
            }
            if (pageCode === totalPages) {
                //当前页码为最大页码时，把尾页设置成高亮，下一页设置成不高亮
                $("#pageUL").append("<li class='page-item disabled'><span class='page-link'>Next&gt;</span></li>").append("<li class='page-item active'><span class='page-link'>Last<span class='sr-only'>(current)</span></span></li>");
            }else {
                //当前页码不为最大页码时，把尾页，下一页设置成超链接
                $("#pageUL").append("<li class='page-item' id='next'><a class='page-link addCursor'>Next&gt;</a></li>").append("<li class='page-item' id='last'><a class='page-link addCursor'>Last</a></li>");
                $("#next").click(function (){
                    functionType(pageCode+1)
                })
                $("#last").click(function (){
                    functionType(totalPages)
                })
            }
        }
    }
}
function calFollower(followNum){
    if (parseInt(followNum)>=1000){
        followNum=Math.floor(parseInt(followNum)/100)/10+"k"
        return followNum;
    }else {return followNum;}
}
function createTime(v){
    const date = new Date(v);
    const y = date.getFullYear();
    let m = date.getMonth() + 1;
    m = m<10?("0"+m):m;
    let d = date.getDate();
    d = d<10?("0"+d):d;
    let h = date.getHours();
    h = h<10?("0"+h):h;
    let M = date.getMinutes();
    M = M<10?("0"+M):M;
    let s = date.getSeconds();
    s = s<10?("0"+s) : s;
    return y + "-" + m + "-" + d;
}