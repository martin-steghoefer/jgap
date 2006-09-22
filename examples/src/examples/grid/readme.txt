To use the grid, do the following in the given order:

1. Start the server on a machine of your choice (called SERVER):
java -cp ".;jgap-examples.jar;lib\log4j.jar;jgap.jar;lib\commons-cli-1.0.jar;lib\jcgrid.jar" examples.grid.JGAPServer

Ensure that the given jar-Files reside in the directories you specify! Each required jar-File is shipped with JGAP!


2. Start as many workers as you like on machines of your choice:
java -cp ".;jgap-examples.jar;lib\log4j.jar;jgap.jar;lib\commons-cli-1.0.jar;lib\jcgrid.jar" examples.grid.JGAPWorker -s SERVER -n myworker1

Ensure that the given jar-Files reside in the directories you specify! Each required jar-File is shipped with JGAP!
Each worker must have a unique name --> specify it after the "-n" option
Each worker must point to the SERVER's ip address --> specify it after the "-s" option

3. Start a client on a machine of your choice
java -cp ".;jgap-examples.jar;lib\log4j.jar;jgap.jar;lib\commons-cli-1.0.jar;lib\jcgrid.jar" examples.grid.JGAPClient -s SERVER

The client must point to the SERVER's ip address --> specify it after the "-s" option

After starting the client the server receives a task from the client.
After that the server splits the work into tasks and asks the started workers to compute them.
After a worker has computed a task, the result is sent back to the server.
The server in turn returns each solution to the client.

Also see http://jgap.sourceforge.net/doc/grid/localgrid.html for information about a LAN grid with JGAP.

If you have questions, please use the forum at
http://sourceforge.net/forum/forum.php?forum_id=36521


Klaus Meffert