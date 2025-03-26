# finadvice-bot

Deployment instruction (CentOS 7). 

I. Change params in application.properties file. 

II. If it is the first deployment to a server.

    1. Build a jar file. 
        mvn clean install 
    2. Move it to right folder on server (for example /home/dev/fabot). 
    3. Create an init.d service. 
        sudo ln -s /home/dev/tomcats/fabot/FinAdviceBot.jar /etc/init.d/fabot 
       Now the standard start, stop, restart and status commands can be used. 
    4. Start the service. 
        service fabot start 
    5. Configure the service auto-start at the system startup. 
        chkconfig --add fabot 
        chkconfig fabot on 
        chkconfig --list 

III. To update the already deployed service. 

    1. Build a jar file. 
        mvn clean install 
    2. Stop service. 
        service fabot stop 
    3. Replace old jar file with the new one. 
    4. Start the service. 
        service fabot start 

You can find logs in the file specified in application.properties or in /var/log/fabot file.
