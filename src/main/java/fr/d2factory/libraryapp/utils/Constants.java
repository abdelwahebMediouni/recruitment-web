package fr.d2factory.libraryapp.utils;

import java.math.BigDecimal;

public class Constants {

	/** valeur du tarif par defaut pour un etudiant **/
	public static final BigDecimal DEFAULT_TARIF_ETUD = new BigDecimal("0.10");
	
	/** valeur du tarif par defaut pour un resident **/
	public static final BigDecimal DEFAULT_TARIF_RESID = new BigDecimal("0.10");
	
	/** valeur du tarif pour un resident majoré **/
	public static final BigDecimal DEFAULT_TARIF_RESID_MAJORE = new BigDecimal("0.20");
	
	
	/** penalité de plus à payer pour chaque jour de retard **/
	public static final BigDecimal DEFAULT_TARIF_ETUD_LATE = new BigDecimal("0.50");
	
	/** la période de 15 jours gratuite pour étudiant **/
	public static final int DAYS_FREE = 15;
	/**
	 * le delai maximum pour un étudiant pour rendre un livre avant d'etre
	 * considerer comme en retard
	 **/
	public static final int MAX_DAYS_STUDENT_KEEP_BOOK = 30;
	/**
	 * le delai maximum pour un resident pour rendre un livre avant d'etre
	 * considerer comme en retard
	 **/
	public static final int MAX_DAYS_RESIDENT_KEEP_BOOK = 60;
	public static final int ZERO_VALUE = 0;
	public static final BigDecimal SUM_TO_PAY_SIXTY_DAYS = DEFAULT_TARIF_RESID.multiply(new BigDecimal(MAX_DAYS_RESIDENT_KEEP_BOOK));


}
