package com.example.isochannelproject;

import org.jpos.iso.*;
import org.jpos.util.*;
import org.jpos.iso.channel.*;
import org.jpos.iso.packager.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class Server implements ISORequestListener{
    org.slf4j.Logger logger1= LoggerFactory.getLogger(Server.class);

    public Server () {
        super();
    }
    public boolean process (ISOSource source, ISOMsg msg) {
        try {
            logger1.info("unsetting from server");
            msg.setMTI ("0110");
            msg.unset(18);
            logger1.info("unset 18 de");
            msg.unset(22);
            logger1.info("unset 22 de");
            msg.unset(25);
            logger1.info("unset 25");
            msg.unset(35);
            logger1.info("unset 35");
            msg.unset(37);
            logger1.info("unset 37");
            msg.unset(40);
            logger1.info("unset 40");
            msg.unset(42);
            msg.unset(52);
            msg.unset(61);
            msg.set (39, "00");
            source.send (msg);
        } catch (ISOException | IOException e) {

            e.printStackTrace();
        }
        return true;
    }
    public static void main (String[] args) throws Exception {
        Logger logger = new Logger ();
        logger.addListener (new SimpleLogListener (System.out));
        ServerChannel channel = new ASCIIChannel(new EuroPackager());
        ((LogSource)channel).setLogger (logger, "channel");
        ISOServer server = new ISOServer (8000, channel, null);
        server.setLogger (logger, "server");
        server.addISORequestListener (new Server ());
        new Thread (server).start ();
    }
}
