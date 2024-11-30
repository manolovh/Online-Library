import { fetchBooks } from './book-search.js';
import { fetchBorrowedBooks } from './book-search.js';

document.addEventListener('DOMContentLoaded', () => {
    let currentPage = 0;

    // Trigger search when the search button is clicked
    $('#searchBtn').click(function () {
        const query = $('#query').val(); // Get the search query
        currentPage = 0; // Reset to the first page for a new search
        fetchBooks(query, currentPage);
    });

    // Handle pagination clicks
    $(document).on('click', '.paginationLink', function (e) {
        e.preventDefault();
        const query = $('#query').val(); // Get the current search query
        const page = $(this).data('page'); // Get the page number
        fetchBooks(query, page); // Fetch books for the selected page
    });

    // Fetch borrowed books on page load
    fetchBorrowedBooks();
});