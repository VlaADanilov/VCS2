$(document).ready(function () {
    $('#registrationForm').on('submit', function (event) {
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
                if (xhr.status === 201) {
                    window.location.href = redirectUrl;
                }
            },
            error: function (xhr, status, error) {
                if (xhr.status === 400) {
                    const errors = JSON.parse(xhr.responseText);

                    // Очищаем предыдущие сообщения об ошибках
                    $('#errors').empty();

                    // Формируем текст ошибок
                    const errorMessages = errors.violations.map(violation =>
                        `${violation.fieldName.charAt(0).toUpperCase() + violation.fieldName.slice(1)}: ${violation.message}`
                    ).join('<br>');

                    // Выводим ошибки в div
                    $('#errors').html(errorMessages);
                } else {
                    alert("Ошибка регистрации: " + xhr.responseText);
                }
            }
        });
    });
});