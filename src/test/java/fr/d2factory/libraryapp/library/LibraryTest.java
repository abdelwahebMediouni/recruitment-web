package fr.d2factory.libraryapp.library;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.MemberRepository;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;
    private MemberRepository memberRepository;
    private static List<Book> books;


    @BeforeEach
    void setup() throws JsonParseException, JsonMappingException, IOException {
    	
    	
    	bookRepository = new BookRepository();
    	memberRepository = new MemberRepository();
    	 library = new LibraryImpl(bookRepository,memberRepository);
        ObjectMapper mapper = new ObjectMapper();
        File booksJson = new File("src/test/resources/books.json");
        
        JsonNode booksJsonNode = mapper.readTree(booksJson);
        books= new ArrayList<Book>();
        booksJsonNode.forEach(bookJsonNode -> {
        	 parseBookObject(bookJsonNode);
        	 
        });
        

        bookRepository.addBooks(books);
    }


	private void parseBookObject(JsonNode bookJsonNode) {
		String title = bookJsonNode.path("title").asText();
		 String author = bookJsonNode.path("author").asText();
		 Long isbnCode = bookJsonNode.path("isbn").path("isbnCode").asLong();
		 Book book = new Book(title, author, new ISBN(isbnCode));
		 books.add(book);

	}
    

    @Test
    void member_can_borrow_a_book_if_book_is_available(){
    	
    	
    	Student student = new Student(Boolean.TRUE,new BigDecimal ("50") );    
    	Book book = library.borrowBook(46578964513L, student, LocalDate.now());
        Assertions.assertNotNull(book);
    }

    @Test
    void borrowed_book_is_no_longer_available(){
    	Resident resident = new Resident(new BigDecimal ("80") );    
        library.borrowBook(46578964513L, resident, LocalDate.now());
    	
    	Student student = new Student(Boolean.TRUE,new BigDecimal ("20") );    
    	Assertions.assertThrows(HasBeenAlreadyBorrowedException.class, () -> {
    		library.borrowBook(46578964513L, student, LocalDate.now());
    		});
    }

    @Test
    void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){

    	Resident resident = new Resident(new BigDecimal ("85") );    

    	Book book = library.borrowBook(46578964513L, resident, LocalDate.of(2020, 1, 1) );
    	library.returnBook(book, resident);
    	
    	BigDecimal expected = new BigDecimal( "0.10").multiply(new BigDecimal((int)ChronoUnit.DAYS.between( LocalDate.of(2020, 1, 1), LocalDate.now())))  ;
    	Assertions.assertEquals(expected, new BigDecimal ("85").subtract( resident.getWallet())," value to pay does not match ( error calculating price");
    	

    }

    @Test
    void students_pay_10_cents_the_first_30days(){
    	
    	Student student = new Student(Boolean.FALSE,new BigDecimal ("20") );    

    	Book book = library.borrowBook(46578964513L, student, LocalDate.of(2020, 1, 1) );
    	library.returnBook(book, student);
    	
    	BigDecimal expected = new BigDecimal( "0.10").multiply(new BigDecimal((int)ChronoUnit.DAYS.between( LocalDate.of(2020, 1, 1), LocalDate.now())))  ;
    	Assertions.assertEquals(expected, new BigDecimal ("20").subtract( student.getWallet())," value to pay does not match ( error calculating price");
    	
    }

    @Test
    void students_in_1st_year_are_not_taxed_for_the_first_15days(){
    	
    	Student student = new Student(Boolean.TRUE,new BigDecimal ("20") );    

    	Book book = library.borrowBook(46578964513L, student, LocalDate.of(2019, 12, 15) );
    	library.returnBook(book, student);
    	
    	BigDecimal expected = new BigDecimal( "0.10").multiply(new BigDecimal((int)ChronoUnit.DAYS.between( LocalDate.of(2019, 12, 15), LocalDate.now()))).subtract(new BigDecimal( "1.5"))  ;
    	Assertions.assertEquals(expected, new BigDecimal ("20").subtract( student.getWallet())," value to pay does not match ( error calculating price");
    	
    	    }
    
    @Test
    void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){

    	Resident resident = new Resident(new BigDecimal ("85") );    

    	Book book = library.borrowBook(46578964513L, resident, LocalDate.of(2019, 11, 1) );
    	library.returnBook(book, resident);
    	
    	BigDecimal expected = new BigDecimal( "6").add(new BigDecimal( "0.20").multiply(new BigDecimal((int)ChronoUnit.DAYS.between( LocalDate.of(2019, 11, 1), LocalDate.now()) - 60)));
    	Assertions.assertEquals(expected, new BigDecimal ("85").subtract( resident.getWallet())," value to pay does not match ( error calculating price");
    	
    }

    @Test
    void members_cannot_borrow_book_if_they_have_late_books(){

    	Student student = new Student(Boolean.TRUE,new BigDecimal ("50") );    
    	library.borrowBook(46578964513L, student, LocalDate.of(2019, 10, 1) );

    	Assertions.assertThrows(HasLateBooksException.class, () -> {
    		library.borrowBook(968787565445L, student, LocalDate.now());
    	});

    	
    }
}
