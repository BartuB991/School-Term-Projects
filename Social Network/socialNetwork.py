import sys
smn = open(sys.argv[1], "r")
commands = open(sys.argv[2], "r")
outputFile = open("output.txt","w")
socialNetwork = {}
def nouvelleUser(user):
    global socialNetwork
    global outputFile
    if user in socialNetwork.keys():
        outputFile.write("ERROR: Wrong input type! for 'ANU'! -- This user already exists!!\n")
    else:
        socialNetwork[user] = []
        outputFile.write("User '{}' has been added to the social network successfully\n".format(user))
def eliminerUser(networkUser):
    global socialNetwork
    global outputFile
    if networkUser not in socialNetwork.keys():
        outputFile.write("ERROR: Wrong input type! for 'DEU'!--There is no user named '{}'!!\n".format(networkUser))
    else:
        socialNetwork.pop(networkUser)
        for value in socialNetwork.values():
            if networkUser in value:
                value.remove(networkUser)
            else: continue
        outputFile.write("User '{}' and his/her all relations have been deleted successfully\n".format(networkUser))
def nouvelAmi(sourceUser, targetUser):
    global socialNetwork
    global outputFile
    if sourceUser not in socialNetwork.keys() and targetUser not in socialNetwork.keys():
        outputFile.write("ERROR: Wrong input type! for 'ANF'!--No user named '{}' and '{}' found!\n".format(sourceUser, targetUser))
    elif targetUser not in socialNetwork.keys():
        outputFile.write("ERROR: Wrong input type! for 'ANF'!--No user named '{}' found!\n".format(targetUser))
    elif sourceUser not in socialNetwork.keys():
        outputFile.write("ERROR: Wrong input type! for 'ANF'!--No user named '{}' found!\n".format(sourceUser))
    else:
        if targetUser in socialNetwork[sourceUser]:
            outputFile.write("ERROR: A relation between '{}' and '{}' already exists!!\n".format(sourceUser, targetUser))
        else:
            socialNetwork[sourceUser].append(targetUser)
            socialNetwork[sourceUser].sort
            socialNetwork[targetUser].append(sourceUser)
            socialNetwork[targetUser].sort
            outputFile.write("Relation beetween '{}' and '{}' has been added successfully\n".format(sourceUser, targetUser))
def pasDami(source, target):
    global socialNetwork
    global outputFile
    if source not in socialNetwork.keys() and target not in socialNetwork.keys():
        outputFile.write("ERROR: Wrong input type! for 'DEF'!--No user named '{}' and '{}' found!\n".format(source, target))
    elif target not in socialNetwork.keys():
        outputFile.write("ERROR: Wrong input type! for 'DEF'!--No user named '{}' found!\n".format(target))
    elif source not in socialNetwork.keys():
        outputFile.write("ERROR: Wrong input type! for 'DEF'!--No user named '{}' found!\n".format(source))
    else:
        if target not in socialNetwork[source]:
            outputFile.write("ERROR: No relation between '{}' and '{}' found!!\n".format(source, target))
        else:
            socialNetwork[source].remove(target)
            socialNetwork[source].sort
            socialNetwork[target].remove(source)
            socialNetwork[target].sort
            outputFile.write("Relation beetween '{}' and '{}' has been deleted successfully\n".format(source, target))
def nombreDamis(userWithFriends):
    global socialNetwork
    global outputFile
    if userWithFriends not in socialNetwork.keys():
        outputFile.write("ERROR: Wrong input type! for 'CF'!--No user named '{}' found!\n".format(userWithFriends))
    else:
        outputFile.write("User '{}' has {} friends\n".format(userWithFriends, len(socialNetwork[userWithFriends])))
