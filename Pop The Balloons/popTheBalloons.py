import sys
class InputLenError(Exception):
    pass
class EmptyError(Exception):
    pass
inputFile = open(sys.argv[1], 'r')
killSwitch = 1
inputPass = 0
gameBoard = []
cellSet = []
playerScore = 0
scoreDict = {'X':0, 'F':1, 'D':2, 'O':3, 'P':4, 'R':5, 'Y':6, 'W':7, 'G':8, 'B':9}
def displayGameBoard():    
    global gameBoard
    for i in gameBoard:
        print(*i)
def endGameCheck():
    global gameBoard
    global killSwitch
    for rowNum in range(len(gameBoard)):
        for colNum in range(len(gameBoard[rowNum])):
            if gameBoard[rowNum][colNum] != ' ':
                try:
                    if gameBoard[rowNum][colNum] == gameBoard[rowNum][colNum+1] and colNum != len(gameBoard[rowNum]):
                        killSwitch = 0
                except IndexError: pass
                if killSwitch == 0:
                    break
                try:
                    if gameBoard[rowNum][colNum] == gameBoard[rowNum+1][colNum] and rowNum != len(gameBoard):
                        killSwitch = 0
                except IndexError: pass
                if killSwitch == 0:
                    break
                try:
                    if gameBoard[rowNum][colNum] == gameBoard[rowNum][colNum-1] and colNum != 0:
                        killSwitch = 0
                except IndexError: pass
                if killSwitch == 0:
                    break
                try:
                    if gameBoard[rowNum][colNum] == gameBoard[rowNum-1][colNum] and rowNum != 0:
                        killSwitch = 0
                except IndexError: pass
                if killSwitch == 0:
                    break
                if gameBoard[rowNum][colNum] == 'X':
                    killSwitch = 0
                if killSwitch == 0:
                    break
        if killSwitch == 0:
            break
def findNeighborCells():
    global gameBoard
    global playerInput
    global cellSet
    cellSet.append([int(playerInput[0]), int(playerInput[1])])
    tempList = []
    oldLen = 0
    newLen = 1
    while newLen != oldLen:
        for cell in cellSet:
            try:                
                if gameBoard[int(cell[0])][int(cell[1])] == gameBoard[int(cell[0])+1][int(cell[1])] and int(cell[0]) != len(gameBoard):
                    tempList.append([int(cell[0])+1, int(cell[1])])
            except IndexError: pass
            try:
                if gameBoard[int(cell[0])][int(cell[1])] == gameBoard[int(cell[0])][int(cell[1])+1] and int(cell[1]) != len(gameBoard[int(cell[0])]):
                    tempList.append([int(cell[0]), int(cell[1])+1])
            except IndexError: pass
            try:
                if gameBoard[int(cell[0])][int(cell[1])] == gameBoard[int(cell[0])-1][int(cell[1])] and int(cell[0]) != 0:
                    tempList.append([int(cell[0])-1, int(cell[1])])
            except IndexError: pass
            try:
                if gameBoard[int(cell[0])][int(cell[1])] == gameBoard[int(cell[0])][int(cell[1])-1] and int(cell[1]) != 0:
                    tempList.append([int(cell[0]), int(cell[1])-1])
            except IndexError: pass
        for member in tempList:
            if member not in cellSet: cellSet.append(member)
        oldLen = newLen
        newLen = len(cellSet)
        tempList.clear
def moveDown():
    global gameBoard
    for a in range(len(gameBoard)):
        for b in range(len(gameBoard[a])):
            try:
                if gameBoard[a+1][b] == ' ':
                    gameBoard[a+1][b] = gameBoard[a][b]
                    gameBoard[a][b] = ' '
            except IndexError: pass
def playerInputCheck():
    global gameBoard
    global playerInput
    global inputPass
    try:
        gameBoard[int(playerInput[0])][int(playerInput[1])]
        if len(playerInput) != 2:
            raise InputLenError
        elif gameBoard[int(playerInput[0])][int(playerInput[1])] == ' ':
            raise EmptyError
    except IndexError:
        print("\nPlease enter a valid size!")
    except ValueError:
        print("\nYou've entered an invalid input type!\nPlease enter integers as row and column number!")
    except InputLenError:
        print("\nYou've entered an invalid input type!\nPlease enter only two integers as row and column number!")
    except EmptyError:
        print("\nPlease enter a valid size!")
    else: inputPass = 1
def removeEmptySpace():
    global gameBoard
    rowCounter = 0
    colCounter = 0
    removeList = []
    colList = []
    for l in range(len(gameBoard[0])):
        for t in range(len(gameBoard)):
            if gameBoard[t][l] == ' ':
                colCounter += 1
        if colCounter == len(gameBoard):
            colList.append(l)
        colCounter = 0
    for col in sorted(colList,reverse=True):
        for metric in gameBoard:
            metric.pop(col)
    """Above is for columns below is for rows"""
    for j in range(len(gameBoard)):
        for k in range(len(gameBoard[j])):
            if gameBoard[j][k] == ' ':
                rowCounter += 1
        if rowCounter == len(gameBoard[j]):
            removeList.append(j)
        rowCounter = 0
    for row in sorted(removeList,reverse=True):
        gameBoard.remove(gameBoard[row])
def removeCell():
    global cellSet
    global gameBoard
    for input in cellSet:
        gameBoard[input[0]][input[1]] = ' '
def ScoreCalculator(key):
    global cellSet
    global scoreDict
    global playerScore
    playerScore = playerScore + scoreDict[key]*len(cellSet)
def kaBOOM(loc1, loc2):
    global playerInput
    global gameBoard
    global playerScore
    global scoreDict
    collateralBombs = []
    gameBoard[loc1][loc2] = ' '
    for index1 in range(len(gameBoard)):
        if gameBoard[index1][loc2] == 'X':
            collateralBombs.append([index1, loc2])
        else:
            try:
                playerScore = playerScore + scoreDict[gameBoard[index1][loc2]]
            except KeyError:
                pass
            gameBoard[index1][loc2] = ' '
    for index2 in range(len(gameBoard[loc1])):
        if gameBoard[loc1][index2] == 'X':
            collateralBombs.append([loc1, index2])
        else:
            try:
                playerScore = playerScore + scoreDict[gameBoard[loc1][index2]]
            except KeyError:
                pass
            gameBoard[loc1][index2] = ' '
    if len(collateralBombs) > 0:
        for bombs in collateralBombs:
            return kaBOOM(bombs[0], bombs[1])
for line in inputFile:
    lineList = line.strip("\n").split(" ")
    gameBoard.append(lineList)
endGameCheck()
while killSwitch == 0:
    killSwitch = 1
    while inputPass == 0:
        print()
        displayGameBoard()
        print("\nYour score is: {}".format(playerScore))
        playerInput = input("\nPlease enter a row and column number: ").split(" ")
        playerInputCheck()
    inputPass = 0
    if gameBoard[int(playerInput[0])][int(playerInput[1])] == 'X':
        kaBOOM(int(playerInput[0]), int(playerInput[1]))
    else:
        dictKey = gameBoard[int(playerInput[0])][int(playerInput[1])]
        findNeighborCells()
        if len(cellSet) > 1:
            removeCell()
            ScoreCalculator(dictKey)
        else: print("\nYou selected a cell with no neighbors!")
    for l in range(len(gameBoard)):
        for t in range(len(gameBoard[l])):
            if gameBoard[l][t] != ' ':
                moveDown()
    removeEmptySpace()
    endGameCheck()
    cellSet.clear()
print()
displayGameBoard()
print("\nYour score is: {}\n\nGame Over!".format(playerScore))
    



    