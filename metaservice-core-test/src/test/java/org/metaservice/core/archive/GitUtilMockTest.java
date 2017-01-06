/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.core.archive;

import org.junit.Before;
import org.junit.Test;
import org.metaservice.api.archive.ArchiveException;
import org.metaservice.core.utils.GitUtil;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GitUtilMockTest {

    GitUtil gitUtil;
    @Before
    public void setUp() throws GitUtil.GitException {
        gitUtil = new GitUtilMock(new File("metaservice-core-test/src/test/resources/fakegit"));
    }

    @Test
    public void testMessages() throws Exception {
        assertEquals(gitUtil.getCommitMessages()[0],"20131206T160423");
        assertEquals(gitUtil.getCommitMessages()[1],"20131207T095823");
        assertEquals(gitUtil.getCommitMessages()[2],"20131208T095620");
    }

    @Test
    public void testGetFileContent() throws Exception {
        assertEquals(gitUtil.getFileContent("0","someFile.txt"),"Test");
        assertEquals(gitUtil.getFileContent("2","someFile.txt"),"und zwei");
        try{
            gitUtil.getFileContent("3","someFile.txt");
            fail();
        }catch (GitUtil.GitException e){
        }
        try{
            gitUtil.getFileContent("2","someFile4.txt");
            fail();
        }catch (GitUtil.GitException e){
        }
    }

    @Test
    public void testGetChangedFiles() throws Exception {
        for(String message : gitUtil.getCommitMessages()){
            File[] result = gitUtil.getChangedFiles(gitUtil.findFirsRevisionWithMessage(message));
            System.err.println(Arrays.asList(result));
        }

    }
}