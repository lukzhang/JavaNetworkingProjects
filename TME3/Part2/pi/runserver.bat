@echo off

rem start rmiregistry

	start rmiregistry

rem start server

	java -Djava.security.policy=server.policy engine.ComputeEngine
	