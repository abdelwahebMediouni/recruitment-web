package fr.d2factory.libraryapp.library;

public class HasBeenAlreadyBorrowedException extends RuntimeException {
	
	HasBeenAlreadyBorrowedException(String error){
		super(error);
	}

}
