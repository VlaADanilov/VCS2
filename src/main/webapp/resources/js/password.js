$(document).ready(function () {
    const $stage1 = $('#stage1');
    const $stage2 = $('#stage2');
    const $emailInput = $('#email');
    const $codeInput = $('#code');
    const $newPasswordInput = $('#newPassword');
    const $errors = $('#errors');
    const url = document.getElementById("myForm").getAttribute("data-url");
    $('#sendCodeBtn').on('click', function () {
        const email = $emailInput.val().trim();

        if (!email) {
            showError("Введите email");
            return;
        }

        $.ajax({
            url: url,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: email }),
            success: function () {
                $stage1.hide();
                $stage2.show();
                hideError()
                $emailInput.prop('disabled', true);
            },
            error: function (xhr) {
                let message = "Ошибка при отправке кода.";
                if (xhr.status === 400) {
                    message = "Неверный формат email.";
                } else if (xhr.status === 404) {
                    message = "Пользователь с таким email не найден.";
                }
                showError(message);
            }
        });
    });

    $('#recoverBtn').on('click', function () {
        const email = $emailInput.val();
        const code = $codeInput.val().trim();
        const newPassword = $newPasswordInput.val().trim();

        if (!code || !newPassword) {
            showError("Заполните все поля");
            return;
        }

        $.ajax({
            url: url + "/recovery", // Укажите правильный endpoint
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                email: email,
                code: code,
                password: newPassword
            }),
            success: function () {
                window.location.href = "/login"; // или другая страница после успешного восстановления
            },
            error: function (xhr) {
                let message = "Ошибка при восстановлении пароля.";
                if (xhr.status === 400) {
                    message = "Неверный формат email или пароля.";
                } else if (xhr.status === 403) {
                    message = "Код не совпадает.";
                }
                showError(message);
            }
        });
    });

    function showError(message) {
        $errors.html(`<div class="error">${message}</div>`);
    }

    function hideError() {
        $errors.html(``);
    }
});