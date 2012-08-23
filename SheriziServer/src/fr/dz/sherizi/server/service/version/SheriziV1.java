package fr.dz.sherizi.server.service.version;

import java.util.List;

import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.server.model.User;

/**
 * Sherizi Service interface V1 to guarantee retrocompatibility
 */
public interface SheriziV1 {

	// Used transfer modes
	public static final String TRANSFER_MODE_BLUETOOTH = "BLUETOOTH";

	/**
	 * Inserts or updates the user in the datastore
	 * @param user
	 * @return
	 */
	public User saveOrUpdateUser(User user) throws SheriziException;

	/**
	 * Deletes the user from the datastore
	 * @param email
	 * @param deviceName
	 * @return
	 */
	public User deleteUser(String email, String deviceName) throws SheriziException;

	/**
	 * Searches for users within a list of emails
	 * @param googleAccounts The comma separated searched emails
	 * @return
	 */
	public List<User> searchFriends(String emails) throws SheriziException;

	/**
	 * Initiates the transfer between 2 users using the specified transfer mode
	 * @param emailsFrom Emails list of the user requesting sharing
	 * @param emailTo Email of the target user
	 * @param deviceTo Device of the target user
	 * @param transferInformations Transfer informations serialized into string
	 */
	public void initiateTransfer(String emailsFrom, String emailTo, String deviceTo, String transferInformations)
		throws SheriziException;
}