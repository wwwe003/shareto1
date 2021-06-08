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
// function changeType(supertype,pid){
//     const subtype=supertype.substring(supertype.indexOf("(")+1,supertype.lastIndexOf(")"))
//     supertype=supertype.substring(0,supertype.lastIndexOf("("))
//     $("#editModal [name='pid']").val(pid)
//     $("#inputType1").empty()
//     $.ajax({
//         url: "/toshare/admin/typeMap",
//         type:"POST",
//         data:{method:"typeMap"},
//         dataType:"json",
//         async:true,
//         cache:false,
//         success:function (result){
//             $.each(result, function (key, value) {
//                 if (supertype===key){
//                     $("#inputType1").prepend("<option selected>"+supertype+"</option>")
//                     $("#inputType2").empty()
//                     $.each(value, function (i, item) {
//                         // console.log(item)
//                         if (subtype===item){
//                             $("#inputType2").prepend("<option selected>"+subtype+"</option>")
//                         }else {
//                             $("#inputType2").append("<option>" + item + "</option>")
//                         }
//                     })
//                 }else {
//                     $("#inputType1").append("<option>"+key+"</option>")}
//             })
//         }
//     })
//     $('#editModal').modal()
// }
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