def amisPossibles(userWithPF, maxDist):
    global socialNetwork
    global outputFile
    possibleFriendsList = []
    possibleFriends = set()
    i = 0
    if userWithPF in socialNetwork and maxDist in range(1,4):
        for j in socialNetwork[userWithPF]:
            possibleFriends.add(j)
        i += 1
        while maxDist > i:
            possibleFriendsList.extend(sorted(possibleFriends))
            for k in possibleFriendsList:
                for l in socialNetwork[k]:
                    if l == '':
                        continue
                    else: possibleFriends.add(l)
            i += 1
        if userWithPF in possibleFriends:
            possibleFriends.discard(userWithPF)
        else: pass
        outputFile.write("User '{}' has {} possible friends when maximum distance is {}\n".format(userWithPF, len(possibleFriends), maxDist))
        outputFile.write("These possible friends: '{}'\n".format(("', '".join(sorted(possibleFriends)))))
    else:
        if userWithPF not in socialNetwork and maxDist not in range(1, 4):
            outputFile.write("ERROR: Wrong input type! for 'FPF'!--No user named '{}' found!\n".format(userWithPF))
            outputFile.write("ERROR: Invalid input for 'FPF'!--Maximum distance must be between 1 and 3(both included)\n")
        elif userWithPF not in socialNetwork:
            outputFile.write("ERROR: Wrong input type! for 'FPF'!--No user named '{}' found!\n".format(userWithPF))
        else:
            outputFile.write("ERROR: Maximum distance is out of range!!\n")
    possibleFriends.clear()
def proposerAmis(userToConnect, mutDegr):
    global socialNetwork
    global outputFile
    if userToConnect in socialNetwork.keys() and mutDegr in range(1,5):    
        mutualFriendsDict = {}
        for prospect in socialNetwork:
            if prospect not in socialNetwork[userToConnect]:
                t = 0
                for d in socialNetwork[userToConnect]:
                    if d in socialNetwork[prospect]:
                        t += 1
                mutualFriendsDict[prospect] = [t]
            else: continue
        outputFile.write("Suggestion list for '{}' (When MD is {}):\n".format(userToConnect, mutDegr))
        suggestedFriends = []
        mutualFriendsDict.pop(userToConnect)
        for f in range(mutDegr, 5):
            for key in sorted(mutualFriendsDict.keys()):
                if mutualFriendsDict[key][0] == f:
                    outputFile.write("'{}' has {} mutual friends with {}\n".format(userToConnect, f, key))
                    suggestedFriends.append(key)
                else: continue
        outputFile.write("The suggested friends for '{}' : '{}'\n".format(userToConnect,"', '".join(sorted(suggestedFriends))))
        mutualFriendsDict.clear()
        suggestedFriends.clear()
    elif userToConnect not in socialNetwork.keys(): 
        outputFile.write("Error: Wrong input type! for 'SF'!--No user named '{}' found!!\n".format(userToConnect))
    elif mutDegr not in range(1,5):
        outputFile.write("Error: Mutually degree cannot be less than 1 or greater than 4\n")
    else: "Error: Wrong input type for 'SF'!-- None of the given parameters match the required ones!!"
for line in smn:
    user = line.strip("\n").split(":")
    socialNetwork[user[0]] = user[1].split(" ")
    user.clear()
for values in socialNetwork.values():
    if values == '':
        socialNetwork.pop(values)
    else: continue
outputFile.write("Welcome to Assignment 3\n")
outputFile.write("{}\n".format("-"*32))
for newLine in commands:
    tempCommand = newLine.strip("\n").split(" ")
    if tempCommand[0] == "ANU":
        nouvelleUser(tempCommand[1])
    elif tempCommand[0] == "DEU":
        eliminerUser(tempCommand[1])
    elif tempCommand[0] == "ANF":
        nouvelAmi(tempCommand[1], tempCommand[2])
    elif tempCommand[0] == "DEF":
        pasDami(tempCommand[1], tempCommand[2])
    elif tempCommand[0] == "CF":
        nombreDamis(tempCommand[1])
    elif tempCommand[0] == "FPF":
        amisPossibles(tempCommand[1], int(tempCommand[2]))
    elif tempCommand[0] == "SF":
        proposerAmis(tempCommand[1], int(tempCommand[2]))
    tempCommand.clear()