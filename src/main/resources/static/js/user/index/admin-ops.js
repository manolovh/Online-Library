import {getCsrfToken} from "../../utils/csrf.js";

const editModal = document.getElementById("editModal");
const editForm = document.getElementById("editBookForm");
const bookTitleInput = document.getElementById("bookTitle");
const bookAuthorInput = document.getElementById("bookAuthor");
const bookGenreInput = document.getElementById("bookGenre");
const bookYearInput = document.getElementById("bookYear");
const bookAvailableCopies = document.getElementById("bookAvailableCopies");
const bookIdInput = document.getElementById("bookId");

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

export function fetchMostPopularBooks() {
    $.ajax({
        url: '/admin/books/popular',
        method: 'GET',
        success: handleBookSearchResponse,
        error: handleBookSearchError
    });
}

export function fetchTakenBooks() {
  $.ajax({
      url: '/admin/books/taken',
      method: 'GET',
      success: handleTakenBooksResponse,
      error: handleBookSearchError
  });
}

export function fetchUserStatus() {
    $.ajax({
        url: '/admin/userStats',
        method: 'GET',
        success: handleUserStatusesResponse,
        error: handleUserStatusesError
    });
}

export function fetchAdminBooks(page) {
  $.ajax({
      url: '/admin/books',
      method: 'GET',
      data: {
          page: page,
          size: 10
      },
      success: handleBookFetchResponse,
      error: handleBookSearchError
  });
}

function handleBookSearchResponse(response) {
    if (response.totalElements === 0) {
        showEmptyBookResponse();
        return;
    }
    displayBooks(response);
}

function handleTakenBooksResponse(response) {
  if (response.totalElements === 0) {
      showEmptyTakenBookResponse();
      return;
  }

  displayTakenBooks(response);
}

function handleBookFetchResponse(response) {
  if (response.totalElements === 0) {
      showEmptyBookResponse();
      return;
  }
  displayFetchedBooks(response.content);
  setupPagination(response.number, response.totalPages);
}

function handleUserStatusesResponse(response) {
  if (response.totalElements === 0) {
      showEmptyUserStatusesResponse();
      return;
  }
  displayUserStatuses(response);
}

function handleBookCreateResponse(response) {
    alert("Book added successfully!")
}

function displayBooks(books) {
    $('#emptyResponse').hide();
    $('#popularBooks').show();

    const tbody = $('#booksTable tbody');
    tbody.empty();

    books.forEach(book => {
        const row = createBookRow(book);
        tbody.append(row);
    });
}

function displayTakenBooks(books) {
  $('#emptyResponse').hide();
  $('#takenBooks').show();

  const tbody = $('#booksTable tbody');
  tbody.empty();

  books.forEach(book => {
      const row = createTakenBookRow(book);
      tbody.append(row);
  });
}

function displayFetchedBooks(books) {
  $('#emptyResponse').hide();
  $('#manageBooks').show();

  const tbody = $('#managedBooksTable tbody');
  tbody.empty();

  books.forEach(book => {
      const row = createFetchedBookRow(book);
      tbody.append(row);
  });

  attachManageEventHandlers();
}

function displayUserStatuses(userStatuses) {
    $('#emptyResponse').hide();
    $('#viewUserCount').show();

    document.getElementById("totalUsers").textContent = `Total Users: ${userStatuses.active + userStatuses.inactive}`;
    document.getElementById("activeUsers").textContent = `Active Users: ${userStatuses.active}`;
    document.getElementById("inactiveUsers").textContent = `Inactive Users: ${userStatuses.inactive}`;
}

function createBookRow(book) {
    return `
        <tr>
            <td>${book.bookName}</td>
            <td>${book.authorName}</td>
            <td>${book.takenCount}</td>
        </tr>
    `;
}

function createTakenBookRow(book) {
  return `
      <tr>
          <td>${book.title}</td>
          <td>${book.author}</td>
          <td>${book.takenFrom}</td>
          <td>${book.daysRemaining}</td>
      </tr>
  `;
}

