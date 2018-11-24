@echo off

rem run client

	java -Djava.security.policy=client.policy client.ComputePi localhost %1 %2
	