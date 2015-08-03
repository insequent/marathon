# Dockerfile for the Apache Mesos-utils
# Outputs a jar file

FROM ubuntu:14.04
MAINTAINER EMC Rubicon <autobots@emc.com>

RUN locale-gen --no-purge en_US.UTF-8
ENV LC_ALL en_US.UTF-8

RUN sudo apt-key adv --keyserver keyserver.ubuntu.com --recv E56151BF
RUN echo "deb http://repos.mesosphere.io/ubuntu trusty main" > /etc/apt/sources.list.d/mesosphere.list
RUN echo "deb http://dl.bintray.com/sbt/debian /" > /etc/apt/sources.list.d/sbt.list

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --force-yes \
    build-essential \
    git \
    openjdk-7-jdk \
    openjdk-7-jre-headless \
    ruby \
    ruby-dev \
    sbt

RUN gem install fpm

ENV BUILD_WORKPLACE /var/workplace
ENV BUILD_OUTPUT $BUILD_WORKPLACE/target/

ADD ./ $BUILD_WORKPLACE

WORKDIR $BUILD_WORKPLACE
RUN sbt update
RUN mkdir deb_extract
RUN apt-get download marathon && dpkg -x marathon_*_amd64.deb deb_extract/ 

CMD cd $BUILD_WORKPLACE && sbt assembly
