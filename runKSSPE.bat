
javac -d classes -classpath classes; common\*.java database\*.java event\*.java exception\*.java model\*.java impresario\*.java userinterface\*.java controller\*.java utilities\*.java KSSPE.java
java -cp mysql-connector-java-5.1.7-bin.jar;classes;. KSSPE