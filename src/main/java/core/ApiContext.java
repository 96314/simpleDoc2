package core;

import core.handler.TreeHandler;
import core.schema.Bucket;
import core.schema.Tree;
import core.util.loging.Logger;
import core.util.loging.LoggerFactory;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.utils.SourceRoot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 工具入口类、上下文
 */
public class ApiContext {

    private static final ThreadLocal<ApiContext> context = new ThreadLocal<>();

    public static ApiContext getContext(){
        return context.get();
    }

    Logger log = LoggerFactory.getLogger(this.getClass());

    Environment env;
    Tree tree;

    public ApiContext() {
        this(new Environment());
    }

    public ApiContext(Environment env) {
        this.env = env;
        this.tree = new Tree();
        this.tree.setId(env.getId());
        this.tree.setName(env.getTitle());
        this.tree.setDescription(env.getDescription());
        this.tree.setVersion(env.getVersion());
        this.tree.setBucket(new Bucket(env.getId()));

        context.set(this);
    }

    public ApiContext(String root) {
        this(new Environment().source(Paths.get(root)));
    }

    /**
     * 搜寻给定代码及依赖环境
     * 找到Endpoints，构建Tree
     *
     * @return
     */
    public ApiContext lookup() {

        ParserConfiguration configuration = env.buildParserConfiguration();
        for (Path source : env.getSources()) {
            log.info("Parsing source : {}", source);
            SourceRoot root = new SourceRoot(source, configuration);
            root.tryToParseParallelized().forEach(result -> result.ifSuccessful(cu -> cu.accept(env.visitor(), this.getTree())));
        }

        Integer totalNodes = tree.getBucket().getGroups().stream()
                .map(g -> g.getNodes().size())
                .reduce(0, (sum, i) -> sum += i);
        log.info("\r\nFound {} Controllers, {} Endpoints", tree.getBucket().getGroups().size(), totalNodes);

        return this;
    }

    /**
     * 执行默认的构建任务
     */
    public void build() {
        env.pipeline().forEach(this::build);
    }

    public void build(TreeHandler... handlers) {
        Arrays.stream(handlers).forEach(this::build);
    }

    public void build(TreeHandler handler) {
        handler.handle(tree, env);
    }

    public Tree getTree() {
        return tree;
    }

    public Environment getEnv() {
        return env;
    }

}
