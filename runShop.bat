@echo off
del /S *.class
javac -cp "dropbox.jar;jackson.jar;" -Xlint:unchecked *.java
java -cp "dropbox.jar;jackson.jar;" ShoppingListApp