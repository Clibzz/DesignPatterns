// Get the current URL
let url = window.location.href;

// Extract the book ID using a regular expression
let match = url.match(/\/bookDetails\/(\d+)/);

if (match) {
    let bookId = match[1];
    console.log("Book ID:", bookId);
} else {
    console.log("No book ID found in the URL.");
}