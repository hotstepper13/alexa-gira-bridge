# Status
[![Build Status](https://travis-ci.org/hotstepper13/alexa-gira-bridge.svg?branch=master)](https://travis-ci.org/hotstepper13/alexa-gira-bridge)

# alexa-gira-bridge
This is a proof of concept / personal application for device discovery using Hue IDs in order to connect with a custom logic module inside the Gira Homeserver in your local network without the need to any lamdba node or internet communication beside the alexa voice services itself.

You will need a custom logic controller in your gir homeserver. This project is able to work with https://github.com/Picpol/HS-AmazonEcho
You may see the (german) thread in the knx-user-forum https://knx-user-forum.de/forum/%C3%B6ffentlicher-bereich/knx-eib-forum/1010815-amazon-echo-logikbaustein

Execute without parameters to get usage information

## Requirements
- Memory: N/A
- Java: JDK8 (tested with Oracle JDK)
- Networkports: UDP/1900, TCP/4711

## Limitation
- Currently it is only possible to switch on/off and dim a light.
- If you have a push button activated device (like a scene) in your Gira Homeserver, be sure to attach it with Objecttype "on" or "off" instead of "onOff". The bridge will recognize it and send the correct command to trigger it.
- If you want to operate this software on a windows pc you must disable locally running upnp services. On windows 10 you have to change a registry value and reboot your system!

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

`java -jar <jarfile> --homeserver-ip <homeserverIp> --homeserver-port <homeserverPort> --token <token> (Optional: --debug true)`

Optional Parameters:

Enable ssl for bridge<->Homeserver communication (Default true)
`--enable-ssl false`

Overwrite ip address for discovery reponse (useful while running behind a proxy or in docker, default empty)
`--http-ip <reachable-ip-address>`

### Docker
As requested within kns-user-forum a docker version of this software is now also available.
Remind that the software will try to find the IP where it is reachable. Under some circumstances it might happen that the application reports a wrong ip.
It might be needed to use host networking.

Further information can be found at: https://hub.docker.com/r/hotstepper13/alexa-gira-bridge/

## Updating Devicelist
With Release 2.0.11 it is possible to update the devicelist in a running system.
1. Tell Echo to start discovery by issuing the command "Alexa, turn Discovery on" (Deutsch: "Alexa, schalte Discovery ein")
2. After you hear the ok from Echo update the Echo Devicelist by telling "Alexa, find my devices" (Deutsch: "Alexa, finde Geräte")

**Hint: Sometimes it might be needed to discard all found devices in the Alexa App in order to Match the IDs.**

## Donations
If you like this software I would appreciate a donation via PayPal: https://www.paypal.me/hotstepper13

Please keep in mind that this would not be possible without the Work of Picpol (who invented the Gira Logic Module that is used as backend). Details for donations to him can be found at his repository information: https://github.com/Picpol/HS-AmazonEcho

## Planned / Roadmap
- ~~Dimming~~ (fully supported since 2.0.4)
- Temperature (Maybe have to look around for a useful api) 
- ~~update devices without restarting (with a voice command?!)~~ (fully supported since 2.0.11)

## Workflow
- As soon as the application is started, it will connect to the gira homeserver via TCP, fetch the devicelist and build an internal device structure.
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

