package com.example.isochannelproject;


import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

@RestController
public class Client {
    @Autowired
    private Environment environment;

    @PostMapping("/iso")
    public  ResStatus clientMethod (@RequestBody ClientData clientData)throws Exception {
        ResStatus resStatus=new ResStatus();


//todo for track 2
        LocalDate currentDate = LocalDate.now();
        int lastDigit = currentDate.getYear() % 10;
        LocalDate jan1 = LocalDate.of(currentDate.getYear(), 1, 1);
        int daysElapsed = currentDate.getDayOfYear() - 1;
        int julianDate = daysElapsed + 1;
        LocalTime currentTime = LocalTime.now();
        int currentHour = currentTime.getHour();


        SimpleDateFormat sdf1 = new SimpleDateFormat("MMdd");
        String currentDate1 = sdf1.format(new Date());
//        DateFormatter dateFormatter=new DateFormatter();
        org.slf4j.Logger logger1= LoggerFactory.getLogger(Client.class);

        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String currentTime1 = sdf.format(new Date());
        Logger logger = new Logger();

//        java.io.File file = new java.io.File("D:\\jpos log file\\logfile.log");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate3 = dateFormat.format(new Date());

        String filePath = "D:\\log\\logfile-" + currentDate3 + ".log";
        File file = new File(filePath);

        try {
            // Create a FileOutputStream with append mode
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            // Pass the FileOutputStream to the PrintStream constructor
            PrintStream printStream = new PrintStream(fileOutputStream);
            logger.addListener(new SimpleLogListener(printStream));
            logger.addListener(new SimpleLogListener(System.out));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//todo card details
//

        ISOChannel channel = new ASCIIChannel ("localhost", 8000, new EuroPackager());
        ((LogSource)channel).setLogger (logger, "client-channel");
        channel.connect ();
        logger1.info("Sending request to the server");
        ISOMsg m=new ISOMsg();
        String propertyValue= environment.getProperty("pancardno");
        if(Objects.equals(clientData.getPan(), propertyValue)) {
            m.setMTI("0100");
            m.set(2, clientData.getPan());
            m.set(3, clientData.getProcessingCode());
            m.set(4, clientData.getAmount() + "00");
            m.set(7, ISODate.getDateTime(new Date()));//mmddhhms
            DistributedSequence distributedSequence = new DistributedSequence();
            String stan7 = String.valueOf(distributedSequence.getStan());
            m.set(11, stan7);
            m.set(12, currentTime1);
            m.set(13, currentDate1);
            m.set(18, "7011");//mcc merchant code
            m.set(19, "356");
            m.set(22, "051");// icc pin capability
            m.set(23, "001");
            m.set(25, "00");// transaction condition
            m.set(32, "123457");// institution code by npci
            m.set(35, "6070936047178733=23066209480000000000");
//        m.set(37,lastDigit+julianDate+currentHour+stan7 );//rrn
            m.set(37, clientData.getRrn());
            m.set(40, "620"); //service condition code:acceptance criteria for magnetic stripe
            m.set(41, "TEST1234");//a card acceptor terminal id
            m.set(42, "TEST12345678901"); //card acceptor operating the POs
            m.set(43, clientData.getCustomerDetails().getName() + clientData.getCustomerDetails().getAddress());
            //       m.set(43, "RUPAY TEST SIMULATOR MUMBAI MHIN");//
            m.set(48, "051005POS01054001M05800500999099016E22C0058D0CB1794");
            m.set(49, "356");
            m.set(52, "1234");// pin data
            m.set(55, "9F02060000000010009F03060000000000009F2608ED47C3FFC40F74FE820219009F2701809F10200FB501A0000000000000000000000000000000000000000000000000000000009F3303E0F0C89F1A020356950500000480005F2A0203569A032302089C01009F3704E685B4EA9F36020100");
            m.set(61, "421112321033000400063NPCIBandraMUMBAIMHIN");
            channel.send(m);
            logger1.info("received response from the server");
            channel.receive ();
            logger1.info("received");
            resStatus.setSuccessfullTransaction("0");
        }
        else
            resStatus.setFailedTransaction("-1");
        return resStatus;


    }
}













































