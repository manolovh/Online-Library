import { fetchBooks } from './book-search.js';
import { fetchMostPopularBooks, handleAddBook, fetchUserStatus, fetchAdminBooks, fetchTakenBooks } from './admin-ops.js';
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

    $(document).on('click', '.paginationLinkAdmin', function (e) {
      e.preventDefault();
      const page = $(this).data('page');
      fetchAdminBooks(page);
    });

    $('#viewStatistics').click(function () {
        fetchMostPopularBooks();
    });

    $('#viewUserStatus').click(function () {
        fetchUserStatus();
    });

    $('#manage').click(function () {
      currentPage = 0;
      fetchAdminBooks(currentPage);
    });

    $('#taken').click(function () {
      fetchTakenBooks();
    });

    $('#addBtn').click(function () {
        handleAddBook();
    });

    fetchBorrowedBooks();
});