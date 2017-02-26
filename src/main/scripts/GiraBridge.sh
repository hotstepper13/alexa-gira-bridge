#!/bin/bash
#*******************************************************************************
# Copyright (C) 2017  Frank Mueller
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#*******************************************************************************

#
# add your details after the equals
# if you have done this before, the settings should
# be saved in the home directory of the run user
#

# These settings may be read from file ~/.gira-bridge/settings.conf
HOMESERVER_IP=
HOMESERVER_PORT=
TOKEN=
ENABLE_SSL=true
HTTP_IP=""

# This setting will always be read from this configuration
DEBUG=false

#####################################################################################################
# DO NOT EDIT BELOW THIS LINE UNLESS YOU KNOW WHAT YOU ARE DOING                                    #
#####################################################################################################

SAVE_CONFIG_DIR=~/.gira-bridge
SAVE_CONFIG_FILE=${SAVE_CONFIG_DIR}/settings.conf

if [ -f ${SAVE_CONFIG_FILE} ] ; then
  echo "Loading config from  ${SAVE_CONFIG_FILE}"
  source ${SAVE_CONFIG_FILE}
fi

if [ ! -d ${SAVE_CONFIG_DIR} ] ; then
  echo "Create configuration directory ${SAVE_CONFIG_DIR}"
  mkdir ${SAVE_CONFIG_DIR}
fi

if [ ! -f ${SAVE_CONFIG_FILE} ] ; then
  if [ -n "${HOMESERVER_IP}" ] && [ -n "${HOMESERVER_PORT}" ] && [ -n "${TOKEN}" ] && [ -n "${DEBUG}" ] && [ -n "${ENABLE_SSL}" ] && [ -z "${LOADEDCONFIG}" ]; then
    echo "Save config to ${SAVE_CONFIG_FILE}"
    echo "HOMESERVER_IP=${HOMESERVER_IP}" > ${SAVE_CONFIG_FILE}
    echo "HOMESERVER_PORT=${HOMESERVER_PORT}" >> ${SAVE_CONFIG_FILE}
    echo "TOKEN=${TOKEN}" >> ${SAVE_CONFIG_FILE}
    echo "ENABLE_SSL=true" >> ${SAVE_CONFIG_FILE}
    echo "HTTP_IP=\"\"" >> ${SAVE_CONFIG_FILE}
    echo "LOADEDCONFIG=1" >> ${SAVE_CONFIG_FILE}
  fi

fi


java -jar gira-bridge-${project.version}-jar-with-dependencies.jar --homeserver-ip ${HOMESERVER_IP} --homeserver-port ${HOMESERVER_PORT} --token ${TOKEN} --debug ${DEBUG} --enable-ssl ${ENABLE_SSL} --http-ip ${HTTP_IP}

