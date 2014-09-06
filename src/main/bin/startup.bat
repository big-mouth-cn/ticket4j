@echo off
"%~dp0jre\bin\java" -Xms256m -Xmx512m -XX:PermSize=64m -XX:MaxPermSize=64m -XX:+HeapDumpOnOutOfMemoryError -jar ../lib/bigmouth-ticket4j-1.0.5.jar & pause