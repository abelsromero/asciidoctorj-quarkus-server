package org.acme.resteasy;

import org.apache.commons.io.IOUtils;
import org.asciidoctor.*;
import org.asciidoctor.jruby.internal.JRubyAsciidoctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Path("/")
public class ExampleResource {

    static final Logger logger = LoggerFactory.getLogger(ExampleResource.class);

    @GET
    @Path("/convert")
    @Produces(MediaType.TEXT_HTML)
    public String convert() throws IOException {

        final JRubyAsciidoctor jRubyAsciidoctor = new JRubyAsciidoctor();

        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        AttributesBuilder attributes = AttributesBuilder.attributes()
                .linkCss(false)
                .dataUri(true)
                .tableOfContents(true)
                .tableOfContents(Placement.LEFT)
                .sourceHighlighter("rouge")
                .attribute("value", LocalDateTime.now());

        OptionsBuilder options = OptionsBuilder.options()
                .safe(SafeMode.UNSAFE)
                .headerFooter(true)
                .attributes(attributes);


        final String sourceContent = IOUtils.toString(file("sample.adoc"), StandardCharsets.UTF_8);
        logger.info("Converting source of size: " + sourceContent.length());

        return asciidoctor.convert(sourceContent, options);
    }

    public InputStream file(String filename) {
        return this.getClass().getClassLoader().getResourceAsStream(filename);
    }
}