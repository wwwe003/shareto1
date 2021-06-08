$(function (){
    $(".nav-item.top").click(function (){
        $(this).addClass("activeB").siblings().removeClass("activeB")
        $(this).addClass("active").siblings().removeClass("active")
    })
    $("#review").addClass("activeB").addClass("active")
    $("#searchButton").click(function (){
        showSearchResult(1)
    })

    $(".sidebar-sticky ul li dl dd a").click(function (){
        $(".sidebar-sticky ul li .supertype").removeClass("active")
        $(".sidebar-sticky ul li dl dd a").removeClass("active")
        $(this).addClass("active").parents("li").children(".supertype").addClass("active")
        showAllBySubtype(1,$(this).attr('data-subtypeID'))
    })
    paginationManagePost()
    // $("#showTable").on('click','.change-button',function (){
    //     const supertypeID=$(this).parents("tr").find(".typeID").attr("data-supertypeID")
    //     const subtypeID=$(this).parents("tr").find(".typeID").attr("data-subtypeID")
    //     const supertype=$(this).parents("tr").find(".typeID").text()
    //     const pid=$(this).parents("tr").find(".td_title").attr("data-pid")
    //         // console.log(supertypeID)
    //         // console.log(subtypeID)
    //         // console.log(supertype)
    //         // console.log(pid)
    //     changeType(supertypeID,subtypeID,supertype,pid)
    // })
    // cascadeMenu()
    sidebarMenu()

})

function paginationManagePost(){
    const pageCode="1"
    const pageSize=$("#showPost").attr('data-PageSize');
    const totalRecords=$("#showPost").attr('data-totalRecord')
    pagination(pageCode,pageSize,totalRecords,showReviewResult);
    $("#all").addClass("active")
}
function showReviewResult(pageCode){
    $.ajax({
        url: "/toshare/admin/ajaxUnderReviewPosts",
        type: "POST",
        data: {method: "ajaxUnderReviewPosts", currentPage: pageCode},
        dataType: "json",
        async: true,
        cache: false,
        success: function (data) {
            if (data.beanList.length !== 0) {
                $("#noResult").remove()
                $("#showPost").empty()
                for (let i = 0; i < data.beanList.length; i++) {
                    const date = createTime(data.beanList[i].update_time)
                    const no = (data.pageCode - 1) * 3 + i + 1
                    $("#showPost").append("<tr><td>" + no + "</td>" +
                        "<td style='width: 220px' class='td_title' data-pid='" + data.beanList[i].post_id + "'>" + data.beanList[i].title + "</td>" +
                        "<td>" + data.beanList[i].author + "</td>" +
                        "<td class='typeID' data-supertypeID='" + data.beanList[i].type + "' data-subtypeID='" + data.beanList[i].type_second_id + "'>" + data.beanList[i].type_first_name + "(" + data.beanList[i].type_second_name + ")" + "</td>" +
                        "<td>" + date + "</td><td class='text-info'>under review</td><td>" +
                        // "<a style='margin-right: 3px' class='btn btn-sm btn-primary change-button'>Change type</a>" +
                        "<a style='margin-right: 3px' class='btn btn-sm btn-warning review-button' href='/toshare/admin/review/"+data.beanList[i].post_id+"'>Review</a></td></tr>")
                }
                $("#pagination .pagination").empty()
                pagination(data.pageCode, data.pageSize, data.totalRecord, showReviewResult);
            }else {
                $("#noResult").remove()
                $("#showPost").empty()
                $("#totalPost span").text("0")
                $("#showTable").after("<div id='noResult'><span>No result</span></div>")
                $("#pagination .pagination").empty()
            }
        }
    })
}
function sidebarMenu(){
    // $(".sidebar-sticky ul li:first img").attr("src","/toshare/images/caret-up-fill.svg")

    // $("#type").click(function () {
    //     const src = $(this).prev("img").attr("src")
    //     console.log(src)
    //     if (src === "/toshare/images/caret-up-fill.svg") {
    //         $(".nav-item.parent").slideUp();
    //         $(this).prev("img").attr("src", "/toshare/images/caret-down-fill.svg");
    //     } else {
    //         $(".nav-item.parent").slideDown();
    //         $(this).prev("img").attr("src", "/toshare/images/caret-up-fill.svg");
    //         console.log($(this).prev("img").attr("src"))
    //     }
    // })
    // $(".all_type img").click(function () {
    //     const src = $(this).attr("src")
    //     if (src === "/toshare/images/caret-up-fill.svg") {
    //         $(".nav-item.parent").slideUp();
    //         $(this).attr("src", "/toshare/images/caret-down-fill.svg");
    //     } else {
    //         $(".nav-item.parent").slideDown();
    //         $(this).attr("src", "/toshare/images/caret-up-fill.svg");
    //     }
    // })
    $(".sidebar-sticky ul li img").click(function () {
        const src = $(this).attr("src")
        if (src === "/toshare/images/caret-up-fill.svg") {
            $(this).siblings("dl").slideUp();
            $(this).attr("src", "/toshare/images/caret-down-fill.svg");
        } else {
            $(this).siblings("dl").slideDown();
            $(this).attr("src", "/toshare/images/caret-up-fill.svg");
        }
    })
    $(".sidebar-sticky ul li .supertype").click(function () {
        const src = $(this).prev("img").attr("src")
        if (src === "/toshare/images/caret-up-fill.svg") {
            $(this).next("dl").slideUp();
            $(this).prev("img").attr("src", "/toshare/images/caret-down-fill.svg");
        } else {
            $(this).next("dl").slideDown();
            $(this).prev("img").attr("src", "/toshare/images/caret-up-fill.svg");
        }
    })
}

