package fr.dz.sherizi.server.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Various utils for Sherizi server-side
 */
public abstract class Utils {

	private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("transactions-optional");

	/**
	 * Returns the entity manager factory
	 * @return
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	/**
	 * Creates the entity manager
	 * @return
	 */
	public static EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}
}