package com.example.splashscreentest;

abstract public class StringFormatter {

    /**
     * Takes a string, capitalizes the first letter of each word.
     *
     * @param str - The string to capitalize
     * @return - String with each letter of first word capital
     */
    public static String capitalizeWord(String str) {
        final String words[] = str.split("\\s");
        //Optimization to avoid String concatenation in loop
        StringBuilder capitalizeWord = new StringBuilder(str.length() + 1);

        for (String w : words) {
            String first = w.substring(0, 1);
            String afterFirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase());
            capitalizeWord.append(afterFirst);
            capitalizeWord.append(" ");
        }

        return capitalizeWord.toString().trim();
    }
}
