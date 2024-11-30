export function handleAddBook(event) {
    document.getElementById('addBookForm').addEventListener('submit', handleAddBook);

    event.preventDefault();

    const title = document.getElementById("title").value;
    const author = document.getElementById("author").value;
    const genre = document.getElementById("genre").value;
    const quantity = document.getElementById("quantity").value;
    const year = document.getElementById("year").value;

    const bookData = {
        title: title,
        author: author,
        genre: genre,
        availableCopies: quantity,
        year: year
    };

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    $.ajax({
        url: '/admin/books/add',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(bookData),
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: handleBookCreateResponse,
        error: handleBookAddError
    });

    document.getElementById("addBookForm").reset();
}

export function fetchMostPopularBooks(query, page) {
    $.ajax({
        url: '/admin/books/popular',
        method: 'GET',
        success: handleBookSearchResponse,
        error: handleBookSearchError
    });
}

// Handle the successful response for fetching books
function handleBookSearchResponse(response) {
    if (response.totalElements === 0) {
        showEmptyResponse();
        return;
    }
    displayBooks(response);
}

function handleBookCreateResponse(response) {
    alert("Book added successfully!")
}

// Display books in the table
function displayBooks(books) {
    $('#emptyResponse').hide();
    $('#viewStats').show();

    const tbody = $('#booksTable tbody');
    tbody.empty();

    books.forEach(book => {
        const row = createBookRow(book);
        tbody.append(row);
    });
}

// Create a table row for a book
function createBookRow(book) {
    return `
        <tr>
            <td>${book.bookName}</td>
            <td>${book.authorName}</td>
            <td>${book.takenCount}</td>
        </tr>
    `;
}

// Show the empty response message
function showEmptyResponse() {
    $('#viewStats').hide();
    $('#emptyResponse').show();
}

function handleBookSearchError(error) {
    console.error('Error fetching books:', error);
}

function handleBookAddError(error) {
    console.error('Error adding book:', error);
}