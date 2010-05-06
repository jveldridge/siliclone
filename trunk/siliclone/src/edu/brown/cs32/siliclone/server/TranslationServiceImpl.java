package edu.brown.cs32.siliclone.server;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SymbolList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.brown.cs32.siliclone.client.dna.SequenceHook;
import edu.brown.cs32.siliclone.client.visualizers2.translation.TranslationService;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.server.SequenceServiceImpl;

@SuppressWarnings("serial")
public class TranslationServiceImpl extends RemoteServiceServlet implements
		TranslationService {

	public String getForwardTranslationOne(SequenceHook seq)
			throws DataServiceException {
		String nucleotides = SequenceServiceImpl.getSequence(seq,
				this.getThreadLocalRequest().getSession()).toString();
		return this.translateSequence(nucleotides);
	}

	public String getForwardTranslationThree(SequenceHook seq)
			throws DataServiceException {
		String nucleotides = SequenceServiceImpl.getSequence(seq,
				this.getThreadLocalRequest().getSession()).toString();
		return this.translateSequence(nucleotides.substring(1));
	}

	public String getForwardTranslationTwo(SequenceHook seq)
			throws DataServiceException {
		String nucleotides = SequenceServiceImpl.getSequence(seq,
				this.getThreadLocalRequest().getSession()).toString();
		return this.translateSequence(nucleotides.substring(2));
	}

	public String getReverseTranslationOne(SequenceHook seq)
			throws DataServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getReverseTranslationThree(SequenceHook seq)
			throws DataServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getReverseTranslationTwo(SequenceHook seq)
			throws DataServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	private String translateSequence(String toTranslate) {
		SymbolList symL = null;
		try {
			int mod = toTranslate.length() % 3;
			toTranslate = toTranslate.substring(0, toTranslate.length() - mod);

			symL = DNATools.createDNA(toTranslate);

			// transcribe to RNA
			symL = DNATools.toRNA(symL);

			// translate to protein
			symL = RNATools.translate(symL);

		} catch (IllegalSymbolException e) {
			// this should never happen
			e.printStackTrace();
		} catch (IllegalAlphabetException e) {
			// this should never happen
			e.printStackTrace();
		}

		// prove that it worked
		return symL.seqString();
	}

}
