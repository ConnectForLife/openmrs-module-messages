#!/bin/bash

HOME_DIR=/media/use/Data4/repositories/
MESSAGES_REPO=$HOME_DIR/cfl/openmrs-module-messages
CALLFLOWS_OMOD=messages-1.0.0-SNAPSHOT.omod
CFL_REPO=$HOME_DIR/cfl/cfl-openmrs
MODULES=~/.cfl-dev/modules

cd $MESSAGES_REPO
mvn clean install -DskipTests -P no-npm,dev &&

sudo mv -f $MESSAGES_REPO/omod/target/$CALLFLOWS_OMOD $MODULES &&

cd $CFL_REPO/cfl/ &&
docker-compose down &&
docker-compose up &&

cd $MESSAGES_REPO
