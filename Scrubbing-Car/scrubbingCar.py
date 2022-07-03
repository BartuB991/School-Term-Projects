def mapCreation(m):
    rows, cols = m+2, m+2
    arr = [["+" for i in range(cols)] for j in range(rows)]
    for k in range(m):
        for l in range(m):
            arr[k+1][l+1] = " "
    return arr
def burshPos(x):
            global initBrush
            if x == "1":
                initBrush = "on"
            else:
                initBrush = "off"
def movemDir(y,t):
    global initDir
    if y == "3" and t == "right":
        initDir = "down"
    elif y == "3" and t == "down":
        initDir = "left"
    elif y == "3" and t == "left":
        initDir = "up"
    elif y == "3" and t == "up":
        initDir = "right"
    elif y == "4" and t == "right":
        initDir = "up"
    elif y == "4" and t == "up":
        initDir = "left"
    elif y == "4" and t == "left":
        initDir = "down"
    else:
        initDir = "right"
def moveNpaint():
    global x1
    global y1
    global initDir
    global mov
    global initBrush
    global gameMap
    if initDir == "right":
        if initBrush == "on":
            for r in range(int(mov[1])+1):
                k[(y1+r)%gameMap[0]+1][x1+1] = "*"
        else:
            None
        y1 = (y1 + int(mov[1]))%gameMap[0]
        mov.clear()
    elif initDir == "left":
        if initBrush == "on":
            for n in range(int(mov[1])+1):
                k[(y1-n)%gameMap[0]+1][x1+1] = "*"
        else:
            None
        y1 = (y1 - int(mov[1]))%gameMap[0]
        mov.clear()
    elif initDir == "down":
        if initBrush == "on":
            for h in range(int(mov[1])+1):
                k[y1+1][(x1+h)%gameMap[0]+1] = "*"
        else:
            None
        x1 = (x1 + int(mov[1]))%gameMap[0]
        mov.clear()
    else :
        if initBrush == "on":
            for a in range(int(mov[1])+1):
                k[y1+1][(x1-a)%gameMap[0]+1] = "*"
        else:
            None
        x1 = (x1 - int(mov[1]))%gameMap[0]
        mov.clear()
def inputCheck():
    global k
    global conn
    global playerInput
    for z in range(len(conn)):
        if conn[z] == "1":
            continue
        elif conn[z] == "2":
            continue
        elif conn[z] == "3":
            continue
        elif conn[z] == "4":
            continue
        elif "5_" in conn[z]:
            continue
        elif conn[z] == "6":
            continue
        elif conn[z] == "7":
            continue
        elif conn[z] == "8":
            continue
        elif conn[z] == "0":
            continue
        else:
            print("You entered an incorrect command. Please try again!")
            playerInput = input("<-----Rules----->\n1. BRUSH UP\n2. BRUSH DOWN\n3. VEHICLE ROTATES RIGHT\n4. VEHICLE ROTATES LEFT\n5. (5_x) MOVE UP TO X\n6. JUMP THREE SQUARES\n7. REVERSE DIRECTION\n8. VIEW THE MATRIX\n0. EXIT\nPlease enter the commands with a plus (+) sign between them.\n")
            conn = playerInput.split("+")
            while '' in conn:
                conn.remove('')
            gameMap.append(int(conn[0]))
            conn.remove(conn[0])
            k = mapCreation(gameMap[0])
playerInput = input("<-----Rules----->\n1. BRUSH UP\n2. BRUSH DOWN\n3. VEHICLE ROTATES RIGHT\n4. VEHICLE ROTATES LEFT\n5. (5_x) MOVE UP TO X\n6. JUMP THREE SQUARES\n7. REVERSE DIRECTION\n8. VIEW THE MATRIX\n0. EXIT\nPlease enter the commands with a plus (+) sign between them.\n")
conn = playerInput.split("+")
gameMap =[]
x1 = 0
y1 = 0
while '' in conn:
    conn.remove('')
gameMap.append(int(conn[0]))
conn.remove(conn[0])
k = mapCreation(gameMap[0])
inputCheck()
initBrush = "off"
initDir = "right"
while int(len(conn)) > 0:
    for a in range(len(conn)):
        if conn[a] == "1" or conn[a] == "2":
            burshPos(conn[a])
            if conn[a] == "1":
                k[y1+1][x1+1] = "*"
        elif conn[a] == "3" or conn[a] =="4":
            movemDir(conn[a], initDir)
        elif "5_" in conn[a]:
            mov = conn[a].split("_")
            moveNpaint()
        elif conn[a] == "6":
            initBrush = "off"
            mov = ["", 3]
            moveNpaint()
            mov.clear()      
        elif conn[a] == "7":
            if initDir == "right":
                initDir = "left"
            elif initDir == "left":
                initDir = "right"
            elif initDir == "up":
                initDir = "down"
            else:
                initDir = "up"
        elif conn[a] == "8":
            for i3 in range(gameMap[0]+2):
                for j3 in range(gameMap[0]+2):
                    print(k[j3][i3], end="")
                print()
        elif conn[a] == "0":
            break
        else:
            None
    while "0" not in conn:
        conn.clear()
        playerInput = input("<-----Rules----->\n1. BRUSH UP\n2. BRUSH DOWN\n3. VEHICLE ROTATES RIGHT\n4. VEHICLE ROTATES LEFT\n5. (5_x) MOVE UP TO X\n6. JUMP THREE SQUARES\n7. REVERSE DIRECTION\n8. VIEW THE MATRIX\n0. EXIT\nPlease enter the commands with a plus (+) sign between them.\n")
        conn = playerInput.split("+")
        while '' in conn:
            conn.remove('')
        inputCheck
    break
