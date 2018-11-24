@echo off
rem use >nul to supress output to console

rem global
del *.class /s >nul

rem compile compute classes
	javac compute/Compute.java compute/Task.java
	jar cvf compute.jar compute\*.class

rem compile server classes
	javac -cp compute.jar engine\ComputeEngine.java

rem compile client classes
	javac -cp compute.jar client\ComputePi.java client\Pi.java
