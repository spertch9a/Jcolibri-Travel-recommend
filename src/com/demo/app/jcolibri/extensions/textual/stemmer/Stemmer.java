/**
 * Stemmer.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 15/04/2007
 */
package com.demo.app.jcolibri.extensions.textual.stemmer;

import java.util.StringTokenizer;

/**
 * Stemmes a word using the Snowball package. It works with several languages.
 * @author Juan A. Recio-Garc�a
 * @version 1.0
 */
public class Stemmer {

	/** Available languages */
	public enum Language { DANISH, DUTCH, ENGLISH, FINNISH, FRENCH, GERMAN, ITALIAN, NORWEGIAN, PORTUGUESE, RUSSIAN, SPANISH, SWEDISH};
	
	private net.sf.snowball.SnowballProgram _stemmer;
	
	/**
	 * Creates a stemmer for English
	 */
	public Stemmer()
	{
		this(Language.ENGLISH);
	}
	
	/**
	 * Creates a stemmer for the given language
	 */
	public Stemmer(Language language)
	{
		if (language == Language.DANISH)
			_stemmer = new net.sf.snowball.ext.danishStemmer();
		else if (language == Language.DUTCH)
			_stemmer = new net.sf.snowball.ext.dutchStemmer();
		else if (language == Language.ENGLISH)
			_stemmer = new net.sf.snowball.ext.englishStemmer();
		else if (language == Language.FINNISH)
			_stemmer = new net.sf.snowball.ext.finnishStemmer();
		else if (language == Language.FRENCH)
			_stemmer = new net.sf.snowball.ext.frenchStemmer();
		else if (language == Language.GERMAN)
			_stemmer = new net.sf.snowball.ext.germanStemmer();
		else if (language == Language.ITALIAN)
			_stemmer = new net.sf.snowball.ext.italianStemmer();
		else if (language == Language.NORWEGIAN)
			_stemmer = new net.sf.snowball.ext.norwegianStemmer();
		else if (language == Language.PORTUGUESE)
			_stemmer = new net.sf.snowball.ext.portugueseStemmer();
		else if (language == Language.RUSSIAN)
			_stemmer = new net.sf.snowball.ext.russianStemmer();
		else if (language == Language.SPANISH)
			_stemmer = new net.sf.snowball.ext.spanishStemmer();
		else if (language == Language.SWEDISH)
			_stemmer = new net.sf.snowball.ext.swedishStemmer();
		else
			_stemmer = new net.sf.snowball.ext.englishStemmer();
	}
	
	/**
	 * Stems a word
	 */
	public String stem(String word)
	{
		if (_stemmer == null)
			return word;
		_stemmer.setCurrent(word.toLowerCase());
		_stemmer.stem();
		return _stemmer.getCurrent();
	}
	
	/**
	 * Stems a sentences. It returns the same sentence but with all words stemmed
	 */
	public String stemSentence(String sentence)
	{
		StringTokenizer st = new StringTokenizer(sentence, " ");
		StringBuffer res = new StringBuffer();
		while(st.hasMoreTokens())
		{
			String nextToken = st.nextToken();
			res.append(stem(nextToken));
			if(st.hasMoreTokens())
				res.append(' ');
		}
		return res.toString();
	}
}
