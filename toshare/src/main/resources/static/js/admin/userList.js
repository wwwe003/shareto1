$(function () {
    $(".nav-item.top").click(function () {
        $(this).addClass("activeB").siblings().removeClass("activeB")
        $(this).addClass("active").siblings().removeClass("active")
    })
    $("#user").addClass("activeB").addClass("active")

    user_Pagination()
})
function user_Pagination(){
    const pageCode=parseInt($("#showUsers").attr('data-pageCode'));
    const pageSize=parseInt($("#showUsers").attr('data-pageSize'));
    const totalRecord=parseInt($("#showUsers").attr('data-totalRecord'));


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
                $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/admin/user?currentPage=" + 1 + "'>First</a></li>")
                    .append("<li class='page-item'><a class='page-link' href='/toshare/admin/user?currentPage=" + pagePrevious + "'>&lt;Previous</a></li>");
            }
            for (let i = begin; i <= end; i++) {
                if (i === pageCode) {
                    $("#pageUL").append("<li class='page-item active'><span class='page-link' >" + i + "<span class='sr-only'>(current)</span></span></li>")
                } else {
                    $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/admin/user?currentPage=" + i + "'>" + i + "</a></li>");
                }
            }
            if (pageCode === totalPages) {
                $("#pageUL").append("<li class='page-item disabled'><span class='page-link'>Next&gt;</span></li>")
                    .append("<li class='page-item active'><span class='page-link'>Last<span class='sr-only'>(current)</span></span></li>");
            } else {
                $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/admin/user?currentPage=" + pageNext + "'>Next&gt;</a></li>")
                    .append("<li class='page-item'><a class='page-link' href='/toshare/admin/user?currentPage=" + totalPages + "'>Last</a></li>");
            }
        }
    }
}