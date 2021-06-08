function showSearchResult(pageCode){
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
                            "<td>" + date + "</td><td>" +
                            "<a style='margin-right: 3px' class='btn btn-sm btn-primary change-button'>Change type</a>" +
                            "<a style='margin-right: 3px' class='btn btn-sm btn-warning review-button' href='#'>Review</a>" +
                            "<a class='btn btn-sm btn-danger deleteBtn delete-button' href='#'>Delete</a></td></tr>")
                    }
                    $("#pagination .pagination").empty()
                    pagination(data.pageCode, data.pageSize, data.totalRecord, showSearchResult);
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