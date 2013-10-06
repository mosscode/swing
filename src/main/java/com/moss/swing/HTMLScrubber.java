/**
 * Copyright (C) 2013, Moss Computing Inc.
 *
 * This file is part of swing.
 *
 * swing is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * swing is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with swing; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */
package com.moss.swing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

class HtmlElement{
	int start, end;
}

class NonTag extends HtmlElement{
	boolean isEmpty=true;
	public String toString(){return "TEXT";}
}
class Tag extends HtmlElement{
	StringBuffer tagname;
	public String toString(){
		return "Tag: " + tagname.toString();
	}
}
class HtmlElementSet{
	HtmlElement start, end;
	ArrayList brTags = new ArrayList();
}
public class HTMLScrubber {


	
	
	
	/**
	 * Converts a basic text comment into a basic html document where lines 
	 * are converted into paragraphs.
	 * @param basictext
	 * @return
	 * @throws Exception
	 */
	private static final String makeHTMLDocument(String basictext){
		StringBuffer html = new StringBuffer();
		html.append("<html>\n");
		html.append("	<head>\n");
//		html.append("		<style type=\"text/css\">\n");
//		html.append("			<!--\n");
//		html.append(STYLES + "\n");
//		html.append("			-->\n");
//		html.append("		</style>\n");
		html.append("	</head>\n");
		html.append("	<body>\n");
		
		try {
			BufferedReader reader = new BufferedReader(new StringReader(basictext));
			String line = reader.readLine();
			while(line != null){
				
				html.append("		<p>\n");
				html.append(line);
				html.append("		</p>\n");
				line = reader.readLine();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		html.append("	</body>\n");
		html.append("</html>");
		
		return html.toString();
	}
	
	/**
	 * <p>Takes an html file with "br" tags and converts them to "p" tags</p>
	 * <p>Limitations:</p>
	 * <ul>
	 * 	<li> 
	 * </ul>
	 * @param html
	 * @return
	 */
	private static final StringBuffer scrubHTML(StringBuffer file){
//		String html = file.toString();
//		String htmlUppercase = html.toUpperCase();
//		int indexOfBodyTag = htmlUppercase.indexOf("<BODY>");
		
		HtmlElement[] elements = findTags(file);
		for (int i = 0; i < elements.length; i++) {
			HtmlElement element = elements[i];
			System.out.println(element);
		}
		Tag openBodyTag = null, closeBodyTag = null;
		ArrayList bodyElements = new ArrayList();
		// Find text between the <body> tags
		for (int i = 0; i < elements.length; i++) {
			HtmlElement element = elements[i];
			if(element instanceof Tag && ((Tag)element).tagname.toString().toUpperCase().equals("BODY"))
				openBodyTag = (Tag)element;
			else if(element instanceof Tag && ((Tag)element).tagname.toString().toUpperCase().equals("/BODY"))
				closeBodyTag = (Tag)element;
			else if(openBodyTag!=null && closeBodyTag==null){//i.e. the body is open
				bodyElements.add(element);
			}
		}
		System.out.println("Found " + bodyElements.size() + " body elements");
		for (Iterator bodyElementsIterator = bodyElements.iterator(); bodyElementsIterator.hasNext();) {
			HtmlElement element = (HtmlElement) bodyElementsIterator.next();
			System.out.println(element);
		}
		// group the body text into segments bounded by <p> tags
		
		ArrayList nonParagraphRanges = new ArrayList();
		System.out.println(openBodyTag + "\n" + closeBodyTag);
		boolean paragraphIsOpen=false;
		HtmlElementSet nonParagraphRange = new HtmlElementSet();
		HtmlElement previousElement=null;
		HtmlElement element=null;
		for (Iterator i = bodyElements.iterator(); i.hasNext();) {
			previousElement=element;
			element = (HtmlElement) i.next();
			System.out.println("PRocessing element" + element);
			if(!paragraphIsOpen){
				if(element instanceof Tag && ((Tag)element).tagname.toString().toUpperCase().equals("P"))
					paragraphIsOpen=true;
					if(nonParagraphRange.start != null){
						nonParagraphRange.end = previousElement;
						nonParagraphRanges.add(nonParagraphRange);
						nonParagraphRange = new HtmlElementSet();
					}
				else{// this is not bound by a paragraph
					
					if(nonParagraphRange.start == null){ 
						// the non-paragraph block has not been initialized.
						// this means this is the first element in this block of 
						// non-paragraph-bound elements
						nonParagraphRange.start = element;
					}
					if(element instanceof Tag && ((Tag)element).tagname.toString().toUpperCase().equals("BR")){
						nonParagraphRange.brTags.add(element);
						System.out.println("Found br tag");
					}
					
				}
			}else{
				if(element instanceof Tag && ((Tag)element).tagname.toString().toUpperCase().equals("/P")){
					paragraphIsOpen=false;
				}

			}
		}
		if(nonParagraphRange.start != null){
			nonParagraphRange.end = element;
			nonParagraphRanges.add(nonParagraphRange);
			nonParagraphRange = new HtmlElementSet();
		}
		System.out.println("Found " + nonParagraphRanges.size() + " non paragraph ranges");
		
		// wrap any segments that are not enclosed by <p> tags with <p> tags
		int numAddedChars=0;
		String parOpenTag="<p>";
		String parCloseTag="</p>";
		for (Iterator i = nonParagraphRanges.iterator(); i.hasNext();) {
			HtmlElementSet range = (HtmlElementSet) i.next();
			file.insert(range.start.start + numAddedChars, parOpenTag);
			numAddedChars += parOpenTag.length();
			
			//remove any <br> tags here
			System.out.println("Found " + range.brTags.size() + " <BR> tags for this range");
			for (Iterator brIterator = range.brTags.iterator(); brIterator.hasNext();) {
				Tag brTag = (Tag) brIterator.next();
				file.replace(brTag.start + numAddedChars, brTag.end + numAddedChars, "");
				numAddedChars -= brTag.end - brTag.start;
			}
			file.insert(range.end.end + numAddedChars, parCloseTag);
			numAddedChars += parCloseTag.length();
		}
		
		
		
		// add our style to the document
		return file;
	}
	
	
	private static final HtmlElement[] findTags(StringBuffer text){
		ArrayList elements = new ArrayList();
		
		boolean tagIsOpen=false;
		int elementStart=-1;
		StringBuffer tagName=new StringBuffer();
		
		boolean nonTagHasText=false;
		
		boolean tagNameStarted=false;
		boolean tagNameEnded=false;
		
		for(int x=0;x<text.length();x++){
			char c = text.charAt(x);
			if(tagIsOpen){
				if(c == '>'){// tag closed
					Tag tag = new Tag();
					tag.tagname=tagName;
					tag.start = elementStart;
					tag.end = x;
					elements.add(tag);
					
					tagIsOpen=false;
					elementStart=-1;
					tagName=new StringBuffer();
				}else if(!tagNameStarted){
					//look for the tag start
					if(!Character.isWhitespace(c)){
						tagNameStarted=true;
						tagName.append(c);
					}
				}else if(tagNameStarted && !tagNameEnded){
					if(!Character.isWhitespace(c)){
						tagName.append(c);
					}else{
						tagNameEnded=true;;
					}
				}
			}else{
				if(c == '<'){ //this is the start of a tag
					if(elementStart!=-1){ //and it is also the character after the end of a non-tag
						NonTag nontag = new NonTag();
						nontag.start = elementStart;
						nontag.end = x-1;
						nontag.isEmpty = nonTagHasText;
						elements.add(nontag);
					}
					tagIsOpen=true;
					elementStart=x;
				}else{ //this is non-tag text
					if(elementStart==-1){//this is not the start of a tag, but it is the start of a non-tag
						elementStart=x;
					}
					if(!Character.isWhitespace(c)){
						nonTagHasText=true;
					}
				}
			}
			
		}
		
		return (HtmlElement[]) elements.toArray(new HtmlElement[0]);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		File fileToScrub = new File(args[0]);
		FileReader reader = new FileReader(fileToScrub);
		String charset = reader.getEncoding();
		char[] buffer = new char[1024*10];//10k character buffer
		StringBuffer html = new StringBuffer();
		int charsRead = reader.read(buffer);
		while(charsRead!=-1){
			html.append(buffer, 0, charsRead);
			charsRead=reader.read(buffer);
		}
		System.out.println(scrubHTML(html));
	}

}
