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

function getCsrfToken() {
    const csrfMetaTag = document.querySelector('meta[name="_csrf"]');
    return csrfMetaTag ? csrfMetaTag.content : null;
}