package bluelight;

import java.io.IOException;
import java.io.OutputStream;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;

public class BluetoothConnection {
    private OutputStream stream;
    private int state = 0;
    
    public BluetoothConnection(final Bluelight bluelight) {
        try {
            final DiscoveryAgent da = LocalDevice.getLocalDevice().getDiscoveryAgent();
            da.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {

                @Override
                public void deviceDiscovered(RemoteDevice rd, DeviceClass dc) {
                    try {
                        String name = rd.getFriendlyName(false);
                        System.out.println("Found: "+ name + " (" + rd.getBluetoothAddress() + ")");
                        if (name.equals("HC-05")) {
                            state = 1;
                            UUID[] uuidSet = new UUID[1];
                            uuidSet[0]=new UUID(0x1101);
                            da.searchServices(null, uuidSet, rd, this);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void servicesDiscovered(int number, ServiceRecord[] srs) {
                    System.out.println("Service discovered");
                    try {
                        stream = Connector.openOutputStream(srs[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
                        if (bluelight != null)
                           bluelight.enableButtons();
                        stream.write(0x00);
                        state = 2;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void serviceSearchCompleted(int i, int i1) {
                    if (state != 2) {
                        System.out.println("Could not connect to HC-05 device");
                    }
                }

                @Override
                public void inquiryCompleted(int i) {
                    if (state == 0) {
                        System.out.println("Could not find HC-05 device");
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private int parity(int val) {
        return (val & 0x0F) ^ ((val & 0xF0) >> 4);
    }
    
    public void setLights(int red, int green, int blue) throws IOException {
        byte[] bytes = new byte[6];
        bytes[0] = (byte) (0x00 | parity(red));
        bytes[1] = (byte) red;
        bytes[2] = (byte) (0x30 | parity(green));
        bytes[3] = (byte) green;
        bytes[4] = (byte) (0x50 | parity(blue));
        bytes[5] = (byte) blue;
        try {
            stream.write(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setLights(int red, int green, int blue, int pos, int strobe) throws IOException {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) (0x00 | parity(red));
        bytes[1] = (byte) red;
        bytes[2] = (byte) (0x30 | parity(green));
        bytes[3] = (byte) green;
        bytes[4] = (byte) (0x50 | parity(blue));
        bytes[5] = (byte) blue;
        bytes[6] = (byte) (0x90 | parity(strobe));
        bytes[7] = (byte) strobe;
        bytes[8] = (byte) (0x60 | parity(pos));
        bytes[9] = (byte) pos;
        try {
            stream.write(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



