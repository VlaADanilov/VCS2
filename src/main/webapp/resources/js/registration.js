$(document).ready(function () {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    $.ajaxSetup({
        headers: {
            [header]: token
        }
    });
    $('#myForm').on('submit', function (event) {
        event.preventDefault();

        const url = $(this).data('url');
        const redirectUrl = $(this).data('redirect');
        const formData = {
            username: $('#username').val(),
            email: $('#email').val(),
            password: $('#password').val(),
        };

        if (formData.password !== $('#confirm').val()) {
            alert("Пароли не совпадают!");
            return;
        }

        $.ajax({
            url: url,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response, status, xhr) {
                $('#errors').empty();
                $('#success').empty();
                if (xhr.status === 201) {
                    $('#success').html("success");
                }
            },
            error: function (xhr, status, error) {
                $('#errors').empty();
                $('#success').empty();

                try {


                    if (xhr.status === 400) {
                        try{const response = JSON.parse(xhr.responseText);
                        if (response.violations && Array.isArray(response.violations)) {
                            // Формируем текст ошибок из массива violations
                            const errorMessages = response.violations.map(violation =>
                                `${violation.fieldName.charAt(0).toUpperCase() + violation.fieldName.slice(1)}: ${violation.message}`
                            ).join('<br>');

                            $('#errors').html(errorMessages);
                        } else if (response.message) {
                            $('#errors').html(response.message);
                        } else {
                            // Неизвестная структура JSON
                            $('#errors').html("Неизвестная ошибка. Пожалуйста, попробуйте снова.");
                        }
                        } catch (e) {
                            $('#errors').html("Ошибка регистрации: " + xhr.responseText);
                        }
                    } else {
                        // Общая ошибка для других статусов
                        $('#errors').html("Ошибка регистрации: " + xhr.responseText);
                    }
                } catch (e) {
                    // Если JSON не удалось распарсить
                    console.error("Ошибка парсинга JSON:", e);
                    $('#errors').html("Произошла ошибка. Пожалуйста, обратитесь к администратору.");
                }
            }
        });
    });
});