package fr.d2factory.libraryapp.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.MemberRepository;

public class LibraryImpl implements Library {
	
	private BookRepository bookRepository =new BookRepository();
	private MemberRepository memberRepository = new MemberRepository();
	
	

	public LibraryImpl(BookRepository bookRepository, MemberRepository memberRepository) {
		super();
		this.bookRepository = bookRepository;
		this.memberRepository = memberRepository;
	}

	public BookRepository getBookRepository() {
		return bookRepository;
	}

	public void setBookRepository(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public MemberRepository getMemberRepository() {
		return memberRepository;
	}

	public void setMemberRepository(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {

			
		Set<Book> listOfBookBorrowed = 	memberRepository.getListOfBooksBorrowed(member);
		if ( listOfBookBorrowed != null ) {
		listOfBookBorrowed.forEach(
			
			liv-> {
				
				if(ChronoUnit.DAYS.between(bookRepository.findBorrowedBookDate(liv), LocalDate.now()) > member.getMaxDaysKeepBook()) {
					throw new HasLateBooksException("l'adherent ne peut pas emprunté un autre livre car il a un livre en retard");
					
				}
			}
		 );
		}
			
		
		Book  book= bookRepository.findBook(isbnCode);
		if (book == null) {
			/** can not be borrowed  **/
			throw new HasBeenAlreadyBorrowedException("livre a dèja été emprunté à un autre adhérent ");
		}
		bookRepository.saveBookBorrow(book,borrowedAt);
		memberRepository.addBookToMember(member, book);
		return book;
	}

	@Override
	public void returnBook(Book book, Member member) {
		
		
		LocalDate dateBorrow = 	bookRepository.findBorrowedBookDate(book);
		member.payBook((int)ChronoUnit.DAYS.between(dateBorrow, LocalDate.now()));
		bookRepository.saveBookReturned(book);
		memberRepository.deleteBookToMember(member, book);
	}

}
