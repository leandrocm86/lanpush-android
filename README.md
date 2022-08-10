# LANPUSH
### Share texts and links through LAN


LANPUSH is a utility to exchange text messages between devices in the same local network.<br>
<u>No internet required! No login!</u> Just send a message and all configured devices in the same LAN will be notified with it.

**LANPUSH is available for PCs with Java (Windows/Linux/Mac) and phones with Android.**<br>
For the PC client, check the project on [https://github.com/leandrocm86/lanpush](https://github.com/leandrocm86/lanpush)<br>
Both projects are free, open sourced, have no ads, and are open to suggestions.

#### Use cases:
- Share a link between PC and phone:
Tired of copying/pasting (and copying/pasting again) online notes to share links or texts between your devices?
LANPUSH allows you to quickly move from one device to another.
Once a message is sent, all PCs and Android phones on the same wifi will be able to show a notification with the new message, along with the option to copy it or browse it.

- Server automation:
With LANPUSH your server can easily send you warnings even when no internet is available.
You can also go the other way around, sending commands for the server to read and start your tasks.
Tired of going through SSH every time just to start trivial jobs? Make your server act upon the messages it receives. You can configure lanpush to log messages on a predefined file, and also choose IPs and ports to listen to.

#### Usage:
After installing and opening the app, a simple window will display received messages and relevant log info.
On the bottom, there's a button to open an input field that you can use to type messages and send them to your LAN. Notice that's not the only way of sending messages. When you select texts on other apps, there will be a new "LANPUSH" option to send it. You can also share links from other apps.

There's also a *settings* option on the top right corner in which you may configure the desired local IP for sending messages to. By default, the app sends to the IP 192.168.0.255, which is a common bradcast IP, but you may have to change it.
You can also change the UDP port it uses. By default, the app will read any message arriving in port 1050, and will also send messages through this same port.

It's not necessary to keep the app on the foreground. Whenever a new message is received, a notification will be created.
When a notification arrives, there's an action link to copy it. If you click the notification itself, the browser will open and will google the message content, unless it was already a web link, in which case the browser will open it directly.

KNOWN PROBLEMS AND IMPORTANT NOTES:
- Although the app consumes little power, Android often tries to kill it on the background. Because of this, the app has defense mecanisms to wake up on its own and might never stop unless you exit it properly. If you want it to stop, click the menu option *Disconnect and close*.
- To prevent missing messages due to Android killing the app too frequently, it's advisable to add LANPUSH to your device's optimization whitelist.
- Android devices might not receive messages coming from broadcasts. When that's the case, you may have to add each Android device IP on the IP settings from the sender devices.
