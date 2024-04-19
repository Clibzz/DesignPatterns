document.addEventListener("DOMContentLoaded", function() {
    function loadBookDetails() {
        let bookList = document.getElementsByClassName("bookCover");

        Array.prototype.forEach.call(bookList, function(chosenBook) {
            chosenBook.addEventListener('click', function() {
                let bookValue = chosenBook.getAttribute("value");
                window.location.href = "/bookDetails/" + bookValue;
            });
        });
    }

    loadBookDetails();
});