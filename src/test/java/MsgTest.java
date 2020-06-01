import org.apache.cxf.attachment.AttachmentDeserializer;
import org.apache.cxf.attachment.DelegatingInputStream;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.message.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//import java.io.InputStream;

//import javax.mail.Message;

public class MsgTest {
    private MessageImpl msg;

    @Before
    public void setUp() throws Exception {
        msg = new MessageImpl();
        Exchange exchange = new ExchangeImpl();
        msg.setExchange(exchange);
    }

    @Test
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
