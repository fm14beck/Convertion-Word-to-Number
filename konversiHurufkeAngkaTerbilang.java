/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author fm14beck
 */
public class konversiHurufkeAngkaTerbilang {
    
    final static List<String> nominalIndonesiaStrings = Arrays.asList("satu", "dua", "tiga", "empat", "lima", "enam", "tujuh",
			"delapan", "sembilan", "sepuluh","sebelas","belas",
                        "puluh","ratus","ribu","juta","milyar","triliun",
			"seratus","seribu","sejuta","semilyar","setriliun",
                        "rupiah");

        public static void main(String[] args) {
          callInput();
        }
        public static void callInput(){         
          Scanner sc = new Scanner(System.in);
          System.out.print("****** silahkan masukan ## untuk berhenti ****** \n");
          System.out.print("Masukan Huruf yang akan disebut: \n");       
          try   {
                while (sc.hasNextLine()){
                    String input = sc.nextLine();
                  if (input.equals("##")) {
                      System.exit(0);
                      sc.close();
                  }else{
                      resultData(input);
                  }
                }
                } finally {
                  System.out.print("****** sampai jumpa >_< ****** \n");
                  if (sc != null) sc.close();
                }
        }
        
        public static void resultData(String input){
            if(validationInput(input)){
             System.out.println("result = " + new konversiHurufkeAngkaTerbilang().hurufToTerbilang(input) + "\n");
             System.out.println ("Dalam format currency = " + KonversiRupiah(new konversiHurufkeAngkaTerbilang().hurufToTerbilang(input))+ "\n");
             callInput();
          }else{
              System.out.print("\n result = Inputan anda salah \n \n");
              callInput();
          }
        }
        
    public static Boolean validationInput(String input){
        List<String> words = new LinkedList<String>(Arrays.asList(input.split(" ")));
        // remove hyphenated textual numbers
        Boolean permission = true;
		for (int i = 0; i < words.size(); i++) {
                        if (!(nominalIndonesiaStrings.contains(words.get(i).toLowerCase()))) {
                            permission = false;
			}
		}
              return permission;
    }
        
    public static String hurufToTerbilang(String Huruf){
        String words = convertTextualNumbersInDocument(Huruf);
        return words;
    }   
    
    public static String convertTextualNumbersInDocument(String inputText) {

		// splits text into words and deals with hyphenated numbers. Use linked
		// list due to manipulation during processing
		List<String> words = new LinkedList<String>(cleanAndTokenizeText(inputText));
                
		// replace all the textual numbers
		words = replaceTextualNumbers(words);

		// put spaces back in and return the string. Should be the same as input
		// text except from textual numbers
		return wordListToString(words);
	}

	/**
	 * Does the replacement of textual numbers, processing each word at a time
	 * and grouping them before doing the conversion
	 * 
	 * @param words
	 * @return
	 */
	private static List<String> replaceTextualNumbers(List<String> words) {
                
		// holds each group of textual numbers being processed together. e.g.
		// "one" or "five hundred and two"
		List<String> processingList = new LinkedList<String>();
                
		int i = 0;
		while (i < words.size() || !processingList.isEmpty()) {             
			// masukan index 0 dari words ke word
			String word = "";

			if (i < words.size()) {
				word = words.get(i);
			}

			// bersihkan dan kecilkan huruf word
			String wordStripped = word.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();

			//jika huruf di word ada dalam daftar dan ( processingList != 0)
			if (nominalIndonesiaStrings.contains(wordStripped)){
				words.remove(i); // hapus indeks 0 dari words
				processingList.add(word); //processingList memasukan word
                              
			} else if (processingList.size() > 0) {
				//if "and" is the last word, add it back in to original list
				String firstProcessedWord = processingList.get(i);
				if (firstProcessedWord.equals("seratus")&&processingList.size() == 1) {
					words.add("100");
					processingList.remove(processingList.get(i));
				}else if (firstProcessedWord.equals("seribu")&&processingList.size() == 1) {
					words.add("1000");
					processingList.remove(processingList.get(i));
				}else if (firstProcessedWord.equals("sejuta")&&processingList.size() == 1) {
					words.add("1000000");
					processingList.remove(processingList.get(i));
				}else if (firstProcessedWord.equals("semilyar")&&processingList.size() == 1) {
					words.add("10000000000");
					processingList.remove(processingList.get(i));
				}else if (firstProcessedWord.equals("setriliun")&&processingList.size() == 1) {
					words.add("10000000000000");
					processingList.remove(processingList.get(i));
				}else{
                                    String wordAsDigits = String.valueOf(convertWordsToNum(processingList));
                                    wordAsDigits = retainPunctuation(processingList, wordAsDigits);
                                    words.add(i, String.valueOf(wordAsDigits));
                                    
                                }

				processingList.clear();
				i += 2;
			} else {
				i++;
			}
		}

		return words;
	}

