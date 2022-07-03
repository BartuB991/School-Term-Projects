This java program sets up a game which consists of a grid; of any size, for example 10x10, of “jewels” by reading an input file. 
Some example jewels are Diamond (D), Square (S), Triangle (T) and Wildcard (W). The goal of the game is 
to find three jewels which are matching in a row, column or diagonal by selecting the right coordinate. 
When jewels are matched, the three jewels are deleted, and other jewels fall from the top to fill in gaps. 
If the selected coordinate is empty or out of grid, then a warning message "Please enter a valid coordinate" 
is displayed to the user and a new coordinate is requested. Each jewel type matches in with it's own type in a different way.
Commands are taken from the user by an input file and after each input command is carried out, the program outputs the current 
game grid and tells the user how much points they gained after the last move, the game ends either when a "E" command is given in the
input file or when there are no matching jewels. Also if a leaderboard.txt is provided, when the game ends, the program reads the leaderboard.txt
and tells the current user his/her position on the leaderboard with point gaps to the player above/below.


Below you can find a detailed description of my take on the problem and my implementation of "Abstract Classes" and "Interfaces".


First of all, I created an abstract class called Jewel and the following subclasses of Jewel such as; Diamond, Square, Triangle, WildCard and MathJewel. 
My Jewel class consists of an array that stores wildcard arrays suitable for subclasses of Jewel (specific jewel instances), which is used to store and 
to print the game grid, a private search method that looks for possible jewel matches according to the given parameters.

I also have direction specific search methods such as LeftDiagonalSearch, RightDiagonalSearch, HorizontalSearch, VerticalSearch and an InputProcessor
method that instantiates these methods according to the hierarchy. The working mechanism of the system is; first the InputProcessor method is called, 
this method checks the sign of the jewel and takes actions according to the selected jewel’s characteristics. For example, if the selected jewel is a Diamond, 
it first calls the left diagonal search method and inside this method there are two method calls of the private search method with different arguments, 
one for left diagonal upwards search and one for left diagonal downwards search. According to the first search method’s return value, the outer method either 
returns a value or checks for the other direction. And this return-or-check behavior moves up the method order from the search method to the direction specific 
search method and lastly the input processor method. In the end, the input processor method finishes working and returns a value and the result is written to the 
“monitoring.txt” according to this return value. 

In my opinion, this return-or-check order supports the addition of the other jewels in the best way possible. 
Because in the case of new jewels, the programmer only has to define the new jewels and call their direction specific methods in order inside the
input processor method and the existing program will do the rest of the job. 

My Jewel class also has a createJewel method to instantiate the jewels, a slideDown
method to move the jewels according to the empty space that exists inside the game grid and a printGrid method to display the game grid. 

I also have a Player class that implements the comparable interface in order to access the current player’s finishing position and points difference to 
the closest competitors that are existing inside the “leaderboard.txt”. The ArrayList containing the players is instantiated inside the main method and
the static playerResult method from the Player class is called to first sort the players array according to the scores of the players and then
the binary search is performed to find the finishing position of the current player and the string representing the result is returned.
