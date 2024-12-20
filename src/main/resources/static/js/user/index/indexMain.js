import { fetchBooks } from './book-search.js';
import { fetchBorrowedBooks } from './book-search.js';

document.addEventListener('DOMContentLoaded', () => {
    let currentPage = 0;

    $('#searchBtn').click(function () {
        const query = $('#query').val();
        currentPage = 0;
        fetchBooks(query, currentPage);
    });

    $(document).on('click', '.paginationLink', function (e) {
        e.preventDefault();
        const query = $('#query').val();
        const page = $(this).data('page');
        fetchBooks(query, page);
    });

    fetchBorrowedBooks();
});