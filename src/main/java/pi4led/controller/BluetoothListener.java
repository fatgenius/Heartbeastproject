package pi4led.controller;


import com.pi4j.io.serial.*;
import java.io.IOException;


public class BluetoothListener
{
    // instance variables - replace the example below with your own
    private Serial serial;
    private Callback callback;

    /**
     * Constructor for objects of class BluetoothListener
     */
    public BluetoothListener()
    {
        serial = SerialFactory.createInstance();
        serial.addListener(new SerialDataEventListener()
        {
            @Override
            public void dataReceived(SerialDataEvent event)
            {




                /*
                val:
                string "start/pause";
                string "




                 */
                try
                {
                    System.out.println(event.getAsciiString());
                    if (callback != null)
                    {
                        callback.onData(event.getAsciiString());
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    /**
     *
     */
    public boolean start()
    {
        try
        {
            SerialConfig config = new SerialConfig();
            config.device("/dev/rfcomm0")
                    .baud(Baud._38400)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);

            serial.open(config);
            return true;
        }
        catch (IOException e)
        {
            System.err.println("====> SERIAL SETUP FAILED: " + e.getMessage());
            return false;
        }
    }

    public void stop()
    {
        if (serial != null){
            try {
                serial.close();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public static interface Callback{
        void onData(String data);
    }
}