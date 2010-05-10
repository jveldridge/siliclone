package edu.brown.cs32.siliclone.database.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.dna.features.Feature;

@RemoteServiceRelativePath("sequence")
public interface SequenceService extends RemoteService {

	public String getNucleotides(SequenceHook seq) throws DataServiceException;
	
	/**
	 * Returns a String representing the nucleotides that make up this sequence.
	 * The String will consist of characters A,C,G,T for nucleotides adenine,
	 * cytosine, guanine, and tyrosine, respectively, and B,D,H,S for the methylated
	 * versions of these nucleotides.
	 * >>> Can we have things like X for unknown?
	 * 
	 * @return a String representing the nucleotides that make up this sequence.
	 */
	//public NucleotideString getSequence(SequenceHook seq) throws DataServiceException;
	
	/**
	 * Returns a Collection of all Features of a given type present in the sequence.
	 * If there are no such features, null will be returned.
	 * 
	 * @param featureType a String indicating the type of features being requested
	 * @return a Collection of all Features of a given type present in the sequence.
	 */
	public Collection<Feature> getFeaturesOfType(SequenceHook seq, String featureType) throws DataServiceException;
	
	/**
	 * Adds a feature to the Collection of features of this sequence.
	 * The type of the feature may not be null; if null is passed as the
	 * featureType parameter, this method will have no affect.
	 * 
	 * @param toAdd the feature that should be added to this collection
	 * @param type a String representing the type of the feature to be added
	 */
	public void addFeature(SequenceHook seq, Feature toAdd) throws DataServiceException;
	
	
	// >>>do we want to have a method to get a single Collection or List with all features?
	
	/**
	 * Returns the length of this DNASequence; that is, the number of nucleotides
	 * in the sequence.  The length is guaranteed to be a non-negative integer.
	 * >>> What if have jagged ends such that the two strands have different lengths?
	 * 
	 * @return the length of this DNASequence
	 */
	public int length(SequenceHook seq) throws DataServiceException;
	
	/**
	 * Adds information about a particular property of this sequence.  The key
	 * should be a String specifying the name of the property; this same name will
	 * be used to request the property in the future.  The value can be any Object,
	 * including null; it is the responsibility of any plugins using properties
	 * stored here to handle their values appropriately.
	 * 
	 * @param key a String representing the name of the property being added
	 * @param value	the value of the property being added
	 */
	public void addProperty(SequenceHook seq, String key, IsSerializable value) throws DataServiceException;
	
	public void addProperty(SequenceHook seq, String key, String value) throws DataServiceException;
	
	public void addProperty(SequenceHook seq, String key, Boolean value) throws DataServiceException;
	
	public void addProperty(SequenceHook seq, String key, Integer value) throws DataServiceException;
	
	/**
	 * Returns information about a particular property of this sequence.
	 * If this property has not previously been set, for example, by using
	 * the addProperty(...) method, null will be returned.
	 * 
	 * @param key a String representing the name of the property requested
	 * @return information about a particular property of this sequence.
	 */
	public IsSerializable getIsSerializableProperty(SequenceHook seq, String key) throws DataServiceException;
	
	public String getStringProperty(SequenceHook seq, String key) throws DataServiceException;
	
	public boolean getBooleanProperty(SequenceHook seq, String key) throws DataServiceException;
	
	public int getIntegerProperty(SequenceHook seq, String key) throws DataServiceException;
	
	
//	/**
//	 * Adds the nucleotide sequence to the database.
//	 * 
//	 * @param nucleotides
//	 * @param features
//	 * @return a SequenceHook that can be used to reference the newly added sequence
//	 */
//	public SequenceHook saveSequence(String nucleotides, Map<String, Collection<Feature>> features, 
//									String seqName,  Map<String,IsSerializable> properties) throws DataServiceException;

	public SequenceHook saveSequence (String nucleotides, String seqName) throws DataServiceException;
	
	public List<String> listAvailableSequences() throws DataServiceException;
	
	public SequenceHook findSequence(String name) throws DataServiceException;
	
	
	public Map getAllProperties(SequenceHook seq) throws DataServiceException;
}
