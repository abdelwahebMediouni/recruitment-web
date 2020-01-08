package fr.d2factory.libraryapp.member;

import java.math.BigDecimal;

import fr.d2factory.libraryapp.utils.Constants;
import fr.d2factory.libraryapp.utils.NotEnoughMoneyException;
import fr.d2factory.libraryapp.utils.NumberOfDaysFormatException;

public class Resident extends Member {

	public Resident() {
		
	}


	public Resident(BigDecimal wallet) {
		super(wallet);
		this.maxDaysKeepBook= Constants.MAX_DAYS_STUDENT_KEEP_BOOK;

	}



	@Override
	public void payBook(int numberOfDays) {
		if (numberOfDays > Constants.ZERO_VALUE) {

			BigDecimal sumToPay = new BigDecimal("0");
			if (isStudentLate(numberOfDays)) {
				
				BigDecimal sumToPayForExtraDays = Constants.DEFAULT_TARIF_RESID_MAJORE.multiply(
						new BigDecimal(numberOfDays - Constants.MAX_DAYS_RESIDENT_KEEP_BOOK));
				
				 sumToPay = Constants.SUM_TO_PAY_SIXTY_DAYS
								.add(sumToPayForExtraDays);

			}else {
				sumToPay = Constants.DEFAULT_TARIF_RESID.multiply(new BigDecimal(numberOfDays));
			}

			if (sumToPay.compareTo(this.getWallet()) == 1) {
				throw new NotEnoughMoneyException("Not enough money to pay");

			} else {
				this.setWallet(this.getWallet().subtract(sumToPay));

			}
		} else {
			throw new NumberOfDaysFormatException("number of days must be greater than zero");
		}

	}

	private Boolean isStudentLate(int numberOfDays) {
		if (numberOfDays > Constants.MAX_DAYS_RESIDENT_KEEP_BOOK) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
