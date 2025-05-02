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

function handleDelete(id) {
    const isConfirmed = confirm("Вы уверены, что хотите удалить этого сотрудника и лишить его всех прав?");
    if (isConfirmed) {
        const url = del.getAttribute("data-url");
        $.ajax({
            url: url + "/" + id,
            type: 'DELETE',
            success: function (response, status, xhr) {
                if (xhr.status === 200) {
                    alert(`Жалоба успешно удалена`);
                    location.reload(true)
                }
            },
            error: function (xhr, status, error) {
                try {
                    const response = JSON.parse(xhr.responseText);

                    if (xhr.status === 404) {
                        console.log("Report not found");
                    } else {
                        console.log("Неизвестная ошибка. Пожалуйста, обратитесь к администратору.");
                    }
                } catch (e) {
                    console.error("Ошибка парсинга JSON:", e);
                }
            }
        });
    }
}