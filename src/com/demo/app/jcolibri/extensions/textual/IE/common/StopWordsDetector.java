/**
 * StopWordsDetector.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 20/06/2007
 */
package com.demo.app.jcolibri.extensions.textual.IE.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.gate.GatePhrasesExtractor;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;


/**
 * Removes stop words (workds without relevant meaning) and punctuation symbols.
 * It uses a built-in list and modifies the "isStopWord" flag of the tokens.
 * <p>
 * The first version was developed at: Robert Gordon University - Aberdeen & Facultad Inform�tica,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * @author Juan A. Recio-Garcia
 * @version 2.0
 */
public class StopWordsDetector
{

    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IEText objects.
     */
    public static void detectStopWords(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(StopWordsDetector.class).info("Detecting stop words.");
	ProgressController.init(StopWordsDetector.class, "Detecting stop words ...", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		detectStopWords((IEText)o);
	    }
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IEText objects.
     */
    public static void detectStopWords(CBRQuery query, Collection<Attribute> attributes)
    {
	    org.apache.commons.logging.LogFactory.getLog(StopWordsDetector.class).info("Detecting stop words.");
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, query);
		detectStopWords((IEText)o);
	    }
    }
    
    /**
     * Performs the algorithm in all the attributes of a collection of cases
     * These attributes must be IEText objects.
     */
    public static void detectStopWords(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(StopWordsDetector.class).info("Detecting stop words.");
	ProgressController.init(StopWordsDetector.class, "Detecting stop words ...", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		detectStopWords(t);
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }
    
    /**
     * Performs the algorithm in all the attributes of a query
     * These attributes must be IEText objects.
     */
    public static void detectStopWords(CBRQuery query)
    {	 
	org.apache.commons.logging.LogFactory.getLog(StopWordsDetector.class).info("Detecting stop words.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            detectStopWords(t);
    }
    
    /**
     * Performs the algorithm in a given IEText object
     */
    public static void detectStopWords(IEText text)
    {
	for(Token t: text.getAllTokens())
	{
	    String word = t.getRawContent().toLowerCase();
	    if(stopWordSet.contains(word))
		t.setStopWord(true);
	}
    }
        
    /**
     * Stop words list
     */
    static String[] stopWords = { "a", "a's", "able", "about", "above",
		"according", "accordingly", "across", "actually", "after",
		"afterwards", "again", "against", "ain't", "all", "allow",
		"allows", "almost", "alone", "along", "already", "also",
		"although", "always", "am", "among", "amongst", "an", "and",
		"another", "any", "anybody", "anyhow", "anyone", "anything",
		"anyway", "anyways", "anywhere", "apart", "appear", "appreciate",
		"appropriate", "are", "aren't", "around", "as", "aside", "ask",
		"asking", "associated", "at", "available", "away", "awfully", "b",
		"be", "became", "because", "become", "becomes", "becoming", "been",
		"before", "beforehand", "behind", "being", "believe", "below",
		"beside", "besides", "best", "better", "between", "beyond", "both",
		"brief", "but", "by", "c", "c'mon", "c's", "came", "can", "can't",
		"cannot", "cant", "cause", "causes", "certain", "certainly",
		"changes", "clearly", "co", "com", "come", "comes", "concerning",
		"consequently", "consider", "considering", "contain", "containing",
		"contains", "corresponding", "could", "couldn't", "course",
		"currently", "d", "definitely", "described", "despite", "did",
		"didn't", "different", "do", "does", "doesn't", "doing", "don't",
		"done", "down", "downwards", "during", "e", "each", "edu", "eg",
		"eight", "either", "else", "elsewhere", "enough", "entirely",
		"especially", "et", "etc", "even", "ever", "every", "everybody",
		"everyone", "everything", "everywhere", "ex", "exactly", "example",
		"except", "f", "far", "few", "fifth", "first", "five", "followed",
		"following", "follows", "for", "former", "formerly", "forth",
		"four", "from", "further", "furthermore", "g", "get", "gets",
		"getting", "given", "gives", "go", "goes", "going", "gone", "got",
		"gotten", "greetings", "h", "had", "hadn't", "happens", "hardly",
		"has", "hasn't", "have", "haven't", "having", "he", "he's",
		"hello", "help", "hence", "her", "here", "here's", "hereafter",
		"hereby", "herein", "hereupon", "hers", "herself", "hi", "him",
		"himself", "his", "hither", "hopefully", "how", "howbeit",
		"however", "i", "i'd", "i'll", "i'm", "i've", "ie", "if",
		"ignored", "immediate", "in", "inasmuch", "inc", "indeed",
		"indicate", "indicated", "indicates", "inner", "insofar",
		"instead", "into", "inward", "is", "isn't", "it", "it'd", "it'll",
		"it's", "its", "itself", "j", "just", "k", "keep", "keeps", "kept",
		"know", "knows", "known", "l", "last", "lately", "later", "latter",
		"latterly", "least", "less", "lest", "let", "let's", "like",
		"liked", "likely", "little", "look", "looking", "looks", "ltd",
		"m", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile",
		"merely", "might", "more", "moreover", "most", "mostly", "much",
		"must", "my", "myself", "n", "name", "namely", "nd", "near",
		"nearly", "necessary", "need", "needs", "neither", "never",
		"nevertheless", "new", "next", "nine", "no", "nobody", "non",
		"none", "noone", "nor", "normally", "not", "nothing", "novel",
		"now", "nowhere", "o", "obviously", "of", "off", "often", "oh",
		"ok", "okay", "old", "on", "once", "one", "ones", "only", "onto",
		"or", "other", "others", "otherwise", "ought", "our", "ours",
		"ourselves", "out", "outside", "over", "overall", "own", "p",
		"particular", "particularly", "per", "perhaps", "placed", "please",
		"plus", "possible", "presumably", "probably", "provides", "q",
		"que", "quite", "qv", "r", "rather", "rd", "re", "really",
		"reasonably", "regarding", "regardless", "regards", "relatively",
		"respectively", "right", "s", "said", "same", "saw", "say",
		"saying", "says", "second", "secondly", "see", "seeing", "seem",
		"seemed", "seeming", "seems", "seen", "self", "selves", "sensible",
		"sent", "serious", "seriously", "seven", "several", "shall", "she",
		"should", "shouldn't", "since", "six", "so", "some", "somebody",
		"somehow", "someone", "something", "sometime", "sometimes",
		"somewhat", "somewhere", "soon", "sorry", "specified", "specify",
		"specifying", "still", "sub", "such", "sup", "sure", "t", "t's",
		"take", "taken", "tell", "tends", "th", "than", "thank", "thanks",
		"thanx", "that", "that's", "thats", "the", "their", "theirs",
		"them", "themselves", "then", "thence", "there", "there's",
		"thereafter", "thereby", "therefore", "therein", "theres",
		"thereupon", "these", "they", "they'd", "they'll", "they're",
		"they've", "think", "third", "this", "thorough", "thoroughly",
		"those", "though", "three", "through", "throughout", "thru",
		"thus", "to", "together", "too", "took", "toward", "towards",
		"tried", "tries", "truly", "try", "trying", "twice", "two", "u",
		"un", "under", "unfortunately", "unless", "unlikely", "until",
		"unto", "up", "upon", "us", "use", "used", "useful", "uses",
		"using", "usually", "uucp", "v", "value", "various", "very", "via",
		"viz", "vs", "w", "want", "wants", "was", "wasn't", "way", "we",
		"we'd", "we'll", "we're", "we've", "welcome", "well", "went",
		"were", "weren't", "what", "what's", "whatever", "when", "whence",
		"whenever", "where", "where's", "whereafter", "whereas", "whereby",
		"wherein", "whereupon", "wherever", "whether", "which", "while",
		"whither", "who", "who's", "whoever", "whole", "whom", "whose",
		"why", "will", "willing", "wish", "with", "within", "without",
		"won't", "wonder", "would", "wouldn't", "x", "y", "yes", "yet",
		"you", "you'd", "you'll", "you're", "you've", "your", "yours",
		"yourself", "yourselves", "z", "zero", "albeit", "author", "av",
		"canst", "cf", "cfrd", "choose", "conducted", "considered",
		"contrariwise", "cos", "crd", "cu", "day", "describes", "designed",
		"determine", "determined", "discussed", "dost", "doth", "double",
		"dual", "due", "excepted", "excepting", "exception", "exclude",
		"excluding", "exclusive", "farther", "farthest", "ff", "forward",
		"found", "front", "furthest", "general", "halves", "hast", "hath",
		"henceforth", "hereabouts", "hereto", "hindmost", "hitherto",
		"howsoever", "I", "include", "included", "including", "indoors",
		"inside", "insomuch", "investigated", "inwards", "kind", "kg",
		"km", "made", "meantime", "mr", "mrs", "ms", "nonetheless", "nope",
		"notwithstandi", "ng", "nowadays", "obtained", "performance",
		"performed", "plenty", "present", "presented", "presents",
		"provide", "provided", "related", "report", "required", "results",
		"round", "sake", "sang", "save", "seldom", "selected", "sfrd",
		"shalt", "shown", "sideways", "significant", "slept", "slew",
		"slung", "slunk", "smote", "spake", "spat", "spoke", "spoken",
		"sprang", "sprung", "srd", "stave", "staves", "studies",
		"supposing", "tested", "thee", "thenceforth", "thereabout",
		"thereabouts", "thereof", "thereon", "thereto", "thou", "thrice",
		"thy", "thyself", "till", "types", "unable", "underneath",
		"unlike", "upward", "upwards", "week", "whatsoever", "whensoever",
		"whereabouts", "whereat", "wherefore", "wherefrom", "whereinto",
		"whereof", "whereon", "wheresoever", "whereto", "whereunto",
		"wherewith", "whew", "whichever", "whichsoevr", "whilst", "whoa",
		"whomever", "whomsoever", "whosoever", "wilt", "worse", "worst",
		"wow", "ye", "year", "yippee",
		//Also include puntuation
		",", ";", ".", ":", "_", "{", "}", "[", "]", "+", "*", "�", "�", "?", "=", ")", "(", "/", "&", "%", "$", "�"
    		};

    static Set<String> stopWordSet = new HashSet<String>(java.util.Arrays.asList(stopWords));

}
