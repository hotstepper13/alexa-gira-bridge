# alexa-gira-bridge
This is a proof of concept / personal application for device discovery using Hue IDs in order to connect with a custom logic module inside the Gira Homeserver in your local network without the need to any lamdba node or internet communication beside the alexa voice services itself.

You will need a custom logic controller in your gir homeserver. This project is able to work with https://github.com/Picpol/HS-AmazonEcho
You may see the (german) thread in the knx-user-forum https://knx-user-forum.de/forum/%C3%B6ffentlicher-bereich/knx-eib-forum/1010815-amazon-echo-logikbaustein

Execute without parameters to get usage information

# Requirements
- Memory: N/A
- Java: JDK8 (tested with Oracle JDK)
- Networkports: UDP/1900, TCP/4711

# Limitation
- Currently it is only possible to switch on/off a light.
- If you have a push button activated device (like a scene) in your Gira Homeserver, be sure to attach it with Objecttype "on" or "off" instead of "onOff". The bridge will recognize it and send the correct command to trigger it.
- If you want to operate this software on a windows pc you must disable locally running upnp services. On windows 10 you have to change a registry value and reboot your system!

# Planned / Roadmap
- Dimming
- Temperature (Maybe have to look around for a useful api) 
- Enhanced Configuration options

# Workflow
- As soon as the application is started, it will connect to the gira homeserver via TCP, fetch the devicelist and build an internal device structure.
- After issuing the Alexa/Echo device discovery ("Alexa find my devices" / "Alexa finde meine Geräte") the Echo will send upnp search requests via UDP Broadcast to your local network.
- The Bridge will respond (via UDP) to this search requests and send the needed information for Echo to discover all devices
- Now the Alexa/Echo will connect to the Bridge via TCP and fetching the details for existing devices.
- After the discovery process is finished Alexa will tell you how much devices are discovered.
- Now the Alexa/Echo is able to use the bridge. If you issue a command like "Alexa turn <roomname> <light> on"/"Alexa schalte <raumname> <licht> ein" Alexa will send a tcp request to the bridge and the bridge will send a tcp request to the Gira server. 

# Influences
This project was influenced by several other project. From some of them i "borrowed" some code and tried to mark it in the sources. In that case for this code passage the corrosponding licenses from the original project may apply.

- https://github.com/bwssytems/ha-bridge
- https://github.com/timfpark/hueBridge
- https://github.com/armzilla/amazon-echo-ha-bridge 

