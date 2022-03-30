/**-
 * Copyright (c) 2006 Hugo Zaragoza and Jose R. P�rez-Ag�era
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL COPYRIGHT HOLDERS OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.demo.app.jcolibri.extensions.textual.lucene.spanish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Set;

import jcolibri.util.FileIO;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;



/**
 * Spanish Lucene analyzer
 * @author Hugo Zaragoza and Jose R. P�rez-Ag�era
 */
public class SpanishAnalyzer extends Analyzer {
	
	private Set stopSet;
	
	/**
	 * Creates the Lucene Spanish Analyzer
	 * @throws IOException
	 */
	public SpanishAnalyzer() throws IOException
	{
		super();
		stopSet = StopFilter.makeStopSet(loadStopWords());
	}
	
	/** Constructs a {@link StandardTokenizer} filtered by a {@link
	StandardFilter}, a {@link LowerCaseFilter} and a {@link StopFilter}. */
	public TokenStream tokenStream(String fieldName, Reader reader) 
	{
	    TokenStream result = new StandardTokenizer(reader);
	    result = new StandardFilter(result);
	    result = new LowerCaseFilter(result);
	    result = new StopFilter(result, stopSet);
	    result = new SpanishStemmerFilter(result);
	    return result;
	}
	
	/**
	 * Loads the spanish stop-words list
	 * @throws IOException
	 */
	private static String[] loadStopWords() throws IOException
	{
		InputStreamReader isr = new InputStreamReader(FileIO.openFile("jcolibri/extensions/textual/lucene/spanish/stopwords-spanish.txt"));
		BufferedReader br = new BufferedReader(isr);
		String line = br.readLine();
	 	ArrayList<String> list = new ArrayList<String>();
		while(line != null)
		{
			list.add(line.trim());
			line = br.readLine();
		}
		String stopWords[] = new String[list.toArray().length];
		for(int i = 0; i<list.toArray().length;i++)
			stopWords[i]= (String) list.get(i);
		
		return stopWords;
	}
	
	

}
