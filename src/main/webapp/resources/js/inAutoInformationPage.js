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