	/**
	 * Retain punctuation at the start and end of a textual number.
	 * 
	 * e.g. (seventy two) -> (72)
	 * 
	 * @param processingList
	 * @param wordAsDigits
	 * @return
	 */
	private static String retainPunctuation(List<String> processingList, String wordAsDigits) {

		String lastWord = processingList.get(processingList.size() - 1);
		char lastChar = lastWord.trim().charAt(lastWord.length() - 1);
		if (!Character.isLetter(lastChar)) {
			wordAsDigits += lastChar;
		}

		String firstWord = processingList.get(0);
		char firstChar = firstWord.trim().charAt(0);
		if (!Character.isLetter(firstChar)) {
			wordAsDigits = firstChar + wordAsDigits;
		}

		return wordAsDigits;
	}

	/**
	 * Splits up hyphenated textual words. e.g. twenty-two -> twenty two
	 * 
	 * @param sentence
	 * @return
	 */
	private static List<String> cleanAndTokenizeText(String sentence) {
		List<String> words = new LinkedList<String>(Arrays.asList(sentence.split(" ")));

		// remove hyphenated textual numbers
		for (int i = 0; i < words.size(); i++) {
			String str = words.get(i);
			if (str.contains("-")) {
				List<String> splitWords = Arrays.asList(str.split("-"));

				// just check the first word is a textual number. Caters for
				// "twenty-five," without having to strip the comma
				if (splitWords.size() > 1 && nominalIndonesiaStrings.contains(splitWords.get(0))) {
					words.remove(i);
					words.addAll(i, splitWords);
				}
			}

		}

		return words;
	}

	/**
	 * Creates string including spaces from a list of words
	 * 
	 * @param list
	 * @return
	 */
	private static String wordListToString(List<String> list) {
		StringBuilder result = new StringBuilder("");
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			if (i == 0 && str != null) {
				result.append(list.get(i));
			} else if (str != null) {
				result.append(" " + list.get(i));
			}
		}