function showAllBySubtype(pageCode,subtypeID){
    // console.log(subtypeID)
    $.ajax({
        url:"/toshare/admin/ajaxUnderReviewPosts",
        type:"POST",
        data:{method:"ajaxUnderReviewPosts", currentPage: pageCode,subtypeID:subtypeID},
        dataType:"json",
        async:true,
        cache:false,
        success:function (data){
            if (data.beanList.length!==0){
                $("#noResult").remove()
                $("#showPost").empty()
                for (let i=0;i<data.beanList.length;i++) {
                    const date=createTime(data.beanList[i].update_time)
                    const no=(data.pageCode-1)*2+i+1
                    $("#showPost").append("<tr><td>"+no+"</td>"+
                        "<td style='width: 220px' class='td_title' data-pid='"+data.beanList[i].post_id+"'>"+data.beanList[i].title+"</td>"+
                        "<td>"+data.beanList[i].author+"</td>"+
                        "<td class='typeID' data-supertypeID='"+data.beanList[i].type+"' data-subtypeID='"+data.beanList[i].type_second_id+"'>"+data.beanList[i].type_first_name+"("+data.beanList[i].type_second_name+")"+"</td>"+
                        "<td>"+date+"</td><td><td class='text-info'>under review</td>"+
                        // "<a style='margin-right: 3px' class='btn btn-sm btn-primary change-button'>Change type</a>"+
                        "<a style='margin-right: 3px' class='btn btn-sm btn-warning review-button' href='/toshare/admin/review/"+data.beanList[i].post_id+"'>Review</a></td></tr>")
                }
                $("#pagination .pagination").empty()
                pagination(data.pageCode.toString(),data.pageSize,data.totalRecord,showReviewResultBySubtype);
            }
        }
    })
}
function showReviewResultBySubtype(pageCode){
    const subtypeID=$("#showPost .typeID").attr('data-subtypeID')
    //console.log(subtypeID)
    $.ajax({
        url:"/toshare/admin/ajaxUnderReviewPosts",
        type:"POST",
        data:{method:"ajaxUnderReviewPosts", currentPage: pageCode,subtypeID:subtypeID},
        dataType:"json",
        async:true,
        cache:false,
        success:function (data){
            if (data.beanList.length!==0){
                $("#noResult").remove()
                $("#showPost").empty()
                for (let i=0;i<data.beanList.length;i++) {
                    const date=createTime(data.beanList[i].update_time)
                    const no=(data.pageCode-1)*2+i+1
                    $("#showPost").append("<tr><td>"+no+"</td>"+
                        "<td style='width: 220px' class='td_title' data-pid='"+data.beanList[i].post_id+"'>"+data.beanList[i].title+"</td>"+
                        "<td>"+data.beanList[i].author+"</td>"+
                        "<td class='typeID' data-supertypeID='"+data.beanList[i].type+"' data-subtypeID='"+data.beanList[i].type_second_id+"'>"+data.beanList[i].type_first_name+"("+data.beanList[i].type_second_name+")"+"</td>"+
                        "<td>"+date+"</td><td><td class='text-info'>under review</td>"+
                        // "<a style='margin-right: 3px' class='btn btn-sm btn-primary change-button'>Change type</a>"+
                        "<a style='margin-right: 3px' class='btn btn-sm btn-warning review-button' href='/toshare/admin/review/"+data.beanList[i].post_id+"'>Review</a></td></tr>")
                }
                $("#pagination .pagination").empty()
                pagination(data.pageCode.toString(),data.pageSize,data.totalRecord,showReviewResultBySubtype);
            }
        }
    })
}

