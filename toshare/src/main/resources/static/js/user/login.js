$(function() {

    $("#loginForm").submit(function(){
        let bool = true;
        $(".form-control").each(function() {
            const inputName = $(this).attr("name");
            if(!invokeValidateFunction(inputName)) {
                bool = false;
            }
        });
        return bool;
    });


    $(".form-control").focus(function() {
        const inputName = $(this).attr("name");
        $("#" + inputName + "Error").css("display", "none");
    }).blur(function() {
        const inputName = $(this).attr("name");
        invokeValidateFunction(inputName);
    })
});


function invokeValidateFunction(inputName) {
    inputName = inputName.substring(0, 1).toUpperCase() + inputName.substring(1);
    const functionName = "validate" + inputName;
    return eval(functionName + "()");
}

/*
 * 校验登录名
 */
function validateUsername() {
    let bool = true;
    $("#usernameError").css("display", "none");
    const value = $("#username").val();
    if(!value) {
        $("#usernameError").css("display", "").text("ユーザー名を入力してください。");
        bool = false;
    }
    return bool;
}

/*
 * 校验密码
 */
function validatePassword() {
    let bool = true;
    $("#passwordError").css("display", "none");
    const value = $("#password").val();
    if(!value) {
        $("#passwordError").css("display", "").text("パスワードを入力してください。");
        bool = false;
    }
    return bool;
}