package interfaces;

/**
 * Specifies the required methods of a Feature of a DNASequence.
 * 
 * @author jeldridg
 */
public interface Feature {

	public String getName();
	
	public int getStartPosition();
	
	public int getEndPosition();
	
}
