window.viewTime = new Date()
window.id = ""
window.onload = function () {
    //用户点击详细信息按钮后记录此时事件
    $("[title='详细信息']").each(function (index, obj) {
        $(obj).on('click', function () {
            window.viewTime = new Date()
            window.id = $(obj).attr("id")
        })
    })
    //用户退出后计算经过秒数
    $("[title='退出详细信息']").each(function (index, obj) {
        $(obj).on('click', function () {
            let nowDate = new Date()
            let second = Math.floor((nowDate - window.viewTime)/1000)
            let url = "/info?phoneId=" + window.id + "&second=" +second
            console.log(url)
            console.log($.get(url))
        })
    })
}

//console.log($.get("/info?phoneId=" + $(obj).attr("id")))
function test_1() {
    console.log("test_1 发送消息完成！")
    $.get("/info?phoneId=" + 8)
    return 8
}