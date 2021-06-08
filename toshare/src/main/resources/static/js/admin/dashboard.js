$(function(){
    $(".nav-item.top").click(function (){
        $(this).addClass("activeB").siblings().removeClass("activeB")
        $(this).addClass("active").siblings().removeClass("active")
    })
    $("#home").addClass("activeB").addClass("active")
    $(".nav-item.parent").click(function (){
        $(this).addClass("active").siblings().removeClass("active")
        $(this).children("a").addClass("active")
        $(this).siblings().children("a").removeClass("active")
    })
    $("#allType").addClass("active")
    $("#allType a").addClass("active")

    sidebarMenu()
    // $("#save-edit").click(function (){
    //     const name=$("#editModal #New-type").val()
    //     validateNewTypeName(name)
    // })
    $("#searchButton").click(function (){
        showResult(1)
    })

    $("#main").on('click','.change-button',function (){
        const supertypeID=$(this).parents("tr").find(".typeID").attr("data-supertypeID")
        const subtypeID=$(this).parents("tr").find(".typeID").attr("data-subtypeID")
        const supertype=$(this).parents("tr").find(".typeID").text()
        const pid=$(this).parents("tr").find(".td_title").attr("data-pid")
        console.log(supertypeID)
        console.log(subtypeID)
        console.log(supertype)
        console.log(pid)
        changeType(supertypeID,subtypeID,supertype,pid)
    })
    cascadeMenu()
})
// function sidebarMenu(){
//     $("#allArticle").click(function (){
//         const src = $("#caret").attr("src");
//         if(src === "/toshare/images/caret-up-fill.svg") {
//             $(".nav-item.children:visible").slideUp();
//             $("#caret").attr("src", "/toshare/images/caret-down-fill.svg");
//         } else {
//             $(".nav-item.children:hidden").slideDown();
//             $("#caret").attr("src", "/toshare/images/caret-up-fill.svg");
//         }
//     })
// }
function sidebarMenu(){
    $("#addType").click(function (){
        const src = $("#caret").attr("src");
        if(src === "/toshare/images/caret-down-fill.svg") {
            $("#caret").attr("src", "/toshare/images/caret-up-fill.svg");
        } else {
            $("#caret").attr("src", "/toshare/images/caret-down-fill.svg");
        }
    })
}
function validateNewTypeName(val){
    const name=val.replace(/[ ]/g,"")
    if (name.length===0){

    }
}
function showResult(pageCode){
    const limit=$("#inputState").val()
    const keyword=$("#keyword").val().replace(/[ ]/g,"")
    if (keyword.length!==0){
        $.ajax({
            url: "/toshare/searchPG",
            type: "POST",
            data: {method: "searchPG", currentPage: pageCode, keyword: keyword, limit: limit},
            dataType: "json",
            async: true,
            cache: false,
            success: function (data) {
                if (data.beanList.length !== 0) {
                    $("#noResult").remove()
                    $("#main .showTable").remove()
                    $("#showPost").empty()
                    $("#showTable").show()
                    for (let i = 0; i < data.beanList.length; i++) {
                        const date = createTime(data.beanList[i].update_time)
                        const no = (data.pageCode - 1) * 3 + i + 1
                        $("#showPost").append("<tr><td>" + no + "</td>" +
                            "<td style='width: 220px' class='td_title' data-pid='" + data.beanList[i].post_id + "'>" + data.beanList[i].title + "</td>" +
                            "<td>" + data.beanList[i].author + "</td>" +
                            "<td class='typeID' data-supertypeID='" + data.beanList[i].type + "' data-subtypeID='" + data.beanList[i].type_second_id + "'>" + data.beanList[i].type_first_name + "(" + data.beanList[i].type_second_name + ")" + "</td>" +
                            "<td>" + date + "</td><td>" +
                            "<a style='margin-right: 3px' class='btn btn-sm btn-primary change-button'>Change type</a>" +
                            "<a style='margin-right: 3px' class='btn btn-sm btn-warning review-button' href='#'>Review</a>" +
                            "<a class='btn btn-sm btn-danger deleteBtn delete-button' href='#'>Delete</a></td></tr>")
                    }
                    $("#pagination .pagination").empty()
                    pagination(data.pageCode, data.pageSize, data.totalRecord, showResult);
                }else {
                    $("#noResult").remove()
                    $("#main .showTable").remove()
                    $("#main #showPost").empty()
                    $("#totalPost span").text("0")
                    $("#showTable").after("<div id='noResult'><span>No result</span></div>")
                    $("#pagination .pagination").empty()
                }
            }
        })
    }
}
// function createTime(v){
//     const date = new Date(v);
//     const y = date.getFullYear();
//     let m = date.getMonth() + 1;
//     m = m<10?("0"+m):m;
//     let d = date.getDate();
//     d = d<10?("0"+d):d;
//     let h = date.getHours();
//     h = h<10?("0"+h):h;
//     let M = date.getMinutes();
//     M = M<10?("0"+M):M;
//     let s = date.getSeconds();
//     s = s<10?("0"+s) : s;
//     return y + "-" + m + "-" + d+" "+h+":"+M+":"+s;
// }
// function _pagination(pageCode,pageSize,totalRecords,funcType) {
//     pageCode=parseInt(pageCode);
//     let begin, end;
//     const totalPages = Math.ceil(totalRecords / pageSize);
//     begin = parseInt(pageCode) - 2;
//     end = parseInt(pageCode) + 2;
//
//     if (pageCode !== undefined && pageSize !== undefined && totalRecords !== undefined) {
//         if (totalPages <= 5) {
//             begin = 1;
//             end = totalPages;
//         } else {
//             if (begin < 1) {
//                 begin = 1;
//                 end = 5;
//             }
//             if (end > totalPages) {
//                 begin = pageCode - 4;
//                 begin+=end-totalPages;
//                 end = totalPages;
//             }
//         }
//         if(begin!==end&&end!==0){
//             if (pageCode === 1) {
//                 $("#pageUL").append("<li class='page-item active'><span class='page-link'>First<span class='sr-only'>(current)</span></span></li>").append("<li class='page-item disabled'><span class='page-link'>&lt;Previous</span></li>");
//             } else {
//                 $("#pageUL").append("<li class='page-item addCursor' id='first'><a class='page-link'>First</a></li>").append("<li class='page-item addCursor' id='previous'><a class='page-link'>&lt;Previous</a></li>");
//                 $("#first").click(function (){
//                     funcType(1)
//                 })
//                 $("#previous").click(function (){
//                     funcType(pageCode-1)
//                 })
//             }
//             for (let i = begin; i <= end; i++) {
//                 if (i=== pageCode) {
//                     $("#pageUL").append("<li class='page-item active'><span class='page-link' >" + i + "<span class='sr-only'>(current)</span></span></li>");
//                 } else {
//                     $("#pageUL").append("<li class='page-item addCursor' id="+"page"+i+"><a class='page-link'>" + i + "</a></li>");
//                     $("#page"+i).click(function (){
//                         funcType(i)
//                     })
//                 }
//             }
//             if (pageCode === totalPages) {
//                 $("#pageUL").append("<li class='page-item disabled'><span class='page-link'>Next&gt;</span></li>").append("<li class='page-item active'><span class='page-link'>Last<span class='sr-only'>(current)</span></span></li>");
//             } else {
//                 $("#pageUL").append("<li class='page-item' id='next'><a class='page-link addCursor'>Next&gt;</a></li>").append("<li class='page-item' id='last'><a class='page-link addCursor'>Last</a></li>");
//                 $("#next").click(function (){
//                     funcType(pageCode+1)
//                 })
//                 $("#last").click(function (){
//                     funcType(totalPages)
//                 })
//             }
//         }
//     }
// }