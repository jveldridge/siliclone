package edu.brown.cs32.siliclone.database.client;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.brown.cs32.siliclone.dna.NucleotideString;
import edu.brown.cs32.siliclone.dna.SequenceHook;
import edu.brown.cs32.siliclone.dna.features.Feature;

@RemoteServiceRelativePath("sequence")
public interface SequenceService extends RemoteService {

	/**
	 * Returns a String representing the nucleotides that make up this sequence.
	 * The String will consist of characters A,C,G,T for nucleotides adenine,
	 * cytosine, guanine, and tyrosine, respectively, and B,D,H,S for the methylated
	 * versions of these nucleotides.
	 * >>> Can we have things like X for unknown?
	 * 
	 * @return a String representing the nucleotides that make up this sequence.
	 */
	public NucleotideString getSequence(SequenceHook seq) throws DataServiceException;
	
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
	public void addProperty(SequenceHook seq, String key, Object value) throws DataServiceException;
	
	/**
	 * Returns information about a particular property of this sequence.
	 * If this property has not previously been set, for example, by using
	 * the addProperty(...) method, null will be returned.
	 * 
	 * @param key a String representing the name of the property requested
	 * @return information about a particular property of this sequence.
	 */
	public Object getProperty(SequenceHook seq, String key) throws DataServiceException;
	
	/**
	 * Adds the nucleotide sequence to the database.
	 * 
	 * @param nucleotides
	 * @param features
	 * @return a SequenceHook that can be used to reference the newly added sequence
	 */
	public SequenceHook saveSequence(NucleotideString nucleotides, Map<String, Collection<Feature>> features, 
									String seqName,  Map<String,Object> properties) throws DataServiceException;
	
}
