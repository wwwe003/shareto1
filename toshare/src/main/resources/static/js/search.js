$(function (){
     pagination()
})
function pagination(){
    const pageCode=1;
    const pageSize=$("#page_info .allPosts").attr('data-pageSize');
    const totalRecords=$("#page_info .allPosts").attr('data-totalRecord');
    _pagination(pageCode,pageSize,totalRecords,showResult);
}
function showResult(pageCode){
    const limit=$("#search_type").attr('data-search')
    const keyword=$("#search_type").attr('data-keyword')
    $.ajax({
        url:"/toshare/searchPG",
        type:"POST",
        data:{method:"searchPG", currentPage: pageCode,keyword:keyword,limit:limit},
        dataType:"json",
        async:false,
        cache:false,
        success:function (data){
            if (data.beanList.length!==0){
                // $("#noResult").remove()
                $("#search_result").empty()
                for (let i=0;i<data.beanList.length;i++) {
                    const date=createTime(data.beanList[i].update_time)
                    $("#search_result").append(
                        "<div class='input-group'><div class='article'><div class='title'><a href='/"+data.beanList[i].post_id+"'>"+
                        "<h4>"+data.beanList[i].title+"</h4></a>"+
                            "<p>"+data.beanList[i].description+"</p></div>"+
                    "<div class='footer'><a href='/toshare/user/"+data.beanList[i].userno+"' target='_blank'>"+data.beanList[i].author+"</a>"+
                        "<span>"+date+"</span></div></div></div>")
                }
                $("#pagination .pagination").empty()
                _pagination(data.pageCode,data.pageSize,data.totalRecord,showResult);
            }
        }
    })
}
function _pagination(pageCode,pageSize,totalRecords,funcType) {
    pageCode=parseInt(pageCode);
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
                if (i=== pageCode) {
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