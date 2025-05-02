function going() {
    const goingButton = document.getElementById("goingButton");
    const url = goingButton.getAttribute("data-url");
    const goingUrl = goingButton.getAttribute("data-going");
    const autoId = goingButton.getAttribute("data-auto")
    const reportId = goingButton.getAttribute("data-rep")
    $.ajax({
        url: url + '/addView/' + reportId,
        method: 'POST',
        success:
            function(response) {
                window.location.href= (goingUrl + '/' + autoId + '?referer=/report')
            },
        error: function(xhr, status, error) {
            alert(error)
            alert("Ошибка, повторите попытку позже");
        }
    });
}