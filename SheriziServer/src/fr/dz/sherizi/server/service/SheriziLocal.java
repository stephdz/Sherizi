package fr.dz.sherizi.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.server.model.User;
import fr.dz.sherizi.server.utils.Utils;

/**
 * Sherizi local service
 */
public class SheriziLocal {

	/**
	 * Gets all the users from the datastore
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public List<User> getAllUsers() throws SheriziException {
		EntityManager mgr = Utils.getEntityManager();
		List<User> result = new ArrayList<User>();
		try {
			Query query = mgr.createQuery("select from "+User.class.getSimpleName());
			for (Object obj : (List<Object>) query.getResultList()) {
				result.add((User) obj);
			}
		} catch(Throwable t) {
			throw new SheriziException("Unknown error during getAllUsers : "+t.getMessage());
		} finally {
			mgr.close();
		}
		return result;
	}

	/**
	 * Retrieves a user from the datastore
	 * @param email
	 * @param deviceName
	 * @return
	 */
	public User getUser(String email, String deviceName) throws SheriziException {
		EntityManager mgr = Utils.getEntityManager();
		User user = null;
		try {
			Query query = mgr.createQuery("select from "+User.class.getSimpleName()+" u "+
					"where u."+User.EMAIL_FIELD+" = :email "+
					"and u."+User.DEVICE_NAME_FIELD+" = :deviceName ");
			query.setParameter("email", email);
			query.setParameter("deviceName", deviceName);
			user = (User) query.getSingleResult();
		} catch(Throwable t) {
			throw new SheriziException("Unknown error during getUser : "+t.getMessage());
		} finally {
			mgr.close();
		}
		return user;
	}
}
