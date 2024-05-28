# json-database
Server-client application for sending and receiving data in Json-format

Hyperskill project "JSON database with java" (Java Developer). Stage 6/6.

Java project with Maven as build tool. 

The application takes a client request from command line, processes the request on a server, and then receives 
a response back. The command line argument can either be written manually, or be read from a file. 

The library JCommander is used to parse the command line arguments, and Gson is used to serialize/deserialize the
request and response. 

The application is able to store both primitive values (numbers, text etc) and Json-objects. The search key can consist of
multiple key-values, and based on this the program will iterate through the database until it reaches the correct key value, 
where the request will be handled (SET, GET or DELETE). 

Example of command line argument: 

  1. -t set -k "Some key" -v "Here is some text to store on the server": Stores a new value at the given key. If the key-value
     does not exist, it will be created and the value-variable will be stored under the new key. 
     
  2. -in getFile.json: reads a get-request from the file "getFile.json". 
  
  3. -t get -k ["person", "name"]: returns the value that is stored under the key "name", by traversing through the key-value 
     "person" first. 
  
The server shuts down when a client sends an exit-request: -t exit.

