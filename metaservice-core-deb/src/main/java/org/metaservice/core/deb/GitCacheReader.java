package org.metaservice.core.deb;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.metaservice.core.ProductionConfig;
import org.metaservice.core.deb.parser.PackagesParser;
import org.metaservice.core.deb.parser.ast.SuperNode;
import org.metaservice.core.deb.utils.GitUtil;
import org.metaservice.core.rdf.BufferedSparql;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.repository.RepositoryException;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.support.ParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GitCacheReader {
    private final static Logger LOGGER = LoggerFactory.getLogger(GitCacheReader.class);
    public static AtomicInteger i = new AtomicInteger(0);

    private final File workdir = new File("/opt/crawlertest/data_all_i386_amd64/");

    @NotNull
    private final Set<String> md5s = Collections.synchronizedSet(new HashSet<String>());

    @NotNull
    private final GitUtil gitUtil;
    @NotNull
    private final ArrayBlockingQueue<Task> queue;
    @NotNull
    ArrayList<Thread> threads = new ArrayList<Thread>();


    public static void main(String[] args) throws IOException, InterruptedException, RepositoryException {
        boolean  continueRun;
        if(args.length > 0 && "continue".equals(args[0])){
            continueRun = true;
        }  else{
            continueRun = false;
        }
        GitCacheReader reader = new GitCacheReader(continueRun);

        reader.run();
    }


    private final boolean continueRun;

    public GitCacheReader(boolean continueRun) throws RepositoryException, IOException, InterruptedException {
        this.continueRun = continueRun;
        queue = new ArrayBlockingQueue<Task>(3);
        gitUtil = new GitUtil(workdir);


    }

    private void run() throws IOException, InterruptedException, RepositoryException {
        for(int i = 0; i< 2;i++){
            Thread thread = new ParserThread();
            thread.start();
            threads.add(thread);
        }
        if(continueRun){
            LOGGER.info("Continuing...");
            gitUtil.checkOutPrev();
        }else{
            gitUtil.checkOutFirst(); //first should be an empty one

        }
        while (!gitUtil.isLast()){
            gitUtil.checkOutNext();
            parseChangedFilesIncremental(gitUtil.getCurrentRevision(),gitUtil.getCurrentMessage());
        }
        for(Thread thread : threads){
            thread.interrupt();
            thread.join();
        }
    }

    private void parseChangedFilesIncremental(final String currentRevision,final String time) throws RepositoryException, IOException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(3);

        for(final File f: gitUtil.getChangedFiles()){

            if("Packages".equals(f.getName())){
                es.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GitUtil.Line[] changes = gitUtil.getChangeList(f.getPath());
                            String[] packageAreas = extractFullPackages(changes);
                            String s =  StringUtils.join(packageAreas, "\n");
                            LOGGER.debug("Parsing: {}", s);
                            parseString(currentRevision, f.getAbsolutePath().replaceFirst(workdir.getAbsolutePath(), ""),s,"http://ftp.debian.org/debian/",time);
                        } catch (RepositoryException e) {
                            LOGGER.error("RepositoryException",e);
                        } catch (InterruptedException e) {
                            LOGGER.error("INterruptedException",e);
                        } catch (IOException e) {
                            LOGGER.error("IOException",e);
                        }
                    }
                });
            }
        }
        es.shutdown();
        es.awaitTermination(60, TimeUnit.MINUTES);
    }

    public static String[] extractFullPackages(@NotNull GitUtil.Line[] changes) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<GitUtil.Line> cleaned = new ArrayList<GitUtil.Line>();
        for(int i = 0; i < changes.length;i++){
            if(changes[i].changeType != GitUtil.Line.ChangeType.OLD){
                cleaned.add(changes[i]);
            }
        }
        changes = cleaned.toArray(new GitUtil.Line[cleaned.size()]);


        int packageStart = 0;
        for(int i = 0; i < changes.length;i++){
            if(changes[i].line.startsWith("Package: ") &&
                    (changes[i].changeType == GitUtil.Line.ChangeType.UNCHANGED ||changes[i].changeType == GitUtil.Line.ChangeType.NEW))
                packageStart = i;
            switch (changes[i].changeType){
                case UNCHANGED:
                    break;
                case NEW:
                    //from Packagestart to here set unchagne to new
                    for(int j = packageStart; j < i;j++){
                        if(changes[j].changeType == GitUtil.Line.ChangeType.UNCHANGED)
                            changes[j].changeType = GitUtil.Line.ChangeType.NEW;
                    }

                    while (i < changes.length){
                        if(changes[i].changeType == GitUtil.Line.ChangeType.UNCHANGED)
                            changes[i].changeType = GitUtil.Line.ChangeType.NEW;
                        if( i+ 1 == changes.length || changes[i+1].line.startsWith("Package: ")){
                            break;
                        }
                        i++;
                    }
            }
        }
        StringBuilder b = new StringBuilder();
        for(int i = 0; i < changes.length;i++){
            if(changes[i].changeType == GitUtil.Line.ChangeType.NEW){
                b.append(changes[i].line);
                b.append('\n');
                if(i+1 >= changes.length || changes[i+1].changeType != GitUtil.Line.ChangeType.NEW){
                    result.add(b.toString());
                    b = new StringBuilder();
                }
            }
        }
        return result.toArray(new String[result.size()]);
    }

    private void parseFile(String currentRevision,String time,@NotNull File f) throws RepositoryException, IOException, InterruptedException {
        FileInputStream inputStream = new FileInputStream(f);
        StringWriter writer = new StringWriter();

        IOUtils.copy(inputStream,writer,"utf-8");

        parseString(currentRevision, f.getAbsolutePath().replaceFirst(workdir.getAbsolutePath(),""),writer.getBuffer().toString(),"http://ftp.debian.org/debian/",time);

    }

    private static class Task{

        private Task(String revision,String source, String path, String content,String time) {
            this.path = path;
            this.source = source;
            this.revision = revision;
            this.time = time;
            this.content = content;
        }

        public String path;
        public String time;
        public String revision;
        public String content;
        public String source;
    }


    private void parseString(String currentRevision, String filename, String content,String source,String time) throws RepositoryException, InterruptedException {
     //   System.err.println("PUT: " + name);
      //  System.err.println("WAITING " + running.incrementAndGet());
        String md5 = DigestUtils.md5Hex(content);
        if(!md5s.contains(md5))
        {
            queue.put(new Task(currentRevision,source,filename, content,time));
            md5s.add(md5);
        }else{
            //these are usually Architecture: all snippets
            LOGGER.warn("Skipping {} {} already processed {}", currentRevision, filename, md5);
        }
    }

    public class ParserThread extends Thread{
        @Override
        public void run() {
            try {
                DebianPackageProvider provider  = new DebianPackageProvider(new BufferedSparql(new ProductionConfig()),null);
                try{

                    while(true){
                        PackagesParser parser =Parboiled.createParser(PackagesParser.class);
                        Task t = queue.take();
                        //      System.err.println("WAITING " + running.decrementAndGet());
                        BasicParseRunner runner = new BasicParseRunner(parser.List());
                        try{
                        ParsingResult<?> result = runner.run(t.content);
                        for (SuperNode node : ((SuperNode) result.valueStack.pop()).getChildren()) {
                            provider.putPackage((org.metaservice.core.deb.parser.ast.Package) node,t.source,t.time,t.path);
                        }
                        }catch (Exception e){
                            try {
                                String errorFilename = "error" + i.incrementAndGet();
                                FileWriter fileWriter = new FileWriter(errorFilename);
                                fileWriter.write(t.path +"\n");
                                fileWriter.write(e.toString() + "\n");
                                fileWriter.write(t.content);
                                fileWriter.close();
                                LOGGER.error("Parsing failed, dumping to file " + errorFilename,e);
                            } catch (IOException e1) {
                                LOGGER.error("Error dumping",e1);
                            }
                        }
                        LOGGER.info("done processing {} {}",t.revision, t.path);
                    }
                } catch (InterruptedException e) {
                    provider.flushModel();
                }
            } catch (RepositoryException | IOException | InterruptedException | MalformedQueryException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
