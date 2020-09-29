package com.qcsoft.ppt2image;

import com.qcsoft.ppt2image.common.ResourceSuffix;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * PPT转换图片服务
 *
 * @author DQC
 * @version 1.0
 * @date 2020/9/28
 */
public class PPT2PNGHandleBean {

    private static BlockingQueue<String> queue;

    private static ExecutorService threadPool;

    static {
        queue = new LinkedBlockingQueue<>(10);
        threadPool = Executors.newCachedThreadPool();
    }


    public static List<String> ppt2png(String pptSourcePath, String pptSourceName, String pptTargetPath, String pptTargetName, String imageTargetPath) {
        PPT2PDFClient pdfClient = new PPT2PDFClient(queue, pptSourcePath, pptSourceName, pptTargetPath, pptTargetName);
        PDF2PNGClient pngClient = new PDF2PNGClient(queue, imageTargetPath, ResourceSuffix.PNG);
        threadPool.execute(pdfClient);
        List<String> aaa = new LinkedList<>();
        try {
            CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(pngClient);
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String separator = File.separator;
        String sourcePath = "E:" + separator;
        for (int i = 0; i < 50; i++) {
            String sourceFileName = "1.ppt";
            String targetFileName = String.valueOf(System.currentTimeMillis());
            String targetPath = "E:" + separator + i + separator;
            List<String> strings = ppt2png(sourcePath, sourceFileName, targetPath, targetFileName, sourcePath + ("image" + i) + separator);
            System.out.println(strings);
        }

        for (int i = 50; i < 100; i++) {
            String sourcePath2 = "E:" + separator;
            String sourceFileName2 = "2.ppt";
            String targetFileName2 = String.valueOf(System.currentTimeMillis());
            String targetPath2 = "E:" + separator + i + separator;
            List<String> strings1 = ppt2png(sourcePath2, sourceFileName2, targetPath2, targetFileName2, sourcePath2 + ("image" + i) + separator);
            System.out.println(strings1);
        }

    }
}
