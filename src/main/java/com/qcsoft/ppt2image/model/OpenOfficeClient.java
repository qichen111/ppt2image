package com.qcsoft.ppt2image.model;

import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * DQC
 */
public class OpenOfficeClient {

    private static Logger logger = LoggerFactory.getLogger(OpenOfficeClient.class);

    private static OpenOfficeConfig config = new OpenOfficeConfig();

    static {
        logger.info("open office client static method init!!!");
        config.setClientPath("C:\\Program Files\\LibreOffice\\program\\soffice.exe -headless -accept=\"socket,host=0.0.0.0,port=8100;urp;");
        config.setHost("127.0.0.1");
        config.setPort(8100);
    }

    private volatile static OpenOfficeConnection instance;


    public OpenOfficeClient() {
        logger.info("open office client construction method init!!!");
    }

    public static OpenOfficeConnection getInstance() {
        logger.info("get open office connection start!!!");
        if (instance == null) {
            synchronized (OpenOfficeClient.class) {
                if (instance == null) {
                    logger.info("open office connection is null,need to create!!!");
                    Process exec = null;
                    try {
                        exec = Runtime.getRuntime().exec(config.getClientPath());
                        instance = new SocketOpenOfficeConnection(config.getHost(), config.getPort());
                        logger.info("open office connection connection success!!!");
                    } catch (IOException e) {
                        logger.info("open office connection connection error!!!");
                        if (exec != null) {
                            exec.destroy();
                        } else {
                            throw new NullPointerException("exec commod error!!!");
                        }
                        if (instance != null) {
                            instance.disconnect();
                        }
                    }
                }
            }
        } else {
            logger.info("open office connection already exits,no need to create!!!");
        }
        logger.info("get open office connection end,object:[{}]!!!", instance);
        return instance;
    }

}
