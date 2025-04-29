function handleDelete(id) {
    const isConfirmed = confirm("Вы уверены, что хотите удалить этого сотрудника и лишить его всех прав?");

    if (isConfirmed) {
        alert(`Элемент с ID ${id} успешно удален!`);
        // Здесь можно добавить AJAX-запрос для отправки запроса на сервер
    }
}