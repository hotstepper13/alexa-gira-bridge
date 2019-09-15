@rem ***************************************************************************
@rem Copyright (C) 2017  Frank Mueller
@rem
@rem This program is free software: you can redistribute it and/or modify
@rem it under the terms of the GNU General Public License as published by
@rem the Free Software Foundation, either version 3 of the License, or
@rem (at your option) any later version.
@rem
@rem This program is distributed in the hope that it will be useful,
@rem but WITHOUT ANY WARRANTY; without even the implied warranty of
@rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
@rem GNU General Public License for more details.
@rem
@rem You should have received a copy of the GNU General Public License
@rem along with this program.  If not, see <http://www.gnu.org/licenses/>.
@rem ***************************************************************************
@ECHO OFF


@rem add your details after the equals
SET CONFIG_FILE=config.json
SET DEBUG=false

@rem DO NOT EDIT BELOW THIS LINE UNLESS YOU KNOW WHAT YOU ARE DOING


java -jar gira-bridge-${project.version}-jar-with-dependencies.jar --debug %DEBUG% --config-file %CONFIG_FILE%

