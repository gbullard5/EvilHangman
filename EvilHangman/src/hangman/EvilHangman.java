package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) throws GuessAlreadyMadeException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the name of the dictionary: ");
        String in = scan.next();
        File wordBase = new File(in);
        System.out.println("Please enter the number of letters in the word: ");
        String numLettersStr = scan.next();
        System.out.println("Please enter the number of guess you want: ");
        String numGuessesStr = scan.next();
        int numLetters = Integer.parseInt(numLettersStr);
        int numGuesses = Integer.parseInt(numGuessesStr);
        if(numGuesses < 1){
            System.out.println("Number of guesses must be larger that 1");
            return;
        }
        if(numLetters < 2){
            System.out.println("Number of letters must be two or more");
            return;
        }
        int totalGuesses = numGuesses;
        EvilHangmanGame eg = new EvilHangmanGame();
        try {
            eg.startGame(wordBase, numLetters);
        } catch (IOException e){
            e.printStackTrace();
        }catch(EmptyDictionaryException e){
            System.out.println(e.getMessage());
        }
        char[] word= new char [numLetters];
        for(int i = 0; i<numLetters; i++){
            word[i] = '-';
        }
        while(numGuesses > 0){
            System.out.println("You have " + numGuesses + " guesses left");
            System.out.println("Used Letters: " + eg.guesses);
            word = check(totalGuesses, eg, word);
            System.out.print("Word: ");
            for (int j=0; j < word.length; j++) {
                System.out.print(word[j]);
            }
            System.out.println();
            System.out.print("Enter Guess: ");
            in = scan.next();
            in.toLowerCase();
            char guess = in.charAt(0);
            if(!Character.isAlphabetic(guess) || Character.isSpaceChar(guess)){
                System.out.println("Invalid input");
                System.out.println();
                continue;
            }
            try{
                eg.makeGuess(guess);

            }catch(GuessAlreadyMadeException g){
                System.out.println(guess + " already guessed, please try again");
                System.out.println();
                continue;
            }
            if(!eg.pattern.contains(Character.toString(guess))){
                System.out.println("There are no " + guess + " in the word.");
                System.out.println();
            }
            else{
                int count = 0;
                char[] pat = eg.pattern.toCharArray();
                for(int i =0; i<pat.length;i++)
                {
                    if(pat[i] == guess){
                        count ++;
                    }
                }
                System.out.println("There is " + count +" " +guess+" in the word");
                System.out.println();
            }


            numGuesses--;

            char[] word2= check(totalGuesses, eg, word);

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < word.length; j++) {
                sb.append(word2[j]);
            }
            String word3 = sb.toString();
            if(eg.possibleWords.contains(word3)){
                System.out.println("You win! You guess the word: " + word3);
                return;
            }
        }
        System.out.println("You Lose! The word was: " + eg.possibleWords.first());
    }
    public static char[] check(int totalGuess, EvilHangmanGame eV, char[] word) {

        char[] guessesArr=new char[totalGuess];
        int i=0;

        for (Character c : eV.guesses) {
            guessesArr[i]=c;
            i++;
        }
        char[] patternArr=eV.pattern.toCharArray();
        for (int j=0; j < patternArr.length; j++) {
            for (int k=0; k < guessesArr.length; k++) {
                if (patternArr[j] == guessesArr[k]) word[j]=guessesArr[k];
            }
        }


        return word;
    }
}


