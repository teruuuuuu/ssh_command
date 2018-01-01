#!/bin/bash

CURRENT_PATH=$(cd $(dirname $0) && pwd)
java -cp "${CURRENT_PATH}/../out/artifacts/ssh_command_jar/*"  command.SshCommand $@