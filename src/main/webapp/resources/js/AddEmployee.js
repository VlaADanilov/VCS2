$(document).ready(function () {
    $('#myForm').on('submit', function (event) {
        event.preventDefault();

        const url = $(this).data('url');
        const formData = {
            name: $('#name').val(),
            profession: $('#profession').val(),
            phone: $('#phone').val(),
            accountName: $('#accountName').val(),
            description: $('#description').val(),
        };

        $.ajax({
            url: url,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response, status, xhr) {
                $('#errors').empty();
                if (xhr.status === 201) {
                    window.location.href = url;
                }
            },
            error: function (xhr, status, error) {
                $('#errors').empty();

                try {
                    const response = JSON.parse(xhr.responseText);

                    if (xhr.status === 404) {
                        $('#errors').html("Ой, ой, ой, какие-то неполадки на сервере, попробуйте позже");
                    } else if (xhr.status === 400) {
                        if (response.violations && Array.isArray(response.violations)) {
                            const errorMessages = response.violations.map(violation =>
                                `${violation.fieldName.charAt(0).toUpperCase() + violation.fieldName.slice(1)}: ${violation.message}`
                            ).join('<br>');

                            $('#errors').html(errorMessages);
                        }
                    } else {
                        $('#errors').html("Неизвестная ошибка. Пожалуйста, обратитесь к администратору.");
                    }
                } catch (e) {
                    console.error("Ошибка парсинга JSON:", e);
                    $('#errors').html("Произошла ошибка. Пожалуйста, обратитесь к администратору.");
                }
            }
        });
    });
});