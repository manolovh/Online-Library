import {borrowBook, returnBook, isEligibleForBorrowing} from "./borrow-return.js";

export function fetchBooks(query, page) {
    $.ajax({
        url: '/user/books/search',
        method: 'GET',
        data: {
            query: query,
            page: page,
            size: 10
        },
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

    displayBooks(response.content);
    setupPagination(response.number, response.totalPages);
}

// Display books in the table
function displayBooks(books) {
    $('#emptyResponse').hide();
    $('#booksTableContainer').show();

    const tbody = $('#booksTable tbody');
    tbody.empty(); // Clear existing rows

    books.forEach(book => {
        if (book.availableCopies > 0) {
            const row = createBookRow(book);
            tbody.append(row);
        }
    });

    attachBorrowEventHandlers();
}

// Create a table row for a book
function createBookRow(book) {
    return `
        <tr>
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td class="borrow-button-table">
                <button data-book-id="${book.id}" class="button-borrow" type="submit">Borrow</button>
            </td>
        </tr>
    `;
}

// Attach click event handlers to borrow buttons
function attachBorrowEventHandlers() {
    const borrowButtons = document.querySelectorAll('.button-borrow');
    borrowButtons.forEach(button => {
        const bookId = button.getAttribute('data-book-id');

        // Check if the book is already borrowed
        isEligibleForBorrowing(bookId).then(isEligible => {
            if (isEligible) {
                button.textContent = 'Borrow'; // Change button to "Return" if borrowed
                button.classList.add('borrow-button'); // Optional: Add specific class for styling
            } else {
                button.textContent = 'Return'; // Default to "Borrow"
                button.classList.remove('borrow-button'); // Optional: Remove return class if it exists
            }

            // Attach click event based on the current state
            button.addEventListener('click', () => {
                if (button.textContent === 'Borrow') {
                    borrowBook(bookId).then(() => {
                        button.textContent = 'Return'; // Change to "Return" after successful borrow
                        button.classList.add('button-return');
                    });
                } else if (button.textContent === 'Return') {
                    returnBook(bookId).then(() => {
                        button.textContent = 'Borrow'; // Change to "Borrow" after successful return
                        button.classList.remove('button-return');
                    });
                }
            });
        }).catch(error => console.error('Error checking book status:', error));
    });
}

// Set up pagination controls
function setupPagination(currentPage, totalPages) {
    const paginationControls = $('#paginationControls');
    paginationControls.empty();

    if (currentPage > 0) {
        paginationControls.append(createPaginationLink(currentPage - 1, 'Previous', '<i class="fas fa-chevron-left"></i>'));
    }

    if (currentPage < totalPages - 1) {
        paginationControls.append(createPaginationLink(currentPage + 1, 'Next', '<i class="fas fa-chevron-right"></i>'));
    }
}

// Create a pagination link
function createPaginationLink(page, text, iconHtml) {
    return `
        <a href="#" class="paginationLink" data-page="${page}">
            ${iconHtml} ${text}
        </a>
    `;
}

// Show the empty response message
function showEmptyResponse() {
    $('#booksTableContainer').hide();
    $('#emptyResponse').show();
}

function handleBookSearchError(error) {
    console.error('Error fetching books:', error);
}

export function fetchBorrowedBooks() {
    fetch('/user/books/getAllBorrowed')
        .then(response => response.json())
        .then(books => {
            const container = document.getElementById('borrowed-books-container');
            container.innerHTML = ''; // Clear the container before appending new data

            books.forEach(book => {
                const bookCard = document.createElement('div');
                bookCard.className = 'borrowed-book-card';
                bookCard.innerHTML = `
                    <h3>${book.title}</h3>
                    <p>Author: ${book.author}</p>
                    <p>Taken At: ${new Date(book.takenAt).toLocaleString()}</p>
                    <button data-book-id="${book.id}" class="button-return">Return</button>
                `;

                container.appendChild(bookCard);
            });

            // Attach event listeners to the return buttons
            const returnButtons = document.querySelectorAll('.button-return');
            returnButtons.forEach(button => {
                button.addEventListener('click', () => {
                    const bookId = button.getAttribute('data-book-id');
                    returnBook(bookId);
                });
            });
        })
        .catch(error => console.error('Error fetching borrowed books:', error));
}
