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

# With version 3.0 of this software a full configuration file contains all information
# you need to create the config on your own
CONFIG_FILE=~/.gira-bridge/configuration.json

# This setting will always be read from this configuration
DEBUG=false

#####################################################################################################
# DO NOT EDIT BELOW THIS LINE UNLESS YOU KNOW WHAT YOU ARE DOING                                    #
#####################################################################################################

# workaround if you try to start without changing directory first
SCRIPTPATH=`dirname "$0"`
cd ${SCRIPTPATH}

java -jar gira-bridge-${project.version}-jar-with-dependencies.jar --debug ${DEBUG} --config-file ${CONFIG_FILE}
