let url
$(document).ready(function () {
    url = del.getAttribute("data-url")
})

function handleDelete(id) {
    const isConfirmed = confirm("Вы уверены, что хотите удалить этого сотрудника и лишить его всех прав?");
    if (isConfirmed) {
        const url = del.getAttribute("data-url");
        $.ajax({
            url: url + "/" + id,
            type: 'DELETE',
            success: function (response, status, xhr) {
                if (xhr.status === 200) {
                    alert(`Сотрудник успешно удален и лишён всех прав!`);
                    window.location.href = url;
                }
            },
            error: function (xhr, status, error) {
                try {
                    const response = JSON.parse(xhr.responseText);

                    if (xhr.status === 404) {
                        console.log("Employee not found");
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

function showUploadDialog(employeeId) {
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
        uploadEmployeeImage(employeeId);
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

// Функция для загрузки изображения сотрудника
function uploadEmployeeImage(employeeId) {
    const form = document.getElementById('uploadForm');
    const formData = new FormData(form);
    const addIcon = document.querySelector('img[alt="Add image"]');
    const url = addIcon.getAttribute("data-url");

    fetch(url + "/" + employeeId, {
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

// Функция для удаления изображения сотрудника
function deleteEmployeeImage(employeeId, imageId) {
    if (confirm('Вы уверены, что хотите удалить это изображение?')) {
        fetch('/api/image/employee/' + employeeId + '/delete/' + imageId, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    alert('Изображение успешно удалено!');
                    location.reload(true);
                } else {
                    throw new Error('Ошибка удаления изображения');
                }
            })
            .catch(error => {
                alert('Ошибка: ' + error.message);
            });
    }
}