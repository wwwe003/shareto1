function pagination(pageCode, pageSize, totalRecords, functionType){
    pageCode=parseInt(pageCode);
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
                const itemFirst="<li class='page-item active'><span class='page-link'>First<span class='sr-only'>(current)</span></span></li>"
                const itemPrevious="<li class='page-item disabled'><span class='page-link'>&lt;Previous</span></li>"
                $("#pageUL").append(itemFirst).append(itemPrevious);
            } else {
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
                    $("#pageUL").append("<li class='page-item active'><span class='page-link' >" + i + "<span class='sr-only'>(current)</span></span></li>");
                } else {
                    $("#pageUL").append("<li class='page-item addCursor' id="+"page"+i+"><a class='page-link'>" + i + "</a></li>");
                    $("#page"+i).click(function (){
                        functionType(i)
                    })
                }
            }
            if (pageCode === totalPages) {
                $("#pageUL").append("<li class='page-item disabled'><span class='page-link'>Next&gt;</span></li>").append("<li class='page-item active'><span class='page-link'>Last<span class='sr-only'>(current)</span></span></li>");
            }else {
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