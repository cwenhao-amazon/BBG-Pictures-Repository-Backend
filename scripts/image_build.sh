#!/bin/bash

cd ..

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

docker build -t molgergo01/bbg-pictures-repository-backend:${VERSION} .
docker push molgergo01/bbg-pictures-repository-backend:${VERSION}
echo "Successfully uploaded image to docker.io/molgergo01/bbg-pictures-repository-backend"