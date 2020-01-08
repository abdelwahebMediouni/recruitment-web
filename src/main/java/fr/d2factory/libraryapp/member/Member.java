package fr.d2factory.libraryapp.member;

import java.math.BigDecimal;

import fr.d2factory.libraryapp.library.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {
    /**
     * An initial sum of money the member has
     */
    private BigDecimal wallet;
	/**
	 * le delai maximum pour un adherent pour rendre un livre avant d'etre
	 * considerer comme en retard
	 **/
	protected int maxDaysKeepBook;

    public Member() {
		super();
	}

	public Member(BigDecimal wallet) {
		super();
		this.wallet = wallet;
	}

	public int getMaxDaysKeepBook() {
		return maxDaysKeepBook;
	}

	public void setMaxDaysKeepBook(int maxDaysKeepBook) {
		this.maxDaysKeepBook = maxDaysKeepBook;
	}

	/**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays);

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }
}
