package fr.d2factory.libraryapp.member;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.d2factory.libraryapp.book.Book;

public class MemberRepository {
	
    private Map<Member, Set<Book>> listOfMemberBooks = new HashMap<>();
    
    /**
     * Add a book to a member
     * 
     * @param member
     * @param book
     */
   public void addBookToMember(Member member, Book book){
	   if (listOfMemberBooks.containsKey(member) ) {
	    	listOfMemberBooks.get(member).add(book);

	   }else {
		   listOfMemberBooks.put(member, new HashSet<Book>(Arrays.asList(book)));
	   }
    	
    }
    /**
     * Delete book to a member
     * 
     * @param member
     * @param book
     */
   public void deleteBookToMember(Member member, Book book){
    	
    	if (listOfMemberBooks.get(member).contains(book) ) {
    		listOfMemberBooks.get(member).remove(book);
    	}
    }
   /**
    * get list of books borrowed
    * 
    * @param member
    * @return
    */
    
   public Set<Book>  getListOfBooksBorrowed(Member member){
    	
    	
		return listOfMemberBooks.get(member);
    	
    }


}
