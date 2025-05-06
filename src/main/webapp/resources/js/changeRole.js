$(document).ready(function () {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    $.ajaxSetup({
        headers: {
            [header]: token
        }
    });
})

function doDefault(id) {
    const isConfirmed = confirm("Вы уверены, что хотите лишить пользователя всех прав?");
    if (isConfirmed) {
        $.ajax({
            url: "/admin/" + id + "/doUser",
            type: 'POST',
            success: function (response, status, xhr) {
                if (xhr.status === 200) {
                    alert(`Пользователь лишён всех прав!`);
                    location.reload(true)
                }
            },
            error: function (xhr, status, error) {
                try {
                    const response = JSON.parse(xhr.responseText);

                    if (xhr.status === 404) {
                        console.log("User not found");
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
function doModerator(id) {
    const isConfirmed = confirm("Вы уверены, что хотите дать пользователю права?");
    if (isConfirmed) {
        $.ajax({
            url: "/admin/" + id + "/doModerator",
            type: 'POST',
            success: function (response, status, xhr) {
                if (xhr.status === 200) {
                    alert(`Пользователь получил все права!`);
                    location.reload(true)
                }
            },
            error: function (xhr, status, error) {
                try {
                    const response = JSON.parse(xhr.responseText);

                    if (xhr.status === 404) {
                        console.log("User not found");
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