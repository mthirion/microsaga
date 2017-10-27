# Microservices SAGA pattern example

## Introduction
This is a basic example of the micro-services SAGA pattern.  
It's just aimed at introducing the concept with some concrete materials

## Overview
This example shows a new user opening a bank account in a new bank.  
The principle consists in having separate microservices to handle users, accounts and cards information but also having all those elements handled in an atomic transaction.

## Saga pattern
A micro-services world is polyglot.  
It involves services written in different languages and writing into different storage technology.  
Not all of them will even understand the concept of a transaction, so a 2PC kind of transaction is not possible within a micro-services architecture.  
   
The "atomicity" therefore relies on the SAGA pattern.  
The management of the Users, Accounts and Cards information is organized in a sequence of events rather than being synchronized in parallel actions.  
The error recovery mechanism is rather based on compensations than rollback mechanisms.  

The SAGA pattern implies:  

 * that we are able to identify "retryable" vs "non-retryable" errors.  Retryable errors will be tried again; non retryable errors will be compensated  

 * that the services are idempotents  
this is because a service can be retried (retryable errors) while the concept of a rollback is not at use so data could have already been persisted before the retry  

 * that the entire system is thought with "eventual consistency" in mind  
once again, because there is no rollbacks but compensations, data can be persisted and then deleted, leading to a window where the information is inconsistent

## Components
The system consists in:

 * UI  
   The UI is written in Angular, where the user can enter its details and choose a bank account type and credit card type (visa/mastercard)  
Submitted data are sent over to the gateway with a ReST call

   >> Source = $PROJECT_FOLDER/ui/src/main/angular/page.html


 * gateway  
   The gateway is written in Java & Spring-boot  
The gateway receives the ReST call from the UI, create a transaction ID and an event message sent to the "events source"
   
   >> ReST URL = http://localhost:8080/      (API = /api/open_account)


 * Events source  
The events source is an activeMQ v6 VirtualTopic named EventsSource.  
The micro-services subscribe to that unique recipient to different event types using JMS selectors.  The event name is thus a JMS header

   >> ActiveMQ endpoint = localhost:61616   (Topic.VirtualTopic.EventsSource)


 * A users service
   The Users service is a Camel XML service under Spring-Boot.  
It generates a new clientID for the user and writes Users information into a Mongo DB database.  
It reads events from the event source using 2 subscriptions:
    - OPEN_ACCOUNT to register a new user
    - ACCOUNT_COMPENSATED to delete a user  

    - NEW_USER_CREATED (upon success) 
    - USER_COMPENSATED (upon failure)

   >> MongoDB URL = localhost:27017  (DBName = fis2demo ; Collection = Users) 


 - An accounts service  
   The Accounts service is a Camel DSL service under Wildfly Swarm  
   It generates a new account number linked to the clientID and writes Accounts information into a Mongo DB database  
   It reads events from the event source using 2 subscriptions:  
   - NEW__USER_CREATED to create a new account for the user  
- CARD_COMPENSATED to delete an account  
- NEW__ACCOUNT_CREATED (upon success)  
- ACCOUNT_COMPENSATED (upon failure)
   
   >> MongoDB URL = localhost:27017  (DBName = fis2demo ; Collection = Accounts) 


 - A card service  
   The Cardss service is a Camel DSL service under Spring-boot  
   It generates a new card number linked to the account number and named to the client name and writes Cards information into a MySQL database  
   It reads events from the event source using 2 subscriptions:  
    - NEW__ACCOUNT_CREATED to create a new card linked to that account
    - CARD__IN_ERROR to delete a card  
    - NEW__CARD_CREATED (upon success)
    - CARD_COMPENSATED (upon failure)

   >> MySQL URL = localhost:3306  (DBName = fis2demo ; Table = Cards)  
   >> Table details:  
        CardNumber    | varchar(255)   
	ClientID      | varchar(255)   
	AccountID     | varchar(255)  
	CardType      | varchar(255)  
	TransactionID | varchar(255)  

 - A monitor  
   The monitor service is a java Spring-boot service.  
   It subscribes to all events and log them in both a MySQL table and a log file.  
   
   >> MySQL URL = localhost:3306  (DBName = fis2demo ; Table = EventsTable)  
   >> Table details:  
	eventID   | int(6)         
	eventName | varchar(255)   
	data      | text          
	ownership | varchar(255)  
 
 - A Viewer service  
   The viewer service is a spring-boot service.  
   It reads events from the EventsTable and write only some parts of the information to another MySQL table for other services to read them  
   This component is there as an implementation of the CQRS pattern  

   >> MySQL URL = localhost:3306  (DBName = fis2demo ; Table = MView)  
   >> Table details:  
	clientNumber  | varchar(255)   
	name          | varchar(255)  
	firstname     | varchar(255)  
	accountNumber | varchar(255)  
	cardNumber    | varchar(255)  

 
 - A viewer GUI  
   The GUI is written in PHP and displays information from the MView Table.  
   The PHP code can be run on Nginx with the php-fpm plugin.  

   >> PHP URL = localhost:8099/myphp.php


# Demo

## Setup
   1) start activeMQ  
      create the VirtualTopic "EventsSource"

   2) start MongoDB   
      create the database "fis2demo"  
      create the 2 collections "Users" and "Accounts" 

   3) start MySQL  
      create the fis2demo database  
      create the Cards table  
      create the EventsTable table  
      create the MView table  

   4) start an nginx server  
      use a configuration similar to this  

	      server {
	        listen       8099;
	        server_name  localhost;
	
	        root    $CHANGE_TO_NGINX_ROOT/www;

	        location ~ \.php$ {
	          try_files $uri =404;
	           root www/fis2demo;
	            fastcgi_split_path_info ^(.+\.php)(/.*)$;
	            fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
	            fastcgi_pass   127.0.0.1:9000;
	            include        fastcgi_params;
	        }
	      }

  4b) install and start the php-fpm CGI server on the port 9000  
	- yum install php-fpm  
	- systemctl start php-fpm  

4c) place the $PROJECT_FOLDER/viewer/src/main/php/myphp.php file in CHANGE-TO-NGINX-ROOT/www/fis2demo/

   4d) disable selinux (setenforce 0) OR :  
- adapt the selinux owner of the myphp.php file (chcon -R -t httpd_sys_content_t $CHANGE-TO-NGINX-ROOT/www/fis2demo/myphp.php)  
- setsebool -P httpd\_can\_network\_connect\_db 1  
   5)
      For all the components (gateway, users, accounts, cards, monitor, viewer):
        - mvn package
        - java -jar $PROJECT\_FOLDER/COMPONENT/target/<component.jar>  (component-swarm.jar for account)

## Exercice
   a) Run the example (submit data from the Angular UI and check the PHP GUI)  
      Observe the logs of the event monitor

   b) Modify the code of the Card service and uncomment the exception thrown and the delay() lines  
      Recompile and restar the Card service  
      Run the example again with different user information.    
      There is a delay of 20sec before the Card service fails  
      During that time you should see partial information (eventual consistency) in the PHP guy.  
      After the detail, the compensation kick in and the data should disappear from the PHP guy  
      Observe the logs of the events monitor and/or the messages within the EventsSource topic

####Author  
Michael thirion  
mthirion@redhat.com

