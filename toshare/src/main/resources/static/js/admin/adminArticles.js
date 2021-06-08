$(function(){
    //top bar dynamic active
    $(".nav-item.top").click(function (){
        $(this).addClass("activeB").siblings().removeClass("activeB")
        $(this).addClass("active").siblings().removeClass("active")
    })
    $("#article").addClass("activeB").addClass("active")


    //sidebar dynamic active
    // $(".sidebar-sticky ul li img").click(function (){
    //     $(".sidebar-sticky ul li .supertype").removeClass("active")
    //     $(this).siblings(".supertype").addClass("active")
    // })
    $(".sidebar-sticky ul li dl dd a").click(function (){
        $(".sidebar-sticky ul li .supertype").removeClass("active")
        $(".sidebar-sticky ul li dl dd a").removeClass("active")
        $(this).addClass("active").parents("li").children(".supertype").addClass("active")
    })
    $(".sidebar-sticky ul li:first .supertype").addClass("active")
    $(".sidebar-sticky ul li:first dl dd:first a").addClass("active")

    sidebarMenu()
    cascadeMenu()
    paginationManagePost();
    $("#showTable").on('click','.change-button',function (){
        // const supertypeID=$(this).parents("tr").find(".typeID").attr("data-supertypeID")
        // const subtypeID=$(this).parents("tr").find(".typeID").attr("data-subtypeID")
        const supertype=$(this).parents("tr").find(".typeID").text()
        const pid=$(this).parents("tr").find(".td_title").attr("data-pid")
        const title=$(this).parents("tr").find(".td_title").text()
        const author=$(this).parents("tr").find(".td_author").text()

        changeType(supertype,pid,author,title,1)
    })
    showBySubtype()
    $("#searchButton").click(function (){
        showResult(1)
    })
})
function showBySubtype(){
    $("#sidebarMenu").on('click','dl dd a',function (){
        const subtypeStr=$(this).text()
        const subtype=subtypeStr.substring(0,subtypeStr.indexOf('('))
        $.ajax({
            url:"/toshare/admin/showByType",
            type:"POST",
            data:{method:"showByType",subtype:subtype},
            dataType:"json",
            async:true,
            cache:false,
            success:function (data) {
                if (data.beanList.length !== 0) {
                    $("#noResult").remove()
                    $("#showPost").empty()
                    for (let i = 0; i < data.beanList.length; i++) {
                        const date = createTime(data.beanList[i].update_time)
                        const no = (data.pageCode - 1) * 2 + i + 1
                        $("#showPost").append("<tr><td>" + no + "</td>" +
                            "<td style='width: 220px' class='td_title' data-pid='" + data.beanList[i].post_id + "'>" + data.beanList[i].title + "</td>" +
                            "<td>" + data.beanList[i].author + "</td>" +
                            "<td class='typeID' data-supertypeID='" + data.beanList[i].type + "' data-subtypeID='" + data.beanList[i].type_second_id + "'>" + data.beanList[i].type_first_name + "(" + data.beanList[i].type_second_name + ")" + "</td>" +
                            "<td>" + date + "</td><td>" +
                            "<a style='margin-right: 3px' class='btn btn-sm btn-primary change-button'>Change type</a>" +
                            "<a style='margin-right: 3px' class='btn btn-sm btn-danger review-button' href='/toshare/admin/review/"+data.beanList[i].post_id+"?mode=review'>Review again</a></td></tr>")
                    }
                    $("#totalPost").remove()
                    $("#showTable").before("<div id='totalPost'><p>total:&nbsp;</p><span>"+data.totalRecord+"</span><p>&nbsp;post</p></div>")
                    $("#pagination .pagination").empty()
                    pagination(data.pageCode.toString(), data.pageSize, data.totalRecord, showMyPost);
                }else {
                    $("#noResult").remove()
                    $("#showPost").empty()
                    $("#totalPost").remove()
                    //$("#totalPost span").text("0")
                    $("#showTable").after("<div id='noResult'><span>No result</span></div>")
                    $("#pagination .pagination").empty()
                }
            }
        })
    })
}
function sidebarMenu(){
    $(".sidebar-sticky ul li:first img").attr("src","/toshare/images/caret-up-fill.svg")
    $(".sidebar-sticky ul li:first dl").show()

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
function paginationManagePost(){
    const pageCode="1"
    const pageSize=$("#showPost").attr('data-PageSize');
    const totalRecords=$("#showPost").attr('data-totalRecord')

    pagination(pageCode,pageSize,totalRecords,showMyPost);
}

function showMyPost(pageCode){
    const supertypeID=$("#showPost .typeID").attr('data-supertypeID')
    const subtypeID=$("#showPost .typeID").attr('data-subtypeID')
    $.ajax({
        url:"/toshare/admin/articlePG",
        type:"POST",
        data:{method:"articlePG", currentPage: pageCode,supertypeID:supertypeID,subtypeID:subtypeID},
        dataType:"json",
        async:false,
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
                        "<td>"+date+"</td><td>"+
                        "<a style='margin-right: 3px' class='btn btn-sm btn-primary change-button'>Change type</a>"+
                        "<a style='margin-right: 3px' class='btn btn-sm btn-danger review-button' href='/toshare/admin/review/"+data.beanList[i].post_id+"?mode=review'>Review again</a></td></tr>")
                }
                $("#pagination .pagination").empty()
                pagination(data.pageCode.toString(),data.pageSize,data.totalRecord,showMyPost);
            }
        }
    })
}
function showResult(pageCode){
    const limit=$("#inputState").val()
    const keyword=$("#keyword").val().replace(/[ ]/g,"")
    if (keyword.length!==0){
        $(".sidebar-sticky ul li:first .supertype").removeClass("active")
        $(".sidebar-sticky ul li:first dl dd:first a").removeClass("active")
        $.ajax({
            url: "/toshare/searchPG",
            type: "POST",
            data: {method: "searchPG", currentPage: pageCode, keyword: keyword, limit: limit},
            dataType: "json",
            async: false,
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
                            "<td>" + date + "</td><td>" +
                            "<a style='margin-right: 3px' class='btn btn-sm btn-primary change-button'>Change type</a>" +
                            "<a style='margin-right: 3px' class='btn btn-sm btn-danger review-button' href='/toshare/admin/review/"+data.beanList[i].post_id+"?mode=review'>Review again</a></td></tr>")
                    }
                    $("#pagination .pagination").empty()
                    pagination(data.pageCode, data.pageSize, data.totalRecord, showResult);
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
}

function cascadeMenu(){
    $("#inputType1").change(function (){
        const type1=$(this).val();
        if (type1!=="select"){
            $.ajax({
                url:"/toshare/admin/typeMap",
                type:"POST",
                data:{method:"typeMap"},
                dataType:"json",
                async:true,
                cache:false,
                success:function (data){
                    $.each(data,function (key,value){
                        if (type1===key){
                            $("#inputType2").empty()
                            $.each(value,function (i,item){
                                $("#inputType2").append("<option>"+item+"</option>")
                            })
                        }
                    })
                }
            })
        }
    })
}
function changeType(supertype,pid,author,title,passed_article){
    $("#editModal [name='oldtype']").val(supertype)
    const subtype=supertype.substring(supertype.indexOf("(")+1,supertype.lastIndexOf(")"))
    // console.log(subtype)
    //console.log(pid)

    supertype=supertype.substring(0,supertype.lastIndexOf("("))
    $("#editModal [name='post_id']").val(pid)
    $("#editModal [name='passed_article']").val(passed_article)
    $("#editModal [name='author']").val(author)
    $("#editModal [name='title']").val(title)
    $("#inputType1").empty()
    $.ajax({
        url: "/toshare/admin/typeMap",
        type:"POST",
        data:{method:"typeMap"},
        dataType:"json",
        async:false,
        cache:false,
        success:function (result){
            $.each(result, function (key, value) {
                if (supertype===key){
                    $("#inputType1").prepend("<option selected>"+supertype+"</option>")
                    $("#inputType2").empty()
                    $.each(value, function (i, item) {
                        // console.log(item)
                        if (subtype===item){
                            $("#inputType2").prepend("<option selected>"+subtype+"</option>")
                        }else {
                            $("#inputType2").append("<option>" + item + "</option>")
                        }
                    })
                }else {
                $("#inputType1").append("<option>"+key+"</option>")}
            })
        }
    })
    $('#editModal').modal()
}
