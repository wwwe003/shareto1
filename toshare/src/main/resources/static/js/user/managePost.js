$(function (){
    paginationManagePost();
    selectFunc()
    $("#btn-check").click(function (){
        showPostByType(1)
    })
    $("#btn-post").click(function (){
        const username = $(this).val()
        window.location.href="/toshare/"+username+"/post/new"
    })
    $("#showPost").on('click','#edit-button',function (){
        draftArticle(1)
    }).on('click','#delete-button',function (){
        deleteArticle(1)
    })

    $("#drafts").click(function (){
        $(this).addClass("active").siblings().removeClass("active")
        $("#btn-manage").removeClass("active")
        // $("#trash").removeClass("active")
        draftArticle(1)
    })
    $("#trash").click(function (){
        $(this).addClass("active").siblings().removeClass("active")
        // $("#drafts").removeClass("active")
        $("#btn-manage").removeClass("active")
        deleteArtice(1)
    })
    $("#under_review").click(function (){
        $(this).addClass("active").siblings().removeClass("active")
        // $("#drafts").removeClass("active")
        $("#btn-manage").removeClass("active")
       underReviewArtice(1)
    })

    $("#showTable").on('click','.edit-button',function (){
        const username=$(this).attr("data-username")
        const pid=$(this).attr("data-pid")
        $("#continue").attr("href","/toshare/"+username+"/edit/"+pid+"")
        $('#editModal').modal()
    }).on('click','.delete-button',function (){
        const username=$(this).attr("data-username")
        const pid=$(this).attr("data-pid")
        $("#continue-delete").attr("href","/toshare/"+username+"/delete/"+pid+"")
        $('#deleteModal').modal()
    }).on('click','.complete-delete-button',function (){
        const username=$(this).attr("data-username")
        const pid=$(this).attr("data-pid")
        $("#continue-complete-delete").attr("href","/toshare/"+username+"/completeDelete/"+pid+"")
        $('#complete-deleteModal').modal()
    })
})
function draftArticle(pageCode){
    $.ajax({
        url: "draft",
        type: "POST",
        data: {method: "draftArticle", currentPage: pageCode},
        dataType: "json",
        async: true,
        cache: false,
        success:function (data){
            let subtypeName;
            let supertypeName;
            if (data.beanList.length !== 0) {
                $("#noResult").remove()
                $("#showPost").empty()
                for (let i = 0; i < data.beanList.length; i++) {
                    if (data.beanList[i].type_first_name == null) {
                        supertypeName = "deleted supertype"
                    } else {
                        supertypeName = data.beanList[i].type_first_name
                    }
                    if (data.beanList[i].type_second_name == null) {
                        subtypeName = "deleted subtype"
                    } else {
                        subtypeName = data.beanList[i].type_second_name
                    }
                    const date = createTime(data.beanList[i].update_time)
                    const no = (data.pageCode - 1) * 3 + i + 1
                    $("#showPost").append("<tr><td>" + no + "</td>" +
                        "<td>" + data.beanList[i].title + "</td>" +
                        "<td>" + supertypeName + "(" + subtypeName + ")" + "</td>" +
                        "<td>" + date + "</td><td>" +
                        "<a class='btn btn-sm btn-primary edit-button' href='/toshare/" + data.beanList[i].author + "/edit/" + data.beanList[i].post_id + "' style='margin-right: 3px'>Edit</a>" +
                        "<a data-username='" + data.beanList[i].author + "' data-pid='" + data.beanList[i].post_id + "' class='btn btn-sm btn-danger deleteBtn delete-button'>Delete</a></td></tr>")
                }
                $("#pagination .pagination").empty()
                $("#totalPost span").text(data.totalRecord)
                $("#totalPost p:last").text("\xa0" + "article is draft")
                _pagination(data.pageCode.toString(), data.pageSize, data.totalRecord, draftArticle);
            } else {
                $("#noResult").remove()
                $("#showPost").empty()
                $("#totalPost span").text("0")
                $("#showTable").after("<div id='noResult'><span>No result</span></div>")
                $("#pagination .pagination").empty()
            }
        }
    })
}
function deleteArtice(pageCode){
    $.ajax({
        url: "deleted",
        type: "POST",
        data: {method: "deletedArticle", currentPage: pageCode},
        dataType: "json",
        async: true,
        cache: false,
        success:function (data){
            let subtypeName;
            let supertypeName;
            if (data.beanList.length !== 0) {
                $("#noResult").remove()
                $("#showPost").empty()
                for (let i = 0; i < data.beanList.length; i++) {
                    if (data.beanList[i].type_second_name == null) {
                        subtypeName = "deleted subtype"
                    } else {
                        subtypeName = data.beanList[i].type_second_name
                    }
                    if (data.beanList[i].type_first_name == null) {
                        supertypeName = "deleted supertype"
                    } else {
                        supertypeName = data.beanList[i].type_first_name
                    }
                    const date = createTime(data.beanList[i].update_time)
                    const no = (data.pageCode - 1) * 3 + i + 1
                    $("#showPost").append("<tr><td>" + no + "</td>" +
                        "<td>" + data.beanList[i].title + "</td>" +
                        "<td>" + supertypeName + "(" + subtypeName + ")" + "</td>" +
                        "<td>" + date + "</td><td>" +
                        "<form method='post' action='/toshare/back'><input type='hidden' name='username' value='" + data.beanList[i].author + "'><input type='hidden' name='pid' value='" + data.beanList[i].post_id + "'>" +
                        "<button type='submit' class='btn btn-sm btn-info'>Back to drafts</button></form>" +
                        "<button data-username='" + data.beanList[i].author + "' data-pid='" + data.beanList[i].post_id + "' class='complete-delete-button btn btn-sm btn-danger'>Complete Delete</button></td></tr>")
                }
                $("#pagination .pagination").empty()
                $("#totalPost span").text(data.totalRecord)
                $("#totalPost p:last").text("\xa0" + "article deleted")
                _pagination(data.pageCode.toString(), data.pageSize, data.totalRecord, deleteArtice);
            } else {
                $("#noResult").remove()
                $("#showPost").empty()
                $("#totalPost span").text("0")
                $("#showTable").after("<div id='noResult'><span>No result</span></div>")
                $("#pagination .pagination").empty()
            }
        }
    })
}
function underReviewArtice(pageCode){
    $.ajax({
        url: "underReview",
        type: "POST",
        data: {method: "underReviewArticle", currentPage: pageCode},
        dataType: "json",
        async: true,
        cache: false,
        success:function (data){
            if (data.beanList.length!==0){
                $("#noResult").remove()
                $("#showPost").empty()
                for (let i=0;i<data.beanList.length;i++) {
                    const date=createTime(data.beanList[i].update_time)
                    const no=(data.pageCode-1)*3+i+1
                    $("#showPost").append("<tr><td>"+no+"</td>"+
                        "<td>"+data.beanList[i].title+"</td>"+
                        "<td>"+data.beanList[i].type_first_name+"("+data.beanList[i].type_second_name+")"+"</td>"+
                        "<td>"+date+"</td><td>"+
                        "<form method='post' action='/toshare/back'><input type='hidden' name='username' value='"+data.beanList[i].author+"'><input type='hidden' name='pid' value='"+data.beanList[i].post_id+"'>" +
                        "<span>Under review</span></td></tr>")
                }
                $("#pagination .pagination").empty()
                $("#totalPost span").text(data.totalRecord)
                $("#totalPost p:last").text("\xa0"+"article is under review")
                _pagination(data.pageCode.toString(),data.pageSize,data.totalRecord,underReviewArtice);
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
function selectFunc(){
$("#inputType1").change(function (){
    const type1=$(this).val();
    const username=$("#btn-post").val()
    if (type1!=="Type1"){
    $.ajax({
        url:"/toshare/"+username+"/getType2",
        type:"POST",
        data:{method:"typeSeconds"},
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
    }else {
        $("#inputType2").empty().append("<option selected>Type2</option>")
    }
})
}
function  showPostByType(pageCode){
    const type1=$("#inputType1").val()
    const type2=$("#inputType2").val()
    if (type1!=="Type1"){
    $.ajax({
        url:"getPost",
        type:"POST",
        data:{method:"getPostByType", typeName1: type1, typeName2: type2, pageCode: pageCode},
        dataType:"json",
        async:true,
        cache:false,
        success:function (data){
            if (data.beanList.length!==0){
                $("#noResult").remove()
                $("#showPost").empty()
            for (let i=0;i<data.beanList.length;i++) {
                const date=createTime(data.beanList[i].update_time)
                const no=(data.pageCode-1)*3+i+1
                $("#showPost").append("<tr><td>"+no+"</td>"+
                    "<td>"+data.beanList[i].title+"</td>"+
                    "<td>"+data.beanList[i].type_first_name+"("+data.beanList[i].type_second_name+")"+"</td>"+
                    "<td>"+date+"</td><td>"+
                    "<a href='/toshare/"+data.beanList[i].author+"/edit/"+data.beanList[i].post_id+"?mode=scan' class='btn btn-sm btn-primary scan-button mr-1'>scan</a>"+
                    "<a data-username='"+data.beanList[i].author+"' data-pid='"+data.beanList[i].post_id+"' class='btn btn-sm btn-danger edit-button'>Edit</a>"+
                    "</td></tr>")
                // < a class = 'btn btn-sm btn-danger deleteBtn delete-button'
                // href = '/toshare/"+data.beanList[i].author+"/delete/"+data.beanList[i].post_id+"'
                // data - toggle = 'tooltip'
                // data - placement = 'bottom'
                // title = 'Deleted article will be put in recycle bin' > Delete < /a>
            }
                $("#pagination .pagination").empty()
                $("#totalPost span").text(data.totalRecord)
                _pagination(data.pageCode.toString(),data.pageSize,data.totalRecord,showPostByType);
        }else {
                $("#noResult").remove()
                $("#showPost").empty()
                $("#totalPost span").text("0")
                $("#showTable").after("<div id='noResult'><span>No result</span></div>")
                $("#pagination .pagination").empty()
            }
        }
    })
    }else {
        showMyPost(1)
    }
}
function paginationManagePost(){
    const pageCode="1"
    const pageSize=$("#showPost").attr('value');
    const totalRecords=$("#showPost").attr('name')

    _pagination(pageCode,pageSize,totalRecords,showMyPost);
}
function showMyPost(pageCode){
    $.ajax({
        url:"getPostByPC",
        type:"POST",
        data:{method:"getPostByPageCode", currentPage: pageCode},
        dataType:"json",
        async:true,
        cache:false,
        success:function (data){
            if (data.beanList.length!==0){
                $("#noResult").remove()
                $("#showPost").empty()
                for (let i=0;i<data.beanList.length;i++) {
                    const date=createTime(data.beanList[i].update_time)
                    const no=(data.pageCode-1)*3+i+1
                    $("#showPost").append("<tr><td>"+no+"</td>"+
                        "<td>"+data.beanList[i].title+"</td>"+
                        "<td>"+data.beanList[i].type_first_name+"("+data.beanList[i].type_second_name+")"+"</td>"+
                        "<td>"+date+"</td><td>"+
                        "<a href='/toshare/"+data.beanList[i].author+"/edit/"+data.beanList[i].post_id+"?mode=scan' class='btn btn-sm btn-primary scan-button mr-1'>scan</a>"+
                        "<a data-username='"+data.beanList[i].author+"' data-pid='"+data.beanList[i].post_id+"' class='btn btn-sm btn-danger edit-button'>Edit</a>"+
                        "</td></tr>")
                    // < a
                    // className = 'btn btn-sm btn-danger deleteBtn delete-button'
                    // href = '/toshare/"+data.beanList[i].author+"/delete/"+data.beanList[i].post_id+"'
                    // data - toggle = 'tooltip'
                    // data - placement = 'bottom'
                    // title = 'Deleted article will be put in recycle bin' > Delete < /a>
                }
            $("#pagination .pagination").empty()
                _pagination(data.pageCode.toString(),data.pageSize,data.totalRecord,showMyPost);
            }
        }
    })
}
function _pagination(pageCode,pageSize,totalRecords,funcType) {
    pageCode=parseInt(pageCode)
    let begin, end;
    const totalPages = Math.ceil(totalRecords / pageSize);
    begin = parseInt(pageCode) - 2;
    end = parseInt(pageCode) + 2;

    if (pageCode !== undefined && pageSize !== undefined && totalRecords !== undefined) {
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
                $("#pageUL").append("<li class='page-item active'><span class='page-link'>First<span class='sr-only'>(current)</span></span></li>").append("<li class='page-item disabled'><span class='page-link'>&lt;Previous</span></li>");
            } else {
                $("#pageUL").append("<li class='page-item addCursor' id='first'><a class='page-link'>First</a></li>").append("<li class='page-item addCursor' id='previous'><a class='page-link'>&lt;Previous</a></li>");
                $("#first").click(function (){
                    funcType(1)
                })
                $("#previous").click(function (){
                    funcType(pageCode-1)
                })
            }
            for (let i = begin; i <= end; i++) {
                if (i === pageCode) {
                    $("#pageUL").append("<li class='page-item active'><span class='page-link' >" + i + "<span class='sr-only'>(current)</span></span></li>");
                } else {
                    $("#pageUL").append("<li class='page-item addCursor' id="+"page"+i+"><a class='page-link'>" + i + "</a></li>");
                    $("#page"+i).click(function (){
                        funcType(i)
                    })
                }
            }
            if (pageCode === totalPages) {
                $("#pageUL").append("<li class='page-item disabled'><span class='page-link'>Next&gt;</span></li>").append("<li class='page-item active'><span class='page-link'>Last<span class='sr-only'>(current)</span></span></li>");
            } else {
                $("#pageUL").append("<li class='page-item' id='next'><a class='page-link addCursor'>Next&gt;</a></li>").append("<li class='page-item' id='last'><a class='page-link addCursor'>Last</a></li>");
                $("#next").click(function (){
                    funcType(pageCode+1)
                })
                $("#last").click(function (){
                    funcType(totalPages)
                })
            }
        }
    }
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
    return y + "-" + m + "-" + d+" "+h+":"+M+":"+s;
}