package hippieloader.hippieloader;

import java.io.File;
import java.util.Set;

import network.Interaction;
import network.PPINetwork;
import network.Protein;

/**
 * Hello world!
 *
 */
public class App {

	public static void loadDatabase(String uri, String username, String password, String filename) {
		PPINetwork network = Util.readAndParseFile(new File(filename));

		Set<Protein> proteins = network.getProteins();
		Set<Interaction> interactions = network.getInteractions();
		System.out.println("Nodes: " + proteins.size());
		System.out.println("Interactions: " + interactions.size());

		ProteinDatabase db = new ProteinDatabase(uri, username, password);
		db.clear();
		System.out.println("Database cleared.");
		db.addNetwork(network);
		System.out.println("Database written.");
		db.close();
		System.out.println("Connection closed.");
	}

	public static void main(String[] args) {
		 loadDatabase("bolt://hobby-akbjkdhmoeaggbkempcehbol.dbs.graphenedb.com:24786",
		 "java-testing2",
		 "b.0WGEt3TCSC2W.QxTpQ1ZWGTvszpmY", "src/main/hippie_current.txt");
	}
}
