This java program is a basic implementation of the Monopoly game. Following the rules of the Monoply, the game
has a set of 40 sequential squares which are implemented using Json files; 28 of them are property squares, 
6 action squares, 2 tax squares and "Go", "Jail", "Free Parking", "Go To Jail" squares. There are two players and
a banker but depending on the input file, the number of players can increase. The game ends when one of these three conditions 
is met: a player goes bankrupt, the banker goes bankrupt or the end of the commands file is reached. Commands file contains the player name
with the total of the rolled dice, for example "Player 1;9" or "Player 2;5". There is also the "show()" command which tells the program to
show the current state of the players and the banker; how much money they have and which properties they own. Also the program outputs the last carried action
after every player's turn, it writes the name of the player, the dice they played, the action carried out such as "Player 1 bought X railroad" or "Player 2 paid tax".

Below you can find a detailed description of my take on the problem and my implementation of 
"Inheritence" and "Polymorphism" which are two of the core concepts of "Object Oriented Programming".

Firstly I created the JsonReader class and a static method inside this class to read two json files; 
list.json and property.json. In this static method, I created objects of each square while reading the json files. 
I stored Chance and Community Chest cards in their respective Arraylists, I also stored Properties in a hashmap with 
their square id's as their keys and property objects as the corresponding values. After creating the objects, I added them 
into my gameBoard arraylist according to their square numbers so they're in the correct order. In my Players.java file, 
my Players class is an abstract class that has methods that are both used by the classes that extend Players class, 
in this case Player class and the Banker class. In my Board.java file, my Board class is an abstract class that 
has a static arraylist called gameBoard, which stores game squares as objects and according to their original order. 
Even though they are stored as objects, later when I call the squares in my main class and in my static methods, I cast them to their 
original state depending on the square that the playing player is standing on. My Property, Chance, CommunityChest and Action 
classes have their own objects on the gameBoard and each class has its own static method that carries out the required steps when called.
In my main class, firstly I call the static method of JsonReader to create the gameBoard, I then read the provided commands.txt 
file to carry out the steps and according to the dice and the current position of the player that is currently playing, I call the 
static method from the subject class to carry out the required actions.
