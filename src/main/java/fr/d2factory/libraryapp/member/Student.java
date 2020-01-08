package fr.d2factory.libraryapp.member;

import java.math.BigDecimal;

import fr.d2factory.libraryapp.utils.Constants;
import fr.d2factory.libraryapp.utils.NotEnoughMoneyException;
import fr.d2factory.libraryapp.utils.NumberOfDaysFormatException;

public class Student extends Member {
	
	private Boolean firstYear;

	public Student() {
		super();
	}
	

	public Student(Boolean firstYear,BigDecimal wallet) {
		super(wallet);
		this.firstYear = firstYear;
		this.maxDaysKeepBook= Constants.MAX_DAYS_STUDENT_KEEP_BOOK;
	}


	public Boolean getFirstYear() {
		return firstYear;
	}

	public void setFirstYear(Boolean firstYear) {
		this.firstYear = firstYear;
	}

	@Override
	public void payBook(int numberOfDays) {
		if (numberOfDays > Constants.ZERO_VALUE) {
			
			
			int numberOfDaysToPay = getNumberOfdaysToPay(numberOfDays);

			BigDecimal sumToPay = Constants.DEFAULT_TARIF_ETUD.multiply(new BigDecimal(numberOfDaysToPay));

			/**
			 * si l'étudiant est en retard, il paye une penalité supplementaire par jour
			 * ,soit 10 centimes + 50 centimes de pénalité
			 **/
			if (isStudentLate(numberOfDays)) {

				sumToPay = sumToPay.add(Constants.DEFAULT_TARIF_ETUD_LATE
						.multiply(new BigDecimal(numberOfDays - Constants.MAX_DAYS_STUDENT_KEEP_BOOK)));
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

	private int getNumberOfdaysToPay(int numberOfDays) {
		if (this.getFirstYear()  ) {
			
			return numberOfDays > Constants.DAYS_FREE ? numberOfDays - Constants.DAYS_FREE : Constants.ZERO_VALUE;
			
		}else {
			return  numberOfDays;
		}
	}

	private Boolean isStudentLate(int numberOfDays) {
		if (numberOfDays > Constants.MAX_DAYS_STUDENT_KEEP_BOOK) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

}
