import org.apache.cxf.attachment.AttachmentDeserializer;
import org.apache.cxf.attachment.DelegatingInputStream;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.message.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

//import java.io.InputStream;

//import javax.mail.Message;

public class MsgTest {
    private String mtomString = "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";

    @Test
    public void testDecodeMtom2Zip() throws IOException {
        FileInputStream fileInputStream = null;

        File file = new File("./content.zip");
        assertTrue("File already exists.", file.createNewFile());
        byte[] data =  Base64.getDecoder().decode(mtomString);
        assertFalse(String.valueOf(data.length), data.length==0);
        assertTrue(String.valueOf(data.length), data.length==6729);

        fileInputStream = new FileInputStream(file);
        fileInputStream.read(data);
    }

    @Test
    @Ignore
    public void testStrMtom() throws IOException {
        assertFalse(String.valueOf(mtomString.length()), mtomString.length()==0);
        assertTrue(String.valueOf(mtomString.length()), mtomString.length()==8972);

        SimpleMailMtom simpleMailMtom = new SimpleMailMtom();
        //InputStream inputStream = null;
        try {
            //inputStream = new BufferedInputStream(new FileInputStream(new File("content")));
            //assertNotNull(simpleMailMtom.mimeParser(inputStream, "text/plain"));
            assertNotNull(simpleMailMtom.mimeParser(mtomString, "text/plain"));
        }
        finally {
            //if (inputStream != null) {inputStream.close();}
        }

    }

    private MessageImpl msg;

    @Before
    @Ignore
    public void setUp() throws Exception {
        msg = new MessageImpl();
        Exchange exchange = new ExchangeImpl();
        msg.setExchange(exchange);
    }

    @Test
    @Ignore
    public void testDeserializerMtom() throws Exception {
        InputStream is = getClass().getResourceAsStream("mimedata");
        String ct = "multipart/related; type=\"application/xop+xml\"; "
                + "start=\"<soap.xml@xfire.codehaus.org>\"; "
                + "start-info=\"text/xml; charset=utf-8\"; "
                + "boundary=\"----=_Part_4_701508.1145579811786\"";

        msg.put(Message.CONTENT_TYPE, ct);
        msg.setContent(InputStream.class, is);

        AttachmentDeserializer deserializer = new AttachmentDeserializer(msg);
        deserializer.initializeAttachments();

        InputStream attBody = msg.getContent(InputStream.class);
        assertTrue(attBody != is);
        assertTrue(attBody instanceof DelegatingInputStream);

        Collection<Attachment> atts = msg.getAttachments();
        assertNotNull(atts);

        Iterator<Attachment> itr = atts.iterator();
        assertTrue(itr.hasNext());

        Attachment attachment = itr.next();
        assertNotNull(attachment);

//        InputStream attIs = attachment.getDataHandler().getInputStream();

        // check the cached output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(attBody, out);
        assertTrue(out.toString().startsWith("<env:Envelope"));

        // try streaming a character off the wire
//        assertTrue(attIs.read() == '/');
//        assertTrue(attIs.read() == '9');
    }
}
