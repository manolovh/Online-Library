import {fetchBorrowedBooks} from "./book-search.js";
import {getCsrfToken} from "../../utils/csrf.js";

export function returnBook(bookId) {
    const { csrfToken, csrfHeader } = getCsrfToken();

    return fetch(`/user/books/return/${bookId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken,
        },
    })
        .then(response => {
            if (response.ok) {
                fetchBorrowedBooks(); // Refresh the list of borrowed books
            }
        })
        .catch(error =>
            //should add some notification on the screen like toastr
            console.error(error)
        );
}

export function borrowBook(bookId) {
    const { csrfToken, csrfHeader } = getCsrfToken();

    return fetch(`/user/books/borrow/${bookId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken,
        },
    })
        .then(response => {
            if (response.ok) {
                fetchBorrowedBooks(); // Refresh the list of borrowed books
            }
        })
        .catch(error => {
            //should add some notification on the screen like toastr
            console.error(error);
        });
}

export function isEligibleForBorrowing(bookId) {
    return fetch(`/user/books/borrow/${bookId}/check`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json()) // Assuming the backend returns a boolean
        .catch(error => {
            console.error('Error checking if book is borrowed:', error);
            return false; // Default to false in case of an error
        });
}