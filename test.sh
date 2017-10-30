#!/bin/bash
set -e

VERSION="`cat esi-version/version`"
sbt new file://./esi-client.g8 --name=democlient --organization=com.example --scala_version=2.11.8 --esi_client_version=$VERSION
cd democlient
sbt compile
echo "it worked! let's bump the default"
cd ../esi-client.g8
sed -i 's/^esi_client_version = .*i$/esi_client_version = $VERSION/g' src/main/g8/default.properties
git config --global user.name "Concourse CI"
git config --global user.email "ci@pizza.moe"

if [ -z $(git status --porcelain) ];
then
    echo "No bump required"
else
    git commit -m "bumping default version to $VERSION"
fi

