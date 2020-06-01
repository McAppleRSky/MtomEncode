import com.sun.tools.javac.util.StringUtils;

import org.apache.commons.lang.StringUtils;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleMailMtom {


    public byte[] mimeParser(InputStream isMtm) {
        String ct = "";
        ByteArrayOutputStream baos = null;
        try {
            MimeMultipart mp = new MimeMultipart(String.valueOf(new ByteArrayDataSource(isMtm,
                    ct)));
            int count = mp.getCount();
            baos = new ByteArrayOutputStream();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = mp.getBodyPart(i);
                if (!Part.ATTACHMENT
                        .equalsIgnoreCase(bodyPart.getDisposition())
                        && !StringUtils.isNotBlank(bodyPart.getFileName())
                ) {
                    continue; // dealing with attachments only
                }
                bodyPart.writeTo(baos);
            }

            byte[] attachment = baos.toByteArray();
//            FileUtils.writeByteArrayToFile(new File("E:/wss/attachment.zip"), attachment);
            return attachment;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception ex) {

                }
            }
        }
        return null;
    }

    public void Encode2File() throws IOException, MessagingException {
        String data = "", contentType = "";
        MimeMultipart mp = new MimeMultipart(String.valueOf(new ByteArrayDataSource(data, contentType)));
        int count = mp.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bp = mp.getBodyPart(i);
//            bp.saveFile(filepath + "_" + i);
        }
    }
}
