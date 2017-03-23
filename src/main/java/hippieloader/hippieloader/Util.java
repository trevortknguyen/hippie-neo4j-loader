package hippieloader.hippieloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import network.Interaction;
import network.PPINetwork;
import network.Protein;

public class Util {
	public static void generateJSFile(Set<Protein> ps, Set<Interaction> rs) {
		StringBuilder sb = new StringBuilder();
		sb.append("var sampleGraph = {");
		
		sb.append("nodes: [");
		
		for (Protein p : ps) {
			sb.append("{");
			sb.append("id: \"" + p.entrezId + "\", ");
//			sb.append("label: \"" + p.uniprotId + "\", ");
			sb.append("x: " + (int) (Math.random() * 100) + ", ");
			sb.append("y: " + (int) (Math.random() * 100) + ", ");
			sb.append("size: " + 1 + ", ");
			
			sb.append("},");
		}
		
		sb.append("],");
		
		sb.append("edges: [");
		
		int counter = 0;
		
		for (Interaction r : rs) {
			sb.append("{");
			sb.append("id: \"" + counter++ + "\", ");
			sb.append("source: \"" + r.src + "\", ");
			sb.append("target: \"" + r.dest + "\", ");
			sb.append("},");
		}
		
		sb.append("],");
		
		sb.append("};");
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/graph.js"))) {
			writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static PPINetwork readAndParseFile(File file) {
		Set<Protein> proteins = new HashSet<>();
		Set<Interaction> interactions = new HashSet<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))){
			
			String line;
			while ((line = reader.readLine()) != null) {
				parseLine(line, proteins, interactions);
			}
			System.out.println("File parsed.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new PPINetwork() {
			@Override
			public Set<Protein> getProteins() {
				return proteins;
			}

			@Override
			public Set<Interaction> getInteractions() {
				return interactions;
			}
		};
	}
	
	public static void parseLine(String line, Set<Protein> proteins, Set<Interaction> interactions) {
		String[] tokens = line.split("\t");
		
		List<String> srcUniprotIds = Arrays.asList(tokens[0].split(","));
		int srcEntrezId = Integer.parseInt(tokens[1]);
		List<String> destUniprotIds = Arrays.asList(tokens[2].split(","));
		int destEntrezId = Integer.parseInt(tokens[3]);
		double strength = Double.parseDouble(tokens[4]);
		
		List<String> experiments = null;
		List<Integer> pmids = null;
		List<String> species = null;
		List<String> sources = null;
		
		if (tokens.length >= 6) {			
			String parameters = tokens[5];
			String[] splits = parameters.split(";");
			for (String s : splits) {
				String param = s.substring(0, s.indexOf(':'));
				String contents = s.substring(s.indexOf(":") + 1);
				if (param.equals("experiments")) {
					experiments = Arrays.asList(contents.split(","));
				} else if (param.equals("pmids")) {
					pmids = Arrays.asList(contents.split(",")).stream().map((str) -> {
						return Integer.parseInt(str);
					}).collect(Collectors.toList());
				} else if (param.equals("sources")) {
					sources = Arrays.asList(contents.split(","));
				} else if (param.equals("species")) {
					species = Arrays.asList(contents.split(","));
				} else {
					throw new RuntimeException("Unexpected interaction parameter.s");
				}
			}
		}
		
		Protein src = new Protein(srcEntrezId, srcUniprotIds);
		Protein dest = new Protein(destEntrezId, destUniprotIds);
		
		proteins.add(src);
		proteins.add(dest);
		
		Interaction interaction = new Interaction(src, dest, strength, experiments, pmids, species, sources);
		interactions.add(interaction);
	}
}
