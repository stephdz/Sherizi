package fr.dz.sherizi.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.api.server.spi.config.Api;

import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.common.push.PushMessage;
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
			user.updateKey();
			mgr.persist(user);
		} catch(Throwable t) {
			throw new SheriziException("Unknown error during saveOrUpdate : "+t.getMessage());
		} finally {
			mgr.close();
		}
		return user;
	}

	@Override
	public User deleteUser(@Named("email") String email, @Named("deviceName") String deviceName) throws SheriziException {
		EntityManager mgr = Utils.getEntityManager();
		User user = null;
		try {
			user = local.getUser(email, deviceName);
			if ( user != null ) {
				mgr.remove(user);
			}
		} catch(Throwable t) {
			throw new SheriziException("Unknown error during deleteUser : "+t.getMessage());
		} finally {
			mgr.close();
		}
		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchFriends(@Named("emails") String emails) throws SheriziException {
		EntityManager mgr = Utils.getEntityManager();
		List<User> result = new ArrayList<User>();
		try {
			String[] splitted = emails != null && ! emails.isEmpty() ? emails.split(",") : null;
			List<String> emailsList = splitted != null && splitted.length > 0 ? Arrays.asList(splitted) : null;
			if ( emailsList != null ) {
				Query query = mgr.createQuery("select from "+User.class.getSimpleName()+" u "+
						"where u."+User.EMAIL_FIELD+" in (:emails)");
				query.setParameter("emails", emailsList);
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
	public void initiateTransfer(@Named("emailsFrom") String emailsFrom,
			@Named("emailTo") String emailTo, @Named("deviceTo") String deviceTo,
			@Named("transferInformations") String transferInformations) throws SheriziException {

		// Gets the target user
		User to = local.getUser(emailTo, deviceTo);

		// Sends a push notification to the targetted user
		PushMessage message = new PushMessage("initiateTransfer");
		message.addParameter("transferInformations", transferInformations);
		message.addParameter("sourceUser", emailsFrom);
		PushUtils.sendMessage(message, to);
	}
}
