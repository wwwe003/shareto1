$(function() {
    /*
     * 1. 得到所有的错误信息，循环遍历之。调用一个方法来确定是否显示错误信息！
     */
    $(".errorClass").each(function() {
        showError($(this));//遍历每个元素，使用每个元素来调用showError方法
    });


    /*
     * 3. 输入框得到焦点隐藏错误信息
     */
    $(".form-control").focus(function () {
        const labelId = $(this).attr("id") + "Error";//通过输入框找到对应的label的id
            $("#" + labelId).text("");//把label的内容清空！
            showError($("#" + labelId));//隐藏没有信息的label
        });

    /*
     * 4. 输入框失去焦点进行校验
     */
    $(".form-control").blur(function() {
        const id = $(this).attr("id");//获取当前输入框的id
        const funName = "validate" + id.substring(0, 1).toUpperCase() + id.substring(1) + "()";//得到对应的校验函数名
            eval(funName);//执行函数调用
        });

    /*
     * 5. 表单提交时进行校验
     */
    $("#registForm").submit(function() {
        let bool = true;
        if(!validateUsername()) {
            bool = false;
        }
        if(!validatePassword()) {
            bool = false;
        }
        if(!validateConfirmPwd()) {
            bool = false;
        }
        if(!validateEmail()) {
            bool = false;
        }
        return bool;
    });
});

/*
 * 登录名校验方法
 */
function validateUsername() {
    const id = "username";
    const value = $("#" + id).val();//获取输入框内容
    /*
     * 1. 非空校验
     */
    if(!value) {
        $("#" + id + "Error").text("ユーザー名を入力してください。");
        showError($("#" + id + "Error"));
        return false;
    }
    /*
     * 2. 长度校验
     */
    if(value.length < 4 || value.length > 20) {
        /*
         * 获取对应的label
         * 添加错误信息
         * 显示label
         */
        $("#" + id + "Error").text("4から20までの文字を入力してください。");
        showError($("#" + id + "Error"));
        return false;
    }
    /*
     * 3. 是否注册校验
     */
    $.ajax({
        url:"fusername",//要请求的servlet
        data:{method:"ajaxValidateUsername", username:value},//给服务器的参数
        type:"POST",
        dataType:"json",
        async:false,//是否异步请求，如果是异步，那么不会等服务器返回，我们这个函数就向下运行了。
        cache:false,
        success:function(result) {
            if(!result) {//如果校验失败
                $("#" + id + "Error").text("ユーザー名はすでに存在します。");
                showError($("#" + id + "Error"));
                return false;
            }
        }
    });
    return true;
}


/*
 * 登录密码校验方法
 */
function validatePassword() {
    var id = "password";
    var value = $("#" + id).val();//获取输入框内容
    /*
     * 1. 非空校验
     */
    if(!value) {
        /*
         * 获取对应的label
         * 添加错误信息
         * 显示label
         */
        $("#" + id + "Error").text("パスワードを入力してください。");
        showError($("#" + id + "Error"));
        return false;
    }
    /*
     * 2. 长度校验
     */
    if(value.length < 6 || value.length > 20) {
        /*
         * 获取对应的label
         * 添加错误信息
         * 显示label
         */
        $("#" + id + "Error").text("6から20までの文字を入力してください。");
        showError($("#" + id + "Error"));
        return  false;
    }
    return true;
}

/*
 * 确认密码校验方法
 */
function validateConfirmPwd() {
    const id = "confirmPwd";
    const value = $("#" + id).val();//获取输入框内容
    /*
     * 1. 非空校验
     */
    if(!value) {
        /*
         * 获取对应的label
         * 添加错误信息
         * 显示label
         */
        $("#" + id + "Error").text("確認パスワードを入力してください。");
        showError($("#" + id + "Error"));
        return false;
    }
    /*
     * 2. 两次输入是否一致校验
     */
    if(value != $("#password").val()) {
        /*
         * 获取对应的label
         * 添加错误信息
         * 显示label
         */
        $("#" + id + "Error").text("入力されたパスワードが一致ではありません。");
        showError($("#" + id + "Error"));
        return false;
    }
    return true;
}

/*
 * Email校验方法
 */
function validateEmail() {
    const id = "email";
    const value = $("#" + id).val();//获取输入框内容
    /*
     * 1. 非空校验
     */
    if(!value) {
        /*
         * 获取对应的label
         * 添加错误信息
         * 显示label
         */
        $("#" + id + "Error").text("emailを入力してください。");
        showError($("#" + id + "Error"));
        return false;
    }
    /*
     * 2. Email格式校验
     */
    if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)) {
        /*
         * 获取对应的label
         * 添加错误信息
         * 显示label
         */
        $("#" + id + "Error").text("正しいメールアドレスの形式を入力してください。");
        showError($("#" + id + "Error"));
        false;
    }
    /*
     * 3. 是否注册校验
     */
    $.ajax({
        url:"femail",//要请求的servlet
        data:{method:"ajaxValidateEmail", email:value},//给服务器的参数
        type:"POST",
        dataType:"json",
        async:false,//是否异步请求，如果是异步，那么不会等服务器返回，我们这个函数就向下运行了。
        cache:false,
        success:function(result) {
            if(!result) {//如果校验失败
                $("#" + id + "Error").text("メールアドレスはすでに存在します。");
                showError($("#" + id + "Error"));
                return false;
            }
        }
    });
    return true;
}

/*
 * 判断当前元素是否存在内容，如果存在显示，不页面不显示！
 */
function showError(ele) {
    var text = ele.text();//获取元素的内容
    if(!text) {//如果没有内容
        ele.css("display", "none");//隐藏元素
    } else {//如果有内容
        ele.css("display", "");//显示元素
    }
}

