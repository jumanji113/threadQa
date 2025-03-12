#!/bin/sh
test_file="src/test/resources/FailedTest.txt"

if [ -f "$test_file" ]; then
  if [ -s "$test_file" ]; then  # Добавлен пробел перед ] и символ ;
    ./gradlew testNGRun -x test $(cat $test_file)
  else
    echo "test file is empty"
  fi
else
  echo "test file does not exist"
fi