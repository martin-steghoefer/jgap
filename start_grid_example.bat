rem
rem Please start "start_grid_server.bat", then "start_grid_worker.bat" and then this file!
rem
java -cp ".;jgap.jar;jgap-examples.jar;lib/jcgrid.jar;lib/log4j.jar;lib/commons-cli-1.0.jar;lib/commons-lang-2.1.jar;lib/commons-cli-1.2.jar" org.jgap.distr.grid.JGAPClient examples.grid.fitnessDistributed.GridConfiguration
pause