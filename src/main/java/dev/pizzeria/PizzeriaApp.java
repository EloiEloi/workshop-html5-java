package dev.pizzeria;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import dev.pizzeria.connection.ConnectionManager;
import dev.pizzeria.controller.clients.ClientController;
import dev.pizzeria.controller.pizzas.PizzaController;
import dev.pizzeria.exception.TechnicalException;

public class PizzeriaApp {

	public static void main(String[] args) throws Exception {

		Server server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		server.setConnectors(new Connector[] { connector });
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");

		DefaultServlet defaultServlet = new DefaultServlet();
		ServletHolder holderPwd = new ServletHolder("default", defaultServlet);
		holderPwd.setInitParameter("resourceBase", "./src/main/webapp/static");
		context.addServlet(holderPwd, "/*");

		// Les contrôleurs de l'application

		// ClientController prend la main pour les requêtes /clients
		context.addServlet(ClientController.class, "/clients");

		// ClientController prend la main pour les requêtes /clients
		context.addServlet(PizzaController.class, "/pizzas");

		server.setHandler(context);
		server.start();
		server.join();

		// recupération du driver sql pour la connection à la bdd
		try {
			Class.forName(ConnectionManager.getDriverName());
		} catch (ClassNotFoundException e) {
			throw new TechnicalException("Le driver JBDC n'a pas été trouvé.", e);
		}

	}
}