		return result.toString();
	}

	/**
	 * Logic for taking a textual number string and converting it into a number
	 * e.g. twenty five -> 25
	 * 
	 * This relies on there only being one textual number being processed. Steps
	 * prior to this deal with breaking a paragraph down into individual textual
	 * numbers, which could consist of a number of words.
	 * 
	 * @param input
	 * @return
	 */
	private static long convertWordsToNum(List<String> words) {
		long finalResult = 0;
		long intermediateResult = 0;
                long specialResult = 0;
                long tempResult = 0;
		for (String str : words) {
                        
			// clean up string for easier processing
			str = str.toLowerCase().replaceAll("[^a-zA-Z\\s]", "");
                        if (str.equalsIgnoreCase("nol")) {
				intermediateResult += 0;
                        }else if (str.equalsIgnoreCase("seratus")) {
				intermediateResult += 1;
                                intermediateResult *= 100;
			} else if (str.equalsIgnoreCase("seribu")) {
                                intermediateResult += 1;
				intermediateResult *= 1000;
				finalResult += intermediateResult;
				intermediateResult = 0;  
                        }else if (str.equalsIgnoreCase("sejuta")) {
                                intermediateResult += 1;
				intermediateResult *= 1000000;
				finalResult += intermediateResult;
				intermediateResult = 0;
			} else if (str.equalsIgnoreCase("semilyar")) {
                                intermediateResult += 1;
				intermediateResult *= 1000000000;
				finalResult += intermediateResult;
				intermediateResult = 0;
			} else if (str.equalsIgnoreCase("setriliun")) {
                                intermediateResult += 1;
				intermediateResult *= 1000000000000L;
				finalResult += intermediateResult;
				intermediateResult = 0;
                        } else if (str.equalsIgnoreCase("satu")) {
                                intermediateResult += 1;
			} else if (str.equalsIgnoreCase("dua")) {
				intermediateResult += 2;
			} else if (str.equalsIgnoreCase("tiga")) {
				intermediateResult += 3;
			} else if (str.equalsIgnoreCase("empat")) {
				intermediateResult += 4;
			} else if (str.equalsIgnoreCase("lima")) {
				intermediateResult += 5;
			} else if (str.equalsIgnoreCase("enam")) {
				intermediateResult += 6;
			} else if (str.equalsIgnoreCase("tujuh")) {
				intermediateResult += 7;
			} else if (str.equalsIgnoreCase("delapan")) {
				intermediateResult += 8;
			} else if (str.equalsIgnoreCase("sembilan")) {
				intermediateResult += 9;
			} else if (str.equalsIgnoreCase("sepuluh")) {
				intermediateResult += 10;
			} else if (str.equalsIgnoreCase("sebelas")) {
				intermediateResult += 11;
			}else if (str.equalsIgnoreCase("belas")){
                            	intermediateResult += 10;
                        }
                        else if (str.equalsIgnoreCase("puluh")) {
                            if(specialResult > 0 && intermediateResult > 0){
                                intermediateResult *= 10;
                                if(finalResult == 0 && tempResult == 0 ){
                                    finalResult += intermediateResult;
                                }else if(finalResult > 0 && tempResult == 0) {
                                    finalResult += intermediateResult;
                                }else{
                                    tempResult+= intermediateResult;
                                }
                                specialResult = intermediateResult;                              
                                intermediateResult = 0;	
                            }else{ 
                                intermediateResult *= 10;
				finalResult += intermediateResult;
                                specialResult = intermediateResult;
				intermediateResult = 0;
                            }
                        }
                        else if (str.equalsIgnoreCase("ratus")) {
                            if(specialResult > 0 && intermediateResult > 9){
				finalResult *= 100;
                                specialResult = 0;
                            }else{
				intermediateResult *= 100;
                                if(finalResult == 0){
                                   finalResult += intermediateResult;
                                }else{
                                    tempResult += intermediateResult;
                                }
                                specialResult = intermediateResult;
				intermediateResult = 0;		
                            }
			} else if (str.equalsIgnoreCase("ribu")) {
                            if(specialResult > 0 && intermediateResult > 0 ){
                                if(tempResult > 0){
                                    tempResult+= intermediateResult;
                                    tempResult *= 1000;
                                    finalResult += tempResult;
                                    tempResult = 0;
                                }else{
                                    finalResult += intermediateResult;
                                    finalResult *= 1000;
                                }
                                intermediateResult = 0;
                                specialResult = 0;
                            }else{
                                if(intermediateResult == 0){
                                    if(tempResult > 0){
                                        tempResult *= 1000;
                                        finalResult += tempResult;
                                    }else{
                                        finalResult *= 1000;
                                    }
                                }else{
                                   intermediateResult *= 1000;
                                   finalResult += intermediateResult;
                                }
                                specialResult = intermediateResult;
				intermediateResult = 0;
                                tempResult = 0;
                            }
			} else if (str.equalsIgnoreCase("juta")) {
                            if(specialResult > 0 && intermediateResult > 0 ){
                                if(tempResult > 0){
                                    tempResult+= intermediateResult;
                                    tempResult *= 1000000;
                                    finalResult += tempResult;
                                }else{
                                    finalResult += intermediateResult;
                                    finalResult *= 1000000;
                                }
                                intermediateResult = 0;
				specialResult = 0;
                                tempResult = 0;
                            }else{
                                intermediateResult *= 1000000;
				finalResult += intermediateResult;
                                specialResult = intermediateResult;
				intermediateResult = 0;
                                tempResult = 0;
                                
                            }
			} else if (str.equalsIgnoreCase("milyar")) {
                            if(specialResult > 0 && intermediateResult > 0){
                                finalResult += intermediateResult;
                                finalResult *= 1000000000;
                                intermediateResult = 0;
				specialResult = 0;
                                tempResult = 0;
                            }else{
				intermediateResult *= 1000000000;
				finalResult += intermediateResult;
				intermediateResult = 0;
                                tempResult = 0;
                            }
			} else if (str.equalsIgnoreCase("triliun")) {
                            if(specialResult > 0 && intermediateResult > 0){
                                finalResult += intermediateResult;
                                finalResult *= 1000000000000L;
                                intermediateResult = 0;
				specialResult = 0;
                                tempResult = 0;
                            }else{
				intermediateResult *= 1000000000000L;
				finalResult += intermediateResult;
				intermediateResult = 0;
                                tempResult = 0;
                            }
			}
//                        System.out.print("cek hasil >> " + finalResult + "\n");
//                        System.out.print("cek temp >> " + intermediateResult + "\n");
		}
                if(tempResult!=0){
                   intermediateResult += tempResult;
                }
		finalResult += intermediateResult;
		intermediateResult = 0;
		return finalResult;
	}
        
        public static String KonversiRupiah(String resultKonversi){
            double convert = Double.valueOf(resultKonversi);
            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol(" ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');              
            kursIndonesia.setDecimalFormatSymbols(formatRp);
            return String.valueOf(kursIndonesia.format(convert));
        }
        
    
}