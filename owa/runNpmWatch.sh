#!/bin/bash

HOME_DIR=~
OWA_PATH=$HOME_DIR/.cfl-dev/owa

check_ownership_and_fix () {
  PATH_TO_CHECK=$1
  if [ $(stat -c '%U' $PATH_TO_CHECK) != $(whoami) ] || [ $(find $PATH_TO_CHECK ! -user $(whoami) | wc -l) -gt 0 ]; then
    sudo chown -R $(whoami):$(whoami) $PATH_TO_CHECK
    echo "Changed ownership of $PATH_TO_CHECK to $(whoami):$(whoami)"
  fi
}

check_ownership_and_fix $OWA_PATH &&

npm run watch
