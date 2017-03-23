package network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an interaction between two proteins. 
 * @author Trevor Thai Kim Nguyen
 *
 */
public class Interaction {
	/**
	 * This is the protein of the source node.
	 */
	final public Protein src;
	/**
	 * This is the protein of the destination node.
	 */
	final public Protein dest;
	/**
	 * This is the strength of the protein-protein interaction.
	 */
	final public double strength;
	
	final public List<String> experiments;
	final public List<Integer> pmids;
	final public List<String> species;
	final public List<String> sources;
	
	final private Map<String,Object> properties;

	public Interaction(Protein src, Protein dest, double strength, List<String> experiments, List<Integer> pmids, List<String> species, List<String> sources) {
		this.src = src;
		this.dest = dest;
		this.strength = strength;
		this.experiments = experiments;
		this.pmids = pmids;
		this.species = species;
		this.sources = sources;
		this.properties = new HashMap<>();
		properties.put("srcId", src.entrezId);
		properties.put("destId", dest.entrezId);
		properties.put("strength", strength);
		properties.put("experiments", experiments);
		properties.put("pmids", pmids);
		properties.put("species", species);
		properties.put("sources", sources);
	}
	
	
	
	public Map<String,Object> getProperties() {
		return properties;
	}
	
	@Override
	public boolean equals(Object anObject) {
		Interaction other = (Interaction) anObject;
		return src.equals(other.src) && dest.equals(other.dest);
	}
	
	@Override
	public int hashCode() {
		return src.hashCode() + dest.hashCode();
	}
}