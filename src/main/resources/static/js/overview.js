function loadBookDetails() {
    let bookList = document.getElementsByClassName("bookCover");

    Array.prototype.forEach.call(bookList, function(chosenBook) {
        window.location.href = "/bookDetails/" + chosenBook.getAttribute("value");
    });
}