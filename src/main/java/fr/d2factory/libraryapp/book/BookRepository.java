package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
	private Map<ISBN, Book> availableBooks = new HashMap<>();
	private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

	public Map<ISBN, Book> getAvailableBooks() {
		return availableBooks;
	}

	public void setAvailableBooks(Map<ISBN, Book> availableBooks) {
		this.availableBooks = availableBooks;
	}

	public Map<Book, LocalDate> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(Map<Book, LocalDate> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}

	/**
	 * 
	 * @param books
	 */
	public void addBooks(List<Book> books) {
		final boolean atLeastOneBookAvailable = (books != null && !books.isEmpty());
		if (atLeastOneBookAvailable) {

			availableBooks.putAll(books.stream().collect(Collectors.toMap(Book::getIsbn, Function.identity())));
		}
	}

	/**
	 *  find book by isbnCode
	 *  
	 * @param isbnCode
	 * @return
	 */
	public Book findBook(long isbnCode) {

		for (Map.Entry<ISBN, Book> book : availableBooks.entrySet()) {
			if (new Long(book.getKey().getIsbnCode()).equals(new Long(isbnCode))) {
				return book.getValue();
			}

		}

		return null;

	}

	/**
	 * save a book that has benn borrowed
	 * 
	 * @param book
	 * @param borrowedAt
	 */

	public void saveBookBorrow(Book book, LocalDate borrowedAt) {
		if (book != null) {
			borrowedBooks.put(book, borrowedAt);
			availableBooks.remove(book.getIsbn());

		}
	}

	/**
	 * Save a book that has been returned
	 * 
	 * @param book
	 */
	public void saveBookReturned(Book book) {
		if (book != null) {
			borrowedBooks.remove(book);
			availableBooks.put(book.getIsbn(), book);

		}
	}

	/**
	 * 
	 * Find borrowed book date
	 * 
	 * @param book
	 * @return
	 */

	public LocalDate findBorrowedBookDate(Book book) {

		return book == null ? null : borrowedBooks.get(book);
	}
}
