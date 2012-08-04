package fr.dz.sherizi.server.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.dz.sherizi.common.push.PushMessage;
import fr.dz.sherizi.server.exception.SheriziException;
import fr.dz.sherizi.server.push.PushUtils;

/**
 * Test servlet
 */
@SuppressWarnings("serial")
public class TestAppEngineServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		try {
			PushMessage message = new PushMessage("notification");
			message.addParameter("message", "Quelqu'un vient d'aller sur la page Web !!!");
			PushUtils.sendMessage(message);
		} catch ( SheriziException e ) {
			resp.getWriter().println("Une erreur s'est produite : ");
			e.printStackTrace(resp.getWriter());
		}
	}
}
