This python program creates and stores a simplified social network with users and
the connections between users. It reads two .txt files, the first one provides the users
and their already existing connections and the second file provides the commands. Commands 
include ANU(Add New User), DEU(Delete Existing User), ANF(Add New Friend), DEF(Delete Existing Friend),
CF(Count Friends) which provides the number of friends for the specified user, FPF(Find Possible Friends)
and SF(Suggest Friends). It outputs the actions carried as a .txt. It also runs checks before every action
to make sure that action is possible, because as you already know, you can't add a non-existing person as your friend
or can't delete a person from your friend list if they're not your friend. If the program detects a command not possible 
to carry out, it writes the specified error message to the .txt file.
