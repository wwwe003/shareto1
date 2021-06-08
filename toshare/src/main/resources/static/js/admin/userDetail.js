$(function (){
    $(".card-body a").click(function (){
        $(this).addClass("active").siblings().removeClass("active")
    })
    // $(".nav-item.top").click(function () {
    //     $(this).addClass("activeB").siblings().removeClass("activeB")
    //     $(this).addClass("active").siblings().removeClass("active")
    // })

    $(".card-body .ajax-review").click(function (){
        const pageCode=1
        showReviewArticle(pageCode)
    })
    $(".card-body .ajax-fav").click(function (){
        const pageCode=1
        showFavArticle(pageCode)
    })
    $("#user").addClass("activeB").addClass("active")

    post_Pagination()
})
function showReviewArticle(pageCode){
    const usernoStr=$(".card #userno").text()
    const uno=usernoStr.substring(usernoStr.indexOf(":")+2)
    $.ajax({
        url:"/toshare/admin/user/review",
        type:"POST",
        data:{method:"reviewArticles", currentPage: pageCode,uno:uno},
        dataType:"json",
        async:true,
        cache:false,
        success:function (data){
            $(".postedArticles.noresult").remove()
            $("#postedArticles").empty()
            $("#postList .post-header p").text("Under review articles")
            if (data.beanList.length!==0){
                for (let i=0;i<data.beanList.length;i++) {
                    const date = createTime(data.beanList[i].update_time)
                    $("#postedArticles").append("<div class='postedArticles'>"+
                        "<div class='_title'><a href='/toshare/" + data.beanList[i].post_id + "' target='_blank'>"+
                        "<p>" + data.beanList[i].title+"</p></a></div><div class='_footer'><span class='postDate'>posted in: " + date + "</span>&nbsp" +
                        "<span class='like'>favorite:&nbsp"+data.beanList[i].favorites+"</span>&nbsp" +
                        "<span class='like'>like:&nbsp"+data.beanList[i].likes+"</span></div></div>")
                }
                $("#pagination .pagination").empty()
                pagination(data.pageCode,data.pageSize,data.totalRecord,showReviewArticle);
            }else {
                $("#postList .post-header").after("<div class='postedArticles noresult'><p> No result </p></div>")
                $("#pagination .pagination").empty()
            }
        }
    })
}

function showFavArticle(pageCode){
    const usernoStr=$(".card #userno").text()
    const uno=usernoStr.substring(usernoStr.indexOf(":")+2)
    $.ajax({
        url:"/toshare/admin/user/favorite",
        type:"POST",
        data:{method:"favoriteArticles", currentPage: pageCode,uno:uno},
        dataType:"json",
        async:true,
        cache:false,
        success:function (favPost){
            $("#postList .post-header p").text("Favorite articles")
            $("#postedArticles").empty()
            $(".postedArticles.noresult").remove()
            if (favPost.beanList.length!==0){
                for (let i=0;i<favPost.beanList.length;i++) {
                    const date = createTime(favPost.beanList[i].update_time)
                    const no=favPost.beanList[i].userno
                    $("#postedArticles").append("<div class='postedArticles'>"+
                        "<div class='_title'><a href='/toshare/" + favPost.beanList[i].post_id +"' target='_blank'>"+
                        "<p>"+favPost.beanList[i].title+"</p></a></div><div class='_footer'><a target='_blank' href='/toshare/admin/detail/"+no+"'>"+favPost.beanList[i].author+"</a>&nbsp<span class='postDate'>posted in: " + date + "</span>&nbsp" +
                        "<span class='like'>favorite:&nbsp"+favPost.beanList[i].favorites+"</span>&nbsp" +
                        "<span class='like'>like:&nbsp"+favPost.beanList[i].likes+"</span></div></div>")
                }
                $("#pagination .pagination").empty()
                pagination(favPost.pageCode,favPost.pageSize,favPost.totalRecord,showFavArticle);
            }else {
                $("#postList .post-header").after("<div class='postedArticles noresult'><p> No result </p></div>")
                $("#pagination .pagination").empty()
            }
        }
    })
}

function post_Pagination(){
    const pageCode=parseInt($(".card-body input").attr('data-pageCode'));
    const pageSize=parseInt($(".card-body input").attr('data-pageSize'));
    const totalRecord=parseInt($(".card-body input").attr('data-totalRecord'));
    const usernoStr=$(".card #userno").text()
    const uno=usernoStr.substring(usernoStr.indexOf(":")+2)

    let begin, end;
    const totalPages = Math.ceil(totalRecord / pageSize);
    const pageNext = parseInt(pageCode) + 1;
    const pagePrevious = parseInt(pageCode) - 1;
    begin = parseInt(pageCode) - 2;
    end = parseInt(pageCode) + 2;

    if (pageCode !== undefined && pageSize !== undefined && totalRecord !== undefined) {
        if (totalPages <= 5) {
            begin = 1;
            end = totalPages;
        } else {
            if (begin < 1) {
                begin = 1;
                end = 5;
            }
            if (end > totalPages) {
                begin = pageCode - 4;
                begin+=end-totalPages;
                end = totalPages;
            }
        }
        if(begin!==end&&end!==0){
            if (pageCode === 1) {
                $("#pageUL").append("<li class='page-item active'><span class='page-link'>First<span class='sr-only'>(current)</span></span></li>")
                    .append("<li class='page-item disabled'><span class='page-link'>&lt;Previous</span></li>");
            } else {
                $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/admin/detail/"+uno+"?currentPage=" + 1 + "'>First</a></li>")
                    .append("<li class='page-item'><a class='page-link' href='/toshare/admin/detail/"+uno+"?currentPage=" + pagePrevious + "'>&lt;Previous</a></li>");
            }
            for (let i = begin; i <= end; i++) {
                if (i === pageCode) {
                    $("#pageUL").append("<li class='page-item active'><span class='page-link' >" + i + "<span class='sr-only'>(current)</span></span></li>")
                } else {
                    $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/admin/detail/"+uno+"?currentPage=" + i + "'>" + i + "</a></li>");
                }
            }
            if (pageCode === totalPages) {
                $("#pageUL").append("<li class='page-item disabled'><span class='page-link'>Next&gt;</span></li>")
                    .append("<li class='page-item active'><span class='page-link'>Last<span class='sr-only'>(current)</span></span></li>");
            } else {
                $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/admin/detail/"+uno+"?currentPage=" + pageNext + "'>Next&gt;</a></li>")
                    .append("<li class='page-item'><a class='page-link' href='/toshare/admin/detail/"+uno+"?currentPage=" + totalPages + "'>Last</a></li>");
            }
        }
    }
}