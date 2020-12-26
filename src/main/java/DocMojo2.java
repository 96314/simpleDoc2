
import com.github.apiggs.Apiggs;
import com.github.apiggs.Environment;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * generate rest doc with apiggs
 */
@Mojo(name = Environment.NAME)
public class DocMojo2 extends AbstractMojo {

        MavenProject project;

        @Parameter
        String id;
        @Parameter
        String title;
        @Parameter
        String description;
        @Parameter
        String out;
        @Parameter
        String production;
        //传字符串，使用逗号分隔
        String source;
        @Parameter
        String dependency;
        @Parameter
        String jar;
        @Parameter
        String ignore;
        @Parameter
        String version;
        @Parameter
        String css;

        public void execute() {
            if(getPluginContext().containsKey("project") && getPluginContext().get("project") instanceof MavenProject){
                project = (MavenProject) getPluginContext().get("project");
                build();
            }
        }

        private void build(){
            Environment env = new Environment();
            if (source != null) {
                for (String dir : source.split(",")) {
                    Path path = resolve(dir);
                    env.source(path);
                }
            } else {
                env.source(Paths.get(project.getBuild().getSourceDirectory()));
                if(project.getCollectedProjects()!=null){
                    for (MavenProject sub : project.getCollectedProjects()) {
                        env.source(Paths.get(sub.getBuild().getSourceDirectory()));
                    }
                }
            }
            if (dependency != null) {
                String[] dirs = dependency.split(",");
                for (String dir : dirs) {
                    Path path = resolve(dir);
                    env.dependency(path);
                }
            }else{
                if(project.getParent()!=null && project.getParent().getCollectedProjects()!=null){
                    for (MavenProject p : project.getParent().getCollectedProjects()) {
                        String path = p.getBuild().getSourceDirectory();
                        env.dependency(Paths.get(path));
                    }
                }
            }
            if (jar != null) {
                for (String dir : jar.split(",")) {
                    Path path = resolve(dir);
                    env.jar(path);
                }
            }
            if (id != null) {
                env.id(id);
            } else {
                env.id(project.getName());
            }
            if (production != null){
                env.production(Paths.get(production));
            }
            if (out != null) {
                Path path = resolve(out);
                env.out(path);
            } else {
                env.out(Paths.get(project.getBuild().getDirectory()));
            }
            if (title != null) {
                env.title(title);
            } else {
                env.title(project.getName());
            }
            if (description != null) {
                env.description(description);
            } else if (project.getDescription()!=null) {
                env.description(project.getDescription());
            }
            if (version != null){
                env.version(version);
            } else if (project.getVersion()!=null){
                env.version(project.getVersion());
            }
            if (ignore != null) {
                env.ignore(ignore.split(","));
            }
            if (css != null) {
                env.css(css);
            }

            new Apiggs(env).lookup().build();

        }

        private Path resolve(String dir){
            Path path = Paths.get(dir);
            if(path.isAbsolute()){
                return path;
            }else{
                return project.getBasedir().toPath().resolve(path);
            }
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getOut() {
            return out;
        }

        public void setOut(String out) {
            this.out = out;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDependency() {
            return dependency;
        }

        public void setDependency(String dependency) {
            this.dependency = dependency;
        }

        public String getJar() {
            return jar;
        }

        public void setJar(String jar) {
            this.jar = jar;
        }

        public String getIgnore() {
            return ignore;
        }

        public void setIgnore(String ignore) {
            this.ignore = ignore;
        }

        public void setProduction(String production) {
            this.production = production;
        }

        public String getProduction() {
            return production;
        }
    }