function createFetchedBookRow(book) {
  return `
      <tr>
          <td>${book.title}</td>
          <td>${book.author}</td>
          <td>${book.year}</td>
          <td>${book.genre}</td>
          <td>${book.availableCopies}</td>
          <td class="button-table">
              <button data-book-id="${book.id}" class="button-edit" type="submit">Edit</button>
              <button data-book-id="${book.id}" class="button-delete" type="submit">Delete</button>
          </td>
      </tr>
  `;
}

function attachManageEventHandlers() {
  const deleteButtons = document.querySelectorAll('.button-delete');
  deleteButtons.forEach(button => {
      const bookId = button.getAttribute('data-book-id');

      button.addEventListener('click', () => {
          deleteBook(bookId);
      });
  });
    const editButtons = document.querySelectorAll('.button-edit');
    editButtons.forEach(button => {
        const bookId = button.getAttribute('data-book-id');

        button.addEventListener('click', () => {
            openEditModal(bookId);
        });
    });
}

async function openEditModal(bookId) {
    try {
        const bookDetails = await getBookById(bookId);

        if (bookDetails) {
            bookTitleInput.value = bookDetails.title || "";
            bookAuthorInput.value = bookDetails.author || "";
            bookGenreInput.value = bookDetails.genre || "";
            bookYearInput.value = bookDetails.year || "";
            bookAvailableCopies.value = bookDetails.availableCopies || "";
            bookIdInput.value = bookId;
            editModal.style.display = "block";
        } else {
            console.error(`Book details for ID ${bookId} not found.`);
        }
    } catch (error) {
        console.error(error);
    }
}

function closeEditModal() {
    editModal.style.display = "none";
}

function getBookById(bookId) {
    return fetch(`admin/books/${bookId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to find book");
            }
            return response.json();
        })
        .catch(error => {
            console.error("Error fetching book details:", error);
        });
}

function handleEditBook(event) {
    event.preventDefault();
    const bookId = bookIdInput.value;
    const updatedBook = {
        id: bookId,
        title: bookTitleInput.value,
        author: bookAuthorInput.value,
        genre: bookGenreInput.value,
        year: parseInt(bookYearInput.value),
        availableCopies: parseInt(bookAvailableCopies.value)
    };

    editBook(bookId, updatedBook)
        .then(() => {
            closeEditModal();
        })
        .catch(error => {
            console.error("Error updating book:", error);
        });
}

editForm.addEventListener("submit", (event) => {
    handleEditBook(event);
});

const closeModalButton = document.getElementById("closeModalButton");

closeModalButton.addEventListener("click", () => {
    closeEditModal();
});

function editBook(bookId, updatedBook) {
    const { csrfToken, csrfHeader } = getCsrfToken();

    return fetch(`admin/books/${bookId}`, {
        method: 'PUT',
        body: JSON.stringify(updatedBook),
        headers: {
            [csrfHeader]: csrfToken,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to update book");
            }
            fetchAdminBooks();
        });
}

function deleteBook(bookId) {
  const { csrfToken, csrfHeader } = getCsrfToken();

  return fetch(`admin/books/${bookId}`, {
      method: 'DELETE',
      headers: {
          [csrfHeader]: csrfToken,
      },
  })
      .then(response => {
          if (response.ok) {
              fetchAdminBooks();
          }
      })
      .catch(error => {
          console.error(error);
      });
}

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

function createPaginationLink(page, text, iconHtml) {
  return `
      <a href="#" class="paginationLinkAdmin" data-page="${page}">
          ${iconHtml} ${text}
      </a>
  `;
}

function showEmptyBookResponse() {
    $('#popularBooks').hide();
    $('#emptyResponse').show();
}

function showEmptyTakenBookResponse() {
  $('#takenBooks').hide();
  $('#emptyResponse').show();
}

function showEmptyUserStatusesResponse() {
  $('#viewUserCount').hide();
  $('#emptyResponse').show();
}

function handleBookSearchError(error) {
    console.error('Error fetching books:', error);
}

function handleBookAddError(error) {
    console.error('Error adding book:', error);
}

function handleUserStatusesError(error) {
  console.error('Error fetching user statuses:', error);
}