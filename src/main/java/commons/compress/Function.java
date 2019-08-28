package commons.compress;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author tanghf
 * @className commons.compress.Function.java
 * @createTime 2019/8/27 15:47
 */
public class Function {

    public static void main(String[] args) throws IOException {
//        zip();
        unZip();
    }

    public static void zip() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("compress");
        FileInputStream InputStream = new FileInputStream("d://logFile.txt");
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream("d://logFile.zip"));
        zipOutputStream.putNextEntry(entry);

        int i = 0;
        while ((i = InputStream.read()) > 0){
            zipOutputStream.write(i);
        }

        zipOutputStream.closeEntry();
        zipOutputStream.close();
        InputStream.close();
    }

    public static void unZip() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("compress");
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new FileInputStream("d://logFile.zip"));
        zipArchiveInputStream.canReadEntryData(entry);
        FileOutputStream fileOutputStream = new FileOutputStream("d://logFile1.txt");

        int i = 0;
        while ((i = zipArchiveInputStream.read()) > 0){
            fileOutputStream.write(i);
        }

        zipArchiveInputStream.close();
        fileOutputStream.close();
    }

}
