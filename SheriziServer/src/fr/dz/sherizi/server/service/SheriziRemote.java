package fr.dz.sherizi.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.api.server.spi.config.Api;

import fr.dz.sherizi.common.push.PushMessage;
import fr.dz.sherizi.server.exception.SheriziException;
import fr.dz.sherizi.server.model.User;
import fr.dz.sherizi.server.push.PushUtils;
import fr.dz.sherizi.server.service.version.SheriziV1;
import fr.dz.sherizi.server.utils.Utils;

/**
 * Sherizi remote published service
 */
@Api(name = "sherizi", version="v1")
public class SheriziRemote implements SheriziV1 {

	private SheriziLocal local = new SheriziLocal();

	@Override
	public User saveOrUpdateUser(User user) throws SheriziException {
		EntityManager mgr = Utils.getEntityManager();
		try {
			mgr.persist(user);
		} catch(Throwable t) {
			throw new SheriziException("Unknown error during saveOrUpdate : "+t.getMessage());
		} finally {
			mgr.close();
		}
		return user;
	}

	@Override
	public User deleteUser(@Named("id") String id) throws SheriziException {
		EntityManager mgr = Utils.getEntityManager();
		User user = null;
		try {
			user = mgr.find(User.class, id);
			mgr.remove(user);
		} catch(Throwable t) {
			throw new SheriziException("Unknown error during deleteUser : "+t.getMessage());
		} finally {
			mgr.close();
		}
		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchFriends(@Named("googleAccounts") String googleAccounts) throws SheriziException {
		EntityManager mgr = Utils.getEntityManager();
		List<User> result = new ArrayList<User>();
		try {
			String[] splitted = googleAccounts != null && ! googleAccounts.isEmpty() ? googleAccounts.split(",") : null;
			List<String> googleAccountsList = splitted != null && splitted.length > 0 ? Arrays.asList(splitted) : null;
			if ( googleAccountsList != null ) {
				Query query = mgr.createQuery("select from "+User.class.getSimpleName()+" u "+
						"where u.googleAccount in (:googleAccounts)");
				query.setParameter("googleAccounts", googleAccountsList);
				for (Object obj : (List<Object>) query.getResultList()) {
					result.add((User) obj);
				}
			}
		} catch(Throwable t) {
			throw new SheriziException("Unknown error during searchFriends : "+t.getMessage());
		} finally {
			mgr.close();
		}
		return result;
	}

	@Override
	public void initiateTransfer(@Named("idFrom") String idFrom,
			@Named("idTo") String idTo,
			@Named("transferMode") String transferMode) throws SheriziException {

		// Gets the two users
		User from = local.getUser(idFrom);
		User to = local.getUser(idTo);

		// Sends a push notification to the targetted user
		PushMessage message = new PushMessage("initiateTransfer");
		message.addParameter("transferMode", transferMode);
		message.addParameter("sourceUser", from.toString());
		PushUtils.sendMessage(message, to);
	}
}
