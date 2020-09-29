package com.qcsoft.ppt2image;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import com.qcsoft.ppt2image.common.ResourceSuffix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

/**
 * PDF 转 PNG 服务
 *
 * @author DQC
 * @version 1.0
 * @date 2020/9/28
 */
public class PDF2PNGClient implements Supplier<List<String>> {

    private final Logger logger = LoggerFactory.getLogger(PDF2PNGClient.class);

    private BlockingQueue<String> queue;

    private String targetPath;

    private ResourceSuffix suffix;

    private List<String> images = null;


    public PDF2PNGClient(BlockingQueue<String> queue, String targetPath, ResourceSuffix suffix) {
        this.queue = queue;
        this.targetPath = targetPath;
        this.suffix = suffix;
        logger.info("service pdf 2 png new create!!!");
    }

    public List<String> get() {
        try {
            logger.info("pdf 2 png start!!!");
            String take = queue.take();
            logger.info("get task:" + take);
            File file = new File(take);
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            images = new LinkedList<String>();
            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 144); // Windows native DPI
                String imagePath = targetPath + i + "-" + UUID.randomUUID().toString() + "." + suffix.getValue();
                ImageIO.write(image, suffix.getValue(), new File(imagePath));
                images.add(imagePath);
            }

            logger.info("new image files:{}", images);
            logger.info("thread name [{}] pdf 2 png task success", Thread.currentThread().getName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return images;
    }
}