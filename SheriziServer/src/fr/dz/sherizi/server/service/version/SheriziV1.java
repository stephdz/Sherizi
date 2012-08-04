package fr.dz.sherizi.server.service.version;

import java.util.List;

import fr.dz.sherizi.server.exception.SheriziException;
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
	 * @param id
	 * @return
	 */
	public User deleteUser(String id) throws SheriziException;

	/**
	 * Searches for users within a list of google accounts
	 * @param googleAccounts The comma separated searched google accounts
	 * @return
	 */
	public List<User> searchFriends(String googleAccounts) throws SheriziException;

	/**
	 * Initiates the transfer between 2 users using the specified transfer mode
	 * @param idFrom
	 * @param idTo
	 * @param transferMode
	 */
	public void initiateTransfer(String idFrom, String idTo, String transferMode)
		throws SheriziException;
}