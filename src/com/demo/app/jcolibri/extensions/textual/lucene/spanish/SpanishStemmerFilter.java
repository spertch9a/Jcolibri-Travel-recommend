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

import java.io.IOException;

import net.sf.snowball.ext.SpanishStemmer;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * Spanish Stemmer Filter
 * @author Hugo Zaragoza and Jose R. P�rez-Ag�era
 */
public class SpanishStemmerFilter extends TokenFilter {

	private SpanishStemmer stemmer;
	
	public SpanishStemmerFilter(TokenStream in)
	{
		super(in);
		this.stemmer = new SpanishStemmer();
	}

	public final Token next() throws IOException {
		Token t = input.next();

		if (t == null)
			return null;
		
	    this.stemmer.setCurrent(t.termText());
	    this.stemmer.stem();	    
		String text = stemmer.getCurrent();
		String type = t.type();
		Token tokenStem = new Token(text, t.startOffset(), t.endOffset(), type);
		
		return tokenStem;
	  }
}
