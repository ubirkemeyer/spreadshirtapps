package com.socialnetworkshirts.twittershirts.renderer;

import com.socialnetworkshirts.twittershirts.renderer.model.Svg;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.output.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author mbs
 */
public class PNGRenderer {
    private JAXBContext jaxbContext = null;

    public PNGRenderer() {
        try {
            jaxbContext = JAXBContext.newInstance("com.socialnetworkshirts.twittershirts.renderer.model");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void renderPNG(Svg svg, OutputStream out)
            throws TranscoderException, JAXBException, IOException {
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bis = null;
        try {
            PNGTranscoder t = new PNGTranscoder();
            bos = new ByteArrayOutputStream();
            jaxbContext.createMarshaller().marshal(svg, bos);
            bis = new ByteArrayInputStream(bos.toByteArray());
            TranscoderInput input = new TranscoderInput(bis);
            TranscoderOutput output = new TranscoderOutput(out);
            t.transcode(input, output);
        } finally {
            if (bos != null)
                bos.close();
            bos = null;
            if (bis != null)
                bis.close();
            bis = null;
        }
    }
}
