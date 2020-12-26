package core.handler;

import core.Environment;
import core.schema.Tree;
import core.util.loging.Logger;
import core.util.loging.LoggerFactory;
import org.asciidoctor.*;
import org.asciidoctor.jruby.AsciiDocDirectoryWalker;

import java.util.Objects;

/**
 * Asciidoctorj文档转换工具
 */
public class HtmlTreeHandler implements TreeHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(Tree tree, Environment env) {
        AttributesBuilder attributes = AttributesBuilder.attributes();
        attributes.sectionNumbers(true);
        attributes.noFooter(true);
        if(Objects.nonNull(env.getCss())){
            attributes.linkCss(true);
            attributes.styleSheetName(env.getCss());
        }
        Options options = OptionsBuilder.options()
                .mkDirs(true)
                .toDir(env.getOutPath().toFile())
                .safe(SafeMode.UNSAFE)
                .attributes(attributes)
                .get();
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.convertDirectory(new AsciiDocDirectoryWalker(env.getOutPath().toString()),options);
        log.info("Render {}",env.getOutPath());
    }

}
