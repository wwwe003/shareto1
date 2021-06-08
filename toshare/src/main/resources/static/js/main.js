$(function (){
   $(".channel .channel-more").hover(function (){
      $(".channel .channel-more-layer").show();},function (){
       $(".channel .channel-more-layer").hide();
   });
    $(document).click(function(){
        $(".tabs-wrapper .sub-tabs .more-sub-tabs .dropdown-menu").hide();
    });
    $(".tabs-wrapper .sub-tabs .more-sub-tabs")
    //     .hover(function (){
    //     $(".tabs-wrapper .sub-tabs .more-sub-tabs .dropdown-menu").show(500);},function (){
    //     $(".tabs-wrapper .sub-tabs .more-sub-tabs .dropdown-menu").hide();
    // })
        .click(function (event){
            event.stopPropagation()
        $(".tabs-wrapper .sub-tabs .more-sub-tabs .dropdown-menu").toggle(500);
    })
   // $("#nav1 li a").click(function (){
   //     $("#nav1 li a").removeClass("active")
   //     $(this).addClass("active")
   // })
   //  $("#a1").addClass("active")
    funMoreItem();
    funMoreSubItem()
    menuFix();
    subMenuFix();
    paginationAll();
    pagination1stType();
    pagination2ndType();

});

function menuFix() {
    let strUrl, strHref;
    // const Navs = document.getElementById("nav1").getElementsByTagName("a");     //nav1是ul 的id
    const Navs =$("#nav1 a");
    // 如果链接没有参数，或者URL链接中不存在我们要获取的参数，则返回数组中的序号
    //let strUrl0 = location.href;//http://localhost:8080/toshare/main
    strUrl=location.href.substring(location.href.lastIndexOf("/")+1);//取得URL页面名称例如：http://localhost:8080/toshare/main→main
    if (strUrl.indexOf("?")!==-1){
    strUrl=strUrl.substring(0,strUrl.indexOf("?"));
    //console.log(strUrl)
    }
    //const Navs =$("#nav1 a");
    for (let i = 0; i < Navs.length; i++) {
        strHref=Navs[i].getAttribute("href").substring(Navs[i].getAttribute("href").lastIndexOf('/')+1);

        if(strUrl===strHref){//高亮当前菜单

             $(Navs[i]).css("background-color","red").css("color","white");
        }
    }
}
function subMenuFix() {
    let subStrUrl, subStrHref, subStr, currentUrl, subStrHrefNew;
    const Navs1 =$("#nav1 a");
    const Navs2 =$(".tab-item .subItems");
    subStrUrl=$(".tab-item #subItem").attr('href');
     if(subStrUrl!==undefined) {
         subStr = subStrUrl.substring(subStrUrl.lastIndexOf("/") + 1);
     }

    for (let i = 0; i < Navs1.length; i++) {
        subStrHref=Navs1[i].getAttribute("href").substring(Navs1[i].getAttribute("href").lastIndexOf('/')+1);
        if(subStr===subStrHref){//高亮当前菜单
            $(Navs1[i]).css("background-color","red").css("color","white");
        }
    }


    currentUrl=location.href.substring(location.href.lastIndexOf("/")+1);
    if (currentUrl.indexOf("?")!==-1){
        currentUrl=currentUrl.substring(0,currentUrl.indexOf("?"));
    }

    //设置二级标题动态高亮
        if(subStr===currentUrl){
        $("#subItem").parent().addClass("active");
        }
        for (let i = 0; i < Navs2.length; i++) {
            //subStrHref = Navs2[i].getAttribute("href").substring(Navs2[i].getAttribute("href").lastIndexOf('/') + 1);
            subStrHref=Navs2[i].getAttribute("href");
            subStrHrefNew = subStrHref.substring(subStrHref.lastIndexOf("/") + 1);
            if (currentUrl === subStrHrefNew) {//高亮当前菜单
                $(Navs2[i]).parent().addClass("active");
                //classList.add("active");
                //console.log(subStrHrefNew);
            }
        }
    }


function funMoreItem(){
    const list = $(".channel ul li");
     if (list.length>5){
         //const more = $(".channel ul li:gt(5)" ).text();
         const more = $(".channel ul li:gt(5) a");
         for(let i=0; i<more.length-1; i++){
             const itemdiv=$('<div></div>');
             itemdiv.appendTo($("#more-layer-parent"));
             $("#more-layer-parent div:eq("+i+")").append(more[i])//是字符串拼接，相当于先执行括号bai部分，字符串".abc:eq(" 拼接i变量再拼接字符串")"
         }
    }
}

