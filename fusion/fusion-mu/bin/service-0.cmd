
set IGNITE_UPDATE_NOTIFIER=false

start "Node" java -Xmx256m ^
	-Dlogging.config=log4j2-0.xml ^
	-Dlog4j.shutdownHookEnabled=false ^
	-Dserver.port=7701 ^
        -Dspring.profiles.active=dev ^
	-Djava.net.preferIPv4Stack=true ^
	-Dreactor.netty.ioWorkerCount=4 ^
	-Dreactor.schedulers.defaultPoolSize=4 ^
	-jar service.jar
