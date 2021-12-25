
start "Node" java -Xmx256m ^
	-Dlogging.config=log4j2-0.xml ^
	-Dlog4j.shutdownHookEnabled=false ^
	-Dserver.port=7777 ^
        -Dspring.profiles.active=dev ^
	-Djava.net.preferIPv4Stack=true ^
	-jar service.jar
