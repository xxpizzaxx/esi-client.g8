#!/bin/bash
VERSION="`cat esi-version/version.sbt`"
sbt new file://./esi-client.g8 --name=democlient --organization=com.example --scala_version=2.11.8 --esi_client_version=$VERSION
