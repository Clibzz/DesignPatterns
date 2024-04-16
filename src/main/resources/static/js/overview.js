function loadBookDetails() {
    // Perform AJAX request
    let xhr = new XMLHttpRequest();
    let bookId = document.getElementById("bookId").getAttribute("value");
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Update DOM with response data

            } else {
                // Handle error
            }
        }
    };
    xhr.open("GET", "/bookDetails/" + bookId);
    xhr.send();
}