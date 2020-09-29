package com.qcsoft.ppt2image;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.qcsoft.ppt2image.model.OpenOfficeClient;
import com.qcsoft.ppt2image.common.ResourceSuffix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.ConnectException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * PPT 转 PDF 服务
 *
 * @author DQC
 * @version 1.0
 * @date 2020/9/28
 */
public class PPT2PDFClient implements Runnable {

    private Logger logger = LoggerFactory.getLogger(PPT2PDFClient.class);

    private BlockingQueue<String> queue;

    private String sourcePath;

    private String sourceFileName;

    private String targetPath;

    private String targetFileName;

    public void run() {
        logger.info("PPT 2 PDF START!!!");
        OpenOfficeConnection connection = null;
        try {
            File sourceFile = new File(sourcePath + sourceFileName);
            if (!sourceFile.exists()) {
                logger.info(sourcePath + sourceFileName + " filepath is not found!!!");
                throw new NullPointerException("需要转换的文件不存在！");
            }
            // 输出文件目录
            File targetFile = new File(targetPath + targetFileName + "." + ResourceSuffix.PDF.getValue());
            if (!targetFile.getParentFile().exists()) {
                logger.info(targetPath + targetFileName + " targetpath is not found,create field!!!");
                targetFile.getParentFile().exists();
            }
            connection = OpenOfficeClient.getInstance();
            connection.connect();
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(sourceFile, targetFile);
            queue.put(targetPath + targetFile.getName());
            logger.info("ppt converter success,date:{}", new Date(System.currentTimeMillis()));
            logger.info("ppt converter success,filename:{}", targetPath + File.separator + targetFile.getName());
            logger.info("thread name [{}] ppt 2 pdf task success", Thread.currentThread().getName());
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    public PPT2PDFClient(BlockingQueue<String> queue, String sourcePath, String sourceFileName, String targetPath, String targetFileName) {
        this.queue = queue;
        this.sourcePath = sourcePath;
        this.sourceFileName = sourceFileName;
        this.targetPath = targetPath;
        this.targetFileName = targetFileName;
        logger.info("service ppt 2 pdf new create!!!");
    }
}
