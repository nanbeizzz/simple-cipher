package cipher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

public class FileWrite {
	
	public static Log logger = LogFactory.getLog(FileWrite.class);

    /**
     * 创建文件
     * @param fileLocation  文件绝对路径
     * @param fileContent   文件内容
     * @return  是否创建成功，成功则返回true
     */
	public static boolean createFile(String fileLocation, String fileContent){
		Boolean bool = false;
        File file = new File(fileLocation);
        try {
        	File srcparent = new File(file.getParent());
        	if(!srcparent.exists()){
				srcparent.mkdirs();
        	}
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                bool = file.createNewFile();
                logger.info("成功创建文件 "+fileLocation);
                //创建文件成功后，写入内容到文件里
                writeFileContent(fileLocation, fileContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bool;
	}

	/**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
	 public static boolean writeFileContent(String filepath, String newstr) throws IOException {
	        Boolean bool = false;
	        String temp  = "";
	        
	        FileInputStream fis = null;
	        InputStreamReader isr = null;
	        BufferedReader br = null;
	        FileOutputStream fos  = null;
	        PrintWriter pw = null;
	        try {
	            File file = new File(filepath);//文件路径(包括文件名称)
	            //将文件读入输入流
	            fis = new FileInputStream(file);
	            isr = new InputStreamReader(fis);
	            br = new BufferedReader(isr);
	            StringBuffer buffer = new StringBuffer();
	            buffer.append(newstr);
	            
	            fos = new FileOutputStream(file);
	            pw = new PrintWriter(fos);
	            pw.write(buffer.toString().toCharArray());
	            pw.flush();
	            bool = true;
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }finally {
	            //不要忘记关闭
	            if (pw != null) {
	                pw.close();
	            }
	            if (fos != null) {
	                fos.close();
	            }
	            if (br != null) {
	                br.close();
	            }
	            if (isr != null) {
	                isr.close();
	            }
	            if (fis != null) {
	                fis.close();
	            }
	        }
	        return bool;
	    }
	 
	 
	 /**
	     * 删除文件
	     * @param fileLocation 文件名称
	     * @return
	     */
	    public static boolean delFile(String fileLocation){
	        Boolean bool = false;
	        File file  = new File(fileLocation);
	        try {
	            if(file.exists()){
	                file.delete();
	                bool = true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return bool;
	    }
	    
}
