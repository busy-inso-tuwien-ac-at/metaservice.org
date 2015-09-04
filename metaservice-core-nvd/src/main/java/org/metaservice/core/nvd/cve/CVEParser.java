package org.metaservice.core.nvd.cve;

import com.google.common.io.CharSource;
import org.apache.commons.io.IOUtils;
import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;
import org.metaservice.nist.cve.jaxb.Nvd;
import org.metaservice.nist.cve.jaxb.VulnerabilityType;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by ilo on 25.02.14.
 */
public class CVEParser implements Parser<VulnerabilityType> {
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Inject
    public CVEParser(){

    }
    @Override
    public List<VulnerabilityType> parse(final Reader source, ArchiveAddress archiveParameters) {
        final List<VulnerabilityType> result = new ArrayList<>();
        List<Future<List<VulnerabilityType>>> futures = new ArrayList<>();

        try {
            final String s =IOUtils.toString(source);
            final JAXBContext jaxbContext = JAXBContext.newInstance(Nvd.class);

            final CharSource header = CharSource.wrap(new CharSequenceSegment(s,0,s.indexOf("<entry")-1));
            final CharSource end = CharSource.wrap(s.substring(s.lastIndexOf("</entry>")+9));

            int i = 0;
//
            int index = 0;
            while(index < s.length()){
                final int start = s.indexOf("<entry ",index);
                if(start == -1){
                    break;
                }
                index = start;
                int e = start;
                for(int j = 0 ; j  < 500;j++) {
                    e = s.indexOf("</entry>",index);
                    if (e == -1) {
                        e = s.lastIndexOf("</entry>")+8;
                        break;
                    }
                    e+=8;
                    index  = e;
                }
                final int finalE = e;
                Future<List<VulnerabilityType>> future =  executorService.submit(new Callable<List<VulnerabilityType>>() {
                    @Override
                    public List<VulnerabilityType> call() {
                        Unmarshaller unmarshaller = null;
                        try {
                            unmarshaller = jaxbContext.createUnmarshaller();
                            Nvd nvd = (Nvd) unmarshaller.unmarshal(CharSource.concat(header,CharSource.wrap(new CharSequenceSegment(s,start, finalE)),end).openStream());
                            return nvd.getEntries();
                        } catch (JAXBException | IOException e1) {
                            throw new RuntimeException(e1);
                        }
                    }
                });
                futures.add(future);

             //   System.err.println(new CharSequenceSegment(s,start,e).toString().substring(0,40));
/*
                if(i++ % 100 == 0)
                    System.err.println(i);*/
            }

        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        for(Future<List<VulnerabilityType>> future : futures){
            try {
                result.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
