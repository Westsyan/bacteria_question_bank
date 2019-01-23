function rebackHome() {
    var url = window.location.href;
    if (String(url).indexOf("frontEnd") != -1) {
        swal({
            title: "",
            text: "确认放弃答题回到首页？",
            type: "warning",
            showCancelButton: true,
            showConfirmButton: true,
            confirmButtonClass: "btn-danger",
            confirmButtonText: "确定",
            closeOnConfirm: true,
            cancelButtonText: "取消"
        }, function () {
            window.location.replace("/")
        })
    } else {
        window.location.replace("/")
    }


}
