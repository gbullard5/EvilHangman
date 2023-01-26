package hangman;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
  TreeSet<String> possibleWords = new TreeSet<>();
  SortedSet <Character> guesses = new TreeSet<>();
  String pattern;
  @Override
  public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
    possibleWords.clear();
    guesses.clear();
    pattern = new String(new char[wordLength]).replace('\0','-');
    if(dictionary.length() > 0){
      try(Scanner scan = new Scanner(new FileReader(dictionary))){
        while(scan.hasNext()){
          String word = scan.next();
          word = word.toLowerCase();
          if(word.length() == wordLength){
            possibleWords.add(word);
          }
      }
        if (possibleWords.size() == 0){
          throw new EmptyDictionaryException();
        }
        }
      }
    else throw new EmptyDictionaryException();
    }



  @Override
  public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
    TreeMap<String, TreeSet<String>> wordPartitions = new TreeMap<>();
    char guess1 = Character.toLowerCase(guess);
    if(!guesses.isEmpty()){
      if(guesses.contains(guess1)){
        throw new GuessAlreadyMadeException();
      }
    }
    guesses.add(guess1);
    for(String s : possibleWords){
      TreeSet<String> w = new TreeSet<>();
      w.add(s);
      String word = makePattern(s, guess1);
      if(!wordPartitions.containsKey(word)){
        wordPartitions.put(word,w);
      }
      else{
        wordPartitions.get(word).addAll(w);
      }
    }
    Map.Entry<String, TreeSet<String>> current = null;
    for(Map.Entry<String, TreeSet<String>> contender : wordPartitions.entrySet()){
      if( current == null || contender.getValue().size() > current.getValue().size()){
        current = contender;
        continue;
      }
      if(contender.getValue().size() == current.getValue().size()){
        if(!contender.getKey().contains(Character.toString(guess1))){
          current = contender;
        }
        else if(contender.getKey().contains(Character.toString(guess1)) && current.getKey().contains(Character.toString(guess1))){
          long a = contender.getKey().chars().filter(ch -> ch == guess1).count();
          long b = current.getKey().chars().filter(ch -> ch == guess1).count();
          if(b > a){
            current = contender;
          }else if(a == b) {
            if(contender.getKey().lastIndexOf(guess1) > current.getKey().lastIndexOf(guess1)){
              current = contender;
            }
          }
        }
      }
    }
    pattern = current.getKey();
    possibleWords = current.getValue();
    return possibleWords;
  }

  public String makePattern(String word, char c){
    StringBuilder sb = new StringBuilder();
    char[] arr = word.toCharArray();
    for(char a: arr){
      if (c != a){
        a = '-';
      }
      sb.append(a);
    }

    return sb.toString();
  }

  @Override
  public SortedSet<Character> getGuessedLetters() {
    return this.guesses;
  }
}
