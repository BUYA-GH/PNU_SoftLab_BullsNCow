# PNU_SoftLab_BullsNCow
at PNU Computer Software Design & Lab in 2021
If this content needs to move in CONTRIBUTOR.md, it will do.

## 1. Now Working
- Formatting TCP/IP communication Packet

    All Packet should splited by ":"

    ```
    ANSWER:Jeon:325
    ```

    This mean Jeon's Answer is 325
    All of Server & Client should use this packet format.

    At *Sending* Operation
    ```java
    String outBuffer = "ANSWER:Jeon:325";
    out.writeUTF(outBuffer);
    ```

    And at *Receive* Operation
    ```java
    String inBuffer = in.readUTF();
    String [] set = inBuffer.split(":");
    ```

    - Change MapActivity's layout to TabLayout
    To make memo for Users's Bulls & Cow game, we think TabLayout will make user more comportable to play this game application.

## 2. Work Completed
- Make Socket Service in Client, Android Studio
- Show Pins in MapActivity
- Activate Attack chance when arrive at Pins