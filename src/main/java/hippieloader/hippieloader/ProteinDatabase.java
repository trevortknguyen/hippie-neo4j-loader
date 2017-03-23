package hippieloader.hippieloader;

import java.util.LinkedList;
import java.util.Queue;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import network.Interaction;
import network.PPINetwork;
import network.Protein;

public class ProteinDatabase {

	public Driver driver;


	public ProteinDatabase(String uri, String username, String password) {
		this.driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
	}

	/**
	 * First adds all of the the proteins and then the interactions.
	 * @param network
	 */
	public void addNetwork(PPINetwork network) {
		try (Session session = driver.session()) {
			try (Transaction tx = session.beginTransaction()) {
				for (Protein protein : network.getProteins()) {
					tx.run("MERGE ( {entrezId: {entrezId}, uniprotIds: {uniprotIds}})", protein.getProperties());
				}
				System.out.println("Loaded proteins.");
				tx.success();
				System.out.println("Success has been had.");
			}
		}
		
		Queue<Interaction> queue = new LinkedList<Interaction>(network.getInteractions());
		
		System.out.println("Loaded queue.");
		
		try (Session session = driver.session()) {
			while (!queue.isEmpty()) {
				try (Transaction tx = session.beginTransaction()) {
					int count = 0;
					while (count++ < 10000 && !queue.isEmpty()) {
						Interaction i = queue.poll();
						tx.run("MATCH (src), (dest) WHERE src.entrezId = {srcId} AND dest.entrezId = {destId} CREATE (src)-[:INTERACTS_WITH {strength: {strength}, experiments: {experiments}, pmids: {pmids}, species: {species}, sources: {sources}}]->(dest)",
								i.getProperties());
					}
					System.out.println("Loaded interactions. Remaining: " + queue.size());
					tx.success();
					System.out.println("Success has been had.");
				}
				System.out.println("Transaction has been closed.");
			}
		}
		System.out.println("Session has been closed.");
		
	}

	/**
	 * Removes all relationships and nodes. <br> MATCH (n) DETACH DELETE n
	 */
	public void clear() {
		try (Session session = driver.session()) {
			try (Transaction tx = session.beginTransaction()) {
				tx.run("MATCH (n) DETACH DELETE n");
				tx.success();
			}
		}
	}

	/**a
	 * Closes the connection with the driver.
	 */
	public void close() {
		driver.close();
	}
}
