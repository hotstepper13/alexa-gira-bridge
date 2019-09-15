# Status
[![Build Status](https://travis-ci.org/hotstepper13/alexa-gira-bridge.svg?branch=master)](https://travis-ci.org/hotstepper13/alexa-gira-bridge)

# NOTE
Version 3.0 and onwards work natively with GiraHomeserver version 4.8 or newer and WITHOUT any additional requirement (no custom skill, no plugin in homeserver needed)

Older versions are no longer supported as the code base has changes completely

# alexa-gira-bridge
This is a proof of concept / personal application for device discovery using Hue IDs in order to connect with a custom logic module inside the Gira Homeserver in your local network without the need to any lamdba node or internet communication beside the alexa voice services itself.

~~You will need a custom logic controller in your gira homeserver. This project is able to work with https://github.com/Picpol/HS-AmazonEcho~~
~~You may see the (german) thread in the knx-user-forum https://knx-user-forum.de/forum/%C3%B6ffentlicher-bereich/knx-eib-forum/1010815-amazon-echo-logikbaustein~~

Execute without parameters to get usage information

## Requirements
- Memory: N/A
- Java: JDK8 (tested with Oracle JDK)
- Networkports: UDP/1900, TCP/4711 (configurable)

## Limitation
- Currently it is only possible to switch on/off and dim a light.
- If you have a push button activated device (like a scene) in your Gira Homeserver, be sure to attach it with Objecttype "on" or "off" instead of "onOff". The bridge will recognize it and send the correct command to trigger it.
- If you want to operate this software on a windows pc you must disable locally running upnp services. On windows 10 you have to change a registry value and reboot your system!

## Configuration
The version 3.0 or newer require a json formatted configuration to store the relevant data. This is needed because the gira bridge uses internal ID´s for the devices. Since a Device could have multiple group addresses for different actions, it was needed to create a mapping.
The configuration is split into 3 main parts:
### hsConfig
Contain the connection information to you local homeserver. The parameters should be self explaining.
### bridgeConfig
The ip can be empty and the software will try to determine its own ip. Only in special circumstances like forwarding it might be needed to enter the ip on which the service is reachable.
The setting in port defines on which port the application is listeing for requests.
### rooms
This is the main configuration. Each room containts items. The room name together with the item name determine the command you need to tell your echo to trigger an action.
Also the id of the room and the item will be used as unique identifier and must not be duplicated. If you add a room or items, ensure that the numbers are unique. See the configuration example for details.

The difference between idTrigger and idSwitch is that the switch will receive on and off commands while the trigger (may be used in scenes) only receives on requests.

## Running / "Installation"
- ensure that java can be found in path (test with "java --version")
- Download a releaseversion suitable for your environment (linux/windows)
- extract to a local directory
- edit the startscript for your system (*.bat for windows and *.sh for linux) and add your details
- execute the script
- As soon as everything is started, tell your Echo "Alexa, find my devices" (Deutsch: "Alexa, finde Geräte")

Hint: The Software will start in foreground. If you close the terminal or hit CTRL+C the application will be stopped.
I assume most people running this software are able to send a process to background, therefore this topic might be handled later.

If you want to run the software by yourself:

`java -jar <jarfile> --config-file <path-to-config-file> (Optional: --debug true)`

Optional Parameters:

no optional parameters (beside of debug) available. All settings go to the new config file.

### Change port of bridge software
If you want to run the software on another port for whatever reason, you can at least change the service port. (UPNP cannot be changed due to protocol)
The default port is 4711 but it is possible to choose a different port by adjusting it in the configuration file.
Note: You should provide a port number above 1024, otherwise you will need super user permissions to run the service!

### Docker
As requested within kns-user-forum a docker version of this software is now also available.
Remind that the software will try to find the IP where it is reachable. Under some circumstances it might happen that the application reports a wrong ip.
It might be needed to use host networking.

Further information can be found at: https://hub.docker.com/r/hotstepper13/alexa-gira-bridge/

UPDATE: Release 3.0 change so many things that the docker image has not yet verified! Especially the configuration must be provided, maybe as mount from the outside or custom docker image

## Updating Devicelist
With the change that all devices are now listed in the configuration, a restart of the bridge to re-read the configuration is needed.
Afterwards you may ask your echo to discover new items. 

## Donations
If you like this software I would appreciate a donation via PayPal: https://www.paypal.me/hotstepper13

~~Please keep in mind that this would not be possible without the Work of Picpol (who invented the Gira Logic Module that is used as backend). 
Details for donations to him can be found at his repository information: https://github.com/Picpol/HS-AmazonEcho~~


## Workflow
- As soon as the application is started, it will read the configuration and build an internal device structure.
- After issuing the Alexa/Echo device discovery ("Alexa find my devices" / "Alexa finde meine Geräte") the Echo will send upnp search requests via UDP Broadcast to your local network.
- The Bridge will respond (via UDP) to this search requests and send the needed information for Echo to discover all devices
- Now the Alexa/Echo will connect to the Bridge via TCP and fetching the details for existing devices.
- After the discovery process is finished Alexa will tell you how much devices are discovered.
- Now the Alexa/Echo is able to use the bridge. If you issue a command like "Alexa turn <roomname> <light> on"/"Alexa schalte <raumname> <licht> ein" Alexa will send a tcp request to the bridge and the bridge will send a tcp request to the Gira server. 

## Influences
This project was influenced by several other project. From some of them i "borrowed" some code and tried to mark it in the sources. In that case for this code passage the corrosponding licenses from the original project may apply.

- https://github.com/bwssytems/ha-bridge
- https://github.com/timfpark/hueBridge
- https://github.com/armzilla/amazon-echo-ha-bridge 

## Contributions
- skulawik: Implemented a Bugfix for non ssl discovery


