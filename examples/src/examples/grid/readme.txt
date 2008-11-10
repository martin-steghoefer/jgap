Howto run a grid with JGAP
--------------------------

The following refers to one specific example. By changing the class and package names you can run any other example
accordingly.

To use the grid, do the following in the given order:

1. Start the server on a machine of your choice (called SERVER):
java -cp ".;jgap-examples.jar;lib\log4j.jar;jgap.jar;lib\commons-cli-1.0.jar;lib\jcgrid.jar" org.jgap.distr.grid.JGAPServer

Ensure that the given jar-files reside in the directories you specify! Each required jar-file is shipped with JGAP!
Attention: For Linux and similar operating systems, you have to use the slash instead of the backslash (for Windows)
           --> Also is true for the following commands!

2. Start as many workers as you like on machines of your choice (following is one command line):
java -cp ".;jgap-examples.jar;lib\log4j.jar;jgap.jar;lib\commons-cli-1.0.jar;lib\jcgrid.jar" org.jgap.distr.grid.JGAPWorker -s SERVER -n myworker1

Ensure that the given jar-files reside in the directories you specify! Each required jar-file is shipped with JGAP!
Each worker must have a unique name --> specify it after the "-n" option.
Each worker must point to the SERVER's ip address --> specify it after the "-s" option instead of the above "SERVER".


3. Start a client on a machine of your choice
java -cp ".;jgap-examples.jar;lib\log4j.jar;jgap.jar;lib\commons-cli-1.0.jar;lib\jcgrid.jar" org.jgap.distr.grid.JGAPClient examples.grid.fitnessDistributed.GridConfiguration -s SERVER

The client must point to the SERVER's ip address --> specify it after the "-s" option instead of the above "SERVER".

After starting the client the server receives a task from the client.
After that the work is split into tasks and the started workers are asked by the server to compute them.
After a worker has computed a task, the result is sent back to the server.
The server in turn returns each solution to the client.
The client can process the result.


Also see http://jgap.sourceforge.net/doc/grid/localgrid.html for information about a LAN grid with JGAP.

If you have questions or comments, please use the forum at
http://sourceforge.net/forum/forum.php?forum_id=36521


Klaus Meffert