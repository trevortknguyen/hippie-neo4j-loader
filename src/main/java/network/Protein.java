
package network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Protein {
	final public int entrezId;
	final public List<String> uniprotId;
	final private Map<String,Object> properties;
	
	public Protein(int entrezId, List<String> uniprotId) {
		this.entrezId = entrezId;
		this.uniprotId = uniprotId;
		this.properties = new HashMap<>();
		properties.put("entrezId", entrezId);
		properties.put("uniprotIds", uniprotId);
	}
	
	public Map<String,Object> getProperties() {
		return properties;
	}
	
	@Override
	public boolean equals(Object anObject) {
		Protein other = (Protein) anObject;
		return this.entrezId == other.entrezId;
	}
	
	@Override
	public int hashCode() {
		return this.entrezId;
	}
}