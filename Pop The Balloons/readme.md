This python program reads a .txt file and creates a game grid accordingly.
The grid consists of different colored balloons (colors are represented by their first letters for simplicity) 
and a bonus balloon labeled "X". Player chooses a starting position by specifying it's coordinates, such as (1,4) or (0,7). 
If there are identically colored balloons connected to the balloon that's on this coordinate, 
the balloons are popped and the balloons which are no longer supported fall down creating empty space 
above the grid. If the "X" labeled balloon is selected, it pops all the balloons that are placed on 
it's horizontal and vertical axis and this action is followed by the same "slide-down" process. 
After every move, the program outputs the current grid, current score and asks the user for further commands.
If the coordinates entered are out of bounds or refer to an empty place, the game asks again for the coordinates.
