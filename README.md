# What is Google Applied Computer Science Skills?

Applied CS Skills is a free online course by Google designed to prepare you for your CS career through hands on coding experience.
During the course, you program a number of mobile applications using Android Studio which take as estimated total of six hours each.
The course is presented using a 'flipped classroom' approach; In a flipped classroom, what would have traditionally been lecture becomes 
the independent review or homework, and what would have been a homework assignment or practice problem set is completed together during classroom time.

# What are Android Developer Guides?

Android Developer Guides teach you how to build Android apps using APIs in the Android framework and other libraries. To gain some basic familiarity with
the documentation, there is a section called 'Build your first app' which I go through in this repository.

# Mobile Applications

Below is a list of the mobile applications I programmed while following the Google Applied Computer Science Skills course and the section called
'Build your first app' in the Android Developer Guides.

## MyFirstApp

This mobile application initially just displayed the text 'Hello, World!' in the main activity but then I learned how to extend it to create a 
new interface for the app that takes user input and switches to a new screen in the app to display it.

## Anagrams

Anagrams is a mobile game that works as follows.

- The game provides the user with a word from the dictionary.
- The user tries to create as many words as possible that contain all the letters of the given word plus one additional letter. Note that adding 
the extra letter at the beginning or the end without reordering the other letters is not valid. For example, if the game picks the word 'ore' as 
a starter, the user might guess 'rose' or 'zero' but not 'sore'.
- The user can give up and see the words that they did not guess.
- In order to ensure that the game is not too difficult, the computer will only propose words that have at least 5 possible valid anagrams.
