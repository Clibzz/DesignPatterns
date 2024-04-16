function loadBookDetails() {
    let bookId = document.getElementById("bookId").getAttribute("value");
    window.location.href = "/bookDetails/" + bookId;
}