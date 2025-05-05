document.addEventListener("DOMContentLoaded", function () {
    const deleteIcon = document.getElementById("delete_auto");

    if (deleteIcon) {
        deleteIcon.addEventListener("click", function () {
            const url = deleteIcon.getAttribute("data-url");
            const backUrl = deleteIcon.getAttribute("data-back-url");

            const isConfirmed = confirm("Вы уверены, что хотите удалить это объявление?");

            if (isConfirmed) {
                fetch(url, {
                    method: "DELETE",
                    headers: {
                        "Content-Type": "application/json",
                        "X-CSRF-TOKEN": getCsrfToken() // Если используется CSRF-защита
                    }
                })
                    .then(response => {
                        if (response.ok) {
                            window.location.href = backUrl;
                        } else {
                            throw new Error("Ошибка при удалении объявления");
                        }
                    })
            }
        });
    }
});

function showUploadDialog(autoId) {
    const modal = document.createElement('div');
    modal.style.position = 'fixed';
    modal.style.top = '0';
    modal.style.left = '0';
    modal.style.width = '100%';
    modal.style.height = '100%';
    modal.style.backgroundColor = 'rgba(0,0,0,0.5)';
    modal.style.display = 'flex';
    modal.style.justifyContent = 'center';
    modal.style.alignItems = 'center';
    modal.style.zIndex = '1000';

    const formContainer = document.createElement('div');
    formContainer.style.backgroundColor = 'white';
    formContainer.style.padding = '20px';
    formContainer.style.borderRadius = '8px';

    const form = document.createElement('form');
    form.id = 'uploadForm';
    form.enctype = 'multipart/form-data';

    // Поле для выбора файла
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.name = 'file';
    fileInput.accept = 'image/*';
    fileInput.required = true;

    const submitButton = document.createElement('button');
    submitButton.type = 'button';
    submitButton.textContent = 'Загрузить';
    submitButton.onclick = function() {
        uploadImage(autoId);
    };

    const closeButton = document.createElement('button');
    closeButton.textContent = 'Закрыть';
    closeButton.onclick = function() {
        document.body.removeChild(modal);
    };

    form.appendChild(fileInput);
    form.appendChild(document.createElement('br'));
    form.appendChild(submitButton);
    form.appendChild(closeButton);
    formContainer.appendChild(form);
    modal.appendChild(formContainer);
    document.body.appendChild(modal);
}

function uploadImage(autoId) {
    const form = document.getElementById('uploadForm');
    const formData = new FormData(form);
    const addIcon = document.getElementById("add_image");
    const url = addIcon.getAttribute("data-url");
    fetch(url + "/" + autoId, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка загрузки');
            }
            return response.text();
        })
        .then(data => {
            const modal = document.querySelector('div[style*="rgba(0,0,0,0.5)"]');
            if (modal) {
                document.body.removeChild(modal);
            }
            alert('Изображение успешно загружено!');
            location.reload(true);
        })
        .catch(error => {
            alert('Ошибка: ' + error.message);
        });
}

function getCsrfToken() {
    const csrfMetaTag = document.querySelector('meta[name="_csrf"]');
    return csrfMetaTag ? csrfMetaTag.content : null;
}

function addLike(autoId) {
    const heart = document.getElementById("heart_block");
    const url = heart.getAttribute("data-url");
    fetch(url + "/" + autoId, {
        method: 'POST',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка сервера, повторите попытку позже');
            }
            return response.text();
        })
        .then(data => {
            alert('Добавлено в избранное!');
            location.reload(true);
        })
        .catch(error => {
            alert('Ошибка: ' + error.message);
        });
}

function deleteLike(autoId) {
    const heart = document.getElementById("heart_block");
    const url = heart.getAttribute("data-url");
    fetch(url + "/" + autoId, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка сервера, повторите попытку позже');
            }
            return response.text();
        })
        .then(data => {
            alert('Удалено из избранного!');
            location.reload(true);
        })
        .catch(error => {
            alert('Ошибка: ' + error.message);
        });
}

function addReport(autoId) {
    const form = document.createElement('div');
    form.innerHTML = `
        <div style="position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: white; padding: 20px; border: 1px solid #ccc; z-index: 1000;">
            <h3>Оставить жалобу</h3>
            <textarea id="reportText" rows="4" cols="40" placeholder="Введите текст жалобы..."></textarea><br>
            <button id="sendReportBtn">Отправить жалобу</button>
            <button id="closeReportBtn">Закрыть</button>
        </div>
        <div style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 999;"></div>
    `;

    document.body.appendChild(form);

    document.getElementById('closeReportBtn').addEventListener('click', () => {
        document.body.removeChild(form);
    });


    document.getElementById('sendReportBtn').addEventListener('click', () => {
        const text = document.getElementById('reportText').value.trim();
        if (!text) {
            alert("Пожалуйста, введите текст жалобы.");
            return;
        }
        const report_icon = document.getElementById("report_icon");
        const url = report_icon.getAttribute("data-url");
        const formData = {
            comment: text
        };
        // Отправка AJAX-запроса
        $.ajax({
            url: url + '/' + autoId, // замените на реальный URL
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(response) {
                alert("Жалоба успешно отправлена!");
                document.body.removeChild(form);
            },
            error: function(xhr, status, error) {
                alert("Ошибка при отправке жалобы.");
                if (xhr.status === 400) {
                    if (response.violations && Array.isArray(response.violations)) {
                        const errorMessages = response.violations.map(violation =>
                            `${violation.fieldName.charAt(0).toUpperCase() + violation.fieldName.slice(1)}: ${violation.message}`
                        ).join('<br>');

                        alert(errorMessages)
                    }
                }
                else {
                    $('#errors').html("Неизвестная ошибка. Пожалуйста, обратитесь к администратору.");
                }
            }
        });
    });
}

function showCurrencyForm() {
    const form = document.getElementById('currency-form');
    if (form.style.display === 'none') {
        loadCurrencies(form.getAttribute("data-url"));
        form.style.display = 'block';
    }
}

function loadCurrencies(url) {
    $.ajax({
        url: url,
        method: 'GET',
        success: function(currencies) {
            const select = document.getElementById('currency');
            select.innerHTML = ''; // очищаем старые опции
            currencies.forEach(currency => {
                const option = document.createElement('option');
                option.value = currency;
                option.text = currency;
                select.appendChild(option);
            });
        },
        error: function() {
            alert('Не удалось загрузить валюты.');
        }
    });
}

function convertToCurrency() {
    const price = document.getElementById('price').textContent;
    const currency = document.getElementById('currency').value;
    const amount = parseFloat(price);
    const form = document.getElementById('currency-form');
    const url = form.getAttribute("data-url");
    if (!currency || isNaN(amount)) {
        alert('Заполните все поля');
        return;
    }

    $.ajax({
        url: url + '/toOtherCurrency',
        method: 'GET',
        data: {
            currency: currency,
            amount: amount
        },
        success: function(result) {
            const formattedResult = result.toFixed(2);
            document.getElementById('converted-price').innerHTML =
                `<strong>${formattedResult}</strong> ${currency}`;
        },
        error: function(xhr) {
            alert('Ошибка при переводе: ' + (xhr.responseText || 'Неизвестная ошибка'));
        }
    });
}

function closeForm() {
    const form = document.getElementById('currency-form');
    form.style.display = 'none';
}