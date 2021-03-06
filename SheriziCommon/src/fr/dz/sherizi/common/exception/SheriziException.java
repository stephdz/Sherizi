package fr.dz.sherizi.common.exception;

public class SheriziException extends Exception {

	private static final long serialVersionUID = -757564899590745182L;

	/**
	 * Constructor
	 * @param message
	 */
	public SheriziException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param message
	 * @param t
	 */
	public SheriziException(String message, Throwable t) {
		super(message, t);
	}
}
