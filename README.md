# AndroidArcoreFacesStreaming

From any Android phone ArCore compatible, using this app will send over TCP 5680 bytes messages:
The first 5616 bytes is a vertex buffer of 468 points mapping the user face in local head space (468 x 3 floats).
Following 64 bytes are the coefficient of a 4x4 Matrix (16floats) representing the transform of the Head bone in world space.

Built with Android Studio 4.2.1

Thread:
https://forums.unrealengine.com/t/face-capture-with-android-metahuman-download-links-free-open-source-demo/234927

![alt text](https://i.imgur.com/T9EV1fr.png)

![alt text](https://i.imgur.com/uC77IqQl.jpg)

