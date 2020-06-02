import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;

public class SimpleMailMtom {


//    public byte[] mimeParser(InputStream inputStreamMtm, String mtmType) {
    public byte[] mimeParser(String stringMtm, String mtmType) {
//        String mtmType = "";
        ByteArrayOutputStream byteArrayOutputStream = null;
        MimeMultipart mimeMultipart ;
        ByteArrayDataSource byteArrayDataSource;
        try {
            byteArrayDataSource = new ByteArrayDataSource(stringMtm, mtmType);
            mimeMultipart = new MimeMultipart(byteArrayDataSource);
            int count = mimeMultipart.getCount();
            byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (!Part.ATTACHMENT
                        .equalsIgnoreCase(bodyPart.getDisposition())
                        && !StringUtils.isNotBlank(bodyPart.getFileName())
                ) {
                    continue; // dealing with attachments only
                }
                bodyPart.writeTo(byteArrayOutputStream);
            }

            byte[] attachment = byteArrayOutputStream.toByteArray();
            FileUtils.writeByteArrayToFile(new File("./attachment.zip"), attachment);
            return attachment;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception ex) {

                }
            }
        }
        return null;
    }

    public void Encode2File() throws IOException, MessagingException {
        String data = "", contentType = "";
        MimeMultipart mimeMultipart = new MimeMultipart(String.valueOf(new ByteArrayDataSource(data, contentType)));
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
//            bodyPart.saveFile(filepath + "_" + i);

            InputStream inputStream = bodyPart.getInputStream();
            File file = new File("/tmp/" + bodyPart.getFileName());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buf = new byte[4096];
            int bytesRead;
            while((bytesRead = inputStream.read(buf))!=-1) {
                fileOutputStream.write(buf, 0, bytesRead);
            }
            fileOutputStream.close();
        }
    }
}
