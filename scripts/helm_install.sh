#!/bin/bash

cd ..
export PROJECT_ROOT_DIRECTORY=$(pwd)
export VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

valid=false

while ! $valid; do
  read -r -p "Do you want to build and push docker image(y/n): " buildImage
  if [[ $buildImage == "y" || $buildImage == "n" ]]
  then
    valid=true
  fi
done

buildImage() {
  if [[ $buildImage == "y" ]]
  then
    cd ${PROJECT_ROOT_DIRECTORY}

    mvn clean build

    docker build -t molgergo01/bbg-pictures-repository-backend:${VERSION} .
    docker push molgergo01/bbg-pictures-repository-backend:${VERSION}
    echo "Successfully uploaded image to docker.io/molgergo01/bbg-pictures-repository-backend"
  fi
}

helmInstall() {
  cd ${PROJECT_ROOT_DIRECTORY}/charts

  helm package . --version 0.0.0
  helm upgrade --install bbg-pictures-repository-backend ./bbg-pictures-repository-backend-0.0.0.tgz --set image.tag=${VERSION}
}

buildImage
helmInstall
