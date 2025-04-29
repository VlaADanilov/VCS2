let url
$(document).ready(function () {
    url = del.getAttribute("data-url")
})

function handleDelete(id) {
    const isConfirmed = confirm("Вы уверены, что хотите удалить этого сотрудника и лишить его всех прав?");
    if (isConfirmed) {
        const url = del.getAttribute("data-url");
        alert(`Сотрудник успешно удален и лишён всех прав!`);
        $.ajax({
            url: url + "/" + id,
            type: 'DELETE',
            success: function (response, status, xhr) {
                if (xhr.status === 200) {
                    const id = xhr.responseText.replace(/^"|"$/g, '');
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