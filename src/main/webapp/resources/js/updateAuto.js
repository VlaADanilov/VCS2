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
        const method = $(this).data('method');
        const formData = {
            brand_id: $('#brand_id').val(),
            model: $('#model').val(),
            year: $('#year').val(),
            price: $('#price').val(),
            mileage: $('#mileage').val(),
            city: $('#city').val(),
            description: $('#description').val(),
            phone: $('#phone').val()
        };

        $.ajax({
            url: url,
            type: method,
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response, status, xhr) {
                $('#errors').empty();
                if (xhr.status === 200) {
                    window.location.href = url;
                } else {
                    if (xhr.status === 201) {
                        const id = xhr.responseText.replace(/^"|"$/g, '');
                        window.location.href = url + id;
                    }
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