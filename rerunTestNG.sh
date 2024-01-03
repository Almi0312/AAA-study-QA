#!/bin/sh
test_file="src/test/resources/FailedTestsNG.txt"
if [ -f "$test_file" ]; then
  if [ -s "$test_file" ]; then
    ./gradlew clean testNGRun -x test $(cat $test_file)
  else
    echo "test file is empty"
  fi
fi

#Скрипт для рерана упавших тестов
#путь к файлу
#-f проверка на существование
#-s проверка на то, пустой ли файл
#вызов команды в терминале
#обязательная команда для закрытия if