function funMoreSubItem(){
    const list = $(".sub-tabs .tab-item");
    if (list.length>3){
        const more = $(".sub-tabs .tab-item:gt(3)");
        for(let i=0; i<more.length-1; i++){
            console.log(list.length)
            $(".sub-tabs .more-sub-tabs-layer ul").append(more[i])
        }
    }
}
function urlSubstring(pageUrl){
    if (pageUrl.indexOf("?")!==-1){
        pageUrl=pageUrl.substring(0,pageUrl.indexOf("?"));
    }
    return pageUrl;
}


function paginationAll(){
    const pageCode=$(".allPosts").attr('id');
    const pageSize=$(".allPosts").attr('name');
    const totalRecords=$(".allPosts").attr('value');
    let pageUrl;
    pageUrl=location.href.substring(location.href.lastIndexOf("/")+1);//取得URL页面名称例如：http://localhost:8080/toshare/main→main
    pageUrl= urlSubstring(pageUrl)
    _pagination(pageCode,pageSize,totalRecords,pageUrl);
}
function pagination1stType(){
    const pageCode=$(".PostBy1stType").attr('id');
    const pageSize=$(".PostBy1stType").attr('name');
    const totalRecords=$(".PostBy1stType").attr('value');
    let pageUrl;
    pageUrl=location.href.substring(location.href.lastIndexOf("/")+1);//取得URL页面名称例如：http://localhost:8080/toshare/main→main
    pageUrl= "type/"+urlSubstring(pageUrl);
    _pagination(pageCode,pageSize,totalRecords,pageUrl);
}
function pagination2ndType(){
    const pageCode=$(".PostBy2ndType").attr('id');
    const pageSize=$(".PostBy2ndType").attr('name');
    const totalRecords=$(".PostBy2ndType").attr('value');
    let pageUrl;
    pageUrl=location.href.substring(location.href.lastIndexOf("/")+1);//取得URL页面名称例如：http://localhost:8080/toshare/main→main
    pageUrl= "type/"+urlSubstring(pageUrl);
    _pagination(pageCode,pageSize,totalRecords,pageUrl);
}
function _pagination(pageCode,pageSize,totalRecords,pageUrl) {
    let begin, end;
    const totalPages = Math.ceil(totalRecords / pageSize);
    const pageNext = parseInt(pageCode) + 1;
    const pagePrevious = parseInt(pageCode) - 1;
    begin = parseInt(pageCode) - 2;
    end = parseInt(pageCode) + 2;//1 3 16 6 begin=-1<1 end=3, begin=1,end=5

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
    if (pageCode === "1") {
        $("#pageUL").append("<li class='page-item active'><span class='page-link'>First<span class='sr-only'>(current)</span></span></li>");
        $("#pageUL").append("<li class='page-item disabled'><span class='page-link'>&lt;Previous</span></li>");
    } else {
        $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/"+pageUrl+"?currentPage=" + 1 + "'>First</a></li>");
        $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/"+pageUrl+"?currentPage=" + pagePrevious + "'>&lt;Previous</a></li>");
    }

    for (let i = begin; i <= end; i++) {
        if (i.toString() === pageCode) {
            $("#pageUL").append("<li class='page-item active'><span class='page-link' >" + i + "<span class='sr-only'>(current)</span></span></li>");
        } else {
            $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/"+pageUrl+"?currentPage=" + i + "'>" + i + "</a></li>");
        }
    }
    if (pageCode === totalPages.toString()) {
        $("#pageUL").append("<li class='page-item disabled'><span class='page-link'>Next&gt;</span></li>");
        $("#pageUL").append("<li class='page-item active'><span class='page-link'>Last<span class='sr-only'>(current)</span></span></li>");
    } else {
        $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/"+pageUrl+"?currentPage=" + pageNext + "'>Next&gt;</a></li>");
        $("#pageUL").append("<li class='page-item'><a class='page-link' href='/toshare/"+pageUrl+"?currentPage=" + totalPages + "'>Last</a></li>");
    }
    }
    }
}