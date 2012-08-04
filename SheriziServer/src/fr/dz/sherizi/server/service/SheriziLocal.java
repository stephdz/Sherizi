package fr.dz.sherizi.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
	public List<User> getAllUsers() {
		EntityManager mgr = Utils.getEntityManager();
		List<User> result = new ArrayList<User>();
		try {
			Query query = mgr.createQuery("select from "+User.class.getSimpleName());
			for (Object obj : (List<Object>) query.getResultList()) {
				result.add((User) obj);
			}
		} finally {
			mgr.close();
		}
		return result;
	}

	/**
	 * Retrieves a user from the datastore
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		EntityManager mgr = Utils.getEntityManager();
		User user = null;
		try {
			user = mgr.find(User.class, id);
		} finally {
			mgr.close();
		}
		return user;
	}
}
